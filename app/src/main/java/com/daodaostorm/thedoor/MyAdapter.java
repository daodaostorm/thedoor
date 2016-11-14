package com.daodaostorm.thedoor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.daodaostorm.thedoor.common.view.AsyncImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener {

    private List<MessageObj> mData;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview,viewGroup,false);
        MyViewHolder vh = new MyViewHolder(v);
        v.setOnClickListener(this);
        return vh;
    }

    public MyAdapter(List<MessageObj> data){
        mData = data;
    }
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.mUsername.setText(mData.get(i).getUsername());
        myViewHolder.mUserIcon.setImageResource(mData.get(i).getIcon());
        //myViewHolder.mCount.setText(mData.get(i).getCount());
        myViewHolder.mTitle.setText(mData.get(i).getTitle());
        myViewHolder.mContent.setText(mData.get(i).getContent());
        myViewHolder.itemView.setTag(mData.get(i).getTitle());
		myViewHolder.mtestPic.setImageUrl(mData.get(i).getimageUrl());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            String title = "test";
            //MyViewHolder itemviewHolder = (MyViewHolder)view.getTag();
            title = (String) view.getTag();
            mOnItemClickListener.onItemClick(view, title);
        }
    }

	public void add(MessageObj msgObj, int position) {
        mData.add(position, msgObj);
        notifyItemInserted(position);
    }
	
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mUsername;
        //public TextView mCount;
        public TextView mTitle;
        public TextView mContent;
		public ImageView mUserIcon;
        public AsyncImageView mtestPic;
        public MyViewHolder(View itemView) {
            super(itemView);
            mUsername = (TextView) itemView.findViewById(R.id.username);
            mUserIcon = (ImageView) itemView.findViewById(R.id.usericon);
			mtestPic = (AsyncImageView) itemView.findViewById(R.id.testpic);
            //mCount = (TextView) itemView.findViewById(R.id.count);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mContent = (TextView) itemView.findViewById(R.id.content);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String title);
    }
}
