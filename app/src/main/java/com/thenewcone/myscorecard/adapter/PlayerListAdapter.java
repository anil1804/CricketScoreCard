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
import com.thenewcone.myscorecard.player.Player;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder> {

	private Player[] players;
	private Context context;
	private ItemClickListener clickListener;
	private int selectedIndex = 0;

	public PlayerListAdapter(@NonNull Context context, @NonNull Player[] players) {
		this.context = context;
		this.players = players;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View playerView = LayoutInflater.from(context).inflate(R.layout.activity_player_list_item, parent, false);

		return new MyViewHolder(playerView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
		Player player = players[position];
		myViewHolder.setData(player);

		if(selectedIndex == position)
			myViewHolder.llPlayerItem.setSelected(true);
		else
			myViewHolder.llPlayerItem.setSelected(false);
	}

	@Override
	public int getItemCount() {
		return players.length;
	}

	public void setClickListener(ItemClickListener itemClickListener) {
		this.clickListener = itemClickListener;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		TextView tvPlayerName;
		LinearLayout llPlayerItem;

		private MyViewHolder(@NonNull View itemView) {
			super(itemView);

			tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
			itemView.setOnClickListener(this);
			llPlayerItem = itemView.findViewById(R.id.llPlayerItem);
		}

		public void setData(Player player) {
			tvPlayerName.setText(player.getName());
			if(getAdapterPosition() == 0) {
			    llPlayerItem.setSelected(true);

                if(clickListener != null)
                    clickListener.onItemClick(llPlayerItem, selectedIndex);
            }
		}


		@Override
		public void onClick(View view)
		{
			notifyItemChanged(selectedIndex);
			selectedIndex = getAdapterPosition();
			notifyItemChanged(selectedIndex);

			if(clickListener != null)
			    clickListener.onItemClick(view, selectedIndex);
		}
	}
}
