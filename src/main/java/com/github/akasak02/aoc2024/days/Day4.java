package com.github.akasak02.aoc2024.days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day4 extends DaySolution {

    private static boolean charListIsStr(List<Character> charList, String str) {
        if(charList.size() != str.length())
            return false;
        boolean front = true, back = true;
        for(int idx = 0; idx < charList.size(); idx++) {
            if(front && charList.get(idx) != str.charAt(idx)) {
                front = false;
            }
            if(back && charList.get(str.length() - 1 - idx) != str.charAt(idx)) {
                back = false;
            }
            if(!front && !back)
                return false;
        }
        return true;
    }

    private static int inRowsCounter(List<List<Character>> grid, String word) {
        int counter = 0;
        List<Character> wordStorage = new ArrayList<>(word.length());
        for(int rowIdx = 0; rowIdx < grid.size(); rowIdx++) {
            for(int colIdx = 0; colIdx <= grid.get(0).size() - word.length(); colIdx++) {
                wordStorage.clear();
                for(int charIdx = 0; charIdx < word.length(); charIdx++) {
                    wordStorage.add(grid.get(rowIdx).get(colIdx + charIdx));
                }
                counter += charListIsStr(wordStorage, word) ? 1 : 0;
            }
        }
        return counter;
    }

    private static int inDiagonalCounter(List<List<Character>> grid, String word) {
        int counter = 0;
        List<Character> wordStorage = new ArrayList<>(word.length());
        for(int rowIdx = 0; rowIdx <= grid.size() - word.length(); rowIdx++) {
            for(int colIdx = (word.length() - 1); colIdx < grid.get(0).size(); colIdx++) {
                wordStorage.clear();
                for(int charIdx = 0; charIdx < word.length(); charIdx++) {
                    wordStorage.add(grid.get(rowIdx + charIdx).get(colIdx - charIdx));
                }
                counter += charListIsStr(wordStorage, word) ? 1 : 0;
            }
        }
        return counter;
    }

    private static <T> List<List<T>> transposed (List<List<T>> original) {
        //here I use generics cause it might be helpful in other exercises
        if(original.isEmpty())
            return new ArrayList<>();
        List<List<T>> result = new ArrayList<>();
        for (int col = 0; col < original.get(0).size(); col++) {
            List<T> newRow = new ArrayList<>();
            for (int row = 0; row < original.size(); row++) {
                newRow.add(original.get(row).get(col));
            }
            result.add(newRow);
        }
        return result;
    }
    
    private static int countWord(List<List<Character>> grid, String word) {
        List<List<Character>> localGrid = grid.stream()
            .map(ArrayList::new)
            .collect(Collectors.toCollection(ArrayList::new));
        int xmasCounter = 0;
        //count horizontal
        xmasCounter += Day4.inRowsCounter(localGrid, word);
        //count diagonal from upper right to bottom left
        xmasCounter += Day4.inDiagonalCounter(localGrid,word);
        //count diagonal from upper left to bottom right
        localGrid.forEach(Collections::reverse);
        xmasCounter += Day4.inDiagonalCounter(localGrid, word);
        //count vertical - transposing the grid will make for more versitile solution
        localGrid = Day4.transposed(localGrid);
        xmasCounter += Day4.inRowsCounter(localGrid, word);
        return xmasCounter;
    }

    private static int[] cornerIdxs(int sideLen) {
        int[] result = new int[4];
        result[0] = 0;
        result[1] = sideLen - 1;
        result[2] = sideLen * (sideLen - 1);
        result[3] = result[2] + result[1];
        return result;
    }

    private static boolean isSameMiddleAndEnd(char[] inSquare, String word) {
        //does not check for the whole X shape but only middle char and ends of x
        if( ( word.length() * word.length() )!= inSquare.length)
            return false;
        int[] cornerIndecies = cornerIdxs(word.length());
        boolean middleIsRight = inSquare[4] == word.charAt(word.length() / 2);
        boolean wordsOnDiagonal = inSquare[cornerIndecies[0]] != inSquare[cornerIndecies[3]]
                && (inSquare[cornerIndecies[1]] != inSquare[cornerIndecies[2]]);
        wordsOnDiagonal = wordsOnDiagonal ||  (word.charAt(0) == word.charAt(word.length()-1));
        boolean containRightLetters = true;
        for(int cIdx : cornerIndecies) {
            containRightLetters = containRightLetters && ( inSquare[cIdx] == word.charAt(0) || inSquare[cIdx] == word.charAt(word.length()-1));
        }
        return middleIsRight && wordsOnDiagonal && containRightLetters;
    }

    private static int countXwords(List<List<Character>> grid, String word) {
        int xmasCounter = 0;

        int padding = word.length();
        if(padding % 2 == 1)
            padding /= 2;
        else
            return -1; //even length words do not form clean X shape

        char[] squareContent = new char[word.length() * word.length()];

        for(int rowIdx = padding; rowIdx < grid.size() - padding; rowIdx++) {
            for(int colIdx = padding; colIdx < grid.get(0).size() - padding; colIdx++) {

                for(int deltaRow = -padding; deltaRow <= padding; deltaRow++) {
                    for(int deltaCol = -padding; deltaCol <= padding; deltaCol++) {
                        int idx = deltaCol + padding + word.length()*(deltaRow + padding);
                        squareContent[idx] = grid.get(rowIdx + deltaRow).get(colIdx + deltaCol);
                    }
                }
                xmasCounter += (isSameMiddleAndEnd(squareContent, word)) ? 1 : 0;
            }
        }
        return xmasCounter;
    }

    private static List<List<Character>> inputGrid(String file) {
        return Day4.inputList(file)
            .stream()
            .map(
                s -> s.chars()
                    .mapToObj(c -> (char)c)
                    .toList()
            ).toList();
    }

    public static String solution1() {
        List<List<Character>> xmasGrid = Day4.inputGrid("input4.txt");
        return String.valueOf(countWord(xmasGrid,"XMAS"));
    }

    public static String solution2() {
        List<List<Character>> xmasGrid = Day4.inputGrid("input4.txt");
        return String.valueOf(countXwords(xmasGrid,"MAS"));
    }
}