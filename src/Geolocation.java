
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

            JSONArray weatherCode = (JSONArray)  hourly.get("weathercode");
            String weatherCond = convertWCode((long)weatherCode.get(index));

            JSONArray humData = (JSONArray) hourly.get("relativehumidity_2m");
            long relHum = (long) humData.get(index);

            JSONArray pressData = (JSONArray) hourly.get("surface_pressure");
            long pressure  = (long)pressData.get(index);

            JSONArray windData = (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) windData.get(index);

            JSONObject wData = new JSONObject();
            wData.put("temperature",temperature);
            wData.put("weather_condition",weatherCond);
            wData.put("humidity",relHum);
            wData.put("pressure",pressure);
            wData.put("windspeed",windspeed);

            return wData;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
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
    public static String getCurT(){
       LocalDateTime curDataT = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formDateT = curDataT.format(formatter);
        return formDateT;
    }
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
