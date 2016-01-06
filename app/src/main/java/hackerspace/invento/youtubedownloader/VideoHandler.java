package hackerspace.invento.youtubedownloader;


import android.app.ProgressDialog;
import android.content.Context;

import android.os.AsyncTask;


import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;


import org.jsoup.Jsoup;



/**
 * Created by vyas on 6/1/16.
 */
public class VideoHandler extends AsyncTask<Void, Void, Void>{

    String Video_Url = null;
    String html = null;
    private ProgressDialog PD;
    Context context;


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
        // Creating service handler class instance

        try {
            String Html = Jsoup.connect(Video_Url).get().html();
            //Log.d("Response: ", "> " + jsonStr);

        } catch (Exception E) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        PD.dismiss();
    }

    void Get_Links(){

        Video_Url = Video_Url.replaceAll(" ", "");
        Video_Url = Video_Url.replace("%25","%");
        Video_Url = Video_Url.replace("\\u0026", "&");
        html = StringUtils.subStringbetween()
    }
}

