package com.example.parent.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.parent.MainActivity;
import com.example.parent.R;
import com.example.parent.ui.UserData;
import com.example.parent.ui.selectReportType.selectReportType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    static ArrayList arrayList;
    static ArrayAdapter<String> arrayAdapter;
    private DatabaseReference mDatabase;
    public ListView listView;
    public View onCreateView(@NonNull  LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView)root.findViewById(R.id.childslist);
        arrayList = new ArrayList();
        listOfDevices();

     return root;
    }


  public void listOfDevices(){

      final FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference ref1 = database.getReference("CallReport/"+UserData.getMobileNumber()+"/");
      Log.i("bbbb",ref1.toString());

      ref1.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {

              for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                  arrayList.add(String.valueOf(dsp.getKey())); //add result into array list

              }

              arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
              listView.setAdapter(arrayAdapter);

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
              System.out.println("The read failed: " + databaseError.getCode());
          }
      });


      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              try {
                  Intent intent = new Intent(getContext(), selectReportType.class);
                  intent.putExtra("name", String.valueOf(listView.getItemAtPosition(i)));
                  startActivity(intent);
              }
              catch (Exception ex){
                  Log.i("errrrrrorrr",ex.getMessage());
              }



          }
      });





  }





}