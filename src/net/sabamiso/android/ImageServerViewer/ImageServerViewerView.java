package net.sabamiso.android.ImageServerViewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.View;

public class ImageServerViewerView extends View {

	Bitmap bitmap;
	String uri = "";
	String key = "";

	int last_status = -1;

	Paint paint = new Paint();
	Handler handler = new Handler();

	public ImageServerViewerView(Context context) {
		super(context);
	}

	public void setURI(String uri) {
		this.uri = uri;
	}

	public void setLastStatus(int last_status) {
		this.last_status = last_status;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;

		handler.post(new Runnable() {
			@Override
			public void run() {
				invalidate();
			}
		});
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		Log.d("ImageServerViewer", "onDraw()");

		if (this.bitmap != null) {
			Rect src = new Rect(0, 0, this.bitmap.getWidth(),
					this.bitmap.getHeight());
			Rect dst = new Rect(0, 0, getWidth(), getHeight());

			canvas.drawBitmap(bitmap, src, dst, paint);
		}

		String uri_msg = "post uri: " + uri;
		drawText(uri_msg, 10, 70, 36, canvas);

		String status_msg = "last status: " + last_status;
		drawText(status_msg, 10, 150, 36, canvas);

	}

	public void drawText(String msg, float x, float y, int size, Canvas canvas) {
		Paint p = new Paint();
		p.setTextSize(size);
		p.setColor(Color.BLACK);

		p.setTypeface(Typeface.DEFAULT_BOLD);

		for (int dy = -2; dy <= 2; dy += 2) {
			for (int dx = -2; dx <= 2; dx += 2) {
				canvas.drawText(msg, x + dx, y + dy, p);
			}
		}

		p.setColor(Color.WHITE);
		canvas.drawText(msg, x, y, p);
	}
}