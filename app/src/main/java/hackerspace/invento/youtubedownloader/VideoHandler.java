package hackerspace.invento.youtubedownloader;


import android.app.ProgressDialog;
import android.content.Context;

import android.os.AsyncTask;

import android.util.Log;
import android.widget.Toast;


import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
        // Creating service handler class instance
        try {
            URL url = new URL(this.Video_Url);
            BufferedReader reader = null;
            StringBuilder builder = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                for (String line; (line = reader.readLine()) != null; ) {
                    builder.append(line.trim());
                }
                Log.d("html",reader.toString());
                success = true;
            } finally {

                this.html = reader.toString();
                if (reader != null) try {
                    reader.close();
                } catch (IOException logOrIgnore) {
                }
            }
        }catch(Exception E){
            success = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        this.PD.dismiss();
    }

    private String sortStringAt ( String source, String delimiter ) {

        String sortedUrl = source.replaceFirst("\\?.*", "");

        String[] unsortedUrl = source.replaceFirst("http.*\\?","").concat("&range=0-999999999").split(delimiter);

        Arrays.sort(unsortedUrl);

        for (int i=0;i<unsortedUrl.length-1;i++) {
            if (unsortedUrl[i].equals(unsortedUrl[i+1]))
                unsortedUrl[i]="";
        }

        sortedUrl += Arrays.toString(unsortedUrl);
        sortedUrl = sortedUrl.replaceAll("\\[, ", delimiter).replaceAll(", ", delimiter).replaceAll(",,", delimiter).replaceAll("]", "").replaceAll(delimiter+delimiter, delimiter);
        sortedUrl = sortedUrl.replaceFirst("/videoplayback\\[", "/videoplayback?").replaceFirst("/videoplayback&", "/videoplayback?");
        return sortedUrl;

    }


    void Get_Links(){

        html = html.replaceAll(" ", "");
        html = html.replace("%25","%");
        html = html.replace("\\u0026", "&");
        String Links1="",Links2="";
        if (html.contains("\"url_encoded_fmt_stream_map\":\"")) {
            Links1 = substringBetween(html,"\"url_encoded_fmt_stream_map\":\"" , "\"");
        }
        else if(html.contains("\"adaptive_fmts\":\"")){
            Links2 = substringBetween(html,"\"adaptive_fmts\":\"","\"");
        }
        String Links = Links1 + "," + Links2;

        String[] sourceCodeYoutubeUrls = Links.split(",");

        for(String url : sourceCodeYoutubeUrls){
            if(url.matches(".*conn=rtmpe.*")){

                Toast.makeText(context,"Could not download videos",Toast.LENGTH_LONG).show();
                break;
            }
            String[] fmtUrlPair = url.split("url=http(s)?",2);
            fmtUrlPair[1] = "url=http"+fmtUrlPair[1]+"&"+fmtUrlPair[0];
            fmtUrlPair[0] = fmtUrlPair[1].substring(fmtUrlPair[1].indexOf("itag=")+5, fmtUrlPair[1].indexOf("itag=")+5+1+(fmtUrlPair[1].matches(".*itag=[0-9]{2}.*")?1:0)+(fmtUrlPair[1].matches(".*itag=[0-9]{3}.*")?1:0));
            if (this.Video_Url.startsWith("https")) {
                fmtUrlPair[1] = fmtUrlPair[1].replaceFirst("url=http%3A%2F%2F", "https://");
            } else {
                fmtUrlPair[1] = fmtUrlPair[1].replaceFirst("url=http%3A%2F%2F", "http://");
            }
            fmtUrlPair[1] = fmtUrlPair[1].replaceAll("%3F","?").replaceAll("%2F", "/").replaceAll("%3B",";")/*.replaceAll("%2C",",")*/.replaceAll("%3D","=").replaceAll("%26", "&").replaceAll("%252C", "%2C").replaceAll("sig=", "signature=").replaceAll("&s=", "&signature=").replaceAll("\\?s=", "?signature=");
            fmtUrlPair[1] = sortStringAt( fmtUrlPair[1], "&" ) ;
            Log.d(fmtUrlPair[1],"YOYOSANTOS");

        }

    }

    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }
}


