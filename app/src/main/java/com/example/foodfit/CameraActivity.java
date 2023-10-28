package com.example.foodfit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class CameraActivity extends FragmentActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView capturedImage;
    private TextView apiResultTextView;

    private Interpreter interpreter;

    private ModelHandler modelHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraactivity); // Ensure you use the correct layout filename here
        modelHandler = new ModelHandler(this);
        capturedImage = findViewById(R.id.capturedImage);
        apiResultTextView = findViewById(R.id.apiResultTextView);

        Button captureButton = findViewById(R.id.captureButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        initializeModel();
        Button apiRequestButton = findViewById(R.id.apiRequestButton);
        apiRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processImage(capturedImage); // Placeholder function to process image through model
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            capturedImage.setImageBitmap(imageBitmap);
        }
    }

    private void processImage(ImageView imageView) {
        // Convert the ImageView's Bitmap into a format the model can understand
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteBuffer inputBuffer = convertBitmapToByteBuffer(bitmap);
        int model_size = 2024;

        // Initialize the output buffer based on your model's specifics
        float[][] output = new float[1][model_size];

        // Make sure the interpreter is initialized
        if (interpreter != null) {
            interpreter.run(inputBuffer, output);
            String result = modelHandler.handleModelOutput(output[0]); // Grab the result after processing
            apiResultTextView.setText(result); // Update the TextView with the result
        } else {
            apiResultTextView.setText("Model not blazing yet!"); // Just a little Phoenix touch!
        }
    }


    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        int bytesPerChannel = 4; // Assuming float model input type
        ByteBuffer buffer = ByteBuffer.allocateDirect(bytesPerChannel * bitmap.getWidth() * bitmap.getHeight() * 3); // 3 for RGB

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int pixel = bitmap.getPixel(x, y);

                // Normalize and extract channels (assuming model takes normalized input)
                buffer.putFloat(((pixel >> 16) & 0xFF) / 255.0f);
                buffer.putFloat(((pixel >> 8) & 0xFF) / 255.0f);
                buffer.putFloat((pixel & 0xFF) / 255.0f);
            }
        }
        return buffer;
    }

    private void initializeModel() {
        CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                .requireWifi()
                .build();

        FirebaseModelDownloader.getInstance()
                .getModel("Food-Detector", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
                .addOnSuccessListener(model -> {
                    File modelFile = model.getFile();
                    if (modelFile != null) {
                        interpreter = new Interpreter(modelFile);
                    }
                });
    }
}
