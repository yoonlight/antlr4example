package com.khubla.antlr4example.asp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import com.khubla.antlr4example.common.Common.Common;
import com.khubla.antlr4example.common.FeatureEntities.ProgramFeatures;

public class ExtractFeaturesTask implements Callable<Void> {
    String code;

    public ExtractFeaturesTask(String code) {
        this.code = code;
    }

    @Override
    public Void call() throws Exception {
		processFile();
        return null;
    }

    public void processFile() {
        ArrayList<ProgramFeatures> features;
        try {
            features = extractSingleFile();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (features == null) {
            return;
        }

        String toPrint = featuresToString(features);
        if (toPrint.length() > 0) {
            System.out.println(toPrint);
        }
    }

    public ArrayList<ProgramFeatures> extractSingleFile() throws IOException {
        FeatureExtractor featureExtractor = new FeatureExtractor();
        ArrayList<ProgramFeatures> features = featureExtractor.extractFeatures(code);
        return features;
    }

    public String featuresToString(ArrayList<ProgramFeatures> features) {
        if (features == null) {
            return Common.EmptyString;
        }

        List<String> methodsOutputs = new ArrayList<>();

        for (ProgramFeatures singleMethodFeatures : features) {
            StringBuilder builder = new StringBuilder();

            String toPrint = Common.EmptyString;
            toPrint = singleMethodFeatures.toString();

            builder.append(toPrint);

            methodsOutputs.add(builder.toString());

        }
        return StringUtils.join(methodsOutputs, "\n");
    }
}