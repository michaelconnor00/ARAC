package ca.unbc.md.arac;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;


public class CalibrationActivity extends Activity {


    private ArrayList<String> marker_id_list = null;
    private ArrayAdapter<String> marker_id_spinner_adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        populateMarkerSelectionSpinner();
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

        marker_id_list = new ArrayList<String>();

        for(int i = 0; i < AppGlobal.current_physical_alignment_tool.tool_tracking_markers.size(); i++){
            marker_id_list.add(Integer.toString(AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(i).marker_ID));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, marker_id_list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.marker_ID_spinner);
        spinner.setAdapter(adapter);

    }

}
