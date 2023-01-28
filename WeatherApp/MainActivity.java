package com.example.weatherproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    URL mainURL;
    //static JSONObject jsonObject = new JSONObject();
    static final String API_KEY = "4c1761d4a5fde351ec772354ad3a7257";
    int numTowns;
    double lat, lon;
    String latString, lonString;
    EditText latitudeUserInput, longitudeUserInput;
    Button getWeatherButton;
    TextView city1NameTextView, city1TemperatureTextView, city1DateTextView, city1TimeTextView, city1DescriptionTextView, city2NameTextView, city2TemperatureTextView, city2DateTextView, city2TimeTextView, city2DescriptionTextView, city3NameTextView, city3TemperatureTextView, city3DateTextView, city3TimeTextView, city3DescriptionTextView;
    ImageView city1ImageView, city2ImageView, city3ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeUserInput = findViewById(R.id.id_editTextLatitudeUserInput);
        longitudeUserInput = findViewById(R.id.id_editTextLongitudeUserInput);
        getWeatherButton = findViewById(R.id.id_getWeatherButton);
        city1NameTextView = findViewById(R.id.id_city1NameTextView);
        city1TemperatureTextView = findViewById(R.id.id_city1TemperatureTextView);
        city1ImageView = findViewById(R.id.id_city1ImageView);
        city1DateTextView = findViewById(R.id.id_city1DateTextView);
        city1TimeTextView = findViewById(R.id.id_city1TimeTextView);
        city1DescriptionTextView = findViewById(R.id.id_city1DescriptionTextView);
        city2NameTextView = findViewById(R.id.id_city2NameTextView);
        city2TemperatureTextView = findViewById(R.id.id_city2TemperatureTextView);
        city2ImageView = findViewById(R.id.id_city2ImageView);
        city2DateTextView = findViewById(R.id.id_city2DateTextView);
        city2TimeTextView = findViewById(R.id.id_city2TimeTextView);
        city2DescriptionTextView = findViewById(R.id.id_city2DescriptionTextView);
        city3NameTextView = findViewById(R.id.id_city3NameTextView);
        city3TemperatureTextView = findViewById(R.id.id_city3TemperatureTextView);
        city3ImageView = findViewById(R.id.id_city3ImageView);
        city3DateTextView = findViewById(R.id.id_city3DateTextView);
        city3TimeTextView = findViewById(R.id.id_city3TimeTextView);
        city3DescriptionTextView = findViewById(R.id.id_city3DescriptionTextView);

        city1ImageView.setImageResource(R.drawable.emptyweatherappplaceholder);
        city2ImageView.setImageResource(R.drawable.emptyweatherappplaceholder);
        city3ImageView.setImageResource(R.drawable.emptyweatherappplaceholder);

        latitudeUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                latString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        longitudeUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lonString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentWeatherTask task = new getCurrentWeatherTask();
                task.execute(latString, lonString);
            }
        });
    }

    private class getCurrentWeatherTask extends AsyncTask<String, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonObject = null;
            try {
                //lat (SBHS) = 40.37425732388781;
                //lon (SBHS) = -74.56398991571521;
                //lat (Portland) = 43.65412178142164;
                //lon (Portland) = -70.2625497275448;
                lat = Double.parseDouble(strings[0]);
                lon = Double.parseDouble(strings[1]);
                numTowns = 3;
                //https://api.openweathermap.org/data/2.5/find?lat=40.37425732388781&lon=-74.56398991571521&cnt=3&appid=4c1761d4a5fde351ec772354ad3a7257
                mainURL = new URL("https://api.openweathermap.org/data/2.5/find?lat=" + lat + "&lon=" + lon + "&cnt=" + numTowns + "&appid=" + API_KEY);
            } catch (MalformedURLException e) {
                Log.d("TAG_1", "URL Error");
                e.printStackTrace();
            }
            try {
                URLConnection connection = mainURL.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp = inputBufferedReader.readLine();
                jsonObject = new JSONObject(temp);
                Log.d("TAG_1", jsonObject.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            //getting image bitmaps
            URL city1WeatherImageURL = null;
            try {
                city1WeatherImageURL = new URL("https://openweathermap.org/img/wn/" + jsonObject.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).get("icon") + "@2x.png");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                URLConnection connectionCity1 = city1WeatherImageURL.openConnection();
                InputStream city1IN = connectionCity1.getInputStream();
                Bitmap city1ImageIcon = BitmapFactory.decodeStream(city1IN);
                city1ImageView.setImageBitmap(city1ImageIcon);
            }catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG_ERROR", "ERROR Bitmap1");
            }
            URL city2WeatherImageURL = null;
            try {
                city2WeatherImageURL = new URL("https://openweathermap.org/img/wn/" + jsonObject.getJSONArray("list").getJSONObject(1).getJSONArray("weather").getJSONObject(0).get("icon") + "@2x.png");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                URLConnection connectionCity2 = city2WeatherImageURL.openConnection();
                InputStream city2IN = connectionCity2.getInputStream();
                Bitmap city2ImageIcon = BitmapFactory.decodeStream(city2IN);
                city2ImageView.setImageBitmap(city2ImageIcon);
            }catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG_ERROR", "ERROR Bitmap2");
            }
            URL city3WeatherImageURL = null;
            try {
                city3WeatherImageURL = new URL("https://openweathermap.org/img/wn/" + jsonObject.getJSONArray("list").getJSONObject(2).getJSONArray("weather").getJSONObject(0).get("icon") + "@2x.png");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                URLConnection connectionCity3 = city3WeatherImageURL.openConnection();
                InputStream city3IN = connectionCity3.getInputStream();
                Bitmap city3ImageIcon = BitmapFactory.decodeStream(city3IN);
                city3ImageView.setImageBitmap(city3ImageIcon);
            }catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG_ERROR", "ERROR Bitmap3");
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                //getting city names
                String city1Name = (String) jsonObject.getJSONArray("list").getJSONObject(0).get("name");
                Log.d("TAG_1", "City 1 Name: " + city1Name);
                String city2Name = (String) jsonObject.getJSONArray("list").getJSONObject(1).get("name");
                Log.d("TAG_1", "City 2 Name: " + city2Name);
                String city3Name = (String) jsonObject.getJSONArray("list").getJSONObject(2).get("name");
                Log.d("TAG_1", "City 3 Name: " + city3Name);
                city1NameTextView.setText(city1Name);
                city2NameTextView.setText(city2Name);
                city3NameTextView.setText(city3Name);

                //getting temperature
                double city1TempKelvin = (double) jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").get("temp");
                double city1TempFahrenheit = (Math.round((((city1TempKelvin - 273.15) * (9.0/5.0)) + 32)*10))/10.0;
                String city1TempFahrenheitString = city1TempFahrenheit + "°F";
                Log.d("TAG_1", "City 1 Temp: " + city1TempFahrenheitString);
                double city2TempKelvin = (double) jsonObject.getJSONArray("list").getJSONObject(1).getJSONObject("main").get("temp");
                double city2TempFahrenheit = (Math.round((((city2TempKelvin - 273.15) * (9.0/5.0)) + 32)*10))/10.0;
                String city2TempFahrenheitString = city2TempFahrenheit + "°F";
                Log.d("TAG_1", "City 2 Temp: " + city2TempFahrenheitString);
                double city3TempKelvin = (double) jsonObject.getJSONArray("list").getJSONObject(2).getJSONObject("main").get("temp");
                double city3TempFahrenheit = (Math.round((((city3TempKelvin - 273.15) * (9.0/5.0)) + 32)*10))/10.0;
                String city3TempFahrenheitString = city3TempFahrenheit + "°F";
                Log.d("TAG_1", "City 3 Temp: " + city3TempFahrenheitString);
                city1TemperatureTextView.setText(city1TempFahrenheitString);
                city2TemperatureTextView.setText(city2TempFahrenheitString);
                city3TemperatureTextView.setText(city3TempFahrenheitString);

                //getting date object for all three cities
                //City 1
                int epochTimeCity1Int = (int) jsonObject.getJSONArray("list").getJSONObject(0).get("dt");
                Long epochTimeCity1 = (long) epochTimeCity1Int;
                Log.d("TAG_1", "City 1 Epoch Time: " + epochTimeCity1);
                Date dateCity1 = new Date(epochTimeCity1*1000L);
                SimpleDateFormat simpleDateFormatCity1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                simpleDateFormatCity1.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                String dateTextCity1 = simpleDateFormatCity1.format(dateCity1);
                Log.d("TAG_1", "City 1 Full Date: " + dateTextCity1);
                //City 2
                int epochTimeCity2Int = (int) jsonObject.getJSONArray("list").getJSONObject(1).get("dt");
                Long epochTimeCity2 = (long) epochTimeCity2Int;
                Log.d("TAG_1", "City 2 Epoch Time: " + epochTimeCity2);
                Date dateCity2 = new Date(epochTimeCity2*1000L);
                SimpleDateFormat simpleDateFormatCity2 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                simpleDateFormatCity2.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                String dateTextCity2 = simpleDateFormatCity2.format(dateCity2);
                Log.d("TAG_1", "City 2 Full Date: " + dateTextCity2);
                //City 3
                int epochTimeCity3Int = (int) jsonObject.getJSONArray("list").getJSONObject(2).get("dt");
                Long epochTimeCity3 = (long) epochTimeCity3Int;
                Log.d("TAG_1", "City 3 Epoch Time: " + epochTimeCity3);
                Date dateCity3 = new Date(epochTimeCity3*1000L);
                SimpleDateFormat simpleDateFormatCity3 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                simpleDateFormatCity3.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                String dateTextCity3 = simpleDateFormatCity3.format(dateCity3);
                Log.d("TAG_1", "City 3 Full Date: " + dateTextCity3);

                //Separating city dates and times
                String city1DateSeparate = dateTextCity1.substring(0, dateTextCity1.indexOf(" "));
                String city1TimeSeparate = dateTextCity1.substring(dateTextCity1.indexOf(" ") + 1);
                if(Integer.parseInt(city1TimeSeparate.substring(0,2)) >= 12 && Integer.parseInt(city1TimeSeparate.substring(0,2)) < 24)
                    city1TimeSeparate += " PM";
                else
                    city1TimeSeparate += " AM";
                if(Integer.parseInt(city1TimeSeparate.substring(0,2)) > 12)
                    city1TimeSeparate = (Integer.parseInt(city1TimeSeparate.substring(0, 2)) - 12) + city1TimeSeparate.substring(2);
                Log.d("TAG_1", "City 1 Date: " + city1DateSeparate);
                Log.d("TAG_1", "City 1 Time: " + city1TimeSeparate);
                String city2DateSeparate = dateTextCity2.substring(0, dateTextCity2.indexOf(" "));
                String city2TimeSeparate = dateTextCity2.substring(dateTextCity2.indexOf(" ") + 1);
                if(Integer.parseInt(city2TimeSeparate.substring(0,2)) >= 12 && Integer.parseInt(city2TimeSeparate.substring(0,2)) < 24)
                    city2TimeSeparate += " PM";
                else
                    city2TimeSeparate += " AM";
                if(Integer.parseInt(city2TimeSeparate.substring(0,2)) > 12)
                    city2TimeSeparate = (Integer.parseInt(city2TimeSeparate.substring(0, 2)) - 12) + city2TimeSeparate.substring(2);
                Log.d("TAG_1", "City 2 Date: " + city2DateSeparate);
                Log.d("TAG_1", "City 2 Time: " + city2TimeSeparate);
                String city3DateSeparate = dateTextCity3.substring(0, dateTextCity3.indexOf(" "));
                String city3TimeSeparate = dateTextCity3.substring(dateTextCity3.indexOf(" ") + 1);
                if(Integer.parseInt(city3TimeSeparate.substring(0,2)) >= 12 && Integer.parseInt(city3TimeSeparate.substring(0,2)) < 24)
                    city3TimeSeparate += " PM";
                else
                    city3TimeSeparate += " AM";
                if(Integer.parseInt(city3TimeSeparate.substring(0,2)) > 12)
                    city3TimeSeparate = (Integer.parseInt(city3TimeSeparate.substring(0, 2)) - 12) + city3TimeSeparate.substring(2);
                Log.d("TAG_1", "City 3 Date: " + city3DateSeparate);
                Log.d("TAG_1", "City 3 Time: " + city3TimeSeparate);
                city1DateTextView.setText(city1DateSeparate);
                city1TimeTextView.setText(city1TimeSeparate);
                city2DateTextView.setText(city2DateSeparate);
                city2TimeTextView.setText(city2TimeSeparate);
                city3DateTextView.setText(city3DateSeparate);
                city3TimeTextView.setText(city3TimeSeparate);

                //getting weather description
                String city1WeatherDescription = (String) jsonObject.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).get("description");
                Log.d("TAG_1", "City 1 Weather: " + city1WeatherDescription);
                String city2WeatherDescription = (String) jsonObject.getJSONArray("list").getJSONObject(1).getJSONArray("weather").getJSONObject(0).get("description");
                Log.d("TAG_1", "City 2 Weather: " + city2WeatherDescription);
                String city3WeatherDescription = (String) jsonObject.getJSONArray("list").getJSONObject(2).getJSONArray("weather").getJSONObject(0).get("description");
                Log.d("TAG_1", "City 3 Weather: " + city3WeatherDescription);
                city1DescriptionTextView.setText(city1WeatherDescription);
                city2DescriptionTextView.setText(city2WeatherDescription);
                city3DescriptionTextView.setText(city3WeatherDescription);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("TAG_1", "error");
            }
        }
    }
}