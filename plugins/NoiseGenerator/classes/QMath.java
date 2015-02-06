/**
 * QMath.java
 * 
 */

package classes;

import static java.lang.Math.*;


 /**
 * QMath.java implements the q-generalized algebra, according to the nonextensive statistical mechanics. <br>
 * 
 * <p>
 * Based on: <br>
 * J. Phys. A: Math. Gen. 31 (1998) 5281–5288.<br>
 * On a q-generalization of circular and hyperbolic functions<br>
 * Ernesto Borges<br>
 * <br>
 * Physica A 340 (2004) 95-101.<br>
 * A possible deformed algebra and calculus inspired in nonextensive thermostatistics<br>
 * Ernesto Borges<br>
 * <br>
 * Complexity, Metastability and Nonextensivity (2007) 8-20. <br>
 * Nonextensive statistical mechanics and central limit theorems I - Convolution of independent random variables and q-product <br>
 * Constantino Tsallis, Silvio M. Duarte Queiros
 * 
 * @author Luiz Otavio Murta Junior
 * date june 23 2007
 * 
 * @reviewer Juliano Jinzenji Duque
 * @reviewer Luiz Eduardo Virgilio da Silva
 * 
 * Last Modified: 12/06/2012 (dd/mm/yyyy)
 */

public class QMath {


    /**
     * q-sum.
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>q-sum(x,y,q) = x + y + (1 - q)*x*y</code>
     * 
     * @param x
     * @param y
     * @param q
     * @return the q-sum of <code>x</code> and <code>y</code>
     */
    public static double qSum(double x, double y, double q) {
        return x + y + (1.0 - q)*x*y;
    }

    /**
     * q-difference.
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>q-difference(x,y,q) = (x - y) / (1 + (1 - q)*y)</code> , <br><br>
     * 
     * for <br>
     * <code>
     * y != (1/(q - 1))
     * </code>
     * 
     * 
     * @param x
     * @param y
     * @param q
     * @return the q-difference of <code>y</code> from <code>x</code>.
     *     <br><code>Double.NaN</code> if <code>y</code> is equal to <code>1/(q - 1)</code>
     */
    public static double qDiff(double x, double y, double q) {
        
        if(y != (1.0/(q - 1.0)))
            return (x - y) / (1.0 + (1.0 - q)*y);

        else
            return Double.NaN;
        
    }
    
    /**
     * It is used by qProd and qProd2 methods.
     * 
     * @param powX
     * @param powY
     * @param q
     * @return 
     */
    private static double qProdCore(double powX, double powY, double q) {
//        if (x <= 0 || y <= 0)
//            return Double.NaN;
        
        double z = powX + powY - 1;
        
        return z <= 0 ? 0 : pow(z, 1/(1-q));
    }
    
        
    /**
     * q-product.
     * 
     * This method does not compute for x or y &lt; 0. See <code>qProd2</code>.
     * 
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>q-product(x,y,q) = C^(1/(1 - q)) </code>, <br><br>
     * 
     * where <br>
     * <code>
     * C = Max{[x^(1 - q) +  y^(1 - q) - 1], 0}
     * </code><br><br>
     * 
     * for <br>
     * <code>
     * x,y >= 0 , for q!=1
     * </code>
     * 
     * 
     * @param x
     * @param y
     * @param q
     * @return the q-product of <code>x</code> and <code>y</code>.
     *    <br><code>Double.NaN</code> if, for <code>q != 1</code>, <code>x</code> or <code>y &lt; 0</code> 
     */
    public static double qProd(double x, double y, double q) {
        if(q == 1.0)
            return x * y;
        
        if (x < 0 || y < 0)            
            return Double.NaN;
        
                
        double nonzero;
        
        if(x==0)
            nonzero = y;
        else 
            if (y==0)
                nonzero = x;
            else
                return qProdCore(pow(x, 1-q),pow(y, 1-q), q);
        
        
        if (q<1 && nonzero>1) 
            return qProdCore(pow(nonzero,1-q), 0, q);
        else
            return 0;
    }
    
