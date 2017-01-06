package com.pescamillam.ewavefem.solver.writer;

import java.io.*;
import java.util.logging.Logger;

//  Finite element printer to file
public class FePrintWriter {

    private static final Logger LOGGER = Logger.getLogger("Window");

    PrintWriter writer;
    public PrintWriter getPrinter(String fileOut) {
        try {
            writer = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter(fileOut)));
        } catch (Exception e) {
            LOGGER.info("Cannot open output file: " + fileOut);
        }
        return writer;
   }

}
