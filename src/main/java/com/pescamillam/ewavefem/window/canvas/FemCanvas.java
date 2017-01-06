package com.pescamillam.ewavefem.window.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class FemCanvas extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;
    Image img;      // Contains the image to draw on MyCanvas
    public FemCanvas() throws IOException {
        BufferedImage myPicture = ImageIO.read(new File("Logo.png"));
        img = myPicture;
        this.setSize(400, 400);
        // Initialize img here.
        this.addMouseListener(this);
    }

    public void paintComponent(Graphics g)
    {
        // Draws the image to the canvas
        g.drawImage(img, 0, 0, null);
    }

    public void mouseClicked(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        System.out.println("hi");
        Graphics g = img.getGraphics();
        g.setColor(Color.MAGENTA);
        g.fillOval(x, y, 3, 3);
        g.dispose();
        repaint();
    }

    public void clearImage() {
        Graphics g = img.getGraphics();
        g.fillRect(0, 0, 400, 400);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
