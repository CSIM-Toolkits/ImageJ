/*
 * Two-dimensional Sample Entropy
 *
 * 
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Creative Common v4.0 License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * SampEn2D - Two-dimensional Sample Entropy
 * 
 * This is a simple GUI to apply SampEn2D algorithm over digital images.
 * At the moment, the GUI offer the possibility to calculate over a single 2D image or over a 2D image stack
 * If you have an image stack, the results table will show up following the slice order. Additionally, if you want to 
 * calculate the SampEn2D over one single slice, leave the "Apply to all the images?" unchecked.
 *  
 */
public class SampEn_2D_ implements PlugInFilter {
       
    private ResultsTable table = new ResultsTable();
    ImagePlus img;
    SampEn2D se = new SampEn2D();
    int m;
    double r;

    @Override
    public int setup(String string, ImagePlus ip) {
        if (ip == null) {
            IJ.noImage();
            return 0;
        }
        img = ip.duplicate();

        return DOES_16 + DOES_8G;
    }

    @Override
    public void run(ImageProcessor ip) {
        if (img.getNSlices() > 1) {
            GenericDialog gd = new GenericDialog("Sample Entropy 2D");
            gd.addMessage("Parameters set:");
            gd.addNumericField("m", 1.0, 3);
            gd.addNumericField("r", 0.3, 3);
            gd.addCheckbox("Use the standard deviation as a threshold delimiter?", true);
            gd.addCheckbox("Apply to all the images?", false);
            gd.showDialog();

            if (gd.wasCanceled()) {
                return;
            }

            if (gd.wasOKed()) {
                boolean doStdThresholdChoice = gd.getNextBoolean();
                boolean doAllStackChoice = gd.getNextBoolean();

                m = (int) gd.getNextNumber();
                r = gd.getNextNumber();
                table.setPrecision(8);

                if (doAllStackChoice) {
                    for (int slice = 1; slice <= img.getNSlices(); slice++) {
                        IJ.showProgress(slice, img.getNSlices());
                        IJ.showStatus("Calculating SampEn2D...Slice " + slice + "...please wait");
                        img.setSlice(slice);

                        table.incrementCounter();
                            if (doStdThresholdChoice) { 
                           table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r * img.getProcessor().getStatistics().stdDev));
                        } else {
                            table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r));
                        }

                    }
                } else {
                    IJ.showStatus("Calculating SampEn 2D metrics...please wait");
                    table.incrementCounter();
                        if (doStdThresholdChoice) {
                        table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r * img.getProcessor().getStatistics().stdDev));
                    } else {
                        table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r));
                    }
                }

                table.show("Results - SampEn2D");
            }
        } else {
            GenericDialog gd = new GenericDialog("Sample Entropy 2D");
            gd.addMessage("Parameters set:");
            gd.addNumericField("m", 1.0, 3);
            gd.addNumericField("r", 0.3, 3);
            gd.addCheckbox("Use the standard deviation as a threshold delimiter?", true);
            gd.showDialog();

            if (gd.wasCanceled()) {
                return;
            }

            if (gd.wasOKed()) {
                boolean doStdThresholdChoice = gd.getNextBoolean();

                m = (int) gd.getNextNumber();
                r = gd.getNextNumber();
                table.setPrecision(8);

                IJ.showStatus("Calculating SampEn2D...please wait");

                table.incrementCounter();
                     if (doStdThresholdChoice) {
                    table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r * img.getProcessor().getStatistics().stdDev));
                } else {
                    table.addValue("SampEn2D", se.fastSampleEn2D(new ImageAccess(img.getProcessor()), m, r));
                }

                table.show("Results - SampEn2D");
            }

        }

    }
}
