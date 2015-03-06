package ca.unbc.md.arac;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import ca.unbc.md.arac.BuildConfig;
import ca.unbc.md.arac.R;
import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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





