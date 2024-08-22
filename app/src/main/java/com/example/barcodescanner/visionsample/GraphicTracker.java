package com.example.barcodescanner.visionsample;

import androidx.annotation.NonNull;
import com.example.barcodescanner.visionsample.ui.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;

class GraphicTracker<T> extends Tracker<T> {
    private final GraphicOverlay mOverlay;
    private final TrackedGraphic<T> mGraphic;

    GraphicTracker(GraphicOverlay overlay, TrackedGraphic<T> graphic) {
        mOverlay = overlay;
        mGraphic = graphic;
    }

    @Override
    public void onNewItem(int id, @NonNull T item) {mGraphic.setId(id);}

    @Override
    public void onUpdate(@NonNull Detector.Detections<T> detectionResults, @NonNull T item) {
        mOverlay.add(mGraphic);
        mGraphic.updateItem(item);
    }

    @Override
    public void onMissing(@NonNull Detector.Detections<T> detectionResults) {mOverlay.remove(mGraphic);}

    @Override
    public void onDone() {mOverlay.remove(mGraphic);}
}