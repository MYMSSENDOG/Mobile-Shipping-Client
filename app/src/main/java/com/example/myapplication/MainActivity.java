package com.example.myapplication;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.BufferedReader;  //우와 많다 ㅎㅎ..
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.File;
import java.net.SocketAddress;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

 

public class MainActivity extends AppCompatActivity {
    String ip = "210.121.205.238";
    int port = 9999;
    Button btn;
    EditText edit_cstm_buyer, edit_start_name, edit_start_dong, edit_start_pnumber, edit_dest_name, edit_dest_dong, edit_dest_pnumber, edit_fee, edit_detail, edit_ip;
    RadioGroup radio_payment, radio_car, radio_deliver, radio_day;
    CheckBox check_test;
    EditText[] Edits;
    RadioGroup[] Radios;
    Socket sock;
    JSONObject jcontainer;

    private BufferedWriter networkWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alloc_id();



        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (inspect_button()) {
                    Thread mt = new SockThread();
                    mt.start();
                }
            }
        });
        new Thread() {
            @Override
            public void run() {
                try {
                    sock = new Socket(ip, port);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
    public void alloc_id(){
        btn = (Button) findViewById(R.id.btn_send);
        edit_cstm_buyer = (EditText) findViewById(R.id.edit_cstm_buyer);
        edit_start_name = (EditText) findViewById(R.id.edit_start_name);
        edit_start_dong = (EditText) findViewById(R.id.edit_start_dong);
        edit_start_pnumber = (EditText) findViewById(R.id.edit_start_pnumber);

        edit_dest_name = (EditText) findViewById(R.id.edit_dest_name);
        edit_dest_dong = (EditText) findViewById(R.id.edit_dest_dong);
        edit_dest_pnumber = (EditText) findViewById(R.id.edit_dest_pnumber);

        edit_fee = (EditText) findViewById(R.id.edit_fee);
        edit_detail = (EditText) findViewById(R.id.edit_detail);

        edit_ip = (EditText) findViewById(R.id.edit_ip);

        radio_payment = (RadioGroup) findViewById(R.id.radioGroup_payment);
        radio_car = (RadioGroup) findViewById(R.id.radioGroup_car);
        radio_deliver = (RadioGroup) findViewById(R.id.radioGroup_deliver);
        radio_day = (RadioGroup) findViewById(R.id.radioGroup_day);

        check_test = (CheckBox)findViewById(R.id.check_test);

         Edits = new EditText[]{edit_cstm_buyer, edit_start_name, edit_start_dong, edit_start_pnumber, edit_dest_name, edit_dest_dong, edit_dest_pnumber, edit_fee, edit_detail};
        Radios = new RadioGroup[]{radio_payment, radio_car, radio_deliver, radio_day};
        //RadioGroup radio_fee, radio_car, radio_ship, radio_day;

    }
    class SockThread extends Thread {
        public void run() {
            try {
                networkWriter = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                Log.d("masg", "send data");
                PrintWriter out = new PrintWriter(networkWriter, true);
                out.printf(MakeJson());
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Toast f = Toast.makeText(MainActivity.this.getApplicationContext(), "보내기 성공", Toast.LENGTH_LONG);
                        f.show();

                    }
                },0);

            } catch (IOException e) {
                
                e.printStackTrace();
            }
        }
    }

    private String MakeJson() {
        String jmessage = "";
        try {
            int num_of_edit = 9;
            int num_of_button = 4;
            String[] EditControlName = {"cstm_buyer", "start_name", "start_dong", "start_pnumber", "dest_name", "dest_dong", "dest_pnumber", "fee", "detail"};
            String[] ButtonControlName = {"payment", "car", "deliver", "day"};
            String[] temp = {"다산", "명지", "금천구 시흥동", "010-3207-8164", "다산", "", "", "50000", "박스 백만 스물 두 개"};

            jcontainer = new JSONObject();
            if (check_test.isChecked()){
                jcontainer.put("test", "ok");
            }
            for (int i = 0; i<num_of_edit;i++) {
                /*
                if(!temp[i].equals("")) {
                    jcontainer.put(EditControlName[i], temp[i]);
                }
                 */
                if(!Edits[i].getText().toString().equals("")) {
                    jcontainer.put(EditControlName[i], Edits[i].getText());
                }
            }
            for(int i=0; i<num_of_button; i++){

                RadioButton id = (RadioButton)findViewById(Radios[i].getCheckedRadioButtonId());
                if (id!=null) {
                    Radios[i].indexOfChild(id);
                    jcontainer.put(ButtonControlName[i], Integer.toString(Radios[i].indexOfChild(id) - 1));
                }
            }
            jmessage = jcontainer.toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return jmessage;
    }
    boolean inspect_button(){

        return true;
    }
}