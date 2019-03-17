package cc.brainbook.armssplash.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import androidx.annotation.Nullable;

/**
 * 用LinkedList实现多个引导页面逻辑
 */
///SplashActivity要用Activity而不是AppCompatActivity，后者会自动加载ActionBar，而SplashActivity无需ActionBar
///https://blog.csdn.net/today_work/article/details/79300181
public class SplashActivity extends Activity {
    private static final String TAG = "TAG";

    private static final int REQUEST_CODE_GUIDE = 1;
//    private static final int REQUEST_CODE_GUIDE2 = 2;   ///other cases ...

    private SharedPreferences mSharedPrefs;

    ///为LinkedList添加初始化数据
    ///https://blog.csdn.net/zhangketuan/article/details/45535989
    private LinkedList<Integer> mRequestCodes = new LinkedList<Integer>() {{
//        add(999); ///test
        add(REQUEST_CODE_GUIDE);
//        add(999); ///test
//        add(REQUEST_CODE_GUIDE2); ///other cases ...
    }};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "SplashActivity# onCreate(): ");
        super.onCreate(savedInstanceState);


        /* ------------- ///[ArmsSplash#Android 5.0以下实现隐藏标题栏和全屏显示] ------------- */
        ///注意：Android 5.0以上则需要重新定义theme主题！
//        ///[ArmsSplash#动态去掉ActionBar（即标题栏title）]如android:style/Theme.Black.NoTitleBar.Fullscreen则不必设置了
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        ///[ArmsSplash#动态全屏显示]如android:style/Theme.Black.NoTitleBar.Fullscreen则不必设置了
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        processRequestCodes(false);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "SplashActivity# onStart(): ");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "SplashActivity# onRestart(): ");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "SplashActivity# onResume(): ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "SplashActivity# onPause(): ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "SplashActivity# onStop(): ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "SplashActivity# onDestroy(): ");
        super.onDestroy();
    }

    /**
     * 得到新打开Activity关闭后返回的结果数据
     *
     * @param requestCode   请求码，即调用startActivityForResult()传递过去的值
     * @param resultCode    结果码，结果码用于标识返回数据来自哪个新Activity
     * @param data          结果数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "SplashActivity# onActivityResult(): requestCode: " + requestCode + ", resultCode: " + resultCode);

        switch (requestCode) {
            case REQUEST_CODE_GUIDE:
                if (resultCode == RESULT_OK) {
                    saveFlag("isShowGuide", false);
                }
                processRequestCodes(true);
                return;

            /// other cases ...
//            case REQUEST_CODE_GUIDE2:
//                if (resultCode == RESULT_OK) {
//                    saveFlag("isShowGuide2", false);
//                }
//                processRequestCodes(true);
//                return;

        }
    }

    /**
     * ///[ArmsSplash#禁用手机的返回键]
     *
     * 注意：onBackPressed和onKeyDown方法有时候没有效果，这个方法能保证禁用手机的返回键
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }
    }

    private void processRequestCodes(boolean isRemove) {
        while (isRemove = innerProcessRequestCodes(isRemove)) {}
    }

    private boolean innerProcessRequestCodes(boolean isRemove) {
        int requestCode;
        try {
            if (isRemove) {
                mRequestCodes.remove();
            }
            requestCode = mRequestCodes.getFirst();
        } catch (NoSuchElementException e) {
            done();
            return false;
        }

        switch (requestCode) {
            case REQUEST_CODE_GUIDE:
                if (loadFlag("isShowGuide", true)) {  ///是否显示引导页面
                    startActivityForResult(new Intent(this, GuideActivity.class), REQUEST_CODE_GUIDE);
                    return false;
                }
                break;

                ///other cases ...
//            case REQUEST_CODE_GUIDE2:
//                if (loadFlag("isShowGuide2", true)) {  ///是否显示引导页面2
//                    startActivityForResult(new Intent(this, Guide2Activity.class), REQUEST_CODE_GUIDE2);
//                    return false;
//                }
//                break;

        }

        return true;
    }

    private boolean loadFlag(String key, boolean defValue) {
        return mSharedPrefs.getBoolean(key, defValue);
    }

    private void saveFlag(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void done() {
        startActivity(new Intent(this, TestMainActivity.class));
        finish();
    }

}
