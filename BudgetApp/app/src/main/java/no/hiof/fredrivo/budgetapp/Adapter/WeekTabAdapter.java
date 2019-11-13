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

public class WeekTabAdapter extends RecyclerView.Adapter<WeekTabAdapter.WeekExpenseViewHolder> {

    private List<Expenses> expenseList;
    private WeekViewClickListener weekViewClickListener;

    //konstruktør
    public WeekTabAdapter(List<Expenses> expenseList) { this.expenseList = expenseList; }

    //setter ViewHolder til week_tab_list_item.xml
    @NonNull
    @Override
    public WeekTabAdapter.WeekExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.week_tab_list_item, viewGroup, false);

        return new WeekTabAdapter.WeekExpenseViewHolder(v);
    }

    //henter posisjon i expenseList, kaller setExpenses-metoden
    @Override
    public void onBindViewHolder(WeekTabAdapter.WeekExpenseViewHolder expenseViewHolder, int i) {
        Expenses ex = expenseList.get(i);
        expenseViewHolder.setExpenses(ex);
    }

    @Override
    public int getItemCount() { return expenseList.size(); }

    //indre klasse for fylling av CardView/ViewHolder
    class WeekExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView txtWeekSum;
        private TextView txtWeekCategory;

        //konstruktør som tar imot ViewHolder og henter info fra den
        public WeekExpenseViewHolder(View v) {
            super(v);
            txtWeekSum = v.findViewById(R.id.txtWeekSum);
            txtWeekCategory = v.findViewById(R.id.txtDayCategory);
        }

        //setter innhold i view til å være info fra expenselist
        public void setExpenses (Expenses ex) {
            txtWeekSum.setText(ex.getSum()+ ",-");
            txtWeekCategory.setText(ex.getCategory());
        }
    }

    public interface WeekViewClickListener {
        void onClick(int position);
    }
}