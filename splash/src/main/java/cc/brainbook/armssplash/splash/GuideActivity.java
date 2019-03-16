package cc.brainbook.armssplash.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_main2);

        Button btnClose=(Button)findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("isShowGuide", false);
                //设置返回数据
                GuideActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                GuideActivity.this.finish();
            }
        });
    }
}
