package com.example.shogun.astroapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.shogun.astroapp.Utility.*;


public class SunFragment extends Fragment {


    @BindView(R.id.tvSunRise)
    TextView tvSunRise;
    @BindView(R.id.tvSunSet)
    TextView tvSunSet;
    @BindView(R.id.tvDusk)
    TextView tvDusk;
    @BindView(R.id.tvDawn)
    TextView tvDawn;

    static SunFragment fragment;
    private Timer autoUpdate;
    private static final int minute= 1000*60;

    private static SunFragment getInstance() {
        return fragment;
    }

    public SunFragment() {
        // Required empty public constructor
    }


    public static SunFragment newInstance() {
        fragment = new SunFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sun, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void update() {
        AstroCalculator.SunInfo sunInfo = Utility.getAstroCalculator().getSunInfo();
        tvSunSet.setText(sunInfo.getSunset().toString());
        tvSunRise.setText(sunInfo.getSunrise().toString());
        tvDawn.setText(sunInfo.getTwilightMorning().toString());
        tvDusk.setText(sunInfo.getTwilightEvening().toString());

    }

    @Override
    public void onResume() {
        super.onResume();
        int refreshTIme= Utility.getREfreshTIme(getContext()) * minute;
        if (getView() != null) {
            autoUpdate = new Timer();
            autoUpdate.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (this == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            update();
                            Toast.makeText(getContext(),"update",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 0, refreshTIme);
        }

    }
}

