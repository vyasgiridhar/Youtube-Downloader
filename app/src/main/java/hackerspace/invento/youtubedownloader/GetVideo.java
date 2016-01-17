package hackerspace.invento.youtubedownloader;

import android.content.Context;;
import android.util.Log;
import android.util.SparseArray;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * Created by night-crawler on 12/11/15.
 */
public class GetVideo {

    String URL;
    Context context;
    String Quality;
    Video Vid;
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
                    YtFile obj = ytFiles.get(key);
                    Log.d("Final URL",obj.getUrl());
                }
            }
        }
    };

    public void run(){
        this.YtEX.setIncludeWebM(false);
        YtEX.setParseDashManifest(true);
        this.YtEX.execute(URL);

    }

}