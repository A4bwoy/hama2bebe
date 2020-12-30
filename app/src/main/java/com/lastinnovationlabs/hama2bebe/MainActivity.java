package com.lastinnovationlabs.hama2bebe;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lastinnovationlabs.hama2bebe.Fragments.clickItemHome.HomeFragment;
import com.lastinnovationlabs.hama2bebe.Fragments.clickItemHome.MakaziFragment;
import com.lastinnovationlabs.hama2bebe.Fragments.clickItemHome.UsafiriFragment;
import com.lastinnovationlabs.hama2bebe.Fragments.menuItemOptions.UsafiFragment;
import com.lastinnovationlabs.hama2bebe.MainOptions.MapActivity;
import com.lastinnovationlabs.hama2bebe.Model.User;
import com.lastinnovationlabs.hama2bebe.UserInterface.UpdateProfileActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;

    String phone = "+255763334918";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tafadhali Subiri...");
        progressDialog.setCancelable(true);

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState, 0);

    }


    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("Hama2bebe");
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.username);
        TextView phoneNumber = headerView.findViewById(R.id.phoneNumber);
        CircleImageView circleImageView = headerView.findViewById(R.id.imageView);

        loadUserInfo(username, phoneNumber, circleImageView);


    }

    private void loadUserInfo(TextView username, TextView phoneNumber, CircleImageView circleImageView) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    username.setText(user.getSearch());
                    assert firebaseUser != null;
                    phoneNumber.setText(firebaseUser.getPhoneNumber());

                    Glide
                            .with(MainActivity.this)
                            .load(user.getUserPicture())
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(circleImageView);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex) {
        if (savedInstanceState == null) {
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);

        }
    }

    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            closeApp();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new HomeFragment())
                        .commit();
                break;
            case R.id.nav_usafiri:
                openUsafiri();
                closeDrawer();
                break;
            case R.id.nav_makazi:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new MakaziFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.nav_usafi:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new UsafiFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.nav_support:
                closeDrawer();
                callUs();
                break;
            case R.id.nav_whatsapp:
                openWhatsApp();
                closeDrawer();
                break;
            case R.id.nav_rate:
                rateApp();
                closeDrawer();
                break;
            case R.id.nav_house_add:
                closeDrawer();
                openAddHouse();
                break;
            case R.id.nav_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hama2bebe");
                    String shareMessage = "\nPata usafiri pata makazi kupitia hama2bebe\n\nPakua sasa 👇 \n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID ;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                deSelectCheckedState();
                closeDrawer();
                break;
        }
        return true;
    }

    private void openUsafiri() {
        Intent i = new Intent(MainActivity.this, MapActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private void openAddHouse() {
        Intent i = new Intent(MainActivity.this, AddHouseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private void callUs() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+255676871871"));
        startActivity(intent);
    }

    private void openWhatsApp() {
        String url = "https://api.whatsapp.com/send?phone=" + phone;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void deSelectCheckedState() {
        int noOfItems = navigationView.getMenu().size();
        for (int i = 0; i < noOfItems; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    public void closeApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> MainActivity.this.finish())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();
        checkProfileUpdate();
    }

    private void checkProfileUpdate() {
        String currentUser = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("profileUpdate").exists()) {
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Update Your Profile First", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, UpdateProfileActivity.class));
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

}