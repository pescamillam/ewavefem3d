package com.pescamillam.ewavefem.generator;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    FileWriter fileWriter;
    
    public Writer(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }
    
    public void write(String string) throws IOException {
        //System.out.print(string);
        fileWriter.write(string);
    }
    
    public void write(Double number) throws IOException {
        write(String.valueOf(number));
    }
    
    public void write(Integer number) throws IOException {
        write(String.valueOf(number));
    }
}
