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
import com.alibaba.fastjson2.JSON;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;

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


    public Model loadMode(String modelName){
        String modelPath = "src/main/java/org/example/djl/mod/";
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
        public static void softmax(float[] input) {
            float[] output = new float[input.length];
            float sum = 0.0f;

            // Compute the exponentials and their sum
            for (int i = 0; i < input.length; i++) {
                output[i] = (float) Math.exp(input[i]);
                sum += output[i];
            }

            // Normalize by the sum to get probabilities
            for (int i = 0; i < output.length; i++) {
                output[i] /= sum;
            }
            float[] copy = Arrays.copyOf(output, output.length);
            Arrays.sort(copy);
            for (int i = 0; i < 3; i++) {
                float v = copy[copy.length - 1 - i];
                int index = Utils.indexOf(output, v);
                DecimalFormat decimalFormat = new DecimalFormat("#0.###"); // 设置格式化模式为保留五位小数
                String formattedNumber = decimalFormat.format(v*100);
                System.out.println(index+"概率"+ formattedNumber+"%");
            }

        }
        private static float[] getTopThreeProbabilities(float[] probabilities) {
            // Create a copy of the original probabilities array
            float[] copy = Arrays.copyOf(probabilities, probabilities.length);

            // Sort the copy in descending order
            Arrays.sort(copy);
            float[] topThree = new float[3];

            // Extract the top three probabilities
            for (int i = 0; i < 3; i++) {
                topThree[i] = copy[copy.length - 1 - i];
            }

            // Convert to percentage with three decimal places
            for (int i = 0; i < topThree.length; i++) {
                topThree[i] = Math.round(topThree[i] * 100000) / 1000.0f;
            }

            return topThree;
        }
        public int getNDArrayMaxIndex(NDArray tempArray){

            // 使用DJL内置的Softmax


            float[] floats = tempArray.toFloatArray();
//            System.err.println(JSON.toJSONString(floats));

            softmax(floats);

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

