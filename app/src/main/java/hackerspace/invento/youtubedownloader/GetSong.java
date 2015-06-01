package hackerspace.invento.youtubedownloader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by invento on 29/5/15.
 */
public class GetSong extends AsyncTask<Void, Void, Void> {

    String URL;
    Context context;

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
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();


        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(URL, ServiceHandler.GET);

        //Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {

                JSONObject jsonObj = new JSONObject(jsonStr);


                String title = jsonObj.getString("title");
                String time = jsonObj.getString("length");
                URL = jsonObj.getString("link");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        context.startActivity(browserIntent);

    }


    private class ServiceHandler {

        public String response = null;
        public static final int GET = 1;
        public static final int POST = 2;

        public ServiceHandler() {

        }

        public String makeServiceCall(String url, int method) {
            return this.makeServiceCall(url, method, null);
        }

        /*
         * Making service call
         * @url - url to make request
         * @method - http request method
         * @params - http request params
         * */
        public String makeServiceCall(String url, int method,
                                      List<NameValuePair> params) {
            try {
                // http client
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;

                // Checking http request method type
                if (method == POST) {
                    HttpPost httpPost = new HttpPost(url);
                    httpResponse = httpClient.execute(httpPost);

                } else if (method == GET) {


                    HttpGet httpGet = new HttpGet(url);
                    httpResponse = httpClient.execute(httpGet);

                }
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                Log.d("Response: ", "> " + response);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;

        }

    }
}

