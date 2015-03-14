package ca.unbc.md.arac;

import android.util.Log;

import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKAndroid;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.Vector3d;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dev on 3/10/2015.
 *
 */
public class PhysicalAlignmentToolConfiguration {


    // Maintains a list of all the specified alignment tools:
    // String = Key = Tool ID Name
    // PhysicalAlignmentTool = The tool object
    Map<String, PhysicalAlignmentTool> physical_alignment_tools = new HashMap<String,PhysicalAlignmentTool>();


    // Global Geometry Parameters:
    float global_geometry_transparency = 0.5f;
    float global_geometry_scale = 1.0f;


    public PhysicalAlignmentToolConfiguration(){

        // Initialize all physical tools:
        try {
            initialize_square_edge_tool();
            initialize_augmented_workspace_tool();
            initialize_A4_drawing_canvas_tool();
        }
        catch(Exception e){
            //TODO : add better error handling here..
            // There was an error in tool initialization
            System.exit(0);
        }

    }



    /****** TOOL Initialization - These Do Not Set The Geometries or Offsets *******/

    private void initialize_square_edge_tool() throws Exception{

        // This tool unique ID:
        String tool_id = "Square Edge";

        // Specify compatible tracking.xml configuration files:
        ArrayList<String> tracking_configuration_filenames = new ArrayList<String>();
        tracking_configuration_filenames.add("Square_Tool_TrackingData_6_Marker.xml");
        tracking_configuration_filenames.add("Square_Tool_TrackingData_6_Marker_Medium_Smoothing_Fuser.xml");
        tracking_configuration_filenames.add("Square_Tool_TrackingData_6_Marker_Heavy_Smoothing_Fuser.xml");

        // Initialize the tool:
        PhysicalAlignmentTool square_edge_tool = new PhysicalAlignmentTool(tool_id, null, tracking_configuration_filenames);

        // Add the tool to the list of tools:
        physical_alignment_tools.put(tool_id, square_edge_tool);
    }


    private void initialize_augmented_workspace_tool(){

        // This tool unique ID:
        String tool_id = "Augmented Workspace";

        // Specify compatible tracking.xml configuration files:
        ArrayList<String> tracking_configuration_filenames = new ArrayList<String>();
        tracking_configuration_filenames.add("Augmented_Workspace_TrackingData_10_Marker.xml");
        tracking_configuration_filenames.add("Augmented_Workspace_TrackingData_10_Marker_Medium_Smoothing_Fuser.xml");
        tracking_configuration_filenames.add("Augmented_Workspace_TrackingData_10_Marker_Heavy_Smoothing_Fuser.xml");

        // Initialize the tool:
        PhysicalAlignmentTool augmented_workspace_tool = new PhysicalAlignmentTool(tool_id, null, tracking_configuration_filenames);

        // Add the tool to the list of tools:
        physical_alignment_tools.put(tool_id, augmented_workspace_tool);
    }



    private void initialize_A4_drawing_canvas_tool(){

        // This tool unique ID:
        String tool_id = "A4 Drawing Canvas";

        // Specify compatible tracking.xml configuration files:
        // TODO add tracking config files
        ArrayList<String> tracking_configuration_filenames = new ArrayList<String>();
        //tracking_configuration_filenames.add("Augmented_Workspace_TrackingData_10_Marker.xml");
        //tracking_configuration_filenames.add("Augmented_Workspace_TrackingData_10_Marker_Medium_Smoothing_Fuser.xml");
        //tracking_configuration_filenames.add("Augmented_Workspace_TrackingData_10_Marker_Heavy_Smoothing_Fuser.xml");

        // Initialize the tool:
        PhysicalAlignmentTool augmented_workspace_tool = new PhysicalAlignmentTool(tool_id, null, tracking_configuration_filenames);

        // Add the tool to the list of tools:
        physical_alignment_tools.put(tool_id, augmented_workspace_tool);
    }









    /****** TOOL CONFIGURATIONS - Set the tool Geometries, Offsets, Etc...  *******/

    public void configure_alignment_tool_by_id(String tool_id, IMetaioSDKAndroid metaioSDK, File model_file, boolean isGeometryAnImage) throws Exception{
        switch(tool_id){
            case "Square Edge":
                configure_square_edge_tool(metaioSDK, model_file, isGeometryAnImage);
                break;
            case "Augmented Workspace":
                configure_augmented_workspace_tool(metaioSDK, model_file, isGeometryAnImage);
                break;
            case "A4 Drawing Canvas":
                configure_A4_drawing_canvas_tool(metaioSDK, model_file, isGeometryAnImage);
                break;
        }
    }

