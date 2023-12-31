package org.example.djl;

/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.paddlepaddle.zoo.cv.objectdetection.PpWordDetectionTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An example of inference using an object detection model.
 *
 * <p>See this <a
 * href="https://github.com/deepjavalibrary/djl/blob/master/examples/docs/object_detection.md">doc</a>
 * for information about this example.
 */
public final class WordDetection {

    private static final Logger logger = LoggerFactory.getLogger(WordDetection.class);

    private WordDetection() {}

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
         WordDetection.predict("D:\\input\\微信截图_20231128104456.png","D:\\output\\微信截图_soccer2.png");
//        System.out.println("识别结果: " + detection);
//        System.out.println(detection);
//        logger.info("{}", detection);
    }

    public static void predict(String inputFileName, String outputFileName) throws IOException, ModelException, TranslateException {
//        Path imageFile = Paths.get("src/test/resources/dog_bike_car.jpg");
        File file = new File(inputFileName);
        Path imageFile = file.toPath();

        Image img = ImageFactory.getInstance().fromFile(imageFile);

        String backbone;
        if ("TensorFlow".equals(Engine.getDefaultEngineName())) {
            backbone = "mobilenet_v2";
        } else {
            backbone = "resnet50";
        }

//        Criteria<Image, DetectedObjects> criteria =
//                Criteria.builder()
////                        .optApplication(Application.CV.OBJECT_DETECTION)
////                        .optApplication(Application.CV.POSE_ESTIMATION)
////                        .optApplication(Application.CV.ACTION_RECOGNITION)
//                        .optApplication(Application.CV.WORD_RECOGNITION)
//                        .setTypes(Image.class, DetectedObjects.class)
//                        .optFilter("backbone", backbone)
//                        .optEngine(Engine.getDefaultEngineName())
//                        .optProgress(new ProgressBar())
//                        .build();
        Criteria<Image, DetectedObjects> criteria = Criteria.builder()
                .optEngine("PaddlePaddle")
                .setTypes(Image.class, DetectedObjects.class)
                .optModelUrls("https://resources.djl.ai/test-models/paddleOCR/mobile/det_db.zip")
                .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
                .build();

        Predictor<Image, DetectedObjects> imageDetectedObjectsPredictor = criteria.loadModel().newPredictor();
        DetectedObjects predict = imageDetectedObjectsPredictor.predict(img);
        Image newImage = img.duplicate();
        newImage.drawBoundingBoxes(predict);
        newImage.getWrappedImage();
        newImage.save(Files.newOutputStream(Paths.get(outputFileName)), "png");
//        try (ZooModel<Image, String> model = criteria.loadModel()) {
//            try (Predictor<Image, String> predictor = model.newPredictor()) {
//                return predictor.predict(img);
////                System.out.println("识别结果: " + detection);
////                saveBoundingBoxImage(img, detection, outputFileName);
////                return detection;
//            }
//        }
    }

    private static void saveBoundingBoxImage(Image img, DetectedObjects detection, String outputFileName)
            throws IOException {
//        Path outputDir = Paths.get("D:\\output");
//        Files.createDirectories(outputDir);
        boolean mkdirs = new File(outputFileName).getParentFile().mkdirs();

        img.drawBoundingBoxes(detection);

        Path imagePath = Paths.get(outputFileName);
        // OpenJDK can't save jpg with alpha channel
        img.save(Files.newOutputStream(imagePath), "png");
        logger.info("Detected objects image has been saved in: {}", imagePath);
    }
}
