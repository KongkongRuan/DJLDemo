package org.example.djl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Utils {

    public static void main(String[] args) {
        String imagePath = "D:\\input\\my\\";
        String outputPath = "D:\\input\\javaConvert\\";

        try {
            for (int i = 0; i < 10; i++) {
                BufferedImage originalImage = ImageIO.read(new File(imagePath+i+".png"));

                // Crop image
                int margin = 5;
                BufferedImage croppedImage = ImageBoundingBox.cropImage(originalImage, margin);

                // Convert to grayscale
                BufferedImage grayscaleImage = convertToGrayScale(croppedImage);
//                BufferedImage grayscaleImage = convertToGrayscale(originalImage);



                // Invert colors
                BufferedImage invertedImage = invertColors(grayscaleImage);



                // Save the processed image
            ImageIO.write(invertedImage, "png", new File(outputPath+i+".png"));
            }

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage convertToGrayscale(BufferedImage originalImage) {
        BufferedImage grayscaleImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g = grayscaleImage.createGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();

        return grayscaleImage;
    }

    public static BufferedImage invertColors(BufferedImage grayscaleImage) {
        BufferedImage invertedImage = new BufferedImage(
                grayscaleImage.getWidth(),
                grayscaleImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < grayscaleImage.getHeight(); y++) {
            for (int x = 0; x < grayscaleImage.getWidth(); x++) {
                int color = grayscaleImage.getRGB(x, y);
                int invertedColor = 255 - (color & 0xFF); // Ensure the value is within 0 to 255
                int newColor = (invertedColor << 16) | (invertedColor << 8) | invertedColor;
                invertedImage.setRGB(x, y, newColor);
            }
        }

        return invertedImage;
    }



    public static BufferedImage convertToGrayScale(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // 遍历原始图像的像素并将其灰度化
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);
                int grayValue = calculateGrayValue(rgb);
                int grayPixel = (grayValue << 16) | (grayValue << 8) | grayValue;
                grayImage.setRGB(x, y, grayPixel);
            }
        }

        return grayImage;
    }

    private static int calculateGrayValue(int rgb) {
        // 从RGB颜色中提取红、绿、蓝分量
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        // 计算灰度值（平均值法）
        return (red + green + blue) / 3;
    }

    public static int indexOf(float[] array, float value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
