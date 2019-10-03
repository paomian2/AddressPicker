package com.linxz.addresspicker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linxz
 * 创建日期：2019年09月29日 13:47
 * version：v4.5.4
 * 描述：
 */
public class InnerFragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager fm;
    private List<Fragment> fragments = new ArrayList<>();

    public InnerFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public long getItemId(int position) {
        return (long) fragments.get(position).hashCode();
    }

    @Override
    public int getItemPosition(Object object) {
        if (!((Fragment) object).isAdded() || !fragments.contains(object)) {
            return PagerAdapter.POSITION_NONE;
        }
        return fragments.indexOf(object);
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((Fragment) obj).getView();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment instantiateItem = ((Fragment) super.instantiateItem(container, position));
        Fragment item = fragments.get(position);
        if (instantiateItem == item) {
            return instantiateItem;
        } else {
            //如果集合中对应下标的fragment和fragmentManager中的对应下标的fragment对象不一致，那么就是新添加的，所以自己add进入；这里为什么不直接调用super方法呢，因为fragment的mIndex搞的鬼，以后有机会再补一补。
            fm.beginTransaction().add(container.getId(), item).commitNowAllowingStateLoss();
            return item;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        //如果getItemPosition中的值为PagerAdapter.POSITION_NONE，就执行该方法。
        if (fragments.contains(fragment)) {
            super.destroyItem(container, position, fragment);
            return;
        }
        //自己执行移除。因为mFragments在删除的时候就把某个fragment对象移除了，所以一般都得自己移除在fragmentManager中的该对象。
        fm.beginTransaction().remove(fragment).commitNowAllowingStateLoss();
    }


    public void addTab(Fragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    public void clear() {
        fragments.clear();
        notifyDataSetChanged();
    }

    public void remove(int index) {
        fragments.remove(index);
        notifyDataSetChanged();
    }

    /**
     * 删除省份以下的数据
     */
    public void clearDataByProvince() {
        while (fragments.size() > 1) {
            fragments.remove(1);
        }
        this.notifyDataSetChanged();
    }

    /**
     * 删除市级以下的数据
     */
    public void clearDataByCity() {
        while (fragments.size() > 2) {
            fragments.remove(2);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除县级以下的数据
     * */
    public void clearDataByCountry(){
        while (fragments.size()>3){
            fragments.remove(3);
        }
        notifyDataSetChanged();
    }


}
