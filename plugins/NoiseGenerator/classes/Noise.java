/*
 * OneFNoise.java
 *
 * Created on 18 April 2008, 06:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package classes;

import ij.IJ;
import java.util.Random;

/**
 * This class is used to generateOneFNoise the 1/f noise
 *
 * @author Luiz Eduardo
 */
public class Noise extends FourierTransform {

    private int size;

//    public Noise(int size) {
//        this.size = size;
//    }
    public Noise() {
    }

//    /**
//     * Acconding to Chaos and Time-Series Analysis, Julien C. Sprott, pg 230.
//     *
//     * @return
//     * @throws InterruptedException
//     */
//    public double[] generateGaussianWhiteNoise() throws InterruptedException {
//        double[] final_signal = new double[size];
//
//        for (int i = 0; i < size; i++) {
//            Thread.sleep(0);//Necessario para o JBios
//            double r1 = Math.random();
//            double r2 = Math.random();
//            final_signal[i] = Math.sqrt((-2) * Math.log(r1)) * Math.sin(2 * Math.PI * r2);
//        }
//
//        return final_signal;
//    }
//
//    public double[] generateUniformWhiteNoise() throws InterruptedException {
//        double[] final_signal = new double[size];
//
//        for (int i = 0; i < size; i++) {
//            Thread.sleep(0);
//
//            final_signal[i] = Math.random();
//        }
//
//        return final_signal;
//    }
//
//    public double[] generateOneFNoise() throws InterruptedException {
//        double[][] power_espectrum = new double[2][size];
//
//        // Creating the power spectrum of 1/f noise
//        // First half
//        for (int i = 0; i <= size / 2; i++) {
//            Thread.sleep(0);
//
//            power_espectrum[0][i] = 1.0 / Math.pow((i + 1.0), 0.5);      // Potencia ==> 1/f
//            power_espectrum[1][i] = 2 * Math.PI * (Math.random() - 0.5);  // Phase ==> [-PI, PI]
//        }
//        // Second half
//        int j = 1;
//        for (int i = size / 2 + 1; i < size; i++) {
//            Thread.sleep(0);
//
//            power_espectrum[0][i] = power_espectrum[0][size / 2 - j];   // Magnitude ==> Mirroring
//            power_espectrum[1][i] = -power_espectrum[1][size / 2 - j];   // Phase ==> Mirroring
//
//            j++;
//        }
//
//        // Converting polar form to cartesian
//        CoordinateConverter.polarToCartesian(power_espectrum[0], power_espectrum[1]);
//        // Calculating inverse FourierTransform
//        FourierTransform.inverseFT1D(power_espectrum[0], power_espectrum[1]);
//
//        // Returning real part
//        return power_espectrum[0];
//    }
    // -----
    /**
     * Cria ruído branco uniforme.<br>
     * Aplica a intensidade do ruido segundo uma porcentagem de ruído
     * desejado.<br><br>
     *
     * A imagem que entra como primeiro parâmetro do método serve, apenas, para
     * a tomada dos valores máximo, mínimo, largura e altura da mesma. Nenhuma
     * modificação é feita na imagem a ser colocada como parâmetro.
     *
     * @param input Imagem base para calcular a intensidade do ruído.
     * @param percent Intensidade do ruído (0 <= p <= 100)
     * @
     * return Ruído aleatório do tipo ImageAccess.
     */
    public ImageAccess generateUniformNoise(ImageAccess input, double percent) {
        ImageAccess noiseRandom = new ImageAccess(input.getWidth(), input.getHeight());
        double num = (input.getMaximum() - input.getMinimum()) * (percent / 100);

        for (int i = 0; i < noiseRandom.getWidth(); i++) {
            for (int j = 0; j < noiseRandom.getHeight(); j++) {
                noiseRandom.putPixel(i, j, Math.random() * num);
            }
        }

        return noiseRandom;
    }

    /**
     * Cria ruído branco uniforme.<br>
     * Aplica a intensidade do ruido segundo uma porcentagem de ruído desejado.
     *
     * @param nx Largura
     * @param ny Altura
     * @param maxValue Valor máximo do ruído.
     * @return Imagem 2D com ruído de distribuição uniforme com valores entre
     * zero e maxValue.
     */
    public ImageAccess generateUniformNoise(int nx, int ny, double maxValue) {
        ImageAccess noiseRandom = new ImageAccess(nx, ny);

        for (int i = 0; i < noiseRandom.getWidth(); i++) {
            for (int j = 0; j < noiseRandom.getHeight(); j++) {
                noiseRandom.putPixel(i, j, Math.random() * maxValue);
            }
        }

        return noiseRandom;
    }

