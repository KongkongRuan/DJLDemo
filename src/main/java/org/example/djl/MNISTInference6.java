package org.example.djl;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.pytorch.engine.PtNDArray;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MNISTInference6 {

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





            Predictor<Image, NDArray> imageClassificationsPredictor = model.newPredictor(new MyTranslator());
            NDArray predict = imageClassificationsPredictor.predict(ImageFactory.getInstance().fromFile(picPath));
            System.out.println(predict.get(0));
            System.out.println(predict);




        } catch (ModelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TranslateException e) {
            throw new RuntimeException(e);
        }
    }
    private static final class MyTranslator implements Translator<Image, NDArray> {

        @Override
        public Batchifier getBatchifier() {
            return Batchifier.STACK;
        }

        @Override
        public NDArray processOutput(TranslatorContext translatorContext,NDList ndList) throws Exception {
            NDArray tempArray = ndList.get(0);
            return tempArray;
        }

    @Override
    public NDList processInput(TranslatorContext ctx, Image myImage) {
        // 在这里对输入进行预处理，例如调整大小、归一化等
        Image bimage = myImage.resize(28,28,false);
        NDManager pyTorch = NDManager.newBaseManager("PyTorch");
        NDArray ndArray = bimage.toNDArray(pyTorch, Image.Flag.GRAYSCALE);
        ndArray=NDImageUtils.toTensor(ndArray);
        PtNDArray ptNDArray=(PtNDArray)ndArray;
//        ptNDArray = JniUtils.unsqueeze(ptNDArray,0);
        ndArray= ptNDArray;
        return new NDList(ndArray.div(255.0));

//        return new NDList(NDImageUtils.toTensor(
//                NDImageUtils.resize(
//                        ImageFactory.getInstance().fromImage(myImage)
//                                .toNDArray(NDManager.newBaseManager(),
//                                        Image.Flag.GRAYSCALE) ,28,28)));

    }


    }
}

