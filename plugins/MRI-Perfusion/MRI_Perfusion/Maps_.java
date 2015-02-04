/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MRI_Perfusion;

import classes.PerfusionMaps;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.gui.Roi;
import ij.plugin.PlugIn;

/**
 *
 * @author antonio
 */
public class Maps_ implements PlugIn {

    ImagePlus img;
//    ImagePlus map;
    int nx, ny;

    @Override
    public void run(String string) {

        GenericDialog gd = new GenericDialog("Perfusion Metrics and Maps");
        gd.addMessage("Chose the maps to generate:");
        gd.addCheckbox("CBV maps", true);
        gd.addCheckbox("CBF maps", true);
        gd.addCheckbox("MTT maps", true);
        gd.showDialog();
        if (gd.wasCanceled()) {
            return;
        }
        boolean[] choices = new boolean[3];
        for (int b = 0; b < choices.length; b++) {
            choices[b] = gd.getNextBoolean();
        }

        if (WindowManager.getCurrentImage() == null) {
            IJ.showMessage("Import Image", "There are no images open\nImport DICOM perfusion images");
            return;
        } else {
            img = WindowManager.getCurrentImage();
//            map = img.duplicate();
        }

        if (!img.isHyperStack()) {
//            int nSlicesByFrame = (int) Double.parseDouble(DicomTools.getTag(map, "0020,0105"));
//            int nFrames = map.getImageStackSize() / nSlicesByFrame;
//            map.setDimensions(1, nSlicesByFrame, nFrames);
//            map.setTitle(img.getTitle());

            IJ.showMessage("Error", "You must convert image stack to a HyperStack.\nClick on the Create HyperStack plugin first.");
            return;
        }

        Roi r = img.getRoi();
        if (r == null) {
            IJ.showMessage("Perfusion ROI Metrics", "Please, create a ROI into perfusion image data set");
            return;
        }
//        ImagePlus aux = img.duplicate();
//        ResultsTable imageData = PerfusionMetrics.fluxPixelTable(aux, aux.getRoi());
        if (choices[0]) {
            //          CBV Map
            PerfusionMaps.createPerfusionMap(img, img.getFrame(), PerfusionMaps.Methods.CBV, r);
        }
        if (choices[2]) {
            //          MTT Map
            PerfusionMaps.createPerfusionMap(img, img.getFrame(), PerfusionMaps.Methods.MTT, r);
        }
        if (choices[1] && choices[0] == true && choices[2] == true) {
            //          CBF Map
            ImagePlus cbv, mtt, cbf;
            cbv = WindowManager.getImage("CBV Map").duplicate();
            mtt = WindowManager.getImage("MTT Map").duplicate();
            cbf = PerfusionMaps.divide(cbv, mtt);
            cbf.setTitle("CBF Map");
            cbf.show();
        } else if (choices[1]) {
            PerfusionMaps.createPerfusionMap(img, img.getFrame(), PerfusionMaps.Methods.CBF, r);
        }

    }

}
