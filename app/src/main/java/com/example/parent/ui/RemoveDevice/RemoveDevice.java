package com.example.parent.ui.RemoveDevice;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parent.R;
import com.example.parent.ui.UserData;
import com.example.parent.ui.selectReportType.selectReportType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;

public class RemoveDevice extends Fragment {

    public ArrayList arrayList;
    public ArrayAdapter arrayAdapter;
    private DatabaseReference mDatabase;
    public ListView listView;
    public Button removebtn;

    public static RemoveDevice newInstance() {
        return new RemoveDevice();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.remove_device_fragment, container, false);
        inital(root);
        listView = (ListView)root.findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter= new ArrayAdapter(getContext(),android.R.layout.simple_list_item_multiple_choice, arrayList);
        listView.setAdapter(arrayAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = database.getReference("CallReport/"+ UserData.getMobileNumber()+"/");
        Log.i("bbbb",ref1.toString());

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    arrayList.add(String.valueOf(dsp.getKey())); //add result into array list

                }

              arrayAdapter.notifyDataSetChanged();
             }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
              removeSelectedDevices();

            }
        });


        return root;
    }



      public void removeSelectedDevices(){

          final SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
          if (checkedItemPositions.toString()=="{}"){
              Toast.makeText(getContext(),"Please Select Device",Toast.LENGTH_LONG).show();
          }
          else{
              AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
              builder.setTitle("Alert").setIcon(R.drawable.ic_warning_black_24dp)
                      .setMessage("Are you sure, you want to Remove this Device?")
                      .setCancelable(false)
                      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              int itemCount = listView.getCount();
                              for(int i=itemCount-1; i >= 0; i--){
                                  if(checkedItemPositions.get(i)){
                                      final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                      DatabaseReference ref1 = database.getReference("CallReport/"+UserData.getMobileNumber()+"/");
                                      ref1.child(""+arrayList.get(i)).removeValue();
                                      arrayList.remove(arrayList.get(i));
                                  }
                              }
                              arrayList.clear();
                              arrayAdapter.notifyDataSetChanged();
                              checkedItemPositions.clear();


                          }
                      })
                      .setNegativeButton("No", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {

                              checkedItemPositions.clear();
                          }
                      });
              //Creating dialog box
              AlertDialog dialog  = builder.create();
              dialog.show();


          }
      }

    public void inital(View root) {

        removebtn=root.findViewById(R.id.btnDel);

    }


    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }


        /*SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        int itemCount = listView.getCount();

        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
                arrayList.remove(arrayList.get(i));
            }
        }
        checkedItemPositions.clear();
      arrayAdapter.notifyDataSetChanged();
*/








}
