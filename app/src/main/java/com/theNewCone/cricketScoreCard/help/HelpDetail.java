package com.theNewCone.cricketScoreCard.help;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class HelpDetail implements Serializable {
	private int contentID, order;
	private List<Integer> sourceIDList;
	private ViewType viewType;
	private String text, content;

	HelpDetail(int contentID, String text) {
		this.contentID = contentID;
		this.viewType = ViewType.TEXT;
		this.text = text;
	}

	HelpDetail(int contentID, String text, List<Integer> sourceIDList) {
		this.contentID = contentID;
		this.viewType = ViewType.TEXT;
		this.text = text;
		this.sourceIDList = sourceIDList;
	}

	HelpDetail(int contentID, boolean isSeparator) {
		if(isSeparator) {
			this.contentID = contentID;
			this.viewType = ViewType.SEPARATOR;
		}
	}

	public HelpDetail(int contentID, String content, ViewType viewType, String text, List<Integer> sourceIDList, int order) {
		this.contentID = contentID;
		this.content = content;
		this.viewType = viewType;
		this.text = text;
		this.sourceIDList = sourceIDList;
		this.order = order;
	}

	public int getContentID() {
		return contentID;
	}

	public List<Integer> getSourceIDList() {
		return sourceIDList;
	}

	public int getOrder() {
		return order;
	}

	public String getText() {
		return text;
	}

	String getContent() {
		return content;
	}

	public ViewType getViewType() {
		return viewType;
	}

	public enum ViewType {
		TEXT, SEPARATOR
	}

	public enum FormattingType {
		BOLD, ITALIC, UNDERLINE, SECTION_HEADER, HIGHLIGHT, HEADER
	}
}
