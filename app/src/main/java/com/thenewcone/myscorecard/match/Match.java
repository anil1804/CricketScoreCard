package com.thenewcone.myscorecard.match;

public class Match {
    String name;
    int id;
    Team team1, team2;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTeam1ID() {
        return team1.getId();
    }

    public String getTeam1ShortName() {
        return team1.getShortName();
    }

    public int getTeam2ID() {
        return team2.getId();
    }

    public String getTeam2ShortName() {
        return team2.getShortName();
    }

    public Match(String name, Team team1, Team team2) {
        this.name = name;
        this.team1 = team1;
        this.team2 = team2;
    }

    public Match(int id, String name, Team team1, Team team2) {
        this.id = id;
        this.name = name;
        this.team1 = team1;
        this.team2 = team2;
    }
}
