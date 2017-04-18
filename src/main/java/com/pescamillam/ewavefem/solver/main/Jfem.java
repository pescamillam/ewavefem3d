package com.pescamillam.ewavefem.solver.main;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.pescamillam.ewavefem.window.Window;

// Main class of the finite element processor
public class Jfem {

    private static final Logger LOGGER = Logger.getLogger("Window");

    public static void main(String[] args) throws SecurityException, IOException {
    	setLogger();
        Locale.setDefault(Locale.US);
        Window window;
        try {
            window = new Window();
            window.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void setLogger() throws SecurityException, IOException {
    	FileHandler fileHandler = new FileHandler("./log.log");
    	fileHandler.setFormatter(new SimpleFormatter());
    	LOGGER.addHandler(fileHandler);
    	LOGGER.info("Started");
    }
}
