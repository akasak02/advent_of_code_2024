package com.github.akasak02.aoc2024.days;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day5 extends DaySolution {
    private static boolean violatesRules(Integer first, Integer second, Set<String> rules) {
        return rules.contains(String.valueOf(second) + "|" + String.valueOf(first));
    }

    private static boolean orderIsCorrect(List<Integer> pages, Set<String> rules) {
        for(int idx = 0; idx < pages.size(); idx++) {
            for(int jdx = idx + 1; jdx < pages.size(); jdx++) {
                if(violatesRules(pages.get(idx), pages.get(jdx), rules))
                    return false;
            }
        }
        return true;
    }

    public static String solution1() {
        List<String> printerData = DaySolution.inputList("input5.txt");
        List<String> rules = printerData.subList(0, printerData.indexOf(""));
        List<List<Integer>> toPrint = printerData.subList(printerData.indexOf("") + 1, printerData.size())
            .stream()
            .map(
                s -> Arrays.stream(s.split(","))
                    .map(Integer::parseInt)
                    .toList()
            ).toList();
        Set<String> existingRules = new HashSet<>();
        for(String rule : rules) {
            existingRules.add(rule);
        }
        int sum = 0;
        for(List<Integer> pages : toPrint) {
            if(orderIsCorrect(pages, existingRules))
                sum += pages.get(pages.size() / 2);
        }
        return String.valueOf(sum);
    }

    public static String solution2() {
        List<String> printerData = DaySolution.inputList("input5.txt");
        List<String> rules = printerData.subList(0, printerData.indexOf(""));
        List<List<Integer>> toPrint = printerData.subList(printerData.indexOf("") + 1, printerData.size())
            .stream()
            .map(
                s -> Arrays.stream(s.split(","))
                    .map(Integer::parseInt)
                    .toList()
            ).toList();
        Set<String> existingRules = new HashSet<>();
        for(String rule : rules) {
            existingRules.add(rule);
        }
        int sum = 0;
        for(List<Integer> pages : toPrint) {
            if(!orderIsCorrect(pages, existingRules)) {
                List<Integer> sortable = new ArrayList<>(pages);
                Collections.sort(
                    sortable,
                    (a, b) -> {
                        if(violatesRules(a,b,existingRules))
                            return -1;
                        if(violatesRules(b,a,existingRules))
                            return 1;
                        return 0;
                    }
                );
                sum += sortable.get(pages.size() / 2);
            }
        }
        return String.valueOf(sum);
    }
}
