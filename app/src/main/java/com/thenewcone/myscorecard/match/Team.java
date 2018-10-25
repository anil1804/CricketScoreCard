package com.thenewcone.myscorecard.match;

import com.thenewcone.myscorecard.player.Player;

import java.util.List;

public class Team {
    int id;
    String name, shortName;
    List<Player> matchPlayers;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team(int id, String shortName) {
        this.id = id;
        this.shortName = shortName;
    }

    public Team(String name, String shortName) {
    	this.id = -1;
        this.name = name;
        this.shortName = shortName;
    }

    public Team(int id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }
}
