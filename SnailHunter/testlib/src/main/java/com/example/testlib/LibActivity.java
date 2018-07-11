package com.example.testlib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import pers.wengzc.hunterKit.ExamineMethodRunTime;

public class LibActivity extends AppCompatActivity{

    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lib);
        btn = findViewById(R.id.test_lib);
        sleepAWhile();

        btn.setOnClickListener(new View.OnClickListener() {

            @ExamineMethodRunTime
            @Override
            public void onClick(View v) {
                sleepAWhile();
            }
        });

    }


    private void sleepAWhile (){
        try{
            Thread.sleep(400);
        }catch (Exception e){
        }
    }
}
