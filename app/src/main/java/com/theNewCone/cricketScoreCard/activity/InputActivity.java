package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.R;

public class InputActivity extends Activity
    implements View.OnClickListener{

    public static final int RESP_CODE_OK = 1;
    public static final int RESP_CODE_CANCEL = -1;

    public static final String ARG_INPUT_TEXT = "InputText";

    EditText etInputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        findViewById(R.id.btnInputOK).setOnClickListener(this);
        findViewById(R.id.btnInputCancel).setOnClickListener(this);

        String inputText = "";
        if(getIntent().getExtras() != null) {
        	inputText = getIntent().getStringExtra(ARG_INPUT_TEXT);
        	inputText = inputText == null ? "" : inputText;
		}

        etInputText = findViewById(R.id.etStringInput);
        etInputText.setText(inputText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInputOK:
                String input = etInputText.getText().toString();
                if(input.length() > 0) {
                    Intent respIntent = new Intent();
                    respIntent.putExtra(ARG_INPUT_TEXT, etInputText.getText().toString());
                    setResult(RESP_CODE_OK, respIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Text", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnInputCancel:

                setResult(RESP_CODE_CANCEL);
                finish();
                break;
        }
    }
}
