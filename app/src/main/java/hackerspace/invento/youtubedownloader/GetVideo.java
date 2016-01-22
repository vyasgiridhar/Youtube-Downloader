package hackerspace.invento.youtubedownloader;

import android.content.Context;;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * Created by night-crawler on 12/11/15.
 */
public class GetVideo {

    String URL;
    Context context;
    List<YtFile> themfiles = new ArrayList<>();
    GetVideo(String u,Context c) {

        this.URL = u;
        this.context = c;

    }


    YouTubeUriExtractor YtEX = new YouTubeUriExtractor(context) {
        @Override
        public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
            if (ytFiles != null) {
                for(int i = 0; i < ytFiles.size(); i++) {
                    int key = ytFiles.keyAt(i);
                    // get the object by the key.
                    themfiles.add(ytFiles.get(key));
                }
            }
        }
    };

    public List<YtFile> run(){
        this.YtEX.setIncludeWebM(false);
        YtEX.setParseDashManifest(true);
        this.YtEX.execute(URL);
        while(YtEX.getStatus()== AsyncTask.Status.FINISHED){
            return this.themfiles;
        }
    return null;
    }

}