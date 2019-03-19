package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.player.Player;
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
		if (playerData.getBatsmanData() != null) {
			hasStats = true;

			((TextView) findViewById(R.id.tvPSBatRuns)).setText(String.valueOf(playerData.getBatsmanData().getTotalInnings()));
			((TextView) findViewById(R.id.tvPSBatInns)).setText(String.valueOf(playerData.getBatsmanData().getRunsScored()));
			((TextView) findViewById(R.id.tvPSBatBalls)).setText(String.valueOf(playerData.getBatsmanData().getBallsPlayed()));
			((TextView) findViewById(R.id.tvPSBatHS)).setText(String.valueOf(playerData.getBatsmanData().getHighestScore()));
			((TextView) findViewById(R.id.tvPSBat50s)).setText(String.valueOf(playerData.getBatsmanData().getFifties()));
			((TextView) findViewById(R.id.tvPSBat100s)).setText(String.valueOf(playerData.getBatsmanData().getHundreds()));
			((TextView) findViewById(R.id.tvPSBatAvg)).setText(CommonUtils.doubleToString(playerData.getBatsmanData().getAverage(), null));
			((TextView) findViewById(R.id.tvPSBatSR)).setText(CommonUtils.doubleToString(playerData.getBatsmanData().getStrikeRate(), null));
		} else {
			findViewById(R.id.llPSBatting).setVisibility(View.GONE);
		}

		/* Bowler Statistics */
		if (playerData.getBowlerData() != null) {
			hasStats = true;

			((TextView) findViewById(R.id.tvPSBowlOvers)).setText(CommonUtils.doubleToString(playerData.getBowlerData().getTotalInnings(), "#.#"));
			((TextView) findViewById(R.id.tvPSBowlOvers)).setText(CommonUtils.doubleToString(playerData.getBowlerData().getOversBowled(), "#.#"));
			((TextView) findViewById(R.id.tvPSBowlRuns)).setText(String.valueOf(playerData.getBowlerData().getRunsGiven()));
			((TextView) findViewById(R.id.tvPSBowlWickets)).setText(String.valueOf(playerData.getBowlerData().getWicketsTaken()));
			((TextView) findViewById(R.id.tvPSBowlBF)).setText(String.valueOf(playerData.getBowlerData().getBestFigures()));
			((TextView) findViewById(R.id.tvPSBowlAvg)).setText(CommonUtils.doubleToString(playerData.getBowlerData().getAverage(), null));
			((TextView) findViewById(R.id.tvPSBowlSR)).setText(CommonUtils.doubleToString(playerData.getBowlerData().getStrikeRate(), null));
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
