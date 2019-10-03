package com.linxz.jdaddress.model;

/**
 * @author Linxz
 * 创建日期：2019年09月28日 13:50
 * version：v4.5.4
 * 描述：
 */
public class PickerAddress {
    private Province province;
    private City city;
    private Country country;
    private Street street;

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }
}
