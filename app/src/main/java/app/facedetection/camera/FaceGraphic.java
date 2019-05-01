package app.facedetection.camera;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.google.android.gms.vision.face.Face;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
public class FaceGraphic extends GraphicOverlay.Graphic {

    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private Paint mBoxPaint;

    private volatile Face mFace;

    public FaceGraphic(GraphicOverlay overlay) {
        super(overlay);
        Paint mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(Color.RED);

        Paint mIdPaint = new Paint();
        mIdPaint.setColor(Color.RED);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(Color.RED);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    public void setId(int id) {
//        int mFaceId = id;
    }

    /**
     * Updates the face instance from the detection of the most recent frame. Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    public void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) return;

        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);

        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;

        float eulerY = face.getEulerY();
        float eulerZ = face.getEulerZ();

        if ((eulerY <= 8.0 && eulerY >= -8.0) && (eulerZ <= 8.0 && eulerZ >= -8.0))
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
    }
}
