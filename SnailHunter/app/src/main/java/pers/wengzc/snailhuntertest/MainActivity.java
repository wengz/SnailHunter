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

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        longTimeWork();
                    }
                }, 5000);

            }
        });

        btnNotUi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        longTimeWork2();
                    }
                }, 5000);

            }
        });
    }

    private void wrapperMethod1 (){
        longTimeWork();
    }

    private void wrapperMethod2 (){
        longTimeWork();
        Log.d("xxx", "wrapperMethod2: ~~~");
        longTimeWork2();
    }

    private void longTimeWork (){
        try{
            Thread.sleep(300);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void longTimeWork2 (){
        try{
            Thread.sleep(350);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void runInWorkThread (){
        Log.d("xxx", "runInWorkThread: ");
    }

    private void useLang3 (){
        String result = StringUtils.repeat("hello,ww", 1000);
        int length = StringUtils.length(result);
        Log.d("xxxx", "useLang3: "+result);

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
