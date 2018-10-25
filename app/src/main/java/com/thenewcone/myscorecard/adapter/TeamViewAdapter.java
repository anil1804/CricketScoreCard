package com.thenewcone.myscorecard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    public TeamViewAdapter(List<Team> teamList, ListInteractionListener listener) {
        this.teamList = teamList;
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  final ViewHolder holder, int position) {

        holder.team = teamList.get(position);
        holder.tvTeamName.setText(holder.team.getName());
        holder.tvTeamShortName.setText(holder.team.getShortName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.team);
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