    /**
     * q-product.
     * 
     * This method leads with x and y belonging to real set.
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>q-product(x,y,q) = sign(x) * sign(y) * C^(1/(1 - q)) </code>, <br><br>
     * 
     * where <br>
     * <code>
     * C = Max{[abs(x)^(1 - q) +  abs(y)^(1 - q) - 1], 0}
     * </code><br><br>
     * 
     * 
     * 
     * @param x
     * @param y
     * @param q
     * @return the q-product of <code>x</code> and <code>y</code>.
     * 
     */
    public static double qProd2(double x, double y, double q) {

        if(q == 1.0) 
            return x * y;
 
        double nonzero;
        
        if(x==0)
            nonzero = y;
        else 
            if (y==0)
                nonzero = x;
            else
                return signum(x) * signum(y)* qProdCore(pow(abs(x), 1-q),pow(abs(y), 1-q), q);
            
        if (q<1 && abs(nonzero)>1) 
            return signum(nonzero) * qProdCore(pow(nonzero,1-q), 0, q);
        else
            return 0;
        
    }
    
    //TODO esta implementacao de qDiv provavelmente nao esta correta. Precisa verificar os detalhes no artigo
    /**
     * q-division.
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>q-division(x,y,q) = B^(1/(1 - q)) </code>, <br><br>
     * 
     * where <br>
     * <code>
     * B = Max{[x^(1 - q) -  y^(1 - q) + 1], 0}
     * </code><br><br>
     * 
     * for <br>
     * <code>
     * x,y >= 0 , if q != 1
     * </code>
     * 
     * 
     * @param x
     * @param y
     * @param q
     * @return the q-division of <code>x</code> by <code>y</code>.
     *     <br><code>Double.NaN</code> if, for <code>q != 1</code>, <code>x</code> or <code>y &lt; 0</code> 
     */
    public static double qDiv(double x, double y, double q) {
        double ratio;
        double z;
        
        if(q == 1.0) {
            ratio = x / y;
        } else {
            if((x >= 0)&&(y >= 0)){
                z = pow(x, 1 - q) -  pow(y, 1 - q) + 1;
                
                if(z > 0) 
                    ratio = pow(z, (1 / (1 - q)));
                else
                    ratio = pow(z, (1 / (1 - q)));
            }
            else{
                return Double.NaN;
            }
        }
        
        return ratio;
    }

    /**
     * q-logarithm.
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>
     * q-logarithm(x,q) = (x^(1 - q) - 1) / (1 - q) ,
     * </code>
     * <br><br>
     * 
     * for <br>
     * <code>
     * x > 0
     * </code>
     * 
     * 
     * @param x 
     * @param q
     * @return the q-logarithm of <code>x</code>.
     *     <br><code>Double.NaN</code> if <code>x</code> &lt;= 0.
     */
    public static double qLog(double x, double q) {
        double y;
        
        if(x > 0) {
            if(q == 1.0)
                y = log(x);
            else
                y = (pow(x, 1-q) - 1) / (1 - q);
            return y;
        } else {
            return Double.NaN;
        }
    }

    /**
     * It is used by qExp method.
     * 
     * @param x
     * @param q
     * @return 
     */
    private static double qExpDefinition(double x,double q) {
        double z = 1 + (1-q) * x;
        
        return z<=0 ? 0 : pow(z, 1/(1-q));
    }
    
    /**
     * q-exponential.
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>
     * q-exponential(x,y,q) = A^(1/(1 - q)) 
     * </code>, <br><br>
     * 
     * where <br>
     * <code>
     * A = Max{[1 + (1-q)*x], 0}
     * </code>
     * 
     * @param x
     * @param q
     * @return the q-exponential of <code>x</code>.
     *     <br><code>for q > 1, Double.NaN</code> if <code>x</code> is greater than or equal to 1/(q-1)
     */
    public static double qExp(double x, double q) {

        if (q == 1)
            return exp(x);
        else {
            
            double t = 1 /(q-1);
            if (q < 1) {
                if (x < t )
                    return 0; //it vanishes to x < 1/(q-1)
                else 
                    return qExpDefinition(x,q);
            } else { //q > 1 
                if (x < t )
                    return qExpDefinition(x,q);
                else
                    return Double.NaN; //it is not defined to x > 1/(q-1)
            }
        }

    }
    
