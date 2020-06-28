package org.techtown.capstoneprojectcocktail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CocktailUploadActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    private FirebaseAuth mAuth;
    private boolean imageCheck = false;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseStorage storage;
    String stringForCocktailName;
    String stringForCocktailHowToMake;
    String stringForCocktailDescription;
    String TAG = "DocSnippets";
    File photoFile;
    int Recipe_count;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cocktail_upload_activity);
        Button takePictureButtonCocktailUpload = findViewById(R.id.button_takePicture_cocktail_upload);
        takePictureButtonCocktailUpload.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view){
                //Snackbar.make(view, "사진을 찍자", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                sendTakePhotoIntent();
            }
        });

        TextInputLayout textInputLayoutForCocktailName = findViewById(R.id.textInputLayout_cocktailName_cocktail_upload);
        textInputLayoutForCocktailName.setCounterEnabled(true);
        textInputLayoutForCocktailName.setCounterMaxLength(20);
        final EditText editTextForCocktailName = textInputLayoutForCocktailName.getEditText();
        editTextForCocktailName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>20) {
                    editTextForCocktailName.setError("20자 이하로 입력해주세요!");
                } else {
                    editTextForCocktailName.setError(null);
                }
            }
        });

        TextInputLayout textInputLayoutForCocktailHowToMake = findViewById(R.id.textInputLayout_cocktailHowToMake_cocktail_upload);
        textInputLayoutForCocktailHowToMake.setCounterEnabled(true);
        textInputLayoutForCocktailHowToMake.setCounterMaxLength(300);
        final EditText editTextForCocktailHowToMake = textInputLayoutForCocktailHowToMake.getEditText();
        editTextForCocktailHowToMake.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>300) {
                    editTextForCocktailHowToMake.setError("300자 이하로 입력해주세요!");
                } else {
                    editTextForCocktailHowToMake.setError(null);
                }
            }
        });

        TextInputLayout textInputLayoutForCocktailDescription = findViewById(R.id.textInputLayout_cocktailDescription_cocktail_upload);
        textInputLayoutForCocktailDescription.setCounterEnabled(true);
        textInputLayoutForCocktailDescription.setCounterMaxLength(300);
        final EditText editTextForCocktailDescription = textInputLayoutForCocktailDescription.getEditText();
        editTextForCocktailDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>300) {
                    editTextForCocktailDescription.setError("300자 이하로 입력해주세요!");
                } else {
                    editTextForCocktailDescription.setError(null);
                }
            }
        });

        Button uploadButtonCocktailUpload = findViewById(R.id.button_upload_cocktail_upload);
        uploadButtonCocktailUpload.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view){
                stringForCocktailName = editTextForCocktailName.getText().toString();
                stringForCocktailHowToMake = editTextForCocktailHowToMake.getText().toString();
                stringForCocktailDescription = editTextForCocktailDescription.getText().toString();

                if (imageCheck==false){
                    Toast.makeText(getApplicationContext(), "업로드 실패! 칵테일 사진을 함께 업로드 해주세요!", Toast.LENGTH_SHORT).show();
                }
                else if(stringForCocktailName.getBytes().length==0 || stringForCocktailHowToMake.getBytes().length==0
                        || stringForCocktailDescription.getBytes().length==0){
                    Toast.makeText(getApplicationContext(), "업로드 실패! 빈칸을 남기지 말아주세요!", Toast.LENGTH_SHORT).show();
                }
                else if (editTextForCocktailName.getError()!=null || editTextForCocktailHowToMake.getError()!=null
                        || editTextForCocktailDescription.getError()!=null){
                    Toast.makeText(getApplicationContext(), "업로드 실패! 문자 길이를 준수해주세요!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //영진이 파트
                    db = FirebaseFirestore.getInstance();
                    mAuth = FirebaseAuth.getInstance();
                    user = mAuth.getCurrentUser();
                    storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    final StorageReference recvRef;

                    final Map<String, Object> uploadRecipe = new HashMap<>();

                    Uri file = Uri.fromFile(new File(photoFile.getPath()));
                    recvRef = storageRef.child("Self/" + file.getLastPathSegment());
                    recvRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            recvRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, uri.toString());
                                    uploadRecipe.put("ref", uri.toString());
                                }
                            });
                        }
                    });
                    uploadRecipe.put("칵테일 만든 유저 id", user.getUid());
                    uploadRecipe.put("칵테일 만든이", user.getDisplayName());
                    uploadRecipe.put("칵테일 이름", stringForCocktailName);
                    uploadRecipe.put("만드는 방법", stringForCocktailHowToMake);
                    uploadRecipe.put("칵테일 설명", stringForCocktailDescription);
                    uploadRecipe.put("good_number", "0");
                    uploadRecipe.put("mark_number", 0);
                    uploadRecipe.put("ref", "gs://sbsimulator-96f70.appspot.com/Self/"+file.getLastPathSegment());

                    Recipe_count = 1;
                    //db Self 컬렉션의 레시피 번호 갯수만큼 Recipe_count 세어서 넣어줌.
                    db.collection("Self")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Recipe_count++;
                                        }
                                        db.collection("Self").document(String.valueOf(Recipe_count+10000))
                                                .set(uploadRecipe)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "업로드 성공!", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        //Log.w(TAG, "DocumentSnapshot successfully written!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "업로드 실패! 다시 확인해주세요!", Toast.LENGTH_SHORT).show();
                                                        //Log.w(TAG, "Error writing document", e);
                                                    }
                                                });
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                }
            }
        });

        Button cancelButtonCocktailUpload = findViewById(R.id.button_cancel_cocktail_upload);
        cancelButtonCocktailUpload.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view){
                finish();
            }
        });

        /*
        ChipGroup chipGroup = findViewById(R.id.chipGroup_cocktail_upload);
        Chip chip = new Chip(this); // Must contain context in parameter
        chip.setText("Apple");
        chip.setCheckable(true);
        chipGroup.addView(chip);
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            ((ImageView) findViewById(R.id.imageView_cocktail_upload)).setImageBitmap(rotate(bitmap, exifDegree));
            imageCheck =true;
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }
}
