package no.hiof.fredrivo.budgetapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import no.hiof.fredrivo.budgetapp.Adapter.CategoryDetailAdapter;
import no.hiof.fredrivo.budgetapp.Adapter.DetailActivityAdapter;
import no.hiof.fredrivo.budgetapp.classes.Expenses;

public class CategoryDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabaseRef;
    private GoogleSignInAccount account;
    private DrawerLayout draw;
    private CategoryDetailAdapter categoryDetailAdapter;
    private ArrayList<Expenses> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = GoogleSignIn.getLastSignedInAccount(this);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        list.clear();

        setContentView(R.layout.activity_category_detail);

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

        //setter opp recyclerview
        RecyclerView categoryDetailRecyclerView = findViewById(R.id.categoryDetailRecyclerView);
        categoryDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //henter intent, henter ut liste fra intent og sender med denne til adapter
        //utgangspunkt: https://stackoverflow.com/questions/13601883/how-to-pass-arraylist-of-objects-from-one-to-another-activity-using-intent-in-an/13616719
        Intent dayDetailIntent = getIntent();
        Bundle b = dayDetailIntent.getBundleExtra("bundle");
        list = (ArrayList<Expenses>) b.getSerializable("list");

        categoryDetailAdapter = new CategoryDetailAdapter(this, list);

        categoryDetailRecyclerView.setAdapter(categoryDetailAdapter);


    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(this, overview.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

        } else if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfilActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

        } else if (id == R.id.detail) {
            Intent intent = new Intent(this, DetailActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            draw.closeDrawers();

        } else if (id == R.id.chart) {
            Intent intent = new Intent(this, ChartActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
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

    public List<Expenses> getExpensesList() {
        return list;
    }

}
