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
public class SampEn2D {
    
    public double fastSampleEn2D(ImageAccess image, int m, double r) {
        long startTime = System.currentTimeMillis();
        double tol = r * BasicStatistic.std(image);
        //System.out.println("Image STDEV: "+BasicStatistics.std(image));

        int nx = image.getWidth();
        int ny = image.getHeight();

        int A = 0; // Matches for (m+1)-length patterns
        int B = 0; // Matches for m-length patterns

        int Cim, Cim1;
        double Cm = 0.0;
        double Cm1 = 0.0;
        // Total number of patterns (for both m and m+1)
        double den = (nx - m) * (ny - m);

        for (int yi = 0; yi < ny - m; yi++) {
            for (int xi = 0; xi < nx - m; xi++) {
//            if(new Double(image.getPixel(xi, yi)).equals(Double.NaN))
//                System.out.println("pixel("+xi+","+yi+") = NaN");
//            System.out.println("("+xi+","+yi+"), B = "+B);

                // Counters of similar patterns for m and m+1
                Cim = Cim1 = 0;

                // Acabar a linha atual
                int yj = yi;
                int xj = xi + 1;
                while (xj < nx - m) {
                    if (similar(image, xi, yi, xj, yj, m, tol)) {  // Similar for M?
                        B++;
                        Cim++;

                        // Are they still similar for the next point?
                        //if(similar(image,xi,yi,xj,yj,m+1,tol))  // Similar for M?
                        if (similarNext(image, xi, yi, xj, yj, m, tol)) { // Similar for M?
                            A++;
                            Cim1++;
                        }
                    }
                    xj++;
                }

                // Proximas linhas
                for (yj = yi + 1; yj < ny - m; yj++) {
                    for (xj = 0; xj < nx - m; xj++) {
                        if (similar(image, xi, yi, xj, yj, m, tol)) {  // Similar for M?
                            B++;
                            Cim++;

                            // Are they still similar for the next point?
                            //if(similar(image,xi,yi,xj,yj,m+1,tol))  // Similar for M?
                            if (similarNext(image, xi, yi, xj, yj, m, tol)) { // Similar for M?
                                A++;
                                Cim1++;
                            }
                        }
                    }
                }

                Cm += Cim / (den - 1);
                Cm1 += Cim1 / (den - 1);
            }
        }
        Cm /= den;
        Cm1 /= den;

        //System.out.println("A="+A+", B="+B);
        //System.out.println("SE: "+(- Math.log( ((double)A)/((double)B) )));
        //System.out.println("\nCm="+Cm+", Cm1="+Cm1);
        //System.out.println("SE: "+(- Math.log( ((double)Cm1)/((double)Cm) )));
        double se = -Math.log(((double) Cm1) / ((double) Cm));

        long finishTime = System.currentTimeMillis();
        //System.out.println("Execution time: "+((finishTime - startTime)/1000.0));

        return se;
    }

    // Compares if m-length patterns are similar with tolerance r
    // First patter start at (x1,y1) and second at (x2,y2)
    private boolean similar(ImageAccess image, int x1, int y1, int x2, int y2, int m, double r) {
        for (int y = 0; y < m; y++) {
            for (int x = 0; x < m; x++) {
                double diff = Math.abs(image.getPixel(x1 + x, y1 + y) - image.getPixel(x2 + x, y2 + y));
                if (diff >= r) {
                    return false;
                }
            }
        }
        return true;
    }

    // Compares only if collum and row m+1 are similar with tolerance r
    // First patter start at (x1,y1) and second at (x2,y2)
    private boolean similarNext(ImageAccess image, int x1, int y1, int x2, int y2, int m, double r) {
        double diff;
        for (int y = 0; y <= m; y++) {  // Compares collumn M
            diff = Math.abs(image.getPixel(x1 + m, y1 + y) - image.getPixel(x2 + m, y2 + y));
            if (diff >= r) {
                return false;
            }
        }

        for (int x = 0; x <= m; x++) {  // Compares row M
            diff = Math.abs(image.getPixel(x1 + x, y1 + m) - image.getPixel(x2 + x, y2 + m));
            if (diff >= r) {
                return false;
            }
        }

        return true;
    }
}
