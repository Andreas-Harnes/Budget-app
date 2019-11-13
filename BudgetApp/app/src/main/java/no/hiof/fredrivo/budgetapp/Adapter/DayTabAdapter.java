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
//Fikk hjelp av Lars Emil for å få til onClick på cardView med interface

public class DayTabAdapter extends RecyclerView.Adapter<DayTabAdapter.DayExpenseViewHolder> {

    private List<Expenses> expenseList;
    private  DayViewClickListener dayViewClickListener;

    //konstruktør
    public DayTabAdapter(List<Expenses> expenseList, DayViewClickListener dayViewClickListener) {
        this.expenseList = expenseList;
        this.dayViewClickListener = dayViewClickListener;

    }

    //setter ViewHolder til day_tab_list_item.xml
    @NonNull
    @Override
    public DayTabAdapter.DayExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.day_tab_list_item, viewGroup, false);

        return new DayTabAdapter.DayExpenseViewHolder(v, dayViewClickListener);
    }

    //henter posisjon i expenseList, kaller setExpenses-metoden
    @Override
    public void onBindViewHolder(DayTabAdapter.DayExpenseViewHolder expenseViewHolder, int i) {
        Expenses ex = expenseList.get(i);
        expenseViewHolder.setExpenses(ex);

        /*expenseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayViewClickListener.onClick(i);
            }
        });*/
    }

    @Override
    public int getItemCount() { return expenseList.size(); }

    //indre klasse for fylling av CardView/ViewHolder
    class DayExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtDaySum;
        private TextView txtDayCategory;
        DayViewClickListener onExpenseClickListener;

        //konstruktør som tar imot ViewHolder og henter info fra den
        public DayExpenseViewHolder(View v, DayViewClickListener onExpenseClickListener) {
            super(v);
            txtDaySum = v.findViewById(R.id.txtDaySum);
            txtDayCategory = v.findViewById(R.id.txtDayCategory);
            this.onExpenseClickListener = onExpenseClickListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onExpenseClickListener.onClick(getAdapterPosition());
        }

        public int sum(Expenses ex){
            int sum = ex.getSum();

            return sum;
        }

        //setter innhold i view til å være info fra expenselist
        public void setExpenses (Expenses ex) {
            txtDayCategory.setText(ex.getCategory());
            txtDaySum.setText(sum(ex)+ ",-");
        }
    }

    public interface DayViewClickListener {
        void onClick(int position);
    }
}
