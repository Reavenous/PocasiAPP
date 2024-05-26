
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Geolocation {

   public static JSONObject getWData(String locName){
        JSONArray locData = getLocData(locName);
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

}
