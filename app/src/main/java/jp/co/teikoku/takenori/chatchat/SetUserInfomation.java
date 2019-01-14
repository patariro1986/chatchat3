package jp.co.teikoku.takenori.chatchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.PriorityQueue;

public class SetUserInfomation extends AppCompatActivity {
    private int mRedColor=100;
    private int mBlueColor=100;
    private int mGreenolor=100;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TextView mtextUsername;
    private SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_infomation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Home/Upナビゲーション
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
         mUser = mAuth.getCurrentUser();
        final TextView textv=findViewById(R.id.textViewColore);
        mtextUsername=findViewById(R.id.textview_username);
        mtextUsername.setText(mUser.getDisplayName());
        final SeekBar sbRed = (SeekBar)findViewById(R.id.seekBarRed);
        final SeekBar sbGreen = (SeekBar)findViewById(R.id.seekBarGreen);
        final SeekBar sbBlue = (SeekBar)findViewById(R.id.seekBarBlue);
        mPref=PreferenceManager.getDefaultSharedPreferences(this);
        mRedColor= mPref.getInt("ColorRed",100);
        mGreenolor=mPref.getInt("ColorGreen",100);
        mBlueColor=mPref.getInt("ColorBlue",100);
        sbRed.setProgress(mRedColor);
        sbGreen.setProgress(mGreenolor);
        sbBlue.setProgress(mBlueColor);
        textv.setBackgroundColor(Color.rgb(mRedColor,mGreenolor,mBlueColor));
        findViewById(R.id.buttonSave).setOnClickListener(button1ClickListener);
        sbRed.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        mRedColor=progress;
                        textv.setBackgroundColor(Color.rgb(mRedColor,mGreenolor,mBlueColor));
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // ツマミに触れたときに呼ばれる
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        textv.setBackgroundColor(Color.rgb(mRedColor,mGreenolor,mBlueColor));
                    }
                }
        );
        sbGreen.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        mGreenolor=progress;
                        textv.setBackgroundColor(Color.rgb(mRedColor,mGreenolor,mBlueColor));
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // ツマミに触れたときに呼ばれる
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        textv.setBackgroundColor(Color.rgb(mRedColor,mGreenolor,mBlueColor));
                        // ツマミを離したときに呼ばれる
                    }
                }
        );
        sbBlue.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        mBlueColor=progress;
                        textv.setBackgroundColor(Color.rgb(mRedColor,mGreenolor,mBlueColor));
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // ツマミに触れたときに呼ばれる
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        textv.setBackgroundColor(Color.rgb(mRedColor,mGreenolor,mBlueColor));
                        // ツマミを離したときに呼ばれる
                    }
                }
        );
    }
    View.OnClickListener button1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mtextUsername.getText().toString().equals("")){
                mtextUsername.setError("Don't blank!!");
                return;
            }
            SharedPreferences.Editor e = mPref.edit();
            e.putString("UserName",mtextUsername.getText().toString());
            e.putInt("ColorRed",mRedColor);
            e.putInt("ColorGreen",mGreenolor);
            e.putInt("ColorBlue",mBlueColor);
            e.commit();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mtextUsername.getText().toString())
                    .build();
            //インテントの作成
            mUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SetUserInfomation.this,"Change user name success!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    };


}
