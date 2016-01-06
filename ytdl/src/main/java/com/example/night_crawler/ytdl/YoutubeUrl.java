package com.example.night_crawler.ytdl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class YoutubeUrl {
	
	private String quality = null;			// quality text from html source code
    private String youtubeId = null;		// unique youtube video ID (11 alphanum letters) from html source code
	private String htmlType = null;			// type from html source code
	private String url = null;				// URL to video of certain format (mpg, flv, webm, mp4, ..?)
	private String size = null;				// dimension of video provided in url
	private String respart = null; 			// e.g. "LD" or "3D" for filename suffix

	private String title = null;			// the video title
	private int percentage = -1;			// % of download finished
	
	public void setPercentage(int p) {
		this.percentage = p;
	}

	private String audioStreamUrl = null;	// if != null URL to audio stream to downlaod in another thread (applies to itag>102)
	
	private boolean isDownloading = false;  // is currently beeing downloaded

	public YoutubeUrl(String url) {
		this.title="";
		this.url = url;
	}
	
	public YoutubeUrl(String url, String videoUrl) {
		this.title="";
		this.url = url;
		this.extractUrlParameters(url, videoUrl);
	}
	
	public YoutubeUrl(String url, String videoUrl, String respart) {
		this.url = url;
		this.title = "";
		this.extractUrlParameters(url, videoUrl);
	}


	public YoutubeUrl(String url, String videoURL, String respart, String audioStreamUrl) {
		this.title="";
		this.url = url;

	}
	

	private void extractUrlParameters(String url, String videoUrl) {
		try {


			this.youtubeId = videoUrl.substring( videoUrl.indexOf("v=")+2);
			String html = Jsoup.connect(videoUrl).get().html();
			/*
			String[] tempSplitedUrl = url.split("&");

			for (int i = 0; i < tempSplitedUrl.length; i++) {
				if(tempSplitedUrl[i].startsWith("quality")) {
					this.quality = tempSplitedUrl[i].substring( tempSplitedUrl[i].indexOf('=')+1);
				}
				if(tempSplitedUrl[i].startsWith("itag")) {
					this.htmlTagId = tempSplitedUrl[i].substring( tempSplitedUrl[i].indexOf('=')+1);
				}
				if(tempSplitedUrl[i].startsWith("type")) {
					this.htmlType = tempSplitedUrl[i].substring( tempSplitedUrl[i].indexOf('=')+1);
				}
				if(tempSplitedUrl[i].startsWith("stereo3d")) {
					this.stereo3D = tempSplitedUrl[i].substring( tempSplitedUrl[i].indexOf('=')+1);
				}
				if(tempSplitedUrl[i].startsWith("size")) {
					this.size = tempSplitedUrl[i].substring( tempSplitedUrl[i].indexOf('=')+1);
				}
			}*/
			
		} catch (NullPointerException npe) {
			//TODO catch must not be empty - happens if URL for selection resolution could not be found
		}catch (IOException e){

		}
	} 
	
	
	public String getSize() {
		return this.size;
	}
	
	public String getQuality() {
		return this.quality==null?"":this.quality;
	}


	public String getYoutubeId() {
		return this.youtubeId;
	}


	public String getUrl() {
		return this.url;
	}
	
	public String getHtmlType() {
		return this.htmlType;
	}
	
	public String getRespart() {
		return this.respart==null?"":this.respart;
	}
	
	public void setRespart(String respart) {
		this.respart = respart;
	}

	public String getAudioStreamUrl() {
		return this.audioStreamUrl==null?"":this.audioStreamUrl;
	}

	public void setDownloading() {
		this.isDownloading = true;
	}
	
	public void setDownloadingFinished() {
		this.isDownloading = false;
		this.percentage = -1;	
	}
	
	public Boolean isDownloading() {
		return this.isDownloading;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
} 
