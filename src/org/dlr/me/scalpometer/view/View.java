/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlr.me.scalpometer.view;

import java.awt.Container;
import org.dlr.me.scalpometer.controller.Controller;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Locale;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.dlr.me.scalpometer.controller.StatisticUnit;
import org.dlr.me.scalpometer.model.FileSaver;

/**
 *
 * @author Matthias Bremser
 */
public class View extends JFrame {
    private final Controller controller;
    private ImagePanel imagePanel;
    private JPanel toolPanel;
    private JMenuBar jMenuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenuItem exitMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem takeScaleHorizontally;
    private JMenuItem takeScaleVertically;
    private JButton blockBezier1;
    private JButton blockBezier2;
    private JButton measure;
    final private JFileChooser jFileChooser = new JFileChooser();
    final private DecimalFormat df = (DecimalFormat)DecimalFormat.getInstance(Locale.GERMAN);
    private FileSaver fileSaver = new FileSaver();
    private javax.swing.JScrollPane measureInfoAreaScrollPane;
    private javax.swing.JTextArea measurementInfoArea;
    private javax.swing.JTextField numberOfMeasurements;
    private JTextField scale;
    private JTextField knownDistance;
    private JTextField unit;
    private JLabel imageCount;
    private ScaleInfoDialog scaleInfoDialog;
    private Container bottomBox;
    private Container bottomLeftBox;
    private JButton nextImage;
    private BoxLayout box;
    private DiagramPanel diagramPanel;
    private BoxLayout bottomBoxLayout;
    private BoxLayout bottomLeftBoxLayout;


    public View(Controller controller) {
        super();
        this.controller = controller;
        initComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(1024, 768);
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2);
        this.setVisible(true);
        scaleInfoDialog = new ScaleInfoDialog(this);
        df.applyPattern( "#,###,##0.000" );
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        imagePanel = new ImagePanel(controller, this);
        toolPanel = new JPanel();
        imageCount = new JLabel("Image 0/0");
        jMenuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        exitMenuItem = new JMenuItem("Exit");
        openMenuItem = new JMenuItem("Open");
        takeScaleHorizontally = new JMenuItem("Take scale horizontally");
        takeScaleVertically = new JMenuItem("Take scale vertically");
        blockBezier1 = new JButton("Bezier1");
        blockBezier2 = new JButton("Bezier2");
        measure = new JButton("Measure");
        nextImage = new JButton("Next Image");
        numberOfMeasurements = new JTextField("100");
        scale = new JTextField("100");
        knownDistance = new JTextField("5");
        unit = new JTextField("mm");
        box = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        measureInfoAreaScrollPane = new JScrollPane();
        diagramPanel = new DiagramPanel();
        measurementInfoArea = new JTextArea();
        bottomBox = new Container();
        bottomBoxLayout = new BoxLayout(bottomBox, BoxLayout.X_AXIS);
        bottomBox.setLayout(bottomBoxLayout);
        bottomLeftBox = new Container();
        bottomLeftBoxLayout = new BoxLayout(bottomLeftBox, BoxLayout.Y_AXIS);
        bottomLeftBox.setLayout(bottomLeftBoxLayout);

