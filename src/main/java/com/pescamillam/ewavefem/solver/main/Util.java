package com.pescamillam.ewavefem.solver.main;

// Miscellaneous static methods
public class Util {

    // Transform text direction into integer.
    // s - direction x/y/z/n.
    // returns  integer direction 1/2/3/0, error: -1.
    public static int direction(String s) {
        if      (s.equals("x")) return 1;
        else if (s.equals("y")) return 2;
        else if (s.equals("z")) return 3;
        else if (s.equals("n")) return 0;
        else return -1;
    }

}
