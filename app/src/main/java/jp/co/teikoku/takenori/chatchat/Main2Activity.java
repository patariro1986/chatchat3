package jp.co.teikoku.takenori.chatchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {

    public FirebaseUser mUser;
    private UserInformation mUserinfo;
    private FirebaseAuth mAuth;
    static final int RESULT_SUBACTIVITY = 1000;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = frage1.newInstance();
                    break;

                case R.id.navigation_notifications:
                    selectedFragment = frage2.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ボタンのクリックイベント
        findViewById(R.id.buttonCreateTopic).setOnClickListener(button1ClickListener);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //mUser id = Uid を取得する
        String uid = mUser.getUid();
        String name = mUser.getDisplayName();
        mUserinfo=new UserInformation(name,uid);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, frage1.newInstance());
        transaction.commit();
    }
    View.OnClickListener button1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), PostActivity.class);
            startActivity(intent);
        }
    };
    void move(String id,String topicDate,String topicTitle,String topic,String rate, String like) {
        Intent intent = new Intent(getApplication(), CommentsActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("Date", topicDate);
        intent.putExtra("Title", topicTitle);
        intent.putExtra("Topic", topic);
        intent.putExtra("UserName", mUserinfo.getName());
        intent.putExtra("UID", mUserinfo.getUID());
        intent.putExtra("Rate", rate);
        intent.putExtra("Like", like);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(getApplication(), SetUserInfomation.class);
                startActivity(intent);
                break;
            case R.id.action_settings2:
                mAuth.signOut();

                intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

