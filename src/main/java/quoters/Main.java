package quoters;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import quoters.model.Quoter;

public class Main {
    // спарсировался xml, создались BeanDefinition'ы, пришли BeanFactoryPostProcessor, выполнили свою логику
    // после этого создались BeanPostProcessor'ы и пошел процесс создания объектов, которые помещаются в IoC-контейнер
    public static void main(String[] args) {
        /*
         xml-контекст
         BeanDefinitionReader парсит xml-контекст, кладет информацию о бинах в BeanDefinition,
         после этого BeanFactory создает те бины, которые имплементируют интерфейс BeanPostProcessor,
         затем создает бины по BeanDefinition,
         которые настраиваются с помощью BeanPostProcessor'ов,
         после BeanPostProcessor'ов вызывается init-метод бинов,
         после init-методов еще раз проходит по BeanPostProcessor,
         потом BeanFactory складывает полностью настроенные объекты в IoC-контейнер (это хеш-мапа)

-------------------------------------------------------

         JavaConfig - это джава-файл и на него при помощи cglib накручивается прокси
         и, каждый раз когда нужен бин, происходит делегация в метод, который прописали в JavaConfig

         AnnotatedBeanDefinitionReader - это класс, который парсирует AnnotationConfigApplicationContext,
         он не является BeanDefinitionReader, он является частью ApplicationContext-а и регистрирует все JavaConfig'и
         Он находится внутри AnnotationConfigApplicationContext

         ConfigurationClassPostProcessor (особый BeanFactoryPostProcessor), который в отличие от обычных
         BeanFactoryPostProcessor-ов, которые меняют уже существующие BeanDefinition-ы, он добавляет другие
         BeanDefinition-ы.
         AnnotationConfigApplicationContext регистрирует ConfigurationClassPostProcessor как BeanFactoryPostProcessor
         Он создает BeanDefinition-ы по @Bean.

         контекст делает рефреш, когда перестают добавлять в него бины
         (бутстрап - бины создаются в основном на этапе поднятия контекста)
-------------------------------------------------------
         если бин является синглтоном, то он по-умолчанию сразу создается и складывается в контейнер
         а prototype бины создаются, когда нам нужны.
         destroy-метод будет работать для синглтона, а для прототайпа не будет, так как, когда контекст закрывается
         спринг проходит по всем бинам, находит их дестрой методы и запускает, а прототайп он нигде не хранит и
         соответственно для прототайп дестрой метод никогда не работает.

         BeanPostProcessor реализует паттерн чейн оф респонсобилити

         ClassPathBeanDefinitionScanner сканирует контекст и ищет все бины, которые аннотированы аннотацией @Component (или производной)
         подгружает дополнительные ресурсы, создает дополнительные BeanDefinition'ы
         */
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:context.xml"); // считывает BeanDefinitionReader
        context.getBean(Quoter.class).sayQuote();
    }
}
