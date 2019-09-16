package com.example.xuversion.search_activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuversion.R;
import com.example.xuversion.model.Building;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchRecordsAdapter extends BaseAdapter {

    private List<Building> searchRecordsList;
    private LayoutInflater inflater;

    /**
     * Constructor
     * @param context the context
     * @param searchRecordsList the list to store search record
     */
    public SearchRecordsAdapter(Context context, List<Building> searchRecordsList) {
        this.searchRecordsList = searchRecordsList;
        inflater = LayoutInflater.from(context);

    }

    /**
     * Get the size of the record list
     * @return
     */
    @Override
    public int getCount() {
        return searchRecordsList.size() == 0 ? 0 : searchRecordsList.size();
    }

    /**
     * Get the item at specified position
     * @param position the specified position
     * @return the item
     */
    @Override
    public Object getItem(int position) {
        return searchRecordsList.size() == 0 ? null : searchRecordsList.get(position);
    }

    /**
     * Get the item ID of a position
     * @param position the position
     * @return the ID
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get the view filled with content
     * @param position the position of the class
     * @param convertView the view need to be converted
     * @param parent the parent view
     * @return the converted view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            convertView = inflater.inflate(R.layout.item_search_records_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String content = searchRecordsList.get(position).getName();
        viewHolder.recordTv.setText(content);
        viewHolder.recordImg.setImageDrawable(viewHolder.getdrawable());
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.search_content_tv) TextView recordTv;
        @BindView(R.id.search_content_img) ImageView recordImg;

        @BindDrawable(R.drawable.ic_sport) Drawable sport;
        @BindDrawable(R.drawable.clock) Drawable clock;
        @BindDrawable(R.drawable.ic_study) Drawable study;
        @BindDrawable(R.drawable.ic_life) Drawable life;
        @BindDrawable(R.drawable.ic_work) Drawable work;

        /**
         * Holder for the view
         * @param view the view that need to be held
         */
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        /**
         * Get the drawable
         * @return the drawable for sport
         */
        public Drawable getdrawable(){
            return sport;
        }
    }
}

