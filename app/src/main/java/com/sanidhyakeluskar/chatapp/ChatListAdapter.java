package com.sanidhyakeluskar.chatapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter{
    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapShortList;

    private ChildEventListener mListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mSnapShortList.add(dataSnapshot);
            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference ref, String name){
        mActivity=activity;
        mDatabaseReference=ref.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
        mDisplayName=name;
        mSnapShortList=new ArrayList<>();
    }

    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams Params;
    }

    @Override
    public int getCount() {
        return mSnapShortList.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot=mSnapShortList.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.chat_msg_row,parent,false);
            final ViewHolder holder=new ViewHolder();
            holder.authorName=(TextView) convertView.findViewById(R.id.author);
            holder.body=(TextView) convertView.findViewById(R.id.message);
            holder.Params=(LinearLayout.LayoutParams)holder.authorName.getLayoutParams();
            convertView.setTag(holder);
        }
        final InstantMessage message=getItem(position);
        final ViewHolder holder=(ViewHolder) convertView.getTag();
        boolean itsMe= message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(itsMe,holder);

        String author=message.getAuthor();
        holder.authorName.setText(author);
        String msg=message.getMessage();
        holder.body.setText(msg);
        return convertView;
    }

    private void setChatRowAppearance(boolean isItMe,ViewHolder holder){
        if(isItMe){
            holder.Params.gravity= Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }
        else{
            holder.Params.gravity= Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.Params);
        holder.body.setLayoutParams(holder.Params);

    }

    public void cleanup(){
        mDatabaseReference.removeEventListener(mListener);
    }
}
