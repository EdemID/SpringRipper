package screensaver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import screensaver.model.ColorFrame;

import java.awt.*;
import java.util.Random;

@Configuration
public class Config {

    @Bean
    @Scope(value = "prototype") // прототайп всегда по лукапу дает новый бин
    // если бы использовали @Lookup, то этот бин не создавали бы
    public Color color() {
        Random random = new Random();
        return new Color(
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255)
        );
    }

    @Bean
    public ColorFrame frame() {
        return new ColorFrame() {
            @Override
            protected Color getColor() {
                return color(); // это не вызов метода color, это обращение к бину, который color
                // (то есть, если он prototype, это всегда будет возвращать новый бин,
                // а если это синглетон, то он будет возвращать старый)
            }
        };
    }

    @Bean
    @Scope(value = "periodical")
    public Color periodicalColor() {
        Random random = new Random();
        return new Color(
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255)
        );
    }

    @Bean
    public ColorFrame periodicalFrame() {
        return new ColorFrame() {
            @Override
            protected Color getColor() {
                return periodicalColor();
            }
        };
    }
}
