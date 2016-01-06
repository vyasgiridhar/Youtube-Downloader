package hackerspace.invento.youtubedownloader;

import java.io.Serializable;

/**
 * Created by invento on 28/5/15.
 */
public class Video implements Serializable{

    private String title;
    private String Artist;
    private String imgurl;
    private String URL;

    public String getImgurl(){
        return imgurl;
    }

    public void setimgurl(String url){
        this.imgurl=url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        this.Artist = artist;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String url) {
        this.URL = url;
        URL =  "https://www.youtube.com/watch?v="+ URL;
    }


}
