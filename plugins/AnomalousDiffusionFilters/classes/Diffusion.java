package classes;

import ij.IJ;

/**
 * Filtros espaciais baseados em equação parabólicas de difusão.<br>
 * Filtros clássicos: isotrópicos e anisotrópicos.<br>
 * Fitlros anômalos: isotrópicos e anisotrópicos
 *
 * @author Antonio Carlos da Silva Senra Filho
 */
public class Diffusion extends Calc {

    private double D;
    private double q;
    private static double delta;
    private double K;
    private int numInteration;
    /**
     * Escolha do tipo de função de parada de aresta (g( delta I )).
     * <br><br>
     * Veja referencia em Perona P. and Malik J., "SCALE-SPACE AND
     * EDGE-DETECTION USING ANISOTROPIC DIFFUSION", IEEE Transactions On Pattern
     * Analysis And Machine Intelligence, Vol. 12, pg 629 - 639. DOI
     * 10.1109/34.56205
     */
    public static final int G_EXPONENTIAL = 0;
    public static final int G_FRACTION = 1;
    public static final int G_PARCIAL = 2;

    /**
     * Retorna o valor da variável anômala q.
     *
     * @return Valor de q.
     */
    public double getQ() {
        return q;
    }

    /**
     * Insere um valor para a variável anômala q.
     *
     * @param q Variável anômala.
     */
    public void setQ(double q) {
        this.q = q;
    }

    /**
     * Retorna o valor do regulador delta para a solucao do EDP da equacao de
     * difusao anisotropica.
     *
     * @return O valor da constante de tempo.
     */
    public double getDelta() {
        return delta;
    }

    /**
     * Insere um valor compreendido entre 0 < delta < 1/7, para o regulador da
     * solucao de EDP da equacao de difusao anisotropica. @param delta
     */
    public void setDelta(double delta) {
        Diffusion.delta = delta;
    }

    /**
     * Insere um valor real compreendido entre 0 <= D <= 1, para o valor do
     * coeficiente de difusao. <br><br> O valor pode ser compreendido como
     * percentual desejado para a difusão. A ponderação que será utilizada leva
     * em consideração a curva experimental retirada para os valores possíveis
     * de q.
     *
     * @param DiffusionCoeficient coeficiente de difusão
     */
    public void setD(double DiffusionCoeficient) {
        D = DiffusionCoeficient;
    }

    /**
     * Retorna o valor percentual do coeficiente de difusao atribuido pela
     * funcao setD().
     *
     * @return double
     */
    public double getD() {
        return D;
    }

    /**
     * Insere um valor inteiro positivo para o número de interações.
     *
     * @param numInteration Número de iterações.
     */
    public void setNumInteration(int numInteration) {
        this.numInteration = numInteration;
    }

    /**
     * Retorna o valor do número de interações.
     *
     * @return Número de iterações. (Valor padrão n = 1)
     */
    public int getNumInteration() {
        return numInteration;
    }

    /**
     * Retorna o valor do coeficiente kappa, no qual é um parâmetro que
     * determina a intesidade de difusão. Utilizado para abordagem de difusao
     * anisotropica, tanto para a abordagem clássica quanto para a abordagem
     * anômala.
     *
     * @return double Valor do coeficiente Kappa.
     */
    public double getK() {
        return K;
    }

    /**
     * Insere um valor inteiro positivo para o coeficiente de difusão kappa, que
     * determina a intensidade de difusão. Utilizado para abordagem de difusao
     * anisotropica, tanto para a abordagem clássica quanto para a abordagem
     * anômala.
     *
     * @param k Valor de kappa. (Valor padrão k = 80)
     */
    public void setK(double k) {
        K = k;
    }

    /**
     * Construtor padrão.
     * <br><br>
     * Seleciona os valores padrões.
     * <br><br>
     * Coeficiente Anômalo: q = 1.0; Coeficiente de Difusão: D = 1.0;
     * Coeficiente Kappa: k = 80; Parâmetro Delta: delta = 0.13; Número de
     * iterações: n = 1;
     */
    public Diffusion() {
        setQ(1.0);
        setD(1.0);
        setK(30.0);
        setDelta(0.13);
        setNumInteration(1);
    }

