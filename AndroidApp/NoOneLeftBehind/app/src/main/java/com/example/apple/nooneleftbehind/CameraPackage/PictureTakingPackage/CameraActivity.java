package com.example.apple.nooneleftbehind.CameraPackage.PictureTakingPackage;

import android.app.Activity;
import android.os.Bundle;

import com.example.apple.nooneleftbehind.R;

/**
 * Activity that allows us to take picture
 */
public class CameraActivity extends Activity {

    //Used to return file path after taking picture
    //from fragment back to FaceTrackerActivity
    public static String EXTRA_DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //Starts fragment Camera2BasicFragment
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

    }
}
