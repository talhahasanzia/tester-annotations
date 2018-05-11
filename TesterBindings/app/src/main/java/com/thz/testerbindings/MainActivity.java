package com.thz.testerbindings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thz.annotationdemo.MainActivityExposed;
import com.thz.testablebinding.Expose;

@Expose
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Presenter presenter = new Presenter();
        
    }


}
