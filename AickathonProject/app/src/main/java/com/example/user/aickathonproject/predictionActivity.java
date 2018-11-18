package com.example.user.aickathonproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class predictionActivity extends AppCompatActivity {

    private PieChart pieChart,pieChart2;
    private TextView pie_title_gender,pie_title_age,prediction_tilte,predict_visitor_txt;
    private Bundle bundle;
    private FirebaseDatabase database;
    private DatabaseReference mDataRef;
    private String prediction_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        bundle = getIntent().getBundleExtra("info_bundle");
        database = FirebaseDatabase.getInstance();
        predict_visitor_txt = findViewById(R.id.predict_visitor_txt);
        prediction_tilte = findViewById(R.id.prediction_tilte);
        pie_title_gender = findViewById(R.id.pie_title_gender);
        pie_title_age = findViewById(R.id.pie_title_age);
        pieChart = findViewById(R.id.forecast_pieChart);

        prediction_tilte.setText("Visitor Forecast for " + bundle.get("location"));
        String[] time = bundle.get("key_date").toString().split("-");

        if((Integer.parseInt(time[0])+3) < 24 && (Integer.parseInt(time[1]) + 3) < 24){
            prediction_time = String.valueOf((Integer.parseInt(time[0])+3) + "-" +(Integer.parseInt(time[1]) + 3));
        }else{
            prediction_time = "0-3";
        }


        generate_prediction();


        pie_title_gender.setText("Visitor By Gender (Prediction Time:" + prediction_time + ")");
        pie_title_age.setText("Visitor By Age (Prediction Time:" + prediction_time + ")");
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


        pieChart2 = findViewById(R.id.pieChart2);

        pieChart2.setUsePercentValues(true);
        pieChart2.getDescription().setEnabled(false);
        pieChart2.setExtraOffsets(5,10,5,5);
        pieChart2.setDragDecelerationFrictionCoef(0.95f);
        pieChart2.setDrawHoleEnabled(true);
        pieChart2.setHoleColor(Color.WHITE);
        pieChart2.setTransparentCircleRadius(61f);

    }

    public void generate_prediction(){

        mDataRef = database.getReference();

        mDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(bundle.get("location").toString().equals(dataSnapshot.getKey())){

                    int total_female = 0,power_female = 0,mean_female=0,variance_female=0,min_prediction_female,max_prediction_female;
                    int total_male = 0,power_male = 0,mean_male=0,variance_male=0,prediction_male,min_prediction_male,max_prediction_male;
                    int age_below_15 = 0,power_below_15 = 0,mean_age_below_15=0,variance_below_15=0,min_prediction_15below,max_prediction_15below;
                    int age_15To55 = 0,power_15To55 = 0,mean_age_15To55=0,variance_age_15To55=0,min_prediction_15_55,max_prediction_15_55;
                    int age_above_55 = 0,power_above_55 = 0,mean_age_above_55=0,variance_age_above_55=0,min_prediction_55above,max_prediction_55above;
                    long num_child = 0;

                    for(DataSnapshot shot : dataSnapshot.getChildren()){
                        for(DataSnapshot snapshot : shot.getChildren()){
                            for(DataSnapshot ss : snapshot.getChildren()){
                                for(DataSnapshot sss : ss.getChildren()){
                                    if(bundle.get("key_date").toString().equals(sss.getKey())){
                                        num_child = snapshot.getChildrenCount();
                                        total_female += Integer.parseInt(ss.child(prediction_time).child("Gender").child("Female").getValue().toString());
                                        power_female += Math.pow(Integer.parseInt(ss.child(prediction_time).child("Gender").child("Female").getValue().toString()),2);
                                        total_male += Integer.parseInt(ss.child(prediction_time).child("Gender").child("Male").getValue().toString());
                                        power_male += Math.pow(Integer.parseInt(ss.child(prediction_time).child("Gender").child("Male").getValue().toString()),2);

                                        age_below_15 += Integer.parseInt(ss.child(prediction_time).child("Age").child("15 below").getValue().toString());
                                        power_below_15 += Math.pow(Integer.parseInt(ss.child(prediction_time).child("Age").child("15 below").getValue().toString()),2);

                                        age_15To55 += Integer.parseInt(ss.child(prediction_time).child("Age").child("15-55").getValue().toString());
                                        power_15To55 += Math.pow(Integer.parseInt(ss.child(prediction_time).child("Age").child("15-55").getValue().toString()),2);

                                        age_above_55 += Integer.parseInt(ss.child(prediction_time).child("Age").child("55 above").getValue().toString());
                                        power_above_55 += Math.pow(Integer.parseInt(ss.child(prediction_time).child("Age").child("55 above").getValue().toString()),2);
                                    }
                                }
                            }

                            mean_female = total_female/(int)num_child;
                            mean_male = total_male/(int)num_child;
                            mean_age_below_15 = age_below_15/(int)num_child;
                            mean_age_15To55 = age_15To55/(int)num_child;
                            mean_age_above_55 = age_above_55/(int)num_child;

                            variance_female = (int)(Math.sqrt((power_female/(int)num_child)/Math.pow(mean_female,2)));
                            variance_male = (int)(Math.sqrt((power_male/(int)num_child)/Math.pow(mean_male,2)));
                            variance_below_15 = (int)(Math.sqrt((power_below_15/(int)num_child)/Math.pow(mean_age_below_15,2)));
                            variance_age_15To55 = (int)(Math.sqrt((power_15To55/(int)num_child)/Math.pow(mean_age_15To55,2)));
                            variance_age_above_55 = (int)(Math.sqrt((power_above_55/(int)num_child)/Math.pow(mean_age_above_55,2)));

                            min_prediction_female = (int)(mean_female - 1.96 * (variance_female/Math.sqrt((double)num_child)));
                            max_prediction_female = (int)(mean_female + 1.96 * (variance_female/Math.sqrt((double)num_child)));

                            min_prediction_male = (int)(mean_male - 1.96 * (variance_male/Math.sqrt((double)num_child)));
                            max_prediction_male = (int)(mean_male + 1.96 * (variance_male/Math.sqrt((double)num_child)));

                            min_prediction_15below = (int)(mean_age_below_15 - 1.96 * (variance_below_15/Math.sqrt((double)num_child)));
                            max_prediction_15below = (int)(mean_age_below_15 + 1.96 * (variance_below_15/Math.sqrt((double)num_child)));

                            min_prediction_15_55 = (int)(mean_age_15To55 - 1.96 * (variance_age_15To55/Math.sqrt((double)num_child)));
                            max_prediction_15_55 = (int)(mean_age_15To55 + 1.96 * (variance_age_15To55/Math.sqrt((double)num_child)));

                            min_prediction_55above = (int)(mean_age_above_55 - 1.96 * (variance_age_above_55/Math.sqrt((double)num_child)));
                            max_prediction_55above = (int)(mean_age_above_55 + 1.96 * (variance_age_above_55/Math.sqrt((double)num_child)));

                            predict_visitor_txt.setText("Predict visitor :" + ((min_prediction_female+max_prediction_female)/2 + (min_prediction_male+max_prediction_male)/2));


                            ArrayList<PieEntry> yvalue = new ArrayList<>();
                            if(num_child > 0){
                                yvalue.add(new PieEntry((min_prediction_female+max_prediction_female)/2,"Female"));
                            }

                            if(num_child > 0){
                                yvalue.add(new PieEntry((min_prediction_male+max_prediction_male)/2,"Male"));
                            }

                            PieDataSet dataSet = new PieDataSet(yvalue,"");
                            dataSet.setSliceSpace(3f);
                            dataSet.setSelectionShift(0);
                            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                            pieChart.animateY(1000, Easing.EaseInOutCubic);

                            PieData data = new PieData(dataSet);
                            data.setValueTextSize(12f);
                            data.setValueTextColor(Color.WHITE);

                            pieChart.setData(data);

                            ArrayList<PieEntry> yyvalue = new ArrayList<>();

                            if(num_child > 0){
                                yyvalue.add(new PieEntry((min_prediction_15below+max_prediction_15below)/2,"15 below"));
                            }
                            if(num_child > 0){
                                yyvalue.add(new PieEntry((min_prediction_15_55+max_prediction_15_55)/2,"15 - 55"));
                            }
                            if(num_child > 0){
                                yyvalue.add(new PieEntry((min_prediction_55above+max_prediction_55above)/2,"55 above"));
                            }

                            PieDataSet datadataSet = new PieDataSet(yyvalue,"");
                            datadataSet.setSliceSpace(3f);
                            datadataSet.setSelectionShift(0);
                            datadataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                            pieChart2.animateY(1000, Easing.EaseInOutCubic);

                            PieData datadata = new PieData(datadataSet);
                            datadata.setValueTextSize(12f);
                            datadata.setValueTextColor(Color.WHITE);

                            pieChart2.setData(datadata);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
