package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theNewCone.cricketScoreCard.help.HelpContent;
import com.theNewCone.cricketScoreCard.help.HelpDetail;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HelpContentDBHandler extends DatabaseHandler {
	public HelpContentDBHandler(Context context) {
		super(context);
	}

	public int addHelpContent(String content) {
		ContentValues values = new ContentValues();
		values.put(TBL_HELP_CONTENT_CONTENT, content);

		SQLiteDatabase db = this.getWritableDatabase();

		boolean alreadyExists = false;
		String selectQuery = String.format(Locale.getDefault(),
				"SELECT %s FROM %s WHERE %s = '%s'",
				TBL_HELP_CONTENT_ID, TBL_HELP_CONTENT, TBL_HELP_CONTENT_CONTENT, content);
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null && cursor.moveToFirst()) {
			alreadyExists = true;
			cursor.close();
		}

		long contentID = CODE_NEW_HELP_CONTENT_DUP_RECORD;

		if (!alreadyExists)
			contentID = db.insert(TBL_HELP_CONTENT, null, values);

		db.close();

		return (int) contentID;
	}

	public boolean hasHelpContent() {
		boolean hasContent = false;

		String selectQuery = String.format(Locale.getDefault(), "SELECT %s FROM %s", TBL_HELP_CONTENT_ID, TBL_HELP_CONTENT);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null && cursor.moveToFirst()) {
			hasContent = true;
			cursor.close();
		}
		db.close();

		return hasContent;
	}

	public List<HelpContent> getAllHelpContent() {
		List<HelpContent> helpContentList = new ArrayList<>();
		String selectQuery = "SELECT * FROM " + TBL_HELP_CONTENT;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null && cursor.moveToFirst()) {
			do {
				int contentID = cursor.getInt(cursor.getColumnIndex(TBL_HELP_CONTENT_ID));
				String content = cursor.getString(cursor.getColumnIndex(TBL_HELP_CONTENT_CONTENT));

				List<HelpDetail> helpDetailList = getHelpDetails(contentID, content);
				helpContentList.add(new HelpContent(contentID, content, helpDetailList));
			} while (cursor.moveToNext());

			cursor.close();
		}
		db.close();

		return helpContentList;
	}

	public void addHelpDetails(HelpDetail helpDetail) {
		int maxContentOrder = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		String getMaxContentOrderIDSQL =
				String.format(Locale.getDefault(), "SELECT MAX(%s) FROM %s WHERE %s = %d",
						TBL_HELP_DETAILS_ORDER, TBL_HELP_DETAILS, TBL_HELP_DETAILS_CONTENT_ID, helpDetail.getContentID());
		Cursor cursor = db.rawQuery(getMaxContentOrderIDSQL, null);
		if (cursor != null && cursor.moveToFirst()) {
			maxContentOrder = cursor.getInt(0);
			cursor.close();
		}

		ContentValues values = new ContentValues();
		values.put(TBL_HELP_DETAILS_CONTENT_ID, helpDetail.getContentID());
		if (helpDetail.getText() != null) {
			values.put(TBL_HELP_DETAILS_TEXT, helpDetail.getText());
		}
		if (helpDetail.getSourceIDList() != null) {
			values.put(TBL_HELP_DETAILS_SRC_ID_JSON, CommonUtils.intListToJSON(helpDetail.getSourceIDList()));
		}
		if (helpDetail.getViewType() != null) {
			values.put(TBL_HELP_DETAILS_VIEW_TYPE, helpDetail.getViewType().toString());
		}
		values.put(TBL_HELP_DETAILS_ORDER, maxContentOrder + 1);

		db.insert(TBL_HELP_DETAILS, null, values);
		db.close();
	}

	private List<HelpDetail> getHelpDetails(int contentID, String content) {
		List<HelpDetail> helpDetailList = new ArrayList<>();
		if (contentID > 0) {
			SQLiteDatabase db = this.getReadableDatabase();

			if (content != null) {
				/*Getting Data from HelpDetail table*/
				String selectQuery = String.format(Locale.getDefault(),
						"SELECT * FROM %s WHERE %s = %d ORDER BY %s",
						TBL_HELP_DETAILS, TBL_HELP_DETAILS_CONTENT_ID, contentID, TBL_HELP_DETAILS_ORDER);

				Cursor cursor = db.rawQuery(selectQuery, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						HelpDetail.ViewType viewType = HelpDetail.ViewType.valueOf(cursor.getString(cursor.getColumnIndex(TBL_HELP_DETAILS_VIEW_TYPE)));
						String text = cursor.getString(cursor.getColumnIndex(TBL_HELP_DETAILS_TEXT));
						String srcIDJson = cursor.getString(cursor.getColumnIndex(TBL_HELP_DETAILS_SRC_ID_JSON));
						int order = cursor.getInt(cursor.getColumnIndex(TBL_HELP_DETAILS_ORDER));

						helpDetailList.add(new HelpDetail(contentID, content, viewType, text, CommonUtils.jsonToIntList(srcIDJson), order));
					} while (cursor.moveToNext());

					cursor.close();
				}
			}

			db.close();
		}

		return helpDetailList;
	}

	public void clearHelpContent() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_HELP_CONTENT, null, null);
		db.delete(TBL_HELP_DETAILS, null, null);

		db.close();
	}
}
