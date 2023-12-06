package org.example;

import ai.djl.Model;
import org.example.djl.MNISTDetection;

public class Main {
    public static void main(String[] args) {

        // 1. 加载 PyTorch 模型
        MNISTDetection mnistDetection = new MNISTDetection();
        String modelName = "j-cnn-gpu.pt";
//        String modelName = "j-cnn-cpu.pt";

        Model model = mnistDetection.loadMode(modelName);

//        String imgPath = "D:\\input\\202312041357073333333.png";
//        int result1 = mnistDetection.predictImg(imgPath, model);
//        System.out.println(result1);

//        String imgPath = "D:\\input\\my\\"; int count = 5;
        String imgPath = "D:\\input\\"; int count = 10;
        for (int i = 0; i < count; i++) {
            int result = mnistDetection.predictImg(imgPath + i + ".png", model);
            System.out.println("Predict:"+result);
            System.out.println("Real:"+i);
        }


    }
}