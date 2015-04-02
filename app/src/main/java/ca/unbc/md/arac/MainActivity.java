package ca.unbc.md.arac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;



import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;



import java.io.IOException;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;

public class MainActivity extends Activity{ //extends CardboardActivity implements CardboardView.StereoRenderer{



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            // Extract all assets and overwrite existing files if debug build
            AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
        }
        catch (IOException e)
        {
            MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }


        SharedPreferences preferences = this.getSharedPreferences("ca.unbc.md.arac", Context.MODE_PRIVATE);
        //Map map = preferences.getAll();
        AppGlobal.shared_preferences = preferences;
        AppGlobal.physical_alignment_tool_configuration =  new PhysicalAlignmentToolManager();


        setContentView(R.layout.activity_main);

        /*
        setContentView(R.layout.activity_main);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        // Associate a CardboardView.StereoRenderer with cardboardView.
        cardboardView.setRenderer(this);
        // Associate the cardboardView with this activity.
        setCardboardView(cardboardView);
        */


        // Enable metaio SDK debug log messages based on build configuration
        MetaioDebug.enableLogging(BuildConfig.DEBUG);

    }

    /**
     * Function to transition to Model Manager
     */
    public void launchModelManager(View view){
        Intent intent = new Intent(this, FileManagerActivity.class);
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

    public void launchToolsMenu(View view){
        Intent intent = new Intent(this, ToolsMenuActivity.class);
        startActivity(intent);
    }

    public void launchHelp(View view){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

}





