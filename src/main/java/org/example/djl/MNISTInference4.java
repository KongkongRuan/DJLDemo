package org.example.djl;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;



import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MNISTInference4 {

    public static void main(String[] args) {
        try {

            String modelPath = "src/main/java/org/example/djl/mod/";
            String modelName = "model-j.pt";
//            String imgPath = "D:\\\\input\\\\202312041357073333333.png";
            String imgPath = "D:\\\\input\\\\number_7.png";

//            Path modelDir = Paths.get("/Users/myname/eclipse-workspace/myProject/src/ML/");
//            Model model = Model.newInstance("model.pt");
//            model.load(modelDir);

            Path modelDir = Paths.get(modelPath);
            Path picPath = Paths.get(imgPath);

            // 1. 加载 PyTorch 模型
            Model model = Model.newInstance(modelName, Device.cpu(),"PyTorch");
            model.load(modelDir,modelName);

            // 2. 定义 Translator
            Pipeline pipeline = new Pipeline();
            pipeline
                    .add(new Resize(28,28))
                    .add(new ToTensor())
                    .add(new Normalize(new float[]{0.5f, }, new float[]{0.5f, }));

            Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                    .setPipeline(pipeline)
                    .optSynsetArtifactName("model.txt")

//                    .optSynsetArtifactName(null)
//                    .optApplySoftmax(true)
                    .build();

            Predictor<Image, Classifications> imageClassificationsPredictor = model.newPredictor(translator);
            Classifications predict = imageClassificationsPredictor.predict(ImageFactory.getInstance().fromFile(picPath));
            System.out.println();





        } catch (ModelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TranslateException e) {
            throw new RuntimeException(e);
        }
    }
}

