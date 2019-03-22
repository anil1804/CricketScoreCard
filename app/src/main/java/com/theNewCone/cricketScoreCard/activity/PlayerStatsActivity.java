package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.statistics.BatsmanData;
import com.theNewCone.cricketScoreCard.statistics.BowlerData;
import com.theNewCone.cricketScoreCard.statistics.PlayerData;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.StatisticsDBHandler;

public class PlayerStatsActivity extends Activity {

	public static final String ARG_PLAYER = "Player";
	Player player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_stats);

		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			player = (Player) extras.getSerializable(ARG_PLAYER);
		}

		PlayerData playerData = new StatisticsDBHandler(this).getPlayerStatistics(player);
		displayStats(playerData);
	}

	private void displayStats(PlayerData playerData) {
		((TextView) findViewById(R.id.tvPSPlayerName)).setText(playerData.getPlayer().getName());
		((TextView) findViewById(R.id.tvPSMatches)).setText(String.valueOf(playerData.getTotalInnings()));
		boolean hasStats = false;

		/* Batsman Statistics */
		BatsmanData batsmanData = playerData.getBatsmanData();
		if (batsmanData != null) {
			hasStats = true;

			String average = batsmanData.getAverage() > 0 ? CommonUtils.doubleToString(batsmanData.getAverage(), null) : "-";
			String strikeRate = batsmanData.getStrikeRate() > 0 ? CommonUtils.doubleToString(batsmanData.getStrikeRate(), null) : "-";

			((TextView) findViewById(R.id.tvPSBatRuns)).setText(String.valueOf(batsmanData.getTotalInnings()));
			((TextView) findViewById(R.id.tvPSBatInns)).setText(String.valueOf(batsmanData.getRunsScored()));
			((TextView) findViewById(R.id.tvPSBatBalls)).setText(String.valueOf(batsmanData.getBallsPlayed()));
			((TextView) findViewById(R.id.tvPSBatHS)).setText(String.valueOf(batsmanData.getHighestScore()));
			((TextView) findViewById(R.id.tvPSBat50s)).setText(String.valueOf(batsmanData.getFifties()));
			((TextView) findViewById(R.id.tvPSBat100s)).setText(String.valueOf(batsmanData.getHundreds()));
			((TextView) findViewById(R.id.tvPSBatAvg)).setText(average);
			((TextView) findViewById(R.id.tvPSBatSR)).setText(strikeRate);
		} else {
			findViewById(R.id.llPSBatting).setVisibility(View.GONE);
		}

		/* Bowler Statistics */
		BowlerData bowlerData = playerData.getBowlerData();
		if (bowlerData != null) {
			hasStats = true;

			String average = bowlerData.getAverage() > 0 ? CommonUtils.doubleToString(bowlerData.getAverage(), null) : "-";
			String economy = bowlerData.getAverage() > 0 ? CommonUtils.doubleToString(bowlerData.getAverage(), null) : "-";
			String strikeRate = bowlerData.getStrikeRate() > 0 ? CommonUtils.doubleToString(bowlerData.getStrikeRate(), null) : "-";

			((TextView) findViewById(R.id.tvPSBowlOvers)).setText(CommonUtils.doubleToString(bowlerData.getTotalInnings(), "#.#"));
			((TextView) findViewById(R.id.tvPSBowlOvers)).setText(CommonUtils.doubleToString(bowlerData.getOversBowled(), "#.#"));
			((TextView) findViewById(R.id.tvPSBowlRuns)).setText(String.valueOf(bowlerData.getRunsGiven()));
			((TextView) findViewById(R.id.tvPSBowlWickets)).setText(String.valueOf(bowlerData.getWicketsTaken()));
			((TextView) findViewById(R.id.tvPSBowlAvg)).setText(economy);
			((TextView) findViewById(R.id.tvPSBowlBF)).setText(String.valueOf(bowlerData.getBestFigures()));
			((TextView) findViewById(R.id.tvPSBowlAvg)).setText(average);
			((TextView) findViewById(R.id.tvPSBowlSR)).setText(strikeRate);
		} else {
			findViewById(R.id.llPSBowling).setVisibility(View.GONE);
		}

		/* Fielder Statistics */
		if (playerData.getCatches() > 0 || playerData.getRunOuts() > 0 || playerData.getStumps() > 0) {
			hasStats = true;

			((TextView) findViewById(R.id.tvPSCatches)).setText(String.valueOf(playerData.getCatches()));
			((TextView) findViewById(R.id.tvPSRunOuts)).setText(String.valueOf(playerData.getRunOuts()));
			((TextView) findViewById(R.id.tvPSStumpings)).setText(String.valueOf(playerData.getStumps()));
		} else {
			findViewById(R.id.llPSFielding).setVisibility(View.GONE);
		}

		if (!hasStats) {
			findViewById(R.id.llPSMatchInfo).setVisibility(View.GONE);
			findViewById(R.id.tvPSNoData).setVisibility(View.VISIBLE);
		}
	}
}
