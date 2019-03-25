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
import com.theNewCone.cricketScoreCard.enumeration.StatisticsType;
import com.theNewCone.cricketScoreCard.statistics.BatsmanData;
import com.theNewCone.cricketScoreCard.statistics.BowlerData;
import com.theNewCone.cricketScoreCard.statistics.PlayerData;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.List;

public class TournamentPlayerStatsAdapter extends RecyclerView.Adapter<TournamentPlayerStatsAdapter.MyViewHolder> {
	private final List dataList;
	private final StatisticsType statisticsType;
	private final Context context;

	public TournamentPlayerStatsAdapter(Context context, List dataList, StatisticsType statisticsType) {
		this.context = context;
		this.statisticsType = statisticsType;
		this.dataList = dataList;
	}

	@NonNull
	@Override
	public TournamentPlayerStatsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.view_stats_player, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
		Object dataItem = (i == 0) ? null : dataList.get(i - 1);

		switch (statisticsType) {
			case HIGHEST_SCORE:
			case TOTAL_RUNS:
			case HUNDREDS_FIFTIES:
				updateBatsmanStatistics(dataItem, holder);
				break;

			case BOWLING_BEST_FIGURES:
			case ECONOMY:
			case TOTAL_WICKETS:
				updateBowlerStatistics(dataItem, holder);
				break;

			case CATCHES:
			case STUMPING:
				updateFielderStatistics(dataItem, holder);
				break;
		}
	}

	@Override
	public int getItemCount() {
		return dataList.size() < 10 ? dataList.size() + 1 : 11;
	}

	private void updateBatsmanStatistics(Object dataItem, MyViewHolder holder) {
		if (dataItem == null) {
			holder.llTPStats.setBackgroundColor(context.getResources().getColor(R.color.gray_300));

			holder.tvPlayerName.setText(R.string.player);
			holder.tvInnings.setText(R.string.stats_hdr_innings);

			switch (statisticsType) {
				case HIGHEST_SCORE:
					holder.tvStats1.setText(R.string.stats_hdr_highScore);
					holder.tvStats2.setText(R.string.headerStrikeRate);
					break;

				case TOTAL_RUNS:
					holder.tvStats1.setText(R.string.stats_hdr_total);
					holder.tvStats2.setText(R.string.stats_hdr_average);
					break;

				case HUNDREDS_FIFTIES:
					holder.tvStats1.setText(R.string.stats_hdr_hundreds);
					holder.tvStats2.setText(R.string.stats_hdr_fifties);
					break;
			}
		} else if (dataItem instanceof BatsmanData) {
			BatsmanData batsmanData = (BatsmanData) dataItem;

			holder.tvPlayerName.setText(batsmanData.getPlayer().getName());
			holder.tvInnings.setText(String.valueOf(batsmanData.getTotalInnings()));

			switch (statisticsType) {
				case HIGHEST_SCORE:
					holder.tvStats1.setText(String.valueOf(batsmanData.getHighestScore()));
					String strikeRate = batsmanData.getStrikeRate() > 0
							? CommonUtils.doubleToString(batsmanData.getStrikeRate(), null)
							: "-";
					holder.tvStats2.setText(strikeRate);
					break;

				case TOTAL_RUNS:
					holder.tvStats1.setText(String.valueOf(batsmanData.getRunsScored()));
					String average = batsmanData.getAverage() > 0
							? CommonUtils.doubleToString(batsmanData.getAverage(), null)
							: "-";
					holder.tvStats2.setText(average);
					break;

				case HUNDREDS_FIFTIES:
					holder.tvStats1.setText(String.valueOf(batsmanData.getHundreds()));
					holder.tvStats2.setText(String.valueOf(batsmanData.getFifties()));
					break;
			}
		}
	}

	private void updateBowlerStatistics(Object dataItem, MyViewHolder holder) {
		if (dataItem == null) {
			holder.llTPStats.setBackgroundColor(context.getResources().getColor(R.color.gray_300));

			holder.tvPlayerName.setText(R.string.player);
			holder.tvInnings.setText(R.string.stats_hdr_innings);

			switch (statisticsType) {
				case BOWLING_BEST_FIGURES:
					holder.tvStats1.setText(R.string.stats_hdr_bestBowlingFigures);
					holder.tvStats2.setText(R.string.stats_hdr_average);
					break;

				case ECONOMY:
					holder.tvStats1.setText(R.string.headerEconomy);
					holder.tvStats2.setText(R.string.stats_hdr_overs);
					break;

				case TOTAL_WICKETS:
					holder.tvStats1.setText(R.string.headerWickets);
					holder.tvStats2.setText(R.string.headerStrikeRate);
					break;
			}
		} else if (dataItem instanceof BowlerData) {
			BowlerData bowlerData = (BowlerData) dataItem;

			holder.tvPlayerName.setText(bowlerData.getPlayer().getName());
			holder.tvInnings.setText(String.valueOf(bowlerData.getTotalInnings()));

			switch (statisticsType) {
				case BOWLING_BEST_FIGURES:
					holder.tvStats1.setText(bowlerData.getBestFigures());
					String average = bowlerData.getAverage() > 0
							? CommonUtils.doubleToString(bowlerData.getAverage(), null)
							: "-";
					holder.tvStats2.setText(average);
					break;

				case ECONOMY:
					String economy = bowlerData.getEconomy() > 0
							? CommonUtils.doubleToString(bowlerData.getEconomy(), null)
							: "-";
					holder.tvStats1.setText(economy);
					holder.tvStats2.setText(String.valueOf(bowlerData.getOversBowled()));
					break;

				case TOTAL_WICKETS:
					holder.tvStats1.setText(String.valueOf(bowlerData.getWicketsTaken()));
					String strikeRate = bowlerData.getStrikeRate() > 0
							? CommonUtils.doubleToString(bowlerData.getStrikeRate(), null)
							: "-";
					holder.tvStats2.setText(strikeRate);
					break;
			}
		}
	}

	private void updateFielderStatistics(Object dataItem, MyViewHolder holder) {
		if (dataItem == null) {
			holder.llTPStats.setBackgroundColor(context.getResources().getColor(R.color.gray_300));

			holder.tvPlayerName.setText(R.string.player);
			holder.tvInnings.setText(R.string.stats_hdr_innings);

			switch (statisticsType) {
				case CATCHES:
					holder.tvStats1.setText(R.string.stats_hdr_catches);
					holder.tvStats2.setText(R.string.stats_hdr_runOuts);
					break;

				case STUMPING:
					holder.tvStats1.setText(R.string.stats_hdr_stumping);
					holder.tvStats2.setText(R.string.stats_hdr_runOuts);
					break;
			}
		} else if (dataItem instanceof PlayerData) {
			PlayerData playerData = (PlayerData) dataItem;

			holder.tvPlayerName.setText(playerData.getPlayer().getName());
			holder.tvInnings.setText(String.valueOf(playerData.getTotalInnings()));

			String runOuts = playerData.getRunOuts() > 0 ? String.valueOf(playerData.getRunOuts()) : "-";
			switch (statisticsType) {
				case CATCHES:
					String catches = playerData.getCatches() > 0 ? String.valueOf(playerData.getCatches()) : "-";
					holder.tvStats1.setText(catches);
					holder.tvStats2.setText(runOuts);
					break;

				case STUMPING:
					String stumpOuts = playerData.getStumps() > 0 ? String.valueOf(playerData.getStumps()) : "-";
					holder.tvStats1.setText(stumpOuts);
					holder.tvStats2.setText(runOuts);
					break;
			}
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tvPlayerName, tvInnings, tvStats1, tvStats2;
		LinearLayout llTPStats;

		private MyViewHolder(@NonNull View itemView) {
			super(itemView);

			llTPStats = itemView.findViewById(R.id.llTPStats);
			tvPlayerName = itemView.findViewById(R.id.tvTPStatsPlayerName);
			tvInnings = itemView.findViewById(R.id.tvTPStatsPlayerInnings);
			tvStats1 = itemView.findViewById(R.id.tvTPStatsPlayerStat1);
			tvStats2 = itemView.findViewById(R.id.tvTPStatsPlayerStat2);
		}
	}
}
