package no.hiof.fredrivo.budgetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import no.hiof.fredrivo.budgetapp.classes.Categories;

public class NewCategoryActivity extends AppCompatActivity {

    private Intent intentAddCategory;
    private String newCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        //intent for å åpne InputActivity
        intentAddCategory = new Intent (this,InputActivity.class);

        //Gjør det mulig å hide keyboardet når man trykker på skjermen
        findViewById(R.id.new_category_background).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //metoden som gjømmer keyboardet
                hideSoftKeyboard();
                return true;
            }
        });

        //onClickListener på knappen for å legge til kategori
        Button btnAddCat = findViewById(R.id.btnAddCat);
        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //finner TextView der kategori skrives inn og henter teksten derifra
                EditText txtNewCat = findViewById(R.id.txtNewCat);
                newCategory = txtNewCat.getText().toString();

                //hvis tekstfeltet er tomt, si ifra med toast
                if (newCategory.isEmpty()){
                    Toast.makeText(NewCategoryActivity.this, "Please add a new category.", Toast.LENGTH_SHORT).show();
                }

                //hvis kategorilisten allerede har denne kategorien, si ifra med toast
                else if (Categories.getUserCategories().contains(newCategory)) {
                    Toast.makeText(NewCategoryActivity.this, "Category already exists. Please add a new category.", Toast.LENGTH_SHORT).show();
                }

                //legg til kategorien i lista og start intent for å åpne InputActivity
                else{
                    new Categories(newCategory);
                    startActivity(intentAddCategory);

                    finish();
                }

            }
        });
    }

    //metode som gjemmer tastatur, inspirert fra https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
}
