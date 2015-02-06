/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Noise_Generator;

import classes.ImageAccess;
import classes.Noise;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antonio
 */
public class Noise_Generator_ implements PlugInFilter {
//TODO Create 16-bits output

    ImageProcessor ip;
    ImageAccess noise;
    ImageAccess img;
    Noise n = new Noise();
    String[] noiseTypes = {"Uniform", "Salt and Pepper", "Gaussian", "Raylegh", "Pink", "Blue", "Purple", "Brown"};
    String[] pixelDepth = {"8-bits Gray", "32-bits Gray"};
    String imgTitle;

    @Override
    public int setup(String string, ImagePlus ip) {

        if (ip != null) {
            imgTitle = ip.getTitle();
            img = new ImageAccess(ip.getProcessor());
            return DOES_8G + DOES_16 + DOES_32;
        }
        return 0;
    }

    @Override
    public void run(ImageProcessor ip) {

        try {
            useAddImageDialog();
        } catch (InterruptedException ex) {
            Logger.getLogger(Noise_Generator_.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void useAddImageDialog() throws InterruptedException {
        noise = new ImageAccess(WindowManager.getCurrentImage().getProcessor());
        GenericDialog gd = new GenericDialog("Noise Generator");
        gd.setOKLabel("Add Noise");
        gd.addChoice("Noise type", noiseTypes, noiseTypes[0]);
        gd.addNumericField("Intensity (%)", 5, 3);
        gd.addChoice("Pixel depth", pixelDepth, pixelDepth[0]);

        gd.showDialog();

        if (gd.wasCanceled()) {
            return;
        }

        if (gd.wasOKed()) {
            int nx = img.getWidth();
            int ny = img.getHeight();
            int nType = gd.getNextChoiceIndex();
            double nIntensity = gd.getNextNumber();
            String pixel = pixelDepth[gd.getNextChoiceIndex()];

            if (pixel.equals(pixelDepth[0])) {
                switch (nType) {
                    case 0:
                        nIntensity *= 2.55;
                        noise = n.generateUniformNoise(noise, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Uniform Noise", img.createByteProcessor()).show();
                        break;
                    case 1:
                        noise = n.generateSaltAndPepperNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Salt and Pepper Noise", img.createByteProcessor()).show();
                        break;
                    case 2:
                        noise = n.generateGaussianNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Gaussian Noise", img.createByteProcessor()).show();
                        break;
                    case 3:
                        noise = n.generateRayleighNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Rayleigh Noise", img.createByteProcessor()).show();
                        break;
                    case 4:
                        noise = n.generatefNoise(img, 1.0, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Pink Noise", img.createByteProcessor()).show();
                        break;
                    case 5:
                        noise = n.generatefNoise(img, -1.0, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Blue Noise", img.createByteProcessor()).show();
                        break;
                    case 6:
                        noise = n.generatefNoise(img, -2.0, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Purple Noise", img.createByteProcessor()).show();
                        break;
                    case 7:
                        noise = n.generatefNoise(img, 2.0, nIntensity).duplicate();
                        ip = noise.createByteProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Brown Noise", img.createByteProcessor()).show();
                        break;
                }

            } else if (pixel.equals(pixelDepth[1])) {
                switch (nType) {
                    case 0:
                        nIntensity *= 2.55;
                        noise = n.generateUniformNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Uniform Noise", img.createFloatProcessor()).show();
                        break;
                    case 1:
                        noise = n.generateSaltAndPepperNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Salt and Pepper Noise", img.createFloatProcessor()).show();
                        break;
                    case 2:
                        noise = n.generateGaussianNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Gaussian Noise", img.createFloatProcessor()).show();
                        break;
                    case 3:
                        noise = n.generateRayleighNoise(nx, ny, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Rayleigh Noise", img.createFloatProcessor()).show();
                        break;
                    case 4:
                        noise = n.generatefNoise(img, 1.0, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Pink Noise", img.createFloatProcessor()).show();
                        break;
                    case 5:
                        noise = n.generatefNoise(img, -1.0, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Blue Noise", img.createFloatProcessor()).show();
                        break;
                    case 6:
                        noise = n.generatefNoise(img, -2.0, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Purple Noise", img.createFloatProcessor()).show();
                        break;
                    case 7:
                        noise = n.generatefNoise(img, 2.0, nIntensity).duplicate();
                        ip = noise.createFloatProcessor();
                        ip.abs();
                        noise = new ImageAccess(ip);
                        img.add(img, noise);
                        new ImagePlus(imgTitle + " plus Brown Noise", img.createFloatProcessor()).show();
                        break;
                }
            }

        }
    }

}
