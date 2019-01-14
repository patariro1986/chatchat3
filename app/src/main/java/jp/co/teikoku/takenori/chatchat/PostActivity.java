package jp.co.teikoku.takenori.chatchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {
    private DatabaseReference myRef;
    private Topic mTopic;
    private int mRate=0;
    private FirebaseAuth mAuth;
    private EditText mtitleEditText;
    private EditText mscriptEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        // ツールバーをアクションバーとしてセット
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

        //Reference Auth
        mAuth = FirebaseAuth.getInstance();

        //Get UID

        //Make ID

        Button button = findViewById(R.id.postButton);
        final EditText titleEditText = findViewById(R.id.titleEditText);
        final EditText scriptEditText = findViewById(R.id.scriptEditText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                if(titleEditText.getText().toString().equals("")){
                    titleEditText.setError("Set title");
                    focusView=titleEditText;
                    focusView.requestFocus();
                    return;
                }
                if(scriptEditText.getText().toString().equals("")) {
                    scriptEditText.setError("Write detail");
                    focusView=scriptEditText;
                    focusView.requestFocus();
                    return;
                }
                FirebaseUser user = mAuth.getCurrentUser();
                String username=user.getDisplayName();
                String uid=user.getUid();
                String randomChar="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
               // String topicid=RandomStringUtils.random(10,randomChar);

                //Reference DB
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Main").child("Topics");
                String topicid= myRef.push().getKey();
                mTopic=new Topic(new Date(),uid,username,mRate,titleEditText.getText().toString(),scriptEditText.getText().toString(),topicid);
                sendTopic(mTopic,myRef);
                finish();

            }
        });
    }
    // Sending topic to DB
    public void sendTopic(Topic test,DatabaseReference ref) {
        //String key = ref.push().getKey();
        String key=test.getTopicID();
        Map<String, Object> map = new HashMap<>();
        map.put(key, test.toMap());
        ref.updateChildren(map);
    }
}