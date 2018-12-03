package com.theNewCone.cricketScoreCard.async;

import android.os.AsyncTask;

import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

public class StoreCCUtils extends AsyncTask<Object, Void, Void> {

	@Override
	protected Void doInBackground(Object... asyncTaskObjects) {
		if (!isCancelled()) {

			if(asyncTaskObjects.length >= 3) {
				CricketCardUtils ccUtils = null;
				DatabaseHandler dbHandler = null;
				int matchID = 0;

				if(asyncTaskObjects[0] instanceof CricketCardUtils)
					ccUtils = (CricketCardUtils) asyncTaskObjects[0];
				if(asyncTaskObjects[1] instanceof DatabaseHandler)
					dbHandler = (DatabaseHandler) asyncTaskObjects[1];
				if(asyncTaskObjects[2] instanceof Integer)
					matchID = (int) asyncTaskObjects[2];

				if(dbHandler!= null && ccUtils != null && matchID > 0) {
					dbHandler.autoSaveMatch(matchID, CommonUtils.convertToJSON(ccUtils), ccUtils.getMatchName());
					dbHandler.clearMatchStateHistory(DatabaseHandler.maxUndoAllowed, matchID, -1);
				}
			}
		}

		return null;
	}
}
