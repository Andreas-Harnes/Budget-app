package no.hiof.fredrivo.budgetapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import no.hiof.fredrivo.budgetapp.Adapter.DetailActivityAdapter;
import no.hiof.fredrivo.budgetapp.classes.Expenses;

public class DetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static ArrayList<Expenses> expensesArrayList = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    private GoogleSignInAccount account;
    private DrawerLayout draw;
    private DetailActivityAdapter detailActivityAdapter;
    private ArrayList<Expenses> list = new ArrayList<>();
    private DataSnapshot ds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        try {
            account = GoogleSignIn.getLastSignedInAccount(this);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        expensesArrayList.clear();

        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
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
        
        RecyclerView detailRecyclerView = findViewById(R.id.detailRecyclerView);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        detailActivityAdapter = new DetailActivityAdapter(this, list);
        detailRecyclerView.setAdapter(detailActivityAdapter);

        // Legger til en lytter for å hente data og lytte etter endringer i databasen
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ds = dataSnapshot;
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
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

    // Legger data fra Firebase inn i en liste
    private void showData(DataSnapshot dataSnapshot) {
        expensesArrayList.clear();
        for(DataSnapshot DS : dataSnapshot.child(account.getId()).child("Expenses").getChildren()) {
            Expenses userExpense = new Expenses();
            userExpense.setSum(DS.getValue(Expenses.class).getSum());
            userExpense.setDate(DS.getValue(Expenses.class).getDate());
            userExpense.setLocation(DS.getValue(Expenses.class).getLocation());
            userExpense.setDescription(DS.getValue(Expenses.class).getDescription());
            userExpense.setCategory(DS.getValue(Expenses.class).getCategory());

            expensesArrayList.add(userExpense);
        }

        ArrayList<Expenses> t = expensesArrayList;
        list.clear();
        list.addAll(sortByDate(t));
        //list = sortByDate(t);
        // Det er denne som oppdaterer viewet
        detailActivityAdapter.notifyDataSetChanged();
    }


    public ArrayList<Expenses> sortByDate (ArrayList<Expenses> list) {
        // utgangspunkt: https://www.mkyong.com/java/how-to-convert-string-to-date-java/
        ArrayList<Expenses> temp = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        for (Expenses ex : list) {
            String dateInString = ex.getDate();

            try {
                Date date = formatter.parse(dateInString);

                Expenses newEx = new Expenses();

                newEx.setCategory(ex.getCategory());
                newEx.setSum(ex.getSum());
                newEx.setLocation(ex.getLocation());
                newEx.setDescription(ex.getDescription());
                newEx.setDateTime(date);

                temp.add(newEx);
            }

            catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //kilde: https://stackoverflow.com/questions/5927109/sort-objects-in-arraylist-by-date
        Collections.sort(temp, new Comparator<Expenses>() {
            @Override
            public int compare(Expenses o1, Expenses o2) {
                if (o1.getDateTime() == null || o2.getDateTime() == null)
                    return 0;

                return o2.getDateTime().compareTo(o1.getDateTime());
            }
        });

        return temp;
    }

    // Legger til funksjonalitet til kanppene i drawer menyen
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(this,overview.class);
            startActivity(intent);

        } else if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);

        } else if (id == R.id.detail) {
            draw.closeDrawers();

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

        draw.closeDrawer(GravityCompat.START);
        return true;
    }
}
