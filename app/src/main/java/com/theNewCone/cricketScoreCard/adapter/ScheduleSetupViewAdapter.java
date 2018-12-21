package com.theNewCone.cricketScoreCard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;

import java.util.List;

public class ScheduleSetupViewAdapter extends RecyclerView.Adapter<ScheduleSetupViewAdapter.ViewHolder> {

	private final List<MatchInfo> matchInfoList;

	public ScheduleSetupViewAdapter(List<MatchInfo> matchInfoList) {
		this.matchInfoList = matchInfoList;
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		int layoutId = R.layout.view_tournament_schedule_item;

		View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		final int adapterPosition = holder.getAdapterPosition();
		holder.matchInfo = matchInfoList.get(adapterPosition);

		String versusText = holder.matchInfo.getTeam1().getShortName() + " vs " + holder.matchInfo.getTeam2().getShortName();
		holder.tvVersus.setText(versusText);
	}

	@Override
	public int getItemCount() {
		return matchInfoList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		final View mView;
		final TextView tvVersus;
		MatchInfo matchInfo;

		ViewHolder(View view) {
			super(view);
			mView = view;

			tvVersus = view.findViewById(R.id.tvScheduleVersus);
		}
	}
}
