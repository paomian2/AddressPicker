package com.linxz.addresspicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.linxz.addresspicker.R;
import com.linxz.addresspicker.adapter.InnerFragmentAdapter;
import com.linxz.addresspicker.biz.AddressDataManager;
import com.linxz.addresspicker.interfaces.AddressListClickListener;
import com.linxz.addresspicker.interfaces.OnCityClickListener;
import com.linxz.addresspicker.interfaces.OnCountryClickListener;
import com.linxz.addresspicker.interfaces.OnProvinceClickListener;
import com.linxz.addresspicker.interfaces.OnStreetClickListener;
import com.linxz.addresspicker.interfaces.PickerResultCallBack;
import com.linxz.addresspicker.model.AddressListBean;
import com.linxz.addresspicker.utils.ViewHelper;

import java.util.List;

/**
 * @author Linxz
 * 创建日期：2019年09月29日 09:09
 * version：v4.5.4
 * 描述：
 */
public class AddressPickerView extends FrameLayout {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private InnerFragmentAdapter adapter;
    private FragmentManager mFragmentManager;
    private AddressListBean provinceBean;
    private AddressListBean cityBean;
    private AddressListBean countryBean;
    private AddressListBean streetBean;
    private OnProvinceClickListener mOnProvinceClickListener;
    private OnCityClickListener mOnCityClickListener;
    private OnCountryClickListener mOnCountryClickListener;
    private OnStreetClickListener mOnStreetClickListener;
    private PickerResultCallBack mPickerResultCallBack;

    private int resColor;
    private PICK_DATA_MODE mPickDataMode = PICK_DATA_MODE.DEFAULT;
    /**
     * 选择器数据的涞源：默认或外部传入
     * 系统默认：数据从raw中读取
     * 外部传入，四级数据分别加载进来
     */
    public enum PICK_DATA_MODE {
        DEFAULT,
        OUTSIDE
    }


    public AddressPickerView(@NonNull Context context) {
        super(context);
    }

