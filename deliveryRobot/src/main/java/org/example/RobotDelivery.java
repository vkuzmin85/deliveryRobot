package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class RobotDelivery {
    private static int processedThreads = 1;
    public static int ROUTES_COUNT = 1000;
    private static String text = "RLRFR";
    public static final Map<Integer, Integer> sizeToFreq = new LinkedHashMap<>();

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int getRCount(String s) {
        int count = 0;
        for (Character c : s.toCharArray()) {
            if (c.equals('R')) {
                count++;
            }
        }
        return count;
    }

    public synchronized void calculateCount() {
        int count = getRCount(generateRoute(text, 100));
        if (sizeToFreq.containsKey(count)) {
            sizeToFreq.put(count, sizeToFreq.get(count) + 1);
        } else {
            sizeToFreq.put(count, 1);
        }
        processedThreads++;
        notify();
    }

    public synchronized void getStatistic() {
        try {
            while (processedThreads <= ROUTES_COUNT) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<Integer, Integer> sortedMap = sizeToFreq.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new));
        Map.Entry<Integer, Integer> entry = sortedMap.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get();
        System.out.println("Самое частое количество повторений " + entry.getKey() + " (встретилось " + entry.getValue() + " раз)");
        System.out.println("Другие размеры:");
        sortedMap.forEach((k, v) -> {
            System.out.println("-" + k + " " + v + " times");
        });
    }

    public static class Main {

        public static void main(String[] args) {

            final RobotDelivery robotDelivery = new RobotDelivery();
            for (int i = 1; i <= ROUTES_COUNT; i++) {
                new Thread(null, robotDelivery::calculateCount).start();
            }
            new Thread(null, robotDelivery::getStatistic).start();
        }

    }
}
