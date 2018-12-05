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
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.List;

public class TournamentViewAdapter extends RecyclerView.Adapter<TournamentViewAdapter.ViewHolder> {

	private final List<Tournament> tournamentList;
	private final ListInteractionListener mListener;
	private final boolean isMultiSelect;

	private SparseBooleanArray selMatchIDs;

	public TournamentViewAdapter(List<Tournament> tournamentList, ListInteractionListener listener, boolean isMultiSelect) {
		this.tournamentList = tournamentList;
		mListener = listener;
		this.isMultiSelect = isMultiSelect;

		selMatchIDs = new SparseBooleanArray();
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.activity_tournament_item_view, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		final int adapterPosition = holder.getAdapterPosition();
		holder.tournament = tournamentList.get(adapterPosition);

		String createdDateText = CommonUtils.dateToString(CommonUtils.stringToDate(holder.tournament.getCreatedDate()), "yyyy.MMMdd HH:mm");

		holder.tvName.setText(holder.tournament.getName());
		holder.tvTeamSize.setText(String.valueOf(holder.tournament.getTeamSize()));
		holder.tvFormat.setText(holder.tournament.getFormat().toString());
		holder.tvCreatedDate.setText(createdDateText);

		if (selMatchIDs.get(holder.tournament.getId())) {
			holder.mView.setSelected(true);
		} else {
			holder.mView.setSelected(false);
		}

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener) {
					if (!isMultiSelect) {
						mListener.onListFragmentInteraction(holder.tournament);
					} else {

						boolean isPresent = selMatchIDs.get(holder.tournament.getId());
						mListener.onListFragmentMultiSelect(holder.tournament, isPresent);
						selMatchIDs.put(holder.tournament.getId(), !isPresent);
						notifyItemChanged(adapterPosition);
					}
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return tournamentList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		final View mView;
		final TextView tvName, tvTeamSize, tvFormat, tvCreatedDate;
		Tournament tournament;

		ViewHolder(View view) {
			super(view);
			mView = view;
			tvName = view.findViewById(R.id.tvTournamentName);
			tvCreatedDate = view.findViewById(R.id.tvTournamentCreatedDate);
			tvTeamSize = view.findViewById(R.id.tvTournamentTeamSize);
			tvFormat = view.findViewById(R.id.tvTournamentFormat);
		}
	}
}
