package quoters.annotations.injectRandomInt;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Random;

// Класс отвечает за обработку всех бинов, классы которых имеют эту аннотацию хотя бы в каком-то поле
public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {

    /*
    PostContract всегда работает на оригинальный объект до того, как все прокси накрутились
     */
    @Override
    @Nullable // на этом этаме BPP рассчитывает, что bean.getClass() вернет оригинальный класс,
    // в котором есть оригинальная метадата, в котором все поля аннотиранны теми аннотациями, потому что у класса, который сгенерируется на лету,
    // не имеет оригинальной метадаты и аннотаций
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
            if (annotation != null) {
                int min = annotation.min();
                int max = annotation.max();
                Random random = new Random();
                int i = min + random.nextInt(max - min);
                field.setAccessible(true); // позволить редактировать в рефлексии
                ReflectionUtils.setField(field, bean, i); // для какого филда задается значение, какого объекта филд и значение
            }
        }

        return bean;
    }

    /*
     те BPP, которые что-то в классе меняют, они должны делать не на этапе
     postProcessBeforeInitialization, а на этапе postProcessAfterInitialization
     PostContract всегда работает на оригинальный метод, то есть до того, как все прокси накрутились на него
     */
    @Override
    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
