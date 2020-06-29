package com.example.parent.ui.getReport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.parent.R;
import com.example.parent.ui.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class getReport extends AppCompatActivity {
    public String name,action;
    public ArrayList arrayList;
    public ArrayAdapter<String> arrayAdapter;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        name=(String) bundle.get("name");
        action=(String) bundle.get("action");
        listView = (ListView)findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void yesterday(View view){
           arrayList.clear();
           arrayAdapter.notifyDataSetChanged();
        if (action.equals("call")){
         showReport("CallReport",1);
      //     showYestrdayReport("CallReport");
        }
        else{
            showReport("MessageReport",1);
        }

    }


    public void weekly(View view){
        arrayList.clear();
        arrayAdapter.notifyDataSetChanged();
        if (action.equals("call")){
            showReport("CallReport",7);
        }
        else{
            showReport("MessageReport",7);
        }

    }


    public void monthly(View view){
        arrayList.clear();
        arrayAdapter.notifyDataSetChanged();
        if (action.equals("call")){
            showReport("CallReport",30);
        }
        else{
            showReport("MessageReport",30);
        }
    }


    public  void  showReport(final String reportType , final int days){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref1 = database.getReference(reportType+"/" +UserData.getMobileNumber()+"/"+ name + "/");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int check=0;
              for (int i=1;i<=days;i++) {

                  if (snapshot.hasChild(daysAgo(i))) {
                      check++;
                    //  for (DataSnapshot child : snapshot.getChildren()) {
                          loadData(daysAgo(i), reportType);
                      //}
                      arrayAdapter.notifyDataSetChanged();
                  }
              }
              if (check==0){
                  arrayList.add("No Suspicious Number found");
                  arrayAdapter = new ArrayAdapter<String>(getReport.this, android.R.layout.simple_list_item_1, arrayList);
                  listView.setAdapter(arrayAdapter);

              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void loadData(final String date, String type){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref2 = database.getReference(type+"/"+UserData.getMobileNumber()+"/"+name+"/"+date);
            ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        arrayList.add(date);
                        arrayList.add(child.getKey());

                    }
                 arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

}

    public String daysAgo(int days) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, -days);
        Date date = gc.getTime();
        String requiredDate = new SimpleDateFormat("dd-MMM-yyyy").format(date);
        return requiredDate;
    }

   public  void  showYestrdayReport(final String reportType){
           final FirebaseDatabase database = FirebaseDatabase.getInstance();
           final DatabaseReference ref1 = database.getReference(reportType+"/" +UserData.getMobileNumber()+"/"+ name + "/");
           ref1.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot snapshot) {
                  if (snapshot.hasChild("13-May-2020")){
                   for (DataSnapshot child : snapshot.getChildren()) {
                       loadData("13-May-2020",reportType);
                     }
                   arrayAdapter.notifyDataSetChanged();
}
                  else{

                    arrayList.add("No Suspicious Number found");
                    arrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, arrayList);
                    listView.setAdapter(arrayAdapter);
                }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

    }


}
