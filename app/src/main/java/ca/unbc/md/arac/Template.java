package ca.unbc.md.arac;

import java.io.File;
import java.util.ArrayList;

import android.content.res.Resources.Theme;
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Log;
import ca.unbc.md.arac.R;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.EDEBUG_VISIBILITY;
import com.metaio.sdk.jni.ETRACKING_STATE;
import com.metaio.sdk.jni.EVISUAL_SEARCH_STATE;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKAndroid;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IVisualSearchCallback;
import com.metaio.sdk.jni.ImageStruct;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.sdk.jni.VisualSearchResponseVector;
import com.metaio.tools.io.AssetsManager;

public class Template extends ARViewActivity {
    private MetaioSDKCallbackHandler mSDKCallback;
    private VisualSearchCallbackHandler mVisualSearchCallback;


    // / Must provide all marker position offsets in setup, or exception will be thrown
    private final int number_of_tracking_markers = 10;
    private TrackingMarker[] tracking_markers = new TrackingMarker[number_of_tracking_markers];

    private String tracking_configuration_filename = "TrackingData_10_Marker.xml";
    private String geometry_filename = "wood_block_3_holes.zip";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mEarthOpened = false;

        mSDKCallback = new MetaioSDKCallbackHandler();
        mVisualSearchCallback = new VisualSearchCallbackHandler();

        if (metaioSDK != null) {
            metaioSDK.registerVisualSearchCallback(mVisualSearchCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSDKCallback.delete();
        mSDKCallback = null;
        mVisualSearchCallback.delete();
        mVisualSearchCallback = null;
    }

    @Override
    protected int getGUILayout() {
        // Attaching layout to the activity
        return R.layout.template;
    }

    @Override
    protected void loadContents() {
        try {
            setupTracking();
        } catch (Exception e) {
            MetaioDebug.log(Log.ERROR, "Failed to load content: " + e);
        }
    }

    protected void setupTracking() throws Exception {

        // Getting a file path for tracking configuration XML file
        final File trackingConfigFile = AssetsManager.getAssetPathAsFile(
                getApplicationContext(), tracking_configuration_filename);

        // Assigning tracking configuration
        boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
        MetaioDebug.log("Tracking data loaded: " + result);

        // Getting a file path for a 3D geometry
        final File model_file = AssetsManager.getAssetPathAsFile(
                getApplicationContext(), geometry_filename);

        if (model_file != null) {

            TrackingMarker tracking_marker;
            IGeometry design_object;
            TrackingMarkerPosition marker_position;

            for (int i = 1; i <= number_of_tracking_markers; i++) {

                // Loading 3D geometry
                design_object = metaioSDK.createGeometry(model_file);

                if (design_object != null) {

                    // design_object.setScale(scale);
                    // design_object.setOcclusionMode(true);
                    // design_object.setDebugDataVisibility(EDEBUG_VISIBILITY.EDV_FULL)

                    design_object.setCoordinateSystemID(i);
                    design_object.setName("" + i);
                    design_object.setVisible(false);
                    design_object.setTransparency(0.5f);

                    // Set the translation offsets and rotation for each
                    // specific marker ID.
                    Rotation rotation = new Rotation(new Vector3d(
                            (float) Math.PI / 2, 0.0f, 0.0f));


                    // Offset Configuration for square tool:
//					switch (i) {
//					case 1:
//						marker_position = new TrackingMarkerPosition(30, 17, 1,
//								rotation);
//						break;
//					case 2:
//						marker_position = new TrackingMarkerPosition(30, -172,
//								1, rotation);
//						break;
//					case 3:
//						marker_position = new TrackingMarkerPosition(30, -365,
//								1, rotation);
//						break;
//					case 4:
//						marker_position = new TrackingMarkerPosition(30, -548,
//								1, rotation);
//						break;
//					case 5:
//						marker_position = new TrackingMarkerPosition(-153, 19,
//								1, rotation);
//						break;
//					case 6:
//						marker_position = new TrackingMarkerPosition(-335, 18,
//								1, rotation);
//						break;
//					default:
//						throw new Exception(
//								"A Marker ID's position offset was not specified in setupTracking()");
//					}


                    // Offset configuration for workbench space
                    switch (i) {
                        case 1:
                            marker_position = new TrackingMarkerPosition(62, 62, 1,
                                    rotation);
                            break;
                        case 2:
                            marker_position = new TrackingMarkerPosition(62, -203,
                                    1, rotation);
                            break;
                        case 3:
                            marker_position = new TrackingMarkerPosition(62, -467,
                                    1, rotation);
                            break;
                        case 4:
                            marker_position = new TrackingMarkerPosition(-202, -467,
                                    1, rotation);
                            break;
                        case 5:
                            marker_position = new TrackingMarkerPosition(-484, -468,
                                    1, rotation);
                            break;
                        case 6:
                            marker_position = new TrackingMarkerPosition(-773, -469,
                                    1, rotation);
                            break;
                        case 7:
                            marker_position = new TrackingMarkerPosition(-773, -192,
                                    1, rotation);
                            break;
                        case 8:
                            marker_position = new TrackingMarkerPosition(-773, 62,
                                    1, rotation);
                            break;
                        case 9:
                            marker_position = new TrackingMarkerPosition(-485, 62,
                                    1, rotation);
                            break;
                        case 10:
                            marker_position = new TrackingMarkerPosition(-202, 62,
                                    1, rotation);
                            break;
                        default:
                            throw new Exception(
                                    "A Marker ID's position offset was not specified in setupTracking()");
                    }


                    // Set No marker offsets:
					/*
					 * marker_position = new TrackingMarkerPosition(0, 0, 1,
					 * rotation);
					 */



                    design_object.setTranslation(new Vector3d(
                            marker_position.x_offset, marker_position.y_offset,
                            marker_position.z_offset));
                    design_object.setRotation(marker_position.rotation);

                    tracking_marker = new TrackingMarker(i, design_object);

                    // Add reference of marker to our list.
                    tracking_markers[i - 1] = tracking_marker;

                } else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "
                            + design_object);
            }
        }
    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {
		/*
		 * MetaioDebug.log("Template.onGeometryTouched: " + geometry);
		 *
		 * if (geometry != mEarthOcclusion) { if (!mEarthOpened) {
		 * mEarth.startAnimation("Open", false);
		 * mEarthIndicators.startAnimation("Grow", false); mEarthOpened = true;
		 * } else { mEarth.startAnimation("Close", false);
		 * mEarthIndicators.startAnimation("Shrink", false); mEarthOpened =
		 * false; } }
		 */

    }

    // Setup auto-focus
    // public void onSurfaceCreated() {
    //
    // android.hardware.Camera camera = IMetaioSDKAndroid.getCamera(this);
    // android.hardware.Camera.Parameters params = camera.getParameters();
    // camera.setParameters(params);
    //
    // }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mSDKCallback;
    }

