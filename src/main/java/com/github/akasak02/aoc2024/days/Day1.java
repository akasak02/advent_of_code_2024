package com.github.akasak02.aoc2024.days;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import com.github.akasak02.aoc2024.SafeInputReader;

public class Day1{
    public static String solution1() {
        List<Integer> left = new ArrayList<>(), right = new ArrayList<>();
        //read input
        try(SafeInputReader input = new SafeInputReader("input1.txt")) {
            String line;
            while((line = input.readLine()) != null) {
                String[] tmp = line.trim().split("\s+");
                left.add(Integer.parseInt(tmp[0]));
                right.add(Integer.parseInt(tmp[1]));
            }
        } catch (Exception e) {
            System.err.println("Day 1: exception during attempt on file reading");
        }
        //compare difference on sorted list
        Collections.sort(left);
        Collections.sort(right);

        return String.valueOf(
            IntStream.range(0, left.size())
                .map(i -> Math.abs(left.get(i) - right.get(i)))
                .sum()
        );
    }

    public static String solution2() {
        return null;
    }
}
