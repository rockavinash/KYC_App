package com.example.kycapplication;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextName, editTextDob, editTextEmail, editTextPhone, editTextAadhar, editTextAddress;
    private TextView textViewError;
    private Button buttonSubmit, buttonShowData;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextDob = findViewById(R.id.editTextDob);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAadhar = findViewById(R.id.editTextAadhar);
        editTextAddress = findViewById(R.id.editTextAddress);
        textViewError = findViewById(R.id.textViewError);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonShowData = findViewById(R.id.buttonShowData);

        sharedPreferences = getSharedPreferences("KYC_DATA", MODE_PRIVATE);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    saveData();
                    showThankYouPopup();
                }
            }
        });

        buttonShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSavedData();
            }
        });
    }

    private boolean validateForm() {
        String name = editTextName.getText().toString().trim();
        String dob = editTextDob.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String aadhar = editTextAadhar.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(aadhar) || TextUtils.isEmpty(address)) {
            showError("All fields are required");
            return false;
        }

        if (!isValidDate(dob)) {
            showError("Invalid date format. Please use YYYY-MM-DD");
            return false;
        }

        if (!isValidEmail(email)) {
            showError("Invalid email format");
            return false;
        }

        if (!isValidPhone(phone)) {
            showError("Invalid phone number");
            return false;
        }

        if (!isValidAadhar(aadhar)) {
            showError("Invalid Aadhar number");
            return false;
        }

        hideError();
        return true;
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", editTextName.getText().toString().trim());
        editor.putString("dob", editTextDob.getText().toString().trim());
        editor.putString("email", editTextEmail.getText().toString().trim());
        editor.putString("phone", editTextPhone.getText().toString().trim());
        editor.putString("aadhar", editTextAadhar.getText().toString().trim());
        editor.putString("address", editTextAddress.getText().toString().trim());
        editor.apply();
    }

    private void showSavedData() {
        String name = sharedPreferences.getString("name", "N/A");
        String dob = sharedPreferences.getString("dob", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String phone = sharedPreferences.getString("phone", "N/A");
        String aadhar = sharedPreferences.getString("aadhar", "N/A");
        String address = sharedPreferences.getString("address", "N/A");

        new AlertDialog.Builder(this)
                .setTitle("Saved KYC Data")
                .setMessage("Name: " + name + "\n" +
                        "Date of Birth: " + dob + "\n" +
                        "Email: " + email + "\n" +
                        "Phone: " + phone + "\n" +
                        "Aadhar: " + aadhar + "\n" +
                        "Address: " + address)
                .setPositiveButton("OK", null)
                .show();
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        return emailPattern.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    private boolean isValidAadhar(String aadhar) {
        return aadhar.matches("\\d{12}");
    }

    private void showError(String message) {
        textViewError.setText(message);
        textViewError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        textViewError.setVisibility(View.GONE);
    }

    private void showThankYouPopup() {
        new AlertDialog.Builder(this)
                .setTitle("Thank You")
                .setMessage("Thank you for submitting your KYC details.")
                .setPositiveButton("OK", null)
                .show();
    }
}
