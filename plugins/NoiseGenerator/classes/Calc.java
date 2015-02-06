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
public class Calc {

    public static final int NORTH = 1;
    public static final int NORTHEAST = 2;
    public static final int EAST = 3;
    public static final int SOUTHEAST = 4;
    public static final int SOUTH = 5;
    public static final int SOUTHWEST = 6;
    public static final int WEST = 7;
    public static final int NORTHWEST = 8;

    public enum Orientation {

        NORTH,
        NORTHEAST,
        EAST,
        SOUTHEAST,
        SOUTH,
        SOUTHWEST,
        WEST,
        NORTHWEST;
    }

    public Calc() {
    }

    protected ImageAccess doLaplacianAllDirections(ImageAccess input) {
        ImageAccess output = input.duplicate();
        double sum;

        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                sum = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i == 0 && j == 0) {
                            sum += (-16) * input.getPixel(x, y);
                        } else {
                            sum += (2) * input.getPixel(x + i, y + j);
                        }
                    }
                }
                output.putPixel(x, y, sum / 32);
            }
        }

        return output;
    }

    /**
     * Calcula o Laplaciano no sentido Norte.
     *
     * @param input Imagem 8 bits GrayScale
     * @return Laplaciano da imagem
     */
    protected ImageAccess doLaplacianN(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i, j + 1) + (-2) * input.getPixel(i, j) + input.getPixel(i, j - 1);
                output.putPixel(i, j, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o Laplaciano no sentido Nordeste.
     *
     * @param input Imagem 8 bits GrayScale
     * @return Laplaciano da imagem
     */
    protected ImageAccess doLaplacianNE(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i + 1, j + 1) + (-2) * input.getPixel(i, j) + input.getPixel(i - 1, j - 1);
                output.putPixel(i, j, pixel / 2.0d);
            }
        }
        return output;
    }

    /**
     * Calcula o Laplaciano no sentido Leste.
     *
     * @param input Imagem 8 bits GrayScale
     * @return Laplaciano da imagem
     */
    protected ImageAccess doLaplacianE(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i + 1, j) + (-2) * input.getPixel(i, j) + input.getPixel(i - 1, j);
                output.putPixel(i, j, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o Laplaciano no sentido Sudeste.
     *
     * @param input Imagem 8 bits GrayScale
     * @return Laplaciano da imagem
     */
    protected ImageAccess doLaplacianSE(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i + 1, j - 1) + (-2) * input.getPixel(i, j) + input.getPixel(i - 1, j + 1);
                output.putPixel(i, j, pixel / 2.0d);
            }
        }
        return output;
    }

    /**
     * Calcula o Laplaciano no sentido Sul.
     *
     * @param input Imagem 8 bits GrayScale
     * @return Laplaciano da imagem
     */
    protected ImageAccess doLaplacianS(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i, j + 1) + (-2) * input.getPixel(i, j) + input.getPixel(i, j - 1);
                output.putPixel(i, j, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o Laplaciano no sentido Sudoeste.
     *
     * @param input Imagem 8 bits GrayScale
     * @return Laplaciano da imagem
     */
    protected ImageAccess doLaplacianSW(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i - 1, j - 1) + (-2) * input.getPixel(i, j) + input.getPixel(i + 1, j + 1);
                output.putPixel(i, j, pixel / 2.0d);
            }
        }
        return output;
    }

    /**
     * Calcula o Laplaciano no sentido Oeste.
     *
     * @param input Imagem 8 bits GrayScale
     * @return Laplaciano da imagem
     */
    protected ImageAccess doLaplacianW(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i - 1, j) + (-2) * input.getPixel(i, j) + input.getPixel(i + 1, j);
                output.putPixel(i, j, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o Laplaciano no sentido Noroeste.
     *
     * @param input Imagem 8 bits GrayScale
     * @return Laplaciano da imagem
     */
    protected ImageAccess doLaplacianNO(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i - 1, j + 1) + (-2) * input.getPixel(i, j) + input.getPixel(i + 1, j - 1);
                output.putPixel(i, j, pixel / 2.0d);
            }
        }
        return output;
    }

    /**
     * Calcula o laplaciano em uma determinada direção.
     *
     * @param input Imagem 8 bits GrayScale
     * @param direction Sentido em que o cálculo deve seguir (Norte, Sul,
     * Sudeste, ...)
     * @return Laplaciano da imagem
     */
    public ImageAccess doLaplacian(ImageAccess input, Orientation direction) {
        ImageAccess output = input.duplicate();

        switch (direction) {
            case NORTH:
                output = doLaplacianN(input);
                return output;
            case NORTHEAST:
                output = doLaplacianNE(input);
                return output;
            case EAST:
                output = doLaplacianE(input);
                return output;
            case SOUTHEAST:
                output = doLaplacianSE(input);
                return output;
            case SOUTH:
                output = doLaplacianS(input);
                return output;
            case SOUTHWEST:
                output = doLaplacianSW(input);
                return output;
            case WEST:
                output = doLaplacianW(input);
                return output;
            case NORTHWEST:
                output = doLaplacianNO(input);
                return output;
        }
        return null;
    }

    /**
     * Calcula o gradiente de uma image no sentido Norte, a partir do pixel
     * central. Utiliza derivada finita de primeira ordem.
     *
     * @param input Imagem 8-bits GrayScale
     * @return Imagem 8-bits GrayScale
     */
    protected ImageAccess doGradN(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int x = 0; x < output.getWidth(); x++) {
            for (int y = 0; y < output.getHeight(); y++) {
                double pixel = input.getPixel(x, y - 1) - input.getPixel(x, y);
                output.putPixel(x, y, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o gradiente de uma image no sentido Sul, a partir do pixel
     * central.
     *
     * @param input Imagem 8-bits GrayScale
     * @return Imagem 8-bits GrayScale
     */
    protected ImageAccess doGradS(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int x = 0; x < output.getWidth(); x++) {
            for (int y = 0; y < output.getHeight(); y++) {
                double pixel1 = input.getPixel(x, y + 1) - input.getPixel(x, y);
                output.putPixel(x, y, pixel1);
            }
        }
        return output;
    }

    /**
     * Calcula o gradiente de uma image no sentido Leste, a partir do pixel
     * central.
     *
     * @param input Imagem 8-bits GrayScale
     * @return Imagem 8-bits GrayScale
     */
    protected ImageAccess doGradL(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int x = 0; x < output.getWidth(); x++) {
            for (int y = 0; y < output.getHeight(); y++) {
                double pixel = input.getPixel(x + 1, y) - input.getPixel(x, y);
                output.putPixel(x, y, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o gradiente de uma image no sentido Oeste, a partir do pixel
     * central.
     *
     * @param input Imagem 8-bits GrayScale
     * @return Imagem 8-bits GrayScale
     */
    protected ImageAccess doGradO(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int x = 0; x < output.getWidth(); x++) {
            for (int y = 0; y < output.getHeight(); y++) {
                double pixel = input.getPixel(x - 1, y) - input.getPixel(x, y);
                output.putPixel(x, y, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o gradiente de uma image no sentido Nordeste, a partir do pixel
     * central.
     *
     * @param input Imagem 8-bits GrayScale
     * @return Imagem 8-bits GrayScale
     */
    protected ImageAccess doGradNL(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i + 1, j - 1) - input.getPixel(i, j);
                output.putPixel(i, j, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o gradiente de uma image no sentido Noroeste, a partir do pixel
     * central.
     *
     * @param input Imagem 8-bits GrayScale
     * @return Imagem 8-bits GrayScale
     */
    protected ImageAccess doGradNO(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i - 1, j - 1) - input.getPixel(i, j);
                output.putPixel(i, j, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o gradiente de uma image no sentido Sudoeste, a partir do pixel
     * central.
     *
     * @param input Imagem 8-bits GrayScale
     * @return Imagem 8-bits GrayScale
     */
    protected ImageAccess doGradSO(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i - 1, j + 1) - input.getPixel(i, j);
                output.putPixel(i, j, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o gradiente de uma image no sentido Sudeste, a partir do pixel
     * central.
     *
     * @param input Imagem 8-bits GrayScale
     * @return Imagem 8-bits GrayScale
     */
    protected ImageAccess doGradSL(ImageAccess input) {
        ImageAccess output = input.duplicate();

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                double pixel = input.getPixel(i + 1, j + 1) - input.getPixel(i, j);
                output.putPixel(i, j, pixel);
            }
        }
        return output;
    }

    /**
     * Calcula o gradiente por diferença finita de passo unitário em uma
     * determinada direção.
     *
     * @param input Imagem de entrada 8 bits GrayScale
     * @param direction Sentido em que o cálculo deve seguir (Norte, Sul,
     * Sudeste, ...)
     * @return Gradiente na direção escolhida.
     */
    public ImageAccess doGradient(ImageAccess input, Orientation direction) {
        ImageAccess output = input.duplicate();

        switch (direction) {
            case NORTH:
                output = doGradN(input);
                return output;
            case NORTHEAST:
                output = doGradNL(input);
                return output;
            case EAST:
                output = doGradL(input);
                return output;
            case SOUTHEAST:
                output = doGradSL(input);
                return output;
            case SOUTH:
                output = doGradS(input);
                return output;
            case SOUTHWEST:
                output = doGradSO(input);
                return output;
            case WEST:
                output = doGradO(input);
                return output;
            case NORTHWEST:
                output = doGradNO(input);
                return output;
        }
        return null;
    }

    /**
     * Calcula o módulo do gradiente. Utilizado no método robustiK em Diffusion.
     *
     * @param input Imagem original no qual irá ser calculado o gradiente.
     * @return módulo do gradiente.
     */
    protected ImageAccess absGradient(ImageAccess input) {
        ImageAccess gradX = doGradL(input);
        ImageAccess gradY = doGradS(input);
        ImageAccess aux = input.duplicate();
        double abs = 0.0d;

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                abs = Math.sqrt((gradX.getPixel(i, j) * gradX.getPixel(i, j)) + (gradY.getPixel(i, j) * gradY.getPixel(i, j)));
                aux.putPixel(i, j, abs);
            }
        }

        return aux;
    }

    /**
     * An approximation of gamma(x)
     *
     * @param x
     * @return gamma(x)
     */
    public double gamma(double x) {
        // 
        double f = 10E99;
        double g = 1;
        if (x > 0) {
            while (x < 3) {
                g = g * x;
                x = x + 1;
            }
            f = (1 - (2 / (7 * Math.pow(x, 2))) * (1 - 2 / (3 * Math.pow(x, 2)))) / (30 * Math.pow(x, 2));
            f = (1 - f) / (12 * x) + x * (Math.log(x) - 1);
            f = (Math.exp(f) / g) * Math.pow(2 * Math.PI / x, 0.5);
        } else {
            Double er = new Double(0);
            f = er.POSITIVE_INFINITY;
        }
        return f;
    }

    /**
     * An approximation to ln(gamma(x))
     *
     * @param xx
     * @return
     */
    public double logGamma(double xx) {
        // 
        // define some constants...
        int j;
        double stp = 2.506628274650;
        double cof[] = new double[6];
        cof[0] = 76.18009173;
        cof[1] = -86.50532033;
        cof[2] = 24.01409822;
        cof[3] = -1.231739516;
        cof[4] = 0.120858003E-02;
        cof[5] = -0.536382E-05;

        double x = xx - 1;
        double tmp = x + 5.5;
        tmp = (x + 0.5) * Math.log(tmp) - tmp;
        double ser = 1;
        for (j = 0; j < 6; j++) {
            x++;
            ser = ser + cof[j] / x;
        }
        double retVal = tmp + Math.log(stp * ser);
        return retVal;
    }
}
