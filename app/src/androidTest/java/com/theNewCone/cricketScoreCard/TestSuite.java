package com.theNewCone.cricketScoreCard;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		PlayerManageTest.class,
		TeamManageTest.class,
		NewLimitedOversMatchTest.class,
		NewTournamentTest.class
})
public class TestSuite {

}
