package no.hiof.fredrivo.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import no.hiof.fredrivo.budgetapp.Adapter.DayTabAdapter;
import no.hiof.fredrivo.budgetapp.classes.Expenses;

public class day_tab extends Fragment {


    private static final String TAG = "Tab1frag";
    private DatabaseReference mDatabaseRef;
    private static ArrayList<Expenses> expensesArrayList = new ArrayList<>();
    private ArrayList<Expenses> dayCategoryList = new ArrayList<>();
    private GoogleSignInAccount account;
    private TextView txtDaySum;
    private DayTabAdapter dayTabAdapter;
    private Intent dayDetailIntent;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expensesArrayList.clear();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        try {
            account = GoogleSignIn.getLastSignedInAccount(getContext());
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_day_tab, container, false);

        //setter opp RecyclerView, LayoutManager og adapter
        RecyclerView dayTabRecyclerView = root.findViewById(R.id.dayTabRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dayTabRecyclerView.setLayoutManager(layoutManager);

        dayDetailIntent = new Intent(getContext(), CategoryDetailActivity.class);

        //sender info til adapter med onclicklistener på cardview
        dayTabAdapter = new DayTabAdapter(dayCategoryList, new DayTabAdapter.DayViewClickListener() {
            @Override
            public void onClick(int position) {
                String category = dayCategoryList.get(position).getCategory();
                ArrayList<Expenses> detailDayList = DetailDayList(expensesArrayList, category);
                Bundle b = new Bundle();
                b.putSerializable("list", detailDayList);
                dayDetailIntent.putExtra("bundle", b);

                startActivity(dayDetailIntent);
            }
        });

        dayTabRecyclerView.setAdapter(dayTabAdapter);

        txtDaySum = root.findViewById(R.id.txtDaySum);

        // Legger til en lytter for å hente data og lytte etter endringer i databasen
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { showData(dataSnapshot); }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        return root;
    }

    //legger sammen alle dagens utgifter til en sum nederst
    private int daySum(ArrayList<Expenses> expenses) {
        int total = 0;

        for (Expenses i : expenses) {
            total += i.getSum();
        }

        return total;
    }

    // Legger data fra Firebase inn i en liste
    private void showData(DataSnapshot dataSnapshot) {
        expensesArrayList.clear();
        dayCategoryList.clear();
        for(DataSnapshot ds : dataSnapshot.child(account.getId()).child("Expenses").getChildren()) {
            if(validData(ds.getValue(Expenses.class))){
                Expenses userExpense = new Expenses();
                userExpense.setSum(ds.getValue(Expenses.class).getSum());
                userExpense.setDate(ds.getValue(Expenses.class).getDate());
                userExpense.setLocation(ds.getValue(Expenses.class).getLocation());
                userExpense.setDescription(ds.getValue(Expenses.class).getDescription());
                userExpense.setCategory(ds.getValue(Expenses.class).getCategory());

                expensesArrayList.add(userExpense);
            }
        }

        ArrayList<Expenses> tempList = Expenses.expensesSortedCategory(expensesArrayList);
        dayCategoryList.addAll(tempList);
        changeTotalSpent(dayCategoryList, txtDaySum);

        // Det er denne som oppdaterer viewet
        dayTabAdapter.notifyDataSetChanged();
    }

    public ArrayList<Expenses> DetailDayList(ArrayList<Expenses> dayList, String category) {
        ArrayList<Expenses> temp = new ArrayList<>();

        for (Expenses e : dayList) {
            if (e.getCategory().equals(category)) {
                temp.add(e);
            }
        }
        return temp;
    }

    // Sjekker om datoen på expens objektet er fra dags dato
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

        regex = "((" + day.substring(0,1) + ")(" + day.substring(1) + "))(/)(("
                + month.substring(0,1) + ")(" + month.substring(1) +
                "))(/)((" + year.substring(0,1) + ")(" + year.substring(1,2) + ")(" +
                year.substring(2,3) + ")(" + year.substring(3) + "))";


        if(data.getDate().matches(regex)){
            return true;
        }
        return false;
    }

    //legger til totalsum nederst på siden
    private void changeTotalSpent(ArrayList<Expenses> arrayList, TextView textField){
        int sum = daySum(arrayList);
        if(sum != 0){
            String s = "Today's total: " + Integer.toString(sum) + ",-";
            textField.setText(s);
        }
    }
}
