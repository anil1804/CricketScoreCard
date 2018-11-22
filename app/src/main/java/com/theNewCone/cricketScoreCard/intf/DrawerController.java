package com.theNewCone.cricketScoreCard.intf;

public interface DrawerController {
	void setDrawerEnabled(boolean enabled);
	void disableAllDrawerMenuItems();
	void disableDrawerMenuItem(int id);
	void enableAllDrawerMenuItems();
	void enableDrawerMenuItem(int id);
}
