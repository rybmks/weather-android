package com.example.weather.views.MainActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.models.DailyForecast;
import com.example.weather.utils.WeatherIconMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for the forecast RecyclerView.
 * <p>
 * This class follows the standard Android RecyclerView Adapter pattern.
 * It manages the collection of {@link DailyForecast} items and binds them to the views
 * defined in {@code item_forecast.xml}.
 * </p>
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private final List<DailyForecast> items = new ArrayList<>();


    public void setItems(List<DailyForecast> forecast) {
        items.clear();
        if (forecast != null) {
            items.addAll(forecast);
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ForecastViewHolder holder,
            int position
    ) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class ForecastViewHolder extends RecyclerView.ViewHolder {

        private final TextView textDay;
        private final TextView textTemp;
        private final TextView textDesc;
        private final ImageView imageIcon;

        ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            textDay = itemView.findViewById(R.id.textDay);
            textTemp = itemView.findViewById(R.id.textTemp);
            textDesc = itemView.findViewById(R.id.textDesc);
            imageIcon = itemView.findViewById(R.id.imageForecastIcon);
        }

        void bind(DailyForecast item) {
            textDay.setText(item.day);
            textTemp.setText(Math.round(item.temperature) + "°");
            textDesc.setText(item.description);
            int iconRes = WeatherIconMapper.getIcon(item.weatherMain);
            imageIcon.setImageResource(iconRes);
        }
    }
}
