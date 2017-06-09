package com.example.shogun.astroapp;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.example.shogun.astroapp.Utility.getTime;

public class MoonFragment extends Fragment {

    @BindView(R.id.tvMoonRise)
    TextView tvMoonRise;
    @BindView(R.id.tvMoonSet)
    TextView tvMoonSet;
    @BindView(R.id.tvNewMoon)
    TextView tvNewMoon;
    @BindView(R.id.tvFullMoon)
    TextView tvFullMon;
    @BindView(R.id.tvPhaseOfMoon)
    TextView tvPhaseOfMoon;
    @BindView(R.id.tvSynodicDay)
    TextView tvSynodicDay;

    private static final int MINUTE = 1000*60;

    private static MoonFragment fragment;
    private Timer autoUpdate;

    public MoonFragment() {
        // Required empty public constructor
    }

    public static MoonFragment getInstance() {
        return fragment;
    }

    public static MoonFragment newInstance() {
        fragment = new MoonFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_moon, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int refreshtime= Utility.getREfreshTIme(getContext())*MINUTE;
        if (getView() != null) {
            autoUpdate = new Timer();
            autoUpdate.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (this == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            update();
                        }
                    });
                }
            }, 0,refreshtime );
        }

    }

    public void update() {
        AstroCalculator.MoonInfo moonInfo = Utility.getAstroCalculator().getMoonInfo();
        Log.d(TAG, "update: value of MOon Calculator MoonRise = " + moonInfo.getMoonrise());
        try {
            tvMoonRise.setText(moonInfo.getMoonrise().toString());
            tvMoonSet.setText(moonInfo.getMoonset().toString());
            tvPhaseOfMoon.setText(String.valueOf(moonInfo.getIllumination()));
            tvFullMon.setText(moonInfo.getNextFullMoon().toString());
            tvNewMoon.setText(moonInfo.getNextNewMoon().toString());
            tvSynodicDay.setText(String.valueOf(moonInfo.getAge()));
        } catch (Exception ex) {
            Snackbar.make(getView(), "lokacja nie poprawna", Snackbar.LENGTH_LONG).show();
        }
    }

}