    public void configure_square_edge_tool(IMetaioSDKAndroid metaioSDK, File model_file, boolean isGeometryAnImage) throws Exception{

        ArrayList<TrackingMarker> tracking_markers = new ArrayList<TrackingMarker>();

        // Specify tracking marker configurations:
        int number_of_tracking_markers = 6;
        TrackingMarker tracking_marker;
        IGeometry geometry;
        TrackingMarkerPosition marker_position;

        for (int i = 1; i <= number_of_tracking_markers; i++) {

            // Loading 3D geometry
            geometry = metaioSDK.createGeometryFromImage(model_file);

            if (geometry != null) {
                geometry.setCoordinateSystemID(i);
                geometry.setName("" + i);
                geometry.setVisible(false);
                geometry.setTransparency(global_geometry_transparency);
                geometry.setScale(global_geometry_scale);

                // Set the translation offsets and rotation for each specific marker ID.
                Rotation rotation;
                if(isGeometryAnImage) {
                    rotation = new Rotation(new Vector3d(
                            (float) 0.0f, 0.0f, 0.0f));
                }
                else{
                    rotation = new Rotation(new Vector3d(
                            (float) Math.PI / 2, 0.0f, 0.0f));
                }



                // Offset Configuration for square tool:
                switch (i) {
                    case 1:
                        marker_position = new TrackingMarkerPosition(30, 17, 1,
                                rotation);
                        break;
                    case 2:
                        marker_position = new TrackingMarkerPosition(30, -172,
                                1, rotation);
                        break;
                    case 3:
                        marker_position = new TrackingMarkerPosition(30, -365,
                                1, rotation);
                        break;
                    case 4:
                        marker_position = new TrackingMarkerPosition(30, -548,
                                1, rotation);
                        break;
                    case 5:
                        marker_position = new TrackingMarkerPosition(-153, 19,
                                1, rotation);
                        break;
                    case 6:
                        marker_position = new TrackingMarkerPosition(-335, 18,
                                1, rotation);
                        break;
                    default:
                        throw new Exception(
                                "A Marker ID's position offset was not specified in setupTracking()");
                }

                geometry.setTranslation(new Vector3d(marker_position.x_offset, marker_position.y_offset, marker_position.z_offset));
                geometry.setRotation(marker_position.rotation);

                tracking_marker = new TrackingMarker(i, geometry);
                tracking_markers.add(tracking_marker);

            } else
                MetaioDebug.log(Log.ERROR, "Error loading geometry: " + geometry);
        }

        // Tool was previously initialized, so just update it with the configuration:
        PhysicalAlignmentTool square_edge_tool = physical_alignment_tools.get("Square Edge");
        square_edge_tool.tool_tracking_markers = tracking_markers;
    }





    public void configure_augmented_workspace_tool(IMetaioSDKAndroid metaioSDK, File model_file, boolean isGeometryAnImage) throws Exception{
        ArrayList<TrackingMarker> tracking_markers = new ArrayList<TrackingMarker>();

        // Specify tracking marker configurations:
        int number_of_tracking_markers = 10;
        TrackingMarker tracking_marker;
        IGeometry geometry;
        TrackingMarkerPosition marker_position;

        for (int i = 1; i <= number_of_tracking_markers; i++) {

            // Loading 3D geometry
            geometry = metaioSDK.createGeometryFromImage(model_file);

            if (geometry != null) {
                geometry.setCoordinateSystemID(i);
                geometry.setName("" + i);
                geometry.setVisible(false);
                geometry.setTransparency(global_geometry_transparency);
                geometry.setScale(global_geometry_scale);


                // Set the translation offsets and rotation for each specific marker ID.
                Rotation rotation;
                if (isGeometryAnImage) {
                    rotation = new Rotation(new Vector3d(
                            (float) 0.0f, 0.0f, 0.0f));
                } else {
                    rotation = new Rotation(new Vector3d(
                            (float) Math.PI / 2, 0.0f, 0.0f));
                }

                // Offset configuration for workbench space
                switch (i) {
                    case 1:
                        marker_position = new TrackingMarkerPosition(62, 62, 20,
                                rotation);
                        break;
                    case 2:
                        marker_position = new TrackingMarkerPosition(62, -203,
                                20, rotation);
                        break;
                    case 3:
                        marker_position = new TrackingMarkerPosition(62, -467,
                                20, rotation);
                        break;
                    case 4:
                        marker_position = new TrackingMarkerPosition(-202, -467,
                                20, rotation);
                        break;
                    case 5:
                        marker_position = new TrackingMarkerPosition(-484, -468,
                                20, rotation);
                        break;
                    case 6:
                        marker_position = new TrackingMarkerPosition(-773, -469,
                                20, rotation);
                        break;
                    case 7:
                        marker_position = new TrackingMarkerPosition(-773, -192,
                                20, rotation);
                        break;
                    case 8:
                        marker_position = new TrackingMarkerPosition(-773, 62,
                                20, rotation);
                        break;
                    case 9:
                        marker_position = new TrackingMarkerPosition(-485, 62,
                                20, rotation);
                        break;
                    case 10:
                        marker_position = new TrackingMarkerPosition(-202, 62,
                                20, rotation);
                        break;
                    default:
                        throw new Exception("A Marker ID's position offset was not specified in setupTracking()");
                }

                geometry.setTranslation(new Vector3d(marker_position.x_offset, marker_position.y_offset, marker_position.z_offset));
                geometry.setRotation(marker_position.rotation);

                tracking_marker = new TrackingMarker(i, geometry);
                tracking_markers.add(tracking_marker);

            } else
                MetaioDebug.log(Log.ERROR, "Error loading geometry: " + geometry);
        }

        // Tool was previously initialized, so just update it with the configuration:
        PhysicalAlignmentTool workspace_tool = physical_alignment_tools.get("Augmented Workspace");
        workspace_tool.tool_tracking_markers = tracking_markers;
    }

