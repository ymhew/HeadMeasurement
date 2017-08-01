/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.controller;

import java.io.File;
import org.dlr.me.scalpometer.model.ImageLoader;
import org.dlr.me.scalpometer.model.ImageStackHolder;
import org.dlr.me.scalpometer.view.View;

/**
 *
 * @author Matthias Bremser
 */
public class Controller {
    private ImageLoader imageLoader;
    private ImageStackHolder stackHolder;
    private View view;

    public void addView(View view) {
        this.view = view;
        this.stackHolder = new ImageStackHolder();
    }

    public void openFile(File file) {
        imageLoader = new ImageLoader(file);
        stackHolder.setStack(imageLoader.getStack());
    }

    public ImageStackHolder getImageStackHolder() {

        return stackHolder;
    }

}
