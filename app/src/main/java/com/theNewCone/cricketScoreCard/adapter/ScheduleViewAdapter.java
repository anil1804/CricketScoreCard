package com.theNewCone.cricketScoreCard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.intf.ItemClickListener;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import java.util.List;

public class ScheduleViewAdapter extends RecyclerView.Adapter<ScheduleViewAdapter.ViewHolder> {

	private final List<MatchInfo> matchInfoList;
	private final boolean isSetup;
	private final Context context;
	private ItemClickListener listener;

	public ScheduleViewAdapter(Context context, List<MatchInfo> matchInfoList, boolean isSetup) {
		this.context = context;
		this.matchInfoList = matchInfoList;
		this.isSetup = isSetup;
	}

	public void setItemClickListener(ItemClickListener listener) {
		this.listener = listener;
	}

	@Override
	@NonNull
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		int layoutId = isSetup ? R.layout.tournament_schedule_item_view : R.layout.tournament_tab_schedule_item_view;

		View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		final int adapterPosition = holder.getAdapterPosition();
		holder.matchInfo = matchInfoList.get(adapterPosition);

		String versusText = holder.matchInfo.getTeam1().getShortName() + " vs " + holder.matchInfo.getTeam2().getShortName();
		holder.tvVersus.setText(versusText);

		if (!isSetup) {
			int resultColor;
			holder.tvGroupName.setText(holder.matchInfo.getGroupName());

			final String matchResult;

			if (holder.matchInfo.hasStarted()) {
				holder.btnMatchStart.setVisibility(View.GONE);
				holder.btnMatchOpen.setVisibility(View.VISIBLE);
				holder.tvMatchDate.setVisibility(View.VISIBLE);

				DatabaseHandler dbh = new DatabaseHandler(context);
				if (holder.matchInfo.isComplete()) {
					CricketCardUtils matchData = dbh.getCompletedMatch(holder.matchInfo.getMatchID());
					matchResult = matchData.getResult();

					if (matchData.isAbandoned())
						resultColor = context.getResources().getColor(R.color.red_900);
					else if (matchData.isMatchTied())
						resultColor = context.getResources().getColor(R.color.orange_600);
					else
						resultColor = context.getResources().getColor(R.color.green_700);

				} else {
					matchResult = "In Progress";
					resultColor = context.getResources().getColor(R.color.blue_700);
				}

				String dateText = "[" + holder.matchInfo.getMatchDate() + "]";
				holder.tvMatchDate.setText(dateText);
			} else {
				holder.btnMatchStart.setVisibility(View.VISIBLE);
				holder.btnMatchOpen.setVisibility(View.GONE);
				holder.tvMatchDate.setVisibility(View.GONE);

				matchResult = "Yet to Start";
				resultColor = context.getResources().getColor(R.color.brown_500);
			}

			holder.tvResult.setText(matchResult);
			holder.tvResult.setTextColor(resultColor);

			holder.btnMatchStart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					listener.onItemClick(view, adapterPosition);
				}
			});

			holder.btnMatchOpen.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					listener.onItemClick(view, adapterPosition);
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return matchInfoList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		final View mView;
		final TextView tvVersus, tvGroupName, tvResult, tvMatchDate;
		final Button btnMatchStart, btnMatchOpen;
		MatchInfo matchInfo;

		ViewHolder(View view) {
			super(view);
			mView = view;

			tvVersus = view.findViewById(R.id.tvScheduleVersus);
			tvGroupName = view.findViewById(R.id.tvScheduleGroupName);
			tvResult = view.findViewById(R.id.tvScheduleMatchResult);
			tvMatchDate = view.findViewById(R.id.tvScheduleMatchDate);

			btnMatchStart = view.findViewById(R.id.btnScheduleMatchStart);
			btnMatchOpen = view.findViewById(R.id.btnScheduleMatchOpen);
		}
	}
}
