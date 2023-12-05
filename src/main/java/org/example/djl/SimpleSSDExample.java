//package org.example.djl;
//
//import ai.djl.modality.cv.DetectedObjects;
//import ai.djl.modality.cv.ImageVisualization;
//import ai.djl.modality.cv.util.BufferedImageUtils;
//import ai.djl.mxnet.zoo.MxModelZoo;
//import ai.djl.repository.zoo.ZooModel;
//import ai.djl.training.util.ProgressBar;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//
//public class SimpleSSDExample {
//    public static void main(String[] args) throws Exception{
//        // Get image file path
////        BufferedImage img = BufferedImageUtils
////                .fromUrl("https://raw.githubusercontent.com/dmlc/web-data/master/gluoncv/pose/soccer.png");
//        //BufferedImage img =
//        File file = new File("D:\\soccer.png");
//        BufferedImage img=BufferedImageUtils.fromFile(file.toPath());
//        //Get resnet model from model zoo.
//        ZooModel<BufferedImage, DetectedObjects> model =
//                MxModelZoo.SSD.loadModel(new ProgressBar());
//        //Create a new predictor from model and predict on image.
//        DetectedObjects predictResult = model.newPredictor().predict(img);
//        // Draw Bounding boxes on image
//        ImageVisualization.drawBoundingBoxes(img, predictResult);
//        //Save result
//        ImageIO.write(img, "png", new File("ssd.png"));
//        model.close();
//    }
//}
