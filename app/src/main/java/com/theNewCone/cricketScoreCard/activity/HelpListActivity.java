package com.theNewCone.cricketScoreCard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.fragment.HelpDetailFragment;

import com.theNewCone.cricketScoreCard.help.HelpContent;
import com.theNewCone.cricketScoreCard.help.HelpContentLoader;

/**
 * An activity representing a list of Question. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link HelpDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class HelpListActivity extends AppCompatActivity {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	HelpContentLoader helpContentLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_list);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		if (findViewById(R.id.question_detail_container) != null) {
			mTwoPane = true;
		}

		View recyclerView = findViewById(R.id.question_list);
		assert recyclerView != null;

		helpContentLoader = new HelpContentLoader(this);
		setupRecyclerView((RecyclerView) recyclerView);
	}

	private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
		recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, helpContentLoader.getHelpContentItems(), mTwoPane));
	}

	public static class SimpleItemRecyclerViewAdapter
			extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

		private final AppCompatActivity mParentActivity;
		private final SparseArray<HelpContent> mValues;
		private final boolean mTwoPane;
		private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				HelpContent item = (HelpContent) view.getTag();
				if (mTwoPane) {
					Bundle arguments = new Bundle();
					HelpDetailFragment fragment = HelpDetailFragment.newInstance(item);
					fragment.setArguments(arguments);
					mParentActivity.getSupportFragmentManager().beginTransaction()
							.replace(R.id.question_detail_container, fragment)
							.commit();
				} else {
					Context context = view.getContext();
					Intent intent = new Intent(context, HelpDetailActivity.class);
					intent.putExtra(HelpDetailFragment.ARG_ITEM, item);

					context.startActivity(intent);
				}
			}
		};

		SimpleItemRecyclerViewAdapter(HelpListActivity parent,
									  SparseArray<HelpContent> items,
									  boolean twoPane) {
			mValues = items;
			mParentActivity = parent;
			mTwoPane = twoPane;
		}

		@Override
		@NonNull
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.help_list_content, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
			HelpContent currentContent = mValues.valueAt(position);
			holder.mIdView.setText(String.valueOf(currentContent.getContentID()));
			holder.mContentView.setText(currentContent.getContent());

			holder.itemView.setTag(currentContent);

			holder.itemView.setOnClickListener(mOnClickListener);
		}

		@Override
		public int getItemCount() {
			return mValues.size();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			final TextView mIdView;
			final TextView mContentView;

			ViewHolder(View view) {
				super(view);
				mIdView = view.findViewById(R.id.id_text);
				mContentView = view.findViewById(R.id.content);
			}
		}
	}
}
