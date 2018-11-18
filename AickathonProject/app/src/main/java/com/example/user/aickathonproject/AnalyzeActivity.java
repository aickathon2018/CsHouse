package com.example.user.aickathonproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class AnalyzeActivity extends AppCompatActivity {

    private PieChart pieChart,pieChart2;
    private Bundle bundle;
    private TextView pie_title_gender,pie_title_age;
    private FloatingActionButton prediction_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        bundle = getIntent().getBundleExtra("Info_bundle");

        pie_title_gender = findViewById(R.id.pie_title_gender);
        pie_title_age = findViewById(R.id.pie_title_age);
        pieChart = findViewById(R.id.forecast_pieChart);
        prediction_btn = findViewById(R.id.prediction_btn);

        prediction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalyzeActivity.this,predictionActivity.class);
                intent.putExtra("info_bundle",bundle);
                startActivity(intent);
            }
        });

        String[] aa = bundle.get("date").toString().split("-");

        SimpleDateFormat fullDate = new SimpleDateFormat("HH:mm");
        fullDate.setTimeZone(TimeZone.getTimeZone("GMT+0800"));
        String time = fullDate.format(new Date());

        pie_title_gender.setText("Visitor By Gender (" +  aa[0] + "to " + time + ")");
        pie_title_age.setText("Visitor By Age (" +  aa[0] +  "to " + time + ")");
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

         ArrayList<PieEntry> yvalue = new ArrayList<>();
         if(Integer.parseInt(bundle.get("Num_female").toString()) > 0){
             yvalue.add(new PieEntry(Integer.parseInt(bundle.get("Num_female").toString()),"Female"));
         }
         if(Integer.parseInt(bundle.get("Num_male").toString()) > 0){
             yvalue.add(new PieEntry(Integer.parseInt(bundle.get("Num_male").toString()),"Male"));
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

        pieChart2 = findViewById(R.id.pieChart2);

        pieChart2.setUsePercentValues(true);
        pieChart2.getDescription().setEnabled(false);
        pieChart2.setExtraOffsets(5,10,5,5);
        pieChart2.setDragDecelerationFrictionCoef(0.95f);
        pieChart2.setDrawHoleEnabled(false);
        pieChart2.setHoleColor(Color.WHITE);
        pieChart2.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yyvalue = new ArrayList<>();

        if(Integer.parseInt(bundle.get("15_below").toString()) > 0){
            yyvalue.add(new PieEntry(Integer.parseInt(bundle.get("15_below").toString()),"15 below"));
        }
        if(Integer.parseInt(bundle.get("15To55").toString()) > 0){
            yyvalue.add(new PieEntry(Integer.parseInt(bundle.get("15To55").toString()),"15 - 55"));
        }
        if(Integer.parseInt(bundle.get("55_above").toString()) > 0){
            yyvalue.add(new PieEntry(Integer.parseInt(bundle.get("55_above").toString()),"55 above"));
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
