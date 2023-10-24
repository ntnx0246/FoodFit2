package com.example.foodfit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import android.graphics.drawable.BitmapDrawable;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIActivity extends FragmentActivity {
    private Button captureButton;
    private Button apiRequestButton;
    private ImageView capturedImage;
    private TextView apiResultTextView;
    private static final String TAG = "APIActivity";
    private final OkHttpClient okHttpClient = new OkHttpClient();

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    capturedImage.setImageBitmap(imageBitmap);
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiactivity);

        requestCameraPermission();

        captureButton = findViewById(R.id.captureButton);
        apiRequestButton = findViewById(R.id.apiRequestButton);
        capturedImage = findViewById(R.id.capturedImage);
        apiResultTextView = findViewById(R.id.apiResultTextView);

        captureButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mGetContent.launch(takePictureIntent);
        });

        apiRequestButton.setOnClickListener(v -> {
            // Check if drawable is not null and it's an instance of BitmapDrawable
            Drawable drawable = capturedImage.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                Bitmap imageBitmap = ((BitmapDrawable) drawable).getBitmap();
                if (imageBitmap != null) {
                    fetchData(imageBitmap);
                }
            } else {
                Toast.makeText(this, "Please capture an image first!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchData(Bitmap imageBitmap) {
        Executors.newSingleThreadExecutor().execute(() -> {
            File file = bitmapToFile(imageBitmap);
            if (file != null) {
                makeApiRequest(file);
            }
        });
    }

    private File bitmapToFile(Bitmap bitmap) {
        File file = new File(getCacheDir(), "tempImage.jpg");
        try {
            file.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, bos);
            byte[] bitmapData = bos.toByteArray();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bitmapData);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return file;
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    // Define a constant for the request code
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted
                } else {
                    // Permission denied
                    Toast.makeText(this, "Camera permission is required to capture images.", Toast.LENGTH_LONG).show();
                    captureButton.setEnabled(false);  // Disable the capture button if permission is denied
                }
                return;
            }
        }
    }

    private RequestBody prepareRequestBody(File file) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "tempImage.jpg", RequestBody.create(file, MediaType.get("image/jpeg")))
                .build();
    }

    private void makeApiRequest(File file) {
        String url = "https://api.someDomain.com/v2/image/recognition/type/v1.0";
        String apiKey = "b33ff2b0675815f3b4b8cb0c1a0f265e3eac45a5";  // Remember to handle this securely!

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "tempImage.jpg", RequestBody.create(file, MediaType.get("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", apiKey)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> apiResultTextView.setText("Error fetching data from API."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> apiResultTextView.setText(responseBody));
                } else {
                    runOnUiThread(() -> apiResultTextView.setText("Error fetching data from API. Status code: " + response.code()));
                }
                // Clean up temporary file
                file.delete();
            }
        });
    }


}