    /**
     * Realiza a operação da função de difusão isotrópica clássica. Também
     * conhecida como a equação de calor.
     * <br><br>
     * d I(x,y,t)/dt = D. nabla^2 ( I(x,y,t) )
     * <br><br>
     * A utilização deste filtro iterativo é equivalente ao uso da máscara de
     * convolução Gaussiana com sigma determinado pela relação de Einstein:
     * sigma = sqrt(2.D.t). Sendo que D é o coeficiente de difusão e t é o
     * número de iterações.
     * <br><br>
     * Utiliza os parâmteros:
     * <br><br>
     * -> Número de interações<br>
     * -> Coeficiente de difusão.
     *
     * @param input Imagem 2D em escala de cinza.
     * @return Imagem 2D suavizada pelo filtro isotrópico clássico.
     */
    public ImageAccess IsoDiff(ImageAccess input) {
        ImageAccess output = input.duplicate();
        ImageAccess Laplacian = new ImageAccess(input.getWidth(), input.getHeight());
        for (int count = 0; count < numInteration; count++) {
            Laplacian = buildLaplacian(output);
            Laplacian.multiply(D);
            output.add(output, Laplacian);
        }

        return output;
    }

    /**
     * Realiza a operação da difusao anisotropica clássica.<br>
     * Aqui é utilizado o método proposto por Perona e Malik.
     * <br><br>
     * Perona P. and Malik J., "SCALE-SPACE AND EDGE-DETECTION USING ANISOTROPIC
     * DIFFUSION", IEEE Transactions On Pattern Analysis And Machine
     * Intelligence, Vol. 12, pg 629 - 639. DOI 10.1109/34.56205
     * <br><br>
     * Utiliza
     * <br><br>
     * -> Número de interações<br>
     * -> Coeficiente kappa (K).
     *
     * @param input Imagem 2D em escala de cinza.
     * @param g Tipo de função de arestra (gFunction())
     * @return Imagem 2D suavizada pelo método de difusão anisotrópica clássica.
     */
    public ImageAccess AnisoDiff(ImageAccess input, int g) {
        ImageAccess output = input.duplicate();
        int nx = input.getWidth();
        int ny = input.getHeight();
        ImageAccess diffimgRes = new ImageAccess(input.getWidth(), input.getHeight());
        ImageAccess[] diffimg = new ImageAccess[8];
        ImageAccess[] Grad = new ImageAccess[8];
        ImageAccess[] fPerona = new ImageAccess[8];

        int dr = 1;
        double dd = Math.sqrt(2);

        for (int i = 0; i < diffimg.length; i++) {
            diffimg[i] = new ImageAccess(input.getWidth(), input.getHeight());
            fPerona[i] = new ImageAccess(input.getWidth(), input.getHeight());
        }

        for (int count = 0; count < getNumInteration(); count++) {
            buildDerivates(Grad, output);
            buildFunction(input, fPerona, Grad, g, 1);

            double auxEven = delta * (1 / Math.pow(dr, 2));
            double auxOdd = delta * (1 / Math.pow(dd, 2));

            for (int x = 0; x < nx; x++) {
                for (int y = 0; y < ny; y++) {
                    for (int b = 0; b < 8; b += 2) {
                        diffimg[b].putPixel(x, y, auxEven * fPerona[b].getPixel(x, y) * Grad[b].getPixel(x, y));
                        diffimg[b + 1].putPixel(x, y, auxOdd * fPerona[b + 1].getPixel(x, y) * Grad[b + 1].getPixel(x, y));
                    }
                }
            }

            double sum = 0;
            for (int x = 0; x < input.getWidth(); x++) {
                for (int y = 0; y < input.getHeight(); y++) {
                    sum = 0;
                    for (int k = 0; k < 8; k++) {
                        sum += diffimg[k].getPixel(x, y);
                    }
                    diffimgRes.putPixel(x, y, sum);
                }
            }
            output.add(output, diffimgRes);
        }

        return output;
    }

