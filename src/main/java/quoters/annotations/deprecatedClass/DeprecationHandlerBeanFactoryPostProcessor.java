package quoters.annotations.deprecatedClass;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/*
до того, как BeanFactory начнет создавать объекты,
BeanFactoryPostProcessor поменяет все классы в
BeanDefinition с Deprecated на новую имплементацию.
это плохая практика
 */
public class DeprecationHandlerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    // листенер в контексте работает на этапе, когда все создано
    // а этот листенер работает на этапе, когда кроме BeanDefinition ничего нет, то есть когда не созданы бины
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Class<?> beanClass = Class.forName(beanClassName);
                DeprecatedClass annotation = beanClass.getAnnotation(DeprecatedClass.class);
                if (annotation != null) {
                    beanDefinition.setBeanClassName(annotation.newImpl().getName());
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
