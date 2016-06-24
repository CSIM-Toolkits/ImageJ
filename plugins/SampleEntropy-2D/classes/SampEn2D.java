/*
 * Two-dimensional Sample Entropy
 *
 * 
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Creative Common v4.0 License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package classes;

/**
 * SampEn2D - Two-dimensional Sample Entropy
 * 
 * This is the class that defines the SampEn2D algorithm.
 *   
 */
public class SampEn2D {
    
    public double fastSampleEn2D(ImageAccess image, int m, double r) {
//        double tol = r * image.createFloatProcessor().getStatistics().stdDev;
        double tol = r;

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

                // Counters of similar patterns for m and m+1
                Cim = Cim1 = 0;

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

        double se = -Math.log(((double) Cm1) / ((double) Cm));

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
