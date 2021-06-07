package com.example.amir.scancer.ui.reg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.amir.scancer.MainActivity;
import com.example.amir.scancer.R;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment implements View.OnClickListener {

    View view;

    EditText email;
    EditText password;
    Button login;
    TextView textView;

    String emaildata;
    String pass;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_login, container, false);




        email = (EditText) view.findViewById(R.id.editTextEmail);
        password = (EditText) view.findViewById(R.id.editTextPassword);
        login = (Button) view.findViewById(R.id.cirLoginButton);
        textView = (TextView) view.findViewById(R.id.lets_reg);

        login.setOnClickListener(this);
        //textView.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cirLoginButton :{

                //Toast.makeText(getActivity(),"Text!",Toast.LENGTH_LONG).show();

                emaildata = email.getText().toString();
                pass = password.getText().toString();




                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("email" , emaildata);
                intent.putExtra("password" , pass);


                startActivity(intent);
                getActivity().finish();
                break;
            }





        }

    }
}
