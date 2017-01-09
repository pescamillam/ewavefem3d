package com.pescamillam.ewavefem.window;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.pescamillam.ewavefem.solver.elem.Element;
import com.pescamillam.ewavefem.solver.elem.ElementQuad2D;
import com.pescamillam.ewavefem.solver.processor.FemProcessor;
import com.pescamillam.ewavefem.solver.reader.FeScanner;
import com.pescamillam.ewavefem.solver.writer.FePrintWriter;
import com.pescamillam.ewavefem.window.canvas.FemCanvas;


public class Window extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("Window");
    private static final JTextArea logTextArea = new JTextArea("");
    private static final JScrollPane logTextAreaScroll = new JScrollPane(logTextArea);
    public static FemCanvas canvas;

    public Window() throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        JButton loadFileButton = new JButton("Load File");
        loadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileSelector = new JFileChooser("/home/peter/Peter/projects/fem/examples/example02");
                int returnVal = fileSelector.showOpenDialog(Window.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileSelector.getSelectedFile();
                    logTextArea.append("Archivo cargado: ");
                    logTextArea.append(file.getAbsolutePath());
                    LOGGER.info("Abriendo: " + file.getAbsolutePath());
                    long inicio = System.nanoTime();
                    FeScanner reader = new FeScanner(file.getAbsolutePath());
                    PrintWriter writer = new FePrintWriter().getPrinter(file.getAbsolutePath() + ".out");
                    FemProcessor processor = new FemProcessor();
                    processor.process(reader, writer, file.getParentFile().getAbsolutePath());
                    logTextArea.append("\nNumero de nodos " + processor.getFem().nNod);
                    logTextArea.append("\nnodos ");
                    
                    System.out.println("Elements:");
                    for (Element a : processor.getFem().elems) {
                        for (int b : a.ind) {
                            System.out.print(b + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Nodes:");
                    for (double[] a : Element.xy) {
                        for (double b : a) {
                            System.out.print(b + " ");
                        }
                        System.out.println();
                    }
                    writer.close();
                    logTextArea.append("\nTomó " + (System.nanoTime() - inicio)/1000000 + "ms");
                    logTextArea.append("\nArchivo generado ");
                    logTextArea.append(file.getAbsolutePath() + ".out\n");
                    LOGGER.info("Archivo procesado " + file.getParentFile().getAbsolutePath());
                    canvas.clearImage();
                    drawInitial(file.getAbsolutePath(), canvas);
                } else {
                    LOGGER.info("Open command cancelled by user.");
                }
            }
        });
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.ipady = 400;

        BufferedImage myPicture = ImageIO.read(new File("Logo.png"));
        canvas = new FemCanvas();
        canvas.setSize(400, 400);
        panel.add(canvas, constraints);
        
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        panel.add(loadFileButton, constraints);

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.ipady = 100;
        panel.add(logTextAreaScroll, constraints);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.add(panel, BorderLayout.NORTH);
        this.setSize(500, 600);
    }

    protected void drawInitial(String absolutePath, FemCanvas canvas2) {
        File file = new File(absolutePath);
        
    }
}