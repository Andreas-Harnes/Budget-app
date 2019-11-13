package no.hiof.fredrivo.budgetapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import no.hiof.fredrivo.budgetapp.classes.N_receiver;

public class overview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Intent intentInputActivity;
    private NotificationCompat.Builder notification;
    private GoogleSignInAccount account;


    //Ha med på flere activities
    private DrawerLayout draw;


    private DatabaseReference mDatabaseRef;

    private DataSnapshot ds;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Google login
            account = GoogleSignIn.getLastSignedInAccount(this);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        // notification start
        Calendar calender = Calendar.getInstance(); // https://developer.android.com/reference/android/app/AlarmManager
        calender.set(Calendar.HOUR_OF_DAY,14);
        calender.set(Calendar.MINUTE,30);
        calender.set(Calendar.SECOND,00);
        // calender.add(Calendar.SECOND,5);
        AlarmManager alarmM = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent NotifyIntent = new Intent(this,N_receiver.class); // intent til broadcast/notification receiver
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(this,123,NotifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        // lager en alarm som skal gi en trigger til notification vår
        if (System.currentTimeMillis() > calender.getTimeInMillis()){
            calender.add(Calendar.DATE,1); // drøyer med en dag dersom den ikke stemmer med tiden idag, for å unngå repetisjoner
        }
        alarmM.cancel(broadcastIntent);
        alarmM.setRepeating(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),AlarmManager.INTERVAL_DAY,broadcastIntent); // det ønskes at den skal være daglig
        // notification end

        setContentView(R.layout.activity_overview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // implementering av navigation drawer
        draw = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,draw,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        draw.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        /*
            Setter profilbildet og navn i drawer menyen

            Piccaso API ble funnet på denne url'en
            http://square.github.io/picasso/

            koden brukt finnes under Introduction delen på siden
        */
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_textView);
        txtProfileName.setText(account.getDisplayName());
        ImageView imgProfilePicture = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        Picasso.get().load(account.getPhotoUrl()).into(imgProfilePicture);



        notification = new NotificationCompat.Builder(this);

        // Creates the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Sets up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // Lager en intent for FAB'en
       intentInputActivity = new Intent(getApplicationContext(), InputActivity.class);

        // FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intentInputActivity);
            }
        });



        //setting up tabs with adapter from new class: sectionspageadapter
        mSectionsPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        tabLayout.setupWithViewPager(mViewPager);

        // Legger til en lytter på database referansen som henter data når appen starter
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ds = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //viewpager and adapter
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new day_tab(),"Day");
        adapter.addFragment(new week_tab(),"Week");
        adapter.addFragment(new month_tab(),"Month");
        viewPager.setAdapter(adapter);
    }

    // inflating button for camera
    @Override
    public boolean onCreateOptionsMenu(Menu m){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.camera_button,m);

        return true;
    }
    //action when button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.camera_button){
//            Toast.makeText(this, "Opening Camera", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,Camera_activity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    // Legger til funksjonalitet til kanppene i drawer menyen
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        if (id == R.id.overview) {
            draw.closeDrawers();

        } else if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);

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


}


