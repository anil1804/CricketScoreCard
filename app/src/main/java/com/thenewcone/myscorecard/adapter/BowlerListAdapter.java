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
import com.thenewcone.myscorecard.intf.ItemClickListener;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.List;

public class BowlerListAdapter extends RecyclerView.Adapter<BowlerListAdapter.MyViewHolder> {

	private List<BowlerStats> bowlers;
	private Context context;
	private ItemClickListener clickListener;
	private int selectedIndex = -1;
    private int maxPerBowler;
    private BowlerStats prevBowler, nextBowler;

	public BowlerListAdapter(@NonNull Context context, @NonNull List<BowlerStats> bowlers, int maxPerBowler, BowlerStats prevBowler, BowlerStats nextBowler) {
		this.context = context;
		this.bowlers = bowlers;
		this.maxPerBowler = maxPerBowler;
		this.prevBowler = prevBowler;
		this.nextBowler = nextBowler;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View playerView = LayoutInflater.from(context).inflate(R.layout.activity_bowler_list_item, parent, false);

		return new MyViewHolder(playerView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        BowlerStats bowler = bowlers.get(position);
		myViewHolder.setData(bowler);

		if(selectedIndex == position)
			myViewHolder.llBowlerItem.setSelected(true);
		else
			myViewHolder.llBowlerItem.setSelected(false);
	}

	@Override
	public int getItemCount() {
		return bowlers.size();
	}

	public void setClickListener(ItemClickListener itemClickListener) {
		this.clickListener = itemClickListener;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		TextView tvBowlerName, tvBowlingStyle, tvOversLeft;
		LinearLayout llBowlerItem;

		private MyViewHolder(@NonNull View itemView) {
			super(itemView);

            tvBowlerName = itemView.findViewById(R.id.tvBowlerName);
            tvBowlingStyle = itemView.findViewById(R.id.tvBowlingStyle);
            tvOversLeft = itemView.findViewById(R.id.tvOversLeft);
            llBowlerItem = itemView.findViewById(R.id.llBowlerItem);
            llBowlerItem.setEnabled(true);
            itemView.setOnClickListener(this);
		}

		public void setData(BowlerStats bowler) {
            tvBowlerName.setText(bowler.getBowlerName());
            tvBowlingStyle.setText(bowler.getPlayer().getBowlingStyle().toString());

            int ballsBowled = CommonUtils.oversToBalls(Double.parseDouble(bowler.getOversBowled()));
            int maxAllowed = CommonUtils.oversToBalls((double) maxPerBowler);
            int remBalls = maxAllowed - ballsBowled;

            if(remBalls > 6) {
                tvOversLeft.setText(String.valueOf(CommonUtils.ballsToOvers(remBalls)));
            } else {
                tvOversLeft.setText("0");
                llBowlerItem.setEnabled(false);
            }

            if(bowler.getBowlerName().equals(prevBowler.getBowlerName() ))
                llBowlerItem.setEnabled(false);

            if(selectedIndex < 0 && bowler.equals(nextBowler)) {
                selectedIndex = getAdapterPosition();
                llBowlerItem.setSelected(true);
                if(clickListener != null)
                    clickListener.onItemClick(llBowlerItem, selectedIndex);
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
