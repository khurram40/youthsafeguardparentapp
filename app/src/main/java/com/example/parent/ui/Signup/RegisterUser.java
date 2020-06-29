package com.example.parent.ui.Signup;

import android.provider.ContactsContract;

public class RegisterUser {

    public String UserName,Password,Email;

    public RegisterUser(){

    }


    public  RegisterUser(String UserName,String  Password,String Email){
       this.UserName=UserName;
       this.Password=Password;
       this.Email= Email;
    }


}