    /**
     * Cria ruído branco Gaussiano.<br>
     *
     * @param nx Largura
     * @param ny Altura
     * @param sigma Desvio padrão do histograma do ruído Gaussiano
     * @return Imagem 2D com ruído de distribuição Gaussiana com média zero e
     * desvio padrão sigma.
     */
    public ImageAccess generateGaussianNoise(int nx, int ny, double sigma) {
        ImageAccess noiseImg = new ImageAccess(nx, ny);
        Random rnd = new Random();

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                noiseImg.putPixel(i, j, rnd.nextGaussian() * sigma);
            }
        }

        return noiseImg;
    }

    /**
     * Cria ruído com distribuição de Rayleigh.
     *
     * @param nx Largura
     * @param ny Altura
     * @param sigma Desvio padrão.
     * @return Imagem 2D com ruído de distribuição Rayleigh com média m =
     * sigma*(sqrt( PI/2) ) e desvio padrão sigmaR = sigma*sqrt( (2 - PI/2) )
     */
    public ImageAccess generateRayleighNoise(int nx, int ny, double sigma) {
        ImageAccess noise = new ImageAccess(nx, ny);

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                noise.putPixel(i, j, Math.sqrt((-1) * Math.log(1.0 - Math.random()) * (2.0 * sigma * sigma)));
            }
        }

        return noise;
    }

    /**
     * Cria ruído Salt and Pepper. Aplica a intensidade do ruido segundo uma
     * porcentagem de pixeis que devem ser aplicados.
     *
     * @param input Imagem base para calcular a intensidade do ruído.
     * @param percent Intensidade do ruído.
     * @return Ruído Salt and Pepper em escala 8 bits de tons de cinza.
     */
    public ImageAccess generateSaltAndPepperNoise(int nx, int ny, double percent) {
        ImageAccess noiseSalt = new ImageAccess(nx, ny);
        int num = (int) ((nx * ny) * (percent / 100));

        for (int i = 0; i < num; i++) {
            noiseSalt.putPixel((int) (noiseSalt.getWidth() * Math.random()), (int) (noiseSalt.getHeight() * Math.random()), Math.random() > 0.5 ? 255 : 0);
        }

        return noiseSalt;
    }

    /**
     * Cria ruído 1/f^beta.
     * <br><br>
     * O ruído 1/f é gerado pela...<br>
     *
     * @param input Imagem base para calcular a intensidade do ruído.
     * @param beta Parâmetro de potência espectral para ruido 1/f^beta
     * @param percent Intensidade de ruído 1/f^beta
     * @return Imagem 2D com ruído do tipo 1/f^beta.
     * @throws InterruptedException Exceção para tratamento - Utilizado para o
     * programa de processamento de sinais JBios.
     */
    public ImageAccess generatefNoise(ImageAccess input, double beta, double percent) throws InterruptedException {
        ImageAccess wNoise = generateUniformNoise(input, percent);
        ImageAccess imaginario = new ImageAccess(input.getWidth(), input.getHeight());
        IJ.showProgress(0.2);
        FourierTransform.ft2D(wNoise, imaginario);
        FourierTransform.convertCartesianToPolar(wNoise, imaginario);

        IJ.showProgress(0.4);
        ImageAccess fImg = makeF(wNoise, beta);

        fImg.multiply(fImg, wNoise);

        IJ.showProgress(0.6);
        FourierTransform.convertPolarToCartesian(fImg, imaginario);
        IJ.showProgress(0.8);
        FourierTransform.inverseFT2D(fImg, imaginario);

        double num = (input.getMaximum() - input.getMinimum()) * (percent / 100.);
        changeScale(fImg, num);
        IJ.showProgress(1.0);
        return fImg;
    }

    private ImageAccess makeF(ImageAccess img, double beta) {
        int nx = img.getWidth();
        int ny = img.getHeight();

        ImageAccess fImg = new ImageAccess(nx, ny);

        for (int i = 0; i < nx / 2; i++) {
            for (int j = 0; j < ny / 2; j++) {
                if (i == 0 && j == 0) {
                    fImg.putPixel(i, j, 1);
                } else {
                    fImg.putPixel(i, j, Math.pow(1. / (i + j), beta));
                }
            }
        }

        for (int i = 0; i < nx / 2; i++) {
            for (int j = 0; j < ny / 2; j++) {
                fImg.putPixel(i + nx / 2, j, fImg.getPixel(nx / 2 - i - 1, j));
                fImg.putPixel(i, j + ny / 2, fImg.getPixel(i, ny / 2 - j - 1));
                fImg.putPixel(i + nx / 2, j + ny / 2, fImg.getPixel(nx / 2 - i - 1, ny / 2 - j - 1));
            }
        }

        return fImg;
    }

    private static void changeScale(ImageAccess fImg, double num) {
        double pMax = fImg.getMaximum();
        double pMin = fImg.getMinimum();

        for (int i = 0; i < fImg.getWidth(); i++) {
            for (int j = 0; j < fImg.getHeight(); j++) {
                double x = num * (fImg.getPixel(i, j) - pMin) / (pMax - pMin);
                fImg.putPixel(i, j, x);
            }
        }
    }
}
