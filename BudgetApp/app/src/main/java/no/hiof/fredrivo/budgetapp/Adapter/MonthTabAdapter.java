package no.hiof.fredrivo.budgetapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import no.hiof.fredrivo.budgetapp.R;
import no.hiof.fredrivo.budgetapp.classes.Expenses;

//brukte kode fra denne linken som utgangspunkt:
//https://medium.com/@Pang_Yao/android-fragment-use-recyclerview-cardview-4bc10beac446
//i tillegg til Lars Emil sitt RecyclerViewExercise eksempel med Animal

public class MonthTabAdapter extends RecyclerView.Adapter<MonthTabAdapter.MonthExpenseViewHolder> {

    private List<Expenses> expenseList;

    //konstruktør
    public MonthTabAdapter(List<Expenses> expenseList) { this.expenseList = expenseList; }

    //setter ViewHolder til month_tab_list_item.xml
    @NonNull
    @Override
    public MonthTabAdapter.MonthExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.month_tab_list_item, viewGroup, false);

        return new MonthTabAdapter.MonthExpenseViewHolder(v);
    }

    //henter posisjon i expenseList, kaller setExpenses-metoden
    @Override
    public void onBindViewHolder(MonthTabAdapter.MonthExpenseViewHolder expenseViewHolder, int i) {
        Expenses ex = expenseList.get(i);
        expenseViewHolder.setExpenses(ex);
    }

    @Override
    public int getItemCount() { return expenseList.size(); }

    //indre klasse for fylling av CardView/ViewHolder
    class MonthExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDaySum;
        private TextView txtDayCategory;

        //konstruktør som tar imot ViewHolder og henter info fra den
        public MonthExpenseViewHolder(View v) {
            super(v);
            txtDaySum = v.findViewById(R.id.txtDaySum);
            txtDayCategory = v.findViewById(R.id.txtDayCategory);
        }

        //setter innhold i view til å være info fra expenselist
        public void setExpenses (Expenses ex) {
            txtDaySum.setText(ex.getSum()+ ",-");
            txtDayCategory.setText(ex.getCategory());
        }
    }
}
