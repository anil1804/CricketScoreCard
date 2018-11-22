package com.theNewCone.cricketScoreCard.help;

import android.content.Context;

import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

public class HelpContentData {
	private DatabaseHandler dbh;

	public HelpContentData(Context context) {
		dbh = new DatabaseHandler(context);
		loadHelpContent();
	}

	private void loadHelpContent() {
		String content = "Overview";
		String text = "The Cricket Score Card app helps you track the score of a currently running Limited overs match.";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD)
			dbh.addHelpDetails(new HelpDetail(contentID, HelpDetail.ViewType.TEXT, text));
	}
}
