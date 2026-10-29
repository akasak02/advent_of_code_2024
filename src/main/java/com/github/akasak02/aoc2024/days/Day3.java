package com.github.akasak02.aoc2024.days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.function.BinaryOperator;

public class Day3 extends DaySolution {
    private record Range(int idxBegin, int idxEnd) { }

    private static String cleanMemory(String corrupted, Pattern instruction) {
        Matcher matcher = instruction.matcher(corrupted);
        StringBuilder clean = new StringBuilder();
        while(matcher.find()) {
            clean.append(matcher.group());
        }
        return clean.toString();
    }

    private static long fetchNumber(String text) {
        return Long.parseLong(text.replaceAll("\\D+", ""));
    }

    private static Long sumOperResult(String clean, String operName, BinaryOperator<Long> operBehaviour) {
        Pattern begining = Pattern.compile("%s\\(\\d{1,3}".formatted(operName));
        Pattern end = Pattern.compile("\\d{1,3}\\)");
        Matcher matchBegin = begining.matcher(clean), matchEnd = end.matcher(clean);
        long sum = 0L;
        while(matchBegin.find()) {
            matchEnd.find();
            sum += operBehaviour.apply(
                fetchNumber(matchBegin.group()),
                fetchNumber(matchEnd.group())
            );
        }
        return sum;
    }

    private static List<Range> findDisabledRanges(String line) {
        int prevIdx = 0, currentIdx = 0;
        boolean enabled = true;
        List<Range> invalid = new ArrayList<>();
        while(currentIdx != -1) {
            prevIdx = currentIdx;
            if((currentIdx = line.indexOf((enabled ? "don't()" : "do()"), currentIdx)) == -1)
                continue;
            enabled = !enabled;
            if(enabled) {
                invalid.add(new Range(prevIdx, currentIdx));
            }
        }
        if(!enabled)
            invalid.add(new Range(prevIdx, line.length()));
        return invalid;
    }

    private static String cropDisabledRanges(String line, List<Range> invalidRanges) {
        StringBuilder enabledOnly = new StringBuilder(line);
        Collections.sort(invalidRanges, Comparator.comparing(Range::idxBegin).reversed());
        for(Range toCrop : invalidRanges) {
            enabledOnly.delete(toCrop.idxBegin, toCrop.idxEnd);
        }
        return enabledOnly.toString();
    }

    public static String solution1() {
        List<String> memory = inputList("input3.txt");
        Pattern operation = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)");
        return String.valueOf(memory.stream()
            .map(line -> cleanMemory(line, operation))
            .map(cleanLine -> sumOperResult(cleanLine, "mul", (a,b) -> a*b))
            .reduce(0L, (Long total, Long element) -> total + element)
        );
    }

    public static String solution2() {
        String memory = inputList("input3.txt").stream().reduce("", (full, line) -> full+line);
        Pattern operation = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)");
        List<Range> invalidRanges = findDisabledRanges(memory);
        memory = cleanMemory(
            cropDisabledRanges(memory, invalidRanges), 
            operation
        );
        return String.valueOf( sumOperResult(memory,"mul", (a,b) -> a*b) );
    }
    
}
