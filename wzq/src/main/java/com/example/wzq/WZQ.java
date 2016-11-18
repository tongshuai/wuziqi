package com.example.wzq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class WZQ extends AppCompatActivity implements View.OnClickListener{
    private WzqView wzqView;
    private Button btn_restart,btn_huiqi;
    private AttributeSet attrs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wzq);
        wzqView=new WzqView(this,attrs);
        btn_restart= (Button) findViewById(R.id.btn_restart);

        wzqView= (WzqView) findViewById(R.id.id_wuziqi);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_restart:
                wzqView.reStart();
                invalidateOptionsMenu();
                break;
        }
    }
}
