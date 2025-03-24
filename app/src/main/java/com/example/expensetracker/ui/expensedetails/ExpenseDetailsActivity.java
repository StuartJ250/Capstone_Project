package com.example.expensetracker.ui.expensedetails;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.database.ExpenseDatabase;
import com.example.expensetracker.entities.Expense;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseDetailsActivity extends AppCompatActivity {

    private EditText expenseName, vendorName, description, amountSpent;
    private Spinner spinnerCategory, spinnerPaymentMethod;
    private Button btnUploadImage, btnUpdateExpense, btnDeleteExpense;
    private String selectedCategory, selectedPaymentMethod, imagePath;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView ivReceipt;
    private int expenseID;

    private EditText expenseDate;
    private Calendar calendar;
    private ExpenseDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Expense currentExpense;

    private Bitmap newBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        database = ExpenseDatabase.getInstance(this);

        expenseID = getIntent().getIntExtra("expenseID", -1);

        expenseName = findViewById(R.id.expenseName);
        vendorName = findViewById(R.id.vendorName);
        description = findViewById(R.id.description);
        amountSpent = findViewById(R.id.amountSpent);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);
        ivReceipt = findViewById(R.id.ivReceipt);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUpdateExpense = findViewById(R.id.btnUpdateExpense);
        btnDeleteExpense = findViewById(R.id.btnDeleteExpense);

        if(expenseID != -1) {
            executorService.execute(() ->{
                currentExpense = database.expenseDao().getExpense(expenseID);
                runOnUiThread(() -> populateExpenseDetails(currentExpense));
            });
        }


        spinnerSetup();
        imageSetup();

        btnUpdateExpense.setOnClickListener(v -> updateExpense());
        btnDeleteExpense.setOnClickListener(v -> deleteExpense());

        expenseDate = findViewById(R.id.expenseDate);
        calendar = Calendar.getInstance();

        expenseDate.setOnClickListener(v -> showDatePickerDialog());

    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ExpenseDetailsActivity.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateField();
                },

                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateField(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy", Locale.getDefault());
        expenseDate.setText(sdf.format(calendar.getTime()));
    }

    public void spinnerSetup(){
        //loading categories and payment methods from strings.xml and setting adapters
        String[] categories = getResources().getStringArray(R.array.categories);
        String[] paymentMethods = getResources().getStringArray(R.array.payment_methods);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(ExpenseDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
        ArrayAdapter<String> pmAdapter = new ArrayAdapter<>(ExpenseDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, paymentMethods);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerPaymentMethod.setAdapter(pmAdapter);

        // set up selection behaviors for categories
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories[position];
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = categories[0];

            }
        });

        // set up selection behaviors for payment methods
        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMethod = paymentMethods[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPaymentMethod = paymentMethods[0];
            }

        });
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            newBitmap = MediaStore.Images.Media.getBitmap(ExpenseDetailsActivity.this.getContentResolver(), selectedImageUri);
                            imagePath = saveImage(newBitmap);
                            ivReceipt.setImageBitmap(newBitmap);
                            ivReceipt.setImageURI(selectedImageUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    public void imageSetup(){
        btnUploadImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }

    //saves image
    private String saveImage(Bitmap bitmap) throws IOException {
        File file = new File(ExpenseDetailsActivity.this.getFilesDir(), System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(file)){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        }
        return file.getAbsolutePath();
    }

    private void populateExpenseDetails(Expense expense) {

        if (expense == null) return;

        ((EditText) findViewById(R.id.expenseName)).setText(expense.getName());
        ((EditText) findViewById(R.id.vendorName)).setText(expense.getVendor());
        ((EditText) findViewById(R.id.description)).setText(expense.getDescription());
        ((EditText) findViewById(R.id.amountSpent)).setText(String.valueOf(expense.getAmountSpent()));
        ((EditText) findViewById(R.id.expenseDate)).setText(expense.getExpenseDate());

        if(expense.getReceiptImagePath() != null && !expense.getReceiptImagePath().isEmpty()) {
            File imageFile = new File(expense.getReceiptImagePath());
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                ((ImageView) findViewById(R.id.ivReceipt)).setImageBitmap(bitmap);
            }
            else {
                ((ImageView) findViewById(R.id.ivReceipt)).setImageResource(R.drawable.ic_menu_camera);
            }
        }
        else {
            ((ImageView) findViewById(R.id.ivReceipt)).setImageResource(R.drawable.ic_menu_camera);
        }
    }

    private void updateExpense() {

        String name = ((EditText) findViewById(R.id.expenseName)).getText().toString();
        String vendor = ((EditText) findViewById(R.id.vendorName)).getText().toString();
        String description = ((EditText) findViewById(R.id.description)).getText().toString();
        String amount = ((EditText) findViewById(R.id.amountSpent)).getText().toString();
        String date = ((EditText) findViewById(R.id.expenseDate)).getText().toString();

        if (name.isEmpty() || amount.isEmpty() || date.isEmpty() || vendor.isEmpty()) {
            Toast.makeText(this, "Please complete required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amountSpent = Double.parseDouble(amount);

        if (newBitmap != null) {
            try {
                String newImagePath = saveImage(newBitmap);
                currentExpense.setReceiptImagePath(newImagePath);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        }



        currentExpense.setName(name);
        currentExpense.setVendor(vendor);
        currentExpense.setDescription(description);
        currentExpense.setAmountSpent(amountSpent);
        currentExpense.setExpenseDate(date);

        executorService.execute(() -> {
            database.expenseDao().update(currentExpense);
            runOnUiThread(() ->{
                Toast.makeText(this,"Expense updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void deleteExpense() {

        if (currentExpense != null) {
            executorService.execute(() -> {
                database.expenseDao().delete(currentExpense);
                runOnUiThread(() -> {
                    Toast.makeText(this,"Expense deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        }
    }
}
