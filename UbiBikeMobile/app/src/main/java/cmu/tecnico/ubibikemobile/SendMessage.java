package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cmu.tecnico.R;
import cmu.tecnico.wifiDirect.CyclistsList;

public class SendMessage extends AppCompatActivity {

    TextView history;
    EditText newMsg;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //final MyApplication app = (MyApplication) getApplicationContext();
        //app.wifiHandler.currActivity = this;

        Intent prevIntent = getIntent();
        final String cyclistName = prevIntent.getStringExtra(CyclistsList.CYCLER_NAME);
        Toast toast=Toast.makeText(getApplicationContext(), cyclistName, Toast.LENGTH_SHORT);
        toast.show();

        history = (TextView) findViewById(R.id.chat_history);
        newMsg = (EditText) findViewById(R.id.new_sms);
        button = (Button) findViewById(R.id.SubmitMsg);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                if(history.getText().toString().contains("Chat History"))
                    history.setText("");
                String newContent = history.getText().toString() + '\n' + "ME: "+ newMsg.getText().toString();
                history.setText(newContent);
                //app.wifiHandler.sendMessage(cyclistName ,newContent);
                newMsg.setText("");
            }
        });

        setSupportActionBar(toolbar);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
