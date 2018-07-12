package pers.wengzc.snailhunter;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;

import com.example.testlib.LibActivity;
import com.example.testlib.LibSuperClass;

import java.util.Random;

import pers.wengzc.hunterKit.ExamineMethodRunTime;


public class MainActivity extends AppCompatActivity {

    private Button btnUi;
    private Button btnNotUi;


    public static class MainSubClass{

        String str = "MainSubClass";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUi =  findViewById(R.id.btn_ui);
        btnNotUi =  findViewById(R.id.btn_not_ui);

        sleepAWhile();

        btnUi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try{
                    gotoLibActivity();
                }catch (Exception e){}
            }
        });

        btnNotUi.setOnClickListener(new View.OnClickListener() {

            @ExamineMethodRunTime
            @Override
            public void onClick(View view) {
                new Thread(){

                    @ExamineMethodRunTime
                    @Override
                    public void run() {
                        try{
                            MainSubClass mainSubClass = new MainSubClass();
                            System.out.println(""+mainSubClass.str);

                            sleepAWhile();
                        }catch (Exception e){}

                    }
                }.start();
            }
        });
    }

    private void gotoLibActivity (){
        Intent intent = new Intent(this, LibActivity.class);
        startActivity(intent);
    }

    @ExamineMethodRunTime
    private void exceptionUncatchedTest () {
        System.out.println(" ------ exceptionUncatchedTest before thow ------ ");
        int a = 4/0;
        System.out.println(" ------ exceptionUncatchedTest after thow ------ ");
    }

    @ExamineMethodRunTime
    private void exceptionUncatchedIntentTest () throws Exception{
        System.out.println(" ------ exceptionUncatchedIntentTest before thow ------ ");
        throw new RuntimeException("--myRuntimeException--");
        //System.out.println(" ------ exceptionUncatchedIntentTest after thow ------ ");
    }


    @ExamineMethodRunTime
    private void exceptionCatchedTest (){
        try{
            System.out.println(" ------ exceptionCatchedTest before sleep ------ ");
            Thread.sleep(300);
            System.out.println(" ------ exceptionCatchedTest after sleep ------ ");
        }catch (InterruptedException e){
            System.out.println("catch (InterruptedException e)="+e);
        }catch (Exception e){
            System.out.println("catch (Exception e)="+e);
        }finally {

        }
    }

    @ExamineMethodRunTime
    private String multiReturnTest (){
        int r = new Random().nextInt(10);
        if (r % 2 == 0){
            return "ooxx";
        }else{
            return "wengzc";
        }
    }

    private void numberRetTest (){
        intRetTest();
        longRetTest();
        floatRetTest();
        doubleRetTest();
    }

    @ExamineMethodRunTime
    private int intRetTest (){
        return 1;
    }

    @ExamineMethodRunTime
    private long longRetTest (){
        return 1L;
    }

    @ExamineMethodRunTime
    private float floatRetTest (){
        return 1.0f;
    }

    @ExamineMethodRunTime
    private double doubleRetTest (){
        return 1.0d;
    }

    private void sleepAWhile (){
        try{
            Thread.sleep(400);
        }catch (Exception e){

        }
    }
}
