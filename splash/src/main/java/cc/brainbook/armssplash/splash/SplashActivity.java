package cc.brainbook.armssplash.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //得到新打开Activity关闭后返回的数据
        //第二个参数为请求码，可以根据业务需求自己编号
        startActivityForResult(new Intent(SplashActivity.this, Main2Activity.class), 1);
    }
    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     *
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
        Log.d("TAG1", "onActivityResult: result: " + result);

    }

}
