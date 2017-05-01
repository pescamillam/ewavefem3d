package com.pescamillam.ewavefem.generator;

public class Generator {

    public static void main(String[] args) {
        int height = 2;
        int width = 3;
        double maxY = 1;
        double maxX = 2;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height * 2 + 1; j++) {
                System.out.print(i * maxX/width);
                System.out.print(" ");
                System.out.println(j * maxY / (height * 2));
            }
            for (int j = 0; j < height * 2 + 1; j++) {
                if (j%2 == 0) {
                    System.out.print(i * maxX/width + maxX/(width*2));
                    System.out.print(" ");
                    System.out.println(j * maxY / (height * 2));
                }
            }
            if (width == i + 1) {
                for (int j = 0; j < height * 2 + 1; j++) {
                    System.out.print(i * maxX / width + maxX * 2 / (width*2));
                    System.out.print(" ");
                    System.out.println(j * maxY / (height * 2));
                }
            }
        }
        
        //connectivities
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print("QUAD8 mat ");
                System.out.print(i*(5)+(height-1)*3*i+j*(2)+1);
                System.out.print(" ");
                System.out.print(i*(5)+(height-1)*2+(height-1)*3*i+j+4);
                System.out.print(" ");
                System.out.print(i*(5)+(height-1)*3+(height-1)*3*i+j*2+6);
                System.out.print(" ");
                System.out.print(i*(5)+(height-1)*3+(height-1)*3*i+j*2+7);
                System.out.print(" ");
                System.out.print(i*(5)+(height-1)*3+(height-1)*3*i+j*2+8);
                System.out.print(" ");
                System.out.print(i*(5)+(height-1)*2+(height-1)*3*i+j+5);
                System.out.print(" ");
                System.out.print(i*(5)+(height-1)*3*i+j*2+3);
                System.out.print(" ");
                System.out.print(i*(5)+(height-1)*3*i+j*2+2);
                System.out.println();
            }
        }
    }
    

}
