/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer;

import org.dlr.me.scalpometer.controller.Controller;
import org.dlr.me.scalpometer.view.View;

/**
 *
 * @author Matthias Bremser
 */
public class Scalpometer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.addView(new View(controller));
    }

}
