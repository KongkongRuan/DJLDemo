//package org.example.djl;
//
//import ai.djl.Application;
//import ai.djl.Model;
//import ai.djl.ModelException;
//import ai.djl.inference.Predictor;
//import ai.djl.modality.Classifications;
//import ai.djl.ndarray.NDList;
//import ai.djl.ndarray.NDManager;
//import ai.djl.translate.Batchifier;
//import ai.djl.translate.Translator;
//import ai.djl.translate.TranslatorContext;
//import ai.djl.util.Utils;
//
//import java.awt.image.BufferedImage;
//import java.nio.file.Path;
//
//public class MyImageTranslator implements Translator<MyImage, Classifications> {
//
//    @Override
//    public NDList processInput(TranslatorContext ctx, MyImage myImage) {
//        // 在这里对输入进行预处理，例如调整大小、归一化等
//        BufferedImage bimage = myImage.getImage();
//
//        // 调整大小为 28x28
//        BufferedImage resizedImage = Utils.resize(bimage, 28, 28);
//
//
//        // 转换为张量
//        try (NDManager manager = NDManager.newBaseManager()) {
//            // 这里假设模型期望的输入是归一化到 [0, 1] 的图像
//            // 实际需根据模型的输入要求进行处理
//            return new NDList(Utils.toTensor(manager, resizedImage).div(255.0));
//        }
//    }
//
//    @Override
//    public NDList processOutput(TranslatorContext ctx, ai.djl.ndarray.NDList outputs) {
//        // 在这里对输出进行后处理，如果需要的话
//        return outputs.singletonOrThrow();
//    }
//
//    @Override
//    public Batchifier getBatchifier() {
//        return null;  // 如果不是批次处理，返回 null
//    }
//
//    @Override
//    public NDList processInput(TranslatorContext ctx, MyImage input) throws Exception {
//        return null;
//    }
//}
//
//
