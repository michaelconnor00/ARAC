package ca.unbc.md.arac;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class ToolsMenuActivity extends ListActivity {

    private ArrayList<String> list = null;
    private ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_menu);

        AppGlobal.physical_alignment_tool_configuration =  new PhysicalAlignmentToolConfiguration();

        loadToolsList();

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list
        );

        setListAdapter(adapter);

        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                final String item = (String) getListAdapter().getItem(position);
                list.remove(item);
                adapter.notifyDataSetChanged();
                makeToast(item + " deleted");
                return true;
            }
        });

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        AppGlobal.current_physical_alignment_tool =
                AppGlobal.physical_alignment_tool_configuration.physical_alignment_tools.get(item);
        AppGlobal.current_geometry_filename = item;
        launchTemplate();
    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void loadToolsList() {
        String toolsList[] = new String[0];

        Map<String, PhysicalAlignmentTool> phy_tools_map = AppGlobal.physical_alignment_tool_configuration.physical_alignment_tools;

        list = new ArrayList<String>();
        for (PhysicalAlignmentTool p :phy_tools_map.values()){
            list.add(p.get_tool_name());
        }

    }

    public void launchTemplate(){
        // TODO remove hardcoded stuff...Once we have the proper user interfaces to specify this stuff...
        AppGlobal.current_geometry_filename = "models/Starry_Night.png"; //TODO this needs to be set in conjuntion with File Manager
        // Start AR Activity on success
        Intent intent = new Intent(getApplicationContext(), Template.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tools) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
