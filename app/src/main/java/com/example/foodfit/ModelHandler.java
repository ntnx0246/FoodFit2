package com.example.foodfit;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ModelHandler {

    private List<String> labels = new ArrayList<>();

    public ModelHandler(Context context) {
        loadLabels(context);
    }

    private void loadLabels(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("probability-labels.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                labels.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String handleModelOutput(float[] probabilities) {
        int maxIndex = 0;
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > probabilities[maxIndex]) {
                maxIndex = i;
            }
        }
        return labels.get(maxIndex);
    }
}
