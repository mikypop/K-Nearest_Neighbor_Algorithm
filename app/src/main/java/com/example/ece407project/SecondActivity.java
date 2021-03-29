package com.example.ece407project;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView tv = (TextView)findViewById(R.id.textView);
        Bundle b = getIntent().getExtras();
        double[][] graph = (double[][])b.getSerializable("graph");
        int amtOfPoints = (int)b.getSerializable("points");
        int[] ClassValues = (int[])b.getSerializable("class");
        tv.setText("Key:\nRed = Class 0\nBlue = Class 1");

        GraphView gv = (GraphView)findViewById(R.id.graphView);
        PointsGraphSeries<DataPoint> seriesZero = new PointsGraphSeries<DataPoint>();
        PointsGraphSeries<DataPoint> seriesOne = new PointsGraphSeries<DataPoint>();

        double minx = 0;
        double miny = 0;
        double maxx = 0;
        double maxy = 0;

        // Temporary min and max x and y values
        if(amtOfPoints >= 1){
            minx = graph[0][0];
            miny = graph[0][1];
            maxx = graph[0][0];
            maxy = graph[0][0];
        }

        // Another very efficient bubble sort
        double temp = 0;
        double temp2 = 0;
        int temp3 = 0;
        if(amtOfPoints > 1){
            for(int i = 0; i < amtOfPoints - 1; i++){
                for(int j = 0; j < amtOfPoints - 1; j++){
                    if(graph[j][0] > graph[j+1][0]){
                        temp = graph[j][0];
                        temp2 = graph[j][1];
                        graph[j][0] = graph[j+1][0];
                        graph[j][1] = graph[j+1][1];
                        graph[j+1][0] = temp;
                        graph[j+1][1] = temp2;
                        temp3 = ClassValues[j];
                        ClassValues[j] = ClassValues[j+1];
                        ClassValues[j+1] = temp3;
                    }
                }
            }
        }

        // Adding all points to the graph and categorizing points to classes
        double x,y;
        for(int i = 0; i < amtOfPoints; i++){
            x = graph[i][0];
            y = graph[i][1];
            if(ClassValues[i] == 0){
                seriesZero.appendData(new DataPoint(x, y), true, amtOfPoints);
            }
            else if(ClassValues[i] == 1){
                seriesOne.appendData(new DataPoint(x, y), true, amtOfPoints);
            }
            else{
                // This should never happen, but in this case the point will be classified as point 1
                seriesOne.appendData(new DataPoint(x, y), true, amtOfPoints);
            }

            // Finding min and max x and y values
            if(minx > x){
                minx = x;
            }
            if(miny > y){
                miny = y;
            }

            if(maxx < x){
                maxx = x;
            }
            if(maxy < y){
                maxy = y;
            }
        }

        // Setting range of view of graph
        if(amtOfPoints != 0){
            gv.getViewport().setXAxisBoundsManual(true);
            gv.getViewport().setMinX(minx - 4);
            gv.getViewport().setMaxX(maxx + 4);

            gv.getViewport().setYAxisBoundsManual(true);
            gv.getViewport().setMinY(miny - 4);
            gv.getViewport().setMaxY(maxy + 4);
        }

        seriesZero.setColor(Color.RED);
        seriesOne.setColor(Color.BLUE);
        gv.addSeries(seriesOne);
        gv.addSeries(seriesZero);

    }
}