    public AddressPickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddressPickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.AddressPickerView);
        int mode=array.getInt(R.styleable.AddressPickerView_dataMode,0);
        resColor=array.getColor(R.styleable.AddressPickerView_tabIndicatorColor,0);
        if (mode==1){
            mPickDataMode=PICK_DATA_MODE.OUTSIDE;
        }
        array.recycle();
        initView();
        setListener();
        initData();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.lib_address_picker_view, this);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        if (mFragmentManager == null) {
            mFragmentManager = ViewHelper.getFragmentManager(getContext());
        }
        adapter = new InnerFragmentAdapter(mFragmentManager);
        viewPager.setAdapter(adapter);
        ViewHelper.changeTextColor(tabLayout,resColor);
    }

    private void setListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                ViewHelper.updateTabTextView(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ViewHelper.updateTabTextView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (mPickDataMode == PICK_DATA_MODE.DEFAULT) {
            mOnProvinceClickListener = new OnProvinceClickListener() {
                @Override
                public void onClick(AddressListBean address) {
                    setCityData(address.getChildren());
                }
            };
            mOnCityClickListener = new OnCityClickListener() {
                @Override
                public void onClick(AddressListBean address) {
                    setCountryData(address.getChildren());
                }
            };
            mOnCountryClickListener = new OnCountryClickListener() {
                @Override
                public void onClick(AddressListBean address) {
                    setStreetData(address.getChildren());
                }
            };
            mOnStreetClickListener = new OnStreetClickListener() {
                @Override
                public void onClick(AddressListBean address) {
                }
            };
        }
    }

    private void initData() {
        if (mPickDataMode == PICK_DATA_MODE.DEFAULT) {
            setProvinceData(AddressDataManager.getProvinceData(getContext()));
        }
    }


    public void setProvinceData(List<AddressListBean> provinceData) {
        tabLayout.addTab(tabLayout.newTab().setText("请选择"));
        AddressListFragment provinceFrag = new AddressListFragment();
        provinceFrag.setAddressData(provinceData);
        provinceFrag.setAddressListClickListener(new AddressListClickListener() {
            @Override
            public void onClick(AddressListBean address) {
                provinceBean = address;
                cityBean = null;
                countryBean = null;
                streetBean = null;
                //清除原有省份对应的市级、县级、镇级(街道)数据
                adapter.clearDataByProvince();
                tabLayout.getTabAt(0).setText(address.getName());
                while (tabLayout.getTabCount() > 1) {
                    tabLayout.removeTabAt(1);
                }
                if (mOnProvinceClickListener != null) {
                    mOnProvinceClickListener.onClick(address);
                }
            }
        });
        adapter.addTab(provinceFrag);
    }


    public void setCityData(List<AddressListBean> cityData) {
        if (provinceBean == null) {
            throw new NullPointerException("请先设置省份数据");
        }
        tabLayout.addTab(tabLayout.newTab().setText("请选择"));
        AddressListFragment cityFrag = new AddressListFragment();
        cityFrag.setAddressData(cityData);
        cityFrag.setAddressListClickListener(new AddressListClickListener() {
            @Override
            public void onClick(AddressListBean address) {
                cityBean = address;
                countryBean = null;
                streetBean = null;
                adapter.clearDataByCity();
                tabLayout.getTabAt(1).setText(address.getName());
                while (tabLayout.getTabCount() > 2) {
                    tabLayout.removeTabAt(2);
                }
                if (mOnCityClickListener != null) {
                    mOnCityClickListener.onClick(address);
                }
            }
        });
        adapter.addTab(cityFrag);
        viewPager.setCurrentItem(1, false);
    }


    public void setCountryData(List<AddressListBean> countryData) {
        if (cityBean == null) {
            throw new NullPointerException("请先设置城市数据");
        }
        tabLayout.addTab(tabLayout.newTab().setText("请选择"));
        AddressListFragment countryFrag = new AddressListFragment();
        countryFrag.setAddressData(countryData);
        countryFrag.setAddressListClickListener(new AddressListClickListener() {
            @Override
            public void onClick(AddressListBean address) {
                countryBean = address;
                streetBean = null;
                adapter.clearDataByCountry();
                tabLayout.getTabAt(2).setText(address.getName());
                while (tabLayout.getTabCount() > 3) {
                    tabLayout.removeTabAt(3);
                }
                if (mOnCountryClickListener != null) {
                    mOnCountryClickListener.onClick(address);
                }
            }
        });
        adapter.addTab(countryFrag);
        viewPager.setCurrentItem(2);
    }


    public void setStreetData(List<AddressListBean> streetData) {
        if (countryBean == null) {
            throw new NullPointerException("请先设置县级数据");
        }
        if (streetData == null || streetData.size() == 0) {
            //只有三级数据
            return;
        }
        tabLayout.addTab(tabLayout.newTab().setText("请选择"));
        AddressListFragment streetFrag = new AddressListFragment();
        streetFrag.setAddressData(streetData);
        streetFrag.setAddressListClickListener(new AddressListClickListener() {
            @Override
            public void onClick(AddressListBean address) {
                streetBean = address;
                if (mOnStreetClickListener != null) {
                    mOnStreetClickListener.onClick(address);
                }
                if (mPickerResultCallBack!=null){
                    mPickerResultCallBack.onResult(provinceBean,cityBean,countryBean,streetBean);
                }
            }
        });
        adapter.addTab(streetFrag);
        viewPager.setCurrentItem(3, false);
    }

    public void setOnProvinceClickListener(OnProvinceClickListener onProvinceClickListener){
        this.mOnProvinceClickListener=onProvinceClickListener;
    }

    public void setOnCityClickListener(OnCityClickListener onCityClickListener){
        this.mOnCityClickListener=onCityClickListener;
    }

    public void setOnCountryClickListener(OnCountryClickListener onCountryClickListener){
        this.mOnCountryClickListener=onCountryClickListener;
    }

    public void setOnStreetClickListener(OnStreetClickListener onStreetClickListener){
        this.mOnStreetClickListener=onStreetClickListener;
    }

    public void setPickerResultCallBack(PickerResultCallBack pickerResultCallBack){
        this.mPickerResultCallBack=pickerResultCallBack;
    }

    public void setTabIndicatorColor(int resColor){
        this.resColor=resColor;
        ViewHelper.changeTextColor(tabLayout,resColor);
    }
}
