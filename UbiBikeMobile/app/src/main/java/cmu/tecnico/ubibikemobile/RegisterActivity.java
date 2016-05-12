package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cmu.tecnico.ubibikemobile.asyncTasks.RegisterTask;
import cmu.tecnico.ubibikemobile.asyncTasks.UserInfoTask;
import cmu.tecnico.ubibikemobile.models.User;

public class RegisterActivity extends AppCompatActivity {
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private final Handler registerHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == App.MESSAGE_CONFIRM) {
                ((App) RegisterActivity.this.getApplication()).setUsername(username);

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.arg1 != 200) {
                            Toast.makeText(getBaseContext(), "Failed to fetch user info", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent;
                            ((App) RegisterActivity.this.getApplication()).setUser((User) msg.obj);

                            intent = new Intent(RegisterActivity.this, MainActivity.class);
                            RegisterActivity.this.startActivity(intent);
                        }
                    }
                };
                new UserInfoTask((App) getApplication(), handler, getResources()).execute(username);
            } else {
                ((EditText) RegisterActivity.this.findViewById(R.id.txt_username)).setError("User already exists");
            }
        }
    };

    public void btn_Register_onClick(View v) {
        username = ((EditText) RegisterActivity.this.findViewById(R.id.txt_username)).getText().toString();
        String password = ((EditText) RegisterActivity.this.findViewById(R.id.txt_password)).getText().toString();
        String displayName = ((EditText) RegisterActivity.this.findViewById(R.id.txt_displayName)).getText().toString();

        new RegisterTask((App) getApplication(), registerHandle, getResources()).execute(username, password, displayName);
    }
}