    /**
     * Realiza a operação da difusao anisotropica anômala.<br>
     * Este método utiliza derivadas finitas de primeira ordem e segue como
     * critério de seleção de bordas as funções de parada de aresta dadas pelo
     * filtro anisotrópico clássico.<br>
     * O parâmetro anômalo deve ser escolhido seguindo o tipo de imagem que deve
     * ser utilizada.<br><br>
     * Veja a referência:<br>
     *
     *
     * <br><br>
     * Utiliza
     * <br><br>
     * -> Número de interações<br>
     * -> Coeficiente Anômalo (q)<br>
     * -> Coeficiente Kappa (K)
     *
     * @param input Imagem 2D em escala de cinza
     * @param g Função parada de aresta (gFunction())
     * @return Imagem suavizada pelo método de difusão anisotrópica anômala.
     */
    public ImageAccess AnomalousAnisoDiff(ImageAccess input, int g) {
        ImageAccess output = input.duplicate();
        ImageAccess qOutput = input.duplicate();
        double minImg = qOutput.getMinimum();
        int nx = input.getWidth();
        int ny = input.getHeight();
        ImageAccess diffimgRes = new ImageAccess(input.getWidth(), input.getHeight());
        ImageAccess[] diffimg = new ImageAccess[8];
        ImageAccess[] Grad = new ImageAccess[8];
        ImageAccess[] fPerona = new ImageAccess[8];

        int dr = 1;
        double dd = Math.sqrt(2);

        for (int i = 0; i < diffimg.length; i++) {
            diffimg[i] = new ImageAccess(input.getWidth(), input.getHeight());
            fPerona[i] = new ImageAccess(input.getWidth(), input.getHeight());
        }

        for (int count = 0; count < getNumInteration(); count++) {
            IJ.showStatus("Anisotropic Anomalous Diffusion filter - 2D...please wait");
            IJ.showProgress(count, getNumInteration());
            if (minImg < 0 && Math.abs(minImg) < 0.05) {
                qOutput.add((-1) * minImg);
            }
            buildDerivates(Grad, qOutput);
            buildFunction(input, fPerona, Grad, g, 1);
            makeAnomalousImg(qOutput, q);
            buildDerivates(Grad, qOutput);

            double auxEven = delta * (1 / Math.pow(dr, 2));
            double auxOdd = delta * (1 / Math.pow(dd, 2));

            for (int x = 0; x < nx; x++) {
                for (int y = 0; y < ny; y++) {
                    for (int b = 0; b < 8; b += 2) {
                        diffimg[b].putPixel(x, y, auxEven * fPerona[b].getPixel(x, y) * Grad[b].getPixel(x, y));
                        diffimg[b + 1].putPixel(x, y, auxOdd * fPerona[b + 1].getPixel(x, y) * Grad[b + 1].getPixel(x, y));
                    }
                }
            }

            double sum = 0;
            for (int x = 0; x < input.getWidth(); x++) {
                for (int y = 0; y < input.getHeight(); y++) {
                    sum = 0;
                    for (int k = 0; k < 8; k++) {
                        sum += diffimg[k].getPixel(x, y);
                    }
                    diffimgRes.putPixel(x, y, sum);
                }
            }
            output.add(output, diffimgRes);
            qOutput = output.duplicate();
            minImg = qOutput.getMinimum();
        }

        return qOutput;
    }

    /**
     * Realiza a operação da difusao isotrópica anômala.<br>
     * Este método utiliza derivadas finitas de primeira ordem.<br>
     * O efeito deste filtro iterativo é análogo a utilização de uma máscara de
     * convolução q-Gaussiana.
     * <br><br>
     * Utiliza
     * <br><br>
     * -> Número de interações<br>
     * -> Coeficiente Anômalo (q)<br>
     * -> Coeficiente de Difusão (D)
     *
     * @param input Imagem 2D em escala de cinza
     * @param percentD Coeficiente de difusão. (0 <= D <= 1)
     * @return Imagem 2D suavizada pelo método de difusão isotrópica anômala.
     */
    public ImageAccess AnomalousIsoDiff(ImageAccess input, double percentD) {
        double inD = curveD(percentD);
        double min = input.getMinimum();
        double minQimg = min;
        ImageAccess output = input.duplicate();
        ImageAccess qOutput = input.duplicate();
        ImageAccess Laplacian = new ImageAccess(input.getWidth(), input.getHeight());

        for (int count = 0; count < numInteration; count++) {
            IJ.showStatus("Isotropic Anomalous Diffusion filter - 2D...please wait");
            IJ.showProgress(count, getNumInteration());
            if (minQimg < 0 && Math.abs(minQimg) < 0.05) {
                qOutput.add((-1) * minQimg);
            }
            makeAnomalousImg(qOutput, q);
            Laplacian = buildLaplacian(qOutput);
            Laplacian.multiply(inD);
            qOutput.add(output, Laplacian);
            output = qOutput.duplicate();
            minQimg = qOutput.getMinimum();
        }

        return qOutput;
    }

