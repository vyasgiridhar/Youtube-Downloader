package hackerspace.invento.youtubedownloader;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by invento on 29/5/15.
 */
public class GetSong extends AsyncTask<Void, Void, Void> {

    String url;
    Context context;
    DownloadManager dm;
    private long enqueue;
    ProgressDialog PD;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36";


    void SetURL(String u, Context c) {

        this.url = "http://YoutubeInMP3.com/fetch/?api=advanced&format=JSON&video="+u;
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

        String jsonStr = null;
        BufferedReader reader = null;
        try {

            URL getUrl = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);
            try {
                 reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                jsonStr  = reader.readLine();

            } finally {
                if (reader != null) {
                    reader.close();
                }
                urlConnection.disconnect();
            }


            Log.d("Response: ",jsonStr);



                Document string = Jsoup.parse(jsonStr);
                this.url = string.getElementById("Link").toString();
                Log.d("THe URL = ",this.url);


            return null;
        }catch(Exception E){
          /*  Toast.makeText(context,"Couldn't fetch Result",
                    Toast.LENGTH_SHORT).show();*/
        }
        Log.d("THe URL = ",this.url);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        PD.dismiss();
        dm = (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(this.url));
        enqueue = dm.enqueue(request);
    }



}

