package com.github.akasak02.aoc2024.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

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

        public static Integer toValue(Sign s) {
            return switch(s) {
                case POSITIVE -> 1;
                case NEGATIVE -> -1;
                default -> 0;
            };
        }
    }

    private record DampenedReport(List<Integer> dampenedDelta, int remTolerance) {}

    private static List<List<Integer>> loadreports() {
        List<String> lines = inputList("input2.txt");
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


    /*
        Initial solution working for part 1 exclusively: 
     */
    // private static boolean isAbsoluteSafe(List<Integer> report) {
    //     if(report.size() < 2)
    //         return true; //not the case if they differ by more than [0 ; 3]
    //     Sign trend = Sign.signum(report.get(1) - report.get(0));
    //     for(int col = 1; col < report.size(); col++) {
    //         int absoluteDiff = Math.abs(report.get(col) - report.get(col-1));
    //         if(absoluteDiff > 3 
    //             || absoluteDiff == 0
    //             || trend != Sign.signum(report.get(col) - report.get(col-1)))
    //             return false;
    //     }
    //     return true; 
    // }
    private static Sign checkMajoritySign(List<Integer> deltas) {
        int vote =(int)(deltas.stream().map(Sign::signum).mapToInt(Sign::toValue).sum());
        return Sign.signum(vote);
    }

    private static int firstProblemIdx(List<Integer> deltas, Sign expected) {
        int upperBound = 0, lowerBound = 0;
        for(int idx = 0; idx < deltas.size(); idx++) {
            switch(expected) {
                case Sign.POSITIVE:
                    upperBound = 3;
                    lowerBound = 1;
                    break;
                default:
                    upperBound = -1;
                    lowerBound = -3;
            }
            if(deltas.get(idx) > upperBound || deltas.get(idx) < lowerBound)
                return idx;
        }
        return -1;
    }

    private static List<Integer> repoertDampener(List<Integer> original, int problemIdx) {
        List<Integer> dampener = new ArrayList<>(original);
        /* In order for Day2.isSafe to process tolerance greater than 1, 
        * Day2.repoertDampener method must handle more robust delta masking
        * as it stands it will sum two adjacent deltas and override both with the sum
        * simulating skiping one read from report
        * this logic fails if tolerance >=2 for two main reasons:
        * -if sum still produces bad result now it is duplicated in the table
        * -if whole blocks of adjacant numbers from report are to be ignored new common value should be propagated across all coresponging deltas
        * To stop this information loss and deformation:
        * List<Integer> dampener should be constructed based on full report
        * and set of indecies to ignore, not a single problemIdx value
        * 
        * advent of code doesn't require tolerance greater than 1 and also
        * as tolerance approaches report length the problem degenerates into
        * a variation of knapsack problem which is NP-complete anyways so this code was never meant to
        * process tolerance greater than a small fraction of repoert length, 
        * for instance my implementation of voting on delta sign is a heuristic not applicable for big tolerance
        * that being said I do want to implement this expansion and I leave it as:
        * TODO: implement handling the tolerance greater than 1
        */
        if(problemIdx == -1)
            dampener = dampener.subList(1,dampener.size());
        else if(problemIdx == original.size() - 1)
            dampener = dampener.subList(0, dampener.size()-1);
        else {
            int deltaSum = dampener.get(problemIdx) + dampener.get(problemIdx+1);
            dampener.set(problemIdx, deltaSum);
            dampener.set(problemIdx + 1, deltaSum);
        }
        return dampener;
    }

    private static boolean isSafe(List<Integer> report, int tolerance) {
        if(tolerance > 1 )
            throw new UnsupportedOperationException("To run this method for tolerance >1 Day2.repoertDampener must support more robust masking");
        List<Integer> deltas = new ArrayList<>(report.size()-1);
        for(int idx = 1; idx < report.size(); idx++) {
            deltas.add(report.get(idx-1) - report.get(idx));
        }
        Sign expectedSign = checkMajoritySign(deltas);
        Stack<DampenedReport> toCheck = new Stack<>();
        toCheck.push(new DampenedReport(deltas, tolerance));
        DampenedReport current;
        int problemIdx = -1;
        while(!toCheck.empty()) {
            current = toCheck.pop();
            problemIdx = firstProblemIdx(current.dampenedDelta, expectedSign);
            if(problemIdx == -1) {
                return true;
            }
            //else I have to consider a few cases:
            if(current.remTolerance == 0) //no tolerance left, record unfit for further dampening
                continue;
            toCheck.push( //problematic number coud be the one before delta (less likely lower in stack)
                new DampenedReport(
                    repoertDampener(current.dampenedDelta, problemIdx-1),
                    tolerance - 1)
            );
            toCheck.push(
                new DampenedReport( //problematic number coud be the one after delta (more likely, will be poped first)
                    repoertDampener(current.dampenedDelta, problemIdx),
                    tolerance - 1)
            );
            
        }
        
        return false;
    }

    public static String solution1() {
        List<List<Integer>> reports = loadreports();
        return String.valueOf(
            reports.stream()
                .mapToInt(x-> Day2.isSafe(x, 0) ? 1 : 0)
                .sum()
        );
    }

    public static String solution2() {
        List<List<Integer>> reports = loadreports();
        return String.valueOf(
            reports.stream()
                .mapToInt(x-> Day2.isSafe(x, 1) ? 1 : 0)
                .sum()
        );
    }
}
