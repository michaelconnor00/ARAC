package ca.unbc.md.arac;

import android.util.Log;

import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.Vector3d;

import java.util.ArrayList;

/**
 * Created by Dev on 3/10/2015.
 */
public class PhysicalAlignmentToolConfiguration {


    // Maintains a list of all the specified alignment tools
    ArrayList<PhysicalAlignmentTool> physical_alignment_tools = new ArrayList<PhysicalAlignmentTool>();



    public PhysicalAlignmentToolConfiguration(){
        // Initialize all physical tools:
        initialize_square_edge_tool();
        initialize_augmented_workspace_tool();
    }


    /****** TOOL DEFINITIONS *******/

    protected void initialize_square_edge_tool(){
        ArrayList<TrackingMarker> tracking_markers = new ArrayList<TrackingMarker>();
        ArrayList<String> tracking_configuration_filenames = new ArrayList<String>();

        // Specify tracking marker configurations:


        // Specify compatible tracking.xml configuration files:


        // Create the tool:
        PhysicalAlignmentTool square_edge_tool = new PhysicalAlignmentTool("Square Edge", tracking_markers, tracking_configuration_filenames );

        // Add the tool to the list of tools:
        physical_alignment_tools.add(square_edge_tool);
    }

    protected void initialize_augmented_workspace_tool(){
            ArrayList<TrackingMarker> tracking_markers = new ArrayList<TrackingMarker>();
            ArrayList<String> tracking_configuration_filenames = new ArrayList<String>();


            // Specify tracking marker configurations:


            // Specify compatible tracking.xml configuration files:


            // Create the tool:
            PhysicalAlignmentTool workspace_tool = new PhysicalAlignmentTool("Augmented Workspace", tracking_markers, tracking_configuration_filenames );

            // Add the tool to the list of tools:
            physical_alignment_tools.add(workspace_tool);
    }
}
