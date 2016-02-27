package com.BBQube.solo.jiuroudaguan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by jacobs2 on 1/30/16.
 */
public class TemperatureDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener {
    public static int MIN_TEMPERATURE = 200;
    public static int MAX_TEMPERATURE = 500;
    private static int INCREMENTAL_STEP = 10;

    int progressChanged = MIN_TEMPERATURE;

    private TextView txtTemperatureValue;

    private SeekBar  seekTemperature;

    private TextView btnSet;
    private TextView btnCancel;

    private static TemperatureDialogListener listener;
    public TemperatureDialog() {
    }

    public static TemperatureDialog newInstance(String title, MainActivityFragment fragment) {
        listener = fragment;
        TemperatureDialog frag = new TemperatureDialog();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_temp, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTemperatureValue = (TextView) view.findViewById(R.id.txtSelectedTempValue);
        // display the unit F
        ((TextView)view.findViewById(R.id.txtSelectedTempValueType)).setText((char) 0x00B0 + "F");
        seekTemperature = (SeekBar) view.findViewById(R.id.seekTemp);

        btnSet =(TextView)view.findViewById(R.id.btnSet);
        btnCancel =(TextView)view.findViewById(R.id.btnCancel);
        // [Ian] set max to be MAX-MIN to compensate the new range from MIN to MIN + MAX after I modify the onProgressChanged method
        seekTemperature.setMax(MAX_TEMPERATURE-MIN_TEMPERATURE);
        seekTemperature.incrementProgressBy(INCREMENTAL_STEP);
        seekTemperature.setOnSeekBarChangeListener(this);
        txtTemperatureValue.setText( "" + MIN_TEMPERATURE);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSetTemperatureDialog(txtTemperatureValue.getText() + "");
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        /*
        if(progress <= MIN_TEMPERATURE){
            progress = MIN_TEMPERATURE + progress;
        }*/
        // to shift the starting point to MIN_TEMPERATURE
        progressChanged = progress + MIN_TEMPERATURE;
        // normalize according to incremental steps
        progressChanged = progressChanged/INCREMENTAL_STEP;
        progressChanged = progressChanged*INCREMENTAL_STEP;

        txtTemperatureValue.setText("" + progressChanged );

    }

    public void onStartTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub

    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    public interface TemperatureDialogListener {
        void onSetTemperatureDialog(String temp);
    }



}
