package com.lastinnovationlabs.hama2bebe.UserInterface;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.lastinnovationlabs.hama2bebe.MainActivity;
import com.lastinnovationlabs.hama2bebe.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText firstName, lastName;
    CheckBox AcceptTerms;
    CircleImageView image_profile;
    FloatingActionButton fab_camera;
    Button continueBtn;
    ProgressDialog progressDialog;


    private Uri mImageUri;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String miUrlOk = "";
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    StorageReference storageRef;
    static int PReqCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("Users");

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        AcceptTerms = findViewById(R.id.AcceptTerms);
        image_profile = findViewById(R.id.image_profile);
        fab_camera = findViewById(R.id.fab_camera);
        continueBtn = findViewById(R.id.continueBtn);

        fab_camera.setOnClickListener(view -> {

            if (Build.VERSION.SDK_INT >= 22) {

                checkAndRequestForPermission();


            } else {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .start(UpdateProfileActivity.this);
            }


        });

        continueBtn.setOnClickListener(v -> {
            String firstNameUser = firstName.getText().toString();
            String lastNameUser = lastName.getText().toString();

            if (firstNameUser.isEmpty()) {
                firstName.setFocusable(true);
                firstName.setError("Provide your First Name");
                Toast.makeText(UpdateProfileActivity.this, "Error", Toast.LENGTH_LONG).show();

            } else if (lastNameUser.isEmpty()) {
                lastName.setFocusable(true);
                lastName.setError("Provide your Last Name");
                Toast.makeText(UpdateProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
            } else if (!AcceptTerms.isChecked()) {
                Toast.makeText(this, "Please read and accept our terms and conditions", Toast.LENGTH_LONG).show();
            } else if (mImageUri == null) {
                Toast.makeText(this, "Select your picture", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();
                UpdateUserProfile(firstNameUser, lastNameUser, mImageUri);
            }

        });

    }


    private void UpdateUserProfile(String firstNameUser, String lastNameUser, Uri mImageUri) {

        if (mImageUri != null) {
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    miUrlOk = downloadUri.toString();

                    final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy 'at' HH:mm:ss", Locale.US);
                    String formattedDate = df.format(c.getTime());

                    String searchUserName = firstNameUser.toLowerCase() + " " + lastNameUser.toLowerCase();
                    String userID = firebaseUser.getUid();
                    String phone = firebaseUser.getPhoneNumber();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", userID);
                    map.put("firstName", firstNameUser);
                    map.put("lastName", lastNameUser);
                    map.put("search", searchUserName);
                    map.put("userPicture", miUrlOk);
                    map.put("profileUpdate", "true");
                    map.put("phoneNumber", phone);
                    map.put("ApplicationTime", formattedDate);

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(searchUserName)
                            .setPhotoUri(mImageUri)
                            .build();

                    firebaseUser.updateProfile(profileChangeRequest);

                    reference.updateChildren(map).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(UpdateProfileActivity.this, "Karibu " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(UpdateProfileActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show());
                    progressDialog.dismiss();
                    startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
                    finish();

                } else {
                    Toast.makeText(UpdateProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(UpdateProfileActivity.this, "Please accept the required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(UpdateProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            CropImage.activity()
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(UpdateProfileActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK
        ) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            image_profile.setImageURI(mImageUri);

        }


    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}