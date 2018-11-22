package com.theNewCone.cricketScoreCard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.fragment.HelpDetailFragment;
import com.theNewCone.cricketScoreCard.help.HelpContent;

/**
 * An activity representing a single Question detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link HelpListActivity}.
 */
public class HelpDetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_detail);
		Toolbar toolbar = findViewById(R.id.detail_toolbar);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			HelpContent helpContent = (HelpContent) getIntent().getSerializableExtra(HelpDetailFragment.ARG_ITEM);
			HelpDetailFragment fragment = HelpDetailFragment.newInstance(helpContent);
			getSupportFragmentManager().beginTransaction().add(R.id.question_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			navigateUpTo(new Intent(this, HelpListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
