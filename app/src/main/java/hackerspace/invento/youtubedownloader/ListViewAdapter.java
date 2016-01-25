package hackerspace.invento.youtubedownloader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import at.huber.youtubeExtractor.YtFile;

/**
 * Created by invento on 28/5/15.
 */
public class ListViewAdapter extends BaseSwipeAdapter {

    private Context context;
    ArrayList<Video> result;
    GetSong song = null;
    GetVideo video = null;
    LayoutInflater inflater;

    public ListViewAdapter(Context c,ArrayList<Video> list){
        result = list;
        this.context = c;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        v.findViewById(R.id.Audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                song = new GetSong();
                song.SetURL(result.get(position).getURL(), context);
                song.execute();

            }
        });
        v.findViewById(R.id.Video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //video = new GetVideo(result.get(position).getURL(),context);
                String url = result.get(position).getURL();
                Intent I = new Intent(context,DownloadActivity.class);
                I.putExtra("THE_URL",url);
                context.startActivity(I);
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position,View convert){

        Holder holder = new Holder();
        holder.tv = (TextView) convert.findViewById(R.id.textView);
        holder.img = (ImageView) convert.findViewById(R.id.imageView);
        holder.tv.setText(result.get(position).getTitle());
        Picasso.with(context).load(result.get(position).getImgurl()).into(holder.img);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Swipe to reveal options", Toast.LENGTH_LONG).show();
            }
        });



    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }

    @Override
    public int getCount() {
        return result.size();
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
