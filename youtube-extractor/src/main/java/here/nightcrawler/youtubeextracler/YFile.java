package here.nightcrawler.youtubeextracler;

/**
 * Created by vyas on 14/1/16.
 */
public class YFile {

    private Meta meta;
    private String url = "";

    YFile(Meta meta, String url) {
        this.meta = meta;
        this.url = url;
    }

    /**
     * The url to download the file.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Meta data for the specific file.
     */
    public Meta getMeta() {
        return meta;
    }
}
