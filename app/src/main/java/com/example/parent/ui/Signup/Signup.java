package com.example.parent.ui.Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parent.R;
import com.example.parent.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Signup extends AppCompatActivity {

    public DatabaseReference mDatabase;
    public EditText tv_Email, tv_UserName, tv_Password, tv_MobileNumber;
    public String Email, UserName, Password, MobileNumber ,phoneNumber;
    public FirebaseAuth mAuth;
    public CountryCodePicker ccp;
    public String verificationId;
    public AlertDialog alertDialog;
    public ProgressDialog progressVerify;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



    }


    public void btn_AlreadyAccount(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        finish();


    }

    public void btn_SignUp_click(View view) {
        SignUpUser();
    }

    public void InitializeUi() {
        mAuth = FirebaseAuth.getInstance();
        tv_Email = (EditText) findViewById(R.id.tv_Email);
        tv_UserName = (EditText) findViewById(R.id.tv_UserName);
        tv_Password = (EditText) findViewById(R.id.tv_Password);
        tv_MobileNumber = (EditText) findViewById(R.id.tv_MobileNumber);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(tv_MobileNumber);

    }

    public void SignUpUser() {
        InitializeUi();
        Email = tv_Email.getText().toString();
        UserName = tv_UserName.getText().toString();
        Password = tv_Password.getText().toString();
        MobileNumber = tv_MobileNumber.getText().toString();
        phoneNumber = tv_MobileNumber.getText().toString().trim();
        phoneNumber = ccp.getFullNumberWithPlus();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (Email.isEmpty()) {
            tv_Email.setError("Please Enter Email");
            tv_Email.requestFocus();
        } else if (Email.length() > 40) {
            tv_Email.setError("Length must be smaller then 40");
            tv_Email.requestFocus();
        } else if (!(Email.matches(emailPattern))) {
            tv_Email.setError("Please Enter Valid Email");
            tv_Email.requestFocus();
        } else if (UserName.isEmpty()) {
            tv_UserName.setError("Please Enter UserName");
            tv_UserName.requestFocus();
        } else if (UserName.length() > 15) {
            tv_UserName.setError("UserName Length must be smaller then 15");
            tv_UserName.requestFocus();
        } else if (UserName.length() < 3) {
            tv_UserName.setError("Length must be greater then 3");
            tv_UserName.requestFocus();
        } else if (Password.isEmpty()) {
            tv_Password.setError("Password is Empty");
            tv_Password.requestFocus();
        } else if (Password.length() > 15) {
            tv_Password.setError("Password Length must be smaller then 15");
            tv_Password.requestFocus();
        } else if (Password.length() < 8) {
            tv_Password.setError("Password Length must be greater then 8");
            tv_Password.requestFocus();
        } else if (MobileNumber.isEmpty()) {
            tv_MobileNumber.setError("Please Enter Mobile Number");
            tv_MobileNumber.requestFocus();
        } else if (MobileNumber.length() != 11) {
            tv_MobileNumber.setError("Please enter valid Number");
            tv_MobileNumber.requestFocus();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(Signup.this);
            progressDialog.setMessage("Please Wait!"); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);


            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
               public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild("ParentData/"+MobileNumber)) {
                        progressDialog.dismiss();

                        tv_MobileNumber.setError("Already Exist");
                        tv_MobileNumber.requestFocus();

                    }

                    else {
                        progressDialog.dismiss();

                        sendVerificationCode(phoneNumber);

                      /*  mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(Email, Password)
                                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {

                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {

                                            RegisterUser();

                                        } else {
                                            Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                        }


                                    }
                                });
*/
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });


        }


    }


    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCall
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCall = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(getApplicationContext(), "Invalid Number", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String vId, PhoneAuthProvider.ForceResendingToken token) {
            verificationId = vId;
            callDialog();
            Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_SHORT).show();


        }
    };


    public void callDialog() {
        LayoutInflater li = LayoutInflater.from(Signup.this);
        View promptsView = li.inflate(R.layout.activity_otp, null);
        final Button btn_cancle = (Button) promptsView.findViewById(R.id.btn_cancle);
        final Button btn_verify = (Button) promptsView.findViewById(R.id.btn_verify);
        final EditText et_otp = (EditText) promptsView.findViewById(R.id.et_otp);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Signup.this);
        alertDialogBuilder.setView(promptsView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);


        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String otp = et_otp.getText().toString();

                if (!otp.isEmpty()) {

                    verifyOtp(verificationId, otp);
                } else {
                    et_otp.setError("Enter Code!");
                }


            }
        });


    }

    public void verifyOtp(String v, String otp) {
        progressVerify = new ProgressDialog(Signup.this);
        progressVerify.setMessage("Please Wait!"); // Setting Message
        progressVerify.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressVerify.show(); // Display Progress Dialog
        progressVerify.setCancelable(false);


        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(v, otp);
        signInWithPhoneAuthCredential(credential);


    }


    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            mAuth.signOut();
                            progressVerify.dismiss();
                            alertDialog.dismiss();
                            mAuth = FirebaseAuth.getInstance();
                            mAuth.createUserWithEmailAndPassword(Email, Password)
                                    .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {

                                                RegisterUser();

                                            } else {
                                                Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                            }


                                        }
                                    });


                        } else {
                            progressVerify.dismiss();
                            alertDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Invalid Code ,Try Again", Toast.LENGTH_LONG).show();

                        }
                    }

                });
    }




    public void RegisterUser() {

        InitializeUi();
        Email = tv_Email.getText().toString();
        UserName = tv_UserName.getText().toString();
        Password = tv_Password.getText().toString();
        MobileNumber = tv_MobileNumber.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(Signup.this);
        progressDialog.setMessage("Please Wait!"); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        RegisterUser registerUser = new RegisterUser(UserName, Password, Email);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("ParentData").child(MobileNumber).setValue(registerUser).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CreateObjectsInDatabase();
                    Toast.makeText(Signup.this, "Registered Successfully,Now you Can Sign In", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    finish();
                 //   Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                   // startActivity(intent);
                } else {
                    Toast.makeText(Signup.this, "Registration Failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }


            }
        });


    }

    public  void CreateObjectsInDatabase(){
        MobileNumber = tv_MobileNumber.getText().toString();
        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();
        database2.child("MessageReport").child(MobileNumber).setValue("");
        DatabaseReference database3 = FirebaseDatabase.getInstance().getReference();
        database3.child("CallReport").child(MobileNumber).setValue("");

    }

}
//