package com.dset.expensetracker.ui.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dset.expensetracker.R;
import com.dset.expensetracker.entities.Expense;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Expense> expenseList = new ArrayList<>();


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Expense expense);
    }

    public ReportAdapter(List<Expense> expenseList, OnItemClickListener onItemClickListener){
        this.expenseList = expenseList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {

        Expense expense = expenseList.get(position);
        holder.expenseName.setText(expense.getName());
        holder.expenseAmount.setText("$" + expense.getAmountSpent());
        holder.expenseDate.setText(expense.getExpenseDate());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(expense));
    }

    @Override
    public int getItemCount() {
        return (expenseList != null) ?expenseList.size() : 0;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenseList = expenses;
        notifyDataSetChanged();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseDate, expenseAmount;


        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.rvExpenseName);
            expenseDate = itemView.findViewById(R.id.rvExpenseDate);
            expenseAmount = itemView.findViewById(R.id.rvAmountSpent);


        }
    }
}
