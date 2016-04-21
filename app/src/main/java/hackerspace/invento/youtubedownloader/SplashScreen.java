package hackerspace.invento.youtubedownloader;

/**
 * Created by vyas on 2/10/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;


public class SplashScreen extends Activity {

    private static String youtubeLink;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Check how it was started and if we can get the youtube link
        if (savedInstanceState == null && Intent.ACTION_SEND.equals(getIntent().getAction())
                && getIntent().getType() != null && "text/plain".equals(getIntent().getType())) {

            String ytLink = getIntent().getStringExtra(Intent.EXTRA_TEXT);

            if (ytLink != null
                    && (ytLink.contains("://youtu.be/") || ytLink.contains("youtube.com/watch?v="))) {
                youtubeLink = ytLink;

                new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity

                        Intent i = new Intent(SplashScreen.this, DownloadActivity.class);
                        i.putExtra("URL",youtubeLink);
                        startActivity(i);

                        // close this activity
                        finish();
                    }
                }, SPLASH_TIME_OUT);
                // We have a valid link
            } else {
                Toast.makeText(this,"Not a Youtube link", Toast.LENGTH_LONG).show();
                finish();
            }
        } else if (savedInstanceState != null && youtubeLink != null) {
            finish();
        } else {
            finish();
        }
    }

}