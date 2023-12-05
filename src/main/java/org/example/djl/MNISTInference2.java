package org.example.djl;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.Shape;
import ai.djl.pytorch.jni.JniUtils;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MNISTInference2 {

    public static void main(String[] args) {
        try {

            String modelPath = "org/example/djl/mod/model-j.pt";
            String modelName = "model-j.pt";
            String imgPath = "D:\\\\input\\\\202312041357073333333.png";

            Path modelDir = Paths.get(modelPath);
            Path picPath = Paths.get(imgPath);

            // 1. 加载 PyTorch 模型
//            Model model = Model.newInstance("PyTorch", Device.cpu());
//            model.load(modelDir,modelName);
            // 2. 定义 Translator
            Pipeline pipeline = new Pipeline();
            pipeline.add(new Resize(28,28))
                    .add(new ToTensor())
                    .add(new Normalize(
                            new float[]{0.5f, },
                            new float[]{0.5f, }));

            Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                    .setPipeline(pipeline)
                    .optSynsetArtifactName("model.txt")
//                    .optSynsetArtifactName(null)
                    .optApplySoftmax(true)
                    .build();

            Criteria<NDList, NDList> criteria =
                    Criteria.builder()
                            .setTypes(NDList.class, NDList.class)
//                            .optEngine("pytorch")
//                            .optModelPath(modelDir)
//                            .optModelPath(Paths.get("D:\\java\\project\\djlDemo\\src\\main\\java\\org\\example\\djl"))
                            .optModelName("mod\\model-j.pt")
//                            .optTranslator(translator)
                            .build();

            ZooModel<NDList, NDList> imageClassificationsZooModel = criteria.loadModel();

            // 3. 创建 Predictor
            try (Predictor<NDList, NDList> predictor = imageClassificationsZooModel.newPredictor()) {

                Image image = ImageFactory.getInstance().fromUrl("http://www.abilitygame.cn/wp-content/uploads/2021/05/number_7.png");
                image.getWrappedImage();
                // 4. 示例输入
                NDList list = processImageInput(image);
                NDArray result = predictor.predict(list).get(0).argSort();
                System.out.println("预测结果是:" + result.get(new NDIndex(0,9)).getLong());

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
    private static NDList processImageInput(Image input) {
        NDManager manager = NDManager.newBaseManager();
        NDArray array = input.toNDArray( manager, Image.Flag.GRAYSCALE );
        array = array.transpose(2, 0, 1).flip(0);
        NDArray allOnes = manager.ones(new Shape(1, 28, 28));
        array = allOnes.sub(array.div(255));
        array = array.expandDims(0);
        return new NDList(array);
    }
}

