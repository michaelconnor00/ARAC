package ca.unbc.md.arac;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

//import ca.unbc.md.arac.AppGlobal;


public class FileManagerActivity extends ListActivity {

//    private AssetsExtracter mTask;
    private ArrayList<String> list = null;
    private ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_manager);
//        getActionBar().show();

//        // extract all the assets
//        mTask = new AssetsExtracter();
//        mTask.execute(0);

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

        String [] values = listFiles("models");

        list = new ArrayList<String>();

        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }

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
        AppGlobal.current_geometry_filename = item;
        launchTemplate();
    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }



    private String [] listFiles(String dirFrom) {
        Resources res = getResources(); //if you are in an activity
        AssetManager am = res.getAssets();
        String fileList[] = new String[0];
        try {
            fileList = am.list(dirFrom);
        } catch (IOException e) {
            MetaioDebug.log(Log.ERROR, "IO Error getting assets list: " + e.getMessage());
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }
        return fileList;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fileman) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void launchTemplate(){
        // TODO remove hardcoded stuff...Once we have the proper user interfaces to specify this stuff...
        AppGlobal.physical_alignment_tool_configuration =  new PhysicalAlignmentToolConfiguration();
        AppGlobal.current_physical_alignment_tool = AppGlobal.physical_alignment_tool_configuration.physical_alignment_tools.get("Augmented Workspace");
        // Start AR Activity on success
        Intent intent = new Intent(getApplicationContext(), Template.class);
        startActivity(intent);
    }


    /**
     * This task extracts all the application assets to an external or internal location
     * to make them accessible to Metaio SDK
     */
//    private class AssetsExtracter extends AsyncTask<Integer, Integer, Boolean>
//    {
//
//        @Override
//        protected void onPreExecute(){
//
//        }
//
//        @Override
//        protected Boolean doInBackground(Integer... params)
//        {
//            try
//            {
//                // Extract all assets and overwrite existing files if debug build
//                AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
//            }
//            catch (IOException e)
//            {
//                MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
//                MetaioDebug.printStackTrace(Log.ERROR, e);
//                return false;
//            }
//
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result)
//        {
//            if (result)
//            {
//                // Start AR Activity on success
////                Intent intent = new Intent(getApplicationContext(), Template.class);
////                startActivity(intent);
//            }
//            else
//            {
//                // Show a toast with an error message
//                Toast toast = Toast.makeText(getApplicationContext(), "Error extracting application assets!", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                toast.show();
//            }
//
//            finish();
//        }
//
//    }
}
