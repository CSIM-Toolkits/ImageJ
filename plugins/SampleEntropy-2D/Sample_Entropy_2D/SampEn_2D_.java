/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sample_Entropy_2D;

import classes.ImageAccess;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import classes.BasicStatistic;
import classes.SampEn2D;

/**
 *
 * @author antonio
 */
public class SampEn_2D_ implements PlugInFilter {

    @Override
    public int setup(String string, ImagePlus ip) {

        return DOES_16 + DOES_8G + DOES_STACKS;
    }

    @Override
    public void run(ImageProcessor ip) {
        SampEn2D se = new SampEn2D();
//        se.
    }

    

}
