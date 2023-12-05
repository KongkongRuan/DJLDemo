package org.example.djl;

import ai.djl.Application;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MyImage {

    private BufferedImage image;  // 图像数据

    private MyImage(BufferedImage image) {
        this.image = image;
    }

    // 通过文件路径创建 MyImage 对象
    public static MyImage fromPath(Path imagePath) {
        try {
            BufferedImage image = loadImage(imagePath.toString());

            return new MyImage(image);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error loading image from path: " + imagePath, e);
        }
    }
    public static BufferedImage loadImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // 获取图像数据
    public BufferedImage getImage() {
        return image;
    }
}

