package quoters.annotations.profiling;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/*
По сути это свой Аспект.
Обрабатывает аннотацию Profiling на этапе создания бина, которую можно поставить над любым бином,
и каждый раз когда она будет включена, будет выполнена логика,
которую описали внутри метода postProcessAfterInitialization
в методе invoke класса InvocationHandler (благодаря dynamic proxy)
 */
public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {


    private Map<String, Class<?>> map = new HashMap<>();
    private ProfilingController controller = new ProfilingController();

    public ProfilingHandlerBeanPostProcessor() throws Exception {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        platformMBeanServer.registerMBean(controller, new ObjectName("profiling", "name", "controller"));
    }

    @Override
    @Nullable
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Profiling.class)) {
            map.put(beanName, beanClass);
        }
        return bean;
    }

    // тут происходит вся магия - ПРОКСИ
    // так как имя бина всегда сохраняется (оригинальный или прокси-объект), поэтому на этапе postProcessBeforeInitialization
    // запоминать оригинальные классы тех бинов, для которых нужно что-то сделать, то есть класть в map, а уже на этом этапе с ними что-то делать
    @Override
    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = map.get(beanName);
        if (beanClass != null) {
            // любой класс знает какой класслоадер его загрузил
            // getInterfaces - возвращает только те интерфейсы, которые унаследовал класс, а родителей не возвращает
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
// InvocationHandler - это объект, который инкапсулирует логику, которая будет в каждом методе класса,
// который сгенерируется на лету, который имплементирует интерфейсы оригинального класса
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (controller.isEnabled()) {
                        System.out.println("START PROFILING");
                        long before = System.nanoTime();
                        Object retVal = method.invoke(bean, args);
                        long after = System.nanoTime();
                        System.out.println(after - before);
                        System.out.println("PROFILING DONE");
                        return retVal;
                    } else {
                        return method.invoke(bean, args); // возвращаем оригинальный объект с оригинальными аргументами
                    }
                }
            }); // создает объект из нового класса, который сам же и сгенерирует на лету
        }
        return bean;
    }
}