    /**
     * Used in q-cosine and q-sine calculations.
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>
     * rq(x,q) = [q-exponential((1 - q) * x^2)]^(1/2)
     * </code>
     * 
     * @param x
     * @param q
     * @return 
     */
    protected static double rq(double x, double q) {
        double r;
        
        r = sqrt(qExp((1 - q) * x * x, q));
        return r;
    }
    
    /**
     * Used in q-cosine and q-sine calculations.
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>
     * fq(x,q) = arctan((1 - q) * x) / (1 - q)
     * </code>
     * 
     * 
     * @param x
     * @param q
     * @return 
     */
    protected static double fq(double x, double q) {
        if(q==1)
            return x;
        else
            return atan2((1 - q) * x, 1) / (1 - q);
    }

    /**
     * q-cosine.
     * 
     * <p>
     * Equation:<br><br>
     * 
     * <code>
     * q-cosine(x,q) = rq(x,q) * cos( fq(x,q) )
     * </code>
     * 
     * 
     * @param x
     * @param q
     * @return the q-cosine of <code>x</code>
     */
    public static double qCos(double x, double q) {
        double y;
        
        if (q == 1)
            y = cos(x);
        else
            y = rq(x, q) * cos(fq(x, q));
        return y;
    }

    /**
     * q-sine.
     * 
     *  <p>
     * Equation:<br><br>
     * 
     * <code>
     * q-sine(x,q) = rq(x,q) * sin( fq(x,q) )
     * </code>
     * 
     * 
     * @param x
     * @param q
     * @return the q-sine of <code>x</code>
     */
    public static double qSin(double x, double q) {
        double y;
        
        if (q == 1)
            y = sin(x);
        else
            y = rq(x, q) * sin(fq(x, q));
        return y;
    }    

    /**
     * q-tangent.
     * 
     *  <p>
     * Equation:<br><br>
     * 
     * <code>
     * q-tangent(x,q) = q-sine(x,q) / q-cosine(x,q)
     * </code>
     * 
     * 
     * @param x
     * @param q
     * @return the q-tan of <code>x</code>
     */
    public static double qTan(double x, double q) {
        double y;
        
        y = tan(fq(x, q));
        return y;
    }
    
    /**
     * q-exponential for an imaginary number.
     * 
     *  <p>
     * Equation:<br><br>
     * 
     * <code>
     * q-expi(x,q) = q-cosine(x,q) + q-sine(x,q)*i
     * </code>
     * 
     * @param x the imaginary part x of an imaginary number <i>i</i>x.
     * @param q
     * @return a double array, index 0 contains the real part and  index 1 the imaginary part.
     */
    public static double[] qExpi(double x, double q) {
        double[] z = new double[2];
        
        z[0] = qCos(x, q);
        z[1] = qSin(x, q);
        return z;
    }
    
    //TODO Este metodo ficaria melhor em alguma classe que manipulasse numeros complexos
//    /**
//     * 
//     * @param real
//     * @param imaginary
//     * @param p
//     * @return 
//     */
//    public static double[] complexPower(double real, double imaginary, double p) {
//
//        double[] c = CoordinateConverter.cartesianToPolar(real,imaginary);
//        
//        double magnitude = c[0];
//        double phase = c[1];
//        
//        // Doing power in cartesian coordinates
//        magnitude = Math.pow(Math.abs(magnitude), p);
//        phase = phase * p;
//        
//        return( CoordinateConverter.polarToCartesian(magnitude,phase) );
//    }
}