/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Perfusion_v1_1;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Plot;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;
import ij.util.DicomTools;
import java.awt.Rectangle;

/**
 *
 * @author Antonio
 */
public class PerfusionMetrics extends Metrics_ {

    private enum METRICS {

        CBV,
        CBF,
        MTT;
    };

    protected static double calcCBV(double[] column, ImagePlus img) {
        double sum = 0.0d;
        int nData = column.length;
        double timePerImage = getTimeAcquisition(img);
        double sumTotal = column[0] * (nData * timePerImage);

        for (int i = 0; i < nData; i++) {
            sum += column[i] * timePerImage;
        }

        return (sumTotal - sum);
    }

    protected static double calcMTT(double[] column, ImagePlus img) {
        int peakIndex = peakIndexDetect(column);
        double point1Index = 0, point2Index = 0;
        double peakValue = peakDetect(column);
        double maxValue = maxValue(column);
        double maxHalfValue = (maxValue - peakValue) / 2.0d;
        double timePerImage = getTimeAcquisition(img);
        double aux1 = column[0] - maxHalfValue;
//        double aux2=column[column.length]-maxHalfValue;

        for (int i = 1; i < peakIndex; i++) {
            if (column[i] - maxHalfValue < aux1 && aux1 > 0d) {
                aux1 = column[i] - maxHalfValue;
                point1Index = i;
            }
        }

        for (int i = peakIndex + 1; i < column.length - 1; i++) {
            if (column[i] < ((column[column.length - 1] - column[peakIndex]) / 2.0d + column[i]) && aux1 > 0d) {
                aux1 = column[i] - maxHalfValue;
                point2Index = i;
            }
        }

        return (double) (point2Index - point1Index) * timePerImage;
    }

    protected static double calcCBF(double[] column, ImagePlus img) {
        return calcCBV(column, img) / calcMTT(column, img);
    }

    private static double meanCalc(ResultsTable data, ImagePlus img, METRICS metric) {
        int nColumns = data.getLastColumn();
        double[] mean = new double[data.getColumn(0).length];
        double sum = 0.0d;
        for (int line = 0; line < data.getColumn(0).length; line++) {
            sum = 0.0d;
            for (int values = 1; values <= nColumns; values++) {
                sum += data.getValueAsDouble(values, line);
            }
            mean[line] = sum / (double) nColumns;
        }

        switch (metric) {
            case CBV:
                return calcCBV(mean, img);
            case CBF:
                return calcCBF(mean, img);
            case MTT:
                return calcMTT(mean, img);
        }
        return 1;
    }

    protected static double timeCalculation(double[] pixelGray, ImagePlus img) {
//        double TR;
        double timePeak = 0.0d;
        double min = pixelGray[0];
        double timePerImage = getTimeAcquisition(img);
//        TR = Double.parseDouble(DicomTools.getTag(img, "0018,0080"));
        for (int i = 1; i < pixelGray.length; i++) {
            if (pixelGray[i] < min) {
                min = pixelGray[i];
                timePeak = (double) (i + 1);
            }
        }
//        timePeak *= TR;
        timePeak *= timePerImage;

        return timePeak;
    }

    private static double mean(double[] times) {
        double sum = 0.0d;

        for (int i = 1; i < times.length; i++) {
            sum += times[i];
        }

        return sum / (double) (times.length - 1);
    }

    private static double std(double[] times) {
        double sd = 0.0;
        double mean = mean(times);

        for (int i = 1; i < times.length; i++) {
            sd += (times[i] - mean) * (times[i] - mean);
        }

        return Math.sqrt(sd / (times.length - 1));

    }

    private static double maxValue(double[] column) {
        double max = column[0];

        for (int i = 1; i < column.length; i++) {
            if (column[i] > max) {
                max = column[i];
            }
        }

        return max;
    }

