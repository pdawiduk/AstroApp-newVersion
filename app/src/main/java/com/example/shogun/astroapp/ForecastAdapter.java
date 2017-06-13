package com.example.shogun.astroapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shogun.astroapp.Database.WeatherEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shogun on 09.06.2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ConsultationHolder> {

    List<WeatherEntity> entityList;


    public ForecastAdapter(List<WeatherEntity> entityList) {
        this.entityList = entityList;

    }

    @Override
    public ConsultationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_item, parent, false);
        return new ConsultationHolder(view);

    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    @Override
    public void onBindViewHolder(ConsultationHolder holder, int position) {
        if (!entityList.isEmpty()) {
            WeatherEntity weatherEntity = entityList.get(position);

            holder.tvMaxTemp.setText(Double.toString(weatherEntity.getMaxTemp()));
            holder.tvMinTemp.setText(Double.toString(weatherEntity.getMinTemp()));
            holder.tvWeatherDescription.setText(weatherEntity.getDescription());
            int weatherId = (int) weatherEntity.getWeatherId();
            holder.ivWeatherIcon.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));
            holder.tvDate.setText(Utility.convertMiliToDate(weatherEntity.getDt()));
        }
    }

    class ConsultationHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvMaxTemp)
        TextView tvMaxTemp;
        @BindView(R.id.tvMinTemp)
        TextView tvMinTemp;
        @BindView(R.id.tvWeatherDescription)
        TextView tvWeatherDescription;
        @BindView(R.id.ivWeatherIcon)
        ImageView ivWeatherIcon;

        @BindView(R.id.tvDate)
        TextView tvDate;

        public ConsultationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
