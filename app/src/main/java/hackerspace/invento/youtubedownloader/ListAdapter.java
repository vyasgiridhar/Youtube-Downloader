package hackerspace.invento.youtubedownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by invento on 29/5/15.
 */
public class ListAdapter extends BaseAdapter{

    ArrayList<Video> result;
    Context context;
    private static LayoutInflater inflater = null;

    public ListAdapter(MainActivity mainactivity,ArrayList<Video> list){
        result = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return result.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder = new Holder();
        View row;
        row = inflater.inflate(R.layout.listview_item,null);
        holder.tv = (TextView) row.findViewById(R.id.textView);
        holder.img = (ImageView) row.findViewById(R.id.image);

        holder.tv.setText(result.get(position).getTitle());
        Picasso.with(context).load(result.get(position).getImgurl()).into(holder.img);
        row.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Swipe to reveal options", Toast.LENGTH_LONG).show();
            }
        });
        return  row;
    }

}
