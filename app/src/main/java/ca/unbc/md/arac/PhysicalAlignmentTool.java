package ca.unbc.md.arac;

import java.util.ArrayList;

/**
 * Created by Dev on 3/10/2015.
 */

/*
    This class defines the properties of a single physical alignment tool to be used for tracking alignment.
 */
public class PhysicalAlignmentTool {


    // The name of the physical tool
    private String tool_id;

    // All the tracking marker data, including marker offsets, geometries, ...
    public ArrayList<TrackingMarker> tool_tracking_markers = new ArrayList<TrackingMarker>();

    // List of tracking.xml files names that are configured to be used with this tool.
    public ArrayList<String> tool_tracking_xml_filenames = new ArrayList<String>();


    public PhysicalAlignmentTool(String tool_id, ArrayList<TrackingMarker> tracking_markers, ArrayList<String> tracking_xml_filenames){
        this.tool_id = tool_id;
        tool_tracking_markers = tracking_markers;
        tool_tracking_xml_filenames = tracking_xml_filenames;
    }


    public String get_tool_name(){
        return tool_id;
    }
}
