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
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.List;

public class SCBowlerAdapter extends RecyclerView.Adapter<SCBowlerAdapter.MyViewHolder> {

	private List<BowlerStats> bowlerList;
	private Context context;
	private BowlerStats bowler;

	public SCBowlerAdapter(@NonNull Context context, @NonNull List<BowlerStats> bowlerList, BowlerStats bowler) {
		this.context = context;
		this.bowlerList = bowlerList;
		this.bowler = bowler;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View playerView = LayoutInflater.from(context).inflate(R.layout.view_scorecard_bowler, parent, false);

		return new MyViewHolder(playerView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		BowlerStats currBowler = null;

		if(position > 0) {
			currBowler = bowlerList.get(position - 1);

			currBowler = (bowler != null && bowler.getPlayer().getID() == currBowler.getPlayer().getID())
					? bowler : currBowler;
		} else {
			holder.llSCBowlerView.setSelected(true);
		}

		String bowlerName = (position > 0) ? currBowler.getBowlerName() : "Name";
		String bowlerOvers = (position > 0) ? String.valueOf(currBowler.getOversBowled()) : "O";
		String bowlerMaidens = (position > 0) ? String.valueOf(currBowler.getMaidens()) : "M";
		String bowlerRuns = (position > 0) ? String.valueOf(currBowler.getRunsGiven()) : "R";
		String bowlerWickets = (position > 0) ? String.valueOf(currBowler.getWickets()) : "W";
		String bowlerER = (position > 0) ? CommonUtils.doubleToString(currBowler.getEconomy(), "#.##") : "ER";

		holder.bowler = currBowler;

		holder.tvSCBowlerName.setText(bowlerName);
		holder.tvSCBowlerOvers.setText(bowlerOvers);
		holder.tvSCBowlerMaidens.setText(bowlerMaidens);
		holder.tvSCBowlerRuns.setText(bowlerRuns);
		holder.tvSCBowlerWickets.setText(bowlerWickets);
		holder.tvSCBowlerER.setText(bowlerER);
	}

	@Override
	public int getItemCount() {
		return bowlerList.size() + 1;
	}

	class MyViewHolder extends RecyclerView.ViewHolder{
		TextView tvSCBowlerName;
		TextView tvSCBowlerOvers, tvSCBowlerMaidens, tvSCBowlerRuns, tvSCBowlerWickets, tvSCBowlerER;
		BowlerStats bowler;
		LinearLayout llSCBowlerView;

        private MyViewHolder(@NonNull View itemView) {
			super(itemView);

			llSCBowlerView = itemView.findViewById(R.id.llSCBowlerView);
			tvSCBowlerName = itemView.findViewById(R.id.tvSCBowlerName);
			tvSCBowlerOvers = itemView.findViewById(R.id.tvSCBowlerOvers);
			tvSCBowlerMaidens = itemView.findViewById(R.id.tvSCBowlerMaidens);
			tvSCBowlerRuns = itemView.findViewById(R.id.tvSCBowlerRuns);
			tvSCBowlerWickets = itemView.findViewById(R.id.tvSCBowlerWickets);
			tvSCBowlerER = itemView.findViewById(R.id.tvSCBowlerER);
		}
	}
}
