package ca.unbc.md.arac;

import android.content.SharedPreferences;
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
public class PhysicalAlignmentToolManager {

    // Maintains a list of all the specified alignment tools:
    // String = Key = Tool ID Name
    // PhysicalAlignmentTool = The tool object
    Map<String, PhysicalAlignmentTool> physical_alignment_tools = new HashMap<String,PhysicalAlignmentTool>();

    public PhysicalAlignmentToolManager(){

        // Initialize all physical tools:
        try {

            if(!isSharedPreferencesInitialized()){
                setPhysicalToolSharedPreferencesToDefault();
            }

            initialize_square_edge_tool();
            initialize_augmented_workspace_tool();
            initialize_A4_drawing_canvas_tool();
        }
        catch(Exception e){
            //TODO : add better error handling here...
            // There was an error in tool initialization
            System.out.println("There was an error with physical tool initialization");
            System.exit(0);
        }

    }



    /****** TOOL Initialization - These Do Not Set The Geometries or Offsets *******/

    private void initialize_square_edge_tool() throws Exception{

        // This tool unique ID:
        String tool_id = "Square_Edge";

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
        String tool_id = "Augmented_Workspace";

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
        String tool_id = "A4_Drawing_Canvas";

        // Specify compatible tracking.xml configuration files:
        ArrayList<String> tracking_configuration_filenames = new ArrayList<String>();
        tracking_configuration_filenames.add("A4_Drawing_Canvas_TrackingData_8_Marker.xml");
        tracking_configuration_filenames.add("A4_Drawing_Canvas_TrackingData_8_Marker_Medium_Smoothing_Fuser.xml");
        //tracking_configuration_filenames.add("Augmented_Workspace_TrackingData_10_Marker_Heavy_Smoothing_Fuser.xml");

        // Initialize the tool:
        PhysicalAlignmentTool a4_drawing_canvas_tool = new PhysicalAlignmentTool(tool_id, null, tracking_configuration_filenames);

        // Add the tool to the list of tools:
        physical_alignment_tools.put(tool_id, a4_drawing_canvas_tool);
    }










    /****** TOOL CONFIGURATIONS - Set the tool Geometries, Offsets, Etc...  *******/

    public void configure_alignment_tool_by_id(String tool_id, IMetaioSDKAndroid metaioSDK, File model_file, boolean isGeometryAnImage) throws Exception{
        switch(tool_id){
            case "Square_Edge":
                configure_square_edge_tool(metaioSDK, model_file, isGeometryAnImage);
                break;
            case "Augmented_Workspace":
                configure_augmented_workspace_tool(metaioSDK, model_file, isGeometryAnImage);
                break;
            case "A4_Drawing_Canvas":
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
            if (isGeometryAnImage){
                geometry = metaioSDK.createGeometryFromImage(model_file);
            }
            else{
                geometry = metaioSDK.createGeometry(model_file);
            }

            if (geometry != null) {
                geometry.setCoordinateSystemID(i);
                geometry.setName("" + i);
                geometry.setVisible(false);
                geometry.setTransparency((float)getGlobalTransparency());
                geometry.setScale((float) getGlobalScale());

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

                geometry.setTranslation(new Vector3d(getPhysicalToolAttribute("Square_Edge", i ,"x"), getPhysicalToolAttribute("Square_Edge", i ,"y"), getPhysicalToolAttribute("Square_Edge" ,"z")));
                geometry.setRotation(rotation);

                tracking_marker = new TrackingMarker(i, geometry);
                tracking_markers.add(tracking_marker);

            } else
                MetaioDebug.log(Log.ERROR, "Error loading geometry: " + geometry);
        }

        // Tool was previously initialized, so just update it with the configuration:
        PhysicalAlignmentTool square_edge_tool = physical_alignment_tools.get("Square_Edge");
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
            if (isGeometryAnImage){
                geometry = metaioSDK.createGeometryFromImage(model_file);
            }
            else{
                geometry = metaioSDK.createGeometry(model_file);
            }


            if (geometry != null) {
                geometry.setCoordinateSystemID(i);
                geometry.setName("" + i);
                geometry.setVisible(false);
                geometry.setTransparency((float)getGlobalTransparency());
                geometry.setScale((float)getGlobalScale());


                // Set the translation offsets and rotation for each specific marker ID.
                Rotation rotation;
                if (isGeometryAnImage) {
                    rotation = new Rotation(new Vector3d(
                            (float) 0.0f, 0.0f, 0.0f));
                } else {
                    rotation = new Rotation(new Vector3d(
                            (float) Math.PI / 2, 0.0f, 0.0f));
                }

                geometry.setTranslation(new Vector3d(getPhysicalToolAttribute("Augmented_Workspace", i ,"x"), getPhysicalToolAttribute("Augmented_Workspace", i ,"y"), getPhysicalToolAttribute("Augmented_Workspace" ,"z")));
                geometry.setRotation(rotation);

                tracking_marker = new TrackingMarker(i, geometry);
                tracking_markers.add(tracking_marker);

            } else
                MetaioDebug.log(Log.ERROR, "Error loading geometry: " + geometry);
        }

