package org.example.djl;

import ai.djl.Application;
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
import ai.djl.pytorch.zoo.PtModelZoo;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.dataset.Batch;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MNISTInference {

    public static void main(String[] args) {
        try {

            String modelPath = "org/example/djl/mod/model-j.pt";
//            String modelName = "model-j.pt";
            String modelName = "traced_resnet50";
//            String imgPath = "D:\\\\input\\\\202312041357073333333.png";
            String imgPath = "D:\\\\input\\\\number_7.png";

            Path modelDir = Paths.get(modelPath);
            Path picPath = Paths.get(imgPath);

            // 1. 加载 PyTorch 模型
//            Model model = Model.newInstance("PyTorch", Device.cpu());
//            model.load(modelDir,modelName);
            // 2. 定义 Translator
            Pipeline pipeline = new Pipeline();
            pipeline
                    .add(new Resize(28,28))
                    .add(new ToTensor())
                    .add(new Normalize(new float[]{0.5f, }, new float[]{0.5f, }));

            Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                    .setPipeline(pipeline)
//                    .optSynsetArtifactName("model.txt")
                    .optSynsetArtifactName("synset.txt")

//                    .optSynsetArtifactName(null)
//                    .optApplySoftmax(true)
                    .build();

            Criteria<Image, Classifications> criteria =
                    Criteria.builder()
                            .setTypes(Image.class, Classifications.class)
                            .optEngine("PyTorch")
//                            .optModelZoo(PtModelZoo.getModelZoo(PtModelZoo.GROUP_ID))
//                            .optModelPath(modelDir)
//                            .optModelPath(Paths.get("D:\\java\\project\\djlDemo\\src\\main\\java\\org\\example\\djl"))
//                            .optModelName("mod\\model-j.pt")
                            .optModelName("traced_resnet50")
                            .optTranslator(translator)
                            .optProgress(new ProgressBar())
                            .build();

            ZooModel<Image, Classifications> imageClassificationsZooModel = criteria.loadModel();

            // 3. 创建 Predictor
            try (Predictor<Image, Classifications> predictor = imageClassificationsZooModel.newPredictor()) {
                // 4. 示例输入
                Image img = ImageFactory.getInstance().fromFile(picPath);
                Object wrappedImage = img.getWrappedImage();


                // 5. 进行推理
                Classifications predictions = predictor.predict(img);

                // 6. 处理预测结果

            } catch (TranslateException e) {
                throw new RuntimeException(e);
            }

        } catch (ModelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

