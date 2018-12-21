package com.theNewCone.cricketScoreCard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.tournament.PointsData;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.List;

public class PointsDataViewAdapter extends RecyclerView.Adapter<PointsDataViewAdapter.ViewHolder> {

	private final List<PointsData> pointsDataList;
	private final Context context;

	PointsDataViewAdapter(Context context, List<PointsData> pointsDataList) {
		this.pointsDataList = pointsDataList;
		this.context = context;
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).
				inflate(R.layout.view_tournament_home_group_points_item_team, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		final int adapterPosition = holder.getAdapterPosition();
		if (adapterPosition > 0) {
			holder.pointsData = pointsDataList.get(adapterPosition - 1);

			holder.tvPTTeamName.setText(holder.pointsData.getTeam().getShortName());
			holder.tvPTPlayed.setText(String.valueOf(holder.pointsData.getPlayed()));
			holder.tvPTWon.setText(String.valueOf(holder.pointsData.getWon()));
			holder.tvPTLost.setText(String.valueOf(holder.pointsData.getLost()));
			holder.tvPTTied.setText(String.valueOf(holder.pointsData.getTied()));
			holder.tvPTNoResult.setText(String.valueOf(holder.pointsData.getNoResult()));
			holder.tvPTPoints.setText(String.valueOf(holder.pointsData.getPoints()));
			holder.tvPTNetRunRate.setText(CommonUtils.doubleToString(holder.pointsData.getNetRunRate(), "#.###"));
		} else {
			holder.llPointsData.setBackgroundColor(context.getResources().getColor(R.color.gray_300));
			holder.tvPTTeamName.setText("Team");
			holder.tvPTPlayed.setText("P");
			holder.tvPTWon.setText("W");
			holder.tvPTLost.setText("L");
			holder.tvPTTied.setText("T");
			holder.tvPTNoResult.setText("NR");
			holder.tvPTPoints.setText("Pts");
			holder.tvPTNetRunRate.setText("NRR");
		}
	}

	@Override
	public int getItemCount() {
		return pointsDataList.size() + 1;
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		final View mView;
		final TextView tvPTTeamName, tvPTPlayed, tvPTWon, tvPTLost, tvPTTied, tvPTNoResult, tvPTPoints, tvPTNetRunRate;
		final LinearLayout llPointsData;
		PointsData pointsData;

		ViewHolder(View view) {
			super(view);
			mView = view;

			llPointsData = view.findViewById(R.id.llPointsData);

			tvPTTeamName = view.findViewById(R.id.tvPTTeamName);
			tvPTPlayed = view.findViewById(R.id.tvPTPlayed);
			tvPTWon = view.findViewById(R.id.tvPTWon);
			tvPTLost = view.findViewById(R.id.tvPTLost);
			tvPTTied = view.findViewById(R.id.tvPTTied);
			tvPTNoResult = view.findViewById(R.id.tvPTNoResult);
			tvPTPoints = view.findViewById(R.id.tvPTPoints);
			tvPTNetRunRate = view.findViewById(R.id.tvPTNetRunRate);
		}
	}
}
