package quoters.annotations.postProxy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// нужно, чтобы те методы, которые аннотированные этой аннотацией запускались сами в тот момент ,
// когда уже все абсолютно настроено и все прокси тоже сгенерировались. и это может сделать ContextListener
@Retention(RetentionPolicy.RUNTIME)
public @interface PostProxy {
}
