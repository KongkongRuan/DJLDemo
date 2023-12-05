package org.example.djl;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 最终能调用成功的版本
 */
public class MNISTDetection {



    public int predictImg(String imgPath,Model model){
        Path picPath = Paths.get(imgPath);

        Predictor<Image, Integer> imageClassificationsPredictor = model.newPredictor(new MyTranslator());
        Integer predict = null;
        try {
            predict = imageClassificationsPredictor.predict(ImageFactory.getInstance().fromFile(picPath));
//            System.out.println("result-----");
//            System.out.println(predict);
//            System.out.println("------------");
            return predict;
        } catch (TranslateException | IOException e) {
            throw new RuntimeException(e);
        }

    }


    public Model loadMode(){
        String modelPath = "src/main/java/org/example/djl/mod/";
        String modelName = "model-j.pt";
        Path modelDir = Paths.get(modelPath);
        Model model = Model.newInstance(modelName, Device.cpu(),"PyTorch");
        try {
            model.load(modelDir,modelName);
        } catch (IOException | MalformedModelException e) {
            throw new RuntimeException(e);
        }
        return model;
    }

    private static final class MyTranslator implements Translator<Image, Integer> {

        @Override
        public Batchifier getBatchifier() {
            return Batchifier.STACK;
//            return null;
        }

        @Override
        public Integer processOutput(TranslatorContext translatorContext,NDList ndList) throws Exception {
            NDArray tempArray = ndList.get(0);

            int ndArrayMaxIndex = getNDArrayMaxIndex(tempArray);
//            NDArray ndArray2 = tempArray.argMax(1);

            return ndArrayMaxIndex;
        }

        public int getNDArrayMaxIndex(NDArray tempArray){
            float[] floats = tempArray.toFloatArray();
//            System.err.println(JSON.toJSONString(floats));
            float max = floats[0];
            int index = 0;
            for (int i = 1; i < floats.length; i++) {
                if(floats[i]>max){
                    max=floats[i];
                    index=i;
                }
            }
            return index;
        }
    @Override
    public NDList processInput(TranslatorContext ctx, Image myImage) {
        // 在这里对输入进行预处理，例如调整大小、归一化等
        Image bimage = myImage.resize(28,28,false);
        NDManager pyTorch = NDManager.newBaseManager("PyTorch");
        NDArray ndArray = bimage.toNDArray(pyTorch, Image.Flag.GRAYSCALE);
//        System.err.println("@@@input image");
//        System.out.println(JSON.toJSONString(ndArray.toUint8Array()));


        ndArray=NDImageUtils.toTensor(ndArray);
//        System.out.println("-----------------------");
//        System.out.println(JSON.toJSONString(ndArray.toUint8Array()));
//        ndArray=NDImageUtils.normalize(ndArray, new float[]{0.5f, }, new float[]{0.5f, });

//        PtNDArray ptNDArray=(PtNDArray)ndArray;
////        ptNDArray = JniUtils.unsqueeze(ptNDArray,0);
//        ndArray= ptNDArray;
        NDArray div = ndArray.div(255);
//        System.err.println("@@@input image");
//        System.err.println(JSON.toJSONString(div.toFloatArray()));
//        System.err.println(JSON.toJSONString(ndArray.toFloatArray()));

        return new NDList(ndArray);

//        return new NDList(NDImageUtils.toTensor(
//                NDImageUtils.resize(
//                        ImageFactory.getInstance().fromImage(myImage)
//                                .toNDArray(NDManager.newBaseManager(),
//                                        Image.Flag.GRAYSCALE) ,28,28)));

    }


    }
}