        setLayout(box);
        toolPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        toolPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);

        BoxLayout toolPanelBox = new BoxLayout(toolPanel, BoxLayout.X_AXIS);
        toolPanel.setLayout(toolPanelBox);
        Dimension toolPanelSize = new Dimension(Integer.MAX_VALUE, 30);
        toolPanel.setPreferredSize(toolPanelSize);
        toolPanel.setMinimumSize(toolPanelSize);
        toolPanel.setMaximumSize(toolPanelSize);

        measurementInfoArea.setColumns(20);
        measurementInfoArea.setRows(5);
        measureInfoAreaScrollPane.setMaximumSize(new Dimension(measurementInfoArea.getWidth(), Integer.MAX_VALUE));
        measureInfoAreaScrollPane.setViewportView(measurementInfoArea);

        getContentPane().add(toolPanel);
        getContentPane().add(bottomBox);
        bottomBox.add(bottomLeftBox);
        bottomBox.add(measureInfoAreaScrollPane);
        bottomLeftBox.add(imagePanel);
        bottomLeftBox.add(diagramPanel);
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        editMenu.add(takeScaleHorizontally);
        editMenu.add(takeScaleVertically);
        jMenuBar.add(fileMenu);
        jMenuBar.add(editMenu);
        toolPanel.add(blockBezier1);
        toolPanel.add(blockBezier2);
        toolPanel.add(imageCount);
        toolPanel.add(nextImage);
        toolPanel.add(new JLabel("Amount of Measurements"));
        toolPanel.add(numberOfMeasurements);
        toolPanel.add(new JLabel("Pixels:"));
        toolPanel.add(scale);
        toolPanel.add(new JLabel("Known distance:"));
        toolPanel.add(knownDistance);
        toolPanel.add(new JLabel("Unit:"));
        toolPanel.add(unit);
        toolPanel.add(measure);


        
        
        setJMenuBar(jMenuBar);
        

        
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //Create a file chooser
                int returnValue = jFileChooser.showOpenDialog(jFileChooser);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    //image = controller.openFile(jFileChooser.getSelectedFile());
                    controller.openFile(jFileChooser.getSelectedFile());
                    imagePanel.resetImageCount();
                    imagePanel.nextImage();
                    imageCount.setText("Image "+(imagePanel.getImageCount() + 1)+"/"+controller.getImageStackHolder().getStack().getSize());
                    //imagePanel.setImage(stack.getImageArray());
                    //setSize(image.getWidth(rootPane) + jScrollPane1.getWidth() + 10,
                    //        image.getHeight(rootPane));
                    repaint();
                } else {
                    // No operation.
                }
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
                dispose();
            }
        });
        takeScaleHorizontally.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                scaleInfoDialog.askHorizontalCalibration();
            }
        });
        takeScaleVertically.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                scaleInfoDialog.askVerticalCalibration();
            }
        });
        blockBezier1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                imagePanel.toggleCurveBlock(1);
                blockBezier1.setText("Bezier1" + imagePanel.getBlocked(1));
            }
        });
        blockBezier2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                imagePanel.toggleCurveBlock(2);
                blockBezier2.setText("Bezier2" + imagePanel.getBlocked(2));
            }
        });
        nextImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (imagePanel.isImageSet()) {
                    imagePanel.nextImage();
                    imageCount.setText("Image: "+(imagePanel.getImageCount()+1)+"/"+controller.getImageStackHolder().getStack().getSize());
                }
            }
        });
        measure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (imagePanel.isImageSet()) {
                    fileSaver.setCurrentFile(jFileChooser.getSelectedFile());
                    String text = "";
                    int amount = Integer.parseInt(numberOfMeasurements.getText());
                    StatisticUnit statUnit;
                    if (amount > 0) {
                        statUnit = imagePanel.measureDistance(amount, 1/(Double.parseDouble(scale.getText())/Double.parseDouble(knownDistance.getText())), unit.getText());
                        diagramPanel.setStatisticalInfo(statUnit);
                        fileSaver.setStatisticUnit(statUnit);
                        if (statUnit != null) {
                            Double[] lineLengths = statUnit.getLineLengthsArray();
                            text = text + "Diameter in "+unit.getText()+":\n";
                            for (int i = 0; i < lineLengths.length; i++) {
                                text = text + df.format(lineLengths[i]) + "\n";
                            }
                            text = text + "\n------------\n\nMean: " + df.format(statUnit.getMean()) + "\nStandardDeviation: " + df.format(statUnit.getStandardDeviation());

                        }
                    }
                    fileSaver.processAndClose();
                    measurementInfoArea.setText(text);
                    repaint();
                }
            }
        });


        /**
         * Add components to frame
         */
   /*     this.setJMenuBar(jMenuBar);
        getContentPane().setLayout(new GridLayout(1,1));
           add(imagePanel);
           add(resultsPanel);


        //this.getContentPane().add(imagePanel);
        //this.getContentPane().add(resultsPanel);
*/
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        measurementInfoArea.setText("");
    }

    ImagePanel getImagePanel() {

        return imagePanel;
    }

    void setScale(int i) {
        scale.setText(String.valueOf(i));
    }


}