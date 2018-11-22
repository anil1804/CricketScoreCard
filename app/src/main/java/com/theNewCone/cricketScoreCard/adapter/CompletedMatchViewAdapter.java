package com.theNewCone.cricketScoreCard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.match.Match;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.List;

public class CompletedMatchViewAdapter extends RecyclerView.Adapter<CompletedMatchViewAdapter.ViewHolder> {

    private final List<Match> savedMatchList;
    private final ListInteractionListener mListener;

    public CompletedMatchViewAdapter(List<Match> savedMatchList, ListInteractionListener listener) {
        this.savedMatchList = savedMatchList;
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_saved_match_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  final ViewHolder holder, final int position) {
        holder.match = savedMatchList.get(position);

        String matchVersus = holder.match.getTeam1ShortName() + " v " +
				holder.match.getTeam2ShortName();

        holder.tvSavedName.setVisibility(View.GONE);
        holder.tvMatchName.setText(holder.match.getName());
        holder.tvTeamVersus.setText(matchVersus);
        holder.tvMatchDate.setText(holder.match.getDate() != null ?
                CommonUtils.dateToString(holder.match.getDate(), "MMM dd, yyyy") : "");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                	mListener.onListFragmentInteraction(holder.match);
				}
			}
		});
    }

    @Override
    public int getItemCount() {
        return savedMatchList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Match match;
        final View mView;
        final TextView tvSavedName, tvMatchName, tvTeamVersus, tvMatchDate;

        ViewHolder(View view) {
            super(view);
            mView = view;
            tvSavedName = view.findViewById(R.id.tvSaveName);
            tvMatchName = view.findViewById(R.id.tvMatchName);
            tvTeamVersus = view.findViewById(R.id.tvTeamVersus);
            tvMatchDate = view.findViewById(R.id.tvMatchDate);
        }
    }
}
