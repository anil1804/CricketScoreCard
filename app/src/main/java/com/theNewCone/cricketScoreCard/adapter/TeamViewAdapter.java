package com.theNewCone.cricketScoreCard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.match.Team;

import java.util.List;

public class TeamViewAdapter extends RecyclerView.Adapter<TeamViewAdapter.ViewHolder> {

    private final List<Team> teamList;
    private final ListInteractionListener mListener;
    private final boolean isMultiSelect;

    private SparseBooleanArray selTeamIDs;

    public TeamViewAdapter(List<Team> teamList, List<Integer> currentlyAssociatedTo, ListInteractionListener listener, boolean isMultiSelect) {
        this.teamList = teamList;
        mListener = listener;
        this.isMultiSelect = isMultiSelect;

		selTeamIDs = new SparseBooleanArray();
        for(Team team : teamList) {
			if (currentlyAssociatedTo != null && currentlyAssociatedTo.contains(team.getId()))
				selTeamIDs.put(team.getId(), true);
		}
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		final int adapterPosition = holder.getAdapterPosition();

		holder.team = teamList.get(adapterPosition);
        holder.tvTeamName.setText(holder.team.getName());
        holder.tvTeamShortName.setText(holder.team.getShortName());

		if (selTeamIDs.get(holder.team.getId())) {
			holder.mView.setSelected(true);
		} else {
			holder.mView.setSelected(false);
		}

		holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				if (null != mListener) {
					if(!isMultiSelect) {
						mListener.onListFragmentInteraction(holder.team);
					} else {
						boolean isPresent = selTeamIDs.get(holder.team.getId());
						mListener.onListFragmentMultiSelect(holder.team, isPresent);
						selTeamIDs.put(holder.team.getId(), !isPresent);
						notifyItemChanged(adapterPosition);
					}
				}
			}
	        });
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Team team;
        final View mView;
        final TextView tvTeamName, tvTeamShortName;

        ViewHolder(View view) {
            super(view);
            mView = view;
            tvTeamName = view.findViewById(R.id.tvTeamName);
            tvTeamShortName = view.findViewById(R.id.tvTeamShortName);
        }
    }
}
