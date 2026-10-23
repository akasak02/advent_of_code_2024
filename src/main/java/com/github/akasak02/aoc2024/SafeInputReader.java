package com.github.akasak02.aoc2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.InvalidPathException;
import java.util.stream.Stream;
import java.util.Iterator;

class SafeInputReader implements AutoCloseable, Iterable<String> {
    private BufferedReader inputReader;
    private Path filePath;
    //Constructors
    SafeInputReader(String resourceName) {
        try {
            this.filePath = Path.of("resources", resourceName); // Clean way to combine paths
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

    SafeInputReader() {
        this("test.txt");
    }

    String readLine() throws IOException {
        if (this.inputReader == null) {
            return null;
        }
        return this.inputReader.readLine();
    }
    //Interable<String> interface
    @Override Stream<String> iterator() {
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
