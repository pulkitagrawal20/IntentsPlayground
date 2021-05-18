package com.example.android.intentsplayground;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.intentsplayground.databinding.ActivityIntentsPlaygroundBinding;

import java.net.URI;

public class IntentsPlaygroundActivity extends AppCompatActivity {
    private static final int REQUEST_COUNT = 0;
    ActivityIntentsPlaygroundBinding B;
    private boolean resultReceived=false;
    private int finalCount=0;

    // Initial setup:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout();

        RestoreDataFromSavedInstances(savedInstanceState);

        setupHideError();
    }

    // Setup the layout using the root element of the UI:

    private void setupLayout() {
        B=ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
    }
    //Text watcher which gives callback when the text in the text fields changes:

    private void setupHideError() {
        TextWatcher myTextWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        B.data.getEditText().addTextChangedListener(myTextWatcher);
        B.initialCounterEditText.getEditText().addTextChangedListener(myTextWatcher);
    }


    //Event Handlers:
    public void SendImplicitIntent(View view) {
        String input=B.data.getEditText().getText().toString().trim();
        //Radio Button defined:
        if(input.isEmpty()){
            B.data.setError("Please Enter Something!!");
            return;
        }

        int type=B.RadioGroup.getCheckedRadioButtonId();

       if(type==R.id.webPage) {
           openWebPage(input);

       }else if(type==R.id.dialNumber) {
           dialNumber(input);

       }else if(type==R.id.shareText) {
           shareText(input);
       }else
           Toast.makeText(this, "Please Select Atleast One Option!!", Toast.LENGTH_SHORT).show();

    }

    public void SendData(View view) {
        String input=B.initialCounterEditText.getEditText().getText().toString().trim();

        if(input.isEmpty()){
            B.initialCounterEditText.setError("Please Enter Something!!");
            return;
        }
        //Get Count:
        int initialCount=Integer.parseInt(input);
        //Create Bundle To Pass:
        Bundle bundle=new Bundle();
        bundle.putInt(Constants.INITIAL_COUNT_KEY,initialCount);
        bundle.putInt(Constants.MIN_VALUE,-100);
        bundle.putInt(Constants.MAX_VALUE,100);

        //Create intent:
        Intent intent=new Intent(this,MainActivity.class);
        //Pass Bundle:
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_COUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_COUNT && resultCode==RESULT_OK){
            //Get Data:
            int count = data.getIntExtra(Constants.FINAL_VALUE,Integer.MIN_VALUE);

            resultReceived=true;
            finalCount=count;

            //Show Data
            setResultInTextView(count);
        }
    }

    private void setResultInTextView(int count) {
        B.result.setText("Final count received : " +count);
        B.result.setVisibility(View.VISIBLE);
    }

    //Implicit Intent Sender:
        //share text class:

    private void shareText(String text) {
        Intent intent = new Intent(); intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "Share text via"));
    }
    //Dial number Class:
    private void dialNumber(String number) {
        //Checking Dial Number match:
        if(!number.matches("^\\d{10}$")){
            B.data.setError("Invalid Mobile Number!!");
            return;
        }
        //If matches:
        Uri uri=Uri.parse("tel:"+ number);
        Intent intent=new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);

        hideError();
    }
    //webPage class
    private void openWebPage(String url) {
        //Checking url match:
        if(!url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
            B.data.setError("Invalid Url!!");
            return;
        }
        //If matches :
        Uri uri=Uri.parse(url);
        Intent intent=new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

        hideError();
    }
    //Utility Function:
    private void hideError(){
        B.data.setError(null);
        B.initialCounterEditText.setError(null);
    }

    public void openMainActivity(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    //To Restore Data:
    private void RestoreDataFromSavedInstances(Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            resultReceived=savedInstanceState.getBoolean("ResultReceived",false);

            if(resultReceived){
                finalCount=savedInstanceState.getInt("FinalCount");

                setResultInTextView(finalCount);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("ResultReceived",resultReceived);

        if(resultReceived)
            outState.putInt("FinalCount",finalCount);
    }
}