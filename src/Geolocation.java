
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Geolocation {

    /**
     * Retrieves weather data for a given location.
     *
     * @param locName the name of the location to get weather data for
     * @return a JSONObject containing weather data such as temperature, weather condition,
     *         humidity, pressure, windspeed, UV index, precipitation, sunrise, and sunset times.
     *         Returns null if unable to fetch data.
     * @throws IllegalArgumentException if the location data cannot be retrieved or parsed
     */
   public static JSONObject getWData(String locName){
        JSONArray locData = getLocData(locName);

        JSONObject location =  (JSONObject) locData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlStr = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude +"&longitude="+ longitude+
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,surface_pressure,wind_speed_10m&daily=weather_code,sunrise,sunset,uv_index_max,precipitation_sum&timezone=Europe%2FBerlin&forecast_days=1";
        try {
            HttpURLConnection connection = fetchApiResponse(urlStr);
            if (connection.getResponseCode() != 200){
                System.out.println("Couldn't connect to api");
                System.out.println(urlStr);
                return null;
            }
            StringBuilder resultJ = new StringBuilder();
            Scanner sc = new Scanner(connection.getInputStream());
            while(sc.hasNext()){
                resultJ.append(sc.nextLine());
            }
            sc.close();
            connection.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resJOBJ  = (JSONObject) parser.parse(String.valueOf(resultJ));

            JSONObject hourly = (JSONObject) resJOBJ.get("hourly");
            JSONArray timeH = (JSONArray) hourly.get("time");
            int index = findCurT(timeH);

            JSONArray tempData = (JSONArray) hourly.get("temperature_2m");
            double temperature  = (double) tempData.get(index);

            JSONArray weatherCode = (JSONArray)  hourly.get("weather_code");
            String weatherCond = convertWCode((long)weatherCode.get(index));

            JSONArray humData = (JSONArray) hourly.get("relative_humidity_2m");
            long relHum = (long) humData.get(index);

            JSONArray pressData = (JSONArray) hourly.get("surface_pressure");
            double pressure  = (double)pressData.get(index);

            JSONArray windData = (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) windData.get(index);



            JSONObject daily = (JSONObject) resJOBJ.get("daily");
            JSONArray day = (JSONArray) daily.get("time");
            int indexDay = findDay(day);

            JSONArray uvIData = (JSONArray) daily.get("uv_index_max");
            double uvIndex = (double) uvIData.get(indexDay);

            JSONArray precipData = (JSONArray) daily.get("precipitation_sum");
            double precipitation = (double) precipData.get(indexDay);

            JSONArray riseData = (JSONArray) daily.get("sunrise");
            String sunrise = (String) riseData.get(indexDay);
            sunrise = sunrise.substring(11);


            JSONArray setData = (JSONArray) daily.get("sunset");
            String sunset = (String) setData.get(indexDay);
            sunset = sunset.substring(11);




            JSONObject wData = new JSONObject();
            wData.put("temperature",temperature);
            wData.put("weather_condition",weatherCond);
            wData.put("humidity",relHum);
            wData.put("pressure",pressure);
            wData.put("windspeed",windspeed);
            wData.put("uvIndex",uvIndex);
            wData.put("precipitation",precipitation);
            wData.put("sunrise",sunrise);
            wData.put("sunset",sunset);


            return wData;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * Retrieves location data for a given location name using the Open-Meteo Geocoding API.
     *
     * @param locName the name of the location to get geocoding data for
     * @return a JSONArray containing location data such as latitude and longitude.
     *         Returns null if unable to fetch data.
     * @throws IllegalArgumentException if the location data cannot be retrieved or parsed
     */
    public static JSONArray getLocData(String locName){
        locName = locName.replaceAll(" ","+");
        String urlS = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locName + "&count=10&language=en&format=json";

        try {
            HttpURLConnection  connection = fetchApiResponse(urlS);
            if (connection.getResponseCode() != 200){
                System.out.println("Couldn't connect to API");
                return null;
            }else{
                StringBuilder resultJ =new StringBuilder();
                Scanner sc = new Scanner(connection.getInputStream());

                while (sc.hasNext()){
                    resultJ.append(sc.nextLine());
                }
                sc.close();
                connection.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultJOBJ = (JSONObject) parser.parse(String.valueOf(resultJ));
                JSONArray locData = (JSONArray) resultJOBJ.get("results");
                return locData;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
   }


    /**
     * Establishes an HTTP connection to the given URL and returns the HttpURLConnection object.
     *
     * @param urlS the URL string to connect to
     * @return the HttpURLConnection object for the specified URL
     * @throws IOException if an I/O exception occurs
     */
    private static HttpURLConnection fetchApiResponse(String urlS){
       try {
           URL url = new URL(urlS);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();

           connection.setRequestMethod("GET");
           connection.connect();
           return connection;

       }catch(IOException e){
           e.printStackTrace();
       }


        return null;
    }

    /**
     * Finds the index of the current time in the given JSON array of times.
     *
     * @param tList a JSONArray containing time strings
     * @return the index of the current time in the given array, or 0 if the current time is not found
     */
    private static int findCurT(JSONArray tList){//cobain
       String curT = getCurT();

       for (int i = 0; i<tList.size();i++){
           String time = (String) tList.get(i);
           if (time.equalsIgnoreCase(curT)){
                return i;
           }
       }
       return 0;
    }
    private static int findDay(JSONArray tList){
        String curD = getDay();

        for (int i = 0; i<tList.size();i++){
            String time = (String) tList.get(i);
            if (time.equalsIgnoreCase(curD)){
                return i;
            }
        }
        return 0;
    }

    /**
     * Retrieves the current date and time formatted as a string.
     * The format used is "yyyy-MM-dd'T'HH':00'".
     *
     * @return the current date and time formatted as a string
     */
    public static String getCurT(){
       LocalDateTime curDataT = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formDateT = curDataT.format(formatter);
        return formDateT;
    }
    public static String getDay(){
        LocalDateTime curDataT = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formDateT = curDataT.format(formatter);
        return formDateT;
    }

    /**
     * Converts a weather code to a human-readable weather condition.
     *
     * @param weatherCode the weather code to convert
     * @return a string representing the weather condition (e.g., "Clear", "Cloudy", "Rain", "Snow")
     */
    private static String convertWCode(long weatherCode){
       String weatherCond = "";
       if (weatherCode ==  0L ){
           weatherCond = "Clear";
       } else if (weatherCode <= 3L && weatherCode > 0L) {
           weatherCond = "Cloudy";
       } else if ((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
           weatherCond = "Rain";
       }else if (weatherCode >= 71L && weatherCode <= 77L){
           weatherCond = "Snow";
       }
       return weatherCond;
    }

}
