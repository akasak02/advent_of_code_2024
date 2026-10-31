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
                System.out.println("[%d - %d]".formatted(current.first, current.second));
                for(Integer child : this.edges.get(current.first)) {
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
            System.out.println(paths.stream().map(Pair::first).toList());
            System.out.println(paths.stream().map(Pair::second).toList());
            //step 3 sorting entries by path lengths, if two entries get the same value the order doesn't matter 
            //...because they shoud be on separate paths anyways, so ordering them is impossible = virtually equal
            Collections.sort(paths, Comparator.comparing(Pair::second));
            return paths.stream()
                .map(Pair::first)
                .toList();
        }
    }



    public static String solution1() {
        List<String> printerData = DaySolution.inputList("testinput.txt");
        List<String> rules = printerData.subList(0, printerData.indexOf(""));
        List<String> toPrint = printerData.subList(printerData.indexOf("") + 1, printerData.size());
        OrderGraph instance = new OrderGraph(rules);
        List<Integer> orderedPages = instance.orderedEntries();
        //[todo]: take each series from toPrint and check whether they are in the same order as equivalent elemnts in orderedPages
        //...if so add the middle element to the result, else skip
        System.out.println(orderedPages);
        return DaySolution.solution1();
    }
}
