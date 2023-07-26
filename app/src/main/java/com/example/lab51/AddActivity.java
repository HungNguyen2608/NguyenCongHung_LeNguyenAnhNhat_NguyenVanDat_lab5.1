package com.example.lab51;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private TextView txtImgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Button btnAdd = findViewById(R.id.btnAdd);
        TextView txtTitle = findViewById(R.id.inputTitle);
        txtImgURL = findViewById(R.id.inputImgURL);
        TextView txtURL = findViewById(R.id.inputURL);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = txtTitle.getText().toString();
                String imgURL = txtImgURL.getText().toString();
                String url = txtURL.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("Title", title);
                intent.putExtra("ImgURL", imgURL);
                intent.putExtra("URL", url);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ImageButton imgbtn = findViewById(R.id.imageButton);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });
    }
    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");

        String[] options = {"Take Photo", "Choose from Gallery"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Check camera permission and take photo
                        if (ContextCompat.checkSelfPermission(AddActivity.this, android.Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_GRANTED) {
                            openCamera();
                        } else {
                            ActivityCompat.requestPermissions(AddActivity.this,
                                    new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
                        }
                        break;
                    case 1:
                        // Check gallery permission and choose image
                        if (ContextCompat.checkSelfPermission(AddActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            openGallery();
                        } else {
                            ActivityCompat.requestPermissions(AddActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
                        }
                        break;
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        String imageUrl = saveImageAndGetPath(imageBitmap);
                        txtImgURL.setText(imageUrl);
                    }
                }
            } else if (requestCode == REQUEST_GALLERY && data != null) {
                Uri imageUri = data.getData();
                txtImgURL.setText(imageUri.toString());
            }
        }
    }
    private String saveImageAndGetPath(Bitmap imageBitmap) {
        String imagePath = null;
        try {
            // Save the image to the internal storage
            File imageFile = createImageFile();
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Get the absolute path of the saved image
            imagePath = imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return imageFile;
    }
}