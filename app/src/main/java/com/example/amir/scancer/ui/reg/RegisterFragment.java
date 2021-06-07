package com.example.amir.scancer.ui.reg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.amir.scancer.MainActivity;
import com.example.amir.scancer.R;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    View view;

    EditText email;
    EditText password;
    EditText mobile;
    EditText name;

    String emailData;
    String pass;
    String phone;
    String profileName;

    Button register;
    TextView textView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_register, container, false);

        name =(EditText) view.findViewById(R.id.name);
        email =(EditText) view.findViewById(R.id.email);
        mobile =(EditText) view.findViewById(R.id.mobile);
        password =(EditText) view.findViewById(R.id.password);

        register = (Button) view.findViewById(R.id.register);

        textView = (TextView) view.findViewById(R.id.already);

        register.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:{
                emailData = email.getText().toString();
                pass = password.getText().toString();
                phone = mobile.getText().toString();
                profileName = name.getText().toString();

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("email" , emailData);
                intent.putExtra("password" , pass);
                intent.putExtra("mobile" , phone);
                intent.putExtra("name" , profileName);


                startActivity(intent);
                getActivity().finish();


                break;
            }


        }

    }
}
