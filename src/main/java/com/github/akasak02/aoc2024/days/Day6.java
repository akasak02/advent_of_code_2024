package com.github.akasak02.aoc2024.days;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public class Day6 extends DaySolution {

    private static class Coord {

        private int y, x;

        Coord(int y, int x) {
            this.y = y;
            this.x = x;
        }

        public void move(int dy, int dx) {
            this.y += dy;
            this.x += dx;
        }

        public boolean outOfBounds(int yMax, int xMax) {
            return (this.y < 0 || this.y >= yMax || this.x < 0 || this.x >= xMax);
        }

        @Override
        public boolean equals(Object o) {
            if(this == o)
                return true;
            if(this == null || this.getClass() != o.getClass())
                return false;
            Coord c = (Coord)o;
            return (this.y == c.y && this.x == c.x);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.y, this.x);
        }
    }

    private static enum Direction {
        NORTH(-1,0),
        EAST(0,1),
        SOUTH(1,0),
        WEST(0, -1);
        
        private int xDelta, yDelta;

        Direction(int y, int x) {
            this.xDelta = x;
            this.yDelta = y;
        }

        public Direction rotate() {
            return values()[(this.ordinal() + 1) % values().length];
        }

        public void nextStep(Coord stepFrom) {
            stepFrom.move(this.yDelta, this.xDelta);
        }

        public void stepBack(Coord stepFrom) {
            stepFrom.move(-this.yDelta, -this.xDelta);
        }
    }

    private static Set<Coord> getObstaclePosition(List<String> grid) {

        Set<Coord> blocked = new HashSet<>();

        for(int row = 0; row < grid.size(); row++) {
            for(int col = 0; col < grid.get(0).length(); col++) {
                if(grid.get(row).charAt(col) == '#')
                    blocked.add(new Coord(row, col));
            }
        }

        return blocked;
    }

    private static Coord startingPosition(List<String> grid) {

        for(int row = 0; row < grid.size(); row++) {
            for(int col = 0; col < grid.get(0).length(); col++) {
                if(grid.get(row).charAt(col) == '^')
                    return new Coord(row,col);
            }   
        }
        throw new RuntimeException("Faulty input data has no starting position for the guard");
    }

    private static long countSteps(Coord guard, Set<Coord> obstacles, Direction dir, int yMax, int xMax) {
        boolean[][] visited = new boolean[yMax][xMax];
        for(boolean[] row : visited) {
            Arrays.fill(row, false);
        }

        while(!guard.outOfBounds(yMax, xMax)) {

            visited[guard.y][guard.x] = true;

            dir.nextStep(guard);

            if(obstacles.contains(guard)) {
                dir.stepBack(guard);
                dir = dir.rotate();
                dir.nextStep(guard);
            }
            // System.out.println("(%d,%d)".formatted(guard.y,guard.x));
        }

        long stepCount = 0L;
        for(int yIdx = 0; yIdx < yMax; yIdx++) {
            for(int xIdx = 0; xIdx < xMax; xIdx++) {
                stepCount += (visited[yIdx][xIdx]) ? 1 : 0;
            }
        }
        return stepCount;
    }

    private static long routeLength(List<String> grid) {
        int hight = grid.size(), width = grid.get(0).length();
        Set<Coord> obstacles = getObstaclePosition(grid);
        Coord guard = startingPosition(grid);
        return countSteps(guard, obstacles, Direction.NORTH, hight, width);
    }

    public static String solution1() {

        List<String> grid = DaySolution.inputList("input6.txt");

        return String.valueOf(routeLength(grid));
    }
    
    public static String solution2() {
        return DaySolution.solution2();
    }
}
