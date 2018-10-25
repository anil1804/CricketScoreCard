package com.thenewcone.myscorecard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.ListInteractionListener;
import com.thenewcone.myscorecard.match.Team;
import java.util.List;

public class TeamViewAdapter extends RecyclerView.Adapter<TeamViewAdapter.ViewHolder> {

    private final List<Team> teamList;
    private final ListInteractionListener mListener;
    private final boolean isMultiSelect;

    private SparseArray<Team> selTeam = new SparseArray<>();

    public TeamViewAdapter(List<Team> teamList, ListInteractionListener listener, boolean isMultiSelect) {
        this.teamList = teamList;
        mListener = listener;
        this.isMultiSelect = isMultiSelect;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  final ViewHolder holder, final int position) {

        holder.team = teamList.get(position);
        holder.tvTeamName.setText(holder.team.getName());
        holder.tvTeamShortName.setText(holder.team.getShortName());

		Team team = teamList.get(position);
		if (selTeam.get(team.getId()) != null) {
			holder.mView.setSelected(false);
			selTeam.remove(team.getId());
		} else {
			holder.mView.setSelected(true);
			selTeam.append(team.getId(), team);
		}

		holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(!isMultiSelect) {
					if (null != mListener) {
						// Notify the active callbacks interface (the activity, if the
						// fragment is attached to one) that an item has been selected.
						mListener.onListFragmentInteraction(holder.team);
					}
				} else {
					notifyItemChanged(position);
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
