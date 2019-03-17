package cc.brainbook.armslaunch.launch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity_main2);

        Button btnClose=(Button)findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                GuideActivity.this.setResult(RESULT_OK);
                GuideActivity.this.finish();
            }
        });
    }
}
