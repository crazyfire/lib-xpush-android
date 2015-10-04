package io.xpush.link.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import io.xpush.link.R;
import io.xpush.link.fragments.ProfileFragment;

/**
 * Created by James on 2015-09-05.
 */
public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = ProfileActivity.class.getSimpleName();

    private TextView mTvNickname;
    private TextView mTvStatusMessage;
    private ProfileFragment f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        f = new ProfileFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.content, f, TAG).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        //ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 수행을 제대로 한 경우
        if(resultCode == RESULT_OK && data != null) {

            if( requestCode == 103 ) {
                mTvNickname = (TextView) f.getView().findViewById(R.id.nickname);
                String nickname = data.getStringExtra("nickname");
                mTvNickname.setText(nickname);

                f.setNickName(nickname);
            } else if( requestCode == 104 ) {
                mTvStatusMessage = (TextView) f.getView().findViewById(R.id.status_message);
                String statusMessage = data.getStringExtra("statusMessage");
                mTvStatusMessage.setText(statusMessage);

                f.setStatusMessage(statusMessage);
            } else if ( requestCode == 110 ){
                Uri selectedImageUri = data.getData();
                f.setImage( selectedImageUri );
            }

        }  else if(resultCode == RESULT_CANCELED){
        }
    }
}