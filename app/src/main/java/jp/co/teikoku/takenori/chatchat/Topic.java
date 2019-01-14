package jp.co.teikoku.takenori.chatchat;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Topic {
    private Date mTime;
    private String uid;
    private String mName;
    private int mRate;
    private String mTitle;
    private String mContents;
    private String topicid;
    public Map<String, Boolean> stars = new HashMap<>();
    public Topic(){

    }
    public Topic(Date time, String user_uid,String name,int rate,String title,String contents,String id){
        this.mTime = time;
        this.uid = user_uid;
        this.mName = name;
        this.mRate = rate;
        this.mTitle = title;
        this.mContents = contents;
        this.topicid = id;

    }
    public Date getTime(){
        return mTime;
    }
    public void setTime(Date time){
        mTime=time;
    }
    public String getUID(){
        return uid;
    }
    public void setUID(String user_uid){
        uid=user_uid;
    }
    public String getName(){
        return mName;
    }
    public void setName(String name){
        mName=name;
    }
    public int getRate(){
        return mRate;
    }
    public void setRate(int rate){
        mRate=rate;
    }
    public String getTitle(){
        return mTitle;
    }
    public void setTitle(String title){
         mTitle=title;
    }
    public String getContents(){
        return mContents;
    }
    public void setContents(String contents){
        mContents=contents;
    }
    public String getTopicID(){
        return topicid;
    }
    public void setTopicID(String id){
        this.topicid =id;
    }



    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("time", mTime);
        hashmap.put("uid", uid);
        hashmap.put("name", mName);
        hashmap.put("rate", mRate);
        hashmap.put("title", mTitle);
        hashmap.put("contents", mContents);
        hashmap.put("topicid", topicid);
        hashmap.put("stars", stars);
        return hashmap;
    }

}
