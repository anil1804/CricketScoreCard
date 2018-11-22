package com.theNewCone.cricketScoreCard.help;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HelpContent implements Serializable {
	private int contentID;
	private String content;
	private List<HelpDetail> helpDetailList = new ArrayList<>();

	public HelpContent(int contentID, String content) {
		this.contentID = contentID;
		this.content = content;
	}

	public HelpContent(int contentID, String content, List<HelpDetail> helpDetails) {
		this.contentID = contentID;
		this.content = content;
		this.helpDetailList = helpDetails;
	}

	public int getContentID() {
		return contentID;
	}

	public String getContent() {
		return content;
	}

	public List<HelpDetail> getHelpDetailList() {
		return helpDetailList;
	}
}
