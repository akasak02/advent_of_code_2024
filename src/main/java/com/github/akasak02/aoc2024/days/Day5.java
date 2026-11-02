package com.github.akasak02.aoc2024.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;;

public class Day5 extends DaySolution {
    private static record Pair(int first, int second) {}
    private static class OrderGraph {
        private Map<Integer,List<Integer>> edges;


        private void buildGraph(List<int[]> rules) {
            for(int[] rule : rules) {
                if(!this.edges.containsKey(rule[0])) {
                    this.edges.put(rule[0], new ArrayList<>());
                }
                if(!this.edges.containsKey(rule[1])) {
                    this.edges.put(rule[1], new ArrayList<>());
                }
                edges.get(rule[0]).add(rule[1]);
            }
        }

        OrderGraph(List<String> rules) {
            this.edges = new HashMap<>();
            this.buildGraph(
                rules.stream()
                .map( rule -> Arrays.stream(rule.split("\\|")).mapToInt(x -> Integer.parseInt(x)).toArray())
                .toList()
            );
        }
        /* I underestimated the data size - solution as it works for smaller problems
        * but run out of heap memory for sets as large as actual data
        * [todo]: revise OrderGraph.calcPathLenghts() to handle big data better
        * graph should be a directed acyclical graph so I suppose there is some optimisation to make
         */
        private List<Pair> calcPathLengths(List<Integer> toOrder) {
            List<Pair> pathLens = new ArrayList<>(toOrder.size());
            Map<Integer,Integer> lengths = new HashMap<>();
            Stack<Pair> toTraverse = new Stack<>();
            for(Integer entry : toOrder) {
                toTraverse.push(new Pair(entry, 1 /*distance from root*/));
                lengths.put(entry, 0);
            }
            Pair current;
            while(!toTraverse.isEmpty()) {
                current = toTraverse.pop();
                if(current.second <= lengths.get(current.first))
                    continue;
                lengths.put(current.first, current.second);
                for(Integer child : this.edges.get(current.first)) {
                    if(lengths.get(current.first) <= current.second)
                        toTraverse.push(new Pair(child, current.second + 1));
                }
            }
            for(Integer entry : toOrder) {
                pathLens.add(new Pair(entry, lengths.get(entry)));
            }
            return pathLens;
        }

        public List<Integer> orderedEntries() {
             //likelyhood of being further in absolute order has somewhat inverse relation with the number of left side occurences
             //step 1 I create this heuristic order which will contribute to minimising graph traversals in step 2
            List<Pair> heuristicReverseOrder = new ArrayList<>();
            for(Integer key : this.edges.keySet()) {
                heuristicReverseOrder.add(new Pair(key, this.edges.get(key).size()));
                Collections.sort(heuristicReverseOrder, Comparator.comparing(Pair::second));
            }
            //step 2 for each node I calculate the longest possible path to reach it from root (virtual element before leftmost one)
            List<Pair> paths = calcPathLengths(heuristicReverseOrder.stream().map(Pair::first).toList());
            //step 3 sorting entries by path lengths, if two entries get the same value the order doesn't matter 
            //...because they shoud be on separate paths anyways, so ordering them is impossible = virtually equal
            Collections.sort(paths, Comparator.comparing(Pair::second));
            return paths.stream()
                .map(Pair::first)
                .toList();
        }
    }

    private static boolean inRightOrder(List<Integer> toCheck, List<Integer> order) {
        int idxInOrder = -1;
        int nextIdx;
        for(int element : toCheck) {
            nextIdx = order.indexOf(element);
            if(nextIdx < idxInOrder)
                return false;
            idxInOrder = nextIdx;
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
        OrderGraph instance = new OrderGraph(rules);
        List<Integer> orderedPages = instance.orderedEntries();
        int sum = 0;
        for(List<Integer> pages : toPrint) {
            if(inRightOrder(pages, orderedPages)) {
                sum += pages.get(pages.size() / 2);
            }
        }
        return "calcPathLengths() need to be revised"/*String.valueOf(sum)*/;
    }
}
