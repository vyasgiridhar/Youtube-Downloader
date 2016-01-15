package hackerspace.invento.youtubedownloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


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