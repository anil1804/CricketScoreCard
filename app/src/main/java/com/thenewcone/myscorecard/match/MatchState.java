package com.thenewcone.myscorecard.match;

import java.io.Serializable;
import java.util.Date;

public class MatchState implements Serializable {
	private int id, saveOrder;
	private boolean isAuto;
	private Date savedDate;
	private String savedName;
	private Match match;

	public int getId() {
		return id;
	}

	public int getSaveOrder() {
		return saveOrder;
	}

	public boolean isAuto() {
		return isAuto;
	}

	public Date getSavedDate() {
		return savedDate;
	}

	public String getSavedName() {
		return savedName;
	}

	public Match getMatch() {
		return match;
	}

	public MatchState(int id, String savedName, boolean isAuto, int saveOrder, Date savedDate, Match match) {
		this.id = id;
		this.savedName = savedName;
		this.isAuto = isAuto;
		this.savedDate = savedDate;
		this.saveOrder = saveOrder;
		this.match = match;
	}
}
