package screensaver;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import screensaver.model.ColorFrame;

@ComponentScan(basePackages = "screensaver")
public class Main {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        while (true) {
            context.getBean("frame", ColorFrame.class).showOnRandomPlace();
            context.getBean("periodicalFrame", ColorFrame.class).showOnRandomPlace();

            Thread.sleep(1000);
        }
    }
}
