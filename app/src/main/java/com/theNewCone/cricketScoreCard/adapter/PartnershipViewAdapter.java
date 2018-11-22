package com.theNewCone.cricketScoreCard.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.match.Partnership;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.List;

public class PartnershipViewAdapter extends RecyclerView.Adapter<PartnershipViewAdapter.MyViewHolder> {
	private Context context;
	private final Activity activity;
	private int maxScoreInPartnership = 1;
	private List<Partnership> partnershipData;

	public PartnershipViewAdapter(@NonNull Context context, @NonNull List<Partnership> partnershipData, @NonNull Activity activity) {
		this.context = context;
		this.activity = activity;
		this.partnershipData = partnershipData;

		for(Partnership partnership : partnershipData) {
			int extras = partnership.getPartnership() - partnership.getP1RunsScored() - partnership.getP2RunsScored();
			maxScoreInPartnership = partnership.getP1RunsScored() > maxScoreInPartnership ? partnership.getP1RunsScored() : maxScoreInPartnership;
			maxScoreInPartnership = partnership.getP2RunsScored() > maxScoreInPartnership ? partnership.getP2RunsScored() : maxScoreInPartnership;
			maxScoreInPartnership = extras > maxScoreInPartnership ? extras : maxScoreInPartnership;
		}
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View partnershipView = 	LayoutInflater.from(context).inflate(R.layout.view_partnership_item, viewGroup, false);

		return new MyViewHolder(partnershipView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
		Partnership partnership = partnershipData.get(position);
		if(partnership != null) {
			myViewHolder.setData(partnership, activity, maxScoreInPartnership);
		}
	}

	@Override
	public int getItemCount() {
		return partnershipData.size();
	}

	static class MyViewHolder extends RecyclerView.ViewHolder{
		TextView tvLeftBatsmanName, tvRightBatsmanName, tvLeftBatsmanScore, tvRightBatsmanScore, tvTotalScore, tvExtras;
		ImageView ivLeftBatsmanScore, ivRightBatsmanScore, ivExtras;

		MyViewHolder(@NonNull View itemView) {
			super(itemView);
			loadHolderViews(itemView);
		}

		private void loadHolderViews(@NonNull View convertView) {
			tvLeftBatsmanName = convertView.findViewById(R.id.tvPGBatsmanLeftName);
			tvRightBatsmanName = convertView.findViewById(R.id.tvPGBatsmanRightName);
			tvLeftBatsmanScore = convertView.findViewById(R.id.tvPGBatsmanLeftScore);
			tvRightBatsmanScore = convertView.findViewById(R.id.tvPGBatsmanRightScore);

			tvTotalScore = convertView.findViewById(R.id.tvPGTotal);
			tvExtras = convertView.findViewById(R.id.tvPGExtras);

			ivLeftBatsmanScore = convertView.findViewById(R.id.ivPGBatsmanLeftScore);
			ivRightBatsmanScore = convertView.findViewById(R.id.ivPGBatsmanRightScore);
			ivExtras = convertView.findViewById(R.id.ivPGExtras);
		}

		private void setData(Partnership partnership, Activity activity, int limit) {
			tvLeftBatsmanName.setText(partnership.getPlayer1().getName());
			tvRightBatsmanName.setText(partnership.getPlayer2().getName());

			String leftBatsmanScore = partnership.getP1RunsScored() + "(" + partnership.getP1BallsPlayed() + ")";
			String rightBatsmanScore = partnership.getP2RunsScored() + "(" + partnership.getP2BallsPlayed() + ")";
			tvLeftBatsmanScore.setText(leftBatsmanScore);
			tvRightBatsmanScore.setText(rightBatsmanScore);

			String totalScore = "Total: " +(partnership.getPartnership()) + "(" + partnership.getTotalBallsPlayed() + ")";
			tvTotalScore.setText(totalScore);

			String extras = "Extras: " + partnership.getExtras();
			tvExtras.setText(extras);

			double multiplier = CommonUtils.getScreenWidth(activity)/(2.25 * limit);
			layoutView(ivLeftBatsmanScore, (int) (partnership.getP1RunsScored() * multiplier));
			layoutView(ivRightBatsmanScore, (int) (partnership.getP2RunsScored() * multiplier));
			layoutView(ivExtras, (int) (partnership.getExtras() * multiplier));
		}

		private void layoutView(View theView, int width) {
			theView.getLayoutParams().width = width;
			theView.getLayoutParams().height = 40;
			theView.requestLayout();
		}
	}
}
