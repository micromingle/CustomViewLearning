package com.jiandan.terence.firstdraglayout.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiandan.terence.firstdraglayout.R;

/**
 * Created by HP on 2017/8/16.
 */

public class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG="SimpleAdapter";
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
       // return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_simple,null));
        return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pager2,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder");
        if(holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mViewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return 3;
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    Log.d(TAG, "instantiateItem");
                    View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_image, null);
                    container.addView(view);
                    return view;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    Log.d(TAG, "destroyItem");
                    container.removeView((View) object);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
         public ViewPager mViewPager;
        public ViewHolder(View itemView) {
            super(itemView);
            mViewPager=(ViewPager)itemView.findViewById(R.id.view_pager);
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder{
        public ViewPager mViewPager;
        public ViewHolder2(View itemView) {
            super(itemView);
          //  mViewPager=(ViewPager)itemView.findViewById(R.id.view_pager);
        }
    }
}
