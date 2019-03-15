package com.example.weatherstation;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String url_Address = "http://iot.uysys.net/app/ws.php";
    String[] hum_ms;
    String[] temp_ms;
    String[] air_ms;
    String[] rain_ms;

    ListView listView;

    BufferedInputStream is;
    String line= null;
    String result = null;
    int i;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_item);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        collectData();

        CustomListView customListView = new CustomListView(this,hum_ms,temp_ms,air_ms,rain_ms);
        listView.setAdapter(customListView);

        auth = FirebaseAuth.getInstance();
        userNameView = (TextView)findViewById(R.id.userNameView);

        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(auth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                userNameView.setText("Welcome " + userProfile.getNameUser());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void collectData(){
        try
        {
            URL url = new URL(url_Address);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            is = new BufferedInputStream(connection.getInputStream());

        }catch (Exception ex){
            ex.printStackTrace();
        }


        try{

            BufferedReader  br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line=br.readLine())!= null){

                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();


        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        try{

            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            hum_ms = new String[ja.length()];
            temp_ms = new String[ja.length()];
            air_ms = new String[ja.length()];
            rain_ms = new String[ja.length()];

            for (i=0;i<=ja.length();i++){
                jo = ja.getJSONObject(i);
                hum_ms[i] = jo.getString("humidity");
                temp_ms[i] = jo.getString("temperature");
                air_ms[i] = jo.getString("wind_speed");
                rain_ms[i] = jo.getString("precipitation");
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){


            case(R.id.settingID):
            {
                auth.signOut();
                Intent intent = new  Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }

            case (R.id.report):
                {
                Intent intent = new  Intent(MainActivity.this,Report.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
