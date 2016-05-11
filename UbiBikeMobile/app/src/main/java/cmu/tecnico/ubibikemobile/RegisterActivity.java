package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cmu.tecnico.ubibikemobile.asyncTasks.LoginTask;
import cmu.tecnico.ubibikemobile.asyncTasks.UserInfoTask;
import cmu.tecnico.ubibikemobile.models.User;

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

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 != 200) {
                    Toast.makeText(getBaseContext(), "Failed to fetch user info", Toast.LENGTH_SHORT);
                } else {
                    getUserData((User) msg.obj);
                }
            }
        };
        new UserInfoTask((App) getApplication(), handler, getResources()).execute(username);
    }

    public void getUserData(User user) {
        ((App) this.getApplication()).setUser(user);

        myIntent = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(myIntent);
    }
}