        // Tool was previously initialized, so just update it with the configuration:
        PhysicalAlignmentTool workspace_tool = physical_alignment_tools.get("Augmented_Workspace");
        workspace_tool.tool_tracking_markers = tracking_markers;
    }


    public void configure_A4_drawing_canvas_tool(IMetaioSDKAndroid metaioSDK, File model_file, boolean isGeometryAnImage){
        ArrayList<TrackingMarker> tracking_markers = new ArrayList<TrackingMarker>();

        // Specify tracking marker configurations:
        int number_of_tracking_markers = 8;
        TrackingMarker tracking_marker;
        IGeometry geometry;
        TrackingMarkerPosition marker_position;

        for (int i = 1; i <= number_of_tracking_markers; i++) {

            // Loading 3D geometry
            if (isGeometryAnImage){
                geometry = metaioSDK.createGeometryFromImage(model_file);
            }
            else{
                geometry = metaioSDK.createGeometry(model_file);
            }


            if (geometry != null) {
                geometry.setCoordinateSystemID(i);
                geometry.setName("" + i);
                geometry.setVisible(false);
                geometry.setTransparency((float)getGlobalTransparency());
                geometry.setScale((float)getGlobalScale());


                // Set the translation offsets and rotation for each specific marker ID.
                Rotation rotation;
                if (isGeometryAnImage) {
                    rotation = new Rotation(new Vector3d(
                            (float) 0.0f, 0.0f, 0.0f));
                } else {
                    rotation = new Rotation(new Vector3d(
                            (float) Math.PI / 2, 0.0f, 0.0f));
                }

                geometry.setTranslation(new Vector3d(getPhysicalToolAttribute("A4_Drawing_Canvas", i ,"x"), getPhysicalToolAttribute("A4_Drawing_Canvas", i ,"y"), getPhysicalToolAttribute("A4_Drawing_Canvas" ,"z")));
                geometry.setRotation(rotation);

                tracking_marker = new TrackingMarker(i, geometry);
                tracking_markers.add(tracking_marker);

            } else
                MetaioDebug.log(Log.ERROR, "Error loading geometry: " + geometry);
        }

        // Tool was previously initialized, so just update it with the configuration:
        PhysicalAlignmentTool workspace_tool = physical_alignment_tools.get("A4_Drawing_Canvas");
        workspace_tool.tool_tracking_markers = tracking_markers;

    }






    /****** SHARED PREFERENCES  - Persistent Storage of tool configurations *******/

    /*
    PERSISTENT STORAGE FORMAT:

    KEY: <Physical_Tool_Name>_<Marker_ID>_<Attribute_ID>
    Value: The value of the attribute

    Attribute_ID's are: {x, y, r}

    EXAMPLE: Augmented_Workspace_1_x = -5


    Additional Keys:

    <Physical_Tool_Name>_z
    Global_Scale
    Global_Transparency

     */

    public void setPhysicalToolMarkerAttribute(String physical_tool_id, int marker_id, String attribute_id, String value){
        SharedPreferences preferences = AppGlobal.shared_preferences;
        preferences.edit().putString(physical_tool_id + "_" + marker_id + "_" + attribute_id, value).commit();

    }

    public float getPhysicalToolAttribute(String physical_tool_id, int marker_id, String attribute_id){
        SharedPreferences preferences = AppGlobal.shared_preferences;
        float return_value = Float.parseFloat(preferences.getString(physical_tool_id + "_" + marker_id + "_" + attribute_id, "0"));
        return return_value;
        // TODO Do some error handling if the default value of 0 is returned...
    }

    public float getPhysicalToolAttribute(String physical_tool_id, String attribute_id){
        SharedPreferences preferences = AppGlobal.shared_preferences;
        float return_value =  Float.parseFloat(preferences.getString(physical_tool_id + "_" + attribute_id, "0"));
        return return_value;
        // TODO Do some error handling if the default value of 0 is returned...
    }


    public double getGlobalScale(){
        SharedPreferences preferences = AppGlobal.shared_preferences;
        return Double.parseDouble(preferences.getString("Global_Scale", "1"));
    }


    public double getGlobalTransparency(){
        SharedPreferences preferences = AppGlobal.shared_preferences;
        return Double.parseDouble(preferences.getString("Global_Transparency", "0.5"));
    }

    public void setGlobalScale(float value){
        SharedPreferences preferences = AppGlobal.shared_preferences;
        preferences.edit().putString("Global_Scale", String.valueOf(value)).commit();

    }

    public void setGlobalTransparency(float value){
        SharedPreferences preferences = AppGlobal.shared_preferences;
        preferences.edit().putString("Global_Transparency", String.valueOf(value)).commit();
    }




    public void setPhysicalToolSharedPreferencesToDefault(){

        SharedPreferences preferences = AppGlobal.shared_preferences;

        // Global Attributes:
        preferences.edit().putString("Global_Scale","1").commit();
        preferences.edit().putString("Global_Transparency","0.5").commit();

        // Square Tool:
        preferences.edit().putString("Square_Tool_1_x", "30").commit();
        preferences.edit().putString("Square_Tool_2_x", "30").commit();
        preferences.edit().putString("Square_Tool_3_x", "30").commit();
        preferences.edit().putString("Square_Tool_4_x", "30").commit();
        preferences.edit().putString("Square_Tool_5_x", "-153").commit();
        preferences.edit().putString("Square_Tool_6_x", "-335").commit();

        preferences.edit().putString("Square_Tool_1_y", "17").commit();
        preferences.edit().putString("Square_Tool_2_y", "-172").commit();
        preferences.edit().putString("Square_Tool_3_y", "-365").commit();
        preferences.edit().putString("Square_Tool_4_y", "-548").commit();
        preferences.edit().putString("Square_Tool_5_y", "19").commit();
        preferences.edit().putString("Square_Tool_6_y", "18").commit();

        preferences.edit().putString("Square_Tool_z", "0").commit();



        // Augmented_Workspace:
        preferences.edit().putString("Augmented_Workspace_1_x", "62").commit();
        preferences.edit().putString("Augmented_Workspace_2_x", "62").commit();
        preferences.edit().putString("Augmented_Workspace_3_x", "62").commit();
        preferences.edit().putString("Augmented_Workspace_4_x", "-202").commit();
        preferences.edit().putString("Augmented_Workspace_5_x", "-484").commit();
        preferences.edit().putString("Augmented_Workspace_6_x", "-773").commit();
        preferences.edit().putString("Augmented_Workspace_7_x", "-773").commit();
        preferences.edit().putString("Augmented_Workspace_8_x", "-773").commit();
        preferences.edit().putString("Augmented_Workspace_9_x", "-485").commit();
        preferences.edit().putString("Augmented_Workspace_10_x", "-202").commit();

        preferences.edit().putString("Augmented_Workspace_1_y", "62").commit();
        preferences.edit().putString("Augmented_Workspace_2_y", "-203").commit();
        preferences.edit().putString("Augmented_Workspace_3_y", "-467").commit();
        preferences.edit().putString("Augmented_Workspace_4_y", "-467").commit();
        preferences.edit().putString("Augmented_Workspace_5_y", "-468").commit();
        preferences.edit().putString("Augmented_Workspace_6_y", "-469").commit();
        preferences.edit().putString("Augmented_Workspace_7_y", "-192").commit();
        preferences.edit().putString("Augmented_Workspace_8_y", "62").commit();
        preferences.edit().putString("Augmented_Workspace_9_y", "62").commit();
        preferences.edit().putString("Augmented_Workspace_10_y", "62").commit();

        preferences.edit().putString("Augmented_Workspace_z", "19").commit();


        // A4_Drawing_Canvas:
        // TODO specify default values and configure this tool (A4_Drawing_Canvas)...

        preferences.edit().putString("A4_Drawing_Canvas_1_x", "180").commit();
        preferences.edit().putString("A4_Drawing_Canvas_2_x", "180").commit();
        preferences.edit().putString("A4_Drawing_Canvas_3_x", "180").commit();
        preferences.edit().putString("A4_Drawing_Canvas_4_x", "10").commit();
        preferences.edit().putString("A4_Drawing_Canvas_5_x", "-175").commit();
        preferences.edit().putString("A4_Drawing_Canvas_6_x", "-175").commit();
        preferences.edit().putString("A4_Drawing_Canvas_7_x", "-175").commit();
        preferences.edit().putString("A4_Drawing_Canvas_8_x", "-3").commit();

        preferences.edit().putString("A4_Drawing_Canvas_1_y", "183").commit();
        preferences.edit().putString("A4_Drawing_Canvas_2_y", "3").commit();
        preferences.edit().putString("A4_Drawing_Canvas_3_y", "-170").commit();
        preferences.edit().putString("A4_Drawing_Canvas_4_y", "-170").commit();
        preferences.edit().putString("A4_Drawing_Canvas_5_y", "-170").commit();
        preferences.edit().putString("A4_Drawing_Canvas_6_y", "-1").commit();
        preferences.edit().putString("A4_Drawing_Canvas_7_y", "183").commit();
        preferences.edit().putString("A4_Drawing_Canvas_8_y", "183").commit();

        preferences.edit().putString("A4_Drawing_Canvas_z", "0").commit();



        // Set the flag specifying that the shared preferences default values have been set:
        preferences.edit().putString("Default_Values_Have_Been_Set","Yes").commit();

    }


   private boolean isSharedPreferencesInitialized(){
       SharedPreferences preferences = AppGlobal.shared_preferences;

       String initialization_test_result = preferences.getString("Default_Values_Have_Been_Set", "No");

       // If the default value is returned, then the shared prefs have not been initialized yet.
       if(initialization_test_result == "Yes"){
           return true;
       }
       else{
           return false;
       }

    }
}
