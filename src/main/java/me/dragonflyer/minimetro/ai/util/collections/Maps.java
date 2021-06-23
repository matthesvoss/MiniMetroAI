package me.dragonflyer.minimetro.ai.util.collections;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Maps {

    static <E> Map<E, Integer> indexMap(Collection<E> list) {
        Map<E, Integer> indexMap = new LinkedHashMap<>(list.size());
        int i = 0;
        for (E e : list) {
            indexMap.put(e, i++);
        }
        return indexMap;
    }

}
