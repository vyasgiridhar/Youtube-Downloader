package hackerspace.invento.youtubedownloader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import java.util.List;


public class MainActivity extends Activity {


    ListView list;
    ListViewAdapter adap;
    ListAdapter adapt;
    private MergeAdapter adapter = new MergeAdapter();
    EditText edit;
    String Search;
    Video [] List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView)findViewById(R.id.listview);


        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit = (EditText) findViewById(R.id.editText);
                do {
                    Search = edit.getText().toString();
                    searchOnYoutube(Search);
                } while (Search == null);

            }
        });
        adap = new ListViewAdapter(this,List);
        adapt = new ListAdapter(this,List);
        adapter.addAdapter(adap);
        adapter.addAdapter(adapt);
        list.setAdapter(adapter);
        adap.setMode(Attributes.Mode.Single);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout) (list.getChildAt(position - list.getFirstVisiblePosition()))).open(true);
            }
        });
        adapter.addAdapter(adap);
        adapter.addAdapter(adapt);
        list.setAdapter(adapter);
    }

    private void searchOnYoutube(final String keywords) {

        YoutubeHandler yc = new YoutubeHandler(MainActivity.this);
        yc.Search(keywords).toArray(List);

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
}
