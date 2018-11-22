package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.PartnershipViewAdapter;
import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.match.Partnership;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.List;

public class PartnershipGraphActivity extends Activity {

	CricketCardUtils ccUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partnership_graph);

		if(getIntent() != null && getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			ccUtils = CommonUtils.convertToCCUtils(extras.getString(GraphsActivity.ARG_CRICKET_CARD_UTILS));
			CricketCard innsCard = (CricketCard) extras.getSerializable(GraphsActivity.ARG_CRICKET_CARD);

			if(innsCard != null) {
				boolean getCurrPartnership = innsCard.getInnings() == ccUtils.getCard().getInnings();

				if (innsCard.getPartnershipData() != null) {
					List<Partnership> partnershipData = innsCard.getPartnershipData();
					if (getCurrPartnership && ccUtils.getCurrentFacing() != null && ccUtils.getOtherBatsman() != null) {
						partnershipData.add(innsCard.getCurrPartnership());
					}

					RecyclerView rcvPartnershipDetails = findViewById(R.id.rcvPartnershipDetails);
					PartnershipViewAdapter adapter = new PartnershipViewAdapter(this, partnershipData, this);
					rcvPartnershipDetails.setAdapter(adapter);

					LinearLayoutManager llm = new LinearLayoutManager(this);
					llm.setOrientation(LinearLayoutManager.VERTICAL);
					rcvPartnershipDetails.setLayoutManager(llm);

					rcvPartnershipDetails.setItemAnimator(new DefaultItemAnimator());
				}
			}
		}
	}
}