    private double curveD(double percentD) {
        double d = 0.0d;
        if (q < 1.0) {
            d = percentD * Math.exp((-1) * (Math.pow(q - 1.0, 2.0)) / 0.08);
        } else if (q > 1.0 && q <= 1.98) {
            d = percentD * Math.exp((-1) * (Math.pow(q - 1.0, 2.0)) / 0.4);
        } else if (q == 1.0) {
            d = 1.0;
        }
        return d;
    }

    private void buildDerivates(ImageAccess[] fD, ImageAccess output) {
        fD[0] = doGradN(output);
        fD[1] = doGradNL(output);
        fD[2] = doGradL(output);
        fD[3] = doGradSL(output);
        fD[4] = doGradS(output);
        fD[5] = doGradSO(output);
        fD[6] = doGradO(output);
        fD[7] = doGradNO(output);
    }

    private ImageAccess buildLaplacian(ImageAccess input) {
        ImageAccess Laplacian = input.duplicate();
        ImageAccess[] fDD = new ImageAccess[8];

        for (int i = 0; i < fDD.length; i++) {
            fDD[i] = new ImageAccess(input.getWidth(), input.getHeight());
        }

        fDD[0] = doLaplacianN(input);
        fDD[1] = doLaplacianNE(input);
        fDD[2] = doLaplacianE(input);
        fDD[3] = doLaplacianSE(input);
        fDD[4] = doLaplacianS(input);
        fDD[5] = doLaplacianSW(input);
        fDD[6] = doLaplacianW(input);
        fDD[7] = doLaplacianNO(input);

        double sum = 0;
        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                sum = 0;
                for (int k = 0; k < fDD.length; k++) {
                    sum += fDD[k].getPixel(x, y);
                }
//                Laplacian.putPixel(x, y, sum / 16d);
                Laplacian.putPixel(x, y, sum / 32d);
            }
        }

        return Laplacian;
    }

    private void buildFunction(ImageAccess input, ImageAccess[] fPerona, ImageAccess[] Grad, int g, double percent) {
        switch (g) {
            case G_EXPONENTIAL:
                for (int i = 0; i < input.getWidth(); i++) {
                    for (int j = 0; j < input.getHeight(); j++) {
                        for (int k = 0; k < fPerona.length; k++) {
                            fPerona[k].putPixel(i, j, curveD(percent) * Math.exp(-Math.pow(Grad[k].getPixel(i, j) / getK(), 2)));
                        }
                    }
                }
                break;
            case G_FRACTION:
                for (int i = 0; i < input.getWidth(); i++) {
                    for (int j = 0; j < input.getHeight(); j++) {
                        for (int k = 0; k < fPerona.length; k++) {
                            fPerona[k].putPixel(i, j, curveD(percent) * (1 / (1 + Math.pow(Grad[k].getPixel(i, j) / getK(), 2))));
                        }
                    }
                }
                break;
            case G_PARCIAL:
                for (int i = 0; i < input.getWidth(); i++) {
                    for (int j = 0; j < input.getHeight(); j++) {
                        for (int k = 0; k < fPerona.length; k++) {
                            fPerona[k].putPixel(i, j, curveD(percent) * ((Grad[k].getPixel(i, j) <= getK() * getK()) ? Math.pow((1. - (Grad[k].getPixel(i, j) / getK())), 2) : 0));
                        }
                    }
                }
                break;
        }
    }

    private void makeAnomalousImg(ImageAccess img, double q) {
        double val = 0.0;
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                val = Math.pow(img.getPixel(i, j), 2.0 - q);
                img.putPixel(i, j, val);
            }
        }
    }
}
