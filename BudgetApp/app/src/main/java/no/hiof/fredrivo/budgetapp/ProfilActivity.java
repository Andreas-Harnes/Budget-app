package no.hiof.fredrivo.budgetapp;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import no.hiof.fredrivo.budgetapp.classes.Expenses;
import no.hiof.fredrivo.budgetapp.classes.Profile;

public class ProfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView txtProfilIncome;
    private TextView txtProfilSave;
    private TextView txtProfilMonthlyEx;
    private TextView txtProfilCategories;
    private TextView txtSL1;
    private TextView txtSL2;
    private TextView txtSL3;
    private TextView txtSL4;
    private TextView txtC1;
    private TextView txtC2;
    private TextView txtC3;
    private TextView txtC4;
    private DrawerLayout draw;
    private GoogleSignInAccount account;
    private DatabaseReference mDatabaseRef;
    private String limit1;
    private String limit2;
    private String limit3;
    private String limit4;
    private String cat1;
    private String cat2;
    private String cat3;
    private String cat4;

    private static int income;
    private DataSnapshot ds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Get GUI elementer
        txtProfilIncome = findViewById(R.id.txtProfilIncome);
        txtProfilSave = findViewById(R.id.txtProfilSave);
        txtProfilMonthlyEx = findViewById(R.id.txtProfilMonthlyEx);
        txtProfilCategories = findViewById(R.id.txtProfilCategories);
        txtSL1 = findViewById(R.id.txtSL1);
        txtSL2 = findViewById(R.id.txtSL2);
        txtSL3 = findViewById(R.id.txtSL3);
        txtSL4 = findViewById(R.id.txtSL4);
        txtC1 = findViewById(R.id.txtC1);
        txtC2 = findViewById(R.id.txtC2);
        txtC3 = findViewById(R.id.txtC3);
        txtC4 = findViewById(R.id.txtC4);

        // Google login
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        account = GoogleSignIn.getLastSignedInAccount(this);

        // Sette profilbildet
        ImageView imgProfilePicture = findViewById(R.id.imageView);
        Picasso.get().load(account.getPhotoUrl()).into(imgProfilePicture);

        // Setter navnet til navnet på profilen
        TextView txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileName.setText(account.getDisplayName());

        //Toolbar og navigationDrawer start:
        Toolbar toolbar = findViewById(R.id.profiltoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // implementering av navigation drawer!
        draw = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,draw,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        draw.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // slutt for navi drawer


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ds = dataSnapshot;
                if(dataSnapshot.child(account.getId()).hasChild("Profile")){
                    showData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        /*
            Setter profilbildet og navn i drawer menyen

            Piccaso API ble funnet på denne url'en
            http://square.github.io/picasso/

            koden brukt finnes under Introduction delen på siden
        */
        TextView txtDrawerProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_textView);
        txtDrawerProfileName.setText(account.getDisplayName());
        ImageView imgDrawerPicture = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        Picasso.get().load(account.getPhotoUrl()).into(imgDrawerPicture);

    }


    // Legger dataen fra Firebase inn i tekst feltene
    private void showData(DataSnapshot dataSnapshot) {

        txtProfilIncome.setText(dataSnapshot.child(account.getId()).child("Profile").child("incomePerMonth").getValue().toString());
        txtProfilSave.setText(dataSnapshot.child(account.getId()).child("Profile").child("savePerMonth").getValue().toString());
        txtProfilMonthlyEx.setText(dataSnapshot.child(account.getId()).child("Profile").child("expensesPerMonth").getValue().toString());
        txtProfilCategories.setText(dataSnapshot.child(account.getId()).child("Profile").child("categoryToSave").getValue().toString());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent intent = new Intent(this, ProfilSettingsActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(resultCode, requestCode, data);

        //utgangspunkt: https://developer.android.com/training/basics/intents/result
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Intent i = data;

                //sjekker om extras er sendt med intenten, setter innhold i tekstbokser lik info som er sendt
                if (i.hasExtra("limit1")) {
                    limit1 = i.getStringExtra("limit1");
                    cat1 = i.getStringExtra("cat1");

                    txtC1.setVisibility(View.VISIBLE);
                    txtSL1.setVisibility(View.VISIBLE);

                    String s = cat1 + " spending limit";

                    txtC1.setText(s);
                    txtSL1.setText(limit1);
                }

                if (i.hasExtra("limit2")) {
                    limit2 = i.getStringExtra("limit2");
                    cat2 = i.getStringExtra("cat2");

                    txtC2.setVisibility(View.VISIBLE);
                    txtSL2.setVisibility(View.VISIBLE);

                    String s = cat2 + " spending limit";

                    txtC2.setText(s);
                    txtSL2.setText(limit2);
                }

                if (i.hasExtra("limit3")) {
                    limit3 = i.getStringExtra("limit3");
                    cat3 = i.getStringExtra("cat3");

                    txtC3.setVisibility(View.VISIBLE);
                    txtSL3.setVisibility(View.VISIBLE);

                    String s = cat3 + " spending limit";

                    txtC3.setText(s);
                    txtSL3.setText(limit3);
                }

                if (i.hasExtra("limit4")) {
                    limit4 = i.getStringExtra("limit4");
                    cat4 = i.getStringExtra("cat4");

                    txtC4.setVisibility(View.VISIBLE);
                    txtSL4.setVisibility(View.VISIBLE);

                    String s = cat4 + " spending limit";

                    txtC4.setText(s);
                    txtSL4.setText(limit4);
                }

            }

            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Result Cancelled", Toast.LENGTH_SHORT);
            }
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(this, overview.class);
            startActivity(intent);

        } else if (id == R.id.profile) {
            draw.closeDrawers();

            draw.closeDrawers();

        } else if (id == R.id.detail) {
            Intent intent = new Intent(this,DetailActivity.class);
            startActivity(intent);

        } else if (id == R.id.chart) {
            if (ds.child(account.getId()).hasChild("Profile")){

                Intent intent = new Intent(this, ChartActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Please fill out profile settings", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ProfilActivity.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.info){
            Intent tips_tricks = new Intent( Intent.ACTION_VIEW, Uri.parse("https://www.lifeinnorway.net/10-ways-to-save-money-on-groceries-in-norway/"));
            startActivity(tips_tricks);
        }

        else if(id == R.id.logOut){
            // kode for å logge ut
        }

        draw.closeDrawer(GravityCompat.START);
        return true;
    }

    public static int getIncome(){
        return income;
    }
}
