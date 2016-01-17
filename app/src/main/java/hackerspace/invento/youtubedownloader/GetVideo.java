package hackerspace.invento.youtubedownloader;

import android.content.Context;

/**
 * Created by night-crawler on 12/11/15.
 */
public class GetVideo {

    String URL;
    Context context;
    String Quality;
    void SetURL(String u, Context c,String Q) {

        this.URL = "http://www.saveitoffline.com/#"+u;
        this.context = c;
        Quality = Q;

    }


}