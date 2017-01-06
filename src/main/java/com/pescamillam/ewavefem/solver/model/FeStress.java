package com.pescamillam.ewavefem.solver.model;

import java.io.*;
import java.util.ListIterator;
import java.util.logging.Logger;

import com.pescamillam.ewavefem.solver.elem.Element;
import com.pescamillam.ewavefem.solver.elem.StressContainer;
import com.pescamillam.ewavefem.solver.material.Material;
import com.pescamillam.ewavefem.solver.reader.FeScanner;
import com.pescamillam.ewavefem.solver.writer.FePrintWriter;

// Stress increment due to displacement increment
public class FeStress {

    private static final Logger LOGGER = Logger.getLogger("Window");

    public static double relResidNorm;
    private FeModel fem;

    // Constructor for stress increment.
    // fem - finite element model
    public FeStress(FeModel fem) {
        this.fem = fem;
    }

    // Compute stress increment for the finite element model
    public void computeIncrement() {

        // Accumulate solution vector in displacement increment
        for (int i=0; i<fem.nEq; i++)
            FeLoad.dDispl[i] += FeLoad.RHS[i];

        // Compute stresses at reduced integration points
        for (int iel = 0; iel < fem.nEl; iel++) {
            Element elm = fem.elems[iel];
            elm.setElemXyT();
            elm.disAssembleElemVector(FeLoad.dDispl);

            for (int ip = 0; ip < elm.str.length; ip++) {
                Material mat =
                    (Material) fem.materials.get(elm.matName);
                mat.strainToStress(elm, ip);
            }
        }
    }

    // Check equilibrium and assemble residual vector.
    // iter - number of iterations performed
    public boolean equilibrium(int iter) {

        if (fem.physLaw == FeModel.PhysLaws.elastic ||
                iter == FeLoad.maxIterNumber) return true;
        // Assemble residual vector to right-hand side
        for (int i=0; i<fem.nEq; i++)
            FeLoad.RHS[i] = FeLoad.spLoad[i]+FeLoad.dpLoad[i];
        Element elm;
        for (int iel = 0; iel < fem.nEl; iel++) {
            elm = fem.elems[iel];
            elm.setElemXy();
            elm.equivStressVector();
            elm.assembleElemVector(Element.evec,FeLoad.RHS);
        }
        // Displacement boundary conditions
        ListIterator<Dof> it = fem.defDs.listIterator(0);
        while (it.hasNext()) {
            Dof d = it.next();
            FeLoad.RHS[d.dofNum-1] = 0;
        }
        // Relative residual norm
        double dpLoadNorm = vectorNorm(FeLoad.dpLoad);
        if (dpLoadNorm < 1e-30)
            dpLoadNorm = vectorNorm(FeLoad.dhLoad);
        relResidNorm = vectorNorm(FeLoad.RHS)/dpLoadNorm;
        return relResidNorm < FeLoad.residTolerance;
    }

    // Returns norm of a vector v
    double vectorNorm(double[] v) {

        double norm = 0;
        for (double aV : v) norm += aV * aV;
        return Math.sqrt(norm);
    }

    // Accumulate loads, temperature and stresses
    public void accumulate() {

        for (int i=0; i<fem.nEq; i++) {
            FeLoad.spLoad[i] += FeLoad.dpLoad[i];
            FeLoad.sDispl[i] += FeLoad.dDispl[i];
        }
        for (int iel = 0; iel < fem.nEl; iel++)
             fem.elems[iel].accumulateStress();
    }

    // Write results to a file.
    public void writeResults(String outputPath) {
        String outputFilePath = outputPath + "/out." + FeLoad.loadStepName;

        PrintWriter writer = new FePrintWriter().getPrinter(outputFilePath);
        LOGGER.info("writing to " + outputFilePath);

        writer.printf("Displacements\n\n");
        if (fem.nDim == 2)
            writer.printf(" Node             ux             uy");
        else
            writer.printf(" Node             ux             uy"
                                        + "             uz");
        for (int i = 0; i < fem.nNod; i++) {
            writer.printf("\n%5d", i + 1);
            for (int j = 0; j < fem.nDim; j++)
              writer.printf("%15.6e", FeLoad.sDispl[fem.nDim*i+j]);
        }

        writer.printf("\n\nStresses\n");
        for (int iel = 0; iel < fem.nEl; iel++) {
            if (fem.nDim == 2)
                writer.printf("\nEl %4d     sxx            syy"
                    +"            sxy            szz"
                    +"            epi", iel+1);
            else
                writer.printf("\nEl %4d     sxx            syy"
                    +"            szz            sxy"
                    +"            syz            szx"
                    +"            epi", iel+1);
            for (StressContainer aStr : fem.elems[iel].str) {
                writer.printf("\n");
                for (int i = 0; i < 2 * fem.nDim; i++)
                    writer.printf("%15.8f", aStr.sStress[i]);
                writer.printf("%15.8f", aStr.sEpi);
            }
        }
        writer.close();
    }

    // Read results from a file.
    // displ - displacements for the finite element model (out)
    public void readResults(String resultFile, double[] displ) {

        if (resultFile==null) return;

        FeScanner reader = new FeScanner(resultFile);
        // Read displacements
        reader.moveAfterLineWithWord("node");
        for (int i = 0; i < fem.nNod; i++) {
            reader.readInt();
            for (int j = 0; j < fem.nDim; j++)
                displ[fem.nDim*i + j] = reader.readDouble();
        }
        // Read stresses
        for (int iel = 0; iel < fem.nEl; iel++) {
            reader.moveAfterLineWithWord("el");
            for (StressContainer aStr : fem.elems[iel].str) {
                for (int i = 0; i < 2 * fem.nDim; i++)
                    aStr.sStress[i] = reader.readDouble();
                aStr.sEpi = reader.readDouble();
            }
        }
        reader.close();
    }

}
