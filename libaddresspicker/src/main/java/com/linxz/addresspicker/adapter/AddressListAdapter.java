package com.linxz.addresspicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linxz.addresspicker.R;
import com.linxz.addresspicker.interfaces.OnAddressItemClickListener;
import com.linxz.addresspicker.model.AddressListBean;

import java.util.List;

/**
 * @author Linxz
 * 创建日期：2019年09月29日 10:09
 * version：v4.5.4
 * 描述：
 */
public class AddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AddressListBean> mList;
    private OnAddressItemClickListener mOnAddressItemClickListener;

    public AddressListAdapter(Context context,List<AddressListBean> list) {
        this.mContext=context;
        this.mList = list;
    }

    public void setOnAddressItemClickListener(OnAddressItemClickListener onAddressItemClickListener){
        this.mOnAddressItemClickListener=onAddressItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view= LayoutInflater.from(mContext).inflate(R.layout.lib_item_address,null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            Holder addressHoler = (Holder) holder;
            final AddressListBean address = mList.get(position);
            if (address.isSelect()) {
                addressHoler.imgSelect.setVisibility(View.VISIBLE);
                addressHoler.tvAddressName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                addressHoler.imgSelect.setVisibility(View.GONE);
                addressHoler.tvAddressName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
            addressHoler.tvAddressName.setText(address.getName());
            addressHoler.layoutMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAddressItemClickListener!=null){
                        mOnAddressItemClickListener.onClick(address);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        LinearLayout layoutMain;
        ImageView imgSelect;
        TextView tvAddressName;


        public Holder(View itemView) {
            super(itemView);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            imgSelect = itemView.findViewById(R.id.imgSelect);
            tvAddressName = itemView.findViewById(R.id.tvAddressName);
        }
    }
}
