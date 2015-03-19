package ca.unbc.md.arac;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class CalibrationActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
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

    public void populateMarkerSelectionSpinner(){
        /*
        ArrayAdapter<CharSequence> array_adapter =
                new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner marker_id_spinner = (Spinner) findViewById(R.id.marker_ID_spinner);
        marker_id_spinner.setAdapter(array_adapter);

        for(int i = 0; i < AppGlobal.current_physical_alignment_tool.tool_tracking_markers.size(); i++){
            array_adapter.add(Integer.toString(AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(i).marker_ID));
        }

        marker_id_spinner.setAdapter(array_adapter);
        */
    }

}
