package com.example.ex4;


import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Client extends AsyncTask<String, String, Boolean> {
    private BlockingQueue<String> commandsQueue = new LinkedBlockingDeque<>();

    //add a command to the command queue
    public void AddCommandToQueue(String command) {
        try {
            this.commandsQueue.put(command);
        }catch (Exception e) {
            System.out.println("Error adding command");
        }
    }

   //connect to the simulator and send Async commands from the commands queue, connection is done in the background.
    public Boolean doInBackground(String... params) {
        try {
            //establish connection with the simulator
            Socket socket = new Socket(params[0], Integer.parseInt(params[1]));
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            //send the commands from the queue
            while (true) {
                dataOutputStream.write((commandsQueue.take()+"\r\n").getBytes());
                dataOutputStream.flush();
            }
        }catch (Exception e) {
            System.out.println(e.toString());
         }
        return true;
    }



}