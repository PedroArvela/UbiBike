package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cmu.tecnico.ubibikemobile.services.NetworkConnectionService;

public class RegisterActivity extends AppCompatActivity {

    Intent myIntent;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final EditText txtUsername = (EditText) findViewById(R.id.txt_username);
        setSupportActionBar(toolbar);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                login(msg.getData().getString("username"));
            }
        };
        final Messenger messenger = new Messenger(handler);

        button = (Button) findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent registerIntent = new Intent(RegisterActivity.this, NetworkConnectionService.class);
                registerIntent.putExtra(NetworkConnectionService.PARAM_USERNAME, txtUsername.getText());
                registerIntent.putExtra("messenger", messenger);

                startService(registerIntent);
            }
        });
    }

    public void login(String username) {
        ((App) this.getApplication()).setUsername(username);

        myIntent = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(myIntent);
    }
}
