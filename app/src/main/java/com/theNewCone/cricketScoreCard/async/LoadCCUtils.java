package com.theNewCone.cricketScoreCard.async;

import android.os.AsyncTask;

import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;
import com.theNewCone.cricketScoreCard.utils.database.MatchStateDBHandler;

public class LoadCCUtils extends AsyncTask<Object, Void, CricketCardUtils> {
	@Override
	protected CricketCardUtils doInBackground(Object... asyncTaskObjects) {
		CricketCardUtils ccUtils = null;
		{
			if(asyncTaskObjects.length >= 2) {
				MatchStateDBHandler matchStateDBHandler = null;
				int matchStateID = 0;
				if(asyncTaskObjects[0] instanceof DatabaseHandler) {
					matchStateDBHandler = (MatchStateDBHandler) asyncTaskObjects[0];
				}
				if(asyncTaskObjects[1] instanceof Integer) {
					matchStateID = (int) asyncTaskObjects[1];
				}

				if (matchStateID > 0 && matchStateDBHandler != null) {
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
	protected void onPostExecute(CricketCardUtils cricketCardUtils) {
		super.onPostExecute(cricketCardUtils);
	}
}
