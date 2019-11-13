package no.hiof.fredrivo.budgetapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import no.hiof.fredrivo.budgetapp.classes.Expenses;

public class ChartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout draw;
    private PieChart pieChart;
    private List<PieEntry> pieChartList;
    private GoogleSignInAccount account;
    private DataSnapshot dsForDrawer;
    private DatabaseReference mDatabaseRef;
    private ArrayList<Expenses> expensesArrayList = new ArrayList<>();


    // Henter nødvendig informasjon fra firebase og google
    // Og setter opp deler av GUI'et
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        try {
            account = GoogleSignIn.getLastSignedInAccount(this);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        draw = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,draw,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        draw.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pieChart = findViewById(R.id.pieChartLayout);

        pieChartList = new ArrayList<>();

        TextView txtDrawerProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_textView);
        txtDrawerProfileName.setText(account.getDisplayName());

        ImageView imgDrawerPicture = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        Picasso.get().load(account.getPhotoUrl()).into(imgDrawerPicture);

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dsForDrawer = dataSnapshot;
                showData(dataSnapshot,expensesArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    // Legger data fra Firebase inn i en liste
    private void showData(DataSnapshot dataSnapshot, ArrayList<Expenses> eArrayList) {
        eArrayList.clear();
        for(DataSnapshot ds : dataSnapshot.child(account.getId()).child("Expenses").getChildren()) {
            if(validData(ds.getValue(Expenses.class))){
                Expenses userExpense = new Expenses();
                userExpense.setSum(ds.getValue(Expenses.class).getSum());
                userExpense.setDate(ds.getValue(Expenses.class).getDate());
                userExpense.setLocation(ds.getValue(Expenses.class).getLocation());
                userExpense.setDescription(ds.getValue(Expenses.class).getDescription());
                userExpense.setCategory(ds.getValue(Expenses.class).getCategory());

                eArrayList.add(userExpense);
            }
        }
        makeChart(eArrayList);
    }

    // Sjekker om datoen på expens objektet er fra denne måneden
    private boolean validData(Expenses data){
        String regex;
        int fBoolean = 0;

        //gets todays date from calendar object
        Calendar calendar = Calendar.getInstance();
        int intYear = calendar.get(Calendar.YEAR);
        int intMonth = calendar.get(Calendar.MONTH) + 1;
        int intDay = calendar.get(Calendar.DAY_OF_MONTH);

        String day = "";
        String month = "";
        String year = "";

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

        year = String.valueOf(intYear);

        // Regex som kun matcher denne måneden
        regex = "((.)(.))(/)(("
                + month.substring(0,1) + ")(" + month.substring(1) +
                "))(/)((" + year.substring(0,1) + ")(" + year.substring(1,2) + ")(" +
                year.substring(2,3) + ")(" + year.substring(3) + "))";

        if(data.getDate().matches(regex)){
            return true;
        }
        return false;
    }

    public void makeChart(ArrayList<Expenses> arrayListExpenses){
        //TestData
        List<Expenses> expensesList = Expenses.expensesSortedCategory(arrayListExpenses);
        int moneyLeft = Integer.parseInt(dsForDrawer.child(account.getId()).child("Profile").child("incomePerMonth").getValue().toString());

        //Legger til expense objekter som entries som pieChartData, Fra https://github.com/PhilJay/MPAndroidChart
        for (Expenses values : expensesList) {
            if(values.getSum() != 0){
                pieChartList.add(new PieEntry(values.getSum(), values.getCategory()));
                moneyLeft = moneyLeft - values.getSum();
            }
        }

        //Bruker samme kode/litt inspirert fra https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Data
        //Hvis income er mindre en 0 blir den satt til 0 fordi det er ikke hensiktsmessig å vise en negativ sum.
        if (moneyLeft < 0){
            moneyLeft = 0;
            PieEntry moneyLeftEntry = new PieEntry(moneyLeft, "Income left");
            pieChartList.add(moneyLeftEntry);
        }
        else {
            PieEntry moneyLeftEntry = new PieEntry(moneyLeft, "Income left");
            pieChartList.add(moneyLeftEntry);
        }

        PieDataSet dataSet = new PieDataSet(pieChartList, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(16f);

        Legend legend = pieChart.getLegend();
        legend.setTextSize(16f);
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }

    // Legger til funksjonalitet til kanppene i drawer menyen
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(this, overview.class);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);

        } else if (id == R.id.detail) {
            Intent intent = new Intent(this,DetailActivity.class);
            startActivity(intent);

        } else if (id == R.id.chart) {
            draw.closeDrawers();
        }
        else if(id == R.id.info){
            Intent tips_tricks = new Intent( Intent.ACTION_VIEW, Uri.parse("https://www.lifeinnorway.net/10-ways-to-save-money-on-groceries-in-norway/"));
            startActivity(tips_tricks);
        }
        else if(id == R.id.logOut){

        }

        draw.closeDrawer(GravityCompat.START);
        return true;
    }
}