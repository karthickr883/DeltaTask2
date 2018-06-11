package com.example.karthik.delta2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    EditText team1, team2, venue, date, time;
    int imgn1 = 1, imgn2 = 0, cameraImg1 = 3, cameraImg2 = 4;
    ListView list;
    public ArrayList<Data> fixtures = new ArrayList<Data>();
    Calendar mcalander;
    int day, month, yr, currenthour, currentminute;
    String amPm;
    Bitmap bitmapimg1, bitmapimg2;
    Button img1, img2;
    DataBaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.fixture);
        team1 = (EditText) findViewById(R.id.team1);
        team2 = (EditText) findViewById(R.id.team2);
        venue = (EditText) findViewById(R.id.venue);
        date = (EditText) findViewById(R.id.date);
        img1 = (Button) findViewById(R.id.img1);
        database = new DataBaseHelper(MainActivity.this);

        Cursor data = database.getdata();
        while (data.moveToNext()) {
            byte[] byteArray1 = data.getBlob(6);
            Bitmap bitimge1 = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
            byte[] byteArray2 = data.getBlob(7);
            Bitmap bitimge2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
            fixtures.add(new Data(data.getInt(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), bitimge1, bitimge2));
        }

        final custom adapter1 = new custom(this, R.layout.alternate, fixtures);
        list.setAdapter(adapter1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("What do u want to do?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LayoutInflater l = getLayoutInflater();
                        View row = l.inflate(R.layout.singlerow, list, false);

                        ImageView img1, img2;
                        img1 = (ImageView) row.findViewById(R.id.imageView);
                        img2 = (ImageView) row.findViewById(R.id.imageView2);
                        final Data fixture = fixtures.get(position);
                        team1.setText(fixture.getTeam1());
                        team2.setText(fixture.getTeam2());
                        venue.setText(fixture.getVenue());
                        date.setText(fixture.getDate());
                        time.setText(fixture.getTime());
                        bitmapimg1 = fixture.getImg1();
                        bitmapimg2 = fixture.getImg2();
                        img1.setImageBitmap(fixture.getImg1());
                        img2.setImageBitmap(fixture.getImg2());

                        Button update = (Button) findViewById(R.id.button2);
                        update.setOnClickListener(new View.OnClickListener() {

                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(View v) {

                                Log.d("Checking id", String.valueOf(fixture.getId()));
                                database.updateData(fixture.getId(), team1.getText().toString(), team2.getText().toString(), venue.getText().toString(), date.getText().toString(), time.getText().toString(), bitmapimg1, bitmapimg2);
                                Cursor data = database.getUpdateData(fixture.getId());

                                while (data.moveToNext()) {
                                    byte[] byteArray1 = data.getBlob(6);
                                    Bitmap bitimge1 = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
                                    byte[] byteArray2 = data.getBlob(7);
                                    Bitmap bitimge2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
                                    fixtures.set(position, new Data(data.getInt(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), bitimge1, bitimge2));
                                }
                                adapter1.notifyDataSetChanged();
                                team1.setText("");
                                team2.setText("");
                                venue.setText("");
                                date.setText("");
                                time.setText("");
                                bitmapimg2 = null;
                                bitmapimg1 = null;
                            }
                        });

                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Data db = fixtures.get(position);
                        database.deleteData(db.getId());
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


        img2 = (Button) findViewById(R.id.img2);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("Choose any one");
                alertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent photopickerintent = new Intent(Intent.ACTION_PICK);
                        File picturedirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        String picturedirectorypath = picturedirectory.getPath();
                        Uri data = Uri.parse(picturedirectorypath);
                        photopickerintent.setDataAndType(data, "image/*");
                        startActivityForResult(photopickerintent, imgn1);
                    }
                });
                alertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, cameraImg1);
                    }
                });
                alertDialog.show();
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("Choose any one");
                alertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent photopickerintent = new Intent(Intent.ACTION_PICK);
                        File picturedirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        String picturedirectorypath = picturedirectory.getPath();
                        Uri data = Uri.parse(picturedirectorypath);
                        photopickerintent.setDataAndType(data, "image/*");
                        startActivityForResult(photopickerintent, imgn2);
                    }
                });

                alertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, cameraImg2);
                    }
                });

                alertDialog.show();
            }
        });

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
                database.addData(team1.getText().toString(), team2.getText().toString(), venue.getText().toString(), date.getText().toString(), time.getText().toString(), bitmapimg1, bitmapimg2);
                Cursor data;
                data = database.getdata();

                fixtures.clear();
                while (data.moveToNext()) {
                    byte[] byteArray1 = data.getBlob(6);
                    Bitmap bitimge1 = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
                    byte[] byteArray2 = data.getBlob(7);
                    Bitmap bitimge2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
                    fixtures.add(new Data(data.getInt(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), bitimge1, bitimge2));
                }


                final custom adapter = new custom(MainActivity.this, R.layout.singlerow, fixtures);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("What do u want to do?");
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LayoutInflater l = getLayoutInflater();
                                View row = l.inflate(R.layout.singlerow, list, false);

                                ImageView img1, img2;
                                img1 = (ImageView) row.findViewById(R.id.imageView);
                                img2 = (ImageView) row.findViewById(R.id.imageView2);
                                final Data fixture = fixtures.get(position);
                                team1.setText(fixture.getTeam1());
                                team2.setText(fixture.getTeam2());
                                venue.setText(fixture.getVenue());
                                date.setText(fixture.getDate());
                                time.setText(fixture.getTime());
                                bitmapimg1 = fixture.getImg1();
                                bitmapimg2 = fixture.getImg2();
                                img1.setImageBitmap(fixture.getImg1());
                                img2.setImageBitmap(fixture.getImg2());

                                Button update = (Button) findViewById(R.id.button2);
                                update.setOnClickListener(new View.OnClickListener() {

                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    public void onClick(View v) {

                                        database.updateData(fixture.getId(), team1.getText().toString(), team2.getText().toString(), venue.getText().toString(), date.getText().toString(), time.getText().toString(), bitmapimg1, bitmapimg2);
                                        Cursor data = database.getUpdateData(fixture.getId());

                                        while (data.moveToNext()) {
                                            byte[] byteArray1 = data.getBlob(6);
                                            Bitmap bitimge1 = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
                                            byte[] byteArray2 = data.getBlob(7);
                                            Bitmap bitimge2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
                                            fixtures.set(position, new Data(data.getInt(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), bitimge1, bitimge2));
                                        }
                                        adapter.notifyDataSetChanged();
                                        team1.setText("");
                                        team2.setText("");
                                        venue.setText("");
                                        date.setText("");
                                        time.setText("");
                                        bitmapimg2 = null;
                                        bitmapimg1 = null;
                                    }
                                });

                            }
                        });
                        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Data db = fixtures.get(position);
                                database.deleteData(db.getId());
                                fixtures.remove(position);
                                custom adapter = new custom(MainActivity.this, R.layout.alternate, fixtures);
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
                bitmapimg1 = null;
                bitmapimg2 = null;
            }
        });
    }

    class custom extends ArrayAdapter<Data> {
        public custom(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Data> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            final Data fixture = getItem(position);
            if (row == null) {
                LayoutInflater l = getLayoutInflater();
                row = l.inflate(R.layout.alternate, parent, false);
            }
            TextView t1, t2, v, d, ti;
            final ImageView img1, img2;
            img1 = (ImageView) row.findViewById(R.id.imageView);
            img2 = (ImageView) row.findViewById(R.id.imageView2);
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
            img1.setImageBitmap(fixture.getImg1());
            img2.setImageBitmap(fixture.getImg2());
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num1 = 1;
                    Intent intent = new Intent(MainActivity.this, NewFixture.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Image1int", num1);
                    bundle.putString("Image1", fixture.getTeam1());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num2 = 2;
                    Intent intent = new Intent(MainActivity.this, NewFixture.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("Image2", fixture.getTeam2());
                    bundle2.putInt("Image2int", num2);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
            });
            return row;

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == imgn1) {
                Uri imageuri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageuri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    bitmapimg1 = BitmapFactory.decodeStream(inputStream, null, options);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == imgn2) {
                Uri imageuri = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(imageuri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    bitmapimg2 = BitmapFactory.decodeStream(inputStream, null, options);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Unable to open", Toast.LENGTH_SHORT).show();

                }
            }
            if (requestCode == cameraImg1)
                bitmapimg1 = (Bitmap) data.getExtras().get("data");
            if (requestCode == cameraImg2)
                bitmapimg2 = (Bitmap) data.getExtras().get("data");
        }
    }

}

