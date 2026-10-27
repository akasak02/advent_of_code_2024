package com.github.akasak02.aoc2024;
import com.github.akasak02.aoc2024.days.*;

public class App 
{
    public static void main( String[] args )
    {
        /* TO DO: In the future this method will parse cli requests and answer to user queries
        *  for now writing out each and every solution will have to wait untill I have more free time
        */
        System.out.println("Advent of Code 2024 solutions");
        System.out.println("result of day 1 pt 1: %s".formatted(Day1.solution1()));
        System.out.println("result of day 1 pt 2: %s".formatted(Day1.solution2()));
        System.out.println("result of day 2 pt 1: %s".formatted(Day2.solution1()));
        System.out.println("result of day 2 pt 2: %s".formatted(Day2.solution2()));
        System.out.println("result of day 3 pt 1: %s".formatted(Day3.solution1()));
        System.out.println("result of day 3 pt 2: %s".formatted(Day3.solution2()));
    }
}
