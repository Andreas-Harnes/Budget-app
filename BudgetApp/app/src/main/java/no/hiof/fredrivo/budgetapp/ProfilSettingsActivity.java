package no.hiof.fredrivo.budgetapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.ArrayList;

import no.hiof.fredrivo.budgetapp.classes.Categories;
import no.hiof.fredrivo.budgetapp.classes.Profile;


public class ProfilSettingsActivity extends AppCompatActivity {

    private Intent intentSaveChanges;
    private Spinner drpSettingsCat;

    private EditText txtMonthlyEx;
    private EditText txtIncome;
    private EditText txtSavePrMonth;
    private TextView txtCategoriesForSaving;
    private TextView txtCat1;
    private TextView txtCat2;
    private TextView txtCat3;
    private TextView txtCat4;
    private EditText txtLimit1;
    private EditText txtLimit2;
    private EditText txtLimit3;
    private EditText txtLimit4;
    private ArrayList<String> saveCategoriesList = new ArrayList<>();

    private DatabaseReference mDatabaseRef;
    private GoogleSignInAccount account;
    private TextView txtProfilSettingsIncome;
    private TextView txtProfilSettingsSave;
    private TextView txtProfilSettingsMonthlyEx;
    private TextView txtSpendingLimitText;
    private TextView getTxtCategoriesForSaving;
    private String id;
    private int monthly;
    private int income;
    private int save;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_settings);

        // Får tak i GUI elementer
        txtProfilSettingsIncome = findViewById(R.id.txtProfilSettingsIncome);
        txtProfilSettingsSave = findViewById(R.id.txtProfilSettingsSave);
        txtProfilSettingsMonthlyEx = findViewById(R.id.txtProfilSettingsMonthlyEx);
        txtCategoriesForSaving = findViewById(R.id.txtProfilSettingsCategories);
        drpSettingsCat = findViewById(R.id.drpSettingsCat);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        try {
            account = GoogleSignIn.getLastSignedInAccount(this);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //finner views fra xml
        txtCat1 = findViewById(R.id.txtCat1);
        txtCat2 = findViewById(R.id.txtCat2);
        txtCat3 = findViewById(R.id.txtCat3);
        txtCat4 = findViewById(R.id.txtCat4);
        txtLimit1 = findViewById(R.id.txtLimit1);
        txtLimit2 = findViewById(R.id.txtLimit2);
        txtLimit3 = findViewById(R.id.txtLimit3);
        txtLimit4 = findViewById(R.id.txtLimit4);
        txtSpendingLimitText = findViewById(R.id.txtSpendingLimitText);

        //adapter for å putte kategorier fra arraylist inni dropdownmeny
        ArrayAdapter<String> adapterSettingsCat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Categories.getUserCategories());
        adapterSettingsCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        drpSettingsCat.setAdapter(adapterSettingsCat);

        //setter toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //onclicklistener på bakgrunn for å gjemme tastatur
        findViewById(R.id.profilSettingsBackground).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //kaller metode som gjemmer tastatur
                hideSoftKeyboard();
                return true;
            }
        });

        //finner knapp for å legge til kategori
        Button btnAddCategory = findViewById(R.id.btnAddCatSettings);

        //setter onClickListener på kategoriknapp
        btnAddCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //nåværende objekt i dropdownmeny
                String fillCategories = drpSettingsCat.getSelectedItem().toString();

                //legger til nåværende objekt i arraylist, hvis det ikke er det allerede
                if (saveCategoriesList.contains(fillCategories)) {
                    Toast.makeText(ProfilSettingsActivity.this, "Category already added", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveCategoriesList.add(fillCategories);
                }


                //lager stringbuilder for å holde på valgte kategorier og legge på nye
                StringBuilder builder = new StringBuilder();

                //lager index for å sjekke hvilket element vi er på
                int i = 0;

                //for each for å legge til elementer fra arraylist til textview
                for (String s : saveCategoriesList) {

                    //sjekker om dette er det første elementet, legger til kategori uten komma
                    //setter tekstbokser for spending limit som synlig og setter navn på kategori som tittel
                    if (i == 0) {
                        builder.append(s);
                        txtCat1.setVisibility(View.VISIBLE);
                        txtLimit1.setVisibility(View.VISIBLE);
                        txtSpendingLimitText.setVisibility(View.VISIBLE);
                        txtCat1.setText(s);
                    }

                    //setter tekstbokser for spending limit som synlig og setter navn på kategori som tittel
                    else if (i == 1) {
                        String s2 = ", " + s;
                        builder.append(s2);
                        txtCat2.setVisibility(View.VISIBLE);
                        txtLimit2.setVisibility(View.VISIBLE);
                        txtSpendingLimitText.setVisibility(View.VISIBLE);
                        txtCat2.setText(s);
                    }

                    //setter tekstbokser for spending limit som synlig og setter navn på kategori som tittel
                    else if (i == 2) {
                        String s2 = ", " + s;
                        builder.append(s2);
                        txtCat3.setVisibility(View.VISIBLE);
                        txtLimit3.setVisibility(View.VISIBLE);
                        txtSpendingLimitText.setVisibility(View.VISIBLE);
                        txtCat3.setText(s);
                    }

                    //setter tekstbokser for spending limit som synlig og setter navn på kategori som tittel
                    else if (i == 3) {
                        String s2 = ", " + s;
                        builder.append(s2);
                        txtCat4.setVisibility(View.VISIBLE);
                        txtLimit4.setVisibility(View.VISIBLE);
                        txtSpendingLimitText.setVisibility(View.VISIBLE);
                        txtCat4.setText(s);
                    }

                    //sjekker om der er mer enn 4 elementer i lista, sier ifra med toast
                    else if (i == 4) {
                        Toast.makeText(getApplicationContext(), "Maximum 4 categories", Toast.LENGTH_SHORT).show();
                    }

                    i++;
                }

                //setter innholder til textview til å være stringen i stringbuilder
                txtCategoriesForSaving.setText(builder);
            }
        });

        final Context context = this.getApplicationContext();

        //finner knapp for å lagre endringer
        Button saveBtn = findViewById(R.id.saveBtn);

        //intent for å gå til ProfilActivity
        intentSaveChanges = new Intent(context, ProfilActivity.class);

        //setter onClickListener på knapp for å lagre endringer
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //sjekker om alle felt er fylt ut, henter ut tekst fra views
                if (TextUtils.isEmpty(txtProfilSettingsMonthlyEx.getText().toString())) {
                    Toast.makeText(ProfilSettingsActivity.this, "Please add monthly expenses", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(txtProfilSettingsIncome.getText().toString())) {
                    Toast.makeText(ProfilSettingsActivity.this, "Please add income", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(txtProfilSettingsSave.getText().toString())) {
                    Toast.makeText(ProfilSettingsActivity.this, "Please add how much to save", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(txtCategoriesForSaving.getText().toString())) {
                    Toast.makeText(ProfilSettingsActivity.this, "Please add categories for saving", Toast.LENGTH_SHORT).show();
                }

                else {

                }

                //lager bundle for å overføre info til ProfilActivity
                Bundle bundle = new Bundle();

                //henter tekst fra spending limit views
                String limit1 = txtLimit1.getText().toString();
                String limit2 = txtLimit2.getText().toString();
                String limit3 = txtLimit3.getText().toString();
                String limit4 = txtLimit4.getText().toString();
                String cat1 = txtCat1.getText().toString();
                String cat2 = txtCat2.getText().toString();
                String cat3 = txtCat3.getText().toString();
                String cat4 = txtCat4.getText().toString();


                //if-tester som sjekker om kategorier er valgt, sender spending limit sum med
                //i bundle hvis tekstfeltet er satt
                if (!cat1.equals("TextView")) {
                    bundle.putString("limit1", limit1);
                    bundle.putString("cat1", cat1);
                }

                if (!cat2.equals("TextView")) {
                    bundle.putString("limit2", limit2);
                    bundle.putString("cat2", cat2);
                }

                if (!cat3.equals("TextView")) {
                    bundle.putString("limit3", limit3);
                    bundle.putString("cat3", cat3);
                }

                if (!cat4.equals("TextView")) {
                    bundle.putString("limit4", limit4);
                    bundle.putString("cat4", cat4);
                }

                //putter bundle inn i extra
                intentSaveChanges.putExtras(bundle);

                if(TextUtils.isEmpty(txtProfilSettingsMonthlyEx.getText().toString()) ||
                        TextUtils.isEmpty(txtProfilSettingsIncome.getText().toString()) ||
                        TextUtils.isEmpty(txtProfilSettingsSave.getText().toString()) ||
                        TextUtils.isEmpty(txtCategoriesForSaving.getText().toString())){
                    Toast.makeText(context, "You need to fill in everything to continue", Toast.LENGTH_SHORT).show();
                }
                else {


                    id = mDatabaseRef.push().getKey();
                    monthly = Integer.parseInt(txtProfilSettingsMonthlyEx.getText().toString());
                    income = Integer.parseInt(txtProfilSettingsIncome.getText().toString());
                    save = Integer.parseInt(txtProfilSettingsSave.getText().toString());
                    category = txtCategoriesForSaving.getText().toString();

                    if(income == 0) {
                        Toast.makeText(context, "Income per month needs to be higher then 0", Toast.LENGTH_SHORT).show();
                    } else if(income != 0 && income > 0){
                        Profile userProfile = new Profile(id, income, save, monthly, category);
                        mDatabaseRef.child(account.getId()).child("Profile") .setValue(userProfile);
                        //starter intent med result ok
                        setResult(Activity.RESULT_OK, intentSaveChanges);
                        finish();
                    }



                }



            }
        });


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(account.getId()).hasChild("Profile")){
                    showData(dataSnapshot);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {

        txtProfilSettingsIncome.setText(dataSnapshot.child(account.getId()).child("Profile").child("incomePerMonth").getValue().toString());
        txtProfilSettingsSave.setText(dataSnapshot.child(account.getId()).child("Profile").child("savePerMonth").getValue().toString());
        txtProfilSettingsMonthlyEx.setText(dataSnapshot.child(account.getId()).child("Profile").child("expensesPerMonth").getValue().toString());

    }


    //metode for å gjemme tastatur
   public void hideSoftKeyboard() {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   public ArrayList<String> getSaveCategoriesList() { return saveCategoriesList; }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(this, overview.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
   }
}
