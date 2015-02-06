/*
 * FourierTransform.java
 *
 * Created on April 24, 2006, 7:39 PM
 */

package classes;

//TODO Comentar a classe apropriadamente

import classes.ImageAccess;
import ij.IJ;


/**
 * Base class containing functions to transform a signal fron and onto
 * Fourier frequency space and manipulates complex number series.
 * 
 * @author murta
 * 
 * Last Modified: 01/12/2012
 */
public class FourierTransform {
    
    //TODO Rever metods para imagens, reutilizar os ja existentes (como o conversor de coordenadas, pois a classe ja existe)
    
    
    /** Creates a new instance of FourierTransform */
    public FourierTransform() {
    }

    
    public static void ft1D(double[] real, double[] imaginary) throws InterruptedException {

        int m = (int)Math.floor(Math.log(real.length) / Math.log(2D));

        if(Math.pow(2D,m) == real.length)
            doFastFT1D(real,imaginary);  // We can use the Fast Algorithm
        else
            doFT1D(real,imaginary,false);  // Otherwise
    }
    
    private static void doFT1D(double real[], double imaginary[], boolean isInverse) throws InterruptedException {
        int N = real.length;
        double omega = 2.0 * Math.PI / (double)N;
        double realX[] = new double[N];
        double imagX[] = new double[N];
        double factor;

        for(int k=0; k<N; k++) {
            Thread.sleep(0);  // Raises an exception for AnalysisThread
            
            realX[k] = 0.0;
            imagX[k] = 0.0;
        }

        for(int k=0; k<N; k++) {
            for(int n=0; n<N; n++) {
                Thread.sleep(0);  // Raises an exception for AnalysisThread
                
                if(isInverse)
                    factor = omega*k*n;
                else
                    factor = -omega*k*n;
                
                // (real + i*imaginario)(cos + i*sen)
                realX[k] += real[n]*Math.cos(factor) - imaginary[n]*Math.sin(factor);
                imagX[k] += real[n]*Math.sin(factor) + imaginary[n]*Math.cos(factor);
            }
        }

        for(int k=0; k<N; k++) {
            Thread.sleep(0);  // Raises an exception for AnalysisThread
            
            real[k] = realX[k]/N;
            imaginary[k] = imagX[k]/N;
        }

    }
    
    /**
     * Esta sem a divisao por N
     *
     * @param real
     * @param imaginary
     */
    private static void doFastFT1D(double real[], double imaginary[]) throws InterruptedException {
        int shift = 0;
        int size = real.length;
        int m = (int) Math.floor(Math.log(size) / Math.log(2D));
        int n = 1 << m;
//        int n = m;
        double Imarg[] = new double[n];
        double Rearg[] = new double[n];
        double arg0 = 2 * Math.PI / (double)n;
        for(int i = 0; i < n; i++)
        {
            Thread.sleep(0);  // Raises an exception for AnalysisThread
            
            double arg = arg0 * (double)i;
            Rearg[i] = Math.cos(arg);
            Imarg[i] = -Math.sin(arg);
        }

        int j;
        for(int i = j = shift; i < (shift + n) - 1; i++)
        {
            Thread.sleep(0);  // Raises an exception for AnalysisThread
            
            if(i < j)
            {
                double Retmp = real[i];
                double Imtmp = imaginary[i];
                real[i] = real[j];
                imaginary[i] = imaginary[j];
                real[j] = Retmp;
                imaginary[j] = Imtmp;
            }
            int k;
            for(k = n >> 1; k + shift <= j; k /= 2)
                j -= k;

            j += k;
        }

        int stepsize = 1;
        for(int shifter = m - 1; stepsize < n; shifter--)
        {
            for(j = shift; j < shift + n; j += stepsize << 1)
            {
                for(int i = 0; i < stepsize; i++)
                {
                    Thread.sleep(0);  // Raises an exception for AnalysisThread
                    
                    int i_j = i + j;
                    int i_j_s = i_j + stepsize;
                    double Retmp;
                    if(i > 0)
                    {
                        Retmp = Rearg[i << shifter] * real[i_j_s] - Imarg[i << shifter] * imaginary[i_j_s];
                        imaginary[i_j_s] = Rearg[i << shifter] * imaginary[i_j_s] + Imarg[i << shifter] * real[i_j_s];
                        real[i_j_s] = Retmp;
                    }
                    Retmp = real[i_j] - real[i_j_s];
                    double Imtmp = imaginary[i_j] - imaginary[i_j_s];
                    real[i_j] += real[i_j_s];
                    imaginary[i_j] += imaginary[i_j_s];
                    real[i_j_s] = Retmp;
                    imaginary[i_j_s] = Imtmp;
                }

            }

            stepsize <<= 1;
        }


        // 
        for(int k=0; k<size; k++) {
            Thread.sleep(0);  // Raises an exception for AnalysisThread
            
            real[k] = real[k]/size;
            imaginary[k] = imaginary[k]/size;
        }

    }
    
    
    public static void inverseFT1D(double real[], double imaginary[]) throws InterruptedException {
        int m = (int)Math.floor(Math.log(real.length) / Math.log(2D));
        int size = real.length;

        if(Math.pow(2D,m) == real.length) { // We can use the Fast Algorithm

            for(int i=0; i<size; i++)
                imaginary[i] = -imaginary[i];

            doFastFT1D(real,imaginary);
        } else {
            doFT1D(real,imaginary,true);  // Otherwise

        }


        // Correcting the normalization done at FT code
        for(int i= 0; i<size; i++) {
            real[i] = real[i]*size;
            imaginary[i] = -imaginary[i]*size;
        }

    }
    
    
    
