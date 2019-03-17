package cc.brainbook.armslaunch.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import androidx.annotation.Nullable;

import cc.brainbook.armslaunch.launch.util.SharedPreferencesUtil;

/**
 * 启动页面LaunchActivity
 *
 * 启动页面在延时期间执行若干个初始化过程（比如获取网络数据、版本更新数据、轮播图片下载等）
 * 还可以根据本地存储的标志位来开启若干个引导页面
 *
 *
 * 特点：
 * 1）启动页面延时
 * 启动页面必须设置一定延时，否则启动图片一闪而过直接进入引导页面或MainActivity页面
 * 启动页面延时显示期间，可以进行一些初始化过程，如读取耗时的网络数据、版本更新数据、轮播图片下载等
 *
 * 2) 可有零个或多个初始化过程（需要配置）
 * 注意：初始化过程调用代码完成后必须调用initComplete()
 * 每完成一个初始化过程，就减一，直到为负数再执行退出启动页面和进入MainActivity页面
 *
 * 3）可有零个或多个引导页面（需要配置）
 * 每个引导页面对应一个唯一的请求码，用LinkedList实现多个引导页面逻辑（processRequestCodes()）
 * 每个引导页面返回RESULT_OK表示不再显示，缺省返回RESULT_CANCEL表示下次打开启动页面仍需显示
 *
 * 4）禁用手机的返回键
 * 注意：onBackPressed和onKeyDown方法有时候没有效果，这个方法能保证禁用手机的返回键
 *
 *
 * 配置：
 * [配置多个初始化过程]
 * 1）设置初始化过程的数量，如果没有任何初始化过程，则为0
 * 2）在init()中添加初始化过程调用代码
 * 3）初始化过程调用代码，注意：完成后必须调用initComplete()
 *
 * [配置多个引导页面]
 * 1）设置引导页面的请求码（可以没有）
 * 2）为LinkedList添加各个引导页面的请求码（可以没有，也可以innerProcessRequestCodes()中没有处理的无效的请求码）
 * 3）根据请求码进入各个引导页面
 * 4）处理各个引导页面的返回结果
 */
///注意：启动页面LaunchActivity要用Activity而不是AppCompatActivity，后者会自动加载ActionBar，而LaunchActivity无需ActionBar
///https://blog.csdn.net/today_work/article/details/79300181
public class LaunchActivity extends Activity {
    private static final String TAG = "TAG";

    /**
     * 启动页面延时（毫秒）
     */
    private static final long DELAY_MILLIS = 3000;

    /**
     * （需要配置代码！）[配置多个引导页面#1]设置各个引导页面的请求码（可以没有）
     */
    private static final int REQUEST_CODE_GUIDE = 1;
//    private static final int REQUEST_CODE_GUIDE2 = 2;   ///other cases ...

    /**
     * （需要配置代码！）[配置多个引导页面#2]为LinkedList添加各个引导页面的请求码（可以没有，也可以innerProcessRequestCodes()中没有处理的无效的请求码）
     * https://blog.csdn.net/zhangketuan/article/details/45535989
     */
    private LinkedList<Integer> mRequestCodes = new LinkedList<Integer>() {{
//        add(999); ///testcase#innerProcessRequestCodes()中没有处理的无效的请求码
        add(REQUEST_CODE_GUIDE);
//        add(999); ///testcase#innerProcessRequestCodes()中没有处理的无效的请求码
//        add(REQUEST_CODE_GUIDE2); ///other cases ...
    }};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "LaunchActivity# onCreate(): ");
        super.onCreate(savedInstanceState);


        /* ------------- ///[Android 5.0以下实现隐藏标题栏和全屏显示] ------------- */
        ///注意：Android 5.0以上则需要重新定义theme主题！
//        ///[动态去掉ActionBar（即标题栏title）]如android:style/Theme.Black.NoTitleBar.Fullscreen则不必设置了
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        ///[动态全屏显示]如android:style/Theme.Black.NoTitleBar.Fullscreen则不必设置了
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ///[启动页面延时关闭]
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                processRequestCodes(false);
            }
        }, DELAY_MILLIS);

        ///[初始化过程]
        init();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "LaunchActivity# onStart(): ");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "LaunchActivity# onRestart(): ");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "LaunchActivity# onResume(): ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "LaunchActivity# onPause(): ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "LaunchActivity# onStop(): ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "LaunchActivity# onDestroy(): ");
        super.onDestroy();
    }

    /**
     * 禁用手机的返回键
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

    /**
     * 处理各个引导页面的请求码，过滤掉innerProcessRequestCodes()中没有处理的无效的请求码
     *
     * @param isRemove
     */
    private void processRequestCodes(boolean isRemove) {
        ///过滤掉innerProcessRequestCodes()中没有处理的无效的请求码
        while (isRemove = innerProcessRequestCodes(isRemove)) {}
    }

    /**
     * （需要配置代码！）内部处理各个引导页面的请求码
     *
     * @param isRemove
     * @return
     */
    private boolean innerProcessRequestCodes(boolean isRemove) {
        int requestCode;
        try {
            if (isRemove) {
                mRequestCodes.remove();
            }
            requestCode = mRequestCodes.getFirst();
        } catch (NoSuchElementException e) {
            initComplete();
            return false;
        }

        ///[配置多个引导页面#3]根据请求码进入各个引导页面
        switch (requestCode) {
            case REQUEST_CODE_GUIDE:
                if (SharedPreferencesUtil.getBoolean(this, "isShowGuide", true)) {  ///是否显示引导页面
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
        ///无效的请求码
        return true;
    }

    /**
     * （需要配置代码！）获取引导页面返回的结果数据
     *
     * @param requestCode   请求码，即调用startActivityForResult()传递过去的值
     * @param resultCode    结果码，结果码用于标识返回数据来自哪个新Activity
     * @param data          结果数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "LaunchActivity# onActivityResult(): requestCode: " + requestCode + ", resultCode: " + resultCode);

        ///[配置多个引导页面#4]处理各个引导页面的返回结果
        switch (requestCode) {
            case REQUEST_CODE_GUIDE:
                if (resultCode == RESULT_OK) {
                    SharedPreferencesUtil.putBoolean(this, "isShowGuide", false);
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
     * （需要配置代码！）[配置多个初始化过程#1]设置初始化过程的数量，如果没有任何初始化过程，则为0
     */
    private volatile int initCount = 0;

    /**
     * （需要配置代码！）[配置多个初始化过程#2]在init()中添加初始化过程调用代码
     */
    private void init() {
//        initLoadUpgradeInfo();
//
//        initLoadGuideInfo();
    }

    /**
     * （需要配置代码！）[配置多个初始化过程#3]初始化过程调用代码，注意：完成后必须调用initComplete()
     */
    private void initLoadUpgradeInfo() {
        // todo ...
        ///注意：完成后必须调用initComplete()
    }
    private void initLoadGuideInfo() {
        // todo ...
        ///注意：完成后必须调用initComplete()
    }

    /**
     * 初始化过程完成
     *
     * 每完成一个初始化过程就减一，直到为负数表示全部初始化过程都完成了，此时再执行退出LaunchActivity、进入MainActivity
     */
    private void initComplete() {
        ///判断初始化过程是否全部结束
        if (--initCount < 0) {
            startActivity(new Intent(this, TestMainActivity.class));
            finish();
        }
    }

}
