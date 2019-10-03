package com.linxz.jdaddress.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.linxz.addresspicker.R;

/**
 * @author Linxz
 * 创建日期：2019年09月28日 13:52
 * version：v4.5.4
 * 描述：
 */
public class AddressView extends FrameLayout {

    private TextView textViewProvince;
    private TextView textViewCity;
    private TextView textViewCounty;
    private TextView textViewStreet;
    private RecyclerView rvAddress;

    public AddressView(@NonNull Context context) {
        super(context);
    }

    public AddressView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AddressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.lib_address_view,this);
        textViewProvince=findViewById(R.id.textViewProvince);
        textViewCity=findViewById(R.id.textViewCity);
        textViewCounty=findViewById(R.id.textViewCounty);
        textViewStreet=findViewById(R.id.textViewStreet);
        rvAddress=findViewById(R.id.rvAddress);
    }

}
