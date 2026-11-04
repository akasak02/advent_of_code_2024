package com.github.akasak02.aoc2024.days;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day6 extends DaySolution {
    private static record MoveRegister(Coord position, DirWrapper dir) {
        @Override
        public boolean equals(Object o) {
            if(this == o)
                return true;
            if(this == null || this.getClass() != o.getClass())
                return false;
            MoveRegister mr = (MoveRegister)o;
            return (
                this.position.y == mr.position.y 
                && this.position.x == mr.position.x 
                && this.dir.direction == mr.dir.direction
            );
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.position.y, this.position.x, this.dir.direction.ordinal());
        }
    }

    private static class Coord {

        private int y, x;

        Coord(int y, int x) {
            this.y = y;
            this.x = x;
        }

        Coord(Coord other) {
            this.x = other.x;
            this.y = other.y;
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

    private static class DirWrapper {
        private Direction direction;

        DirWrapper(Direction dir) {
            this.direction = dir;
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

    private static long countSteps(List<String> grid) {
        Set<Coord> uniq = uniqSteps(grid);
        return uniq.size();
    }

    private static Set<Coord> uniqSteps(List<String> grid) {
        int yMax = grid.size(), xMax = grid.get(0).length();
        Set<Coord> obstacles = getObstaclePosition(grid);
        Coord guard = startingPosition(grid);
        Direction dir = Direction.NORTH;
        Set<Coord> uniqSteps = new HashSet<>();

        while(!guard.outOfBounds(yMax, xMax)) {

            uniqSteps.add(new Coord(guard.y, guard.x));

            dir.nextStep(guard);

            while(obstacles.contains(guard)) {
                dir.stepBack(guard);
                dir = dir.rotate();
                dir.nextStep(guard);
            }
        }

        return uniqSteps;
    }

    private static boolean hasCycle(Coord position, Direction dir, Set<Coord> obstacles, int yMax, int xMax) {
        Set<MoveRegister> visited = new HashSet<>();
        MoveRegister simulatedGuard = new MoveRegister(new Coord(position), new DirWrapper(dir));

        while(!simulatedGuard.position.outOfBounds(yMax, xMax)) {

            if(visited.contains(simulatedGuard))
                return true;
            visited.add(new MoveRegister(new Coord(simulatedGuard.position),new DirWrapper(simulatedGuard.dir.direction)));

            simulatedGuard.dir.direction.nextStep(simulatedGuard.position);

            while(obstacles.contains(simulatedGuard.position)) {
                simulatedGuard.dir.direction.stepBack(simulatedGuard.position);
                simulatedGuard.dir.direction = simulatedGuard.dir.direction.rotate();
                simulatedGuard.dir.direction.nextStep(simulatedGuard.position);
            }
            
        }

        return false;
    }

    private static long countCycles(List<String> grid) {
        Set<Coord> uniq = uniqSteps(grid);
        Set<Coord> obstacles = getObstaclePosition(grid);
        Coord start = startingPosition(grid);

        uniq.remove(start);
        long cycleCounter = 0;
        for(Coord possibleObstacle : uniq) {
            if(possibleObstacle.equals(start))
                continue;
            obstacles.add(possibleObstacle);
            if(hasCycle(start,Direction.NORTH, obstacles, grid.size(), grid.get(0).length()))
                cycleCounter++;
            obstacles.remove(possibleObstacle);
        }
        return cycleCounter;
    }

    public static String solution1() {

        List<String> grid = DaySolution.inputList("input6.txt");

        return String.valueOf(countSteps(grid));
    }

    public static String solution2() {
        List<String> grid = DaySolution.inputList("input6.txt");
        return String.valueOf(countCycles(grid));
    }
}
