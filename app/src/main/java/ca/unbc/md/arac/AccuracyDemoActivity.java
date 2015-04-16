package ca.unbc.md.arac;

import java.io.File;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.EVISUAL_SEARCH_STATE;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IVisualSearchCallback;
import com.metaio.sdk.jni.ImageStruct;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.sdk.jni.VisualSearchResponseVector;
import com.metaio.tools.io.AssetsManager;

public class AccuracyDemoActivity extends ARViewActivity {

    boolean DEBUG_OUT = true;

    private MetaioSDKCallbackHandler mSDKCallback;
    private VisualSearchCallbackHandler mVisualSearchCallback;
    private TextView t1;
    private float distance;
    private ArrayList<Float> distances;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        distances = new ArrayList<>();
        t1 = (TextView) findViewById(R.id.distance); // TextView for displaying distance

        mSDKCallback = new MetaioSDKCallbackHandler();
        mVisualSearchCallback = new VisualSearchCallbackHandler();

        if (metaioSDK != null) {
            metaioSDK.registerVisualSearchCallback(mVisualSearchCallback);
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
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
        return R.layout.activity_accuracy_demo;
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

    public void setupTracking() throws Exception {

        AppGlobal.physical_alignment_tool_configuration =  new PhysicalAlignmentToolManager();
        AppGlobal.current_physical_alignment_tool =
                AppGlobal.physical_alignment_tool_configuration.physical_alignment_tools.get("accuracy_demo");
        AppGlobal.current_geometry_filename = "Coloring_Book_Dog.png";

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
            PhysicalAlignmentToolManager physical_alignment_tool_configuration = AppGlobal.physical_alignment_tool_configuration;
            physical_alignment_tool_configuration.configure_alignment_tool_by_id(AppGlobal.current_physical_alignment_tool.get_tool_id(), metaioSDK, model_file, is_file_type_image(geometry_filename));
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

            t1 = (TextView) findViewById(R.id.distance);

            if(DEBUG_OUT)
                System.out.println("----Tracking Event Occurred----");
                System.out.println("Tracking size: " + trackingValues.size());

            TrackingValues pose1 = trackingValues.get(0);
            TrackingValues pose2 = trackingValues.get(1);

            System.out.println("Pose 1: " + pose1.getTranslation());
            System.out.println("Pose 2: " + pose2.getTranslation());

//            boolean success = metaioSDK.getCosRelation(1, 2, pose1);
//            if (success){
//                distance = pose1.getTranslation().norm();
//                new TextViewUpdate().execute();
//            }


            // First, Update all tracking statuses from trackingValues vector
            for (int tracking_values_index = 0; tracking_values_index < trackingValues.size(); tracking_values_index++) {

                // Extract marker tracking information from TrackingValuesVector
                final TrackingValues values = trackingValues.get(tracking_values_index);
                int marker_id = trackingValues.get(tracking_values_index).getCoordinateSystemID(); // ID's start from 1.


                // Calculate the distance between markers 1 and 2
                boolean success = metaioSDK.getCosRelation(1, 2, values);
                if (success){
                    distance = values.getTranslation().norm();

                    distances.add(distance);
                    new TextViewUpdate().execute();
                }

                // Update tracking data based on the TrackingValuesVector attributes
                AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(marker_id -1).tracking_state = values.isTrackingState();
                AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(marker_id -1).tracking_quality = values.getQuality();

                //Logging:
                if(DEBUG_OUT) {
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
                TrackingMarker current_tracking_marker =  AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(i);
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
                if(DEBUG_OUT) {
                    System.out.println("Chosen Tracking Marker: " + (best_quality_index + 1));
                    System.out.println("Chosen Marker's Tracking Quality: " + best_tracking_quality);
                }

            } else {
                // ...Do Something in the UI to show the user that there is insufficient tracking quality
                if(DEBUG_OUT)
                    System.out.println("Not Tracking: All tracking quality values are less than the threshold of: " + minimum_quality_thresh);
            }
            if(DEBUG_OUT)
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
            else{

            }
        }

        @Override
        public void onVisualSearchStatusChanged(EVISUAL_SEARCH_STATE state) {
            MetaioDebug.log("The current visual search state is: " + state);
        }
    }


    ///////////////  ASYNC_TASKS  //////////////////////

    private class TextViewUpdate extends AsyncTask<Void, Void, Boolean> {

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
                t1.post(new Runnable() {
                    public void run() {
                        Log.d("--ARAC--", "Text Updated");

                        // Calculate the average:
                        float average = 0;
                        float sum = 0;
                        for(int i = 0 ; i < distances.size() ; i++){
                            sum += distances.get(i);
                        }
                        average = sum / distances.size();

                        t1.setText(Float.toString(average));
                    }});
            }catch (Exception e) {
                Log.d("ARAC", e.toString());
            }

        }

    }

}
