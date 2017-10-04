package main.bot;

import java.util.*;

public class MapOperations {
    public static ArrayList sortingKeys(HashMap hmap) {
        ArrayList<Object> sortedUsers = new ArrayList<>();
        Map<Integer, String> map = sortByValues(hmap);
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            sortedUsers.add(me2.getKey());
        }
        return sortedUsers;
    }

    public static ArrayList sortingValues(HashMap hmap) {
        ArrayList<Object> sortedValues = new ArrayList<>();
        Map<Integer, String> map = sortByValues(hmap);
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            sortedValues.add(me2.getValue());
        }
        return sortedValues;
    }


    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

}