package com.theNewCone.cricketScoreCard.scorecard;

import com.theNewCone.cricketScoreCard.enumeration.ExtraType;

import java.io.Serializable;

public class Extra implements Serializable {
	private ExtraType type, subType;
	private int runs;

	public ExtraType getType() {
		return type;
	}

	public int getRuns() {
		return runs;
	}

    public void setRuns(int runs) {
        this.runs =  runs;
    }

	public Extra(ExtraType type, int runs) {
		this.type = type;
		this.runs = runs;
		this.subType = ExtraType.NONE;
	}

	public Extra(ExtraType type, int runs, ExtraType subType) {
		this.type = type;
		this.runs = runs;
		this.subType = subType;
	}

	public ExtraType getSubType() {
		return subType;
	}

}
