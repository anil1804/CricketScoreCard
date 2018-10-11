package com.thenewcone.myscorecard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.thenewcone.myscorecard.NavigationDrawerHandler;
import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.match.CricketCard;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;

public class LimitedOversScorecardActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

	CricketCard card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_limited_overs_scorecard);

		initCricketCard();
		initialSetup();
		setupDrawer();
	}

	void setupDrawer() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.limited_overs_scorecard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		Intent intent = NavigationDrawerHandler.onNavigationItemSelected(
				getApplicationContext(), item, this.getClass());

		if(intent != null)
			startActivity(intent);

		DrawerLayout drawer =  findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);

		return true;
	}

	private void initialSetup() {
		updateCardDetails(true);

		findViewById(R.id.btnRuns0).setOnClickListener(this);
		findViewById(R.id.btnRuns1).setOnClickListener(this);
		findViewById(R.id.btnRuns2).setOnClickListener(this);
		findViewById(R.id.btnRuns3).setOnClickListener(this);
		findViewById(R.id.btnRuns4).setOnClickListener(this);
		findViewById(R.id.btnRuns5).setOnClickListener(this);
		findViewById(R.id.btnRuns6).setOnClickListener(this);
		findViewById(R.id.btnWicket).setOnClickListener(this);
		findViewById(R.id.btnExtrasLegByes).setOnClickListener(this);
		findViewById(R.id.btnExtrasByes).setOnClickListener(this);
		findViewById(R.id.btnExtrasWides).setOnClickListener(this);
		findViewById(R.id.btnExtrasNoBall).setOnClickListener(this);
	}

	private void initCricketCard() {
		card = new CricketCard("Team1", "50.0", 5, 10, 10);

		card.newBatsman(new BatsmanStats("Batsman 1", 1), null);
		card.newBatsman(new BatsmanStats("Batsman 2", 2), null);
		card.setBowler(new BowlerStats("Bowler"));
	}

	private void updateCardDetails(boolean isInitial) {
		/* Main Score Details*/
		if(isInitial) {
			TextView tvBattingTeam = findViewById(R.id.tvBattingTeam);
			tvBattingTeam.setText(card.battingTeam);

		}

		TextView tvCurrScore = findViewById(R.id.tvScore);
		TextView tvOvers = findViewById(R.id.tvOvers);
		TextView tvCRR = findViewById(R.id.tvCRR);

		tvCurrScore.setText(String.valueOf(card.score + "/" + card.wicketsFallen));
		tvOvers.setText(String.format(getString(R.string.tvOversText), card.totalOversBowled));
		tvCRR.setText(CommonUtils.doubleToString(card.runRate, "#.##"));

		/* Chasing Score Details*/
		TextView tvTarget = findViewById(R.id.tvTarget);
		TextView tvRRR = findViewById(R.id.tvRRR);
		TextView tvMaxOvers = findViewById(R.id.tvMaxOvers);
		if(card.innings == 2) {
			if (isInitial) {
				tvTarget.setText(String.valueOf(card.target));

					tvMaxOvers.setText(String.format(getString(R.string.tvOversText), card.maxOvers));
			}

			tvRRR.setText(CommonUtils.doubleToString(card.reqRate, "#.##"));
		} else {
			tvTarget.setText("-");
			tvRRR.setText("-");
			tvMaxOvers.setText("");
		}

		/* Batsman-1 Details*/
		TextView tvBat1Name = findViewById(R.id.tvBat1Name);
		TextView tvBat1Runs = findViewById(R.id.tvBat1RunsScored);
		TextView tvBat1Balls = findViewById(R.id.tvBat1BallsFaced);
		TextView tvBat14s = findViewById(R.id.tvBat14sHit);
		TextView tvBat16s = findViewById(R.id.tvBat16sHit);
		TextView tvBat1SR = findViewById(R.id.tvBat1SR);

		tvBat1Name.setText(card.currentFacing.equals(card.batsman1) ? String.valueOf(card.batsman1.getBatsmanName() + " *") : card.batsman1.getBatsmanName());
		tvBat1Runs.setText(String.valueOf(card.batsman1.getRunsScored()));
		tvBat1Balls.setText(String.valueOf(card.batsman1.getBallsPlayed()));
		tvBat14s.setText(String.valueOf(card.batsman1.getNum4s()));
		tvBat16s.setText(String.valueOf(card.batsman1.getNum6s()));
		tvBat1SR.setText(CommonUtils.doubleToString(card.batsman1.getStrikeRate(), "#.##"));

		/* Batsman-2 Details*/
		TextView tvBat2Name = findViewById(R.id.tvBat2Name);
		TextView tvBat2Runs = findViewById(R.id.tvBat2RunsScored);
		TextView tvBat2Balls = findViewById(R.id.tvBat2BallsFaced);
		TextView tvBat24s = findViewById(R.id.tvBat24sHit);
		TextView tvBat26s = findViewById(R.id.tvBat26sHit);
		TextView tvBat2SR = findViewById(R.id.tvBat2SR);

		tvBat2Name.setText(card.currentFacing.equals(card.batsman2) ? String.valueOf(card.batsman2.getBatsmanName() + " *") : card.batsman2.getBatsmanName());
		tvBat2Runs.setText(String.valueOf(card.batsman2.getRunsScored()));
		tvBat2Balls.setText(String.valueOf(card.batsman2.getBallsPlayed()));
		tvBat24s.setText(String.valueOf(card.batsman2.getNum4s()));
		tvBat26s.setText(String.valueOf(card.batsman2.getNum6s()));
		tvBat2SR.setText(CommonUtils.doubleToString(card.batsman2.getStrikeRate(), "#.##"));

		/* Extras Details*/
		TextView tvLegByes = findViewById(R.id.tvLegByes);
		TextView tvByes = findViewById(R.id.tvByes);
		TextView tvWides = findViewById(R.id.tvWides);
		TextView tvNoBalls = findViewById(R.id.tvNoBalls);

		tvLegByes.setText(String.format(getString(R.string.legByes), card.legByes));
		tvByes.setText(String.format(getString(R.string.byes), card.byes));
		tvWides.setText(String.format(getString(R.string.wides), card.wides));
		tvNoBalls.setText(String.format(getString(R.string.noBalls), card.noBalls));

		/* Bowler Details */
		TextView tvBowlName = findViewById(R.id.tvBowlName);
		TextView tvBowlOvers = findViewById(R.id.tvBowlOvers);
		TextView tvBowlMaidens = findViewById(R.id.tvBowlMaidens);
		TextView tvBowlRuns = findViewById(R.id.tvBowlRuns);
		TextView tvBowlWickets = findViewById(R.id.tvBowlWickets);
		TextView tvBowlEconomy = findViewById(R.id.tvBowlEconomy);

		tvBowlName.setText(card.bowler.getBowlerName());
		tvBowlOvers.setText(card.bowler.getOversBowled());
		tvBowlMaidens.setText(String.valueOf(card.bowler.getMaidens()));
		tvBowlRuns.setText(String.valueOf(card.bowler.getRunsGiven()));
		tvBowlWickets.setText(String.valueOf(card.bowler.getWickets()));
		tvBowlEconomy.setText(CommonUtils.doubleToString(card.bowler.getEconomy(), "#.##"));
	}

	private void newBallBowled(int runs, @Nullable Extra extraData, @Nullable WicketData wicketData) {
		card.newBallBowled(extraData, runs, wicketData, false);
		updateCardDetails(false);
	}

	@Override
	public void onClick(View view) {
		Extra extra;
		switch (view.getId()) {
			case R.id.btnRuns0:
				newBallBowled(0, null, null);
				break;
			case R.id.btnRuns1:
				newBallBowled(1, null, null);
				break;
			case R.id.btnRuns2:
				newBallBowled(2, null, null);
				break;
			case R.id.btnRuns3:
				newBallBowled(3, null, null);
				break;
			case R.id.btnRuns4:
				newBallBowled(4, null, null);
				break;
			case R.id.btnRuns6:
				newBallBowled(6, null, null);
				break;
			case R.id.btnWicket:
				WicketData wicketData = new WicketData(card.currentFacing, WicketData.DismissalType.BOWLED, null, card.bowler);
				newBallBowled(0, null, wicketData);
				break;
			case R.id.btnExtrasLegByes:
				extra = new Extra(Extra.ExtraType.LEG_BYE, 1);
				newBallBowled(0, extra, null);
				break;
			case R.id.btnExtrasByes:
				extra = new Extra(Extra.ExtraType.BYE, 1);
				newBallBowled(0, extra, null);
				break;
			case R.id.btnExtrasWides:
				extra = new Extra(Extra.ExtraType.WIDE, 1);
				newBallBowled(0, extra, null);
				break;
			case R.id.btnExtrasNoBall:
				extra = new Extra(Extra.ExtraType.NO_BALL, 0);
				newBallBowled(0, extra, null);
				break;
		}
	}
}