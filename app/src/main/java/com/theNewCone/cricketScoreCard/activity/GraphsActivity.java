package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.Locale;

public class GraphsActivity extends Activity
	implements View.OnClickListener{

	public static final String ARG_CRICKET_CARD_UTILS = "CricketCardUtils";
	public static final String ARG_CRICKET_CARD = "CricketCard";
	public static final String ARG_CRICKET_CARD_PREV_INNS = "PreviousInningsCricketCard";

	RadioButton rbInnings1, rbInnings2;
	Button btnOverGraph, btnPartnershipGraph, btnRunRateGraph;

	CricketCardUtils ccUtils = null;
	CricketCard currentCard = null, previousCard = null, selectedInningsCard = null;
	String jsonCCUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graphs);

		rbInnings1 = findViewById(R.id.rbInnings1);
		rbInnings2 = findViewById(R.id.rbInnings2);

		rbInnings1.setOnClickListener(this);
		rbInnings2.setOnClickListener(this);

		btnOverGraph = findViewById(R.id.btnOverGraph);
		btnPartnershipGraph = findViewById(R.id.btnPartnershipGraph);
		btnRunRateGraph = findViewById(R.id.btnRunRateGraph);

		btnOverGraph.setOnClickListener(this);
		btnPartnershipGraph.setOnClickListener(this);
		btnRunRateGraph.setOnClickListener(this);

		Intent incomingIntent = getIntent();
		if (incomingIntent != null && incomingIntent.getExtras() != null) {
			Bundle extras = incomingIntent.getExtras();
			jsonCCUtils = extras.getString(ARG_CRICKET_CARD_UTILS);
			ccUtils = CommonUtils.convertToCCUtils(jsonCCUtils);

			if(ccUtils != null)
				initializeViewAndData();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.rbInnings1:
				selectedInningsCard = (previousCard != null) ? previousCard : currentCard;
				break;

			case R.id.rbInnings2:
				selectedInningsCard = currentCard;
				break;

			case R.id.btnOverGraph:
				showOverGraph();
				break;

			case R.id.btnPartnershipGraph:
				showPartnershipGraph();
				break;

			case R.id.btnRunRateGraph:
				showRunRateGraph();
				break;
		}
	}

	private void initializeViewAndData() {
		rbInnings1.setText(String.format(Locale.getDefault(), getString(R.string.tabInningsText), ccUtils.getTeam1().getShortName()));
		rbInnings2.setText(String.format(Locale.getDefault(), getString(R.string.tabInningsText), ccUtils.getTeam2().getShortName()));

		currentCard = ccUtils.getCard();
		selectedInningsCard = currentCard;

		if(ccUtils.getCard().getInnings() == 1) {
			rbInnings2.setEnabled(false);
			rbInnings1.setChecked(true);
		} else if(ccUtils.getCard().getInnings() == 2) {
			rbInnings2.setEnabled(true);
			rbInnings2.setChecked(true);
			previousCard = ccUtils.getPrevInningsCard();
		}
	}

	private void showOverGraph() {
		Intent ogIntent = new Intent(this, OverGraphActivity.class);
		ogIntent.putExtra(ARG_CRICKET_CARD, selectedInningsCard);
		startActivity(ogIntent);
	}

	private void showPartnershipGraph() {
		Intent pgIntent = new Intent(this, PartnershipGraphActivity.class);
		pgIntent.putExtra(ARG_CRICKET_CARD_UTILS, jsonCCUtils);
		pgIntent.putExtra(ARG_CRICKET_CARD, selectedInningsCard);
		startActivity(pgIntent);
	}

	private void showRunRateGraph() {
		Intent rrgIntent = new Intent(this, RunsGraphActivity.class);
		rrgIntent.putExtra(ARG_CRICKET_CARD, currentCard);
		rrgIntent.putExtra(ARG_CRICKET_CARD_PREV_INNS, previousCard);
		startActivity(rrgIntent);
	}
}
