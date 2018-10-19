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
import com.thenewcone.myscorecard.player.BatsmanStats;

import java.util.List;

public class BatsmanListAdapter extends RecyclerView.Adapter<BatsmanListAdapter.MyViewHolder> {

	private List<BatsmanStats> batsmen;
	private Context context;
	private ItemClickListener clickListener;
	private int selectedIndex = -1, newBatsmanPosn = -1, defaultSelectedIx;

	public BatsmanListAdapter(@NonNull Context context, @NonNull List<BatsmanStats> batsmen, int position, int defaultSelIx) {
		this.context = context;
		this.batsmen = batsmen;
		this.newBatsmanPosn = position;
		this.defaultSelectedIx = defaultSelIx;
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
            tvBatsmanName.setText(batsman.getBatsmanName());

             if(newBatsmanPosn > -1) {
                 tvBattingStyle.setText(batsman.getPlayer().getBattingStyle().toString());
             }

            if (newBatsmanPosn > batsman.getPosition()) {
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