    final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {

        @Override
        public void onTrackingEvent(TrackingValuesVector trackingValues) {
            System.out.println("----Tracking Event Occurred----");

            // First, Update all tracking statuses from trackingValues vector
            for (int tracking_values_index = 0; tracking_values_index < trackingValues.size(); tracking_values_index++) {

                // Extract marker tracking information from TrackingValuesVector
                final TrackingValues values = trackingValues.get(tracking_values_index);
                int marker_id = trackingValues.get(tracking_values_index).getCoordinateSystemID(); // ID's start from 1.

                // Update tracking data based on the TrackingValuesVector attributes
                tracking_markers[marker_id - 1].tracking_state = values.isTrackingState();
                tracking_markers[marker_id - 1].tracking_quality = values.getQuality();

                //Logging:
                System.out.println("Tracking marker: " + marker_id);
                System.out.println("Tracking state: " + values.isTrackingState());
                System.out.println("Tracking quality: " + values.getQuality());
            }

            // Next, Determine the marker with the best tracking quality
            double minimum_quality_threshhold = 0.0; // [0,1]
            double best_tracking_quality = 0.0;
            int best_quality_index = -1;

            for (int i = 0; i < tracking_markers.length; i++) {
                tracking_markers[i].tracking_state = false;
                tracking_markers[i].marker_3d_content.setVisible(false);
                if (tracking_markers[i].tracking_quality >= minimum_quality_threshhold
                        && tracking_markers[i].tracking_quality > best_tracking_quality) {
                    best_tracking_quality = tracking_markers[i].tracking_quality;
                    best_quality_index = i;
                }
            }

            // Finally, Set the highest quality tracking marker's geometry to visible.
            if (best_quality_index != -1) {
                tracking_markers[best_quality_index].marker_3d_content.setVisible(true);

                // Logging:
                System.out.println("Chosen Tracking Marker: "+ (best_quality_index + 1));
                System.out.println("Chosen Marker's Tracking Quality: " + best_tracking_quality);

            } else {
                // ...Do Something in the UI to show the user that there is insufficient tracking quality
                System.out.println("Not Tracking: All tracking quality values are less than the threshold of: " + minimum_quality_threshhold);
            }
            System.out.println("----End of Tracking Event----");
        }

        @Override
        public void onSDKReady() {
            MetaioDebug.log("The SDK is ready");
        }

        @Override
        public void onAnimationEnd(IGeometry geometry, String animationName) {
            MetaioDebug.log("animation ended" + animationName);
        }

        @Override
        public void onMovieEnd(IGeometry geometry, File filePath) {
            MetaioDebug.log("movie ended" + filePath.getPath());
        }

        @Override
        public void onNewCameraFrame(ImageStruct cameraFrame) {
            MetaioDebug.log("a new camera frame image is delivered"
                    + cameraFrame.getTimestamp());
        }

        @Override
        public void onCameraImageSaved(File filePath) {
            MetaioDebug.log("a new camera frame image is saved to"
                    + filePath.getPath());
        }

        @Override
        public void onScreenshotImage(ImageStruct image) {
            MetaioDebug.log("screenshot image is received"
                    + image.getTimestamp());
        }

        @Override
        public void onScreenshotSaved(File filePath) {
            MetaioDebug
                    .log("screenshot image is saved to" + filePath.getPath());
        }

        @Override
        public void onInstantTrackingEvent(boolean success, File filePath) {
            if (success) {
                MetaioDebug.log("Instant 3D tracking is successful");
            }
        }
    }

    final class VisualSearchCallbackHandler extends IVisualSearchCallback {
        @Override
        public void onVisualSearchResult(VisualSearchResponseVector response,
                                         int errorCode) {
            if (errorCode == 0) {
                MetaioDebug.log("Visual search is successful");
            }
        }

        @Override
        public void onVisualSearchStatusChanged(EVISUAL_SEARCH_STATE state) {
            MetaioDebug.log("The current visual search state is: " + state);
        }
    }
}



