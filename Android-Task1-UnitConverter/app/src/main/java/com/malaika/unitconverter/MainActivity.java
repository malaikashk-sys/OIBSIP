package com.malaika.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCategory, spinnerFrom, spinnerTo;
    private EditText editTextInput;
    private Button btnConvert;
    private TextView textResult;

    private final String[] categories = {"Length", "Weight", "Temperature"};
    private final String[] lengthUnits = {"Centimeters", "Meters", "Inches", "Feet"};
    private final String[] weightUnits = {"Grams", "Kilograms", "Pounds"};
    private final String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editTextInput = findViewById(R.id.editTextInput);
        btnConvert = findViewById(R.id.btnConvert);
        textResult = findViewById(R.id.textResult);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnConvert.setOnClickListener(v -> performConversion());
    }

    private void updateUnitSpinners(int categoryPosition) {
        String[] currentUnits;
        if (categoryPosition == 0) {
            currentUnits = lengthUnits;
        } else if (categoryPosition == 1) {
            currentUnits = weightUnits;
        } else {
            currentUnits = tempUnits;
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, currentUnits);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(unitAdapter);
        spinnerTo.setAdapter(unitAdapter);
    }

    private void performConversion() {
        String inputStr = editTextInput.getText().toString().trim();

        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Please enter a value to convert", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputValue;
        try {
            inputValue = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid numeric input", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryPos = spinnerCategory.getSelectedItemPosition();
        String fromUnit = spinnerFrom.getSelectedItem().toString();
        String toUnit = spinnerTo.getSelectedItem().toString();
        double result = 0;

        if (categoryPos == 0) {
            result = convertLength(inputValue, fromUnit, toUnit);
        } else if (categoryPos == 1) {
            result = convertWeight(inputValue, fromUnit, toUnit);
        } else if (categoryPos == 2) {
            result = convertTemperature(inputValue, fromUnit, toUnit);
        }

        textResult.setText(String.format("Result: %.2f %s", result, toUnit));
    }

    private double convertLength(double val, String from, String to) {
        double inMeters = 0;
        switch (from) {
            case "Centimeters": inMeters = val / 100.0; break;
            case "Meters": inMeters = val; break;
            case "Inches": inMeters = val * 0.0254; break;
            case "Feet": inMeters = val * 0.3048; break;
        }
        switch (to) {
            case "Centimeters": return inMeters * 100.0;
            case "Meters": return inMeters;
            case "Inches": return inMeters / 0.0254;
            case "Feet": return inMeters / 0.3048;
            default: return 0;
        }
    }

    private double convertWeight(double val, String from, String to) {
        double inKg = 0;
        switch (from) {
            case "Grams": inKg = val / 1000.0; break;
            case "Kilograms": inKg = val; break;
            case "Pounds": inKg = val * 0.453592; break;
        }
        switch (to) {
            case "Grams": return inKg * 1000.0;
            case "Kilograms": return inKg;
            case "Pounds": return inKg / 0.453592;
            default: return 0;
        }
    }

    private double convertTemperature(double val, String from, String to) {
        if (from.equals(to)) return val;
        double celsius = 0;
        switch (from) {
            case "Celsius": celsius = val; break;
            case "Fahrenheit": celsius = (val - 32) * 5 / 9; break;
            case "Kelvin": celsius = val - 273.15; break;
        }
        switch (to) {
            case "Celsius": return celsius;
            case "Fahrenheit": return (celsius * 9 / 5) + 32;
            case "Kelvin": return celsius + 273.15;
            default: return 0;
        }
    }
}