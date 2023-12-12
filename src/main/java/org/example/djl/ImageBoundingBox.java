package org.example.djl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageBoundingBox {

    public static void main(String[] args) {
        String imagePath = "D:\\input\\my\\1.png";

        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));

//            // 获取图像内容的边界框
//            BoundingBox bbox = getBoundingBox(originalImage);
//
//            // 在这里可以根据需要扩大或缩小边界框
//            bbox.expand(5);
//
//            // 裁剪图像内容
//            BufferedImage croppedImage = originalImage.getSubimage(
//                    bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
            BufferedImage croppedImage=cropImage(originalImage,5);

            // 将裁剪后的图像保存到文件
            ImageIO.write(croppedImage, "png", new File("D:\\input\\javaConvert\\2-1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static BufferedImage cropImage(BufferedImage image, int margin) {
        // 获取图像内容的边界框
        ImageBoundingBox.BoundingBox bbox = getBoundingBox(image);

        // 在这里可以根据需要扩大或缩小边界框
        bbox.expand(margin);

        // 裁剪图像内容
        return image.getSubimage(
                bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
    }

    public static BoundingBox getBoundingBox(BufferedImage image) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                if ((rgb & 0xFF) != 255) {  // Assuming white background
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        return new BoundingBox(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    public static class BoundingBox {
        private int x;
        private int y;
        private int width;
        private int height;

        public BoundingBox(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public void expand(int margin) {
            this.x = Math.max(x - margin, 0);
            this.y = Math.max(y - margin, 0);
            this.width += 2 * margin;
            this.height += 2 * margin;
        }
    }
}

