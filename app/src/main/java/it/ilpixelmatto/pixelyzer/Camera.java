package it.ilpixelmatto.pixelyzer;

/**
 * Created by leopa on 11/06/2016.
 */

import android.app.Activity;
import android.os.Bundle;

public class Camera extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }

}
