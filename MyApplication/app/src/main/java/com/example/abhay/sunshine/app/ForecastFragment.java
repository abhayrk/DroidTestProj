package com.example.abhay.sunshine.app;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        String[] forecast={
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/30",
                "Wed - Rainy - 68/52",
                "Thu - Sunny - 88/44",
                "Fri - Cloudy - 88/56",
                "Sat - Meteorites - 88/89",
                "Sun - Asteroids - 88/66"};
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecast));

        ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,weekForecast);
        //int tempid = R.id.listView_forecast;
        //System.out.println("R.id.listView_forecast is " + tempid);
        ListView forecastView = (ListView) rootview.findViewById(R.id.listView_forecast);
        System.out.println("Forecast View is " + forecastView);
        forecastView.setAdapter(forecastAdapter);

        String urlstring="http://api.openweathermap.org/data/2.5/forecast/daily?id=1263780&appid=57fcb1d19ea118227a096d1958d6d84a";
        try {
            new FetchWeatherData().execute(urlstring);
        }catch(Exception e){
            Log.e("FetchDataException","The exception is " + e);
        }

        return rootview;
    }

    public class FetchWeatherData extends AsyncTask<String, Void, Void>{

        private final String LOG_TAG = FetchWeatherData.class.getSimpleName();
        @Override
        protected Void doInBackground(String... urls) {
            Log.i(LOG_TAG,"String url is" + urls[0]);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonresp = null;

            try
            {
                //setting url and establishing connection
                Log.i(LOG_TAG,"Forming URL Connection and connecting");
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                Log.i(LOG_TAG, "Reading input stream from connection");
                //reading response into string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null)
                {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                Log.i(LOG_TAG,"Reading from InputStream --> InputStreamReader --> BufferReader --> StringBuffer");
                while((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0)
                {
                    return null;
                }

                jsonresp = buffer.toString();
            }
            catch(IOException e)
            {
                Log.e("MainActivityFragment","Nothing to return from Weathermap!!" + e);
                return null;
            }
            finally {
                if(urlConnection != null ){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch(IOException e){
                        Log.e("PlaceholderFragment","Reader not closing!!" + e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.i("FetchWeatherData","Executing on pre-execute");
        }
    }
}
