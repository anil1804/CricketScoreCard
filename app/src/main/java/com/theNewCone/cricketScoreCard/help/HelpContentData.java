package com.theNewCone.cricketScoreCard.help;

import android.content.Context;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class HelpContentData {
	private DatabaseHandler dbh;

	public HelpContentData(Context context) {
		dbh = new DatabaseHandler(context);
	}

	public void loadHelpContent() {
		dbh.clearHelpContent();
		loadOverviewContent();
		loadPlayersContent();
		loadTeamsContent();
		loadNewMatchContent();
		loadMatchCardContent();
		loadFinishedMatchesContent();
		loadGraphsContent();
		loadFAQContent();
	}

	private void loadOverviewContent() {
		String content = "Overview";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			List<Integer> imageSourceList = new ArrayList<>();

			/*Initial Overview*/
			String text = "<h>Overview</h>" +
					"\n\nThe Cricket Score Card app helps you track the score of a currently " +
					"running Limited overs match.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Player Overview*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_person);
			text = "<sh>Players</sh>" +
					"\nYou can manage players by clicking on the [I] icon on the home screen or " +
					"the <b>'Manage Player'</b> link in the navigation (left hand) menu.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Team Overview*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_people);
			text = "<sh>Teams</sh>" +
					"\nTeams can be managed using the [I] icon or using the <b>'Manage Team'</b> " +
					"link in the navigation menu.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Match Overview*/
			text = "<sh>Match</sh>" +
					"\nBy managing the teams and players, you do not need to add/type the player " +
					"name for every match. Since these are stored on your mobile, you only need to " +
					"select a team for the match and all the players  in the team are listed down " +
					"for selection for the particular match." +
					"\nMore about this in the <i>New Match</i> section." +
					"\n\nThe matches can be managed from the multiple icons on the home screen." +
					"\nMore about this in the <i>Matches</i> section of help";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
		}
	}

	private void loadPlayersContent() {
		String content = "Players";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			List<Integer> imageSourceList = new ArrayList<>();

			/*Overview of Manage Players*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_person);
			String text = "<h>Manage Players</h>"+
					"\n\nThe Manage Players screen helps in managing the players. Players can be " +
					"added, deleted, updated or added to/removed from a team." +
					"The screen can be accessed either by clicking on the [I] button on the home " +
					"screen or the <b>'Manage Player'</b> option on the navigation drawer";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Add Player*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Add Player</sh>" +
					"\nTo add a new player, provide the name, select the batting type, bowling " +
					"type and  if the player is a wicket-keeper. At this time, you can also select " +
					"which all teams the player has to be associated to. Finally, click on the [I] " +
					"button to save the player.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Update Player*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_list);
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Update Player</sh>" +
					"\nTo update a player, first select the player by clicking on the [I] icon in " +
					"the right top menu. The player details will be then shown on the screen. " +
					"Update the required details and click on the [I] icon to save the details.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Delete Player*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_list);
			imageSourceList.add(R.drawable.ic_delete);
			text = "<sh>Delete Player</sh>" +
					"\nTo delete a player, first select the player by clicking on the [I] icon in " +
					"the right top menu. The player details will be then shown on the screen." +
					"Click on the [I] button to delete the player. <hi>Player once deleted, cannot " +
					"be restored</hi>";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Associate Player*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_assign);
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Associate to Team</sh>" +
					"\nThe player can be associated to a team either during or after creating the " +
					"player, by clicking on the [I] icon in the right top menu. Select all the " +
					"teams and click on <b><i>OK</i></b>. The number of teams selected will be " +
					"displayed on the screen. Click on the [I] button to save the player.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
		}
	}

	private void loadTeamsContent() {
		String content = "Teams";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			List<Integer> imageSourceList = new ArrayList<>();

			/*Overview of Manage Teams*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_people);
			String text = "<h>Manage Teams</h>" +
					"\n\nThe Manage Teams screen helps in managing the teams. Teams can be " +
					"added, deleted, updated or players assigned to/removed from team" +
					"The screen can be accessed either by clicking on the [I] button on the home " +
					"screen or the <b>'Manage Team'</b> option on the navigation drawer";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Add Team*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Add Team</sh>" +
					"\nTo add a new player, provide the name and a short name." +
					"Finally, click on the [I] button to save the team.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Update Team*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_list);
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Update Team</sh>" +
					"\nTo update a team, first select the player by clicking on the [I] icon in " +
					"the right top menu. The team details will be then shown on the screen. Update " +
					"the required details and click on the [I] icon to save the details.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Delete Team*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_list);
			imageSourceList.add(R.drawable.ic_delete);
			text = "<sh>Delete Player</sh>" +
					"\nTo delete a team, first select the team by clicking on the [I] icon in the " +
					"right top menu. The team details will be then shown on the screen. Click on " +
					"the [I] button to delete the team. <hi>Team once deleted, cannot be restored</hi>";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Associate Players*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_assign);
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Associate Players</sh>" +
					"\nPlayers can be associated to the team either during or after creating the " +
					"team, by clicking on the [I] icon in the right top menu. Select all the " +
					"players and click on <b><i>OK</i></b>. The number of players selected will be " +
					"displayed on the screen. Click on the [I] button to save the team.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
		}
	}

	private void loadNewMatchContent() {
		String content = "New Match";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			List<Integer> imageSourceList = new ArrayList<>();

			/*Overview of Screen*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_add_screen);
			String text = "<h>New Match</h>" +
					"\n\nA new match can be started by clicking on the [I] button on the home " +
					"screen or  the <b>'Play New Match'</b> option on the navigation drawer" +
					"\n\nThe screen is enabled only if there are at-least 2 teams created";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Team Selection*/
			text = "<sh>Team Selection</sh>" +
					"\nClick on <b>'Select Team'</b> to select the team(s) from the list of " +
					"available teams.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Match Name*/
			text = "<sh>Match Name</sh>" +
					"\nThe team name is automatically generated once both the teams are selected, " +
					"if the name is not already keyed in. The generated name can be modified to " +
					"whatever the user's preference is.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Player Selection*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_edit);
			text = "<sh>Player Selection</sh>" +
					"\nThe player selection option [I] is available once the team is selected. The " +
					"number of players selected will be  determined based on the value provided " +
					"against <b>'Player Count'</b>. This value is 11 by default." +
					"\n\nYou can <i>Cancel</i> the player selection, but unless the correct number " +
					"of players are selected, <i>OK</i> will not result in player selection" +
					"\n\nIf enough players are not available in the team, then the <b>Player " +
					"Selection</b> will not be available.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Captain & Wicket Keeper Selection*/
			text = "<sh>Captain and Wicket-Keeper</sh>" +
					"\nCaptain and Player selection options are available once the players are " +
					"selected." +
					"\n\nClick on the text saying <b>'Captain'</b> and <b>'WK'</b> to select them " +
					"from the players selected.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Overs and Players*/
			text = "<sh>Overs and Players</sh>" +
					"\nThe <b>'Max Overs'</b> and <b>'Max Overs'</b> are editable and can be " +
					"updated to the desired values. These values are 50 and 10 by default." +
					"\n\nThe <b>'Max per Bowler'</b> is automatically calculated based on the " +
					"maximum overs and wickets. This value is editable, but needs to be logical. " +
					"Else, validation will fail" +
					"\n\nThe <b>'Player Count'</b> is by default 1 more than the maximum wickets. " +
					"This is editable and has to be more than the maximum wickets. Last man " +
					"standing option is not available";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Validation*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_check);
			text = "<sh>Validation</sh>" +
					"\nOnce all details are updated, click on the [I] to validate the input. " +
					"Various details like the team selection, players selected, captain & " +
					"wicket-keeper selection, overs, wickets, players etc. will be validated. Upon " +
					"successful validation, the toss option will be enabled. At this point, no " +
					"details will be editable, except for the <b>'Match Name'</b>";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Toss & Start*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_start);
			text = "<sh>Toss & Start</sh>" +
					"\nOnce the validation is successful, the Toss section is visible. Select the " +
					"team winning the toss and the choice.  Once both are selected the start match " +
					"icon [I] is visible. Clicking on it open up the <hi><b>Match Card</b></hi>";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
		}
	}

	private void loadMatchCardContent() {
		String content = "Match Card";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			List<Integer> imageSourceList = new ArrayList<>();

			/*Overview of Screen*/
			String text = "<h>Match Card</h>" +
					"\n\nThe screen is divided into multiple sections." +
					"\n\n<hi>1. </hi>The first section is the score portion. Here, the first line " +
					"shows the score of the  current batting team, number of overs bowled and the " +
					"run-rate at which they are scoring." +
					"\nThe next line in this section contains the Target, the maximum overs and the " +
					"required run-rate. However, this section is visible only for the second innings." +
					"\nThe 3rd line, also visible only for the second innings, shows the number of " +
					"runs required  and the number of balls remaining" +
					"\n\n<hi>2. </hi> The next section shows the runs scored in the last 12 balls " +
					"on the first line,  followed by the line showing the number of runs scored in " +
					"the current over." +
					"\n\n<hi>3. </hi> Next section captures the Batsman details, which includes the " +
					"runs scored,  balls played, boundaries and strike-rate." +
					"\nThe batsman details are followed by details of the extras in the innings, " +
					"which is followed by  the Bowler details, which includes the overs bowled, " +
					"maidens, runs given, wickets and economy" +
					"\n\n<hi>4. </hi>The final section contains the scoring details, which contains " +
					"a series of buttons  which enables the user to update the score. These will " +
					"be detailed in the sections below.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Starting the match*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Starting the match</sh>" +
					"\nAs soon as the match starts, you are required to select the batsmen starting " +
					"the innings, the  batsman facing the first ball and the bowler bowling the " +
					"first over. Once done, the screen layout  is similar to the one described " +
					"above." +
					"\n\nAt this point, you have the option to Save the Match. To save the match, " +
					"click on the [I] icon. This pops up a window to provide the name using which " +
					"to save the match.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));

			/*Run Scoring Buttons*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.button_round_normal);
			imageSourceList.add(R.drawable.button_round_boundary);
			text = "<sh>Run Scoring Buttons</sh>" +
					"\nThe scoring buttons are divided into 3 categories, which are runs, wickets " +
					"and extras." +
					"\n\nThe buttons related to runs will enable you to record the number of runs " +
					"scored on the  ball bowled. Clicking on any of the buttons looking like [I] " +
					"or [I] will result in runs being scored. This will update the batsman's " +
					"statistics, batsman facing the next delivery, bowler statistics, score, " +
					"run-rate, score in the last 12 balls, runs scored in the current over, " +
					"required run-rate (if applicable) and runs required in balls remaining (while " +
					"chasing)." +
					"\n\nIn the rare scenarios where the number of runs scored is not available on " +
					"these buttons, there is a <b>'More Runs'</b> button, which gives the user the " +
					"option to provide other amount of runs scored. Currently, this value is " +
					"limited to 7." +
					"\n\nThe batsman facing the next ball is automatically determined based on the " +
					"runs scored.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Wickets*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.button_round_wicket);
			text = "<sh>Wicket</sh>" +
					"\nThe button looking like [I] allows you to capture the wicket. A new window " +
					"is shown to select the type of dismissal. Some types of dismissal might not " +
					"be shown based on the current match situation." +
					"\n\nUpon selecting the dismissal type, you might require to select additional " +
					"details. For example if the dismissal is a run-out, the player effecting the " +
					"run-out and the batsman out will also need to be selected." +
					"\n\nAdditional options are also made available based on the dismissal type." +
					"\n<b><i>1. </b></i>During a run-out, there might be runs scored. So, you'll " +
					"have the option to capture the runs scored. This is also the case when the " +
					"dismissal is 'Obstructing Field'<b><i>2. </b></i> A player could be run-out, " +
					"given out obstructing field or stumped while it's an extra. For capturing " +
					"this information, click on the <b>'is Extra'</b> check-box for the additional " +
					"options to be displayed" +
					"\n\nOnce a batsman is out, the <hi>Select Batsman</hi> option is enabled on " +
					"the Match Card screen. For certain types of dismissals, the next facing " +
					"batsman might change. In this case, the <hi>Select Current Facing</hi> option " +
					"will be enabled. In case the wicket falls on the last ball, ensure that the " +
					"batsman facing the delivery in the next over is selected.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Extras*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.button_round_extra);
			text = "<sh>Extra</sh>" +
					"\nExtras as a part and parcel of the game. They can be leg-byes (LB), byes " +
					"(B), wide (WD), no-ball (N) or a penalty (P). All the extras are denoted by a " +
					"button looking like [I]." +
					"\n\nA ball is counted for leg-byes and byes (unless they come on a no-ball), " +
					"and they don't get added to either the batsman/bowler's statistics. Clicking " +
					"on the button pops up a window to select the number of extras. Default is 1." +
					"\n\nIn the case of a wide, the ball is not counted for either the batsman or " +
					"the bowler. A window pops up upon the click of the button and the runs to be " +
					"selected are the ones which are in addition to the wide-ball. This means, the " +
					"batsman will either have to run or the ball needs to reach the boundary. Else," +
					" the runs will have to be selected as 0. Default is 0." +
					"\n\nFor a no-ball, the ball is counted in the batsman's account, but not " +
					"counted as a legacy delivery for the bowler. Any runs scored will be added to " +
					"te batsman's account, unless they are leg-byes or byes. There is an option to " +
					"select if the runs scored are leg-byes/byes in the window that pops up when " +
					"the button is clicked. Here again, the runs are in addition to the no-ball. " +
					"Default is 0 for normal runs and 1 for leg-byes/byes." +
					"\n\n<hi>To capture a wicket on an illegal delivery, use the <b><i>Wicket</i></b>" +
					" option.</hi>";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Player Hurt*/
			text = "<sh>Injuries</sh>" +
					"\nIn the unfortunate incident of a player getting hurt, use the <u><i>'Batsman " +
					"Hurt'</i></u> or the <u><i>Bowler Hurt</i></u> options." +
					"\n\nThe screen layout will be automatically updated for Batsman/Bowler " +
					"selection accordingly. You will have to select the batsman facing the next " +
					"delivery, in case the injured player is one of the batsman.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Innings Completion*/
			text = "<sh>Innings Completion</sh>" +
					"\n<hi>First Innings - </hi>Once the first innings is complete, either because " +
					"the overs are complete or all wickets have fallen, a new button <hi><i>'Start " +
					"Next Innings'</i></hi> will be shown. Click the button to start the next " +
					"innings." +
					"\n\n<hi>Second Innings - </hi>The second innings is completed either when the " +
					"overs are complete, all wickets have fallen or the score has been chased. At " +
					"this instance, the <b><i>Man of the Match</i></b> needs to be selected and the" +
					" match completion button will appear. Upon completion the match will appear in" +
					"the <b><hi>Finished Match</hi></b> list.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));

			/*Others*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_save);
			imageSourceList.add(R.drawable.ic_card);
			text = "<sh>Other Details</sh>" +
					"\n<hi>UNDO - </hi>It is but normal that sometimes we might have to undo what " +
					"we have done. Use the <u><i>'Undo Last Ball'</i></u> option to undo the last " +
					"delivery bowled." +
					"\n\n<hi>Change Facing Batsman - </hi>In case you have accidentally select " +
					"incorrect batsman as the facing batsman, you don't have to go through the " +
					"hassle of undoing and repeat all actions. Just make use of the <u><i>'Change " +
					"Facing Batsman'</i></u> option." +
					"\n\n<hi>Save Match - </hi>You can save the match anytime during play, except " +
					"for when an over has been completed or a new batsman needs to be selected. " +
					"The [I] icon on the right top menu helps in saving the match." +
					"\n\n<hi>Load Match - </hi>Load any previous saved instance related to the " +
					"current match running. Upon loading a match, the current state is overwritten " +
					"and there is no way to come back to the current state, unless the current " +
					"state is saved. Undo option is also not available upon load. However, " +
					"subsequent deliveries after load can be undone." +
					"\n\n<hi>Score Card - </hi>The full score card can be viewed by clicking the " +
					"[I] icon. More details on this in the <b>'Finished Matches'</b> section of Help." +
					"\n\n<hi>Graphs - </hi>Graphs related to the match are also available. Use the " +
					"<u><i>'Graphs'</i></u> option to view them. More details on this in the <b>" +
					"'Graphs'</b> section of Help.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
		}
	}

	private void loadFinishedMatchesContent() {
		String content = "Finished Matches";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			List<Integer> imageSourceList = new ArrayList<>();

			/*Initial Overview*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_card);
			String text = "<h>Finished Matches</h>" +
					"\n\nFinished matches can be accessed using the [I] button the home screen. " +
					"Select the match from the list of matches available and the Match Summary " +
					"will be opened. The screen will show the summary of the match." +
					"\n\nThe top right menu has options to open up the Score Card and the Graphs." +
					"\n\nFor details on Graphs, refer to Graphs item on Help.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Score Card*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_card);
			text = "<sh>Score Card</sh>" +
					"\nThe score card can be opened up using the [I] option on the right top menu. " +
					"The score card has 2 tabs, one for both the innings." +
					"\nThe score card is also accessible from the <i>Match Card</i>. If the score " +
					"card is accessed from the match card during the first innings, only 1 tab is " +
					"visible." +
					"\n\nThe score card has full list of the batsman statistics , dismissal " +
					"details, details of the extras, bowler statistics and fall of wickets for " +
					"each innings played.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
		}
	}

	private void loadGraphsContent() {
		String content = "Graphs";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			List<Integer> imageSourceList = new ArrayList<>();

			/*Initial Overview*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_graph);
			String text = "<h>Graphs</h>" +
					"\n\nGraphs can be accessed from either the <i>Match Card</i> using the menu " +
					"option on top right or from the <i>Match Summary</i> by clicking the [I] icon " +
					"on the top right menu." +
					"\n\nThere are 3 different kinds of graphs available" +
					"\n<hi>Manhattan : </hi>Shows the runs scored in each over and the number of " +
					"wickets taken in each. There are different graphs for each innings." +
					"\n<hi>Partnership : </hi>Shows the partnership graph for each of the innings " +
					"based on the selection. Has separate bars for contribution by each batsman as " +
					"well as the contribution of extras." +
					"\n<hi>Worm : </hi>Shows the cumulative runs scored over the entire innings. " +
					"This graph also shows the wickets fallen in the form of dots and in addition " +
					"includes both innings graphs in one screen, for a comparison.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
		}
	}

	private void loadFAQContent() {
		String content = "FAQ";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			String text = "<h>FAQ</h>\n";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>Q. How is the penalty applied?</b></hi>" +
					"\n\nA. Penalty is applied based on the team to whom the penalty is favouring. " +
					"\n\nIf the penalty is favouring the batting team, then it is applied " +
					"immediately." +
					"\n\nIf the penalty is favouring the bowling team, then there are 2 " +
					"possibilities" +
					"\n - If the innings is the first innings, then the score will start with the " +
					"penalty added at the start of second innings." +
					"\n - If the innings is the second innings, then the first innings score is " +
					"incremented with the penalty and the second innings' target is revised " +
					"accordingly.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>Q. How to record a wicket during an extra?</b></hi>" +
					"\n\nA. There are different possibilities of wickets based on the extra." +
					"\nOn a no-ball, bye or leg-bye, you could have a field obstruction or a " +
					"run-out." +
					"\nOn a wide ball though, in addition to the above, a stumped out is also " +
					"possible." +
					"\n\nIrrespective of what is the dismissal type or the extra, a wicket is always " +
					"recorded using <hi><b>Wicket</b></hi> button." +
					"\n\nBased on the type of dismissal, the extra option is enabled if it's " +
					"possible to have that dismissal type on an extra.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>Q. Wait, can I not have a bye or a leg-bye on a no-ball?</b></hi>" +
					"\n\nA. Well yes, you certainly can. When a no ball is bowled and additional " +
					"runs are scored, then you will have the option to select if the runs scored " +
					"are byes or leg-byes." +
					"\nThis option is available not only upon clicking the <hi><b>Wicket</b></hi> " +
					"button, but also if a wicket is recorded on a no-ball.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>Q. Oops, I selected the current facing batsman incorrectly. What " +
					"do I do?</b></hi>" +
					"\n\nA. Don't worry. You have the <hi><b>Change Facing Batsman</b></hi> option " +
					"in the match card, in the top right menu. Use that to change the facing " +
					"batsman.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>Q. I have incorrectly recorded a wicket. Do I need to restart all over " +
					"again?</b></hi>" +
					"\n\nA. Not really. Just for cases like these, you have the Undo option. You " +
					"can undo up-to last 6 changes, which includes addition of any penalty." +
					"\nChanges to the score/balls bowled/extras/wickets will be rolled back for " +
					"each undo." +
					"\n\nIn the even there was a penalty added, this will be undone as well and " +
					"can be seen as a change in the extras column and score column, if the penalty " +
					"was favouring the batting team." +
					"\nIf it's favouring the bowling team and the current innings is second, then " +
					"the target is updated." +
					"\nHowever, if the favouring team is bowling and the current innings is the " +
					"first, then you do not see any changes on the screen because of undo. But " +
					"rest assured, the penalty runs added to the bowling team will be removed.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));
		}
	}
}
