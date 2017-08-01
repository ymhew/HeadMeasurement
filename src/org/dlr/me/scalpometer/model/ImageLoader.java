/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.model;

import ij.ImageStack;
import ij.process.ImageProcessor;
import java.io.File;
import java.io.IOException;
import loci.formats.ChannelSeparator;
import loci.formats.FormatException;
import loci.plugins.util.ImageProcessorReader;
import loci.plugins.util.LociPrefs;

/**
 *
 * @author Matthias Bremser
 */
public class ImageLoader {
    private ImageStack stack;

    public ImageLoader(File file) {
        ImageProcessorReader r = new ImageProcessorReader(
        new ChannelSeparator(LociPrefs.makeImageReader()));
        //String name = file.getName();
        String id = file.getAbsolutePath();
        try {
            r.setId(id);
            int num = r.getImageCount();
            int width = r.getSizeX();
            int height = r.getSizeY();
            ImageStack newStack = new ImageStack(width, height);
            byte[][][] lookupTable = new byte[r.getSizeC()][][];
            for (int i=0; i<num; i++) {
                ImageProcessor ip = r.openProcessors(i)[0];
                newStack.addSlice("" + (i + 1), ip);
                int channel = r.getZCTCoords(i)[1];
                lookupTable[channel] = r.get8BitLookupTable();
            }
            r.close();
            this.stack = newStack;
        } catch (FormatException exc) {
            System.err.println("Sorry, an error occurred: " + exc.getMessage());
        } catch (IOException exc) {
            System.err.println("Sorry, an error occurred: " + exc.getMessage());
        }
    }

    public ImageStack getStack() {

        return stack;
    }
}


