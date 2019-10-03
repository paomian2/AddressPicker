package com.linxz.addresspicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.linxz.addresspicker.interfaces.PickerResultCallBack;
import com.linxz.addresspicker.model.AddressListBean;
import com.linxz.addresspicker.view.AddressPickerView;

/**
 * @author Linxz
 * 创建日期：2019年09月29日 09:27
 * version：v4.5.4
 * 描述：
 */
public class AddressPickerSimpleActivity extends AppCompatActivity {

    public static int REQUEST_CODE=1008;
    private AddressPickerView pickerView;

    public static void launch(Activity activity){
        Intent intent=new Intent(activity,AddressPickerSimpleActivity.class);
        activity.startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_act_address_picker);
        pickerView=findViewById(R.id.pickerView);
        pickerView.setPickerResultCallBack(new PickerResultCallBack() {
            @Override
            public void onResult(AddressListBean province, AddressListBean city, AddressListBean country, AddressListBean street) {
                Intent intent=new Intent();
                intent.putExtra("province",province);
                intent.putExtra("city",city);
                intent.putExtra("country",country);
                intent.putExtra("street",street);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
