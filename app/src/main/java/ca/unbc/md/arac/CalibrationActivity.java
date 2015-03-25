package ca.unbc.md.arac;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import static android.view.View.OnClickListener;


public class CalibrationActivity extends Activity {


    private ArrayList<String> marker_id_list = null;
    private ArrayAdapter<String> marker_id_spinner_adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        initializeMarkerSelectionSpinner();
        //populateNumberPickers();
        initializeTextFieldEventHandlers();
        updateControlsWithValuesFromSharedPreferences();
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

    @Override
    protected void onStop() {
        updateSharedPreferencesWithControlValues();
        super.onStop();

        //Intent intent = new Intent(getApplicationContext(), TrackingActivity.class);
        //startActivity(intent);
        //final PhysicalAlignmentToolConfiguration tool_manager = AppGlobal.physical_alignment_tool_configuration;
    }

    private void initializeMarkerSelectionSpinner() {
        marker_id_list = new ArrayList<String>();

        for (int i = 0; i < AppGlobal.current_physical_alignment_tool.tool_tracking_markers.size(); i++) {
            marker_id_list.add(Integer.toString(AppGlobal.current_physical_alignment_tool.tool_tracking_markers.get(i).marker_ID));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, marker_id_list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.marker_ID_spinner);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateControlsWithValuesFromSharedPreferences();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        spinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    updateSharedPreferencesWithControlValues();
                }
                return false;
            }
        });


    }

    private void initializeTextFieldEventHandlers() {

        // Get the tool manager, and currently selected tool.
        final PhysicalAlignmentToolManager tool_manager = AppGlobal.physical_alignment_tool_configuration;
        final PhysicalAlignmentTool current_tool = AppGlobal.current_physical_alignment_tool;

        Spinner spinner = (Spinner) findViewById(R.id.marker_ID_spinner);
        final int marker_id = Integer.parseInt((String) spinner.getSelectedItem());

        final EditText z_offset_editText = (EditText) findViewById(R.id.z_offset_editText);
        final EditText scale_editText = (EditText) findViewById(R.id.scale_editText);
        final EditText transparency_editText = (EditText) findViewById(R.id.transparency_editText);
        final EditText x_offset_editText = (EditText) findViewById(R.id.x_offset_editText);
        final EditText y_offset_editText = (EditText) findViewById(R.id.y_offset_editText);
        final EditText rotation_editText = (EditText) findViewById(R.id.rotation_editText);

        // TODO put validation on input types... (may or may not need this? (It might work already!))

        // Create all the EditText event handlers:
        z_offset_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateSharedPreferencesWithControlValues();
                //tool_manager.setPhysicalToolMarkerAttribute(current_tool.get_tool_id(), marker_id, "z", z_offset_editText.getText().toString());
            }
        });


        scale_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateSharedPreferencesWithControlValues();
                //tool_manager.setGlobalScale(Float.parseFloat(scale_editText.getText().toString()));
            }
        });


        transparency_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateSharedPreferencesWithControlValues();
               // tool_manager.setGlobalTransparency(Float.parseFloat(transparency_editText.getText().toString()));
            }
        });


        x_offset_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateSharedPreferencesWithControlValues();
               // tool_manager.setPhysicalToolMarkerAttribute(current_tool.get_tool_id(), marker_id, "x", x_offset_editText.getText().toString());
            }
        });


        y_offset_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateSharedPreferencesWithControlValues();
                //tool_manager.setPhysicalToolMarkerAttribute(current_tool.get_tool_id(), marker_id, "y", y_offset_editText.getText().toString());
            }
        });


        // TODO rotation editing through calibration activity
