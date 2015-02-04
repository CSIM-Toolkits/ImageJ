/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author antonio
 */
public class BasicStatistic {


    public static double std(ImageAccess img) {
        double std = 0.0d;
        double meanImg = mean(img);
        double M = (img.getWidth() * img.getHeight()) - 1;

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                std += Math.pow(img.getPixel(i, j) - meanImg, 2);
            }
        }

        std /= M;

        return Math.sqrt(std);
    }

    public static double mean(ImageAccess img) {
        double mean = 0;
        double N = img.getWidth() * img.getHeight();

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                mean += img.getPixel(i, j);
            }
        }
        return (mean / N);
    }
}
