package org.example.djl;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.CenterCrop;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MNISTInference3 {


    public static void main(String[] args) throws Exception {

        Path modelDir = Paths.get("src/main/java/org/example/djl/mod/");

        Model model = Model.newInstance("PyTorch",Device.cpu());  //MXNet

        model.load(modelDir, "model-j");



        Pipeline pipeline = new Pipeline();

        pipeline.add(new CenterCrop()).add(new Resize(224, 224)).add(new ToTensor());

        ImageClassificationTranslator translator = ImageClassificationTranslator.builder()

                .setPipeline(pipeline)

                .optSynsetArtifactName("model.txt")

                .optApplySoftmax(true)

                .build();



//        BufferedImage img = BufferedImageUtils.fromUrl("http://bigdata.res.yqxiu.cn/banff-4380804_960_720.jpg");
        Image img = ImageFactory.getInstance().fromUrl("http://www.abilitygame.cn/wp-content/uploads/2021/05/number_7.png");
        Predictor<Image, Classifications> predictor = model.newPredictor(translator);

        long s1 = System.currentTimeMillis();

        Classifications classifications = predictor.predict(img);

        System.out.printf(classifications.best().toString()+"===="+(System.currentTimeMillis()-s1));

    }



}

