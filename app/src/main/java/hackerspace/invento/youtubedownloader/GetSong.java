package hackerspace.invento.youtubedownloader;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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


            Log.v("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    this.url = jsonObj.getString("link");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }catch(Exception E){
          /*  Toast.makeText(context,"Couldn't fetch Result",
                    Toast.LENGTH_SHORT).show();*/
        }
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

