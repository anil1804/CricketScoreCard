package com.theNewCone.cricketScoreCard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.tournament.Group;

import java.util.List;

public class TournamentGroupScheduleAdapter extends RecyclerView.Adapter<TournamentGroupScheduleAdapter.ViewHolder>
		implements ListInteractionListener {

	private final List<Group> groupList;
	private final Context context;
	private final ListInteractionListener listener;
	private final TournamentFormat tournamentFormat;

	public TournamentGroupScheduleAdapter(@NonNull TournamentFormat format, @NonNull List<Group> groupList, @NonNull Context context,
										  ListInteractionListener listener) {
		this.tournamentFormat = format;
		this.groupList = groupList;
		this.context = context;
		this.listener = listener;
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.view_tournament_group_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		final int adapterPosition = holder.getAdapterPosition();
		holder.group = groupList.get(adapterPosition);

		String groupName = tournamentFormat == TournamentFormat.BILATERAL ? context.getResources().getString(R.string.matches) : holder.group.getName();
		holder.tvGroupName.setText(groupName);

		holder.rcvGroupTeamList.setHasFixedSize(false);

		holder.rcvGroupTeamList.setLayoutManager(new LinearLayoutManager(context));
		ScheduleViewAdapter adapter = new ScheduleViewAdapter(context, tournamentFormat, holder.group.getMatchInfoList(), this);
		holder.rcvGroupTeamList.setAdapter(adapter);

		LinearLayoutManager llm = new LinearLayoutManager(context);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		holder.rcvGroupTeamList.setLayoutManager(llm);

		holder.rcvGroupTeamList.setItemAnimator(new DefaultItemAnimator());
	}

	@Override
	public int getItemCount() {
		return groupList.size();
	}

	@Override
	public void onListFragmentInteraction(Object selItem) {
		listener.onListFragmentInteraction(selItem);
	}

	@Override
	public void onListFragmentMultiSelect(Object selItem, boolean removeItem) {
		listener.onListFragmentMultiSelect(selItem, removeItem);
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		final View mView;
		final TextView tvGroupName;
		final RecyclerView rcvGroupTeamList;
		Group group;

		ViewHolder(View view) {
			super(view);
			mView = view;
			tvGroupName = view.findViewById(R.id.tvGroupName);
			rcvGroupTeamList = view.findViewById(R.id.rcvGroupTeamList);
		}
	}

}