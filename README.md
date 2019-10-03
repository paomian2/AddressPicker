#### 仿京东地址选择器

##### 用法
Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```javascript
allprojects {
   repositories{
       maven { url 'https://jitpack.io' }
    }
}

```
Step 2. Add the dependency

```
dependencies {
    implementation 'com.github.paomian2:AddressPicker:v1.0.0'
}
```

Step 3.Use in code
```
AddressPickerSimpleActivity.launch(this@MainActivity)
```
eg:
```
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

```

效果图

![image](https://raw.githubusercontent.com/paomian2/AddressPicker/master/screen/地址选择器.gif)