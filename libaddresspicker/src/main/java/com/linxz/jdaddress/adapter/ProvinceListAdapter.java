package com.linxz.jdaddress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.linxz.jdaddress.model.Province;

import java.util.List;

/**
 * @author Linxz
 * 创建日期：2019年09月28日 14:44
 * version：v4.5.4
 * 描述：
 */
public class ProvinceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Province> mList;

    public ProvinceListAdapter(Context context,List<Province> list){
        this.mContext=context;
        this.mList=list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        public Holder(View itemView) {
            super(itemView);
        }
    }
}
