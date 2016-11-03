package com.app.rolltext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.rolltext.view.RollTextView;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.number)
    EditText number;
    @Bind(R.id.high_fast)
    Button highFast;
    @Bind(R.id.high_slow)
    Button highSlow;
    @Bind(R.id.custom)
    Button custom;
    @Bind(R.id.all)
    Button all;
    @Bind(R.id.rollText)
    RollTextView rollText;

    private int[] mDeviation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.high_fast, R.id.high_slow, R.id.custom, R.id.all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.high_fast:
                rollText.setText(number.getText().toString())
                        .setDeviation(RollTextView.HIGH_FAST)
                        .start();
                break;
            case R.id.high_slow:
                rollText.setText(number.getText().toString())
                        .setDeviation(RollTextView.HIGH_SLOW)
                        .start();
                break;
            case R.id.custom:
                mDeviation = new int[number.getText().toString().length()];
                for (int i = 0; i < number.getText().toString().length(); i++) {
                    mDeviation[i] = new Random().nextInt(20 - 10) + 10;
                }
                rollText.setText(number.getText().toString())
                        .setMaxLine(20)
                        .setDeviation(mDeviation)
                        .start();
                break;
            case R.id.all:
                rollText.setText(number.getText().toString())
                        .setDeviation(RollTextView.ALL)
                        .start();
                break;
        }
    }

}
