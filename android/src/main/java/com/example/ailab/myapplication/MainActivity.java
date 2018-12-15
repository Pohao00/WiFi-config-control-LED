package com.example.ailab.myapplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Thread thread;                //執行緒
    private Socket clientSocket;        //客戶端的socket
    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;                    //做為接收時的緩存
    private JSONObject json_write,json_read;        //從java伺服器傳遞與接收資料的json
    private Button button, button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thread=new Thread(Connection);                //賦予執行緒工作
        thread.start();                    //讓執行緒開始執行
    }
    //連結socket伺服器做傳送與接收
    private Runnable Connection=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try{
                // IP為Server端
                InetAddress serverIp = InetAddress.getByName("140.120.14.117");
                int serverPort = 5050;
                clientSocket = new Socket(serverIp, serverPort);
                //取得網路輸出串流
                bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));
                // 取得網路輸入串流
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // 當連線後
                button = (Button)findViewById(R.id.button);
                while (clientSocket.isConnected()) {
                    button.setOnClickListener(new Button.OnClickListener(){
                        //PrintStream writer = new PrintStream(clientSocket.getOutputStream());
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            try {
                                bw.write('1' +"\n");
                                bw.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    button2 = (Button)findViewById(R.id.button2);
                    button2.setOnClickListener(new Button.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            try {
                                bw.write('0' +"\n");
                                bw.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    // 取得網路訊息

                    tmp = br.readLine();    //宣告一個緩衝,從br串流讀取值
                    // 如果不是空訊息
                    if(tmp!=null){
                        //將取到的String抓取{}範圍資料
                        tmp=tmp.substring(tmp.indexOf("{"), tmp.lastIndexOf("}") + 1);
                        json_read=new JSONObject(tmp);
                        //從java伺服器取得值後做拆解,可使用switch做不同動作的處理
                    }
                }
            }catch(Exception e){
                //當斷線時會跳到catch,可以在這裡寫上斷開連線後的處理
                e.printStackTrace();
                Log.e("text","Socket連線="+e.toString());
                //finish();    //當斷線時自動關閉房間
            }
        }
    };
    @Override
    protected void onDestroy() {            //當銷毀該app時
        super.onDestroy();
        try {
            json_write=new JSONObject();
            json_write.put("action","離線");    //傳送離線動作給伺服器
            Log.i("text","onDestroy()="+json_write+"\n");
            //寫入後送出
            bw.write(json_write+"\n");
            bw.flush();
            //關閉輸出入串流後,關閉Socket
            //會跳出java.net.SocketException:Socket is closed錯誤,讓catch內的處理再重複執行,如有同樣問題的可以將下面這3行註解掉
            bw.close();
            br.close();
            clientSocket.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("text","onDestroy()="+e.toString());
        }
    }
}