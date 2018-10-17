package com.thenewcone.myscorecard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;

public class WicketDialogActivity extends Activity
	implements View.OnClickListener{

	RadioGroup rgWicket;

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wicket_dialog);

		setView();
	}

	private void setView() {
		rgWicket = findViewById(R.id.rgWicket);
		String[] wicketTypes = getResources().getStringArray(R.array.wicketType);

		Button btnEffectedBy = findViewById(R.id.btnEffectedByText);
		Button btnBatsmanOut = findViewById(R.id.btnBatsmanOutText);
		Button btnOK = findViewById(R.id.btnWktOK);
		Button btnCancel = findViewById(R.id.btnWktCancel);

		btnEffectedBy.setOnClickListener(this);
		btnBatsmanOut.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		int i=0;
		for(String wicketType : wicketTypes) {
			RadioButton rbWkt = new RadioButton(this);
			rbWkt.setText(wicketType);
			rbWkt.setOnClickListener(this);

			if(i == 0) {
				rbWkt.setChecked(true);
				rbWkt.setId(View.generateViewId());
			}
			i++;

			rgWicket.addView(rbWkt);
		}
	}

	@Override
	public void onClick(View view) {
		LinearLayout llEffectedBy = findViewById(R.id.llEffectedBy);
		LinearLayout llRunout = findViewById(R.id.llRunout);

		TextView tvEffectedBy = findViewById(R.id.tvEffectedBy);
		TextView tvBatsmanOut = findViewById(R.id.tvBatsmanOut);

		switch (view.getId()) {
			case R.id.btnBatsmanOutText:
				Toast.makeText(getApplicationContext(), "Show Batsman Dialog", Toast.LENGTH_SHORT).show();
				break;

			case R.id.btnEffectedByText:
				Toast.makeText(getApplicationContext(), "Show Player Dialog", Toast.LENGTH_SHORT).show();
				break;

			case R.id.btnWktOK:
				sendResponse(RESP_CODE_OK);
				break;

			case R.id.btnWktCancel:
				sendResponse(RESP_CODE_CANCEL);
				break;

			default:
				int getWicketTypeId = rgWicket.getCheckedRadioButtonId();
				String wicketType = ((RadioButton) findViewById(getWicketTypeId)).getText().toString();

				switch(wicketType.toUpperCase()) {
					case "CAUGHT":
						llEffectedBy.setVisibility(View.VISIBLE);
						tvEffectedBy.setText(R.string.caughtBy);
						break;
					case "RUN OUT":
						llEffectedBy.setVisibility(View.VISIBLE);
						llRunout.setVisibility(View.VISIBLE);
						tvEffectedBy.setText(R.string.runoutBy);
						break;
				}
		}
	}

	private void sendResponse(int responseCode) {
		setResult(responseCode);
		finish();
	}
}
