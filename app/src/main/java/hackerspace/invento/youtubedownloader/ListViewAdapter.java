package hackerspace.invento.youtubedownloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

/**
 * Created by invento on 28/5/15.
 */
public class ListViewAdapter extends BaseSwipeAdapter {

    private Context context;
    Video [] vids;
    GetSong song = new GetSong();

    public ListViewAdapter(Context c,Video [] v) {

        vids = v;
        this.context = c;

    }

    @Override
    public int getSwipeLayoutResourceId(int position){
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent){
        View v = LayoutInflater.from(context).inflate(R.layout.listview_item,null);
        SwipeLayout swipe = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipe.setShowMode(SwipeLayout.ShowMode.LayDown);

        swipe.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                Toast.makeText(context, "Audio or video?", Toast.LENGTH_LONG).show();
            }
        });
        v.findViewById(R.id.Video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context,Vid_Download_Page.class);
                i.putExtra("The Vid",vids[position]);
                context.startActivity(i);

            }
        });
        v.findViewById(R.id.Audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                song.SetURL(vids[position].getURL());
                song.execute();

            }
        });
        return v;
    }

    public void fillValues(int position,View convert){

    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