    //----
    
    public static void ft2D(ImageAccess real, ImageAccess imaginary) throws InterruptedException {
        int nx = real.getWidth();
        int ny = real.getHeight();
        double reCol[] = new double[ny];
        double imCol[] = new double[ny];
        for (int x = 0; x < nx; x++) {
            real.getColumn(x, reCol);
            imaginary.getColumn(x, imCol);
            ft1D(reCol, imCol);//FFT1D(reCol, imCol);
            real.putColumn(x, reCol);
            imaginary.putColumn(x, imCol);
        }
        
        double reRow[] = new double[nx];
        double imRow[] = new double[nx];
        for (int y = 0; y < ny; y++) {
            real.getRow(y, reRow);
            imaginary.getRow(y, imRow);
            ft1D(reRow, imRow);//doFFT1D(reRow, imRow);
            real.putRow(y, reRow);
            imaginary.putRow(y, imRow);
        }

    }
    
    public static void inverseFT2D(ImageAccess real, ImageAccess imaginary) throws InterruptedException {
        int nx = real.getWidth();
        int ny = real.getHeight();
        double reCol[] = new double[ny];
        double imCol[] = new double[ny];
        for (int x = 0; x < nx; x++) {
            real.getColumn(x, reCol);
            imaginary.getColumn(x, imCol);
            inverseFT1D(reCol, imCol);//inverseFFT1D(reCol, imCol);
            real.putColumn(x, reCol);
            imaginary.putColumn(x, imCol);
        }

        double reRow[] = new double[nx];
        double imRow[] = new double[nx];
        for (int y = 0; y < ny; y++) {
            real.getRow(y, reRow);
            imaginary.getRow(y, imRow);
            inverseFT1D(reRow, imRow);//inverseFFT1D(reRow, imRow);
            real.putRow(y, reRow);
            imaginary.putRow(y, imRow);
        }

    }
    
//    private static void doFFT1D(double real[], double imaginary[]) {
//        int shift = 0;
//        int size = real.length;
//        int m = (int) Math.floor(Math.log(size) / Math.log(2D));
//        int n = 1 << m;
//        double Imarg[] = new double[n];
//        double Rearg[] = new double[n];
//        double arg0 = 2 * Math.PI / (double) n;
//        for (int i = 0; i < n; i++) {
//            double arg = arg0 * (double) i;
//            Rearg[i] = Math.cos(arg);
//            Imarg[i] = -Math.sin(arg);
//        }
//
//        int j;
//        for (int i = j = shift; i < (shift + n) - 1; i++) {
//            if (i < j) {
//                double Retmp = real[i];
//                double Imtmp = imaginary[i];
//                real[i] = real[j];
//                imaginary[i] = imaginary[j];
//                real[j] = Retmp;
//                imaginary[j] = Imtmp;
//            }
//            int k;
//            for (k = n >> 1; k + shift <= j; k /= 2) {
//                j -= k;
//            }
//
//            j += k;
//        }
//
//        int stepsize = 1;
//        for (int shifter = m - 1; stepsize < n; shifter--) {
//            for (j = shift; j < shift + n; j += stepsize << 1) {
//                for (int i = 0; i < stepsize; i++) {
//                    int i_j = i + j;
//                    int i_j_s = i_j + stepsize;
//                    double Retmp;
//                    if (i > 0) {
//                        Retmp = Rearg[i << shifter] * real[i_j_s] - Imarg[i << shifter] * imaginary[i_j_s];
//                        imaginary[i_j_s] = Rearg[i << shifter] * imaginary[i_j_s] + Imarg[i << shifter] * real[i_j_s];
//                        real[i_j_s] = Retmp;
//                    }
//                    Retmp = real[i_j] - real[i_j_s];
//                    double Imtmp = imaginary[i_j] - imaginary[i_j_s];
//                    real[i_j] += real[i_j_s];
//                    imaginary[i_j] += imaginary[i_j_s];
//                    real[i_j_s] = Retmp;
//                    imaginary[i_j_s] = Imtmp;
//                }
//
//            }
//
//            stepsize <<= 1;
//        }
//
//    }

