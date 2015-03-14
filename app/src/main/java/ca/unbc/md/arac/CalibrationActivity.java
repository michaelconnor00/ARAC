package ca.unbc.md.arac;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class CalibrationActivity extends Activity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        // TODO remove this.. (it's a test)
        setPhysicalToolAttributesToDefault();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setPhysicalToolAttribute(String physical_tool_id, int marker_id, String attribute_id){
        SharedPreferences preferences = this.getSharedPreferences("ca.unbc.md.arac", Context.MODE_PRIVATE);


    }

    public int getPhysicalToolAttribute(String physical_tool_id, int marker_id, String attribute_id){
        return -1;
    }


    public void setPhysicalToolAttributesToDefault(){

        SharedPreferences preferences = this.getSharedPreferences("ca.unbc.md.arac", Context.MODE_PRIVATE);

        // Global Attributes:
        preferences.edit().putString("Global_Scale","1").apply();
        preferences.edit().putString("Global_Transparency","0.5").apply();


        // Square Tool:
        preferences.edit().putString("Square_Tool_1_x", "30").apply();
        preferences.edit().putString("Square_Tool_2_x", "30").apply();
        preferences.edit().putString("Square_Tool_3_x", "30").apply();
        preferences.edit().putString("Square_Tool_4_x", "30").apply();
        preferences.edit().putString("Square_Tool_5_x", "-153").apply();
        preferences.edit().putString("Square_Tool_6_x", "-335").apply();

        preferences.edit().putString("Square_Tool_1_y", "17").apply();
        preferences.edit().putString("Square_Tool_2_y", "-172").apply();
        preferences.edit().putString("Square_Tool_3_y", "-365").apply();
        preferences.edit().putString("Square_Tool_4_y", "-548").apply();
        preferences.edit().putString("Square_Tool_5_y", "19").apply();
        preferences.edit().putString("Square_Tool_6_y", "18").apply();

        preferences.edit().putString("Square_Tool_z", "1").apply();



        // Augmented_Workspace:
        preferences.edit().putString("Augmented_Workspace_1_x", "62").apply();
        preferences.edit().putString("Augmented_Workspace_2_x", "62").apply();
        preferences.edit().putString("Augmented_Workspace_3_x", "62").apply();
        preferences.edit().putString("Augmented_Workspace_4_x", "-202").apply();
        preferences.edit().putString("Augmented_Workspace_5_x", "-484").apply();
        preferences.edit().putString("Augmented_Workspace_6_x", "-773").apply();
        preferences.edit().putString("Augmented_Workspace_7_x", "-773").apply();
        preferences.edit().putString("Augmented_Workspace_8_x", "-773").apply();
        preferences.edit().putString("Augmented_Workspace_9_x", "-485").apply();
        preferences.edit().putString("Augmented_Workspace_10_x", "-202").apply();

        preferences.edit().putString("Augmented_Workspace_1_y", "62").apply();
        preferences.edit().putString("Augmented_Workspace_2_y", "-203").apply();
        preferences.edit().putString("Augmented_Workspace_3_y", "-467").apply();
        preferences.edit().putString("Augmented_Workspace_4_y", "-467").apply();
        preferences.edit().putString("Augmented_Workspace_5_y", "-468").apply();
        preferences.edit().putString("Augmented_Workspace_6_y", "-469").apply();
        preferences.edit().putString("Augmented_Workspace_7_y", "-192").apply();
        preferences.edit().putString("Augmented_Workspace_8_y", "62").apply();
        preferences.edit().putString("Augmented_Workspace_9_y", "62").apply();
        preferences.edit().putString("Augmented_Workspace_10_y", "62").apply();

        preferences.edit().putString("Augmented_Workspace_z", "1").apply();


    }

}
