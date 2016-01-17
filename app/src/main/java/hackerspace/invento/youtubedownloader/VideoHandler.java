package hackerspace.invento.youtubedownloader;


import android.app.ProgressDialog;
import android.content.Context;

import android.os.AsyncTask;

import android.util.Log;
import android.widget.Toast;


import java.util.Arrays;


/**
 * Created by vyas on 6/1/16.
 */
public class VideoHandler extends AsyncTask<Void, Void, Void>{

    String Video_Url = null;
    String html = null;
    private ProgressDialog PD;
    Context context;
    Boolean success = false;

    public VideoHandler(String u, Context c) {

        this.Video_Url = u;
        context = c;

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


        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        this.PD.dismiss();
    }

}


