package quoters.annotations.profiling;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// профилирование - сколько времени работает метод
/*
 добавить логику в существующий класс - на лету сгенерировать новый класс,
 объект которого заменяет оригинальный объект (spring aop, транзакции, бенчмарк и тд) - работает через прокси
 это делает BPP.
 два подхода работы dynamic proxy:
 1. имплементировать те же самые интерфейсы, которые есть у оригинального класса
 2. наследовать от оригинального класса, переопределяя методы и добавляя новую логику
*/
@Retention(RetentionPolicy.RUNTIME)
public @interface Profiling {
}
