package com.linxz.addresspicker.model;

import java.io.Serializable;

/**
 * @author Linxz
 * 创建日期：2019年09月29日 10:10
 * version：v4.5.4
 * 描述：
 */
public class BaseAddress implements Serializable {
    private int id;
    private String code;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
