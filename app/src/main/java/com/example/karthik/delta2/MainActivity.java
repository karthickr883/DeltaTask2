package com.example.karthik.delta2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {
    EditText team1, team2, venue, date, time;
    ListView list;
    public ArrayList<Data> fixtures = new ArrayList<Data>();
    Calendar mcalander;
    int day, month, yr, currenthour, currentminute;
    String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        team1 = (EditText) findViewById(R.id.team1);
        team2 = (EditText) findViewById(R.id.team2);
        venue = (EditText) findViewById(R.id.venue);
        date = (EditText) findViewById(R.id.date);
        mcalander = Calendar.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = mcalander.get(Calendar.DAY_OF_MONTH);
                month = mcalander.get(Calendar.MONTH);
                yr = mcalander.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int months, int dayOfMonth) {
                        months = months + 1;
                        date.setText(dayOfMonth + "/" + months + "/" + year);

                    }
                }, yr, month, day);
                datePickerDialog.show();
            }
        });
        time = (EditText) findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcalander = Calendar.getInstance();
                currenthour = mcalander.get(Calendar.HOUR_OF_DAY);
                currentminute = mcalander.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12)
                            amPm = "PM";
                        else
                            amPm = "AM";
                        time.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);
                    }
                }, currenthour, currentminute, false);
                timePickerDialog.show();
            }
        });

        Button add = (Button) findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixtures.add(new Data(team1.getText().toString(), team2.getText().toString(), venue.getText().toString(), date.getText().toString(), time.getText().toString()));
                list = (ListView) findViewById(R.id.fixture);
                final custom adapter = new custom(MainActivity.this, R.layout.singlerow, fixtures);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("What do u want to do?");
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Data fixture = fixtures.get(position);
                                team1.setText(fixture.getTeam1());
                                team2.setText(fixture.getTeam2());
                                venue.setText(fixture.getVenue());
                                date.setText(fixture.getDate());
                                time.setText(fixture.getTime());

                                Button update = (Button) findViewById(R.id.button2);
                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        fixtures.set(position, new Data(team1.getText().toString(), team2.getText().toString(), venue.getText().toString(), date.getText().toString(), time.getText().toString()));

                                        adapter.notifyDataSetChanged();
                                        team1.setText("");
                                        team2.setText("");
                                        venue.setText("");
                                        date.setText("");
                                        time.setText("");
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fixtures.remove(position);
                                custom adapter = new custom(MainActivity.this, R.layout.singlerow, fixtures);
                                list.setAdapter(adapter);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Alert Dialog box");
                        dialog.show();
                    }

                });
                team1.setText("");
                team2.setText("");
                venue.setText("");
                date.setText("");
                time.setText("");
            }
        });
    }

    class custom extends ArrayAdapter<Data> {
        public custom(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Data> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            Data fixture = getItem(position);
            if (row == null) {
                LayoutInflater l = getLayoutInflater();
                row = l.inflate(R.layout.singlerow, parent, false);
            }
            TextView t1, t2, v, d, ti;
            t1 = (TextView) row.findViewById(R.id.listteam1);
            t2 = (TextView) row.findViewById(R.id.listteam2);
            v = (TextView) row.findViewById(R.id.listvenue);
            d = (TextView) row.findViewById(R.id.listdate);
            ti = (TextView) row.findViewById(R.id.listtime);
            t1.setText(fixture.getTeam1());
            t2.setText(fixture.getTeam2());
            v.setText(fixture.getVenue());
            d.setText(fixture.getDate());
            ti.setText(fixture.getTime());

            return row;
        }
    }
}
