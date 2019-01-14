package jp.co.teikoku.takenori.chatchat;

public class UserInformation {
    private String mUID;
    private String mName;


    public UserInformation(){

    }
    public UserInformation( String name, String uid){
        this.mName = name;
        this.mUID = uid;
    }

    public String getName(){
        return mName;
    }
    public void setName(String name){
        mName=name;
    }
    public String getUID(){
        return mUID;
    }
    public void setUID(String uid){
        mUID=uid;
    }


}
