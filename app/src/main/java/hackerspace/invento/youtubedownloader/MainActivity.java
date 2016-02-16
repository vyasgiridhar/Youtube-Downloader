package hackerspace.invento.youtubedownloader;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;



import java.util.ArrayList;


public class MainActivity extends Activity {


    ListView list;
    ListViewAdapter adap;
    EditText edit;
    String Search;
    ArrayList<Video> List;
    GetVids v = new GetVids(this);
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView)findViewById(R.id.listview);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8949860465376306/4840483874");
        //list.setVisibility(View.VISIBLE);
        //YoYo.with(Techniques.RollOut).duration(700).playOn(findViewById(R.id.empty));
        list.setEmptyView(findViewById(R.id.empty));
        List = new ArrayList<>();
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdRequest adRequest = new AdRequest.Builder()
                        .build();

                mInterstitialAd.loadAd(adRequest);
                edit = (EditText) findViewById(R.id.editText);
                Search = edit.getText().toString();


                if(!Search.isEmpty()&&v.getStatus().toString()!="RUNNING") {
                    v = new GetVids(MainActivity.this);
                    v.SetSearch(Search);
                    v.execute();
                    mInterstitialAd.show();
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }

                }
            }

        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout) (list.getChildAt(position - list.getFirstVisiblePosition()))).open(true);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
        Context context;
        Boolean Obtained = false;
        ProgressDialog PD;
        public GetVids(Context con){
            this.context = con;
        }
        void SetSearch(String u) {

            search = u;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD = new ProgressDialog(context);
            PD.setMessage("Parsing Data");
            PD.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
        try {
            searchOnYoutube(Search);
            Obtained = true;
        }catch(Exception E){
            Obtained = false;
        }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(this.Obtained){
                adap = new ListViewAdapter(MainActivity.this, List);
                list.setAdapter(adap);
                adap.notifyDataSetChanged();
                list.setVisibility(View.VISIBLE);
                adap.setMode(Attributes.Mode.Single);
            }
            PD.dismiss();
            // Dismiss the progress dialog

        }


        private void searchOnYoutube(final String keywords) {

            YoutubeHandler yc = new YoutubeHandler(MainActivity.this);
            List = yc.Search(keywords);
            Log.d("Error 2", valueOf(List.size()));


        }
    }
}
