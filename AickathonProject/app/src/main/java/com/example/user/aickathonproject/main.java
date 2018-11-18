package com.example.user.aickathonproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PieChart pieChart,pieChart2;
    private FirebaseDatabase database;
    private DatabaseReference mDataRef;
    private ArrayList<Place> items = new ArrayList<>();
    private String currenttime;
    private Place_listview_adapter adapter;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyste);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PopulationLight");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adapter = new Place_listview_adapter(this, items);
        listview = this.findViewById(R.id.place_listview);
        listview.setAdapter(adapter);

        SimpleDateFormat fullyear = new SimpleDateFormat("yyyy");
        fullyear.setTimeZone(TimeZone.getTimeZone("GMT+0800"));

        SimpleDateFormat fullmon = new SimpleDateFormat("MM");
        fullmon.setTimeZone(TimeZone.getTimeZone("GMT+0800"));

        SimpleDateFormat fullDate = new SimpleDateFormat("dd");
        fullDate.setTimeZone(TimeZone.getTimeZone("GMT+0800"));

        SimpleDateFormat fullTime = new SimpleDateFormat("HH");
        fullTime.setTimeZone(TimeZone.getTimeZone("GMT+0800"));

        final String year = fullyear.format(new Date());
        final String month = fullmon.format(new Date());
        final String date = fullDate.format(new Date());
        final String time = fullTime.format(new Date());

        if(0 <= Integer.parseInt(time) && 3 > Integer.parseInt(time)){
            currenttime="0-3";
        }else if(3 <= Integer.parseInt(time) && 6 > Integer.parseInt(time)){
            currenttime="3-6";
        }else if(6 <= Integer.parseInt(time) && 9 > Integer.parseInt(time)){
            currenttime="6-9";
        }else if(9 <= Integer.parseInt(time) && 12 > Integer.parseInt(time)){
            currenttime="9-12";
        }else if(12 <= Integer.parseInt(time) && 15 > Integer.parseInt(time)){
            currenttime="12-15";
        }else if(15 <= Integer.parseInt(time) && 18 > Integer.parseInt(time)){
            currenttime="15-18";
        }else if(18 <= Integer.parseInt(time) && 21 > Integer.parseInt(time)){
            currenttime="18-21";
        }else if(21 <= Integer.parseInt(time) && 24 > Integer.parseInt(time)){
            currenttime="21-24";
        }

        database = FirebaseDatabase.getInstance();
        mDataRef = database.getReference();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle info = new Bundle();
                info.putString("Num_female", items.get(i).getNum_female());
                info.putString("Num_male", items.get(i).getNum_male());
                info.putString("15_below", items.get(i).getAge_below_15());
                info.putString("15To55", items.get(i).getAge_15To55());
                info.putString("55_above", items.get(i).getAge_above55());
                info.putString("date", items.get(i).getDate());
                info.putString("location", items.get(i).getLocation());
                info.putString("key_date", items.get(i).getKey_date());

                Intent intent = new Intent(main.this,AnalyzeActivity.class);
                intent.putExtra("Info_bundle",info);

                startActivity(intent);
            }
        });

        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Place place = new Place();
                    place.setLocation(snapshot.getKey());

                    if(snapshot.child(year).child(month).child(date).child(currenttime).child("Age").child("15 below").getValue() != null &&
                            snapshot.child(year).child(month).child(date).child(currenttime).child("Age").child("15-55").getValue() != null &&
                            snapshot.child(year).child(month).child(date).child(currenttime).child("Age").child("55 above").getValue() != null){
                        place.setAge_below_15(snapshot.child(year).child(month).child(date).child(currenttime).child("Age").child("15 below").getValue().toString());
                        place.setAge_15To55(snapshot.child(year).child(month).child(date).child(currenttime).child("Age").child("15-55").getValue().toString());
                        place.setAge_above55(snapshot.child(year).child(month).child(date).child(currenttime).child("Age").child("55 above").getValue().toString());
                        place.setKey_date(currenttime);

                        String[] dateformat = currenttime.split("-");
                        SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm");
                        currentDate.setTimeZone(TimeZone.getTimeZone("GMT+0800"));
                        String time = currentDate.format(new Date());

                        place.setDate(dateformat[0]+":00 - "+ time);

                        int num_male = Integer.parseInt(snapshot.child(year).child(month).child(date).child(currenttime).child("Gender").child("Male").getValue().toString());
                        int num_female = Integer.parseInt(snapshot.child(year).child(month).child(date).child(currenttime).child("Gender").child("Female").getValue().toString());
                        place.setTotal_num(String.valueOf(num_male+num_female));
                        place.setNum_female(String.valueOf(num_female));
                        place.setNum_male(String.valueOf(num_male));

                        items.add(place);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.analyste, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, AnalyzeActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(this, AnalyzeActivity.class);
            startActivity(i);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
