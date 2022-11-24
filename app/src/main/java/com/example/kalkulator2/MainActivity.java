package com.example.kalkulator2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editValue1, editValue2;
    Button buttonCalc;
    TextView textResult;
    RadioGroup radioOperators;
    String OPERATOR;
    SharedPreferences preferences;

    private ArrayList<Riwayat> listRiwayat;
    private RecyclerView recRiwayat;

    RiwayatAdapter adapter;
    int temp=1, id=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        setupListener();
        showArray();

        if (listRiwayat.size() == 0) {
            id = 1;
        } else {
            id = Integer.parseInt(listRiwayat.get(listRiwayat.size()-1).getId())+1;
        }
    }

    private void setupView() {
        editValue1 = findViewById(R.id.edit_value1);
        editValue2 = findViewById(R.id.edit_value2);
        buttonCalc = findViewById(R.id.button_calc);
        textResult = findViewById(R.id.text_result);
        radioOperators = findViewById(R.id.radio_operators);

        preferences = this.getSharedPreferences("hisotry", Context.MODE_PRIVATE);
        recRiwayat = findViewById(R.id.rvRiwayat);

        listRiwayat = new ArrayList<>();
        temp = preferences.getAll().size()+1;

        adapter = new RiwayatAdapter(listRiwayat, this, preferences);
    }

    public void showArray() {
        Map<String, ?> entries = preferences.getAll();
        for (Map.Entry<String, ?> entry: entries.entrySet()) {
            getArray(entry.getKey(), entry.getValue().toString());
        }
    }

    public void saveToShared(String id,String hasil){
        try {
            preferences.edit().putString(id,hasil).apply();
            String value =preferences.getString(id,"");
            getArray(id,value);
            temp++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getArray(String no,String rwyt){
        try{
            recRiwayat.setAdapter(new RiwayatAdapter(listRiwayat, this, preferences));
            recRiwayat.setLayoutManager(new LinearLayoutManager(this));
            listRiwayat.add(new Riwayat(no,String.valueOf(rwyt)));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("gagal tambah array");
        }
    }

    private void setupListener() {
        buttonCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()) {
                    double value1 = Double.parseDouble(editValue1.getText().toString());
                    double value2 = Double.parseDouble(editValue2.getText().toString());
                    textResult.setText(
                            value(value1, value2)
                    );
                } else {
                    showMessage("Masukkan data dengan benar");
                }

            }
        });

        radioOperators.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                OPERATOR = radioButton.getText().toString();
                textResult.setText("Hasil");
            }
        });
    }

    private  Boolean validate() {
        if (editValue1.getText().toString().equals("") || editValue1.getText() == null) {
            return false;
        } else if (editValue2.getText().toString().equals("") || editValue2.getText() == null) {
            return false;
        } else if (OPERATOR == null) {
            return false;
        }

        return true;
    }

    private String value(double value1, double value2) {
        double value = 0;
        String idRwyt = String.valueOf(id);

        if (OPERATOR.equals("Tambah")) {
            value = value1 + value2;
            String riwayat = Double.toString(value1)+ " + " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);

        } else if (OPERATOR.equals("Kurang")) {
            value = value1 - value2;
            String riwayat = Double.toString(value1)+ " - " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);

        } else if (OPERATOR.equals("Kali")) {
            value = value1 * value2;
            String riwayat = Double.toString(value1)+ " x " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);

        } else if (OPERATOR.equals("Bagi")) {
            value = value1 / value2;
            String riwayat = Double.toString(value1)+ " : " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);
        }
        id++;

        // menampilkan histori dari yang paling baru
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recRiwayat.setLayoutManager(linearLayoutManager);

        return String.valueOf(value);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}