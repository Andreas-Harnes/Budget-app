package no.hiof.fredrivo.budgetapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import no.hiof.fredrivo.budgetapp.R;
import no.hiof.fredrivo.budgetapp.classes.Expenses;

//brukte Lars Emil sitt RecyclerViewExercise eksempel med Animal som utgangspunkt

public class CategoryDetailAdapter extends RecyclerView.Adapter<CategoryDetailAdapter.ExpenseViewHolder> {

    private List<Expenses> expenseList;
    private LayoutInflater layoutInflater;

    public CategoryDetailAdapter(Context context, List<Expenses> expenseList) {
        layoutInflater = LayoutInflater.from(context);
        this.expenseList = expenseList;
    }


    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.category_detail_list_item, viewGroup, false);

        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder expenseViewHolder, int i) {
        Expenses ex = expenseList.get(i);

        expenseViewHolder.setExpenses(ex);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCatDetailSum;
        private TextView txtCatDetailDate;
        private TextView txtCatDetailLocation;
        private TextView txtCatDetailCategory;

        public ExpenseViewHolder(View v) {
            super(v);
            txtCatDetailSum = v.findViewById(R.id.txtCatDetailSum);
            txtCatDetailDate = v.findViewById(R.id.txtCatDetailDate);
            txtCatDetailLocation = v.findViewById(R.id.txtCatDetailLocation);
            txtCatDetailCategory = v.findViewById(R.id.txtCatDetailCategory);
        }

        public void setExpenses (Expenses ex) {
            txtCatDetailSum.setText(ex.getSum()+"");
            txtCatDetailDate.setText(ex.getDate());
            txtCatDetailLocation.setText(ex.getLocation());
            txtCatDetailCategory.setText(ex.getCategory());
        }
    }

}

