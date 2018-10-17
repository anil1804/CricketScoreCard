package com.thenewcone.myscorecard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.ItemClickListener;
import com.thenewcone.myscorecard.player.Player;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder> {

	private Player[] players;
	private Context context;
	private ItemClickListener clickListener;

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

		private MyViewHolder(@NonNull View itemView) {
			super(itemView);

			tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
			itemView.setOnClickListener(this);
		}

		public void setData(Player player) {
			tvPlayerName.setText(player.getName());
		}


		@Override
		public void onClick(View view)
		{
			if(clickListener != null)
			    clickListener.onClick(view, getAdapterPosition());
		}
	}
}
