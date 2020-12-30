package com.lastinnovationlabs.hama2bebe.Test;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.lastinnovationlabs.hama2bebe.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {

    Button add_images;
    ImageView pic_one, pic_two, pic_three, pic_four, pic_five;

    private static final int PICK_IMG = 1;
    private static final int PICK_IMG1 = 2;
    private static final int PICK_IMG2 = 3;
    private static final int PICK_IMG3 = 4;
    private static final int PICK_IMG4 = 5;

    ProgressDialog progressDialog;

    Uri Uri1, Uri2, Uri3;

    String pic1 = "";
    String pic2 = "";


    StorageReference storageRef;

    StorageTask<UploadTask.TaskSnapshot> uploadTask;

    DatabaseReference reference;

    String postid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding House");

        storageRef = FirebaseStorage.getInstance().getReference("Houses");

        reference = FirebaseDatabase.getInstance().getReference("Houses");

        postid = reference.push().getKey();

        add_images = findViewById(R.id.add_images);

        pic_one = findViewById(R.id.pic_one);
        pic_two = findViewById(R.id.pic_two);
        pic_three = findViewById(R.id.pic_three);
        pic_four = findViewById(R.id.pic_four);
        pic_five = findViewById(R.id.pic_five);

        add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                uploadPicture();
            }
        });

        pic_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        pic_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGaller1();
            }
        });

        pic_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery2();
            }
        });
    }

    private void openGallery2() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, PICK_IMG2);
    }

    private void openGaller1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, PICK_IMG1);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, PICK_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri1 = data.getData();

            pic_one.setImageURI(Uri1);
        }

        if (requestCode == PICK_IMG1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri2 = data.getData();

            pic_two.setImageURI(Uri2);
        }

        if (requestCode == PICK_IMG2 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri3 = data.getData();

            pic_three.setImageURI(Uri3);
        }


    }

    private void uploadPicture() {
        final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(Uri1));

        uploadTask = fileReference.putFile(Uri1);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return fileReference.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    pic1 = downloadUri.toString();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy 'at' HH:mm:ss", Locale.US);
                    String formattedDate = df.format(c.getTime());

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", postid);
                    map.put("photo1", pic1);
                    map.put("ApplicationTime", formattedDate);

                    reference.child(postid).setValue(map).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            //add another image
                            final StorageReference fileReference1 = storageRef.child(System.currentTimeMillis()
                                    + "." + getFileExtension(Uri2));

                            uploadTask = fileReference1.putFile(Uri2);
                            uploadTask.continueWithTask(task2 -> {
                                if (!task2.isSuccessful()) {
                                    throw task2.getException();
                                }
                                return fileReference1.getDownloadUrl();
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri downloadUri1 = task.getResult();
                                    pic2 = downloadUri1.toString();

                                    HashMap<String, Object> map1 = new HashMap<>();

                                    map1.put("photo2", pic2);

                                    reference.child(postid).updateChildren(map1)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            Toast.makeText(TestActivity.this, "Deal Done", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                        }
                    });

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(TestActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(TestActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void uploadPictures(String picture1, String picture2, String picture3) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy 'at' HH:mm:ss", Locale.US);
        String formattedDate = df.format(c.getTime());

        DatabaseReference reference;

        reference = FirebaseDatabase.getInstance().getReference("Houses");
        String postid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("houseId", postid);
        hashMap.put("houseUploadTime", formattedDate);
        hashMap.put("image1", picture1);
        hashMap.put("image2", picture2);
        hashMap.put("image3", picture3);

        assert postid != null;
        reference.child(postid).updateChildren(hashMap).addOnCompleteListener(task1 -> {
            progressDialog.dismiss();
            Toast.makeText(this, "House Uploaded Successfully!", Toast.LENGTH_LONG).show();

        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}