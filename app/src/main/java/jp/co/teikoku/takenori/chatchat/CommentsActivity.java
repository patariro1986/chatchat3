package jp.co.teikoku.takenori.chatchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    private DatabaseReference myRef;
    private DatabaseReference mTopicRef;
    private RecyclerView mRecyclerView;
    private RecyclerArticleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ArticleComment> myDataset = new ArrayList<ArticleComment>();
    private ArticleComment mComment;
    private  EditText mTitleEditText;
    private  String mUserName;
    private  String mUid;
    private Topic mTopic;
    private int mRedColor=100;
    private int mBlueColor=100;
    private int mGreenolor=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        //Add action button

        Intent intent =getIntent();
        String id = intent.getStringExtra("ID");
        String date = intent.getStringExtra("Date");
        String title = intent.getStringExtra("Title");
        String topic = intent.getStringExtra("Topic");
         mUserName = intent.getStringExtra("UserName");
        mUid = intent.getStringExtra("UID");
        String like="";
        if(intent.getStringExtra("Like").equals("LIKE")){
            like="LIKE";
        }
        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
        mRedColor= pref.getInt("ColorRed",255);
        mGreenolor=pref.getInt("ColorGreen",255);
        mBlueColor=pref.getInt("ColorBlue",255);
        mRecyclerView = findViewById(R.id.recyclerView_article);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Reference DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Main").child("Comments").child(id);
        mTopicRef = database.getReference("Main").child("Topics").child(id);

        // Set TestAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
       /* mTopicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    mTopic=new Topic();
                    mTopic=dataSnapshot.getValue(Topic.class);
                }
                // 保存した情報を用いた描画処理などを記載する。
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ログを記録するなどError時の処理を記載
            }
        });*/
        //ここで、データーをセットする
        List<String> topicinfo=new ArrayList<String >();
        topicinfo.add(date);
        topicinfo.add(title);
        topicinfo.add(topic);
        topicinfo.add(like);
        mAdapter = new RecyclerArticleAdapter(myDataset,topicinfo){
            @Override
            protected void onCheckedChangedRecycle(CompoundButton comButton, final boolean isChecked){
                mTopicRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Topic t = mutableData.getValue(Topic.class);
                        if (t == null) {
                            return Transaction.success(mutableData);
                        }

                        if (isChecked==true) {
                            // Unstar the post and remove self from stars
                            t.setRate(t.getRate()+1);
                            t.stars.put(mUid,true);
                        } else {
                            // Star the post and add self to stars
                            t.setRate(t.getRate()-1);
                            t.stars.remove(mUid);
                        }

                        // Set value and report transaction success
                        mutableData.setValue(t);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed

                    }
                });
            }
        };
        // Set TestAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        //ボタンのクリックイベント
        findViewById(R.id.button2).setOnClickListener(button1ClickListener);
        mTitleEditText = findViewById(R.id.editText2);


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                //  Logger.log( dataSnapshot.child("topicid").getValue(String.class));

                //    dataSnapshot.child("topicid").getValue();
                mComment = new ArticleComment();
                mComment = dataSnapshot.getValue(ArticleComment.class);

               // mAdapter.addItem(0, mComment);
                mAdapter.addItem(mAdapter.getItemCount()-1, mComment);
                mAdapter.updateItem(mAdapter.getItemCount()-1,mComment);
                mLayoutManager.scrollToPosition(mAdapter.getItemCount()-1);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addChildEventListener(childEventListener);
        // idがtoggleのトグルボタンを取得

    }

    View.OnClickListener button1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //  finish();
            if(mTitleEditText.getText().toString().equals("")){
                return;
            }
            //setting color
            int color[]=new int[3];
            color[0]= mRedColor;
            color[1]=mGreenolor;
            color[2]=mBlueColor;
            mComment = new ArticleComment(new Date(),mTitleEditText.getText().toString(),mUserName,mUid,color);
            sendTopic(mComment,myRef);
            //Delete all text
            mTitleEditText.setText("");
        }
    };

    // Sending topic to DB
    public void sendTopic(ArticleComment test,DatabaseReference ref) {
        String key = ref.push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put(key, test.toMap());
        ref.updateChildren(map);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return true;
    }
}
