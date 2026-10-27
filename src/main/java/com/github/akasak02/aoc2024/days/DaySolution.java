package com.github.akasak02.aoc2024.days;

import java.util.List;

import com.github.akasak02.aoc2024.SafeInputReader;

public abstract class DaySolution {
    protected DaySolution() {
        throw new UnsupportedOperationException("Error: the class is not meant to be instantiated");
    }
    public static String solution1() {
        return "not implemented yet";
    }
    public static String solution2() {
        return "not implemented yet";
    }

    protected static List<String> inputList(String fileName) {
        List<String> lines = null;
        try(var input = new SafeInputReader(fileName)) {
            lines = input.getInput();
        } catch (Exception e) {
            System.err.println("exception during attempt on file reading");
        }
        return lines;
    }
}
