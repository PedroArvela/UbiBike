package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import cmu.tecnico.R;
import cmu.tecnico.wifiDirect.WifiHandler;

public class MainActivity extends AppCompatActivity {

    Intent myIntent;
    Button button;
    Button button2;
    Button btnUserInfo;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MyApplication app = (MyApplication) getApplicationContext();
        app.wifiHandler = new WifiHandler(getApplicationContext());
        app.wifiHandler.currActivity = this;
        app.wifiHandler.wifiOn();

        //myIntent.putExtra("key", value); //Optional parameters
        //CurrentActivity.this.startActivity(myIntent);
        username = "Zé das Couves"; // TODO mudar isto para ir buscar o user autenticado

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(MainActivity.this, StationsList.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(MainActivity.this, CyclistsList.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        btnUserInfo = (Button) findViewById(R.id.btn_UserInfo);
        btnUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(MainActivity.this, UserInfo.class);
                myIntent.putExtra("username", username);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
