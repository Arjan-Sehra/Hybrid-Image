package uk.ac.soton.ecs.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import uk.ac.soton.ecs.as9e20.hybridimages.MyConvolution;

import java.io.File;
import java.io.IOException;

public class MyHybridImages {

    public static MBFImage makeHybrid(MBFImage lowImage, float lowSigma, MBFImage highImage, float highSigma) {
        float[][] lowKernel = makeGaussianKernel(lowSigma);
        float[][] highKernel = makeGaussianKernel(highSigma);

        MyConvolution lowConvolution = new MyConvolution(lowKernel);
        lowImage.processInplace(lowConvolution);

        MyConvolution highConvolution = new MyConvolution(highKernel);
        MBFImage blurredHighImage = highImage.clone();
        blurredHighImage.processInplace(highConvolution);
        MBFImage highPassImage = highImage.subtract(blurredHighImage);

        lowImage.addInplace(highPassImage);

        return lowImage;
    }

    public static float[][] makeGaussianKernel(float sigma) {
        int size = (int) Math.floor(8 * sigma + 1);
        if (size % 2 == 0) {
            size++;
        }

        float[][] kernel = new float[size][size];
        int center = size / 2;
        float sum = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int x = i - center;
                int y = j - center;
                float value = (float) Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
                kernel[i][j] = value;
                sum += value;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                kernel[i][j] /= sum;
            }
        }

        return kernel;
    }


    public static void main(String[] args) throws IOException {
        MBFImage image = ImageUtilities.readMBF(new File("src/bear.bmp"));
        MBFImage image2 = ImageUtilities.readMBF(new File("src/owl.bmp"));

        MyHybridImages myHybridImages =  new MyHybridImages();
        MBFImage newImage = myHybridImages.makeHybrid(image, 3, image2, 3);
        DisplayUtilities.display(newImage);
        File outputFile = new File("HybridImage.png");
        try {
            ImageUtilities.write(newImage, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}