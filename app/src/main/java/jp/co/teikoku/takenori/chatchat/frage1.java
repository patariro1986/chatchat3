package jp.co.teikoku.takenori.chatchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class frage1 extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Topic> myDataset = new ArrayList<Topic>();
    private Topic mTopic;
    private DatabaseReference myRef;
    private Main2Activity parent;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private UserInformation mUserInfo;
    public static frage1 newInstance() {
        frage1 fragment = new frage1();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frage_timeline, container, false);
        //Define button

        //RecyclerViewの定義
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Main").child("Topics");
        //
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserInfo=new UserInformation(mUser.getDisplayName(),mUser.getUid());
        // specify an adapter (see also next example)
        mAdapter = new RecyclerViewAdapter(myDataset,mUserInfo){
            @Override
            protected void onClickedRecycle(int positon,String date,String title,String topic,String rate,String like) {
                super.onClickedRecycle(positon,date,title,topic,rate,like);
                // Activity 側でタップされたときの処理を行う
                //Open Comments page
                String id=mAdapter.getDBItemID(positon);
                parent.move(id,date,title,topic,rate,like);
                return;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        // ボタンを設定
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                mTopic=new Topic();
                mTopic=dataSnapshot.getValue(Topic.class);
                mAdapter.addItem(0,mTopic);
                mLayoutManager.scrollToPosition(0);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                // A comment has changed, use the key to determine if we are displaying this

                mTopic=new Topic();
                mTopic=dataSnapshot.getValue(Topic.class);
                for(int i=0;i<myDataset.size();i++){
                    if(myDataset.get(i).getTopicID().equals(mTopic.getTopicID())){
                        mAdapter.updateRecyclerView(i,mTopic);
                        break;
                    }
                }
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
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        parent = (Main2Activity) activity;
        super.onAttach(activity);
    }


}