    public static void convertCartesianToPolar(ImageAccess image1, ImageAccess image2) {
        int nx = image1.getWidth();
        int ny = image2.getHeight();
        double real[] = new double[ny];
        double imaginary[] = new double[ny];
        double magnitude[] = new double[ny];
        double phase[] = new double[ny];
        for (int x = 0; x < nx; x++) {
            image1.getColumn(x, real);
            image2.getColumn(x, imaginary);
            for (int y = 0; y < ny; y++) {
                if (real[y] != 0.0D && imaginary[y] != 0.0D) {
                    double r = real[y];
                    magnitude[y] = Math.sqrt(r * r + imaginary[y] * imaginary[y]);
//                    phase[y] = Math.atan2(magnitude[y], r);
                    phase[y] = Math.atan2(imaginary[y], r);
                } else if (real[y] != 0.0D && imaginary[y] == 0.0D) {
                    if (real[y] < 0.0D) {
                        magnitude[y] = -real[y];
                        phase[y] = Math.PI;
                    } else {
                        magnitude[y] = real[y];
                        phase[y] = 0.0D;
                    }
                } else if (real[y] == 0.0D && imaginary[y] != 0.0D) {
                    if (imaginary[y] > 0.0D) {
                        magnitude[y] = -imaginary[y];
                        phase[y] = -Math.PI / 2.0D;
                    } else {
                        magnitude[y] = imaginary[y];
                        phase[y] = Math.PI / 2.0D;
                    }

//                    magnitude[y] = imaginary[y];
//                    phase[y] = Math.PI / 2;
//                    if(real[y] < 0.0D)
//                    {
//                        magnitude[y] = -real[y];
//                        phase[y] = -imaginary[y];
//                    }
                } else {
                    magnitude[y] = 0.0D;
                    phase[y] = 0.0D;
                }
            }

            image1.putColumn(x, magnitude);
            image2.putColumn(x, phase);
        }

    }

    public static void convertPolarToCartesian(ImageAccess image1, ImageAccess image2) {
        double magnitude[] = new double[image1.getHeight()];
        double phase[] = new double[image2.getHeight()];
        double real[] = new double[image1.getHeight()];
        double imaginary[] = new double[image2.getHeight()];
        for (int x = 0; x < image1.getWidth(); x++) {
            image1.getColumn(x, magnitude);
            image2.getColumn(x, phase);
            for (int y = 0; y < image1.getHeight(); y++) {
                double mag = magnitude[y];
                real[y] = mag * Math.cos(phase[y]);
                imaginary[y] = mag * Math.sin(phase[y]);
            }

            image1.putColumn(x, real);
            image2.putColumn(x, imaginary);
        }

    }
    
//    public static void inverseFFT1D(double real[], double imaginary[]) {
//        int size = real.length;
//
//        for (int i = 0; i < size; i++) {
//            imaginary[i] = -imaginary[i];
//        }
//
//        doFFT1D(real, imaginary);
//        for (int i = 0; i < size; i++) {
//            real[i] = real[i] / (double) size;
//            imaginary[i] = -imaginary[i] / (double) size;
//        }
//
//    }

}
