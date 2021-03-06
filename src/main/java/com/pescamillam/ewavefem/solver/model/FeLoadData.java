package com.pescamillam.ewavefem.solver.model;

import java.util.LinkedList;

import com.pescamillam.ewavefem.solver.reader.FeScanner;

// Load data
public class FeLoadData {

    FeScanner reader;
    public static String loadStepName;
    // Load scale multiplier
    double scaleLoad;
    // Relative residual norm tolerance
    static double residTolerance = 0.01;
    // Maximum number of iterations (elastic-plastic problem)
    static int maxIterNumber = 100;
    // Degrees of freedom with node forces
    LinkedList<Dof> nodForces;
    // Element face surface loads
    LinkedList<ElemFaceLoad> surForces;
    // Temperature increment
    public static double[] dtemp;

    // Increment of force load
    static double[] dpLoad;
    // Total force load
    static double[] spLoad;
    // Increment of fictitious thermal loading
    static double[] dhLoad;
    // Displacement increment
    static double[] dDispl;
    // Total displacements
    public static double[] sDispl;
    // Right-hand side of global equation system
    public static double[] RHS;

    // Working arrays
    static int[] iw = new int[8];
    static double[] dw = new double[8];
    static double[][] box = new double[2][3];

    enum vars {
        loadstep, scaleload,
        residtolerance, maxiternumber,
        nodforce, surforce, boxsurforce, nodtemp,
        includefile, end
    }

}
