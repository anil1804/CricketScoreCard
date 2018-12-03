package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
//import android.graphics.Canvas;
//import android.graphics.Paint;
import android.os.Bundle;
import android.util.SparseIntArray;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.match.BallInfo;
import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.OverInfo;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class RunsGraphActivity extends Activity {

	GraphView graph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_only_graph);

		CricketCard currInnsCard = null, prevInnsCard = null;

		OverInfo currOverInfo = null;
		String maxOvers, maxOversPrev;
		double overLimit = 50, overLimitPrev = 0;
		int maxScore = 50;

		graph = findViewById(R.id.graph);

		Intent incomingIntent = getIntent();
		if (incomingIntent != null && incomingIntent.getExtras() != null) {
			Bundle extras = incomingIntent.getExtras();

			currInnsCard = (CricketCard) extras.getSerializable(GraphsActivity.ARG_CRICKET_CARD);
			if(currInnsCard != null) {
				currOverInfo = currInnsCard.getCurrOver();
				maxOvers = currInnsCard.getMaxOvers();
				overLimit = (maxOvers != null) ? Double.parseDouble(maxOvers) : 50;
				maxScore = currInnsCard.getScore();
			}

			prevInnsCard = (CricketCard) extras.getSerializable(GraphsActivity.ARG_CRICKET_CARD_PREV_INNS);
			if(prevInnsCard != null) {
				maxOversPrev = prevInnsCard.getMaxOvers();
				overLimitPrev = (maxOversPrev != null) ? Double.parseDouble(maxOversPrev) : overLimit;
				maxScore = prevInnsCard.getScore() > maxScore ? prevInnsCard.getScore() : maxScore;
			}

		}

		if (currInnsCard != null) {
			List<OverInfo> overInfoList = currInnsCard.getOverInfoData();
			overLimit = (overLimitPrev > overLimit) ? overLimitPrev : overLimit;
			if (currOverInfo != null && currOverInfo.getOverNumber() > overInfoList.size()) {
				overInfoList.add(currOverInfo);
			}

			int colorCode = (currInnsCard.getInnings() == 1)
					? getResources().getColor(R.color.blue_A700)
					: getResources().getColor(R.color.orange_900);

			drawGraph(overInfoList, overLimit, maxScore, colorCode, currInnsCard.getBattingTeam().getShortName());
		}

		if(prevInnsCard != null) {
			int colorCode = (prevInnsCard.getInnings() == 1)
					? getResources().getColor(R.color.blue_A700)
					: getResources().getColor(R.color.orange_900);

			List<OverInfo> overInfoList = prevInnsCard.getOverInfoData();
			drawGraph(overInfoList, overLimitPrev, maxScore, colorCode, prevInnsCard.getBattingTeam().getShortName());
		}
	}

	private void drawGraph(List<OverInfo> overInfoList, double overLimit, int maxScore, int colorCode, String teamShortName) {
		double maxYValue = maxScore;

		int scale = CommonUtils.getScreenWidth(this)/50;
		final double size = scale * 6/overLimit;

		/*Data points for runs and wickets*/
		List<DataPoint> runsDataPointsList = new ArrayList<>();

		int runsScored = 0;
		runsDataPointsList.add(new DataPoint(0, runsScored));

		List<DataPoint> wicketDataPoints = new ArrayList<>();
		for (OverInfo overInfo : overInfoList) {
			SparseIntArray runsPerBall = overInfo.getRunsPerBall();
			for(int i=0; i<runsPerBall.size(); i++) {
				int ballNumber = runsPerBall.keyAt(i);
				double overPosition = (double) (overInfo.getOverNumber() - 1) + ((double) ballNumber)/6;
				runsScored += runsPerBall.valueAt(i);
				runsDataPointsList.add(new DataPoint(overPosition, runsScored));

				int numWickets = 0;
				for(BallInfo ball : overInfo.getBallInfo()) {
					if(ball.getBallNumber() == ballNumber && ball.getWicketData() != null) {
						numWickets++;
					}
				}

				if (numWickets > 0) {
					wicketDataPoints.addAll(getNewWicketSeries(numWickets, overPosition, runsScored, scale, size));
				}
			}
		}

		if(wicketDataPoints.size() > 0) {
			PointsGraphSeries<DataPoint> wicketsSeries =
					new PointsGraphSeries<>(CommonUtils.objectArrToDataPointArr(wicketDataPoints.toArray()));
			wicketsSeries.setShape(PointsGraphSeries.Shape.POINT);
			wicketsSeries.setSize((float) size);
			wicketsSeries.setColor(colorCode);
			maxYValue = wicketsSeries.getHighestValueY() > maxYValue ? wicketsSeries.getHighestValueY() : maxYValue;
			graph.addSeries(wicketsSeries);
		}

		DataPoint[] runsDataPoints = CommonUtils.objectArrToDataPointArr(runsDataPointsList.toArray());

		/*Runs Series*/
		LineGraphSeries<DataPoint> runsSeries;
		if(runsDataPoints != null) {
			runsSeries = new LineGraphSeries<>(runsDataPoints);
			runsSeries.setAnimated(true);

			runsSeries.setColor(colorCode);
			runsSeries.setThickness(5);
			//runsSeries.setCustomPaint(paint);

			runsSeries.setTitle(teamShortName + " Inns");
			graph.addSeries(runsSeries);
		}

		int xLabelSpace = (int) overLimit/5;
		/*Updating Graph Layout*/
		if(xLabelSpace <= 5)
			xLabelSpace = 5;
		else
			xLabelSpace = 10;

		maxYValue = (maxYValue + size/scale * 2);
		if(maxYValue < 20)
			maxYValue *= 1.25;
		else if(maxYValue < 50)
			maxYValue *= 1.12;
		else
			maxYValue *= 1.05;

		graph.getViewport().setDrawBorder(false);
		graph.getViewport().setXAxisBoundsManual(true);
		graph.getViewport().setMinX(0);
		graph.getViewport().setMaxX(overLimit + 2);
		graph.getViewport().setYAxisBoundsManual(true);
		graph.getViewport().setMinY(0);
		graph.getViewport().setMaxY(maxYValue);
		graph.getViewport().setBackgroundColor(getResources().getColor(R.color.cyan_100));
		graph.animate();

		graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
		graph.getGridLabelRenderer().setLabelsSpace(xLabelSpace);
		graph.getLegendRenderer().setVisible(true);
		graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

		/*LegendRenderer legendRenderer = new LegendRenderer(graph);
		legendRenderer.setAlign(LegendRenderer.LegendAlign.BOTTOM);
		legendRenderer.setVisible(true);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(colorCode);
		paint.setStrokeWidth(5);
		Canvas canvas = new Canvas();
		canvas.drawRect(0, 0, 4, 2, paint);
		canvas.drawText(teamShortName + " Inns", 7, 2, paint);

		legendRenderer.draw(canvas);
		graph.setLegendRenderer(legendRenderer);*/
	}

	private List<DataPoint> getNewWicketSeries(int numWickets, double xPosition, int runsScored, double scale, final double size) {
		double yPosition = size * 1/ scale + runsScored;
		List<DataPoint> wicketDataPoints = new ArrayList<>();

		for(int i=0; i<numWickets; i++) {
			wicketDataPoints.add(new DataPoint(xPosition, yPosition + scale/size * i));
		}

		return wicketDataPoints;
	}
}
