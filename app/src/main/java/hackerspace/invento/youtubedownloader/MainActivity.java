package hackerspace.invento.youtubedownloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {


    ListView list;
    ListViewAdapter adap;
    EditText edit;
    String Search;
    ArrayList<Video> List;
    GetVids v = new GetVids();
    String error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView)findViewById(R.id.listview);

        list.setVisibility(View.INVISIBLE);
        List = new ArrayList<>();
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit = (EditText) findViewById(R.id.editText);
                do {
                    Search = edit.getText().toString();
                } while (Search == null);
                if(!Search.isEmpty()) {
                    v.SetSearch(Search);
                    v.execute();

                    Log.d("Error","Starting to sleep");
                    try {
                        Thread.sleep(2000);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    Log.d("Error","Stopping to sleep");
                    Log.d("Error", valueOf(List.size()));
                    if(!List.isEmpty())
                    adap = new ListViewAdapter(MainActivity.this, List);
                    list.setAdapter(adap);
                    list.setVisibility(View.VISIBLE);
                    adap.setMode(Attributes.Mode.Single);

                }
            }

        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout) (list.getChildAt(position - list.getFirstVisiblePosition()))).open(true);
            }
        });


    }

    public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    public class GetVids extends AsyncTask<Void, Void, Void> {

        String search;

        void SetSearch(String u) {

            search = u;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            searchOnYoutube(Search);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

        }


        private void searchOnYoutube(final String keywords) {

            YoutubeHandler yc = new YoutubeHandler(MainActivity.this);
            List = yc.Search(keywords);
            Log.d("Error 2", valueOf(List.size()));


        }
    }
}
