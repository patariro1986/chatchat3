package jp.co.teikoku.takenori.chatchat;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerArticleAdapter extends RecyclerView.Adapter<RecyclerArticleAdapter.ViewHolder> {

    private List<ArticleComment> mDataset;
    private List<String> mTopicinfo;
    private View.OnClickListener listener;
    private String mArticleDate;
    private String mArticleTitle;
    private String mArticleTopic;
    private boolean mfirstOpenArticle=false;
    protected void onCheckedChangedRecycle(CompoundButton comButton, boolean isChecked) {
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new Sample1ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(Sample1ItemViewHolder.LAYOUT_ID, parent, false));
            case 1:
                return new Sample2ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(Sample2ItemViewHolder.LAYOUT_ID, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ((Sample1ItemViewHolder)holder).onBindItemViewHolder(holder);
                break;
            case 1:
                ((Sample2ItemViewHolder)holder).onBindItemViewHolder(holder,position-1);
                break;

        }

    }
    @Override
    public int getItemViewType(int position) {
        int type=0;
        if(position==0){
            type=0;
        }else{
            type=1;
        }
        return type;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView mTextViewComments;
        public TextView mTextViewComentdate;
        public TextView mTextViewTopicDate;
        public TextView mTextViewTopicTitle;
        public TextView mTextViewContents;
        public TextView mTextViewUsername;
        public ToggleButton mToggleButton;
        public CardView mCardArticle;

        public ViewHolder(View v) {
            super(v);
            mToggleButton = (ToggleButton) v.findViewById(R.id.toggleButton);

        }
    }
    public class Sample1ItemViewHolder extends ViewHolder {
        public static final int LAYOUT_ID = R.layout.row_article_title;


        public Sample1ItemViewHolder(View v) {
            super(v);
            mTextViewTopicDate = (TextView)v.findViewById(R.id.text_view_date);
            mTextViewTopicTitle = (TextView)v.findViewById(R.id.text_view_title);
            mTextViewContents = (TextView)v.findViewById(R.id.textView_contents);

        }

        public void onBindItemViewHolder(ViewHolder holder) {
            holder.mTextViewTopicDate.setText(mTopicinfo.get(0));
            holder.mTextViewTopicTitle.setText(mTopicinfo.get(1));
            holder.mTextViewContents.setText(mTopicinfo.get(2));
            //はじめにチェックしてから、リスナー登録しないと、トグルボタンを設定した瞬間イベントが発生するので順序に気をつける
            if(mTopicinfo.get(3)=="LIKE"){
                mToggleButton.setChecked(true);
            }else {
                mToggleButton.setChecked(false);
            }
            holder.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton comButton, boolean isChecked){
                    onCheckedChangedRecycle( comButton,  isChecked);
                }
            });
        }
    }

    public class Sample2ItemViewHolder extends ViewHolder {
        public static final int LAYOUT_ID = R.layout.row_article;

        public Sample2ItemViewHolder(View v) {
            super(v);
            mTextViewComments = (TextView)v.findViewById(R.id.text_view_comments);
            mTextViewComentdate = (TextView)v.findViewById(R.id.text_comentdate);
            mTextViewUsername = (TextView)v.findViewById(R.id.text_username);
            mCardArticle=v.findViewById(R.id.card_article);
        }

        public void onBindItemViewHolder(ViewHolder holder,int position) {
            holder.mTextViewComments.setText(mDataset.get(position).getComment());
            holder.mTextViewComentdate.setText(getCreateRecordDate(position));
            holder.mTextViewUsername.setText(mDataset.get(position).getName());
            holder.mCardArticle.setCardBackgroundColor(Color.rgb(mDataset.get(position).getColorRed(),mDataset.get(position).getColorGreen(),mDataset.get(position).getColorBlue()));
        }
        private String getCreateRecordDate(int position) {
            Date date=mDataset.get(position).getTime();
            String commentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
            return commentDate;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerArticleAdapter(List<ArticleComment> myDataset, List<String> topicInfo) {
        mDataset = myDataset;
        mTopicinfo=topicInfo;
        mfirstOpenArticle=true;

    }




    public void setOnItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    public void addItem(int position, ArticleComment info) {
        if(mDataset!=null) {
            mDataset.add(position, info);
            notifyItemInserted(position);
        }

    }
    public void updateItem(int position, ArticleComment info) {
        if(mDataset!=null) {
            notifyItemChanged(position,info);
        }

    }
    public void deleteItem(ArticleComment item) {
        if (mDataset != null) {
            int index = mDataset.indexOf(item);
            if (-1 != index) {
                boolean isDelete = mDataset.remove(item);
                if (isDelete) {
                    notifyItemRemoved(index);
                }
            }
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size()+1;
     //   return mDataset.size()+mTopicinfo.size();
    }
}
