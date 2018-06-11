package pers.wengzc.snailhunter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pers.wengzc.hunterpackage.Examine;

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
                sleepAWhile();
            }
        });

        btnNotUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){

                    @Override
                    public void run() {
                        sleepAWhile();
                    }
                }.start();
            }
        });

    }

    @Examine
    private void sleepAWhile (){
        try{
            Thread.sleep(500);
        }catch (Exception e){

        }
    }
}
