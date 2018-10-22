package com.thenewcone.myscorecard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.PlayerInteractionListener;
import com.thenewcone.myscorecard.player.Player;

import java.util.List;

public class PlayerViewAdapter extends RecyclerView.Adapter<PlayerViewAdapter.ViewHolder> {

    private final List<Player> playerList;
    private final PlayerInteractionListener mListener;

    public PlayerViewAdapter(List<Player> playerList, PlayerInteractionListener listener) {
        this.playerList = playerList;
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_player_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  final ViewHolder holder, int position) {

        holder.player = playerList.get(position);
        holder.tvPlayerName.setText(holder.player.getName());
        holder.tvBatStyle.setText(holder.player.getBattingStyle().toString());
        holder.tvBowlStyle.setText(holder.player.getBowlingStyle().toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.player);
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
