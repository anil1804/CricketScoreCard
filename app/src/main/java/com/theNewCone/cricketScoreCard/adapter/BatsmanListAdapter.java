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
import com.theNewCone.cricketScoreCard.intf.ItemClickListener;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.scorecard.WicketData;

import java.util.List;

public class BatsmanListAdapter extends RecyclerView.Adapter<BatsmanListAdapter.MyViewHolder> {

	private List<BatsmanStats> batsmen;
	private Context context;
	private ItemClickListener clickListener;
	private int selectedIndex = -1, newBatsmanPosition, defaultSelectedIx, wkPlayerID, captainPlayerID;

	public BatsmanListAdapter(@NonNull Context context, @NonNull List<BatsmanStats> batsmen, int position, int defaultSelIx, int captainPlayerID, int wkPlayerID) {
		this.context = context;
		this.batsmen = batsmen;
		this.newBatsmanPosition = position;
		this.defaultSelectedIx = defaultSelIx;
		this.captainPlayerID = captainPlayerID;
		this.wkPlayerID = wkPlayerID;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View playerView = LayoutInflater.from(context).inflate(R.layout.activity_batsman_list_item, parent, false);

		return new MyViewHolder(playerView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        BatsmanStats batsman = batsmen.get(position);
		myViewHolder.setData(batsman);

        if (selectedIndex == position)
            myViewHolder.llBatsmanItem.setSelected(true);
        else
            myViewHolder.llBatsmanItem.setSelected(false);
	}

	@Override
	public int getItemCount() {
		return batsmen.size();
	}

	public void setClickListener(ItemClickListener itemClickListener) {
		this.clickListener = itemClickListener;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		TextView tvBatsmanName, tvBattingStyle;
		LinearLayout llBatsmanItem;

        private MyViewHolder(@NonNull View itemView) {
			super(itemView);

            tvBatsmanName = itemView.findViewById(R.id.tvBatsmanName);
            tvBattingStyle = itemView.findViewById(R.id.tvBattingStyle);
			llBatsmanItem = itemView.findViewById(R.id.llBatsmanItem);
			llBatsmanItem.setEnabled(true);
            itemView.setOnClickListener(this);
		}

		public void setData(BatsmanStats batsman) {
        	int batsmanPlayerID = batsman.getPlayer().getID();
        	String batText = batsman.getBatsmanName() +(batsmanPlayerID == wkPlayerID ? " (w)" : (batsmanPlayerID == captainPlayerID ? " (c)" : ""));
            tvBatsmanName.setText(batText);

             if(newBatsmanPosition > -1) {
                 tvBattingStyle.setText(batsman.getPlayer().getBattingStyle().toString());
             }

            //Disabling batsman who are out or currently playing
            if (batsman.getPosition() < newBatsmanPosition && batsman.getDismissalType() != WicketData.DismissalType.RETIRED_HURT) {
                llBatsmanItem.setEnabled(false);
            }

            if(selectedIndex < 0 && getAdapterPosition() == defaultSelectedIx) {
                 selectedIndex = defaultSelectedIx;
                 llBatsmanItem.setSelected(true);
                if(clickListener != null)
                    clickListener.onItemClick(llBatsmanItem, selectedIndex);
            }
		}


		@Override
		public void onClick(View view)
		{
		    if(selectedIndex > -1)
                notifyItemChanged(selectedIndex);

			selectedIndex = getAdapterPosition();
			notifyItemChanged(selectedIndex);

			if(clickListener != null)
			    clickListener.onItemClick(view, selectedIndex);
		}
	}
}
