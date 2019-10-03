package com.linxz.addresspickerdemo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.linxz.addresspicker.AddressPickerSimpleActivity
import com.linxz.addresspicker.model.AddressListBean

class MainActivity : AppCompatActivity() {

    private var tvAddress: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvAddress=findViewById(R.id.tvAddress)
        findViewById<Button>(R.id.btnAddress).setOnClickListener {
            AddressPickerSimpleActivity.launch(this@MainActivity)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==AddressPickerSimpleActivity.REQUEST_CODE && resultCode== Activity.RESULT_OK){
            val province=data?.getSerializableExtra("province") as AddressListBean?
            val city= data?.getSerializableExtra("city") as AddressListBean?
            val country= data?.getSerializableExtra("country") as AddressListBean?
            val street= data?.getSerializableExtra("street") as AddressListBean?
            Toast.makeText(this@MainActivity,province?.name+city?.name+country?.name+street?.name,Toast.LENGTH_SHORT).show()
            tvAddress?.text = province?.name+city?.name+country?.name+street?.name
        }
    }
}
