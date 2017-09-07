package com.example.lizhiwei.threadappclock;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final String tag = "myTag";
    public int time=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void button(View view){
        final Button buttonStartType = (Button)findViewById(R.id.buttonStart);
        Button buttonRecordType = (Button)findViewById(R.id.buttonRecord);
        final TextView minute = (TextView)findViewById(R.id.minute);
        final TextView second = (TextView)findViewById(R.id.second);
        TextView textViewRecord = (TextView)findViewById(R.id.textViewRecord);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                String saveSecond = "";
                String saveMinute = "";
                if(message.arg1<10){
                    saveSecond = "0" + message.arg1;
                }
                else
                    saveSecond = message.arg1+"";
                if(message.arg2<10){
                    saveMinute = "0" + message.arg2;
                }
                second.setText(saveSecond);
                minute.setText(saveMinute);
            }
        };

        Runnable myWorker = new Runnable() {
            @Override
            public void run() {
                while(buttonStartType.getText().toString().equals("停止")){
                    int intSecond = Integer.parseInt(second.getText().toString());
                    int intMinute = Integer.parseInt(minute.getText().toString());
                    Message message1 = new Message();
                    if(intSecond==59){
                        message1.arg1 = 0;
                        message1.arg2 = intMinute + 1;
                    }
                    else{
                        message1.arg1 = intSecond + 1;
                        message1.arg2 = intMinute;
                    }
                    handler.sendMessage(message1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(tag,"**************"+intSecond+"intSecond" + intMinute + "intMinute");
                }
            }
        };

        Log.d(tag,"执行到switch上面");
        switch(view.getId()){
            case R.id.buttonStart:
                Thread workerThread = new Thread(null,myWorker,"myWorker");
                if(buttonStartType.getText().toString().equals("开始")){
                    Log.d(tag,"开始计时");
                    buttonStartType.setText("停止");
                    buttonRecordType.setText("计时");
                    workerThread.start();
                }
                else if(buttonStartType.getText().toString().equals("停止")){
                    Log.d(tag,"停止计时");
                    buttonStartType.setText("开始");
                    buttonRecordType.setText("复位");
                }
                else
                    Log.d(tag,"开始按钮出现错误响应");
                break;
            case R.id.buttonRecord:
                if(buttonRecordType.getText().toString().equals("计时")){
                    String recordTime = minute.getText().toString()+":"+second.getText().toString();
                    String outputTime = "计次" + time + "     " + recordTime;
                    textViewRecord.append(outputTime);
                    time++;
                }
                else if(buttonRecordType.getText().toString().equals("复位")){
                    textViewRecord.setText("");
                    minute.setText("00");
                    second.setText("00");
                    time = 1;
                }
                else
                    Log.d(tag,"复位按钮出现错误响应");
                break;
        }
    }
}
