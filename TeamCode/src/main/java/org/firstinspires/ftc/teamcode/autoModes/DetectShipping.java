package org.firstinspires.ftc.teamcode.autoModes;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

/*
    @author Declan J. Scott
*/

public class DetectShipping extends OpenCvPipeline {

    double avgX = 0;
    double avgY = 0;
    double estimatedDistance;

    @Override
    public Mat processFrame(Mat input) {
        ArrayList<Mat> channels = new ArrayList<>();
        Core.split(input, channels);
        // Isolate green channel
        Mat green = new Mat();
        green = channels.get(1);

        // Threshold, subtract, threshold again
        Mat threshold = new Mat();
        Imgproc.threshold(green, threshold, 200, 255, Imgproc.THRESH_BINARY);
        Core.subtract(threshold, channels.get(0), threshold);
        Imgproc.threshold(threshold, threshold, 60, 255, Imgproc.THRESH_BINARY);

        double xSum = 0;
        double ySum = 0;
        int pixelCount = 0;
        for(int x = 0; x < green.size().width; x++)
        {
            for(int y = 0; y < green.size().height; y++)
            {
                double pixelValue = threshold.get(y,x)[0];
                if(pixelValue > 100)
                {
                    pixelCount++;
                    xSum += x;
                    ySum += y;
                }
            }
        }

        // Do some of them sick calculations
        avgX = xSum / pixelCount;
        avgY = ySum / pixelCount;
        estimatedDistance = (input.size().width * input.size().height) - pixelCount;

        // Draw circle for debug
        Point coordinates = new Point(avgX, avgY);
        Imgproc.circle(input, coordinates, 25, new Scalar(255, 0, 255));

        return input;
    }

    public double[] ShippingElementCoordinates()
    {
        double[] coordinates = {avgX, avgY};
        return coordinates;
    }

    public double ShippingElementDistance()
    {
        return estimatedDistance;
    }
}
