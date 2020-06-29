package com.example.parent.ui.selectReportType;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.parent.R;
import com.example.parent.ui.UserData;
import com.example.parent.ui.getReport.getReport;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class selectReportType extends AppCompatActivity {
    TextView tv_Name;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_report_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_Name=(TextView)findViewById(R.id.tv_Name);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        name= (String) bundle.get( "name");
        tv_Name.setText(name);
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


    public void getCallReport(View view){
        Intent intent=new Intent(selectReportType.this, getReport.class);
        intent.putExtra("name",name);
        intent.putExtra("action","call");
        startActivity(intent);
    }

    public void getMessageReport(View view){
       insertDummychild();
       insertDummySuspiciousNumbers();
        /*Intent intent=new Intent(selectReportType.this, getReport.class);
        intent.putExtra("name",name);
        intent.putExtra("action","message");
        startActivity(intent);
   */

    }

    public void insertDummychild(){
        String[] strArray2 = {"Yusra","ubaid","Ayesha","usman","Ahmed","Ali"};
        for (int i=0;i<strArray2.length;i++){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref1 = database.getReference("CallReport/"+ UserData.getMobileNumber());
            DatabaseReference ref2 = database.getReference("MesasgeReport/"+ UserData.getMobileNumber());
            ref1.child(strArray2[i]).setValue("");
            ref2.child(strArray2[i]).setValue("");

        }

    }
    public void insertDummySuspiciousNumbers(){
        String[] strArray2 = {"25","24","23","22","21","20","19"};
        String[] strArray = {"03481618040","03481618040","03074590456","03481618040","03074590640","03481618040","03074590456"};
        for (int i=0;i<strArray2.length;i++){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref1 = database.getReference("CallReport/"+ UserData.getMobileNumber()+"/Ubaid");
            ref1.child(strArray2[i]+"-Jun-2020").child(strArray[i]).setValue("");
        }

    }

}
