package com.example.barcodescanner.visionsample;


import com.example.barcodescanner.visionsample.ui.GraphicOverlay;

abstract class TrackedGraphic<T> extends GraphicOverlay.Graphic {
    private int mId;

    TrackedGraphic(GraphicOverlay overlay) {super(overlay);}

    void setId(int id) {mId = id;}

    protected int getId() {return mId;}

    abstract void updateItem(T item);
}