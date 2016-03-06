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
    public static String NORMAL_MODE = "000";
    public static String SOUS_VIDE_MODE = "666";
    public static String ELECTRICAL_SMOKE_MODE = "555";

    public static int MIN_TEMPERATURE;
    public static int MAX_TEMPERATURE;
    public static int INCREMENTAL_STEP;

    public static int progressChanged = MIN_TEMPERATURE;

    public static String MODE_CODE;
    public static String TITLE_MESSAGE;

    private TextView txtTemperatureValue;
    private TextView txtView_MinTemp;
    private TextView txtView_MaxTemp;
    private TextView txtView_DialogTitle;

    private SeekBar  seekTemperature;

    private TextView btnSet;
    private TextView btnCancel;

    private static TemperatureDialogListener listener;
    public TemperatureDialog() {
    }

    // [Ian] added four more parameters, min_Temp, max_Temp, incrementalStep, twoMessageMode,
    public static TemperatureDialog newInstance(String TitleMessage, MainActivityFragment fragment, int MinTemp, int MaxTemp, int IncrementalStep, String ModeCode) {
        listener = fragment;
        MIN_TEMPERATURE = MinTemp;
        MAX_TEMPERATURE = MaxTemp;
        INCREMENTAL_STEP = IncrementalStep;
        MODE_CODE = ModeCode;
        TITLE_MESSAGE = TitleMessage;

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
        txtView_MinTemp = (TextView) view.findViewById(R.id.TextView_minTemp);
        txtView_MaxTemp = (TextView) view.findViewById(R.id.TextView_maxTemp);
        txtView_DialogTitle = (TextView) view.findViewById(R.id.txtTitle);

        // display the unit F
        ((TextView)view.findViewById(R.id.txtSelectedTempValueType)).setText((char) 0x00B0 + "F");
        seekTemperature = (SeekBar) view.findViewById(R.id.seekTemp);

        btnSet =(TextView)view.findViewById(R.id.btnSet);
        btnCancel =(TextView)view.findViewById(R.id.btnCancel);
        // [Ian] set max to be MAX-MIN to compensate the new range from MIN to MIN + MAX after I modify the onProgressChanged method
        seekTemperature.setMax(MAX_TEMPERATURE-MIN_TEMPERATURE);
        seekTemperature.incrementProgressBy(INCREMENTAL_STEP);
        seekTemperature.setOnSeekBarChangeListener(this);
        txtTemperatureValue.setText("" + MIN_TEMPERATURE);
        txtView_MinTemp.setText("" + MIN_TEMPERATURE);
        txtView_MaxTemp.setText("" + MAX_TEMPERATURE);
        txtView_DialogTitle.setText(TITLE_MESSAGE);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // [Ian] depending on whether we are using TwoMessageMode or not, we may send one message or two messages
                if (MODE_CODE.equalsIgnoreCase(NORMAL_MODE)){
                    // normal mode
                    listener.onSetTemperatureDialog(txtTemperatureValue.getText() + "");
                } else if (MODE_CODE.equalsIgnoreCase(SOUS_VIDE_MODE)){
                    listener.onSetTemperatureDialog(SOUS_VIDE_MODE + "," + txtTemperatureValue.getText());
                } else if (MODE_CODE.equalsIgnoreCase(ELECTRICAL_SMOKE_MODE)){
                    listener.onSetTemperatureDialog(ELECTRICAL_SMOKE_MODE + "," + txtTemperatureValue.getText());
                }
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
