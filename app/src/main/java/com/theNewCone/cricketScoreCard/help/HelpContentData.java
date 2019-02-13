package com.theNewCone.cricketScoreCard.help;

import android.content.Context;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.utils.database.HelpContentDBHandler;

import java.util.ArrayList;
import java.util.List;

public class HelpContentData {
	private HelpContentDBHandler dbh;

	public HelpContentData(Context context) {
		dbh = new HelpContentDBHandler(context);
	}

	public void loadHelpContent() {
		dbh.clearHelpContent();
		loadOverviewContent();
		loadPlayersContent();
		loadTeamsContent();
		loadMatchesContent();
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
					"running Limited overs match." +
					"\n\nYou can manage your players, teams and running matches." +
					"\n\nA new match can be created and score updated, while saving interim " +
					"instances while scoring the match. Load the saved matches, delete unwanted " +
					"instances and view Completed matches." +
					"\n\nLook at the complete score-card and see the various graphs, which provide " +
					"a glimpse of the match." +
					"\n\nRead on all the sections of the Help to get more insight into the app.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Player Overview*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_person);
			text = "<sh>Players</sh>" +
					"\nYou can manage players by clicking on the [I] (Player) icon on the home screen or " +
					"the <b>'Manage Player'</b> link in the navigation (left hand) menu.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Team Overview*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_people);
			text = "<sh>Teams</sh>" +
					"\nTeams can be managed using the [I] (Team) icon or using the <b>'Manage Team'</b> " +
					"link in the navigation menu.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Match Overview*/
			text = "<sh>Match</sh>" +
					"\nBy managing the teams and players, you do not need to key in the player " +
					"every match. Since teams and players are stored on your device, you only need " +
					"to select a team for the match and all the players in the team are listed " +
					"down for selection for the particular match." +
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
					"The screen can be accessed either by clicking on the [I] (Player) button on the home " +
					"screen or the <b>'Manage Player'</b> option on the navigation menu";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Add Player*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Add Player</sh>" +
					"\nTo add a new player, provide the name, select the batting type, bowling " +
					"type and  if the player is a wicket-keeper. At this time, you can also select " +
					"which all teams the player has to be associated to. Finally, click on the [I] (Save)" +
					"button to save the player.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Update Player*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_list);
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Update Player</sh>" +
					"\nTo update a player, first select the player by clicking on the [I] (List) icon in " +
					"the right top menu. The player details will be then shown on the screen. " +
					"Update the required details and click on the [I] (Save) icon to save the details.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Delete Player*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_list);
			imageSourceList.add(R.drawable.ic_delete);
			text = "<sh>Delete Player</sh>" +
					"\nTo delete a player, first select the player by clicking on the [I] (List) icon in " +
					"the right top menu. The player details will be then shown on the screen." +
					"Click on the [I] (Delete) button to delete the player. <hi>Player once deleted, cannot " +
					"be restored</hi>";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Associate Player*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_assign);
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Associate to Team</sh>" +
					"\nThe player can be associated to a team either during or after creating the " +
					"player, by clicking on the [I] (Assign) icon in the right top menu. Select all the " +
					"teams and click on <b><i>OK</i></b>. The number of teams selected will be " +
					"displayed on the screen. Click on the [I] (Save) button to save the player.";
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
					"added, deleted, updated or players assigned to/removed from team. " +
					"The screen can be accessed either by clicking on the [I] (Team) button on the " +
					"home screen or the <b>'Manage Team'</b> option on the navigation menu";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Add Team*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Add Team</sh>" +
					"\nTo add a new player, provide the name and a short name. " +
					"Finally, click on the [I] (Save) button to save the team.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Update Team*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_list);
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Update Team</sh>" +
					"\nTo update a team, first select the player by clicking on the [I] (List) " +
					"icon in the right top menu. The team details will be then shown on the screen." +
					" Update the required details and click on the [I] (Save) icon to save the " +
					"details.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Delete Team*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_list);
			imageSourceList.add(R.drawable.ic_delete);
			text = "<sh>Delete Player</sh>" +
					"\nTo delete a team, first select the team by clicking on the [I] (List) icon " +
					"in the right top menu. The team details will be then shown on the screen. " +
					"Click on the [I] (Delete) button to delete the team. <hi>Team once deleted, " +
					"cannot be restored</hi>";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Associate Players*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_assign);
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Associate Players</sh>" +
					"\nPlayers can be associated to the team either during or after creating the " +
					"team, by clicking on the [I] (Assign) icon in the right top menu. Select all " +
					"the players and click on <b><i>OK</i></b>. The number of players selected " +
					"will be displayed on the screen. Click on the [I] (Save) button to save the " +
					"team.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
		}
	}

	private void loadMatchesContent() {
		String content = "Matches";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			List<Integer> imageSourceList = new ArrayList<>();

			/*Overview of Manage Matches*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_add_screen);
			imageSourceList.add(R.drawable.ic_download);
			imageSourceList.add(R.drawable.ic_delete);
			String text = "<h>Manage Matches</h>" +
					"\n\nMatches are managed across various screens. The home screen has 3 buttons " +
					"[I] (New Match), [I] (Load Match) and [I] (Delete Matches)." +
					"\n\nRead more about <i>New Match</i> in the New Match section under Help.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Load Match*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_download);
			text = "<sh>Load Match</sh>" +
					"\nTo load a saved match, click on the [I] (Load) button the home screen. A " +
					"list of saved matches will be shown, with details of the <i>Saved</i> name, " +
					"<i>Match</i> name, match date and teams playing." +
					"\n\nClick on the match to be loaded and the match card will be loaded." +
					"\n\nThe list of matches shown here are different from the list of matches " +
					"shown on the Match Card's Load Match option. Refer to </i>Other Details</i> " +
					"section of the <b>Match Card</b> Help page for further details on this.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Delete Matches*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_delete);
			text = "<sh>Delete Matches</sh>" +
					"\nThere might be many a times where a lot of instances of a match are saved " +
					"and they need to be deleted. This can be done using the [I] (Load) button on " +
					"the home screen." +
					"\n\nSimilar to when loading a match, clicking on the delete button will list " +
					"all the saved matches to be deleted. However, here you can select multiple " +
					"matches to be deleted all at once. A confirmation dialog is shown before the " +
					"matches are deleted.";
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
					"\n\nA new match can be started by clicking on the [I] (New) button on the " +
					"home screen or the <b>'Play New Match'</b> option on the navigation menu." +
					"\n\nThe screen is enabled only if there are at-least 2 teams created.";
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
					"\nProvide a name you would like to give the match. Provide a meaningful/" +
					"logical name as it will be later useful for loading saved instances or for " +
					"looking at Finished matches." +
					"\n\nIf not provided, yhe team name is automatically generated once both the " +
					"teams are selected. The generated name is editable.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Player Selection*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_edit);
			text = "<sh>Player Selection</sh>" +
					"\nThe player selection option [I] (Select) is available once the team is " +
					"selected. The number of players selected will be determined based on the " +
					"value provided against <b>'Player Count'</b>. This value is 11 by default." +
					"\n\nChoose all the players, else the <i><b>OK</b></i> button will result in a " +
					"validation error." +
					"\n\nOn the other hand, if enough players are not available in the team, then " +
					"the <b>Player Selection</b> will not be available.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Captain & Wicket Keeper Selection*/
			text = "<sh>Captain and Wicket-Keeper</sh>" +
					"\nCaptain and Player selection options are available once the players are " +
					"selected." +
					"\n\nClick on the text saying <b>'Captain'</b> and <b>'WK'</b>, one after the " +
					"other, to select them from the players selected.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Overs and Players*/
			text = "<sh>Overs and Players</sh>" +
					"\nThe <b>'Max Overs'</b> and <b>'Max Wickets'</b> are editable and can be " +
					"updated to the desired values. These values are 50 and 10 respectively by " +
					"default." +
					"\n\nThe <b>'Max per Bowler'</b> is automatically calculated based on the " +
					"maximum overs and wickets. This value is editable, but needs to be logical. " +
					"Else, validation will fail." +
					"\n\nThe <b>'Player Count'</b> is by default 1 more than the maximum wickets. " +
					"This is editable and has to be more than the maximum wickets. Last man " +
					"standing option is not available.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Validation*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_check);
			text = "<sh>Validation</sh>" +
					"\nOnce all details are updated, click on the [I] (Validate) to validate the " +
					"input. Various details like the teams selected, players selected, captain & " +
					"wicket-keeper, overs, wickets, players etc. will be validated." +
					"\n\nUpon successful validation, the toss option will be enabled. At this " +
					"point, no details will be editable, except for the <b>'Match Name'</b>.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Toss & Start*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_start);
			text = "<sh>Toss & Start</sh>" +
					"\nOnce the validation is successful, the Toss section is visible. Select the " +
					"team winning the toss and the choice.  Once both are selected the start match " +
					"icon [I] (Start) is visible. Clicking on it open up the <hi><b>Match Card</b></hi>";
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
					"\n\n<hi>1. </hi>The first section is for the score. The first line shows the " +
					"score of the current batting team, number of overs bowled and the rate at " +
					"which they are scoring." +
					"\nThe next line contains the Target, the maximum overs and the required " +
					"run-rate. However, this section is visible only in the second innings." +
					"\nThe 3rd line, also visible only in the second innings, shows the number of " +
					"runs required and the number of balls remaining." +
					"\n\n<hi>2. </hi> The next section shows the runs scored in the last 12 balls, " +
					"followed by the line showing the number of runs scored in the current over." +
					"\n\n<hi>3. </hi> Next section captures the Batsman details, which includes the " +
					"runs scored, balls played, boundaries and strike-rate." +
					"\nThe batsman details are followed extras in the innings." +
					"\nFinally the  Bowler details are shown which includes the overs bowled, " +
					"maidens, runs conceded, wickets and economy." +
					"\n\n<hi>4. </hi>The final section contains the scoring details, which contains " +
					"a series of buttons enabling the user to update the score. These will be " +
					"detailed in the sections below.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Starting the match*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_save);
			text = "<sh>Starting the match</sh>" +
					"\nAs soon as the match starts, you are required to select the batsmen starting " +
					"the innings, the  batsman facing the first ball and the bowler bowling the " +
					"first over. Once done, the screen layout will switch to scoring view." +
					"\n\nAt this point, you have the option to Save the Match. Refer to <i>Other " +
					"Details</i> section for further reading on saving a match.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Run Scoring Buttons*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.button_round_normal);
			imageSourceList.add(R.drawable.button_round_boundary);
			text = "<sh>Run Scoring Buttons</sh>" +
					"\nThe scoring buttons are divided into 3 categories, which are runs, wickets " +
					"and extras." +
					"\n\nThe buttons related to runs will allow you to record the number of runs " +
					"scored on the  ball bowled. Clicking on any of the buttons looking like [I] " +
					"or [I] will result in runs being scored. This will update the batsman's " +
					"statistics, batsman facing the next delivery, bowler statistics, score, " +
					"run-rate and other details." +
					"\n\nIn the rare scenarios where the number of runs scored is not available " +
					"through these buttons, the <b>'More Runs'</b> button gives user the option " +
					"to provide other number of runs scored. Currently, this value is limited to 7." +
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
					"\n\nUpon selecting the dismissal type, you might require to provide additional" +
					" details. For example if the dismissal is a run-out, the player effecting the " +
					"run-out and the batsman out will also need to be selected." +
					"\n\nAdditional options are also made available based on the dismissal type." +
					"\n<b><i>1. </i></b>During a run-out, there might be runs scored. So, you'll " +
					"have the option to capture the runs scored. This is also the case when the " +
					"dismissal is 'Obstructing Field'." +
					"\n<b><i>2. </i></b> A player could be run-out, given out obstructing field or " +
					"stumped while it's an extra. For capturing this information, click on the " +
					"<b>'is Extra'</b> check-box for the additional options to be displayed." +
					"\n\nOnce a batsman is out, the <hi>Select Batsman</hi> option is enabled on " +
					"the Match Card screen. For certain types of dismissals, the next facing " +
					"batsman might change. In this case, the <hi>Change Current Facing</hi> option " +
					"will be enabled. If the wicket falls on the last ball, ensure that the " +
					"batsman facing the delivery in the next over is selected.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Extras*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.button_round_extra);
			text = "<sh>Extra</sh>" +
					"\nExtras is a part and parcel of the game. They can be leg-byes (LB), byes " +
					"(B), wide (WD), no-ball (N) or a penalty (P). All the extras are denoted by a " +
					"button looking like [I]." +
					"\n\nA delivery is legal for leg-byes and byes (unless they come on a no-ball)," +
					" and they don't get added to either the batsman/bowler's statistics. Clicking " +
					"on the Lb or B button pops up a window to select the number of extras. " +
					"Default is 1." +
					"\n\nIn the case of a wide, the delivery is not counted for either the batsman " +
					"or the bowler. A window pops up upon the click of the button and the runs to " +
					"be selected are the ones which are in addition to the wide-ball. This means, " +
					"the batsman will either have to run or the ball needs to reach the boundary. " +
					"Default is 0." +
					"\n\nFor a no-ball, the ball is counted in the batsman's account, but not " +
					"counted as a legal delivery for the bowler. Any runs scored will be added to " +
					"the batsman's account, unless they are leg-byes or byes. There is an option " +
					"to select if the runs scored are leg-byes/byes in the window that pops up " +
					"when the button is clicked. Here again, the runs are in addition to the no-" +
					"ball. Default is 0 for normal runs and 1 for leg-byes/byes." +
					"\n\n<hi>To capture a wicket on an delivery conceding an extra, use the " +
					"<b><i>Wicket</i></b> option.</hi>";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Player Hurt*/
			text = "<sh>Injuries</sh>" +
					"\nIn the unfortunate incident of a player getting hurt, use the <u><i>'Batsman " +
					"Hurt'</i></u> or the <u><i>'Bowler Hurt'</i></u> options." +
					"\n\nThe screen layout will be automatically updated for Batsman/Bowler " +
					"selection accordingly. You will have to select the batsman facing the next " +
					"delivery, if a batsman is hurt.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Innings Completion*/
			text = "<sh>Innings Completion</sh>" +
					"\n<hi>First Innings - </hi>Once the first innings is complete, either because " +
					"the overs are complete or all wickets have fallen, the <hi><i>'Start  Next " +
					"Innings'</i></hi> button will be shown. Click the button to start the next " +
					"innings." +
					"\n\n<hi>Second Innings - </hi>The second innings is completed either when the " +
					"overs are complete, all wickets have fallen or the score has been chased. At " +
					"this instance, the <b><i>Man of the Match</i></b> needs to be selected and " +
					"the match completion button will appear. Upon completion the match will " +
					"appear in the <b><hi>Finished Match</hi></b> list." +
					"\n\n<hi>Completing a match will delete all saved instances of the match</hi>";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Others*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_save);
			imageSourceList.add(R.drawable.ic_card);
			text = "<sh>Other Details</sh>" +
					"\n<hi>UNDO - </hi>It is but normal that sometimes we might have to undo what " +
					"we have done. Use the <u><i>'Undo Last Ball'</i></u> option to undo the last " +
					"delivery bowled." +
					"\n\n<hi>Change Facing Batsman - </hi>In case you have accidentally selected " +
					"incorrect facing batsman , you don't have to go through the hassle of undoing " +
					"and repeat all actions. Just make use of the <u><i>'Change Facing Batsman'" +
					"</i></u> option." +
					"\n\n<hi>Save Match - </hi>You can save the match anytime during play, except " +
					"for when an over has been completed or a new batsman needs to be selected. " +
					"\n\nTo save the match, click on the [I] (Save) icon. This pops up a window to " +
					"provide the name using which to save the match. To verify if the match has " +
					"been saved or not, see the list of saved instances shown when clicking on the " +
					"<i>Load Match</i> menu option." +
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
					"\n\nFinished matches can be accessed using the [I] (Finished) button the home " +
					"screen. Select the match from the list of matches available and the <b><i>Match " +
					"Summary</i></b> screen will be opened." +
					"\n\nThe top right menu has options to open up the Score Card and the Graphs." +
					"\n\nFor details on Graphs, refer to Graphs item on Help.";
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			/*Score Card*/
			imageSourceList.clear();
			imageSourceList.add(R.drawable.ic_card);
			text = "<sh>Score Card</sh>" +
					"\nThe score card can be opened up using the [I] (Card) option on the right " +
					"top menu. The score card has 2 tabs, one for both the innings." +
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
			dbh.addHelpDetails(new HelpDetail(contentID, text, imageSourceList));
		}
	}

	private void loadFAQContent() {
		String content = "FAQ";

		int contentID = dbh.addHelpContent(content);

		if(contentID != dbh.CODE_NEW_HELP_CONTENT_DUP_RECORD) {
			String text = "<h>FAQ</h>\n";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>[Q]. How is the penalty applied?</b></hi>" +
					"\n\n<b>[A. ]</b>Penalty is applied based on the team to whom the penalty is favouring. " +
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

			text = "<hi><b>[Q]. How to record a wicket during an extra?</b></hi>" +
					"\n\n<b>[A. ]</b>There are different possibilities of wickets based on the extra." +
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

			text = "<hi><b>[Q]. Wait, can I not have a bye or a leg-bye on a no-ball?</b></hi>" +
					"\n\n<b>[A. ]</b>Well yes, you certainly can. When a no ball is bowled and additional " +
					"runs are scored, then you will have the option to select if the runs scored " +
					"are byes or leg-byes." +
					"\nThis option is available not only upon clicking the <hi><b>Wicket</b></hi> " +
					"button, but also if a wicket is recorded on a no-ball.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>[Q]. Oops, I selected the current facing batsman incorrectly. What " +
					"do I do?</b></hi>" +
					"\n\n<b>[A. ]</b>Don't worry. You have the <hi><b>Change Facing Batsman</b></hi> option " +
					"in the match card, in the top right menu. Use that to change the facing " +
					"batsman.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>[Q]. I have incorrectly recorded a wicket. Do I need to restart all over " +
					"again?</b></hi>" +
					"\n\n<b>[A. ]</b>Not really. Just for cases like these, you have the Undo option. You " +
					"can undo up-to last 6 changes, which includes addition of any penalty." +
					"\nChanges to the score/balls bowled/extras/wickets will be rolled back for " +
					"each undo." +
					"\n\nIn the event there was a penalty added, this will be undone as well and " +
					"can be seen as a change in the extras column and score column, if the penalty " +
					"was favouring the batting team." +
					"\nIf it's favouring the bowling team and the current innings is second, then " +
					"the target is updated." +
					"\nHowever, if the favouring team is bowling and the current innings is the " +
					"first, then you do not see any changes on the screen because of undo. But " +
					"rest assured, the penalty runs added to the bowling team will be removed.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>[Q]. Am unable to see Timed-out in the list of dismissals?</b></hi>" +
					"\n\n<b>[A. ]</b>A batsman can only be timed-out if the batsman does not reach " +
					"the ground/crease within the stipulated amount of time." +
					"\n\nIf you are unable to see the option, it means that at least a delivery " +
					"has been bowled since the batsman has entered the crease.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>[Q]. I had saved some match instances and am pretty sure I have not " +
					"deleted them. But now, am not able to see those</b></hi>" +
					"\n\n<b>[A. ]</b>If a match is completed, all the saved instances related to " +
					"the match are deleted. This could be the reason why the saved instances are " +
					"not visible." +
					"\n\nAlternatively, if you have deleted all the data pertaining to the app, " +
					"then not only the saved matches, but the information on the players, teams, " +
					"finished matches will also be lost.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));

			text = "<hi><b>[Q]. I've been scoring a match, but my phone got switched-off. " +
					"Do I have to restart all over again?</b></hi>" +
					"\n\n<b>[A. ]</b>No. The match can be restored if application has accidentally " +
					"closed because of any reason. " +
					"\n\nA pop-up message will be shown upon starting the application and clicking " +
					"on <b><i>Yes</i></b> will restore the match. You will only loose the " +
					"information about the last delivery. All other information will be restored." +
					"\n\n<hi>Beware, clicking on <b><i>No</i></b> will result in deleting the " +
					"instance and you will not be able to restore the match anymore</hi>" +
					"\n\nHowever, in case you clicked on any other portion of the screen and the " +
					"pop-up accidentally got closed, please restart your app to see the pop-up " +
					"again.";
			dbh.addHelpDetails(new HelpDetail(contentID, text));
			dbh.addHelpDetails(new HelpDetail(contentID, true));
		}
	}
}
