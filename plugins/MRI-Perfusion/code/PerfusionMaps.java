/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Perfusion_v1_1;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.NewImage;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.ContrastEnhancer;
import ij.process.ImageProcessor;
import ij.process.LUT;
import ij.util.DicomTools;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.IndexColorModel;

/**
 *
 * @author antonio
 */
public class PerfusionMaps extends PerfusionMetrics {

    private static void cutBackground(ImagePlus imgWithROI, ImagePlus roiBase, Roi roi, Methods method) {
        int nx = roiBase.getWidth(), ny = roiBase.getHeight();
        ImageProcessor mask = roi.getMask();
        Rectangle rec = roi.getBounds();
        roiBase = new ImagePlus("", roiBase.getProcessor().createProcessor(nx, ny));

        for (int x = 0; x < rec.width; x++) {
            for (int y = 0; y < rec.height; y++) {
                if (mask == null || mask.getPixel(x, y) != 0) {
                    roiBase.getProcessor().putPixelValue(x + rec.x, y + rec.y, imgWithROI.getProcessor().getPixelValue(x + rec.x, y + rec.y));
                }
            }
        }
        WindowManager.setTempCurrentImage(roiBase);
        IJ.run("Jet");
        new ContrastEnhancer().stretchHistogram(roiBase.getProcessor(), 0.5);
        
        switch(method){
            case CBV:
                roiBase.setTitle("CBV Map");
                roiBase.show();
                break;
            case MTT:
                roiBase.setTitle("MTT Map");
                roiBase.show();
                break;
            case CBF:
                roiBase.setTitle("CBF Map");
                roiBase.show();
                break;
            case T2P:
                roiBase.setTitle("Time to Peak Map");
                roiBase.show();
                break;
        }
                      
        
//        for (int x = 0; x < nx; x++) {
//            for (int y = 0; y < ny; y++) {
//                if(!(roiBase.getProcessor().getPixelValue(x, y)==0)){
//                    imgWithROI.getProcessor().putPixelValue(x, y, roiBase.getProcessor().getPixelValue(x, y));
//                }
//            }
//        }
    }

    public enum Methods {

        CBV,
        CBF,
        MTT,
        T2P;
    }

