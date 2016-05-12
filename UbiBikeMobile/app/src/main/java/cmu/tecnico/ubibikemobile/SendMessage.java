package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cmu.tecnico.ubibikemobile.helpers.ConcreteWifiHandler;

public class SendMessage extends AppCompatActivity {

    public TextView history;
    EditText newMsg;
    Button button;
    ConcreteWifiHandler wifiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent prevIntent = getIntent();
        final String cyclistName = prevIntent.getStringExtra(CyclistsList.CYCLER_NAME);
        Toast toast=Toast.makeText(getApplicationContext(), cyclistName, Toast.LENGTH_SHORT);
        toast.show();

        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        final App app = (App) getApplicationContext();
        app.getWifiHandler().currActivity = this;
        this.wifiHandler = app.getWifiHandler();

        final ScrollView chatScroll = (ScrollView) findViewById(R.id.chat_scroll);

        history = (TextView) findViewById(R.id.chat_history);
        newMsg = (EditText) findViewById(R.id.new_sms);
        button = (Button) findViewById(R.id.SubmitMsg);
        wifiHandler.readFile(cyclistName);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newContent = history.getText().toString() + '\n' + "Me: " + newMsg.getText().toString();
                history.setText(newContent);
                wifiHandler.sendMessage(cyclistName, newContent);
                wifiHandler.saveFile(cyclistName, newContent);
                newMsg.setText("");
                chatScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        setSupportActionBar(toolbar);
    }

}
