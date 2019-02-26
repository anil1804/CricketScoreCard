package com.theNewCone.cricketScoreCard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;

import java.util.List;

public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.MyViewHolder> {
	private final List<Object> itemList;
	private final ListInteractionListener mListener;

	public SimpleListAdapter(@NonNull List<Object> itemList, ListInteractionListener mListener) {
		this.itemList = itemList;
		this.mListener = mListener;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_team_view, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
		final int adapterPosition = holder.getAdapterPosition();

		holder.tvSimpleListText.setText(itemList.get(adapterPosition).toString());

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onListFragmentInteraction(itemList.get(adapterPosition));
			}
		});
	}

	@Override
	public int getItemCount() {
		return itemList.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		final View mView;
		TextView tvSimpleListText;

		private MyViewHolder(@NonNull View itemView) {
			super(itemView);

			mView = itemView;
			tvSimpleListText = itemView.findViewById(R.id.tvSimpleListText);
		}
	}
}
