package com.theNewCone.cricketScoreCard.async;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.MatchStateDBHandler;

public class LoadCCUtils extends AsyncTask<Object, Void, CricketCardUtils> {
	private static ProgressBar progressBar;

	@Override
	protected CricketCardUtils doInBackground(Object... asyncTaskObjects) {
		CricketCardUtils ccUtils = null;
		{
			Context context = null;
			if(asyncTaskObjects.length >= 2) {
				int matchStateID = 0;
				if (asyncTaskObjects[0] != null && asyncTaskObjects[0] instanceof Context) {
					context = (Context) asyncTaskObjects[0];
				}
				if (asyncTaskObjects[1] != null && asyncTaskObjects[1] instanceof ProgressBar) {
					progressBar = (ProgressBar) asyncTaskObjects[1];
				}
				if (asyncTaskObjects[2] instanceof Integer) {
					matchStateID = (int) asyncTaskObjects[1];
				}

				if (matchStateID > 0 && context != null) {
					MatchStateDBHandler matchStateDBHandler = new MatchStateDBHandler(context);
					String matchData = matchStateDBHandler.retrieveMatchData(matchStateID);
					if (matchData != null) {
						ccUtils = CommonUtils.convertToCCUtils(matchData);
					}
				}
			}
		}

		return ccUtils;
	}

	@Override
	protected void onPreExecute() {
		if (progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
		}
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(CricketCardUtils cricketCardUtils) {
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
		super.onPostExecute(cricketCardUtils);
	}
}
