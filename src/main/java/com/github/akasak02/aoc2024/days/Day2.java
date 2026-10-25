package com.github.akasak02.aoc2024.days;

import java.util.Arrays;
import java.util.List;

import com.github.akasak02.aoc2024.SafeInputReader;

public final class Day2 extends DaySolution{
    private Day2(){
        super();
    }

    private enum Sign {
        POSITIVE,
        NEGATIVE,
        ZERO;

        public static Sign signum(int toCheck) {
            if(toCheck > 0)
                return Sign.POSITIVE;
            else if(toCheck < 0)
                return Sign.NEGATIVE;
            return Sign.ZERO;
        }
    }

    private static List<List<Integer>> loadreports() {
        List<String> lines = null;
        try(var input = new SafeInputReader("input2.txt")) {
            lines = input.getInput();
        } catch (Exception e) {
            System.err.println("Day 1: exception during attempt on file reading");
        }
        if(lines != null)
            return lines.stream()
                .map( line -> 
                    Arrays.stream(
                        line.trim().split("\s+")
                    ).map(Integer::parseInt)
                    .toList()
                )
                .toList();
        throw new NullPointerException("Error: input data evaluated to nullptr");
    }

    private static boolean isSafe(List<Integer> report) {
        if(report.size() < 2)
            return true;
        Sign trend = Sign.signum(report.get(1) - report.get(0));
        for(int col = 1; col < report.size(); col++) {
            int absoluteDiff = Math.abs(report.get(col) - report.get(col-1));
            if(absoluteDiff > 3 
                || absoluteDiff == 0
                || trend != Sign.signum(report.get(col) - report.get(col-1)))
                return false;
        }
        return true; 
    }

    public static String solution1() {
        List<List<Integer>> reports = loadreports();
        return String.valueOf(
            reports.stream()
                .mapToInt(x-> Day2.isSafe(x) ? 1 : 0)
                .sum()
        );
    }
}
