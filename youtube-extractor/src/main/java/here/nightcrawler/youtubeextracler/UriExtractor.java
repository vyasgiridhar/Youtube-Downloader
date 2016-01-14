package here.nightcrawler.youtubeextracler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vyas on 14/1/16.
 */
public abstract class UriExtractor extends AsyncTask<String, Void ,SparseArray<YFile>> {


    private final static boolean CACHING = true;
    private final static boolean LOGGING = false;
    private final static String LOG_TAG = "UriExtractor";
    private final static String CACHE_FILE_NAME = "decipher_js_funct";
    private final static int DASH_PARSE_RETRIES = 5;

    private Context context;
    private String videoTitle;
    private String youtubeID;
    private boolean includeWebM = true;
    private boolean useHttp = false;
    private boolean parseDashManifest = false;

    private volatile String decipheredSignature;

    private static String decipherJsFileName;
    private static String decipherFunctions;
    private static String decipherFunctionName;

    private final Lock lock = new ReentrantLock();
    private final Condition jsExecuting = lock.newCondition();
    private static final Pattern patYouTubePageLink = Pattern.compile("(http|https)://(www\\.|m.|)youtube\\.com/watch\\?v=(.+?)( |\\z|&)");
    private static final Pattern patYouTubeShortLink = Pattern.compile("(http|https)://(www\\.|)youtu.be/(.+?)( |\\z|&)");

    private static final Pattern patDashManifest1 = Pattern.compile("dashmpd=(.+?)(&|\\z)");
    private static final Pattern patDashManifest2 = Pattern.compile("\"dashmpd\":\"(.+?)\"");
    private static final Pattern patDashManifestEncSig = Pattern.compile("/s/([0-9A-F|\\.]{10,}?)(/|\\z)");

    private static final Pattern patItag = Pattern.compile("itag=([0-9]+?)(&|,)");
    private static final Pattern patEncSig = Pattern.compile("s=([0-9A-F|\\.]{10,}?)(&|,|\")");
    private static final Pattern patUrl = Pattern.compile("url=(.+?)(&|,)");

    private static final Pattern patVaribleFunction = Pattern.compile("(\\{|;| |=)([a-zA-Z$][a-zA-Z0-9]{0,2})\\.([a-zA-Z$][a-zA-Z0-9$]{0,2})\\(");
    private static final Pattern patFunction = Pattern.compile("(\\{|;||=)([a-zA-Z$_][a-zA-Z0-9$]{0,2})\\(");
    private static final Pattern patDecryptionJsFile = Pattern.compile("jsbin\\\\/(player-(.+?).js)");
    private static final Pattern patSignatureDecFunction = Pattern.compile("\\(\"signature\",(.+?)\\(");

    private static final SparseArray<Meta> META_MAP = new SparseArray<>();

