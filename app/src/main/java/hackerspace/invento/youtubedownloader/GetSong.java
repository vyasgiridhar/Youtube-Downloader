package hackerspace.invento.youtubedownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

/**
 * Created by invento on 29/5/15.
 */
public class GetSong extends AsyncTask<Void, Void, Void> {

    String URL;
    Context context;
    DownloadManager dm;
    private long enqueue;

    void SetURL(String u, Context c) {

        this.URL = "http://YoutubeInMP3.com/fetch/?api=advanced&format=JSON&video="+u;
        context = c;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... arg0) {

        try {
            String jsonStr = Jsoup.connect("http://en.wikipedia.org/").get().html();


            //Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    URL = jsonObj.getString("link");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }catch(Exception E){
            Toast.makeText(context,"Couldn't fetch Result",
                    Toast.LENGTH_SHORT).show();
        }
    return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        dm = (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(URL));
        enqueue = dm.enqueue(request);
    }


}

