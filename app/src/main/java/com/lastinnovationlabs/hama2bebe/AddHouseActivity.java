package com.lastinnovationlabs.hama2bebe;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Locale;

public class AddHouseActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private ProgressDialog progressDialog;

    Button upload;

    EditText location, price, service_fee, total_rooms, terms_conditions, more_about_house;

    Spinner spinner_months;

    String[] months = {"--Choose Months--", "1 Month (Mwezi Mmoja)", "3 Months (Miezi Mitatu)", "6 Months (Miezi Sita)", "1 year (Mwaka MMoja)"};

    ImageView pic_one, pic_two, pic_three, pic_four, pic_five;

    private static final int PICK_IMG = 1;

    private static final int PICK_IMG1 = 2;

    private static final int PICK_IMG2 = 3;

    private static final int PICK_IMG3 = 4;

    private static final int PICK_IMG4 = 5;

    Uri Uri1, Uri2, Uri3;

    String pic1 = "";

    String pic2 = "";

    String pic3 = "";

    StorageReference storageRef;

    StorageTask<UploadTask.TaskSnapshot> uploadTask;

    DatabaseReference reference;

    String houseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding House");
        progressDialog.setCancelable(false);

        storageRef = FirebaseStorage.getInstance().getReference("Houses");

        reference = FirebaseDatabase.getInstance().getReference("Houses");

        houseId = reference.push().getKey();

        upload = findViewById(R.id.upload);

        pic_one = findViewById(R.id.pic_one);

        pic_two = findViewById(R.id.pic_two);

        pic_three = findViewById(R.id.pic_three);

        pic_four = findViewById(R.id.pic_four);

        pic_five = findViewById(R.id.pic_five);

        more_about_house = findViewById(R.id.more_about_house);

        location = findViewById(R.id.location);

        price = findViewById(R.id.price);

        service_fee = findViewById(R.id.service_fee);

        total_rooms = findViewById(R.id.total_rooms);

        terms_conditions = findViewById(R.id.terms_conditions);

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

        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                String HousePrice = price.getText().toString();
                String HouseMoreAbout = more_about_house.getText().toString();
                String HouseLocation = location.getText().toString();
                String HouseServiceFee = service_fee.getText().toString();
                String HouseTotalRooms = total_rooms.getText().toString();
                String HouseTerms = terms_conditions.getText().toString();
                String monthsSelected = spinner_months.getSelectedItem().toString();

                if (HouseLocation.isEmpty()) {
                    location.setError("Enter House Location");
                    location.setFocusable(true);
                } else if (HousePrice.isEmpty()) {
                    price.setFocusable(true);
                    price.setError("Enter House Price");
                } else if (HouseServiceFee.isEmpty()) {
                    service_fee.setFocusable(true);
                    service_fee.setError("Enter House Service Fee");
                } else if (HouseMoreAbout.isEmpty()) {
                    more_about_house.setFocusable(true);
                    more_about_house.setError("Enter Information about house");
                } else if (HouseTotalRooms.isEmpty()) {
                    total_rooms.setFocusable(true);
                    total_rooms.setError("Enter total rooms about house");
                } else {
                    progressDialog.show();
                    UploadHouse(HousePrice, HouseMoreAbout, HouseLocation, HouseServiceFee, HouseTotalRooms, HouseTerms, monthsSelected);
                }

            }
        });


        //Spinner
        spinner_months = findViewById(R.id.spinner_months);

        spinner_months.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_months.setAdapter(monthsAdapter);

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void UploadHouse(String housePrice, String houseMoreAbout, String houseLocation,
                             String houseServiceFee, String houseTotalRooms, String houseTerms,
                             String monthsSelected) {


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
                    SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d 'at' HH:mm", Locale.US);
                    String formattedDate = df.format(c.getTime());

                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("houseId", houseId);
                    hashMap.put("houseUploadTime", formattedDate);
                    hashMap.put("houseAvailability", "available");
                    hashMap.put("houseCover", pic1);
                    hashMap.put("houseHouseDetails", houseMoreAbout);
                    hashMap.put("houseSearchLocation", houseLocation.toLowerCase());
                    hashMap.put("houseLocation", houseLocation);
                    hashMap.put("houseTotalRooms", houseTotalRooms);
                    hashMap.put("houseServiceFee", houseServiceFee + " " + "Tsh");
                    hashMap.put("housePrice", housePrice + " " + "Tsh");
                    hashMap.put("housePaymentInterval", monthsSelected);
                    hashMap.put("houseTerms", houseTerms);

                    reference.child(houseId).setValue(hashMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            //second image
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

                                    map1.put("housePictureOne", pic2);

                                    reference.child(houseId).updateChildren(map1)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //third image
                                                    final StorageReference fileReference2 = storageRef.child(System.currentTimeMillis()
                                                            + "." + getFileExtension(Uri3));

                                                    uploadTask = fileReference2.putFile(Uri3);
                                                    uploadTask.continueWithTask(task3 -> {
                                                        if (!task3.isSuccessful()) {
                                                            throw task3.getException();
                                                        }
                                                        return fileReference2.getDownloadUrl();
                                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {

                                                            Uri downloadUri2 = task.getResult();
                                                            pic3 = downloadUri2.toString();

                                                            HashMap<String, Object> map2 = new HashMap<>();

                                                            map2.put("housePictureTwo", pic3);

                                                            reference.child(houseId).updateChildren(map2)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(AddHouseActivity.this, "Umeweza Mzee 😂🤣", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                        }
                                                    });
                                                }
                                            });

                                }
                            });
                        }
                    });
                }
            }
        });


    }


    @SuppressLint("SetTextI18n")
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
