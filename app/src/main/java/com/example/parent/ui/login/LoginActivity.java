package com.example.parent.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parent.MainActivity;
import com.example.parent.R;
import com.example.parent.ui.ForgetPassword.forgetPassword;
import com.example.parent.ui.UserData;
import com.example.parent.ui.Signup.Signup;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;

public class LoginActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    private TextView tv_Email;
    private TextView tv_Password;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isConnected(LoginActivity.this))
            buildDialog(LoginActivity.this).show();

        else {
            firebaseAuth = FirebaseAuth.getInstance();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {


                        updateUI(user.getEmail());
                    } else {

                        setContentView(R.layout.activity_login);
                    }

}


    }



    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else
                return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

  public  void updateUI(String Email){
      final FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference ref1 = database.getReference("/ParentData/");
      ref1.orderByChild("Email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                  final String parentMobileNumber = childSnapshot.getKey();

                  UserData.setMobileNumber(parentMobileNumber);

                  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                  startActivity(intent);
                  finish();
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });








  }


    public void btn_Signup_click(View view) {
        Intent intent = new Intent(this, Signup.class);
        this.startActivity(intent);
    }

    public void btn_Login_click(View view) {
        LoginUser();
    }

    private void LoginUser() {
        InitializeUi();
        final String Email = tv_Email.getText().toString();
        final String Password = tv_Password.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        //mAuth= FirebaseAuth.getInstance();

        if (Email.isEmpty() && Password.isEmpty()) {

            Toast.makeText(LoginActivity.this, "Please Enter Email and Password", Toast.LENGTH_LONG).show();


        } else if (Email.isEmpty()) {
            tv_Email.setError("Please Enter Email");
            tv_Email.requestFocus();

        } else if (!(Email.matches(emailPattern))) {
            tv_Email.setError("Please Enter Valid Email");
            tv_Email.requestFocus();
        } else if (Password.isEmpty()) {
            tv_Password.setError("Please Enter Password");
            tv_Password.requestFocus();


        } else if (Password.length() < 8) {
            tv_Password.setError("Please Enter Valid Password");
            tv_Password.requestFocus();
        } else {


            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(Email, Password).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                                updateUI(Email);


                            } else {

                                Toast.makeText(LoginActivity.this, "Login Failed  " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);


                            }

                        }
                    });
        }


    }

    public void btn_ForgetPassword_click(View view){
        Intent intent=new Intent(LoginActivity.this, forgetPassword.class);
        startActivity(intent);
    }

    private void InitializeUi() {
        mAuth = FirebaseAuth.getInstance();
        tv_Email = (TextView) findViewById(R.id.tv_Email);
        tv_Password = (TextView) findViewById(R.id.tv_Password);
        progressBar = findViewById(R.id.progressBar);

    }


}
