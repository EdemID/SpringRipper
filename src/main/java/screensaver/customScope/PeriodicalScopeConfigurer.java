package screensaver.customScope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalTime.now;

// зарегистрирован как часть спринга
// контексту нужно сказать, что этот бин отвечает за стринг "periodical" в аннотации Scope,
// до создания контекста, то есть на этапе BeanFactoryPostProcessor
public class PeriodicalScopeConfigurer implements Scope {

    private Map<String, Map.Entry<Long, Object>> map = new HashMap<>();

    // каждый раз когда спрингу нужно будет знать как относиться к скоупу, он будет делегировать в метод get
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        // смотрим в паре сколько времени этот бин уже существует. если он создан давно,
        // то заменяем его на новый объект и возвращаем его тому, кто попросил
        if (map.containsKey(name)) {
            Map.Entry<Long, Object> pair = map.get(name);
            long secondsSingleLastRequest = System.currentTimeMillis() - pair.getKey();

            if (secondsSingleLastRequest > 5000) {
                map.put(name, new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), objectFactory.getObject()));
            }
        } else {
            map.put(name, new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), objectFactory.getObject()));
        }
        return map.get(name).getValue();
    }

    @Override
    public Object remove(String name) {
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
