package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.enumeration.InputType;

public class InputActivity extends Activity
    implements View.OnClickListener{

    public static final int RESP_CODE_OK = 1;
    public static final int RESP_CODE_CANCEL = -1;

	public static final String ARG_INPUT_TYPE = "InputType";
	public static final String ARG_TITLE = "Title";

	public static final String ARG_TEXT_INPUT = "InputText";

	public static final String ARG_SB_MAX = "SeekBarMaximum";
	public static final String ARG_SB_PROGRESS_INPUT = "SeekBarProgress";

    EditText etInputText;
	SeekBar sbInput;
	InputType inputType = InputType.TEXT;
	TextView tvSBInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        findViewById(R.id.btnInputOK).setOnClickListener(this);
        findViewById(R.id.btnInputCancel).setOnClickListener(this);

        String inputText = "";
		int sbMax = 3;
        if(getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			inputText = extras.getString(ARG_TEXT_INPUT);
        	inputText = inputText == null ? "" : inputText;

			inputType = extras.get(ARG_INPUT_TYPE) != null ? InputType.valueOf(extras.getString(ARG_INPUT_TYPE)) : InputType.TEXT;

			if (extras.get(ARG_TITLE) != null) {
				((TextView) findViewById(R.id.tvInputTitle)).setText(extras.getString(ARG_TITLE));
			}

			sbMax = extras.get(ARG_SB_MAX) != null ? extras.getInt(ARG_SB_MAX) : 3;
		}

		etInputText = findViewById(R.id.etStringInput);
		sbInput = findViewById(R.id.sbInput);
		tvSBInput = findViewById(R.id.tvSBInput);

		sbInput.setProgress(1);
		tvSBInput.setText(
				String.format(
						getResources().getString(R.string.cancelledRuns),
						sbInput.getProgress()));

		switch (inputType) {
			case TEXT:
				etInputText.setVisibility(View.VISIBLE);
				etInputText.setText(inputText);
				break;

			case SEEK_BAR:
				findViewById(R.id.llSBInput).setVisibility(View.VISIBLE);
				sbInput.setMax(sbMax);
				sbInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
						if (sbInput.getProgress() < 1) {
							sbInput.setProgress(1);
						}
						tvSBInput.setText(
								String.format(
										getResources().getString(R.string.cancelledRuns),
										sbInput.getProgress()));
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}
				});
				break;
		}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInputOK:
				Intent respIntent = new Intent();
				switch (inputType) {
					case TEXT:
						String input = etInputText.getText().toString();
						if (input.length() > 0) {
							respIntent.putExtra(ARG_TEXT_INPUT, input);
							setResult(RESP_CODE_OK, respIntent);
							finish();
						} else {
							Toast.makeText(getApplicationContext(), "Please Enter Valid Text", Toast.LENGTH_SHORT).show();
						}
						break;

					case SEEK_BAR:
						int progress = sbInput.getProgress();
						respIntent.putExtra(ARG_SB_PROGRESS_INPUT, progress);
						setResult(RESP_CODE_OK, respIntent);
						finish();
						break;
				}
                break;

            case R.id.btnInputCancel:
                setResult(RESP_CODE_CANCEL);
                finish();
                break;
        }
    }
}
