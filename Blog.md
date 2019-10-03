@[toc]

##### 一、思路

  看页面效果可以知道，上层Tab为选择的地址，下层为省、市、区、街道等数据，那么这种效果可以使用TabLayout+Fragment实现。
  一级数据一个Tab+Fragment,Tab为选择的或对应的数据含义(即：Tab(省)、Tab(市)、Tab(区)、Tab(街道)）， Fragment为对应的数据列表。
  初始化的时候自动生成一个Tab+Fragment,往后选择一个地址如(广西壮族自治区)生成一个新的Tab+Fragment,如果重新选择地址，先清空对应的下级Tab+Fragment再按原来的逻辑生成新的Tab+Fragment。
  例如：开始的时候选择广西壮族自治区->贵港市->平南县，这时候应该分别生成省Tab+Fragment、市Tab+Fragment、区/县Tab+Fragment这三个，重新选择省(北京)的时候，市级、区/县级的Tab+Fragment都
  移除，然后再重新生成省(北京)对应的市级Tab+Fragment，然后使用ViewPager切换到生成的Fragment。

  生成Tab+Fragment关键代码：
```
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

```

##### 二、TabLayout使用
      
   TabLayout新增Tab:
   ```
   tabLayout.addTab(tabLayout.newTab().setText("请选择"));
   ```
  
   由于TabLayout没有动态修改Tab字体、style、指示器颜色的API，需要使用反射实现。
   ```
       public static void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {
        if (isSelect) {
            try {
                java.lang.reflect.Field fieldView = tab.getClass().getDeclaredField("mView");
                fieldView.setAccessible(true);
                View view = (View) fieldView.get(tab);
                java.lang.reflect.Field fieldTxt = view.getClass().getDeclaredField("mTextView");
                fieldTxt.setAccessible(true);
                TextView tabSelect = (TextView) fieldTxt.get(view);
                tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tabSelect.setText(tab.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                java.lang.reflect.Field fieldView = tab.getClass().getDeclaredField("mView");
                fieldView.setAccessible(true);
                View view = (View) fieldView.get(tab);
                java.lang.reflect.Field fieldTxt = view.getClass().getDeclaredField("mTextView");
                fieldTxt.setAccessible(true);
                TextView tabSelect = (TextView) fieldTxt.get(view);
                tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tabSelect.setText(tab.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   ```
   
   ```
       public static void changeTextColor(TabLayout tabLayout, int resColor) {
        if (resColor == 0) {
            return;
        }
        try {
            //拿到tabLayout的mTabStrip属性
            Field field = TabLayout.class.getDeclaredField("mTabStrip");
            field.setAccessible(true);
            //拿mTabStrip属性里面的值
            Object mTabStrip = field.get(tabLayout);
            //通过mTabStrip对象来获取class类，不能用field来获取class类，参数不能写Integer.class
            Method method = mTabStrip.getClass().getDeclaredMethod("setSelectedIndicatorColor", int.class);
            method.setAccessible(true);
            method.invoke(mTabStrip, resColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   ```

##### 三、ViewPager

   ViewPager来进行Fragment的切换，由于ViewPager自带缓存，所以使用常规的FragmentPagerAdapter来进行Fragment切换时会存在一个问题，Fragment被替换之后展示的还是原来的Fragment数据。
   比如原本选择的是省(广西壮族自治区)、市(贵港市)，然后点击Tab切换到省对应的Fragment下，选择省(北京)、市(市辖区)，这时候会发现省(北京)对应的数据还是广西对应的市级数据。需要重写getItemPosition、
   instantiateItem、destroyItem来解决：
   ```
    @Override
    public int getItemPosition(Object object) {
        if (!((Fragment) object).isAdded() || !fragments.contains(object)) {
            return PagerAdapter.POSITION_NONE;
        }
        return fragments.indexOf(object);
    }
   ```
   
   
   ```
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
   ```
   
   
   ```
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
   ```

##### 四、自定义View

   简单定义AddressPickerView，实现用户直接使用该View即可实现地址选择，支持使用默认的地址数据、外部传入数据两种方式，可以根据需求修改指示器颜色。
   添加View对应的属性：
   
 ```
 <declare-styleable name="AddressPickerView">
        <attr name="dataMode" format="enum">
            <enum name="def" value="0"/>
            <enum name="outside" value="1"/>
        </attr>
        <attr name="tabIndicatorColor" format="color"/>
  </declare-styleable>
  ```
  
  关键代码：
 ```
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

 ```
  
  
  

##### 五、三级、四级、五级地址数据

  中华人民共和国行政区划：省级（省份直辖市自治区）、 地级（城市）、 县级（区县）、 乡级（乡镇街道）、 村级（村委会居委会） ，中国省市区镇村二级三级四级五级联动地址数据 Node.js 爬虫。
  [数据地址](https://github.com/paomian2/Administrative-divisions-of-China)


##### 六、效果

![京东效果](https://raw.githubusercontent.com/paomian2/AddressPicker/master/screen/JD地址选择器.gif)
![本案例效果](https://raw.githubusercontent.com/paomian2/AddressPicker/master/screen/地址选择器.gif)


本案例代码下载地址：[Demo](https://github.com/paomian2/AddressPicker)