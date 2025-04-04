package com.dset.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dset.expensetracker.entities.Expense;
import com.dset.expensetracker.ui.login.LoginActivity;
import com.dset.expensetracker.ui.report.ReportViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.dset.expensetracker.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainActivity extends AppCompatActivity {

    private static final Logger log = LoggerFactory.getLogger(MainActivity.class);
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private TextView headerUsername;
    private Button headerLogOut;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);

        headerUsername = headerView.findViewById(R.id.tvUsername);
        headerLogOut = headerView.findViewById(R.id.btnLogout);

        headerLogOut.setOnClickListener(v -> {
            handleLogout();
                });

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        String username = preferences.getString("userName", "Guest");

        if(!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // System.out.println("Debug: Username retrieved from SharedPreferences: " + username);

        headerUsername.setText(username);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_expense, R.id.nav_report)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void handleLogout() {
        SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void exportExpenses() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userID = preferences.getInt("userID", -1);

        if (userID == -1) {
            Toast.makeText(this, "Error: User is not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        ReportViewModel reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        reportViewModel.getExpenseUser(userID).observe(this, expenses -> {
            if (expenses != null && !expenses.isEmpty()) {
                try{
                    File pdfFile = generatePDF(expenses);
                    sharePDF(pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error Generating PDF", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "No expenses found for the user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File generatePDF(List<Expense> expenses) throws IOException {
        File pdfFile = new File(getExternalFilesDir(null), "Expenses_Report.pdf");

        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        document.add(new Paragraph("Expense Report").setBold().setFontSize(22).setTextAlignment(TextAlignment.CENTER));

        float[] columnWidths = {40, 80, 80, 80, 80, 80, 80, 80, 150};
        Table table = new Table(columnWidths);
        table.addCell(new Cell().add(new Paragraph("Expense ID").setBold()));
        table.addCell(new Cell().add(new Paragraph("Name").setBold()));
        table.addCell(new Cell().add(new Paragraph("Vendor").setBold()));
        table.addCell(new Cell().add(new Paragraph("Category").setBold()));
        table.addCell(new Cell().add(new Paragraph("Amount").setBold()));
        table.addCell(new Cell().add(new Paragraph("Payment Method").setBold()));
        table.addCell(new Cell().add(new Paragraph("Expense Date").setBold()));
        table.addCell(new Cell().add(new Paragraph("Input Date").setBold()));
        table.addCell(new Cell().add(new Paragraph("Receipt").setBold()));

        for (Expense expense: expenses){
            table.addCell(new Cell().add(new Paragraph(String.valueOf(expense.getExpenseID()))));
            table.addCell(new Cell().add(new Paragraph(expense.getName())));
            table.addCell(new Cell().add(new Paragraph(expense.getVendor())));
            table.addCell(new Cell().add(new Paragraph(expense.getCategory())));
            table.addCell(new Cell().add(new Paragraph("$" + expense.getAmountSpent())));
            table.addCell(new Cell().add(new Paragraph(expense.getPaymentMethod())));
            table.addCell(new Cell().add(new Paragraph(expense.getExpenseDate())));
            table.addCell(new Cell().add(new Paragraph(expense.getTimeStamp())));

            if (expense.getReceiptImagePath() != null && !expense.getReceiptImagePath().isEmpty()){
                File imageFile = new File(expense.getReceiptImagePath());
                if (imageFile.exists()) {
                    ImageData imagedata = ImageDataFactory.create(imageFile.getAbsolutePath());
                    Image receiptImage = new Image(imagedata).setWidth(80).setHeight(80);
                    table.addCell(new Cell().add(receiptImage));
                }
                else {
                    table.addCell(new Cell().add(new Paragraph("No Receipt")));
                }
            }
            else {
                table.addCell(new Cell().add(new Paragraph("No Receipt")));
            }
        }
        document.add(table);
        document.close();

        return pdfFile;
    }

    private void sharePDF(File pdfFile){
        Uri pdfUri = FileProvider.getUriForFile(this, "com.example.expensetracker.fileprovider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Expense Report");

        startActivity(Intent.createChooser(intent, "Send Report"));
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.send_report) {
            exportExpenses();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}