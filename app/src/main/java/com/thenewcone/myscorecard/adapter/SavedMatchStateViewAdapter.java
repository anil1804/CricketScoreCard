package com.thenewcone.myscorecard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.ListInteractionListener;
import com.thenewcone.myscorecard.match.Match;
import com.thenewcone.myscorecard.match.MatchState;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.List;

public class SavedMatchStateViewAdapter extends RecyclerView.Adapter<SavedMatchStateViewAdapter.ViewHolder> {

    private final List<MatchState> savedMatchList;
    private final ListInteractionListener mListener;
    private final boolean isMultiSelect;

	private SparseBooleanArray selMatchStateIDs;

    public SavedMatchStateViewAdapter(List<MatchState> savedMatchList, ListInteractionListener listener, boolean isMultiSelect) {
        this.savedMatchList = savedMatchList;
        mListener = listener;
        this.isMultiSelect = isMultiSelect;

		selMatchStateIDs = new SparseBooleanArray();
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
        holder.matchState = savedMatchList.get(position);

        String matchVersus = holder.matchState.getMatch().getTeam1ShortName() + " v " +
				holder.matchState.getMatch().getTeam2ShortName();

		Match match = holder.matchState.getMatch();

        holder.tvSavedName.setText(holder.matchState.getSavedName());
        holder.tvMatchName.setText(match.getName());
        holder.tvTeamVersus.setText(matchVersus);
        holder.tvMatchDate.setText(match.getDate() != null ? CommonUtils.dateToString(match.getDate()) : "");

		if (selMatchStateIDs.get(holder.matchState.getId())) {
			holder.mView.setSelected(true);
		} else {
			holder.mView.setSelected(false);
		}

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
						if(!isMultiSelect) {
							mListener.onListFragmentInteraction(holder.matchState);
						} else {

							boolean isPresent = selMatchStateIDs.get(holder.matchState.getId());
							mListener.onListFragmentMultiSelect(holder.matchState, isPresent);
							selMatchStateIDs.put(holder.matchState.getId(), !isPresent);
							notifyItemChanged(position);
						}
					}
				}
	        });
    }

    @Override
    public int getItemCount() {
        return savedMatchList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MatchState matchState;
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
