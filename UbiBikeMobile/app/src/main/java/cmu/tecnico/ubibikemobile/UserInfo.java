package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class UserInfo extends AppCompatActivity {

    int points;
    String username;
    TextView textViewPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent prevIntent = getIntent();
        username = prevIntent.getStringExtra("username");

        textViewPoints = (TextView) findViewById(R.id.userPoints);
        GetPoints();
    }

    private void GetPoints() {
        // Buscar info ao servidor para o user logged in

        points = 100;
        textViewPoints.setText("Points: " + points);
    }
}
