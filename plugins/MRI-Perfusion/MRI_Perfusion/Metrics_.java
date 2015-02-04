/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MRI_Perfusion;

import classes.PerfusionMetrics;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.gui.GenericDialog;

/**
 * PlugIn para cálculo de métricas em sequências de imagens de perfusão em MRI.
 * Calcula as métricas: 1. Integral sob a curva temporal. 2. Tempo ao pico de
 * contraste 3. Valor de pico
 *
 * @author Antonio
 */
public class Metrics_ implements PlugIn {

    @Override
    public void run(String string) {

        GenericDialog gd = new GenericDialog("Perfusion Metrics");
        gd.addMessage("Chose the tables to generate:");
        gd.addCheckbox("CBV", true);
        gd.addCheckbox("MTT", true);
        gd.addCheckbox("CBF", true);
        gd.addCheckbox("Time to Peak", true);
        gd.addCheckbox("Peak Value", true);
        gd.addMessage("Show central ROI pixel graph?");
        gd.addCheckbox("Pixel graph", false);
        gd.addMessage("Show all ROI pixel data table?");
        gd.addCheckbox("Pixel vs Time", false);
        gd.showDialog();
        if (gd.wasCanceled()) {
            return;
        }
        boolean[] choices = new boolean[7];
        for (int b = 0; b < choices.length; b++) {
            choices[b] = gd.getNextBoolean();
        }

        ImagePlus img = WindowManager.getCurrentImage();
        if (img == null) {
            IJ.showMessage("Error", "There are no image open");
            return;
        }

        if (!img.isHyperStack()) {
            IJ.showMessage("Error", "You must convert image stack to a HyperStack.\nClick on the Create HyperStack plugin first.");
            return;
        }

        Roi r = img.getRoi();
        if (r == null) {
            IJ.showMessage("Perfusion ROI Metrics", "Please, create a ROI into perfusion image data set");
            return;
        }

        //Calculation: All pixel intensity in time. Creation of the main result table.
        ResultsTable data = PerfusionMetrics.fluxPixelTable(img, r);

        if (choices[6]) {
            data.show("Pixel Intensity versus Time");
        }
        if (choices[0]) {
            //Calculation: CBV
            PerfusionMetrics.CBV(data, img).show("CBV");
        }
        if (choices[1]) {
//        Calculation: MTT
            PerfusionMetrics.MTT(data, img).show("MTT");
        }
        if (choices[2]) {
//        Calculation: CBF = CBV/MTT
            PerfusionMetrics.CBF(data, img).show("CBF");
        }
        if (choices[3]) {
            //Calculation: Time to Peak
            PerfusionMetrics.time2Peak(data, img).show("Time to Peak");
        }
        if (choices[4]) {
            //        Calculation: Peak Value
            PerfusionMetrics.peakValue(data).show("Peak Value");
        }
        if (choices[5]) {
//        Plotting mean values
            PerfusionMetrics.plotData(data);
        }

    }

}
