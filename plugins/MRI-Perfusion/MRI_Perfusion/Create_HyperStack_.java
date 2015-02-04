/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MRI_Perfusion;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.PlugIn;
import ij.util.DicomTools;

/**
 *
 * @author antonio
 */
public class Create_HyperStack_ implements PlugIn{

    @Override
    public void run(String arg) {
        ImagePlus img = WindowManager.getCurrentImage();
        if (img == null) {
            IJ.showMessage("Error", "There are no image open");
            return;
        }
        
        int nSlicesByFrame = (int)Double.parseDouble(DicomTools.getTag(img, "0020,0105"));
        int nFrames = img.getImageStackSize()/nSlicesByFrame;
        img.setDimensions(1, nSlicesByFrame, nFrames);
        ImagePlus out = img.duplicate();
        out.setTitle(img.getTitle());
        img.close();
        out.setOpenAsHyperStack(true);
        out.show();
    }
    
}
