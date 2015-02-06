/*
 * CoordinateConverter.java
 *
 * Created on 19 April 2008, 18:00
 *
 */

package classes;

//TODO comentar adequadamente

/**
 * Cartesian to Polar (Polar to Cartesian) coordinates converter for complex numbers.
 * 
 * @author Luiz Eduardo Virgilio da Silva
 * 
 * @reviewer Juliano Jinzenji Duque
 * 
 * Last Modified: 11/28/2011
 */
public class CoordinateConverter {
    
    /** 
     * Creates a new instance of CoordinateConverter 
     */
    public CoordinateConverter() {
    }
    
    /**
     * 
     * @param real
     * @param imaginary
     * @return 
     */
    public static double[] cartesianToPolar(double real, double imaginary) {
        double magnitude;
        double phase;
        
        if(real != 0.0D && imaginary != 0.0D) {
        // Neither real and imaginary are 0.0
            magnitude = Math.sqrt(real * real + imaginary * imaginary);
            phase = Math.atan2(imaginary, real);
        } else if(real != 0.0D && imaginary == 0.0D) {
        // Only imaginary is 0.0
            if(real < 0.0D) {
                magnitude = -real;
                phase = Math.PI;
            } else {
                magnitude = real;
                phase = 0.0;
            }
        } else if(real == 0.0D && imaginary != 0.0D) { 
        // Only real is 0.0
            if(imaginary < 0.0D) {
                magnitude = -imaginary;
                phase = - Math.PI / 2.0;
            } else {
                magnitude = imaginary;
                phase = Math.PI / 2.0;
            } 
        } else {
        // Both are 0.0
            magnitude = 0.0;
            phase = 0.0;
        }
        
        double[] res = new double[2];
        res[0] = magnitude;
        res[1] = phase;
        
        return res;
    }
    
    /**
     * 
     * @param input1
     * @param input2 
     */
    public static void cartesianToPolar(double input1[], double input2[]) {
        int size = input1.length;

        for(int i = 0; i < size; i++) {
            double[] coord = cartesianToPolar(input1[i],input2[i]);
            input1[i] = coord[0];
            input2[i] = coord[1];
        }
    }

    /**
     * 
     * @param magnitude
     * @param phase
     * @return 
     */
    public static double[] polarToCartesian(double magnitude, double phase) {
        double real;
        double imaginary;
        
        real = magnitude * Math.cos(phase);
        imaginary = magnitude * Math.sin(phase);
        
        double[] res = new double[2];
        res[0] = real;
        res[1] = imaginary;
        
        return res;
    }
    
    /**
     * 
     * @param input1
     * @param input2 
     */
    public static void polarToCartesian(double input1[], double input2[]) {
        int size = input1.length;
        
        for(int i = 0; i < size; i++) {
            double[] coord = polarToCartesian(input1[i],input2[i]);
            input1[i] = coord[0];
            input2[i] = coord[1];
        }
    }
    
}
