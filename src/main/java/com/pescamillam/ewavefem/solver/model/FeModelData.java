package com.pescamillam.ewavefem.solver.model;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import com.pescamillam.ewavefem.solver.elem.Element;
import com.pescamillam.ewavefem.solver.material.Material;
import com.pescamillam.ewavefem.solver.reader.FeScanner;

// Finite element model data
public class FeModelData {

    static FeScanner reader;
    static PrintWriter writer;
    static String baseFilePath;

    // Problem dimension =2/3
    public int nDim = 3;
    // Number of degrees of freedom per node =2/3
    public int nDf = 3;
    // Number of nodes
    public int nNod;
    // Number of elements
    public int nEl;
    // Number of degrees of freedom in the FE model
    public int nEq;
    // Elements
    public Element elems[];
    // Materials
    public HashMap<String, Material> materials = new HashMap<String, Material>();
    // Coordinates of nodes
    public double xyz[];
    // Constrained degrees of freedom
    public  LinkedList<Dof> defDs = new LinkedList<Dof>();
    public boolean thermalLoading;
    static String varName;

    public static enum StrStates {
        plstrain, plstress, axisym, threed
    }
    public static StrStates stressState = StrStates.threed;

    public static enum PhysLaws {
        elastic, elplastic
    }
    public PhysLaws physLaw = PhysLaws.elastic;

    // Input data names
    enum vars {
        nel, nnod, ndim, stressstate, physlaw, solver,
        elcon, nodcoord, material,
        constrdispl, boxconstrdispl, thermalloading,
        includefile, user, end
    }

    // Allocation of nodal coordinate array
    public void newCoordArray() {
        xyz = new double[nNod*nDim];
    }

    // Set coordinates of node
    public void setNodeCoords(int node, double[] xyzn) {
        for (int i=0; i<nDim; i++) xyz[node*nDim+i] = xyzn[i];
    }

    // Set ith coordinates of node
    public void setNodeCoord(int node, int i, double v) {
        xyz[node*nDim+i] = v;
    }

    // Get coordinates of node
    public double[] getNodeCoords(int node) {
        double nodeCoord[] = new double[nDim];
        for (int i=0; i<nDim; i++)
            nodeCoord[i] = xyz[node*nDim+i];
        return nodeCoord;
    }

    // Get ith coordinate of node
    public double getNodeCoord(int node, int i) {
        return xyz[node*nDim+i];
    }

}
