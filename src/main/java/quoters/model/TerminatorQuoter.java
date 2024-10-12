package quoters.model;

import jakarta.annotation.PostConstruct;
import quoters.annotations.deprecatedClass.DeprecatedClass;
import quoters.annotations.injectRandomInt.InjectRandomInt;
import quoters.annotations.postProxy.PostProxy;
import quoters.annotations.profiling.Profiling;

@Profiling
//@DeprecatedClass(newImpl = T1000.class) // вкл\выкл, но это плохая практика
public class TerminatorQuoter implements Quoter {

    @InjectRandomInt(min = 2, max = 7)
    private int repeat;
    private String message;

    // двухфазовый констуктор
    @PostConstruct
    public void init() {
        System.out.println("Phase 2");
        System.out.println("repeat = " + repeat);
    }

    /*
    сначала объект создается джавой, спринг инициализирует этот процесс - сканирует контекст,
    создает бин дифинишены, по ним создает синглтон, при помощи рефлекшн запустил конструктор,
    конструктор отработал, объект создался, и потом спринг может этот объект настраивать.
    соответственно, если мы в конструкторе пытаемся обратиться к каким-то вещам, которые должен
    настроить спринг, то получаем нули или null pointer exception, так как их еще нет, по этому нужен @PostConstruct
     */

    public TerminatorQuoter() {
        System.out.println("repeat at constructor: " + repeat);
        System.out.println("Phase 1");
    }
    // PostProcessBeforeInitialization -> PostConstruct -> PostProcessAfterInitialization
    // PostConstruct работает до того, как настроились все прокси, включая те прокси,
    // что отвечают за транзацкии
    @Override
    @PostProxy
    // этот метод не вызовется при построении бина,
    // так как на этапе PostConstruct никаких прокси нет
    public void sayQuote() {
        System.out.println("Phase 3");
        for (int i = 0; i < repeat; i++) {
            System.out.println("message = " + message);
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
