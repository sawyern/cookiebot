package sawyern.cookiebot.util;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class MapUtil {

    /**
     * sorts a map by value descending by converting the map to a linked hash map
     * @param map map to sort
     * @param <K> type of the key of the map
     * @param <V> type of the value of the map
     * @return LinkedHashMap sorted starting from the lowest value to highest value
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
