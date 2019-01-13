package tk.kiritofeng.analysis;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class GUI extends JFrame {

    ImagePane img;
    JButton colButton, fileButton, start;
    JSlider tolSlider;
    JTextField tolDisplay;
    Color col;
    int tol;


    public GUI() {
        init();
        addActionListeners();
        setPreferredSize(new Dimension(400,300));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
        img = new ImagePane();
        col = Color.RED;
        setLayout(new BorderLayout());
        tol=255;
        tolSlider = new JSlider(JSlider.VERTICAL, 0, 255, tol);
        Dimension d = tolSlider.getMaximumSize();
        d.height = Integer.MAX_VALUE;
        tolSlider.setMaximumSize(d);
        tolDisplay = new JTextField(String.valueOf(tol));
        d = tolDisplay.getMaximumSize();
        d.height = tolDisplay.getPreferredSize().height;
        tolDisplay.setMaximumSize(d);
        colButton = new JButton("Colour");
        fileButton = new JButton("Choose Input File");
        start = new JButton("Start");
        add(img, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        buttons.add(colButton);
        buttons.add(fileButton);
        buttons.add(start);
        add(buttons, BorderLayout.SOUTH);
        JPanel slider = new JPanel();
        slider.setLayout(new BoxLayout(slider, BoxLayout.Y_AXIS));
        slider.add(tolSlider);
        slider.add(tolDisplay);
        add(slider, BorderLayout.EAST);
        setTitle("Single Slit Analyzer");
    }

    private void addActionListeners() {
        ActionListener btnlistener = new BtnListener();
        TolListener tollistener = new TolListener();
        colButton.addActionListener(btnlistener);
        fileButton.addActionListener(btnlistener);
        start.addActionListener(btnlistener);
        tolSlider.addChangeListener(tollistener);
    }

    public TreeMap<Double, Double> analyse() {
        BufferedImage toAnalyze = img.getImg();
        ArrayList<Point> points = new ArrayList<>();
        for(int i = 0; i < img.getHeight(); ++i) {
            for(int j = 0; j < img.getWidth(); ++j) {
                int rgb = toAnalyze.getRGB(i, j);
                if(Utils.isSimilar(new Color(rgb), col, tol)) {
                    points.add(new Point(i,j));
                }
            }
        }

        // regress all points
        double sumY = 0.0, sumX = 0.0, sumXY = 0.0, sumX2 = 0.0, sumY2 = 0.0;
        for(Point P : points) {
            sumY += P.y;
            sumX += P.x;
            sumXY += 1.0 * P.x * P.y;
            sumX2 += 1.0 * P.x * P.x;
            sumY2 += 1.0 * P.y * P.y;
        }
        double a = (sumY * sumX2 - sumX * sumXY) / (points.size() * sumX2 - sumX * sumX);
        double b = (points.size() * sumXY - sumX * sumY) / (points.size() * sumX2 - sumX * sumX);

        TreeMap<Double, Double> intensities = new TreeMap<>();
        for(Point P: points) {
            // Take dot product to get projection
            double loc = P.x + (P.y - a) * b;
            intensities.put(loc, intensities.getOrDefault(loc, 0.0) + Utils.getIntensity(new Color(toAnalyze.getRGB(P.x, P.y))));
        }

        return intensities;
    }

    public void printOutput(File F, TreeMap<Double, Double> data) throws IOException {
        if(data.isEmpty()) return;
        double first = data.firstKey();
        FileWriter fw = new FileWriter(F);
        PrintWriter pw = new PrintWriter(fw);
        for(Map.Entry<Double, Double> entry: data.entrySet()) {
            pw.printf("%f,%f\n",entry.getKey() - first,entry.getValue());
        }
        pw.close();
    }


    class BtnListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == colButton) {
                JColorChooser.showDialog(null, "Choose Source Colour", col);
            } else if(ae.getSource() == fileButton) {
                JFileChooser inputFile = new JFileChooser();
                inputFile.setDialogType(JFileChooser.SAVE_DIALOG);
                inputFile.setDialogTitle("Open...");
                inputFile.setFileFilter(new FileNameExtensionFilter("Images", "bmp", "jpg", "png", "raw", "tiff", "gif"));
                if(inputFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    img.setImg(Utils.openImage(inputFile.getSelectedFile()));
                }
            } else if(ae.getSource() == start) {
                JFileChooser outputFile = new JFileChooser();
                outputFile.setDialogType(JFileChooser.SAVE_DIALOG);
                outputFile.setDialogTitle("Save As...");
                outputFile.setFileFilter(new FileNameExtensionFilter("CSV File", "csv"));
                if(outputFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File F = outputFile.getSelectedFile();
                    try {
                        printOutput(F, analyse());
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    class TolListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            tol = source.getValue();
            tolDisplay.setText(String.valueOf(tol));
        }
    }
}
