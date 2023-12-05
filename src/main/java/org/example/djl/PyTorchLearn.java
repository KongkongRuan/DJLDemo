package org.example.djl;


import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.CenterCrop;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.training.util.DownloadUtils;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PyTorchLearn {
    public static void main(String[] args) throws IOException, TranslateException, MalformedModelException, ModelNotFoundException {

        Path modelDir = Paths.get("D:\\java\\project\\djlDemo\\src\\main\\java\\org\\example\\djl\\mod");
        Model model = Model.newInstance("model-j.pt");
        model.load(modelDir, "model-j.pt");
        Pipeline pipeline = new Pipeline();
        pipeline.add(new CenterCrop()).add(new Resize(28, 28)).add(new ToTensor());

        Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                .setPipeline(pipeline)
//                .optSynsetArtifactName("synset.txt")
                .optApplySoftmax(true)
                .build();

        Image img = ImageFactory.getInstance().fromUrl("D:\\input\\202312041357073333333.png");
        img.getWrappedImage();
        Predictor<Image, Classifications> predictor = model.newPredictor(translator);
        Classifications classifications = predictor.predict(img);
        System.out.println(classifications);
    }
}