    public static ResultsTable peakValue(ResultsTable data) {
        ResultsTable table = new ResultsTable();
        table.incrementCounter();
        int nColunms = data.getLastColumn();
        double peak = 0.0d;
        double[] pixelArray;
        double[] meanArray;

        if (data.getColumnHeading(0).equals("Time")) {
            for (int i = 1; i <= nColunms; i++) {
                IJ.showProgress(i, nColunms);
                if (i < nColunms) {
                    table.addValue("Peak Pixel " + (i + 1), peakDetect(data.getColumnAsDoubles(i)));
                } else {
                    table.addValue("Peak Mean ", peakDetect(data.getColumnAsDoubles(nColunms)));
                }
            }
        } else {
            for (int i = 0; i <= nColunms; i++) {
                if (i < nColunms) {
                    table.addValue("Peak Pixel " + (i + 1), peakDetect(data.getColumnAsDoubles(i)));
                } else {
                    table.addValue("Peak Mean ", peakDetect(data.getColumnAsDoubles(nColunms)));
                }
            }
        }

        return table;
    }

    private static double peakDetect(double[] values) {
        int nValues = values.length;
        double min = values[0];

        for (int v = 1; v < nValues; v++) {
            if (values[v] < min) {
                min = values[v];
            }
        }
        return min;
    }

    private static int peakIndexDetect(double[] values) {
        int nValues = values.length;
        int index = 0;
        double min = values[0];

        for (int v = 1; v < nValues; v++) {
            if (values[v] < min) {
                min = values[v];
                index = v;
            }
        }

        return index;
    }

    protected static double getTimeAcquisition(ImagePlus img) {
        ImagePlus aux = img.duplicate();
        double time1 = 0.0d;
        double time2 = 0.0d;

        aux.setSlice(1);
        time1 = Double.parseDouble(DicomTools.getTag(aux, "0008,0032"));
        aux.setSlice(2);
        time2 = Double.parseDouble(DicomTools.getTag(aux, "0008,0032"));

        return (time2 - time1);
    }

    public PerfusionMetrics() {

    }

//    public static ResultsTable fluxPixelTable(ImagePlus img, Roi roi) {
//        ResultsTable table = new ResultsTable();
//        int nSlice = img.getNSlices();
//        ImageProcessor ip;
//        ImageProcessor mask = roi.getMask();
//        Rectangle rec = roi.getBounds();
//
//        int count = 0;
//        for (int slice = 1; slice <= nSlice; slice++) {
//            img.setSlice(slice + (img.getFrame()-1)*nSlice);
//            ip = img.getProcessor();
//            table.incrementCounter();
//            for (int x = 0; x < rec.width; x++) {
//                for (int y = 0; y < rec.height; y++) {
//                    if (mask == null || mask.getPixel(x, y) != 0) {
//                        count++;
//                        if (img.getFileInfo().unit != null) {
//                            table.addValue("Time", Double.parseDouble(DicomTools.getTag(img, "0018,0080")) * (double) slice);
//                            table.addValue("Pixel: " + count + " Intensity", ip.getPixelValue(x + rec.x, y + rec.y));
//                        } else {
//                            table.addValue("Pixel: " + count + " Intensity", ip.getPixelValue(x + rec.x, y + rec.y));
//                        }
//                    }
//                }
//            }
//            count = 0;
//        }
//
//        return table;
//    }
    public static ResultsTable fluxPixelTable(ImagePlus img, Roi roi) {
        int nSlice = (int) Double.parseDouble(DicomTools.getTag(img, "0020,0105"));
//        int nFrames = img.getImageStackSize() / nSlice;
        double timeSlice = getTimeAcquisition(img);
        ResultsTable table = new ResultsTable();
        ImageProcessor ip;

        ImageProcessor mask = roi.getMask();
        Rectangle rec = roi.getBounds();
        int count = 0;
//        for (int frame = 0; frame < nFrames; frame++) {
//            table[frame] = new ResultsTable();
//            table[frame].setPrecision(6);
        for (int slice = 1; slice <= nSlice; slice++) {
            IJ.showStatus("Perfusion data into ROI...");
            IJ.showProgress(slice, nSlice);
            img.setSlice(slice + (img.getFrame() - 1) * nSlice);
            ip = img.getProcessor();
            table.incrementCounter();
            for (int x = 0; x < rec.width; x++) {
                for (int y = 0; y < rec.height; y++) {
                    if (mask == null || mask.getPixel(x, y) != 0) {
                        count++;
                        if (img.getFileInfo().unit != null) {
                            table.addValue("Time", timeSlice * (double) slice);
                            table.addValue("Pixel: " + count + " Intensity", ip.getPixelValue(x + rec.x, y + rec.y));
                        } else {
                            table.addValue("Pixel: " + count + " Intensity", ip.getPixelValue(x + rec.x, y + rec.y));
                        }
                    }
                }
            }
            count = 0;
        }

        return table;
    }

