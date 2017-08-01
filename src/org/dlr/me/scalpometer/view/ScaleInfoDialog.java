/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author Matthias Bremser
 */
public class ScaleInfoDialog extends JDialog {
    private View view;
    private JLabel label;
    private JButton ok;
    private JButton cancel;
    /** (0) no, (1) horizontal, (2) vertical calibration */
    private int calibration = 0;
    
    ScaleInfoDialog(final View view) {
        this.view = view;
        label = new JLabel();
        label.setText("The next two clicks will determine the scale.");
        ok = new JButton("OK");
        cancel = new JButton("Cancel");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (calibration == 1) {
                    view.getImagePanel().calibrateScalesHorizontally();
                } else if (calibration == 2) {
                    view.getImagePanel().calibrateScaleVertically();
                }
                setVisible(false);
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                calibration = 0;
                setVisible(false);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(ok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                        .addComponent(cancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancel)
                    .addComponent(ok))
                .addContainerGap())
        );

        organizeDialog();
    }

    private void ask() {
        organizeDialog();
        setVisible(true);
    }

    public void askHorizontalCalibration() {
        this.calibration = 1;
        ask();
    }

    public void askVerticalCalibration() {
        this.calibration = 2;
        ask();
    }

    private void organizeDialog() {
        pack();
        int x = view.getLocation().x + view.getWidth() / 2 - getWidth() / 2;
        int y = view.getLocation().y + view.getHeight() / 2 - getHeight() / 2;
        this.setLocation(x, y);
    }

}
