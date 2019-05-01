package app.facedetection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import app.facedetection.camera.CameraSourcePreview;
import app.facedetection.camera.FaceGraphic;
import app.facedetection.camera.GraphicOverlay;
import app.facedetection.db.DatabaseManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by Satish on 29/04/2019 5:22 PM.
 */
public class MainActivity  extends AppCompatActivity {

    private static final String TAG = "MarkAttendance";

    AlertDialog.Builder builder;
    AlertDialog dialog;
    private Context xContext = this;
    private boolean isTakingSelfie = true;
    private FaceDetector mDetector;
    private CameraSource mCameraSource = null;
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private SharedPreferences pre;
    String roll;
    Bundle bundle;
    DatabaseManager mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        mDb=new DatabaseManager(MainActivity.this);
        if (extras != null) {
            roll = extras.getString("id");
            Log.e(TAG, "onCreate:id " +roll );
        }

        mPreview = (CameraSourcePreview) findViewById(R.id.camera_source_preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.graphic_overlay);
// send attandance after class end


    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPreview.stop();
        if (mCameraSource != null) {
            mCameraSource.release();
            mDetector.release();
        }
    }

    /**
     * Initialize camera with face detection
     */
    private void initCamera() {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(xContext);
        if (code != ConnectionResult.SUCCESS)
            GoogleApiAvailability.getInstance().getErrorDialog(this, code, 13).show();
        else {
            mDetector = new FaceDetector.Builder(xContext)
                    .setClassificationType(FaceDetector.NO_CLASSIFICATIONS)
                    .setMode(FaceDetector.ACCURATE_MODE)
                    .setLandmarkType(FaceDetector.NO_LANDMARKS)
                    .setProminentFaceOnly(true)
                    .setTrackingEnabled(true)
                    .build();

            mDetector.setProcessor(new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory()).build());

            if (mDetector.isOperational()) {
                mCameraSource = new CameraSource.Builder(xContext, mDetector)
                        .setRequestedPreviewSize(640, 480)
                        .setFacing(1)
                        .setAutoFocusEnabled(true)
                        //.s
                        .setRequestedFps(25.0f)
                        .build();

                try {
                    mPreview.stop();
                    mPreview.start(mCameraSource, mGraphicOverlay);
                    mPreview.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Log.e(TAG, "Unable to start camera source.\n" + e);
                    mCameraSource.release();
                    mCameraSource = null;
                }
            } else {
                Log.e(TAG, "Face detector dependencies are not yet available.");
                Toast.makeText(xContext, "Face detection not available and please reboot you phone",
                        Toast.LENGTH_SHORT);
            }
        }
    }


    class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
        }

        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, final Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);
            float y = face.getEulerY();
            float z = face.getEulerZ();
            if ((y <= 6.0 && y >= -6.0) && (z <= 6.0 && z >= -6.0))
                if (isTakingSelfie) {
                    mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
                        @Override
                        public void onPictureTaken(final byte[] bytes) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    save(bytes);
                                    mDb.updateAttend(roll);
                                    long fileSizeInKB = bytes.length / 1024;
                                    Log.e(TAG, "run:bytes " + fileSizeInKB);
                                }
                            });
                        }
                    });
                    isTakingSelfie = false;
                }
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
        }

        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }

    public void save(byte[] src) {


        // mInSelfie = StringLoginDao dao = xStore.getData();.format("%s_%s_%s_%s.jpg", mClassId, mProfilePreference.getUserId(), mDate, time.replace(":", "_"));

        String imageName = String.format("names.jpg");


        String mSavedSelfiePath = Files.saveSelfie(getApplicationContext(), src, imageName);

        finish();


    }
}
