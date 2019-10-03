package com.linxz.addresspicker.biz;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linxz.addresspicker.R;
import com.linxz.addresspicker.model.AddressListBean;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Linxz
 * 创建日期：2019年09月29日 15:45
 * version：v4.5.4
 * 描述：
 */
public class AddressDataManager {

    /**
     * 获取地址文件数据
     * */
    public static String getRawFile(Context context) {
        final InputStream is = context.getResources().openRawResource(R.raw.address);
        final BufferedInputStream bis = new BufferedInputStream(is);
        final InputStreamReader isr = new InputStreamReader(bis);
        final BufferedReader br = new BufferedReader(isr);
        final StringBuilder stringBuilder = new StringBuilder();
        String str;
        try {
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取省份数据
     * */
    public static List<AddressListBean> getProvinceData(Context context){
        String addressJson=getRawFile(context);
        List<AddressListBean> provinceList=new Gson().fromJson(addressJson,new TypeToken<List<AddressListBean>>(){}.getType());
        return provinceList;
    }

    /**获取市级*/
    public static List<AddressListBean> getCityData(List<AddressListBean> provinceList,String provinceCode){
        if (provinceList==null || provinceList.isEmpty()){
            throw new IllegalArgumentException("省份列表不能为空");
        }
        List<AddressListBean> cityList=new ArrayList<>();
        for (AddressListBean bean:provinceList){
            if ((provinceCode+"").equals(bean.getCode())){
                cityList.addAll(bean.getChildren());
            }
        }
        return cityList;
    }

    /**
     * 获取县级数据
     * */
    public static List<AddressListBean> getCountryData(List<AddressListBean> cityList,String cityCode){
        if (cityList==null || cityList.isEmpty()){
            throw new IllegalArgumentException("城市列表不能为空");
        }
        List<AddressListBean> countyList=new ArrayList<>();
        for (AddressListBean bean:cityList){
            if ((cityCode+"").equals(bean.getCode())){
                cityList.addAll(bean.getChildren());
            }
        }
        return countyList;
    }

    /**
     * 获取镇级数据
     * */
    public static List<AddressListBean> getStreet(List<AddressListBean> countyList,String countryCode){
        if (countyList==null || countyList.isEmpty()){
            throw new IllegalArgumentException("城市列表不能为空");
        }
        List<AddressListBean> streetList=new ArrayList<>();
        for (AddressListBean bean:countyList){
            if ((countryCode+"").equals(bean.getCode())){
                countyList.addAll(bean.getChildren());
            }
        }
        return streetList;
    }



}
