package com.thenewcone.myscorecard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.List;

public class SCBatsmanAdapter extends RecyclerView.Adapter<SCBatsmanAdapter.MyViewHolder> {

	private List<BatsmanStats> batsmen;
	private Context context;
	private BatsmanStats currentFacing, otherBatsman;

	public SCBatsmanAdapter(@NonNull Context context, @NonNull List<BatsmanStats> batsmen, BatsmanStats currentFacing, BatsmanStats otherBatsman) {
		this.context = context;
		this.batsmen = batsmen;
		this.currentFacing = currentFacing;
		this.otherBatsman = otherBatsman;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View playerView = LayoutInflater.from(context).inflate(R.layout.scorecard_batsman_view, parent, false);

		return new MyViewHolder(playerView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		BatsmanStats batsman = null;

		if(position > 0) {
			batsman = batsmen.get(position - 1);

			batsman = (currentFacing != null && currentFacing.getPlayer().getID() == batsman.getPlayer().getID())
					? currentFacing
					: ((otherBatsman != null && otherBatsman.getPlayer().getID() == batsman.getPlayer().getID())
						? otherBatsman
						: batsman);
		} else {
			holder.llSCBatsmanView.setSelected(true);
		}

		String batsmanName = (position > 0) ? batsman.getBatsmanName() : "Name";
		String batsmanRuns = (position > 0) ? String.valueOf(batsman.getRunsScored()) : "R";
		String batsmanBalls = (position > 0) ? String.valueOf(batsman.getBallsPlayed()) : "B";
		String batsmanFours = (position > 0) ? String.valueOf(batsman.getNum4s()) : "4s";
		String batsmanSixes = (position > 0) ? String.valueOf(batsman.getNum6s()) : "6s";
		String batsmanSR = (position > 0) ? CommonUtils.doubleToString(batsman.getStrikeRate(), "#.#") : "SR";

		holder.batsman = batsman;

		if(position > 0) {
			holder.tvSCBatsmanOutDetails.setVisibility(View.VISIBLE);
			holder.tvSCBatsmanOutDetails.setText(CommonUtils.getBatsmanOutData(batsman));
		} else {
			holder.tvSCBatsmanOutDetails.setVisibility(View.GONE);
		}

		holder.tvSCBatsmanName.setText(batsmanName);
		holder.tvSCBatsmanRuns.setText(batsmanRuns);
		holder.tvSCBatsmanBalls.setText(batsmanBalls);
		holder.tvSCBatsmanFours.setText(batsmanFours);
		holder.tvSCBatsmanSixes.setText(batsmanSixes);
		holder.tvSCBatsmanSR.setText(batsmanSR);
	}

	@Override
	public int getItemCount() {
		return batsmen.size() + 1;
	}

	class MyViewHolder extends RecyclerView.ViewHolder{
		TextView tvSCBatsmanName, tvSCBatsmanOutDetails;
		TextView tvSCBatsmanRuns, tvSCBatsmanBalls, tvSCBatsmanFours, tvSCBatsmanSixes, tvSCBatsmanSR;
		BatsmanStats batsman;
		LinearLayout llSCBatsmanView;

        private MyViewHolder(@NonNull View itemView) {
			super(itemView);

			llSCBatsmanView = itemView.findViewById(R.id.llSCBatsmanView);
			tvSCBatsmanName = itemView.findViewById(R.id.tvSCBatsmanName);
			tvSCBatsmanOutDetails = itemView.findViewById(R.id.tvSCBatsmanOutDetails);
			tvSCBatsmanRuns = itemView.findViewById(R.id.tvSCBatsmanRuns);
			tvSCBatsmanBalls = itemView.findViewById(R.id.tvSCBatsmanBalls);
			tvSCBatsmanFours = itemView.findViewById(R.id.tvSCBatsmanFours);
			tvSCBatsmanSixes = itemView.findViewById(R.id.tvSCBatsmanSixes);
			tvSCBatsmanSR = itemView.findViewById(R.id.tvSCBatsmanSR);
		}
	}
}
