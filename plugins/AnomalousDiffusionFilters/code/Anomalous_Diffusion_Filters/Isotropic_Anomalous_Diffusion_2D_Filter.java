package Anomalous_Diffusion_Filters;

import classes.Diffusion;
import classes.ImageAccess;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author antonio
 */
public class Isotropic_Anomalous_Diffusion_2D_Filter implements PlugIn {
//TODO Aprimorar plugin para rodar com imagestack, mesmo com processamento 2D. Rever filtro IAD, em questao do uso do parametro delta.

    final int imageDimension = 2;

    String[] choiceItemsEdgeFunction = {"Exponential", "Fractional", "Partial"};
    Diffusion diff = new Diffusion();
    final double delta_limit = 1 / Math.pow(2, imageDimension + 1);
    final double default_q = 1.0, default_Dq = 1.0, default_delta = delta_limit;
    final int default_iterations = 5;
    double q, Dq, delta;
    int iterations;
    boolean stackProcessing = false;

    @Override
    public void run(String string) {
        //Get the image opened in the current ImageJ scene.
        ImagePlus img = WindowManager.getCurrentImage();

        if (img == null) {
            IJ.showMessage("No image open", "There is no image open\nPlease, open an image first");
            return;
        }

        //Test if the image open is not an RGB image.
        if (img.getType() == ImagePlus.COLOR_RGB) {
            IJ.showMessage("Not gray scale image", "The image open is not gray scale\nPlease, convert the image to 8-bits, 16-bits or 32 bits gray scale.");
            return;
        }

        //Test if the image is a stack
        if (img.getNSlices() > 1) {
            GenericDialog stackDialog = new GenericDialog("Image stack processing");
            stackDialog.addMessage("The image is a stack");
            stackDialog.addCheckbox("Apply the filter over all the images?", false);
            stackDialog.showDialog();

            if (stackDialog.wasCanceled()) {
                return;
            }

            if (stackDialog.wasOKed()) {
                stackProcessing = stackDialog.getNextBoolean();
            }
        }

        GenericDialog gd = new GenericDialog("Isotropic Anomalous Diffusion - 2D");
        gd.setOKLabel("Run");

        gd.addNumericField("Anomalous parameter (Q)", default_q, 4);
        gd.addNumericField("Generalized Diffusion Coeff.", default_Dq, 4);
        gd.addNumericField("Time step", default_delta, 4);
        gd.addNumericField("Number of iterations", default_iterations, 1);
        gd.addMessage("If any values were set wrong,\nthe default values will be used.");

        //gd.addCheckbox("Use automatic condutance estimator function", false);
        gd.showDialog();

        //Close diaglog if the user click on the Cancel button.
        if (gd.wasCanceled()) {
            return;
        }

        if (gd.wasOKed()) {

            if (stackProcessing) {
                int NSlices = img.getNSlices();
                ImageStack outStack = new ImageStack(img.getWidth(), img.getHeight());

                q = gd.getNextNumber();
                Dq = gd.getNextNumber();
                delta = gd.getNextNumber();
                iterations = (int) gd.getNextNumber();
                //Check the parameters values set up from the user. If anything is wrong, it is used the default values.
                checkParametersValues();

                diff.setQ(q);
                diff.setD(Dq);
                diff.setDelta(delta);
                diff.setNumInteration(iterations);
                for (int slice = 1; slice <= NSlices; slice++) {
                    img.setSlice(slice);
                    outStack.addSlice(diff.AnomalousIsoDiff(new ImageAccess(img.getProcessor()), Dq).createFloatProcessor());
                }
                new ImagePlus("IAD-Q" + diff.getQ() + "numInt" + diff.getNumInteration() + "timeStep" + diff.getDelta() + "Dq" + diff.getD(), outStack).show();
            } else {
                q = gd.getNextNumber();
                Dq = gd.getNextNumber();
                delta = gd.getNextNumber();
                iterations = (int) gd.getNextNumber();
                //Check the parameters values set up from the user. If anything is wrong, it is used the default values.
                checkParametersValues();

                diff.setQ(q);
                diff.setD(Dq);
                diff.setDelta(delta);
                diff.setNumInteration(iterations);
                int edgeChoice = gd.getNextChoiceIndex() + 1;
                diff.AnomalousIsoDiff(new ImageAccess(img.getProcessor()), Dq).show("IAD-Q" + diff.getQ() + "numInt" + diff.getNumInteration() + "timeStep" + diff.getDelta() + "Dq" + diff.getD());
            }
        }
    }

    private void checkParametersValues() {
        //Test for the anomalous parameter (Q)
        if (q < 0d || q > 2.0) {
            q = default_q;
        }
        //Test for the generalized diffusion coefficient parameter (Dq)
        if (Dq < 0d || Dq > 1.0) {
            Dq = default_Dq;
        }
        //Test for the time step parameter (delta)
        if (delta > delta_limit) {
            delta = default_delta;
        }
        //Test for the number of iterations parameter (t)
        if (iterations < 0) {
            iterations = default_iterations;
        }
    }

}
