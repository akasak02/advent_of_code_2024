package com.github.akasak02.aoc2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SafeInputReader implements AutoCloseable, Iterable<String> {
    /*TODO: refactor 
    I am not satisfied with my SafeInputReader implementation
    it should present interface that is even more simple and abstracted out
    as it stands it's not much better to using BufferedReader within Day's methods
    for now I want to see how I will end up using it in my solutions
    and refactor this code untill it fits the project */
    private BufferedReader inputReader;
    private Path filePath;
    //Constructors
    public SafeInputReader(String resourceName) {
        try {
            this.filePath = Path.of("resources", resourceName);
        } catch (InvalidPathException e) {
            System.err.println("Error: Path syntax is invalid for: " + resourceName);
            throw new RuntimeException("Invalid path syntax", e);
        }

        if (!Files.exists(this.filePath)) {
            System.err.println("Error: File does not exist at: " + this.filePath.toAbsolutePath());
        } else if (!Files.isReadable(this.filePath)) {
            System.err.println("Error: Could not read file, insufficient permissions.");
        }

        try {
            this.inputReader = Files.newBufferedReader(this.filePath);
        } catch (IOException e) {
            System.err.println("Error: Failed to create BufferedReader for " + this.filePath);
            throw new RuntimeException("Failed to open file reader", e);
        }
    }

    public SafeInputReader() {
        this("test.txt");
    }

    public String readLine(){
        if (this.inputReader == null) {
            return null;
        }
        try {
            return this.inputReader.readLine();
        } catch (IOException e) {
            System.err.println("Error: couldn't read line from " + filePath.toAbsolutePath());
            return null;
        }
    }

    public List<String> getInput() {
        List<String> lines = new ArrayList<>();
        String line;
        while((line = this.readLine()) != null)
            lines.add(line);
        return lines;
    } 
    //Interable<String> interface
    @Override 
    public Iterator<String> iterator() {
        return this.inputReader.lines().iterator();
    }
    //AutoClosable interface
    @Override
    public void close() throws IOException {
        if(this.inputReader != null) {
            this.inputReader.close();
        }
    }
}
