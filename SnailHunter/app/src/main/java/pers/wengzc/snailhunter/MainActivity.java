package pers.wengzc.snailhunter;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btnUi;
    private Button btnNotUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUi =  findViewById(R.id.btn_ui);
        btnNotUi =  findViewById(R.id.btn_not_ui);


        btnUi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ExtendViewMethodSignature testObj = new ExtendViewMethodSignature();
                testObj.fun1();
            }
        });

        btnNotUi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        ExtendViewMethodSignature testObj = new ExtendViewMethodSignature();
                        testObj.fun1();
                    }
                }.start();

            }
        });
    }


    void printThreadInfo (){

        String threadName = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();
        Log.d("xxx", "threadName: "+threadName+" threadid="+id);
    }


}
