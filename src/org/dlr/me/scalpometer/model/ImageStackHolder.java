/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.model;

import ij.ImageStack;

/**
 *
 * @author Matthias Bremser
 */
public class ImageStackHolder {
    ImageStack stack;

    public void setStack(ImageStack stack) {
        this.stack = stack;
    }

    public ImageStack getStack() {

        return stack;
    }

}
