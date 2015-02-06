/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import classes.ImageAccess;

/**
 *
 * @author antonio
 */
public class Morphology {

    /**
     * Implements "dilation" method for 8-connected pixels of an ImageAccess
     * object. For each pixel, the maximum value of the gray levels of its 3x3
     * local neighborhood which is 8-connected is found.
     *
     * @param img an ImageAccess object
     */
    public static ImageAccess doDilation(ImageAccess img) {
        int nx = img.getWidth();
        int ny = img.getHeight();
        ImageAccess out = new ImageAccess(nx, ny);
        double arr[] = new double[9];
        double max;

        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                img.getPattern(x, y, arr, ImageAccess.PATTERN_SQUARE_3x3);
                max = arr[0];
                for (int k = 1; k < 9; k++) {
                    if (arr[k] > max) {
                        max = arr[k];
                    }
                }
                out.putPixel(x, y, max);
            }
        }
        return out;
    }

    static public ImageAccess doErosion(ImageAccess img) {
        int nx = img.getWidth();
        int ny = img.getHeight();
        ImageAccess out = new ImageAccess(nx, ny);
        double arr[] = new double[9];
        double min;

        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                img.getPattern(x, y, arr, ImageAccess.PATTERN_SQUARE_3x3);
                min = arr[0];
                for (int k = 1; k < 9; k++) {
                    if (arr[k] < min) {
                        min = arr[k];
                    }
                }
                out.putPixel(x, y, min);
            }
        }
        return out;
    }

    /**
     * Abertura: (f - s) + s
     *
     * @param img
     * @return
     */
    public static ImageAccess doOpen(ImageAccess img) {
        ImageAccess aux = doErosion(img);

        return doDilation(aux);
    }

    /**
     * Fechamento: (f + s) - s
     *
     * @param img
     * @return
     */
    public static ImageAccess doClose(ImageAccess img) {
        ImageAccess aux = doDilation(img);

        return doErosion(aux);
    }
}
