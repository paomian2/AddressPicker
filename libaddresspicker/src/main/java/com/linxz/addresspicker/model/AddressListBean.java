package com.linxz.addresspicker.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Linxz
 * 创建日期：2019年09月29日 11:13
 * version：v4.5.4
 * 描述：
 */
public class AddressListBean extends BaseAddress implements Serializable {

    private boolean select;
    private List<AddressListBean> children;

    public AddressListBean(){}

    public AddressListBean(String code,String name){
        this.setCode(code);
        this.setName(name);
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<AddressListBean> getChildren() {
        return children;
    }

    public void setChildren(List<AddressListBean> children) {
        this.children = children;
    }
}
