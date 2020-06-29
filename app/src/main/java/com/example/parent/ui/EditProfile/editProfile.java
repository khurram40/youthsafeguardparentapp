package com.example.parent.ui.EditProfile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.parent.R;
import com.example.parent.ui.UserData;
import com.example.parent.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class editProfile extends Fragment {
    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail, btnChangeUserName,
            changeEmail, changePassword, sendEmail, changeUserName, signOut;
    private EditText oldEmail, newEmail, password, newPassword,newUsername;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
   public String emailPattern;
    private FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final  View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        btnChangeEmail = (Button)view.findViewById(R.id.change_email_button);
        btnChangePassword = (Button)view.findViewById(R.id.change_password_button);
        btnSendResetEmail = (Button)view.findViewById(R.id.sending_pass_reset_button);
        btnChangeUserName = (Button)view.findViewById(R.id.change_username_button);
        changeEmail = (Button)view.findViewById(R.id.changeEmail);
        changePassword = (Button)view.findViewById(R.id.changePass);
        sendEmail = (Button)view.findViewById(R.id.send);
        changeUserName = (Button)view.findViewById(R.id.changeUserName);
        signOut = (Button)view.findViewById(R.id.sign_out);

        oldEmail = (EditText)view.findViewById(R.id.old_email);
        newEmail = (EditText)view.findViewById(R.id.new_email);
        password = (EditText)view.findViewById(R.id.password);
        newPassword = (EditText)view.findViewById(R.id.newPassword);
        newUsername = (EditText)view.findViewById(R.id.newUsername);
        newUsername.setVisibility(View.GONE);
        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        changeUserName.setVisibility(View.GONE);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        btnChangeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUsername.setVisibility(View.VISIBLE);
                changeUserName.setVisibility(View.VISIBLE);
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.GONE);


            }
        });

        changeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateUserName();
            }
        });

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.GONE);
                changeUserName.setVisibility(View.GONE);
                newUsername.setVisibility(View.GONE);

            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           updateEmail();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.VISIBLE);
                sendEmail.setVisibility(View.GONE);
                changeUserName.setVisibility(View.GONE);
                newUsername.setVisibility(View.GONE);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           updatePassword();
            }
        });



        btnSendResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail.setVisibility(View.VISIBLE);
                oldEmail.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.GONE);
                changeUserName.setVisibility(View.GONE);
                newUsername.setVisibility(View.GONE);
            }
        });
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             resetPasswordEmail();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();

            }
        });




        return view;
    }


    public void signOut() {
        auth.signOut();
        getActivity().finish();
        Intent intent=new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    public void updateUserName(){
        String newusername=newUsername.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        if (!(newusername.isEmpty()) && !(newusername.length() > 15) && !(newusername.length() < 3) )
        {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref1 = database.getReference("ParentData/"+ UserData.getMobileNumber()+"/");
            ref1.child("UserName").setValue(newusername);
            Toast.makeText(getContext(), "UserName is updated Successfully!", Toast.LENGTH_LONG).show();
            newUsername.setVisibility(View.GONE);
            changeUserName.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

        }

        else if (newusername.isEmpty())
        {
            newUsername.setError("Enter New UserName");
            progressBar.setVisibility(View.GONE);


        }
        else if (newusername.length() > 15)
        {
            newUsername.setError("UserName Length must be smaller then 15");
            progressBar.setVisibility(View.GONE);

        }
        else if (newusername.length() < 3)
        {
            newUsername.setError("Length must be greater then 3");
            progressBar.setVisibility(View.GONE);

        }


    }

    public void updatePassword(){
        progressBar.setVisibility(View.VISIBLE);
        final String oldpassword = password.getText().toString().trim();
        final String newpassword = newPassword.getText().toString().trim();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldpassword);

        if (!newpassword.isEmpty()&& !oldpassword.isEmpty()) {
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (!newpassword.isEmpty() && !(newpassword.length() > 15) && !(newpassword.length() < 8))
                        {
                            user.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(getContext(), "Password is updated. Please sign in with new password!", Toast.LENGTH_LONG).show();
                                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference ref1 = database.getReference("ParentData/"+ UserData.getMobileNumber()+"/");
                                        ref1.child("Password").setValue(newpassword);
                                        signOut();
                                        progressBar.setVisibility(View.GONE);


                                    }
                                    else {
                                        Toast.makeText(getContext(), "Failed to update Password ! Try Again", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                    }


                                }
                            });

                        } else if (newpassword.isEmpty()) {
                            newEmail.setError("Enter New Password");
                            progressBar.setVisibility(View.GONE);
                        } else if (newpassword.length() > 15) {
                            newEmail.setError("Password Length must be smaller then 15");
                            progressBar.setVisibility(View.GONE);
                        } else if (newpassword.length() < 8) {
                            newEmail.setError("Password Length must be greater then 8");
                            progressBar.setVisibility(View.GONE);
                        }


                    }
                    else {
                        password.setError("Old password is not valid");
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });

        }
        else if (newpassword.isEmpty()){
            newPassword.setError("Enter New Password");
            progressBar.setVisibility(View.GONE);

        }
        else if (oldpassword.isEmpty()){
            password.setError("Enter Old Password");
            progressBar.setVisibility(View.GONE);

        }




    }

    public void updateEmail(){
        final String newemail = newEmail.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        if ((newemail).matches(emailPattern) &&user != null &&(user.getEmail()).equals(oldEmail.getText().toString().trim()) && !newemail.equals("")) {
            user.updateEmail(newemail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Email address is updated. Please sign in with new email!", Toast.LENGTH_LONG).show();
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref1 = database.getReference("ParentData/"+ UserData.getMobileNumber()+"/");
                                ref1.child("Email").setValue(newemail);
                                signOut();

                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getContext(), "Failed to update email ! Try Again", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
        else if (newEmail.getText().toString().trim().equals("")) {
            newEmail.setError("Enter email");
            progressBar.setVisibility(View.GONE);
        }
        else if (!(newEmail.getText().toString().trim()).equals(oldEmail)) {
            oldEmail.setError("Old email is not valid");
            progressBar.setVisibility(View.GONE);
        }
        else if (!(newEmail.getText().toString().trim()).matches(emailPattern)) {
            newEmail.setError("New email is not valid");
            progressBar.setVisibility(View.GONE);
        }



    }

    public void resetPasswordEmail(){
        progressBar.setVisibility(View.VISIBLE);
        if (!oldEmail.getText().toString().trim().equals("") && (oldEmail.getText().toString().trim()).equals(user.getEmail()) ) {
            auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Reset password. email is sent!", Toast.LENGTH_SHORT).show();
                                sendEmail.setVisibility(View.GONE);
                                oldEmail.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        } else if (oldEmail.getText().toString().trim().equals("")){
            oldEmail.setError("Enter email");
            progressBar.setVisibility(View.GONE);
        }
        else if (!(oldEmail.getText().toString().trim()).equals(user.getEmail())){
            oldEmail.setError("Enter Valid email");
            progressBar.setVisibility(View.GONE);
        }


    }

}
