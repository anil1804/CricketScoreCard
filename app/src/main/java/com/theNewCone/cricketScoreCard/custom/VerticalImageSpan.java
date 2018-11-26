package com.theNewCone.cricketScoreCard.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ImageSpan;

public class VerticalImageSpan extends ImageSpan {
	private int width, height;

	public VerticalImageSpan(Drawable drawable, int height, int width) {
		super(drawable);
		this.height = height;
		this.width = width;
	}

	/**
	 * update the text line height
	 */
	@Override
	public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
		Drawable drawable = getDrawable();

		if(width > 0 & height > 0)
			drawable.setBounds(0, 0, width, height);

		Rect rect = drawable.getBounds();
		if (fontMetricsInt != null) {
			Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
			int fontHeight = fmPaint.descent - fmPaint.ascent;
			int drHeight = rect.bottom - rect.top;
			int centerY = fmPaint.ascent + fontHeight / 2;

			fontMetricsInt.ascent = centerY - drHeight / 2;
			fontMetricsInt.top = fontMetricsInt.ascent;
			fontMetricsInt.bottom = centerY + drHeight / 2;
			fontMetricsInt.descent = fontMetricsInt.bottom;
		}
		return rect.right;
	}

	/**
	 * see detail message in android.text.TextLine
	 *
	 * @param canvas the canvas, can be null if not rendering
	 * @param text the text to be draw
	 * @param start the text start position
	 * @param end the text end position
	 * @param x the edge of the replacement closest to the leading margin
	 * @param top the top of the line
	 * @param y the baseline
	 * @param bottom the bottom of the line
	 * @param paint the work paint
	 */
	@Override
	public void draw(@Nullable Canvas canvas, CharSequence text, int start, int end,
					 float x, int top, int y, int bottom, @NonNull Paint paint) {

		Drawable drawable = getDrawable();
		if(canvas != null)
			canvas.save();
		Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
		int fontHeight = fmPaint.descent - fmPaint.ascent;
		int centerY = y + fmPaint.descent - fontHeight / 2;
		int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;
		if(canvas != null) {
			canvas.translate(x, transY);
			drawable.draw(canvas);
			canvas.restore();
		}
	}
}
