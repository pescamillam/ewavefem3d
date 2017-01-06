package com.pescamillam.ewavefem.solver.main;

import java.io.IOException;
import java.util.Locale;

import com.pescamillam.ewavefem.window.Window;

// Main class of the finite element processor
public class Jfem {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Window window;
        try {
            window = new Window();
            window.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
