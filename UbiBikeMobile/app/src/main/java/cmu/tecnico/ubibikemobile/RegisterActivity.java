package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Console;
import java.net.MalformedURLException;
import java.net.URL;

import cmu.tecnico.ubibikemobile.asyncTasks.LoginTask;
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

        button = (Button) findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    new LoginTask().execute(
                            new Pair<RegisterActivity, URL>(RegisterActivity.this,
                                    new URL("http", "web.tecnico.ulisboa.pt", "/~jose.pedro.arvela/ubibike/user/" + txtUsername.getText().toString())));
                } catch (MalformedURLException e) {
                    // ignore
                }
            }
        });
    }

    public void login(String username) {
        ((App) this.getApplication()).setUsername(username);
        Log.v("Info", "Username received: " + username);

        myIntent = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(myIntent);
    }
}
