package com.example.android_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView Temp,Hum,Date,Time;
    DatabaseReference ref;
    DatabaseReference refPast;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Graafin määrittely
        GraphView graph = (GraphView) findViewById(R.id.graph);

        // Datalistat
        List<Integer> humidityList = new ArrayList<>();
        List<Float> temperatureList = new ArrayList<>();

        Temp = findViewById(R.id.textViewTemp);
        Hum = findViewById(R.id.textViewHum);
        Date = findViewById(R.id.textViewDate);
        Time = findViewById(R.id.textViewTime);
        btnUpdate = findViewById(R.id.button);

        ref = FirebaseDatabase.getInstance().getReference().child("mikael/current");
        ref.addValueEventListener(new ValueEventListener() {
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

        refPast = FirebaseDatabase.getInstance().getReference().child("mikael/past");
        refPast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Käydään jokainen mikael/past polun alla oleva child-node läpi for-loopissa,
                // ja työnnetään halutut arvot niille määriteltyihin listoihin

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Integer humidity = postSnapshot.child("humidity").getValue(Integer.class);
                    Float temperature = postSnapshot.child("oneWire").getValue(Float.class);
                    humidityList.add(humidity);
                    temperatureList.add(temperature);

                    //konsoliin tulostus debugaamista varten
                    //Log.d("TAG", "Values: " + time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        btnUpdate.setOnClickListener(v -> {
            // Buttonia klikatessa viedään listassa oleva data graafiin for-loopin sisällä
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            for (int x = 0; x < 10; x++) {
                series.appendData(new DataPoint(x, temperatureList.get(x)),true,10);
            }
            graph.addSeries(series);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(8);
            series.setThickness(5);
        });
    }
}