package com.linxz.addresspicker.interfaces;

import com.linxz.addresspicker.model.AddressListBean;

/**
 * @author Linxz
 * 创建日期：2019年09月30日 15:58
 * version：v4.5.4
 * 描述：
 */
public interface PickerResultCallBack {
    void onResult(AddressListBean province,AddressListBean city,AddressListBean country,AddressListBean street);
}
