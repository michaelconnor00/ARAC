package ca.unbc.md.arac;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.EVISUAL_SEARCH_STATE;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IVisualSearchCallback;
import com.metaio.sdk.jni.ImageStruct;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.VisualSearchResponseVector;
import com.metaio.tools.io.AssetsManager;

public class Template extends ARViewActivity {

    boolean DEBUG_OUT = false;

    private MetaioSDKCallbackHandler mSDKCallback;
    private VisualSearchCallbackHandler mVisualSearchCallback;
    private ImageButton settings_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            System.exit(0);
        }
    }


    public void launchCalibration(View view) {
        Intent intent = new Intent(this, CalibrationActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void setupTracking() throws Exception {

        // Retrieve the tracking xmk configuration filename for the selected tool: // TODO make tracking file index variable
        String tracking_configuration_filename = AppGlobal.current_physical_alignment_tool.tool_tracking_xml_filenames.get(0);

        // Getting a file path for tracking configuration XML file:
        final File trackingConfigFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), tracking_configuration_filename);

        MetaioDebug.log("------------------------TRACKING CONFIG: " + tracking_configuration_filename);

        // Assigning tracking configuration
        boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
        MetaioDebug.log("Tracking data loaded: " + result);


        String geometry_filename = AppGlobal.current_geometry_filename;
        final File model_file = AssetsManager.getAssetPathAsFile(getApplicationContext(), geometry_filename);

        if (model_file != null) {
            PhysicalAlignmentToolConfiguration physical_alignment_tool_configuration = AppGlobal.physical_alignment_tool_configuration;
            physical_alignment_tool_configuration.configure_alignment_tool_by_id(AppGlobal.current_physical_alignment_tool.get_tool_name(), metaioSDK, model_file, is_file_type_image(geometry_filename));
        }

    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {

    }


    // Currently, we will only support .png images
    protected boolean is_file_type_image(String content_filename) {
        String file_extension = content_filename.substring(content_filename.indexOf('.'));
        if (file_extension.equalsIgnoreCase(".png")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mSDKCallback;
    }


    //////////////////// START Public Interface ////////////////////////////////////////////////////
/*
    public void run_tracking_setup()throws Exception{
        setupTracking();
        metaioSDK.startCamera(); //TODO  Need to test this
    }

    public void terminate_tracking_processes(){
        metaioSDK.stopCamera(); //TODO Need to test this
    }
    */

    //////////////////// END Public Interface //////////////////////////////////////////////////////


    /*
    Handles all the call back events from the Metaio SDK
     */
    final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {


        /**
         * Fires whenever a tracking event occurs in the MetaioSDK.  Events include: tracking marker
         * entering scene, tracking marker leaving scene, ...
         *
         * @param trackingValues
         */
        @Override
        public void onTrackingEvent(TrackingValuesVector trackingValues) {

            if (DEBUG_OUT)
                System.out.println("----Tracking Event Occurred----");

            // First, Update all tracking statuses from trackingValues vector
            for (int tracking_values_index = 0; tracking_values_index < trackingValues.size(); tracking_values_index++) {

                // Extract marker tracking information from TrackingValuesVector
                final TrackingValues values = trackingValues.get(tracking_values_index);
                int marker_id = trackingValues.get(tracking_values_index).getCoordinateSystemID(); // ID's start from 1.

                // Update tracking data based on the TrackingValuesVector attributes
                AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(marker_id - 1).tracking_state = values.isTrackingState();
                AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(marker_id - 1).tracking_quality = values.getQuality();

                //Logging:
                if (DEBUG_OUT) {
                    System.out.println("Tracking marker: " + marker_id);
                    System.out.println("Tracking state: " + values.isTrackingState());
                    System.out.println("Tracking quality: " + values.getQuality());
                }
            }

            // Next, Determine the marker with the best tracking quality
            double minimum_quality_thresh = 0.0; // Range: [0, 1]
            double best_tracking_quality = 0.0;
            int best_quality_index = -1;

            for (int i = 0; i < AppGlobal.current_physical_alignment_tool.tool_tracking_markers.size(); i++) {
                TrackingMarker current_tracking_marker = AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(i);
                current_tracking_marker.tracking_state = false;
                current_tracking_marker.marker_3d_content.setVisible(false);
                if (current_tracking_marker.tracking_quality > minimum_quality_thresh && current_tracking_marker.tracking_quality > best_tracking_quality) {
                    best_tracking_quality = current_tracking_marker.tracking_quality;
                    best_quality_index = i;
                }
            }

            // Finally, Set the highest quality tracking marker's geometry to visible.
            if (best_quality_index != -1) {
                AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(best_quality_index).marker_3d_content.setVisible(true);

                // Logging:
                if (DEBUG_OUT) {
                    System.out.println("Chosen Tracking Marker: " + (best_quality_index + 1));
                    System.out.println("Chosen Marker's Tracking Quality: " + best_tracking_quality);
                }

            } else {
                // ...Do Something in the UI to show the user that there is insufficient tracking quality
                if (DEBUG_OUT)
                    System.out.println("Not Tracking: All tracking quality values are less than the threshold of: " + minimum_quality_thresh);
            }
            if (DEBUG_OUT)
                System.out.println("----End of Tracking Event----");
        }

        @Override
        public void onSDKReady() {

            MetaioDebug.log("The SDK is ready");

            // Turn settings_button visible
            settings_button = (ImageButton) findViewById(R.id.tracking_settings_button);
            new ToggleButtonVisible().execute();

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
            } else {

            }
        }

        @Override
        public void onVisualSearchStatusChanged(EVISUAL_SEARCH_STATE state) {
            MetaioDebug.log("The current visual search state is: " + state);
        }
    }


    ///////////////  ASYNC_TASKS  //////////////////////

    private class ToggleButtonVisible extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... agr0) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            try{
                settings_button.post(new Runnable() {
                    public void run() {
                        Log.d("--ARAC--", "Set button visible");
//                        settings_button.requestFocus();
                        settings_button.setVisibility(View.VISIBLE);
                        settings_button.bringToFront();
                    }});
//                settings_button.invalidate();
            }catch (Exception e) {
                Log.d("ARAC", e.toString());
            }

//            // Check if the button is visible, otherwise make visible
//            if (settings_button.getVisibility() == View.VISIBLE) {
//                Log.d("--ARAC--", "Set button invisible");
//                settings_button.setVisibility(View.INVISIBLE);
//            } else {
//                Log.d("--ARAC--", "Set button visible");
//                settings_button.setVisibility(View.VISIBLE);
//            }
        }

    }
}