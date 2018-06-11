package com.example.karthik.delta2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewFixture extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    ArrayList<Data> specific = new ArrayList<>();
    ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fixture);
        dataBaseHelper = new DataBaseHelper(this);
        Bundle bundle = getIntent().getExtras();

        listView = (ListView) findViewById(R.id.newFixture);
        if (bundle.getInt("Image1int") == 1) {
            String team1 = bundle.getString("Image1");
            Cursor image1 = dataBaseHelper.getFixture(team1);

            while (image1.moveToNext()) {
                byte[] img1 = image1.getBlob(6);
                Bitmap flag1 = BitmapFactory.decodeByteArray(img1, 0, img1.length);
                byte[] img2 = image1.getBlob(7);
                Bitmap flag2 = BitmapFactory.decodeByteArray(img2, 0, img2.length);
                specific.add(new Data(image1.getInt(0), image1.getString(1), image1.getString(2), image1.getString(3), image1.getString(4), image1.getString(5), flag1, flag2));
            }
        }
       if(bundle.getInt("Image2int")==2) {
            Bundle bundle2 = getIntent().getExtras();
            String team2 = bundle2.getString("Image2");
            Cursor image2 = dataBaseHelper.getFixture2(team2);
            while (image2.moveToNext()) {
                byte[] img1 = image2.getBlob(6);
                Bitmap flag1 = BitmapFactory.decodeByteArray(img1, 0, img1.length);
                byte[] img2 = image2.getBlob(7);
                Bitmap flag2 = BitmapFactory.decodeByteArray(img2, 0, img2.length);
                specific.add(new Data(image2.getInt(0), image2.getString(1), image2.getString(2), image2.getString(3), image2.getString(4), image2.getString(5), flag1, flag2));
            }
        }
        customAdapter adapter = new customAdapter(this, R.layout.alternate, specific);
        listView.setAdapter(adapter);
    }

    class customAdapter extends ArrayAdapter<Data> {

        public customAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Data> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                row = layoutInflater.inflate(R.layout.alternate, parent, false);
            }
            Data data = getItem(position);
            TextView t1, t2, v, d, ti;
            ImageView i1, i2;
            t1 = (TextView) row.findViewById(R.id.listteam1);
            t2 = (TextView) row.findViewById(R.id.listteam2);
            v = (TextView) row.findViewById(R.id.listvenue);
            d = (TextView) row.findViewById(R.id.listdate);
            ti = (TextView) row.findViewById(R.id.listtime);
            i1 = (ImageView) row.findViewById(R.id.imageView);
            i2 = (ImageView) row.findViewById(R.id.imageView2);
            t1.setText(data.getTeam1());
            t2.setText(data.getTeam2());
            v.setText(data.getVenue());
            d.setText(data.getDate());
            ti.setText(data.getTime());
            i1.setImageBitmap(data.getImg1());
            i2.setImageBitmap(data.getImg2());
            return row;
        }
    }


}
