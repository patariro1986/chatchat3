package jp.co.teikoku.takenori.chatchat;

import android.text.LoginFilter;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ArticleComment {
    private Date mCommentTime;
    private String mComment;
    private String UID;
    private String mName;
    private int mColorRed;
    private int mColorGreen;
    private int mColorBlue;


    public ArticleComment(){
        mColorRed=255;
        mColorGreen=255;
        mColorBlue=255;
    }
    public ArticleComment(Date time, String comment,String name,String uid,int[] color){
        this.mCommentTime = time;
        this.mComment = comment;
        this.mName = name;
        this.UID = uid;
        this.mColorRed=color[0];
        this.mColorGreen=color[1];
        this.mColorBlue=color[2];

    }
    public Date getTime(){
        return mCommentTime;
    }
    public void setTime(Date time){
        mCommentTime =time;
    }
    public String getComment(){
        return mComment;
    }
    public void setComment(String comment){
        mComment =comment;
    }

    public String getName(){

        return mName;
    }
    public void setName(String name){
        mName=name;
    }
    public String getUID(){
        return UID;
    }
    public void setUID(String uid){
        UID=uid;
    }
    public int getColorRed(){
        return mColorRed;
    }
    public int getColorGreen(){
        return mColorGreen;
    }
    public int getColorBlue(){
        return mColorBlue;
    }
    public void setcolorRed(int red){
        mColorRed =red;
    }
    public void setcolorGreen(int green){
        mColorGreen =green;
    }
    public void setcolorBlue(int blue){
        mColorBlue =blue;
    }
        @Exclude
        public Map<String, Object> toMap(){
            HashMap<String, Object> hashmap = new HashMap<>();
            hashmap.put("time", mCommentTime);
            hashmap.put("UID", UID);
            hashmap.put("name", mName);
            hashmap.put("comment", mComment);
            hashmap.put("colorRed", mColorRed);
            hashmap.put("colorBlue", mColorBlue);
            hashmap.put("colorGreen", mColorGreen);
            return hashmap;
        }

}
