/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.model;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dlr.me.scalpometer.controller.StatisticUnit;

/**
 *
 * @author Matthias Bremser
 */
public class FileSaver {
    File currentFile;
    boolean open = false;
    int measurementCount = 0;
    FileWriter fileWriter;
    BufferedWriter out;
    private StatisticUnit statUnit;
    DecimalFormat df = (DecimalFormat)DecimalFormat.getInstance(Locale.GERMAN);

    public FileSaver() {
            df.applyPattern( "#,###,##0.000" );
    }

    public void appendLine(String text) {
        if (!open) {
            open();
        }
        try {
            out.write(text + "\n");
        } catch (IOException ex) {
            Logger.getLogger(FileSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void open() {
        try {
            fileWriter = new FileWriter(currentFile.getPath() + "_" + measurementCount++ + ".txt");
            out = new BufferedWriter(fileWriter);
            open = true;
        } catch (IOException ex) {
            Logger.getLogger(FileSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void close() {
        try {
            out.close();
            open = false;
        } catch (IOException ex) {
            Logger.getLogger(FileSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCurrentFile(File currentDirectory) {
        this.currentFile = currentDirectory;
        open();
    }


    public void setStatisticUnit(StatisticUnit statUnit) {
        this.statUnit = statUnit;
    }

    public void processAndClose() {
        appendLine("Points:\n");
        ArrayList<Point> measurePoints = statUnit.getMeasurePoints();
        for (int i = 0; i < measurePoints.size(); i += 2) {
            Point p1 = measurePoints.get(i);
            Point p2 = measurePoints.get(i + 1);
            appendLine(p1.x + "," + p1.y + " ; " + p2.x + "," + p2.y);
        }
        appendLine("\nDiameter in mm:\n");
        for (Double d : statUnit.getLineLengthsArray()) {
            appendLine(df.format(d));
        }
        appendLine("");
        appendLine("------------");
        appendLine("");
        appendLine("Mean: " + df.format(statUnit.getMean()));
        appendLine("StandardDeviation: " + df.format(statUnit.getStandardDeviation()));
        close();
    }
}
