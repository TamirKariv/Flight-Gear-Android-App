package com.example.ex4;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    //onCreate event
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button connectBtn = (Button)findViewById(R.id.button_connect);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the ip and the port from the user and save for the next activity
                EditText ip = (EditText) findViewById(R.id.EDIT_TEXT_IP);
                EditText port = (EditText) findViewById(R.id.EDIT_TEXT_PORT);
                Intent intent = new Intent(getApplicationContext(), JoyStickActivity.class);
                intent.putExtra("IP", ip.getText().toString());
                intent.putExtra("Port", port.getText().toString());
                startActivity(intent);
            }
        });
    }
}
