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
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

public class HelpListActivity extends AppCompatActivity {

	private final static String BUNDLE_HELP_CONTENTS = "HelpContents";
	private boolean mTwoPane;
	HelpContent[] helpContents;

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

		RecyclerView recyclerView = findViewById(R.id.question_list);
		assert recyclerView != null;

		if(savedInstanceState != null) {
			helpContents =
					CommonUtils.objectArrToHelpContentArr((
							Object[]) savedInstanceState.getSerializable(BUNDLE_HELP_CONTENTS));
		} else {
			SparseArray<HelpContent> helpContentSparseArray = new HelpContentLoader(this).getHelpContentItems();
			int helpContentSize = helpContentSparseArray != null ? helpContentSparseArray.size() : 0;
			helpContents = new HelpContent[helpContentSize];

			for (int i = 0; i < helpContentSize; i++) {
				helpContents[i] = helpContentSparseArray.valueAt(i);
			}
		}

		recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, helpContents, mTwoPane));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(BUNDLE_HELP_CONTENTS, helpContents);
	}

	public static class SimpleItemRecyclerViewAdapter
			extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

		private final AppCompatActivity mParentActivity;
		private final HelpContent[] helpContents;
		private final boolean mTwoPane;

		SimpleItemRecyclerViewAdapter(HelpListActivity parent,
									  HelpContent[] items,
									  boolean twoPane) {
			helpContents = items;
			mParentActivity = parent;
			mTwoPane = twoPane;
		}

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

		@Override
		@NonNull
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.help_list_content, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
			HelpContent currentContent = helpContents[position];
			holder.mIdView.setText(String.valueOf(currentContent.getContentID()));
			holder.mContentView.setText(currentContent.getContent());

			holder.itemView.setTag(currentContent);

			holder.itemView.setOnClickListener(mOnClickListener);
		}

		@Override
		public int getItemCount() {
			return helpContents.length;
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
