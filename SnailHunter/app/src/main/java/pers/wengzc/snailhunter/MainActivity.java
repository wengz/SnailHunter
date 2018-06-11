package pers.wengzc.snailhunter;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;

import java.util.Random;

import pers.wengzc.hunterpackage.Examine;

public class MainActivity extends AppCompatActivity {

    private Button btnUi;
    private Button btnNotUi;

    @Examine
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUi =  findViewById(R.id.btn_ui);
        btnNotUi =  findViewById(R.id.btn_not_ui);

        btnUi.setOnClickListener(new View.OnClickListener() {

            @Examine
            @Override
            public void onClick(View view) {
                try{
                    exceptionUncatchedIntentTest();
                }catch (Exception e){}
            }
        });

        btnNotUi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Thread(){

                    @Override
                    public void run() {
                        try{
                            exceptionUncatchedIntentTest();
                        }catch (Exception e){}

                    }
                }.start();
            }
        });
    }

    @Examine
    private void exceptionUncatchedTest () {
        System.out.println(" ------ exceptionUncatchedTest before thow ------ ");
        int a = 4/0;
        System.out.println(" ------ exceptionUncatchedTest after thow ------ ");
    }

    @Examine
    private void exceptionUncatchedIntentTest () throws Exception{
        System.out.println(" ------ exceptionUncatchedIntentTest before thow ------ ");
        throw new RuntimeException("--myRuntimeException--");
        //System.out.println(" ------ exceptionUncatchedIntentTest after thow ------ ");
    }


    @Examine
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

    @Examine
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

    @Examine
    private int intRetTest (){
        return 1;
    }

    @Examine
    private long longRetTest (){
        return 1L;
    }

    @Examine
    private float floatRetTest (){
        return 1.0f;
    }

    @Examine
    private double doubleRetTest (){
        return 1.0d;
    }

    private void sleepAWhile (){
        try{
            Thread.sleep(100);
        }catch (Exception e){

        }
    }
}
