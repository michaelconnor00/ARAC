package ca.unbc.md.arac;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.metaio.sdk.MetaioDebug;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        // Initialize the physical alignment tool Objects (Offset and Geometries are not yet specified):
        // TODO Move this into the onCreate in the physical alignment tool picker view
        PhysicalAlignmentToolConfiguration toolConfiguration = new PhysicalAlignmentToolConfiguration();
        AppGlobal.physical_alignment_tool_configuration = toolConfiguration;


        setContentView(R.layout.activity_main);

        // Enable metaio SDK debug log messages based on build configuration
        MetaioDebug.enableLogging(BuildConfig.DEBUG);

    }

    /**
     * Function to transition to Model Manager
     */
    public void launchModelManager(View view){
        Intent intent = new Intent(this, ModelManagerActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void launchCalibration(View view){
        Intent intent = new Intent(this, CalibrationActivity.class);
        startActivity(intent);
    }

    public void launchSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void launchAdjust(View view){

    }

}





