package com.pescamillam.ewavefem.solver.processor;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.pescamillam.ewavefem.solver.elem.Element;
import com.pescamillam.ewavefem.solver.model.FeLoad;
import com.pescamillam.ewavefem.solver.model.FeModel;
import com.pescamillam.ewavefem.solver.model.FeStress;
import com.pescamillam.ewavefem.solver.reader.FeScanner;
import com.pescamillam.ewavefem.solver.solver.Solver;

public class FemProcessor {

    FeModel fem;

    public void process(FeScanner reader, PrintWriter writer, String outputPath) {
        printDate(writer);

        fem = new FeModel(reader, writer, outputPath);
        Element.fem = fem;

        fem.readData();

        writer.printf("\nNumber of elements    nEl = %d\n"+
                  "Number of nodes      nNod = %d\n"+
                  "Number of dimensions nDim = %d\n",
                  fem.nEl, fem.nNod, fem.nDim);

        long t0 = System.currentTimeMillis();

        Solver solver = Solver.newSolver(fem);
        solver.assembleGSM();

        writer.printf("Memory for global matrix: %7.2f MB\n",
                Solver.lengthOfGSM*8.0e-6);

        FeLoad load = new FeLoad(fem);
        Element.load = load;

        FeStress stress = new FeStress(fem);

        // Load step loop
        while (load.readData( )) {
            load.assembleRHS();
            int iter = 0;
            // Equilibrium iterations
            do {
                iter++;
                int its = solver.solve(FeLoad.RHS);
                if (its > 0) writer.printf(
                    "Solver: %d iterations\n", its);
                stress.computeIncrement();
            } while (!stress.equilibrium(iter));

            stress.accumulate();
            stress.writeResults(outputPath);
            writer.printf("Loadstep %s", FeLoad.loadStepName);
            if (iter>1) writer.printf(" %5d iterations, " +
                "Relative residual norm = %10.5f",
                iter, FeStress.relResidNorm);
            writer.printf("\n");
        }

        writer.printf("\nSolution time = %10.2f s\n",
                (System.currentTimeMillis()-t0)*0.001);
    }

    // Print date and time.
    // PR - PrintWriter for listing file
    public static void printDate(PrintWriter writer) {

        Calendar c = new GregorianCalendar();

        writer.printf("Date: %d-%02d-%02d  Time: %02d:%02d:%02d\n",
            c.get(Calendar.YEAR),  c.get(Calendar.MONTH)+1,
            c.get(Calendar.DATE),  c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
    }

    public FeModel getFem() {
        return fem;
    }

}
