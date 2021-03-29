package com.example.ece407project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final double[][] graph = new double[100][2];
        final int[] ClassValues = new int[100];
        final int[] amtOfPoints = {0};

        // Adds a point in a specific class
        Button Btn1 = (Button)findViewById(R.id.Btn1);
        Btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText xCoordET = (EditText)findViewById(R.id.xCoord);
                EditText yCoordET = (EditText)findViewById(R.id.yCoord);
                EditText ClassET = (EditText)findViewById(R.id.Class);
                TextView resultTextView = (TextView)findViewById(R.id.resultTextView);

                double xCoord = Double.parseDouble(xCoordET.getText().toString());
                double yCoord = Double.parseDouble(yCoordET.getText().toString());
                int Class = Integer.parseInt(ClassET.getText().toString());

                // Checking for errors
                boolean error = false;
                if(Class != 0 && Class != 1){
                    error = true;
                    resultTextView.setText("ERROR: Point must be class 0 or class 1.");
                }
                for(int i = 0; i < amtOfPoints[0]; i++){
                    if(xCoord == graph[i][0] && yCoord == graph[i][1]){
                        error = true;
                        resultTextView.setText("ERROR: Point already exists");
                    }
                }

                if(!error){
                    // Updating values
                    ClassValues[amtOfPoints[0]] = Class;
                    graph[amtOfPoints[0]][0] = xCoord;
                    graph[amtOfPoints[0]][1] = yCoord;
                    resultTextView.setText(graph[amtOfPoints[0]][0] + " x " + graph[amtOfPoints[0]][1] + "\nadded to class " + ClassValues[amtOfPoints[0]]);
                    amtOfPoints[0] = amtOfPoints[0] + 1;
                }
            }
        });

        // Adds a point classified based on K Nearest Neighbor
        Button Btn2 = (Button)findViewById(R.id.Btn2);
        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText xCoordET2 = (EditText)findViewById(R.id.xCoord2);
                EditText yCoordET2 = (EditText)findViewById(R.id.yCoord2);
                EditText KValueET = (EditText)findViewById(R.id.KValue);
                TextView resultTextView2 = (TextView)findViewById(R.id.resultTextView2);

                double xCoord2 = Double.parseDouble(xCoordET2.getText().toString());
                double yCoord2 = Double.parseDouble(yCoordET2.getText().toString());
                int KValue = Integer.parseInt(KValueET.getText().toString());

                // Checking for errors
                boolean error = false;
                if(KValue > amtOfPoints[0]){
                    error = true;
                    resultTextView2.setText("ERROR: The value of K is higher than the total amount of points.");
                }
                if(KValue == 0){
                    error = true;
                    resultTextView2.setText("ERROR: Cannot have a value of K=0.");
                }
                for(int i = 0; i < amtOfPoints[0]; i++){
                    if(xCoord2 == graph[i][0] && yCoord2 == graph[i][1]){
                        error = true;
                        resultTextView2.setText("ERROR: Point already exists");
                    }
                }

                if(!error){
                    int CurrentClasses[] = new int[100];
                    double distances[] = new double[100];
                    double tempx = 0.0;
                    double tempy = 0.0;

                    // Calculating distances between each point
                    for(int i = 0; i < amtOfPoints[0]; i++){
                        tempx = graph[i][0];
                        tempy = graph[i][1];
                        distances[i] = Math.sqrt(Math.pow(tempx - xCoord2, 2) + Math.pow(tempy - yCoord2, 2));
                        CurrentClasses[i] = ClassValues[i];
                    }

                    // Very efficient bubble sort
                    double temp = 0;
                    int temp2 = 0;
                    for(int i = 0; i < amtOfPoints[0] - 1; i++){
                        for(int j = 0; j < amtOfPoints[0] - 1; j++){
                            if(distances[j] > distances[j+1]){
                                temp = distances[j];
                                distances[j] = distances[j+1];
                                distances[j+1] = temp;

                                temp2 = CurrentClasses[j];
                                CurrentClasses[j] = CurrentClasses[j+1];
                                CurrentClasses[j+1] = temp2;
                            }
                        }
                    }

                    // Finding amount of points in each class given a certain K value
                    int oneCount = 0;
                    int zeroCount = 0;
                    for(int i = 0; i < KValue; i++){
                        if(CurrentClasses[i] == 0){
                            zeroCount++;
                        }
                        else if(CurrentClasses[i] == 1){
                            oneCount++;
                        }
                        else{
                            throw new RuntimeException("ERROR: somehow a class doesn't have a value of 0 nor 1");
                        }
                    }
                    int Class;
                    if(oneCount > zeroCount){
                        Class = 1;
                    }
                    else if(zeroCount > oneCount){
                        Class = 0;
                    }
                    else{
                        // Equal amounts of zero and one classes with given K Value
                        // So we pick a random class
                        Class = (int)(Math.random() * 2);
                    }

                    // Updating values
                    graph[amtOfPoints[0]][0] = xCoord2;
                    graph[amtOfPoints[0]][1] = yCoord2;
                    ClassValues[amtOfPoints[0]] = Class;
                    resultTextView2.setText(graph[amtOfPoints[0]][0] + " x " + graph[amtOfPoints[0]][1] + "\nadded to class " + ClassValues[amtOfPoints[0]]
                            + "\nbased on K=" + KValue + "\nwith " + oneCount + " nearest points in class 1\nand " + zeroCount + " nearest points in class 0.");
                    amtOfPoints[0] = amtOfPoints[0] + 1;
                }


            }
        });

        // Deletes all points
        Button deleteBtn = (Button)findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i > amtOfPoints[0]; i++){
                    graph[i][0] = 0;
                    graph[i][1] = 0;
                }
                amtOfPoints[0] = 0;
                showToast(v, "All points deleted.");
            }
        });

        // Shows graph of all points
        Button graphBtn = (Button)findViewById(R.id.graphBtn);
        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("graph", graph);
                b.putSerializable("points", amtOfPoints[0]);
                b.putSerializable("class", ClassValues);
                startIntent.putExtras(b);
                startActivity(startIntent);
            }
        });


    }
    // Basic temporary popup message
    public void showToast(View v, String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
