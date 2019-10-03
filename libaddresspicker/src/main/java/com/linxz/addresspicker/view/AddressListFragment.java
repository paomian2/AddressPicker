package com.linxz.addresspicker.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linxz.addresspicker.R;
import com.linxz.addresspicker.adapter.AddressListAdapter;
import com.linxz.addresspicker.interfaces.AddressListClickListener;
import com.linxz.addresspicker.interfaces.OnAddressItemClickListener;
import com.linxz.addresspicker.model.AddressListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linxz
 * 创建日期：2019年09月29日 10:05
 * version：v4.5.4
 * 描述：
 */
public class AddressListFragment extends Fragment {

    private View contentView;
    private RecyclerView rvAddress;
    private AddressListAdapter adapter;
    private List<AddressListBean> mList = new ArrayList<>();
    private AddressListClickListener mAddressListClickListener;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.lib_frag_address_list, null);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setListener();
    }

    private void initView() {
        rvAddress = contentView.findViewById(R.id.rvAddress);
        adapter = new AddressListAdapter(getActivity(),mList);
        rvAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAddress.setAdapter(adapter);
    }

    private void setListener() {
        adapter.setOnAddressItemClickListener(new OnAddressItemClickListener() {
            @Override
            public void onClick(AddressListBean address) {
                if (mAddressListClickListener!=null){
                    mAddressListClickListener.onClick(address);
                    for (AddressListBean bean:mList){
                        bean.setSelect(false);
                    }
                    address.setSelect(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void setAddressData(List<AddressListBean> list) {
        if (list==null){
            return;
        }
        mList.clear();
        mList.addAll(list);
        for (AddressListBean bean:mList){
            bean.setSelect(false);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void setAddressListClickListener(AddressListClickListener listener){
        this.mAddressListClickListener=listener;
    }


}
