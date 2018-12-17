package com.theNewCone.cricketScoreCard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.enumeration.BowlingType;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.player.Player;

import java.util.List;

public class PlayerViewAdapter extends RecyclerView.Adapter<PlayerViewAdapter.ViewHolder> {

    private final List<Player> playerList;
    private final ListInteractionListener mListener;
    private final boolean isMultiSelect;

	private SparseBooleanArray selPlayerIDs;

    public PlayerViewAdapter(List<Player> playerList, ListInteractionListener listener, boolean isMultiSelect, List<Integer> associatedPlayers) {
        this.playerList = playerList;
        mListener = listener;
        this.isMultiSelect = isMultiSelect;

        selPlayerIDs = new SparseBooleanArray();
		for(Player player : playerList) {
			if (associatedPlayers != null && associatedPlayers.contains(player.getID()))
				selPlayerIDs.put(player.getID(), true);
		}
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_player_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		final int adapterPosition = holder.getAdapterPosition();
		holder.player = playerList.get(adapterPosition);
        String playerNameText = holder.player.getName() + (holder.player.isWicketKeeper() ? " (w)" : "");
        holder.tvPlayerName.setText(playerNameText);
        holder.tvBatStyle.setText(holder.player.getBattingStyle().toString());
		holder.tvBowlStyle.setText(holder.player.getBowlingStyle() != BowlingType.NONE ? holder.player.getBowlingStyle().toString() : "");

		if (selPlayerIDs.get(holder.player.getID())) {
			holder.mView.setSelected(true);
		} else {
			holder.mView.setSelected(false);
		}

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
						if(!isMultiSelect) {
							mListener.onListFragmentInteraction(holder.player);
						} else {
							boolean isPresent = selPlayerIDs.get(holder.player.getID());
							mListener.onListFragmentMultiSelect(holder.player, isPresent);
							selPlayerIDs.put(holder.player.getID(), !isPresent);
							notifyItemChanged(adapterPosition);
						}
					}
				}
	        });
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Player player;
        final View mView;
        final TextView tvPlayerName, tvBatStyle, tvBowlStyle;

        ViewHolder(View view) {
            super(view);
            mView = view;
            tvPlayerName = view.findViewById(R.id.tvPlayerName);
            tvBatStyle = view.findViewById(R.id.tvBattingStyle);
            tvBowlStyle = view.findViewById(R.id.tvBowlingStyle);
        }
    }
}