    public void configure_A4_drawing_canvas_tool(IMetaioSDKAndroid metaioSDK, File model_file, boolean isGeometryAnImage){

        /*
        ArrayList<TrackingMarker> tracking_markers = new ArrayList<TrackingMarker>();

        // Specify tracking marker configurations:
        int number_of_tracking_markers = 10;
        TrackingMarker tracking_marker;
        IGeometry geometry;
        TrackingMarkerPosition marker_position;

        for (int i = 1; i <= number_of_tracking_markers; i++) {

            // Loading 3D geometry
            geometry = metaioSDK.createGeometryFromImage(model_file);

            if (geometry != null) {
                geometry.setCoordinateSystemID(i);
                geometry.setName("" + i);
                geometry.setVisible(false);
                geometry.setTransparency(global_geometry_transparency);
                geometry.setScale(global_geometry_scale);


                // Set the translation offsets and rotation for each specific marker ID.
                Rotation rotation;
                if (isGeometryAnImage) {
                    rotation = new Rotation(new Vector3d(
                            (float) 0.0f, 0.0f, 0.0f));
                } else {
                    rotation = new Rotation(new Vector3d(
                            (float) Math.PI / 2, 0.0f, 0.0f));
                }

                // Offset configuration for workbench space
                switch (i) {
                    case 1:
                        marker_position = new TrackingMarkerPosition(62, 62, 20,
                                rotation);
                        break;
                    case 2:
                        marker_position = new TrackingMarkerPosition(62, -203,
                                20, rotation);
                        break;
                    case 3:
                        marker_position = new TrackingMarkerPosition(62, -467,
                                20, rotation);
                        break;
                    case 4:
                        marker_position = new TrackingMarkerPosition(-202, -467,
                                20, rotation);
                        break;
                    case 5:
                        marker_position = new TrackingMarkerPosition(-484, -468,
                                20, rotation);
                        break;
                    case 6:
                        marker_position = new TrackingMarkerPosition(-773, -469,
                                20, rotation);
                        break;
                    case 7:
                        marker_position = new TrackingMarkerPosition(-773, -192,
                                20, rotation);
                        break;
                    case 8:
                        marker_position = new TrackingMarkerPosition(-773, 62,
                                20, rotation);
                        break;
                    case 9:
                        marker_position = new TrackingMarkerPosition(-485, 62,
                                20, rotation);
                        break;
                    case 10:
                        marker_position = new TrackingMarkerPosition(-202, 62,
                                20, rotation);
                        break;
                    default:
                        throw new Exception("A Marker ID's position offset was not specified in setupTracking()");
                }

                geometry.setTranslation(new Vector3d(marker_position.x_offset, marker_position.y_offset, marker_position.z_offset));
                geometry.setRotation(marker_position.rotation);

                tracking_marker = new TrackingMarker(i, geometry);
                tracking_markers.add(tracking_marker);

            } else
                MetaioDebug.log(Log.ERROR, "Error loading geometry: " + geometry);
        }

        // Tool was previously initialized, so just update it with the configuration:
        PhysicalAlignmentTool workspace_tool = physical_alignment_tools.get("Augmented Workspace");
        workspace_tool.tool_tracking_markers = tracking_markers;
        */

    }


}