/*
        rotation_editText.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                tool_manager.setPhysicalToolMarkerAttribute(current_tool.get_tool_id(),marker_id,"r",rotation_editText.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
*/
    }

    private void updateSharedPreferencesWithControlValues() {
        // Get the tool manager, and currently selected tool.
        final PhysicalAlignmentToolManager tool_manager = AppGlobal.physical_alignment_tool_configuration;
        final PhysicalAlignmentTool current_tool = AppGlobal.current_physical_alignment_tool;

        Spinner spinner = (Spinner) findViewById(R.id.marker_ID_spinner);
        final int marker_id = Integer.parseInt((String) spinner.getSelectedItem());

        final EditText z_offset_editText = (EditText) findViewById(R.id.z_offset_editText);
        final EditText scale_editText = (EditText) findViewById(R.id.scale_editText);
        final EditText transparency_editText = (EditText) findViewById(R.id.transparency_editText);
        final EditText x_offset_editText = (EditText) findViewById(R.id.x_offset_editText);
        final EditText y_offset_editText = (EditText) findViewById(R.id.y_offset_editText);
        final EditText rotation_editText = (EditText) findViewById(R.id.rotation_editText);

        tool_manager.setPhysicalToolMarkerAttribute(current_tool.get_tool_id(), marker_id, "z", z_offset_editText.getText().toString());
        tool_manager.setGlobalScale(Float.parseFloat(scale_editText.getText().toString()));
        tool_manager.setGlobalTransparency(Float.parseFloat(transparency_editText.getText().toString()));
        tool_manager.setPhysicalToolMarkerAttribute(current_tool.get_tool_id(), marker_id, "x", x_offset_editText.getText().toString());
        tool_manager.setPhysicalToolMarkerAttribute(current_tool.get_tool_id(), marker_id, "y", y_offset_editText.getText().toString());

    }

    private void updateControlsWithValuesFromSharedPreferences() {
        // Get the current Selected marker from the marker spinner.
        Spinner spinner = (Spinner) findViewById(R.id.marker_ID_spinner);
        int marker_id = Integer.parseInt((String) spinner.getSelectedItem());

        // Get the tool manager, and currently selected tool.
        PhysicalAlignmentToolManager tool_manager = AppGlobal.physical_alignment_tool_configuration;
        PhysicalAlignmentTool current_tool = AppGlobal.current_physical_alignment_tool;

        // Get the references to text fields form the layout:
        final EditText z_offset_editText = (EditText) findViewById(R.id.z_offset_editText);
        final EditText scale_editText = (EditText) findViewById(R.id.scale_editText);
        final EditText transparency_editText = (EditText) findViewById(R.id.transparency_editText);
        final EditText x_offset_editText = (EditText) findViewById(R.id.x_offset_editText);
        final EditText y_offset_editText = (EditText) findViewById(R.id.y_offset_editText);
        final EditText rotation_editText = (EditText) findViewById(R.id.rotation_editText);

        // Assign the existing values to this activities' controls.
        z_offset_editText.setText(String.valueOf(tool_manager.getPhysicalToolAttribute(current_tool.get_tool_id(), "z")));
        scale_editText.setText(String.valueOf(tool_manager.getGlobalScale()));
        transparency_editText.setText(String.valueOf(tool_manager.getGlobalTransparency()));
        x_offset_editText.setText(String.valueOf(tool_manager.getPhysicalToolAttribute(current_tool.get_tool_id(), marker_id, "x")));
        y_offset_editText.setText(String.valueOf(tool_manager.getPhysicalToolAttribute(current_tool.get_tool_id(), marker_id, "y")));
        //rotation_editText.setText(String.valueOf(tool_manger.get));  // TODO add rotation functionality
    }


    public void populateNumberPickers() {
/*
        NumberPicker np = (NumberPicker) findViewById(R.id.x_offset_numberPicker);
        Integer[] nums = new Integer[2000];
        for(int i = 0; i < nums.length; i++)
            nums[i] = (i - 1000);

       // np.setMinValue(-1000);
       // np.setMaxValue(1000);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(nums);
        np.setValue(1);
    */
    }

}
