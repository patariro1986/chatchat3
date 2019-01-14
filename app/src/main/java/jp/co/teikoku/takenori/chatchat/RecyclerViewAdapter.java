package jp.co.teikoku.takenori.chatchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Topic> mDataset;
    private UserInformation mUser;
    private View.OnClickListener listener;
    protected void onClickedRecycle(int version,String date,String title, String topic,String rate,String like) {
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mTextView2;
        public TextView mTextViewTopicDate;
        public ImageView mImageViewLike;
        public TextView mTextViewRate;


        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.text_view);
            mTextView2 = (TextView)v.findViewById(R.id.text_view2);
            mTextViewTopicDate = (TextView)v.findViewById(R.id.text_topicdate);
            mImageViewLike = (ImageView) v.findViewById(R.id.imageViewLike);
            mTextViewRate = (TextView)v.findViewById(R.id.textView_rate);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(List<Topic> myDataset,UserInformation userInfo) {
        mDataset = myDataset;
        mUser=userInfo;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view

        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row, parent, false);


        // set the view's size, margins, paddings and layout parameters


        final ViewHolder vh = new ViewHolder(view);
        // onCreateViewHolder でリスナーをセット
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = vh.getAdapterPosition();
                String stateForlike="";
                if(vh.mImageViewLike.getVisibility()==View.VISIBLE){
                    stateForlike="LIKE";
                }
                onClickedRecycle(position,getCreateRecordDate(position),vh.mTextView.getText().toString(),vh.mTextView2.getText().toString() ,vh.mTextViewRate.getText().toString(),stateForlike);
                return;
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getTitle());
        holder.mTextView2.setText(mDataset.get(position).getContents());
        holder.mTextViewTopicDate.setText(getCreateRecordDate(position));
        if(exsitUid(position,mUser.getUID())){
            holder.mImageViewLike.setVisibility(View.VISIBLE);
        }else{
            holder.mImageViewLike.setVisibility(View.INVISIBLE);
        }
        holder.mTextViewRate.setText(String.valueOf(mDataset.get(position).getRate()));

    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    public void addItem(int position, Topic info) {
        mDataset.add(position, info);
        notifyItemInserted(position);
    }
    public String getDBItemID(int position) {
        String id=mDataset.get(position).getTopicID();
        return id;
    }
    public String getCreateRecordDate(int position) {
        Date date=mDataset.get(position).getTime();
        String topicDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
        return topicDate;
    }
    public boolean exsitUid(int position,String uid) {
        boolean flag=false;
        if(mDataset.get(position).stars.size()==0){
            return false;
        }
        if(mDataset.get(position).stars.containsKey(uid)){
            flag=true;
        }

        return flag;
    }
    public void updateRecyclerView(int position,Topic item) {
        mDataset.set(position,item);
        notifyItemChanged(position, item);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
