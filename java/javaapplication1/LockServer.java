/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.io.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;
import java.io.IOException;
import static javaapplication1.LockServer.onlineCount;

/**
 *
 * 
 */
public class LockServer extends Thread {

    public static String[] arr = null;
    //====================================================================================
    public static int onlineCount = 0;
    private boolean welcome = true;
    Socket s;

    LockServer(Socket s) {
        this.s = s;
    }

    public void run() {
        cont main = new cont();
        main.initialize();
        onlineCount++;
        try {
            System.out.println(s.getRemoteSocketAddress() + "　已連線...");
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
            while (true) {
                if (welcome == true) {
                    out.println("您好．．．我是Lock伺服器，這是伺服器的訊息");
                    welcome = false;
                }
                String str = in.readLine();
                if (str != null) {
                        main.writeData(Integer.parseInt(str));
                        Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            System.err.println(s.getRemoteSocketAddress() + " 已離線...");
        }
        onlineCount--;
    }

    public static void main(String[] args) throws Exception {
        //================================================================================
        // TODO code application logic here
        int aServerPort = 5050;
        String aServer_IP = "140.120.14.117";
        new countPeople();
        //connect to Arduino server
        ServerSocket ss = new ServerSocket(5050);
        System.out.println("伺服器已啟動...");
        while (true) {
            Socket cliSocket = ss.accept();
            Thread t = new Thread(new LockServer(cliSocket));
            t.start();
        }
    }
}
class countPeople {

    countPeople() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("目前線上人數: " + onlineCount);
                        Thread.sleep(3000);
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
    }
}