package ca.unbc.md.arac;

import android.content.SharedPreferences;

import com.metaio.sdk.jni.IGeometry;

/**
 * Created by Dev on 3/10/2015.
 */
public class AppGlobal {
    public static SharedPreferences shared_preferences;

    public static PhysicalAlignmentToolConfiguration physical_alignment_tool_configuration;
    public static PhysicalAlignmentTool current_physical_alignment_tool;
    public static String current_geometry_filename;
}
