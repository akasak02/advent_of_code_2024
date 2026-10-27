package com.github.akasak02.aoc2024.days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.HashMap;

public final class Day1 extends DaySolution {
    private Day1() {
        super();
    }
    private record LeftRight(List<Integer> left, List<Integer> right) {}
    
    private static LeftRight historianLists() {
        LeftRight result = new LeftRight(new ArrayList<>(), new ArrayList<>());
        List<String> lines = inputList("input1.txt");
        if(lines != null) {
            lines.forEach(ln -> {
                    String[] tmp = ln.trim().split("\s+");
                    result.left.add(Integer.parseInt(tmp[0]));
                    result.right.add(Integer.parseInt(tmp[1]));
                }
            );
        } else {
            throw new NullPointerException("Error: input data evaluated to nullptr");
        }
        return result; 
    } 

    private static int totalDist(LeftRight twoLists) {
        List<Integer> left = new ArrayList<>(twoLists.left);
        List<Integer> right = new ArrayList<>(twoLists.right);
        Collections.sort(left);
        Collections.sort(right);
        return IntStream.range(0, twoLists.left.size())
            .map(i -> Math.abs(left.get(i) - right.get(i)))
            .sum();
    }

    private static HashMap<Integer,Long> countRepetitions(LeftRight twoLists) {
        HashMap<Integer, Long> result = new HashMap<>();
        List<Integer> keys = twoLists.left.stream()
            .distinct()
            .toList();
        keys.forEach(k -> {
                result.put(
                    k,
                    twoLists.right.stream()
                        .filter(val -> val.equals(k))
                        .count()
                );
            }
        );
        return result;
    }

    private static long similarityScore(List<Integer> left, HashMap<Integer, Long> count) {
        return left.stream()
            .mapToLong(x -> count.containsKey(x) ? (x * count.get(x)) : 0L)
            .sum();
    }

    public static String solution1() {
        LeftRight twoLists = historianLists();
        return String.valueOf(totalDist(twoLists));
    }

    public static String solution2() {
        LeftRight twoLists = historianLists();
        HashMap<Integer,Long> counted = countRepetitions(twoLists);
        return String.valueOf(similarityScore(twoLists.left, counted));
    }
}
