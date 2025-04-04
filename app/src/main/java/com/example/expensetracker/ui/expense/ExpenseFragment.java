package com.example.expensetracker.ui.expense;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetracker.R;
import com.example.expensetracker.database.ExpenseDatabase;
import com.example.expensetracker.databinding.FragmentExpenseBinding;
import com.example.expensetracker.entities.Expense;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseFragment extends Fragment {

    private FragmentExpenseBinding binding;
    private EditText expenseName, vendorName, description, amountSpent;
    private Spinner spinnerCategory, spinnerPaymentMethod;
    private Button  btnUploadImage, btnSubmitExpense;
    private String selectedCategory, selectedPaymentMethod, imagePath;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView ivReceipt;
    private ExpenseViewModel expenseViewModel;


    private EditText expenseDate;
    private Calendar calendar;

    public ExpenseFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        expenseName = view.findViewById(R.id.expenseName);
        vendorName = view.findViewById(R.id.vendorName);
        description = view.findViewById(R.id.description);
        amountSpent = view.findViewById(R.id.amountSpent);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerPaymentMethod = view.findViewById(R.id.spinnerPaymentMethod);
        ivReceipt = view.findViewById(R.id.ivReceipt);
        btnUploadImage = view.findViewById(R.id.btnUploadImage);
        btnSubmitExpense = view.findViewById(R.id.btnSubmitExpense);

        expenseViewModel =
                new ViewModelProvider(this).get(ExpenseViewModel.class);

        spinnerSetup();
        imageSetup();

        btnSubmitExpense.setOnClickListener(v -> saveExpense());

        expenseDate = view.findViewById(R.id.expenseDate);
        calendar = Calendar.getInstance();

        expenseDate.setOnClickListener(v -> showDatePickerDialog());

        return view;

    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
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
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        ArrayAdapter<String> pmAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, paymentMethods);
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

    // sets imageView to the selected image
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                            imagePath = saveImage(bitmap);
                            ivReceipt.setImageBitmap(bitmap);
                            ivReceipt.setImageURI(selectedImageUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    // sets onClickListener to detect click of btnUploadImage
    public void imageSetup(){
        btnUploadImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }

    //saves image
    private String saveImage(Bitmap bitmap) throws IOException {
        File file = new File(requireContext().getFilesDir(), System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(file)){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        }
        return file.getAbsolutePath();
    }

    private void saveExpense(){
        String name = expenseName.getText().toString();
        String vendor = vendorName.getText().toString();
        String mDescription = description.getText().toString();
        String amountString = amountSpent.getText().toString();
        String mExpenseDate = expenseDate.getText().toString();

        SharedPreferences preferences = requireActivity().getSharedPreferences("user_prefs", requireActivity().MODE_PRIVATE);
        int userID = preferences.getInt("userID", -1);

        if(userID == -1) {
            Snackbar.make(requireView(), "Error! No login detected!", Snackbar.LENGTH_LONG).show();
            return;
        }

        // validate values are filled out
        if (name.isEmpty() || vendor.isEmpty() || amountString.isEmpty()) {
            Snackbar.make(requireView(), "Please complete the required fields", Snackbar.LENGTH_LONG).show();
            return;
        }
        double amountDouble = Double.parseDouble(amountString);
        Expense expense = new Expense(name, vendor, selectedCategory, mDescription, amountDouble, selectedPaymentMethod, imagePath, mExpenseDate, userID);

        //inserts into database
        new Thread(() ->
                ExpenseDatabase.getInstance(getContext()).expenseDao().insert(expense)).start();

        // notify user of save
        Snackbar.make(requireView(), "Expense added successfully", Snackbar.LENGTH_LONG).show();


        //field reset
        expenseName.setText("");
        vendorName.setText("");
        description.setText("");
        amountSpent.setText("");
        ivReceipt.setImageResource(android.R.color.darker_gray);
        imagePath = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}