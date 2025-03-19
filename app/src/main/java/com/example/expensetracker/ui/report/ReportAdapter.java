package com.example.expensetracker.ui.report;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.entities.Expense;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Expense> expenseList = new ArrayList<>();


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
        holder.expenseCategory.setText(expense.getCategory());
        holder.expenseVendor.setText(expense.getVendor());


        if (expense.getReceiptImagePath() != null){
            File imagefile = new File(expense.getReceiptImagePath());
            if (imagefile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
                holder.expenseImage.setImageBitmap(bitmap);
            }
            else holder.expenseImage.setImageResource(R.drawable.ic_menu_camera);
        }
        else holder.expenseImage.setImageResource(R.drawable.ic_menu_camera);

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenseList = expenses;
        notifyDataSetChanged();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseDate, expenseVendor, expenseCategory, expenseAmount;
        ImageView expenseImage;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.rvExpenseName);
            expenseDate = itemView.findViewById(R.id.rvExpenseDate);
            expenseAmount = itemView.findViewById(R.id.rvAmountSpent);
            expenseCategory = itemView.findViewById(R.id.rvCategory);
            expenseVendor = itemView.findViewById(R.id.rvVendor);
            expenseImage = itemView.findViewById(R.id.rvReceiptImage);
        }
    }
}
