package pers.wengzc.snailhuntertest;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

import pers.wengzc.hunterkit.HunterTarget;

public class MainActivity extends AppCompatActivity {

    private Button btnUi;
    private Button btnNotUi;

    private Handler handler;

    @HunterTarget
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUi =  findViewById(R.id.btn_ui);
        btnNotUi =  findViewById(R.id.btn_not_ui);
        handler = new Handler();

        btnUi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("xxx", "onClick: ");
            }
        });

        btnNotUi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