    public static ResultsTable CBV(ResultsTable data, ImagePlus img) {
        ResultsTable table = new ResultsTable();
        table.incrementCounter();
        int nColunms = data.getLastColumn();

        for (int i = 1; i <= nColunms; i++) {
            IJ.showProgress(i, nColunms);
            table.addValue("CBV Pixel " + i, calcCBV(data.getColumnAsDoubles(i), img));
        }
        table.addValue("CBV ROI Mean", meanCalc(data, img, METRICS.CBV));
//        table.addValue("Integral Std", stdIntegral(data));

        return table;
    }

    public static ResultsTable MTT(ResultsTable data, ImagePlus img) {
        ResultsTable table = new ResultsTable();
        table.incrementCounter();
        int nColunms = data.getLastColumn();
//        double timePerImage = getTimeAcquisition(img);

        for (int i = 1; i <= nColunms; i++) {
            IJ.showProgress(i, nColunms);
            table.addValue("MTT Pixel " + i, calcMTT(data.getColumnAsDoubles(i), img));
        }
        table.addValue("MTT ROI Mean", meanCalc(data, img, METRICS.MTT));
//        table.addValue("Integral Std", stdIntegral(data));

        return table;
    }

    public static ResultsTable CBF(ResultsTable data, ImagePlus img) {
        ResultsTable table = new ResultsTable();
        table.incrementCounter();
        int nColunms = data.getLastColumn();
//        double timePerImage = getTimeAcquisition(img);

        for (int i = 1; i <= nColunms; i++) {
            IJ.showProgress(i, nColunms);
            table.addValue("CBF Pixel " + i, calcCBF(data.getColumnAsDoubles(i), img));
        }
        table.addValue("CBF ROI Mean", meanCalc(data, img, METRICS.CBF));
//        table.addValue("Integral Std", stdIntegral(data));

        return table;
    }

    public static ResultsTable time2Peak(ResultsTable data, ImagePlus img) {
        ResultsTable table = new ResultsTable();
        table.incrementCounter();
        int nColunms = data.getLastColumn();
        double[] times = new double[nColunms + 1];

        if (DicomTools.getTag(img, "0018,0080") != null) {
            for (int i = 1; i <= nColunms; i++) {
                IJ.showProgress(i, nColunms);
                table.addValue("Time to Peack: Pixel " + i + " (" + img.getCalibration().getTimeUnit() + ")", timeCalculation(data.getColumnAsDoubles(i), img));
                times[i] = timeCalculation(data.getColumnAsDoubles(i), img);
            }
            table.addValue("Time to Peak: Mean (" + img.getCalibration().getTimeUnit() + ")", mean(times));
            table.addValue("Time to Peak: Std (" + img.getCalibration().getTimeUnit() + ")", std(times));
        } else {
            IJ.showMessage("Time to Peak", "No DICOM Header tag to Repetition Time was found.");
            table.addValue("Time to Peak", 0);
        }

        return table;
    }

    public static void plotData(ResultsTable data) {
        if ("Time".equals(data.getColumnHeading(0))) {
            Plot graph = new Plot("Plot", "Time", "Pixel Intensity", data.getColumnAsDoubles(0), data.getColumnAsDoubles(data.getLastColumn() / 2));
            graph.show();
        } else {
            IJ.showMessage("DICOM", "No DICOM Header was found to inform the Repetition Time");
        }

    }
}
