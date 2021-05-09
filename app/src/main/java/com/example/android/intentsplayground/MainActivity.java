package com.example.android.intentsplayground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.intentsplayground.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private int qty=0;
    private ActivityMainBinding b;
    private int minValue,maxValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setupEventHandlers();
        getInitialCount();
    }

    private void getInitialCount() {
        //Get Data From Intent:
        Bundle bundle=getIntent().getExtras();
        qty=bundle.getInt(Constants.INITIAL_COUNT_KEY,0);
        minValue = bundle.getInt(Constants.MIN_VALUE, Integer.MIN_VALUE);
        maxValue=bundle.getInt(Constants.MAX_VALUE,Integer.MAX_VALUE);
        b.qty.setText(String.valueOf(qty));

        if(qty !=0){
            b.sendBackButton.setVisibility(View.VISIBLE);
        }
        b.qty.setText(String.valueOf(qty));
    }

    private void setupEventHandlers() {
        b.incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incQty();
            }
        });
        b.decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decQty();
            }
        });
    }


    public void decQty() {
        b.qty.setText(""+ --qty);
    }

    public void incQty() {
        b.qty.setText(""+ ++qty);
    }

    public void sendDataBack(View view) {
        //Validate Quantity:
        if (qty >= minValue && qty <= maxValue) {
            // Send the data to the starter activity
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_VALUE, qty);
            setResult(RESULT_OK, intent);

            // Close the activity
            finish();
        }
        // When not in range
        else {
            Toast.makeText(this, "Not in Range!", Toast.LENGTH_SHORT).show();
        }
    }

    // Instance State

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Constants.COUNT, qty);
    }
    }

