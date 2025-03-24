package com.example.expensetracker.ui.report;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.dao.ExpenseDAO;
import com.example.expensetracker.database.ExpenseDatabase;
import com.example.expensetracker.databinding.FragmentReportBinding;
import com.example.expensetracker.entities.Expense;
import com.example.expensetracker.ui.expensedetails.ExpenseDetailsActivity;

public class ReportFragment extends Fragment {

    private FragmentReportBinding binding;

    private EditText searchBar;
    private ImageView btnSearch;
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private int userID;
    private ExpenseDAO expenseDAO;


    public ReportFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report, container, false);

        ReportViewModel reportViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);


        searchBar = view.findViewById(R.id.searchBar);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        reportAdapter = new ReportAdapter(null, new ReportAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Expense expense) {
                Intent intent = new Intent(getContext(), ExpenseDetailsActivity.class);
                intent.putExtra("expenseID", expense.getExpenseID());
                startActivity(intent);
            }
        });




        recyclerView.setAdapter(reportAdapter);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", requireActivity().MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);

        if(userID ==-1){
            System.out.println("Error: User ID is not found.");
        } else {
            reportViewModel.getExpenseUser(userID).observe(getViewLifecycleOwner(), expenses -> {
                if (expenses != null) {
                    reportAdapter.setExpenses(expenses);
                }
            });
        }

        reportViewModel.getSearchResults().observe(getViewLifecycleOwner(), expenses -> {

            if(expenses == null || expenses.isEmpty()){
                reportAdapter.setExpenses(expenses);
                new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(getContext(), "No expenses found.", Toast.LENGTH_SHORT).show()
                );
            }
            reportAdapter.setExpenses(expenses);
        });

        btnSearch.setOnClickListener(v -> {
            String searchQuery = searchBar.getText().toString().trim();

            reportViewModel.searchExpenses(userID,searchQuery);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}