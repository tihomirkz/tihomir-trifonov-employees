package com.example.tihomir_trifonov_employees;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int request_code =1;

    public static int PICK_FILE = 1;
    private String TAG ="mainactivty";
    GridView gridView;

    int numberCoupleEmp = 0;
    ArrayList<Employees> arrayEmployees = new ArrayList<Employees>();
    ArrayList<CoupleEmployees> arrayCoupleEmployees = new ArrayList<CoupleEmployees>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            readTextFile(getAssets().open("test.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            checkEqualProjects();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Button openFile=(Button) findViewById(R.id.button);

        openFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    // start runtime permission
                    Boolean hasPermission =( ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission){
                        Log.e(TAG, "get permision   ");
                        ActivityCompat.requestPermissions( MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_code);

                    }else {
                        Log.e(TAG, "get permision-- already granted ");

                        showFileChooser();
                    }
                }else {
                    showFileChooser();
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:{
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //readfile();
                    showFileChooser();
                }else {
                    // show a msg to user
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE) {
            if (resultCode == RESULT_OK) {
                // User pick the file
                Uri uri = data.getData();

                try {
                    readTextFile(getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    checkEqualProjects();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, data.toString());
            }
        }
    }

    private void readTextFile(InputStream file){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        String[] readData;
        arrayEmployees.clear();
        numberCoupleEmp = 0;
        arrayCoupleEmployees.clear();
        try {
            reader = new BufferedReader(new InputStreamReader(file));
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append(line);
                readData = line.split(",");
                Employees student1= new Employees(readData[0], readData[1], readData[2], readData[3]);
                arrayEmployees.add(student1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("text/plain");
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE);

    }

//  Проверка за двойка служители, които работят по еднакъв проект
    public void checkEqualProjects() throws ParseException {

        for(int i = 0; i < arrayEmployees.size(); i++) {
            for (int j = i + 1; j < arrayEmployees.size(); j++) {
                if (i != j) {
                    if (arrayEmployees.get(i).getProjectId().equals(arrayEmployees.get(j).getProjectId())) {
                        int employerOne = i;
                        int employerTwo = j;
                        compareDate(employerOne, employerTwo);
                    }
                }
            }
        }
        checkEqualCoupleEmp();
    }

    public void checkEqualCoupleEmp() {
        for(int i = 0; i < arrayCoupleEmployees.size(); i++){
            for(int j = i+1; j < arrayCoupleEmployees.size(); j++){

                if ((arrayCoupleEmployees.get(i).getEmployerOneId().equals(arrayCoupleEmployees.get(j).getEmployerOneId()))
                        && (arrayCoupleEmployees.get(i).getEmployerTwoId().equals(arrayCoupleEmployees.get(j).getEmployerTwoId()))) {

                    if (arrayCoupleEmployees.get(i).getSumWorkDays() == 0){
                        arrayCoupleEmployees.get(i).setSumWorkDays(arrayCoupleEmployees.get(i).getWorkDays() + arrayCoupleEmployees.get(j).getWorkDays());
                    } else {
                        arrayCoupleEmployees.get(i).setSumWorkDays(arrayCoupleEmployees.get(i).getSumWorkDays() + arrayCoupleEmployees.get(j).getWorkDays());
                    }
                    arrayCoupleEmployees.get(j).setSumWorkDays(arrayCoupleEmployees.get(i).getSumWorkDays());

                } else if ((arrayCoupleEmployees.get(i).getEmployerOneId().equals(arrayCoupleEmployees.get(j).getEmployerTwoId()))
                            && (arrayCoupleEmployees.get(i).getEmployerTwoId().equals(arrayCoupleEmployees.get(j).getEmployerOneId()))) {

                    if (arrayCoupleEmployees.get(i).getSumWorkDays() == 0){
                        arrayCoupleEmployees.get(i).setSumWorkDays(arrayCoupleEmployees.get(i).getWorkDays() + arrayCoupleEmployees.get(j).getWorkDays());
                    } else {
                        arrayCoupleEmployees.get(i).setSumWorkDays(arrayCoupleEmployees.get(i).getSumWorkDays() + arrayCoupleEmployees.get(j).getWorkDays());
                    }
                    arrayCoupleEmployees.get(j).setSumWorkDays(arrayCoupleEmployees.get(i).getSumWorkDays());
                }
            }
        }
        addResultToTable();
    }

    public String currentData() {
        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        Log.d("result curent data:", String.valueOf(date));
        return String.valueOf(date);
    }

    public int workingDays (Date date1, Date date2) {
        long difference_In_Time
                = date1.getTime() - date2.getTime();
        long difference_In_Days
                = (difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;
        Log.d("result days:", String.valueOf(difference_In_Days));
        return (int)difference_In_Days;
    }

// Сравняване на периодите от време, когато са работили служителите по един и същи проект
    public void compareDate(int employerOne, int employerTwo) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String employerOneId = arrayEmployees.get(employerOne).getEmployerId();
        String employerTwoId = arrayEmployees.get(employerTwo).getEmployerId();
        String projectId = arrayEmployees.get(employerOne).getProjectId();

        String finishDateOne = arrayEmployees.get(employerOne).getDateTo();
        if (finishDateOne.equals("NULL")) {
            finishDateOne = currentData();
        }

        String finishDateSecond = arrayEmployees.get(employerTwo).getDateTo();
        if (finishDateSecond.equals("NULL")) {
            finishDateSecond = currentData();
        }

        Date startDate1 = sdf.parse(arrayEmployees.get(employerOne).getDateFrom());
        Date startDate2 = sdf.parse(arrayEmployees.get(employerTwo).getDateFrom());


        Date finishDate1 = sdf.parse(finishDateOne);
        Date finishDate2 = sdf.parse(finishDateSecond);
        if (startDate1.after(startDate2)) {
            if (startDate1.after(finishDate2)) {
                Log.d("result:", "Not match");
            } else if(startDate1.before(finishDate2)) {
                Log.d("result:", "Match: startData1 is, before  finishData2");
                CoupleEmployees employers = new CoupleEmployees(employerOneId , employerTwoId, projectId, workingDays(finishDate2, startDate1), 0);
                arrayCoupleEmployees.add(numberCoupleEmp, employers);
                numberCoupleEmp ++;
            }
        }

        if (startDate1.before(startDate2)) {
            if (startDate2.before(finishDate1)) {
                Log.d("result:", "Match: startData2 is, before finishData1");
                CoupleEmployees employers = new CoupleEmployees(employerOneId, employerTwoId, projectId, workingDays(finishDate1, startDate2), 0);
                arrayCoupleEmployees.add(numberCoupleEmp, employers);
                numberCoupleEmp ++;

            } else if (startDate2.after(finishDate1)) {
                Log.d("result:", "Not match");
            }
        }

        if (startDate1.equals(startDate2)) {
            Log.d("result:", "Match: startData1 = startData2");

            if(finishDate1.before(finishDate2)) {
                CoupleEmployees employers = new CoupleEmployees(employerOneId, employerTwoId, projectId, workingDays(finishDate1, startDate1), 0);
                arrayCoupleEmployees.add(numberCoupleEmp, employers);

            } else if(finishDate1.after(finishDate2)) {
                CoupleEmployees employers = new CoupleEmployees(employerOneId, employerTwoId, projectId, workingDays(finishDate2, startDate1), 0);
                arrayCoupleEmployees.add(numberCoupleEmp, employers);

            } else if(finishDate1.equals(finishDate2)) {
                CoupleEmployees employers = new CoupleEmployees(employerOneId, employerTwoId , projectId, workingDays(finishDate1, startDate1), 0);
                arrayCoupleEmployees.add(numberCoupleEmp, employers);

            }
            numberCoupleEmp ++;
        }
    }

    public void addResultToTable(){
        Collections.sort(
                arrayCoupleEmployees,
                new Comparator<CoupleEmployees>() {
                    @Override
//                    public int compare(CoupleEmployess employer1, CoupleEmployess employer2) {
//                        return employer2.getWorkDays()
//                                - employer1.getWorkDays();
//                    }
                    public int compare(CoupleEmployees employer1, CoupleEmployees employer2) {
                        return employer2.getSumWorkDays()
                                - employer1.getSumWorkDays();
                    }
                });

        List list = new ArrayList();
        for (CoupleEmployees array : arrayCoupleEmployees) {
            list.add(Arrays.asList(array));
        }

        List<String> values = new ArrayList<String>();
        values.add("Employee ID #1");
        values.add("Employee ID #2");
        values.add("Project ID");
        values.add("Days worked");
        values.add("Sum of days worked");

        for(int i = 0; i < arrayCoupleEmployees.size(); i++) {
            values.add(arrayCoupleEmployees.get(i).getEmployerOneId());
            values.add(arrayCoupleEmployees.get(i).getEmployerTwoId());
            values.add(arrayCoupleEmployees.get(i).getProjectId());
            values.add(String.valueOf(arrayCoupleEmployees.get(i).getWorkDays()));
            values.add(String.valueOf(arrayCoupleEmployees.get(i).getSumWorkDays()));
        }

        gridView = (GridView) findViewById(R.id.gridView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        R.layout.cell, values);
        gridView.setAdapter(adapter);
    }
}