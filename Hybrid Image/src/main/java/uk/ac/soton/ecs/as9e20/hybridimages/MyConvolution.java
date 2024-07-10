package uk.ac.soton.ecs.as9e20.hybridimages;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;
import java.util.ArrayList;
import java.util.List;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
    private float[][] kernel;

    public MyConvolution(float[][] kernel) {
        //kernel = [row][col]
        this.kernel = kernel;
    }

    public void processImage(FImage image) {
        //convolve image with kernel and store result back in image
        //hint: use FImage#internalAssign(FImage) to set the contents of temporary buffer image to image
        float[][] newKernel = new float[kernel.length][kernel[0].length];
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                newKernel[kernel.length - 1 - i][kernel[0].length - 1 - j] = kernel[i][j];
            }
        }

        FImage clone = image.clone();

        int kernelX = newKernel.length;
        int kernelY = newKernel[0].length;
        int paddingX;
        int paddingY;


        if (((kernelX + 1)/2) % 2 == 0) {
            paddingX = (kernelX + 1) / 2;
        } else {
            paddingX = ((kernelX + 1) / 2) + 1;
        }

        if (((kernelY + 1)/2) % 2 == 0) {
            paddingY = (kernelY + 1) / 2;
        } else {
            paddingY = ((kernelY + 1) / 2) + 1;
        }

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                float conv = 0.0f;

                for (int i = 0; i < kernelX; i++) {
                    for (int j = 0; j < kernelY; j++) {
                        int imageX = x + j - paddingX;
                        int imageY = y + i - paddingY;

                        if (imageX>=0 && imageX < clone.width && imageY>= 0 && imageY < clone.height) {
                            conv += newKernel[i][j] * clone.pixels[imageY][imageX];
                        }
                    }
                }

                clone.pixels[y][x] = conv;
            }
        }
        image.internalAssign(clone);
    }


}