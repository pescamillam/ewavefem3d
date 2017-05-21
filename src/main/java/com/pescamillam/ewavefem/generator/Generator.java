package com.pescamillam.ewavefem.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Generator {

    public static void main(String[] args) throws IOException {
        File file = new File("examples/autogen/f.fem");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileWriter fileWriter = new FileWriter(file);
        Writer writer = new Writer(fileWriter);
        
        int height = 10;
        int width = 13;
        double maxY = 1;
        double maxX = 2;
        writer.write("nNod = " + (1 + 3 * width * height + 2 * height + 2 * width));
        writer.write(" nEl = " + height * width);
        writer.write(" nDim=2\n" + 
        "physLaw = elastic\n" + 
        "stressState = plstress\n" + 
        "#      Mat  name E nu alpha SY   k   m\n" + 
        "material = mat 1 0.3 0.0\n");
        
        writeNodes(writer, height, width, maxY, maxX);
        
        writeConnectivities(writer, height, width);
        
        writer.write("# constrDispl = x 0.0 2  1 -3\n" + 
                "constrDispl = y 0.0 ");
        
        writer.write(2 * width + 1);
        writer.write(" ");
        int lowerNode = 1;
        writer.write(lowerNode);
        writer.write(" ");
        for (int i = 0; i < width; i++) {
            lowerNode+=(2*height+1);
            writer.write(lowerNode);
            writer.write(" ");
            lowerNode+=(height+1);
            writer.write(lowerNode);
            writer.write(" ");
        }
        
        
        writer.write("\n" + 
                "end  \n" + 
                "  \n" + 
                "# Load\n" + 
                "  loadStep = 1\n" + 
                "  \n" + 
                "# Surface load: direction, element number, number of face\n" + 
                "#    nodes, face node numbers, intensities  \n" + 
                "  surForce = y ");
        writer.write(width % 2 == 0 ? width * height / 2 : (int)(width * height / 2.0 + height / 2.0));
        writer.write(" 3 ");
        writer.write(width % 2 == 0 ? 3 * width * height / 2 + width - height - 2 : 3 * width * height / 2 + width + height / 2);
        writer.write(" ");
        writer.write(width % 2 == 0 ? 3 * width * height / 2 + width : 3 * width * height / 2 + width + 3 * height / 2 + 1);
        writer.write(" ");
        writer.write(width % 2 == 0 ? 3 * width * height / 2 + width + 2 * height + 1 : 3 * width * height / 2 + width + 7 * height / 2 + 2);
        writer.write(" ");
        writer.write(" 1 1 1\n" + 
                "\n" + 
                "\n" + 
                "end");
        
        fileWriter.close();
    }

    private static void writeConnectivities(Writer writer, int height, int width) throws IOException {
        writer.write("elCon = ");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.write("QUAD8 mat ");
                writer.write(i*(5)+(height-1)*3*i+j*(2)+1);
                writer.write(" ");
                writer.write(i*(5)+(height-1)*2+(height-1)*3*i+j+4);
                writer.write(" ");
                writer.write(i*(5)+(height-1)*3+(height-1)*3*i+j*2+6);
                writer.write(" ");
                writer.write(i*(5)+(height-1)*3+(height-1)*3*i+j*2+7);
                writer.write(" ");
                writer.write(i*(5)+(height-1)*3+(height-1)*3*i+j*2+8);
                writer.write(" ");
                writer.write(i*(5)+(height-1)*2+(height-1)*3*i+j+5);
                writer.write(" ");
                writer.write(i*(5)+(height-1)*3*i+j*2+3);
                writer.write(" ");
                writer.write(i*(5)+(height-1)*3*i+j*2+2);
                writer.write("\n");
            }
        }
    }

    private static void writeNodes(Writer writer, int height, int width, double maxY, double maxX) throws IOException {
        writer.write("nodCoord = ");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height * 2 + 1; j++) {
                writer.write(i * maxX/width);
                writer.write(" ");
                writer.write(j * maxY / (height * 2));
                writer.write("\n");
            }
            for (int j = 0; j < height * 2 + 1; j++) {
                if (j%2 == 0) {
                    writer.write(i * maxX/width + maxX/(width*2));
                    writer.write(" ");
                    writer.write(j * maxY / (height * 2));
                    writer.write("\n");
                }
            }
            if (width == i + 1) {
                for (int j = 0; j < height * 2 + 1; j++) {
                    writer.write(i * maxX / width + maxX * 2 / (width*2));
                    writer.write(" ");
                    writer.write(j * maxY / (height * 2));
                    writer.write("\n");
                }
            }
        }
    }
}
