package com.theNewCone.cricketScoreCard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.match.Team;

import java.util.List;

public class GroupTeamViewAdapter extends RecyclerView.Adapter<GroupTeamViewAdapter.ViewHolder> {

	private final List<Team> teamList;

	GroupTeamViewAdapter(@NonNull List<Team> teamList) {
		this.teamList = teamList;
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.view_tournament_group_item_team, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		final int adapterPosition = holder.getAdapterPosition();
		holder.team = teamList.get(adapterPosition);

		holder.tvGroupTeamName.setText(holder.team.getName());
	}

	@Override
	public int getItemCount() {
		return teamList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		final View mView;
		final TextView tvGroupTeamName;
		Team team;

		ViewHolder(View view) {
			super(view);
			mView = view;
			tvGroupTeamName = view.findViewById(R.id.tvGroupTeamName);
		}
	}

}