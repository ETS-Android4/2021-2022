package org.firstinspires.ftc.teamcode.autoModes;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

// At this point in time. The pipeline just converts the image to gray to test if the library works correctly.
public class DetectionPipeline extends OpenCvPipeline {
    Mat gray = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, gray, Imgproc.COLOR_RGB2GRAY);
        return gray;
    }
}
