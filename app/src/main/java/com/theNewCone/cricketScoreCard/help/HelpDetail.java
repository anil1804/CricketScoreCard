package com.theNewCone.cricketScoreCard.help;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class HelpDetail implements Serializable {
	private int contentID, sourceID, order;
	private ViewType viewType;
	private String text, content;

	HelpDetail(int contentID, @NonNull ViewType viewType, String text) {
		this.contentID = contentID;
		this.viewType = viewType;
		this.text = text;
	}

	public HelpDetail(int contentID, @NonNull ViewType viewType, int sourceID) {
		this.contentID = contentID;
		this.viewType = viewType;
		this.sourceID = sourceID;
	}

	public HelpDetail(int contentID, String content, @NonNull ViewType viewType, String text, int srcID, int order) {
		this.contentID = contentID;
		this.content = content;
		this.viewType = viewType;
		this.text = text;
		this.sourceID = srcID;
		this.order = order;
	}

	public int getContentID() {
		return contentID;
	}

	public int getSourceID() {
		return sourceID;
	}

	public int getOrder() {
		return order;
	}

	public ViewType getViewType() {
		return viewType;
	}

	public String getText() {
		return text;
	}

	String getContent() {
		return content;
	}

	public enum ViewType {
		TEXT, IMAGE
	}
}
