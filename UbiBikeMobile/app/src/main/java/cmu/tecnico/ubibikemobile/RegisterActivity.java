package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cmu.tecnico.ubibikemobile.asyncTasks.LoginTask;

public class RegisterActivity extends AppCompatActivity {

    Intent myIntent;
    Button button;
    EditText txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final EditText txtUsername = (EditText) findViewById(R.id.txt_username);
        final EditText txtPassword = (EditText) findViewById(R.id.txt_password);
        setSupportActionBar(toolbar);

        button = (Button) findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new LoginTask(RegisterActivity.this,
                        txtUsername.getText().toString(),
                        txtPassword.getText().toString()).execute(true);
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
