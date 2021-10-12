package com.example.android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView Temp,Hum,Date,Time;
    Button btn;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Temp = (TextView) findViewById(R.id.textViewTemp);
        Hum = (TextView) findViewById(R.id.textViewHum);
        Date = (TextView) findViewById(R.id.textViewDate);
        Time = (TextView) findViewById(R.id.textViewTime);
        btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reff = FirebaseDatabase.getInstance().getReference().child("petri");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String date = snapshot.child("date").getValue().toString();
                        String temp = snapshot.child("oneWire").getValue().toString();
                        String hum = snapshot.child("humidity").getValue().toString();
                        String time = snapshot.child("time").getValue().toString();
                        Temp.setText(temp);
                        Date.setText(date);
                        Hum.setText(hum);
                        Time.setText(time);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}