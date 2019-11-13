package no.hiof.fredrivo.budgetapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import no.hiof.fredrivo.budgetapp.classes.Categories;
import no.hiof.fredrivo.budgetapp.classes.Expenses;

public class InputActivity extends AppCompatActivity {

    private Intent intentOverview;
    private Intent intentNewCategory;
    private EditText numPrice;
    private EditText txtLocation;
    private EditText txtDescription;
    private TextView txtDatePicker;
    private Button btnChangeDate;
    private Spinner drpCategory;
    private DatePickerDialog.OnDateSetListener dateDialog;
    private int price;
    private String date;
    private String location;
    private String description;
    private String category;
    private GoogleSignInAccount account;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        try {
            account = GoogleSignIn.getLastSignedInAccount(this);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //Gjør det mulig å hide keyboardet når man trykker på skjermen
        findViewById(R.id.activity_input_background).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //metoden som gjømmer keyboardet
                hideSoftKeyboard();
                return true;
            }
        });

        numPrice = findViewById(R.id.numPrice);
        txtLocation = findViewById(R.id.txtLocation);
        txtDescription = findViewById(R.id.txtDescription);
        drpCategory = findViewById(R.id.drpCategory);
        txtDatePicker = findViewById(R.id.txtDatePicker);
        btnChangeDate = findViewById(R.id.btnChangeDate);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(account.getId());

        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Categories.getUserCategories());

        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drpCategory.setAdapter(adapterCategories);

        //utgangspunkt for date picker: https://www.youtube.com/watch?v=hwe1abDO2Ag
        //lager Calendar objekt og henter dagens dato
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int intMonth = calendar.get(Calendar.MONTH);
        int intDay = calendar.get(Calendar.DAY_OF_MONTH);
        String day;
        String month;

        intMonth++;


        if(intDay < 10){
            day = "0" + String.valueOf(intDay);
        } else {
            day = String.valueOf(intDay);
        }

        if(intMonth < 10){
            month = "0" + String.valueOf(intMonth);
        } else {
            month = String.valueOf(intMonth);
        }


        //puts todays date into string today and sets it in txtdatepicker
        date = day + "/" + month + "/" + year;
        txtDatePicker.setText(date);

        //setter en onClickListener til knappen changeDate
        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int intMonth = calendar.get(Calendar.MONTH);
                int intDay = calendar.get(Calendar.DAY_OF_MONTH);

                //creates datepickerdialog
                DatePickerDialog dialog = new DatePickerDialog(InputActivity.this, R.style.datepicker, dateDialog, year, intMonth, intDay);
                dialog.show();
            }
        });

        //når dato er valgt, sett den i txtDatePicker
        dateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String sDay;
                String sMonth;

                month++;

                if(dayOfMonth < 10){
                    sDay = "0" + String.valueOf(dayOfMonth);
                } else {
                    sDay = String.valueOf(dayOfMonth);
                }

                if(month < 10){
                    sMonth = "0" + String.valueOf(month);
                } else {
                    sMonth = String.valueOf(month);
                }

                date = sDay + "/" + sMonth + "/" + year;
                txtDatePicker.setText(date);
            }
        };

        intentOverview = new Intent(this, overview.class);
        Button btnAdd = findViewById(R.id.btnRegister);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Expenses userExpense = new Expenses();

                if (TextUtils.isEmpty(numPrice.getText().toString())){
                    Toast.makeText(InputActivity.this, "Please fill inn a price.", Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(txtLocation.getText().toString())){
                    Toast.makeText(InputActivity.this, "Please fill inn a location.", Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(txtDescription.getText().toString())){
                    Toast.makeText(InputActivity.this, "Please fill inn a description.", Toast.LENGTH_SHORT).show();

                }
                else {

                    String id = mDatabaseRef.push().getKey();
                    price = Integer.parseInt(numPrice.getText().toString());
                    location = txtLocation.getText().toString();
                    description = txtDescription.getText().toString();
                    category = drpCategory.getSelectedItem().toString();

                    userExpense.setId(id);
                    userExpense.setCategory(category);
                    userExpense.setDate(date);
                    userExpense.setDescription(description);
                    userExpense.setLocation(location);
                    userExpense.setSum(price);

                    // Sender data til Firebase
                    mDatabaseRef.child("Expenses").child(id).setValue(userExpense);

                    startActivity(intentOverview);
                    finish();
                }
            }
        });

        intentNewCategory = new Intent (this, NewCategoryActivity.class);

        Button btnNewCategory = findViewById(R.id.btnAddCategory);
        btnNewCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(intentNewCategory);

            }
        });
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
}
