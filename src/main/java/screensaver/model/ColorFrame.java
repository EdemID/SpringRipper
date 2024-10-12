package screensaver.model;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

// какая разница между стринг, спринг и свинг?
// есть тимлиды? я майки не кину, честно
// фрейм должен быть один, значит это синглтон (т.к. каждый раз мы лукапим его, должны получать один и тот же объект),
// а цвет должен меняться
// как обновляются прототайпы в синглетоне?
/*
1. внедрять контекст - худшее решение, так как будет очень большой каплинг
2. ScopedProxyMode.TARGET_CLASS - каждый раз к обращению этого бина, каждый раз получаем новый бин (в зависимости от ситуации)
3. лучшее решение - лукап-метод (@Lookup)
 */
@Component
public abstract class ColorFrame extends JFrame {

    public ColorFrame() {
        setSize(200, 200);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void showOnRandomPlace() {
        Random random = new Random();
        setLocation(random.nextInt(1200), random.nextInt(700));
        getContentPane().setBackground(getColor()); // не инжектим в этот класс объект Color
        repaint();
    }

    // когда вызывается метод getColor(), то получаем новый объект
    protected abstract Color getColor();
    // если есть потребность обратиться к контексту в методе,
    // то не нужно этот метод описывать.
    // потому что не надо, чтобы в этом классе держать контекст
    // описываем его там, где описываем бин


//    @Lookup // используется для внедрения прототипных бинов в синглтон бины
    // для того, чтобы использовать этот бин, надо удалить прототайп бин в Config
//    protected Color getColor() {
//        Random random = new Random();
//        return new Color(
//                random.nextInt(255),
//                random.nextInt(255),
//                random.nextInt(255)
//        );
//    };
}
