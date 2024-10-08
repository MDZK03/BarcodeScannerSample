package com.example.barcodescanner.visionsample;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import com.example.barcodescanner.visionsample.ui.GraphicOverlay;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

class FaceTrackerFactory implements MultiProcessor.Factory<Face> {
    private final GraphicOverlay mGraphicOverlay;

    FaceTrackerFactory(GraphicOverlay graphicOverlay) {mGraphicOverlay = graphicOverlay;}

    @NonNull
    @Override
    public Tracker<Face> create(@NonNull Face face) {
        FaceGraphic graphic = new FaceGraphic(mGraphicOverlay);
        return new GraphicTracker<>(mGraphicOverlay, graphic);
    }
}

class FaceGraphic extends TrackedGraphic<Face> {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int[] COLOR_CHOICES = {Color.MAGENTA, Color.RED, Color.YELLOW};
    private static int mCurrentColorIndex = 0;

    private final Paint mFacePositionPaint;
    private final Paint mIdPaint;
    private final Paint mBoxPaint;

    private volatile Face mFace;

    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void updateItem(Face face) {
        mFace = face;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float cx = translateX(face.getPosition().x + face.getWidth() / 2);
        float cy = translateY(face.getPosition().y + face.getHeight() / 2);
        canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mFacePositionPaint);
        canvas.drawText("id: " + getId(), cx + ID_X_OFFSET, cy + ID_Y_OFFSET, mIdPaint);

        // Draws an oval around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = cx - xOffset;
        float top = cy - yOffset;
        float right = cx + xOffset;
        float bottom = cy + yOffset;
        canvas.drawOval(left, top, right, bottom, mBoxPaint);
    }
}