    static {
        // http://en.wikipedia.org/wiki/YouTube#Quality_and_formats

        // Video and Audio
        META_MAP.put(17, new Meta(17, "3gp", 144, Meta.VCodec.MPEG4, Meta.ACodec.AAC, 24, false));
        META_MAP.put(36, new Meta(36, "3gp", 240, Meta.VCodec.MPEG4, Meta.ACodec.AAC, 32, false));
        META_MAP.put(5, new Meta(5, "flv", 240, Meta.VCodec.H263, Meta.ACodec.MP3, 64, false));
        META_MAP.put(43, new Meta(43, "webm", 360, Meta.VCodec.VP8, Meta.ACodec.VORBIS, 128, false));
        META_MAP.put(18, new Meta(18, "mp4", 360, Meta.VCodec.H264, Meta.ACodec.AAC, 96, false));
        META_MAP.put(22, new Meta(22, "mp4", 720, Meta.VCodec.H264, Meta.ACodec.AAC, 192, false));

        // Dash Video
        META_MAP.put(160, new Meta(160, "mp4", 144, Meta.VCodec.H264, Meta.ACodec.NONE, true));
        META_MAP.put(133, new Meta(133, "mp4", 240, Meta.VCodec.H264, Meta.ACodec.NONE, true));
        META_MAP.put(134, new Meta(134, "mp4", 360, Meta.VCodec.H264, Meta.ACodec.NONE, true));
        META_MAP.put(135, new Meta(135, "mp4", 480, Meta.VCodec.H264, Meta.ACodec.NONE, true));
        META_MAP.put(136, new Meta(136, "mp4", 720, Meta.VCodec.H264, Meta.ACodec.NONE, true));
        META_MAP.put(137, new Meta(137, "mp4", 1080, Meta.VCodec.H264, Meta.ACodec.NONE, true));
        META_MAP.put(264, new Meta(264, "mp4", 1440, Meta.VCodec.H264, Meta.ACodec.NONE, true));
        META_MAP.put(266, new Meta(266, "mp4", 2160, Meta.VCodec.H264, Meta.ACodec.NONE, true));

        META_MAP.put(298, new Meta(298, "mp4", 720, Meta.VCodec.H264, 60, Meta.ACodec.NONE, true));
        META_MAP.put(299, new Meta(299, "mp4", 1080, Meta.VCodec.H264, 60, Meta.ACodec.NONE, true));

        // Dash Audio
        META_MAP.put(140, new Meta(140, "m4a", Meta.VCodec.NONE, Meta.ACodec.AAC, 128, true));
        META_MAP.put(141, new Meta(141, "m4a", Meta.VCodec.NONE, Meta.ACodec.AAC, 256, true));

        // WEBM Dash Video
        META_MAP.put(278, new Meta(278, "webm", 144, Meta.VCodec.VP9, Meta.ACodec.NONE, true));
        META_MAP.put(242, new Meta(242, "webm", 240, Meta.VCodec.VP9, Meta.ACodec.NONE, true));
        META_MAP.put(243, new Meta(243, "webm", 360, Meta.VCodec.VP9, Meta.ACodec.NONE, true));
        META_MAP.put(244, new Meta(244, "webm", 480, Meta.VCodec.VP9, Meta.ACodec.NONE, true));
        META_MAP.put(247, new Meta(247, "webm", 720, Meta.VCodec.VP9, Meta.ACodec.NONE, true));
        META_MAP.put(248, new Meta(248, "webm", 1080, Meta.VCodec.VP9, Meta.ACodec.NONE, true));
        META_MAP.put(271, new Meta(271, "webm", 1440, Meta.VCodec.VP9, Meta.ACodec.NONE, true));
        META_MAP.put(313, new Meta(313, "webm", 2160, Meta.VCodec.VP9, Meta.ACodec.NONE, true));

        META_MAP.put(302, new Meta(302, "webm", 720, Meta.VCodec.VP9, 60, Meta.ACodec.NONE, true));
        META_MAP.put(308, new Meta(308, "webm", 1440, Meta.VCodec.VP9, 60, Meta.ACodec.NONE, true));
        META_MAP.put(303, new Meta(303, "webm", 1080, Meta.VCodec.VP9, 60, Meta.ACodec.NONE, true));
        META_MAP.put(315, new Meta(315, "webm", 2160, Meta.VCodec.VP9, 60, Meta.ACodec.NONE, true));

        // WEBM Dash Audio
        META_MAP.put(171, new Meta(171, "webm", Meta.VCodec.NONE, Meta.ACodec.VORBIS, 128, true));

        META_MAP.put(249, new Meta(249, "webm", Meta.VCodec.NONE, Meta.ACodec.OPUS, 48, true));
        META_MAP.put(250, new Meta(250, "webm", Meta.VCodec.NONE, Meta.ACodec.OPUS, 64, true));
        META_MAP.put(251, new Meta(251, "webm", Meta.VCodec.NONE, Meta.ACodec.OPUS, 160, true));
    }

    public UriExtractor(Context con) {
        context = con;
    }


    @Override
    protected void onPostExecute(SparseArray<YFile> ytFiles) {
        onUrisAvailable(youtubeID, videoTitle, ytFiles);
    }

    public abstract void onUrisAvailable(String videoId, String videoTitle, SparseArray<YFile> ytFiles);

    @Override
    protected SparseArray<YFile> doInBackground(String... params){
        youtubeID = params[1];
        String ytUrl = params[0];
        try{
            return getStreamUrls();
        }catch(Exception E){
            Log.d(LOG_TAG, "doInBackground: Stream URL Failed");
        }
        return null;
    }
    private SparseArray<YFile> getStreamUrls() throws IOException , InterruptedException {

        String ytInfoUrl = (useHttp)? "http://" : "htpps://";
        ytInfoUrl += "www.youtube";
    }
}
