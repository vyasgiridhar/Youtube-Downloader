package hackerspace.invento.youtubedownloader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.github.axet.vget.vhs.YoutubeInfo;

/**
 * Created by invento on 29/5/15.
 */
public class Vid_Download_Page extends Activity {

    Video vid = new Video();
    YoutubeInfo.YoutubeQuality Q = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_info);
        Intent I = getIntent();
        vid = (Video) I.getSerializableExtra("The Vid");

        findViewById(R.id.down_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Q == null){
                    Toast.makeText(getParent(),"Select a quality",Toast.LENGTH_SHORT).show();

                }
                else
                {}
            }
        });

    }

    public void P720(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        if(checked){
             Q= YoutubeInfo.YoutubeQuality.p720;
        }
    }

    public void P480(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        if(checked){
            Q= YoutubeInfo.YoutubeQuality.p480;
        }
    }

    public void P360(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        if(checked){
            Q= YoutubeInfo.YoutubeQuality.p360;
        }
    }

    public void P240(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        if(checked){
            Q= YoutubeInfo.YoutubeQuality.p240;
        }
    }

}
