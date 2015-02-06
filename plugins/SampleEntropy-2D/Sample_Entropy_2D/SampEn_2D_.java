/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sample_Entropy_2D;

import classes.ImageAccess;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import classes.SampEn2D;
import ij.gui.GenericDialog;
import ij.measure.ResultsTable;
import ij.IJ;

/**
 *
 * @author antonio
 */
public class SampEn_2D_ implements PlugInFilter {

    private final String[] labelsCheckbox = {"Mean", "Standard Dev.", "Minimum", "Maximum", "Sample Entropy"};
    private final boolean[] defaultCheckbox = {true, true, true, true, true};
    private ResultsTable table = new ResultsTable();
    ImagePlus img;
    SampEn2D se = new SampEn2D();
    int m;
    double r;

    @Override
    public int setup(String string, ImagePlus ip) {
        img = ip.duplicate();

        return DOES_16 + DOES_8G;
    }

    @Override
    public void run(ImageProcessor ip) {
        if (img.getNSlices() > 1) {
            GenericDialog gd = new GenericDialog("Sample Entropy 2D");
            gd.addMessage("Set the measurements:");
            gd.addCheckboxGroup(3, 2, labelsCheckbox, defaultCheckbox);
            gd.addNumericField("m", 1.0, 3);
            gd.addNumericField("r", 0.3, 3);
            gd.addCheckbox("Use the standard deviation as a threshold delimiter?", true);
            gd.addCheckbox("Apply to all the images?", false);
            gd.showDialog();

            if (gd.wasCanceled()) {
                return;
            }

            if (gd.wasOKed()) {
                boolean meanChoice = gd.getNextBoolean();
                boolean stdChoice = gd.getNextBoolean();
                boolean minChoice = gd.getNextBoolean();
                boolean maxChoice = gd.getNextBoolean();
                boolean seChoice = gd.getNextBoolean();
                boolean doStdThresholdChoice = gd.getNextBoolean();
                boolean doAllStackChoice = gd.getNextBoolean();

                m = (int) gd.getNextNumber();
                r = gd.getNextNumber();
                table.setPrecision(4);

                if (doAllStackChoice) {
                    for (int slice = 1; slice <= img.getNSlices(); slice++) {
                        IJ.showProgress(slice, img.getNSlices());
                        IJ.showStatus("Calculating SampEn 2D metrics...Slice " + slice + "...please wait");
                        img.setSlice(slice);

                        table.incrementCounter();
                        if (meanChoice) {
                            table.addValue("Mean", img.getProcessor().getStatistics().mean);
                        }
                        if (stdChoice) {
                            table.addValue("Std", img.getProcessor().getStatistics().stdDev);
                        }
                        if (minChoice) {
                            table.addValue("Minimum", img.getProcessor().getStatistics().min);
                        }
                        if (maxChoice) {
                            table.addValue("Maximum", img.getProcessor().getStatistics().max);
                        }
                        if (seChoice && doStdThresholdChoice) {
                            table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r * img.getProcessor().getStatistics().stdDev));
                        } else {
                            table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r));
                        }

                    }
                } else {
                    IJ.showStatus("Calculating SampEn 2D metrics...please wait");
                    table.incrementCounter();
                    if (meanChoice) {
                        table.addValue("Mean", img.getProcessor().getStatistics().mean);
                    }
                    if (stdChoice) {
                        table.addValue("Std", img.getProcessor().getStatistics().stdDev);
                    }
                    if (minChoice) {
                        table.addValue("Minimum", img.getProcessor().getStatistics().min);
                    }
                    if (maxChoice) {
                        table.addValue("Maximum", img.getProcessor().getStatistics().max);
                    }
                    if (seChoice && doStdThresholdChoice) {
                        table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r * img.getProcessor().getStatistics().stdDev));
                    } else {
                        table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r));
                    }
                }

                table.show("Results - SampEn2D");
            }
        } else {
            GenericDialog gd = new GenericDialog("Sample Entropy 2D");
            gd.addMessage("Set the measurements:");
            gd.addCheckboxGroup(3, 2, labelsCheckbox, defaultCheckbox);
            gd.addNumericField("m", 1.0, 3);
            gd.addNumericField("r", 0.3, 3);
            gd.addCheckbox("Use the standard deviation as a threshold delimiter?", true);
            gd.showDialog();

            if (gd.wasCanceled()) {
                return;
            }

            if (gd.wasOKed()) {
                boolean meanChoice = gd.getNextBoolean();
                boolean stdChoice = gd.getNextBoolean();
                boolean minChoice = gd.getNextBoolean();
                boolean maxChoice = gd.getNextBoolean();
                boolean seChoice = gd.getNextBoolean();
                boolean doStdThresholdChoice = gd.getNextBoolean();

                m = (int) gd.getNextNumber();
                r = gd.getNextNumber();
                table.setPrecision(4);

                IJ.showStatus("Calculating SampEn 2D metrics...please wait");

                table.incrementCounter();
                if (meanChoice) {
                    table.addValue("Mean", img.getProcessor().getStatistics().mean);
                }
                if (stdChoice) {
                    table.addValue("Std", img.getProcessor().getStatistics().stdDev);
                }
                if (minChoice) {
                    table.addValue("Minimum", img.getProcessor().getStatistics().min);
                }
                if (maxChoice) {
                    table.addValue("Maximum", img.getProcessor().getStatistics().max);
                }
                if (seChoice && doStdThresholdChoice) {
                    table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r * img.getProcessor().getStatistics().stdDev));
                } else {
                    table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r));
                }

                table.show("Results - SampEn2D");
            }

        }

    }
}
