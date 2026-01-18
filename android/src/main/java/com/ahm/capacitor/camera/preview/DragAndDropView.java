package com.ahm.capacitor.camera.preview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DragAndDropView extends View {

    private List<Shape> shapes = new ArrayList<>();
    private Paint paint;
    private Shape activeShape = null;
    private PointF lastTouch = new PointF();

    public DragAndDropView(Context context) {
        super(context);
        init();
    }

    public DragAndDropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public void addShape(String type, int color) {
        Shape shape = new Shape(type, color);
        // Default position center-ish
        shape.rect = new RectF(100, 100, 300, 300);
        shapes.add(shape);
        invalidate();
    }

    public void clearShapes() {
        shapes.clear();
        invalidate();
    }
    

    public void drawOnCanvas(Canvas canvas) {
        drawShapes(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawShapes(canvas);
    }
    
    private void drawShapes(Canvas canvas) {
         for (Shape shape : shapes) {
            paint.setColor(shape.color);
            if ("square".equalsIgnoreCase(shape.type)) {
                canvas.drawRect(shape.rect, paint);
            }
            // Maybe need to add more shapes 
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = shapes.size() - 1; i >= 0; i--) {
                    Shape s = shapes.get(i);
                    if (s.rect.contains(x, y)) {
                        activeShape = s;
                        lastTouch.set(x, y);
                        shapes.remove(i);
                        shapes.add(s);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (activeShape != null) {
                    float dx = x - lastTouch.x;
                    float dy = y - lastTouch.y;
                    activeShape.rect.offset(dx, dy);
                    lastTouch.set(x, y);
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                activeShape = null;
                break;
        }
        return super.onTouchEvent(event);
    }

    private static class Shape {
        String type;
        int color;
        RectF rect;

        Shape(String type, int color) {
            this.type = type;
            this.color = color;
        }
    }
}
