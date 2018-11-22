package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.OverInfo;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import java.util.List;

public class OverGraphActivity extends Activity {

	GraphView graph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_only_graph);

		CricketCard card = null;
		OverInfo currentOverInfo;
		String maxOvers;
		double overLimit;

		graph = findViewById(R.id.graph);

		Intent incomingIntent = getIntent();
		if (incomingIntent != null && incomingIntent.getExtras() != null) {
			Bundle extras = incomingIntent.getExtras();
			card = (CricketCard) extras.getSerializable(GraphsActivity.ARG_CRICKET_CARD);
		}

		if (card != null) {
			currentOverInfo = card.getCurrOver();
			maxOvers = card.getMaxOvers();
			overLimit = (maxOvers != null) ? Double.parseDouble(maxOvers) : 50;

			List<OverInfo> overInfoList = card.getOverInfoData();
			if (currentOverInfo != null && currentOverInfo.getOverNumber() > overInfoList.size()) {
				overInfoList.add(currentOverInfo);
			}

			int colorCode = (card.getInnings() == 1)
					? getResources().getColor(R.color.blue_A700)
					: getResources().getColor(R.color.orange_900);

			drawGraph(overInfoList, overLimit, colorCode);
		}
	}

	private void drawGraph(List<OverInfo> overInfoList, double overLimit, int colorCode) {
		double maxYValue = 0;

		int maxXLimit = 0;
		if(overLimit <= 20)
			maxXLimit = 20;
		else if(overLimit <= 50)
			maxXLimit = 50;
		else if(overLimit < 100)
			maxXLimit = 100;


		int scale = CommonUtils.getScreenWidth(this)/50;
		final double size = scale * 10/maxXLimit;

		/*Data points for runs and wickets*/
		DataPoint[] runsDataPoints = new DataPoint[overInfoList.size()];
		int maxRuns = 0, maxWickets = 0;
		int i = 0;

		for (OverInfo overInfo : overInfoList) {
			int runsScored = overInfo.getRunsScored();
			int wickets = overInfo.getWickets();
			maxRuns = maxRuns < runsScored ? runsScored : maxRuns;
			maxWickets = maxWickets < wickets ? wickets : maxWickets;
			double maxValue = (runsScored + wickets * size/scale * 2);
			maxYValue = maxValue > maxYValue ? maxValue : maxYValue;
		}

		for (OverInfo overInfo : overInfoList) {
			int runsScored = overInfo.getRunsScored();
			int wickets = overInfo.getWickets();
			Log.i(Constants.LOG_TAG, String.format("Over - %d, Runs - %d, Wickets - %d", overInfo.getOverNumber(), runsScored, wickets));
			runsDataPoints[i++] = new DataPoint(overInfo.getOverNumber(), runsScored);

			if (wickets > 0) {
				for(int wicketNumber=1; wicketNumber<=wickets; wicketNumber++) {
					PointsGraphSeries<DataPoint> currentWicketSeries =
							getNewWicketSeries(wicketNumber, overInfo.getOverNumber(), runsScored, scale, size);
					maxYValue = currentWicketSeries.getHighestValueY() > maxYValue ? currentWicketSeries.getHighestValueY() : maxYValue;
					graph.addSeries(currentWicketSeries);
				}
			}
		}

		/*Runs Series*/
		BarGraphSeries<DataPoint> runsSeries = new BarGraphSeries<>(runsDataPoints);

		runsSeries.setAnimated(true);
		runsSeries.setSpacing(10);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(colorCode);
		runsSeries.setCustomPaint(paint);

		int xLabelSpace = (int) overLimit/5;
		if(xLabelSpace <= 5)
			xLabelSpace = 5;
		else
			xLabelSpace = 10;

		/*Updating Graph Layout*/
		graph.getViewport().setDrawBorder(false);
		graph.getViewport().setXAxisBoundsManual(true);
		graph.getViewport().setMinX(0.6);
		graph.getViewport().setMaxX(overLimit + 0.4);
		graph.getViewport().setYAxisBoundsManual(true);
		graph.getViewport().setMinY(0);
		graph.getViewport().setMaxY(maxYValue + size/scale * 2);
		graph.getViewport().setBackgroundColor(getResources().getColor(R.color.cyan_400));

		graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
		graph.getGridLabelRenderer().setLabelsSpace(xLabelSpace);

		graph.addSeries(runsSeries);
	}

	private PointsGraphSeries<DataPoint> getNewWicketSeries(int wicketNum, int overNum, int runsScored, double scale, final double size) {
		DataPoint[] wicketDataPoint = new DataPoint[1];
		double yPosition = size*1.25/scale * (wicketNum) + runsScored;
		wicketDataPoint[0] = new DataPoint(overNum, yPosition);

		PointsGraphSeries<DataPoint> wicketsSeries = new PointsGraphSeries<>(wicketDataPoint);
		wicketsSeries.setShape(PointsGraphSeries.Shape.POINT);
		wicketsSeries.setSize((float) size);
		wicketsSeries.setColor(Color.WHITE);

		Log.i(Constants.LOG_TAG, String.format("Runs - %d, Wicket Number - %d, Y-Position - %f, Size - %f", runsScored, wicketNum, yPosition, size));

		return wicketsSeries;
	}
}