    public static void createPerfusionMap(ImagePlus img, int frame, Methods method, Roi roi) {
        ImageProcessor mask = roi.getMask();
        Rectangle rec = roi.getBounds();
        ImageProcessor ip = img.getProcessor();
        ImagePlus aux = img.duplicate();
        ImagePlus roi_base = new ImagePlus("", ip).duplicate();
        ImagePlus img_All = new ImagePlus("", ip).duplicate();
        int nx = img.getWidth();
        int ny = img.getHeight();
        int nSlice = (int) Double.parseDouble(DicomTools.getTag(img, "0020,0105"));
        double[] pixelData = new double[nSlice];
//        ImageStack perfusionMetricVolume = new ImageStack(nx, ny);
        for (int x = 0; x < rec.width; x++) {
            IJ.showStatus("Perfusion " + method.toString() + " for all image...please wait");
            IJ.showProgress(x, rec.width);
            for (int y = 0; y < rec.height; y++) {
                if (mask == null || mask.getPixel(x, y) != 0) {
                    if (ip.getPixelValue(x + rec.x, y + rec.y) == 0.0) {
                        aux.getProcessor().putPixelValue(x + rec.x, y + rec.y, 0d);
                        roi_base.getProcessor().putPixelValue(x + rec.x, y + rec.y, 0d);
                    } else {
                        aux.getProcessor().putPixelValue(x + rec.x, y + rec.y, getPerfusionMeasure(pixelData, img, x + rec.x, y + rec.y, frame, nSlice, method));
                        roi_base.getProcessor().putPixelValue(x + rec.x, y + rec.y, getPerfusionMeasure(pixelData, img, x + rec.x, y + rec.y, frame, nSlice, method));
                    }
                }
            }
        }

//        perfusionMetricVolume.addSlice(roi_base.getProcessor());
        switch (method) {
            case CBV:
                ImagePlus cbv = new ImagePlus("CBV Maps", roi_base.getProcessor());
                cbv.setDisplayRange(cbv.getProcessor().getMin(), cbv.getProcessor().getMax());
                new ContrastEnhancer().stretchHistogram(cbv.getProcessor(), 0.5);
                //cbv.getProcessor().setLut(LUT.);
                //String m = "setBatchMode(true); run(\"Blobs (25K)\"); run(\"Fire\");"; 
                cutBackground(roi_base, img_All, roi, method);
//                WindowManager.setTempCurrentImage(img_All);
//                IJ.run("Jet");
                //IJ.runMacro(m);
//                img_All.show();
                break;
            case CBF:
                ImagePlus cbf = new ImagePlus("CBF Maps", roi_base.getProcessor());
                cbf.setDisplayRange(cbf.getProcessor().getMin(), cbf.getProcessor().getMax());
                new ContrastEnhancer().stretchHistogram(cbf.getProcessor(), 0.5);
//                cbv.getProcessor().setLut(LUT.createLutFromColor());
                cutBackground(roi_base, img_All, roi, method);
                break;
            case MTT:
                ImagePlus mtt = new ImagePlus("MTT Maps", roi_base.getProcessor());
                mtt.setDisplayRange(mtt.getProcessor().getMin(), mtt.getProcessor().getMax());
                new ContrastEnhancer().stretchHistogram(mtt.getProcessor(), 0.5);
//                cbv.getProcessor().setLut(LUT.createLutFromColor());
                cutBackground(roi_base, img_All, roi, method);
                break;
            case T2P:
                ImagePlus t2p = new ImagePlus("Time to Peak Maps", roi_base.getProcessor());
                t2p.setDisplayRange(t2p.getProcessor().getMin(), t2p.getProcessor().getMax());
                new ContrastEnhancer().stretchHistogram(t2p.getProcessor(), 0.5);
//                cbv.getProcessor().setLut(LUT.createLutFromColor());
                cutBackground(roi_base, img_All, roi, method);
                break;
        }

    }

    private static double getPerfusionMeasure(double[] pixelData, ImagePlus img, int x, int y, int frame, int nSlices, Methods method) {
        double measure = 0d;

        switch (method) {
            case CBV:
                IJ.showStatus("Creating CBV Map...please wait");
                for (int slice = 1; slice <= nSlices; slice++) {
                    img.setSlice(slice + (frame - 1) * nSlices);
                    pixelData[slice - 1] = img.getProcessor().getPixelValue(x, y);
                }
                measure = calcCBV(pixelData, img);
                break;
            case CBF:
                IJ.showStatus("Creating CBF Map...please wait");
                for (int slice = 1; slice <= nSlices; slice++) {
                    img.setSlice(slice + (frame - 1) * nSlices);
                    pixelData[slice - 1] = img.getProcessor().getPixelValue(x, y);
                }
                measure = calcCBF(pixelData, img);
                break;
            case MTT:
                IJ.showStatus("Creating MTT Map...please wait");
                for (int slice = 1; slice <= nSlices; slice++) {
                    img.setSlice(slice + (frame - 1) * nSlices);
                    pixelData[slice - 1] = img.getProcessor().getPixelValue(x, y);
                }
                measure = calcMTT(pixelData, img);
                break;
//            case T2P:
//                for (int slice = 1; slice <= nSlices; slice++) {
//            img.setSlice(slice + (frame - 1) * nSlices);
//            pixelData[slice-1] = img.getProcessor().getPixelValue(x, y);
//        }
//            measure = time2Peak(null, img)
        }

        return measure;
    }

    public static ImagePlus divide(ImagePlus cbv, ImagePlus mtt) {
        ImagePlus result = cbv.duplicate();

        for (int x = 0; x < cbv.getWidth(); x++) {
            for (int y = 0; y < mtt.getHeight(); y++) {
                result.getProcessor().putPixelValue(x, y, cbv.getProcessor().getPixelValue(x, y) / mtt.getProcessor().getPixelValue(x, y));
            }
        }

        return result;
    }
}
