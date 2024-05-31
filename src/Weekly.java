import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Weekly {
    private static final int DAYS_TO_DISPLAY = 7;

    /**
     * Sets up the Swing-based user interface for the Weather Forecast application.
     * The UI includes a text field for entering the location, a search button to fetch weather data,
     * and a text area to display the weather forecast.
     */
public Weekly() {
    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Weather Forecast");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        JTextField locationTextField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(locationTextField);
        searchPanel.add(searchButton);

        JTextArea weatherTextArea = new JTextArea();
        weatherTextArea.setEditable(false);
        weatherTextArea.setFont(new Font("Serif", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(weatherTextArea);

        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = locationTextField.getText().trim();
                if (!location.isEmpty()) {
                    try {
                        JSONObject weatherData = getWData(location);
                        if (weatherData != null) {
                            String formattedWeather = formatWeatherData(weatherData);
                            weatherTextArea.setText(formattedWeather);
                        } else {
                            weatherTextArea.setText("Unable to fetch weather data for " + location);
                        }
                    } catch (Exception ex) {
                        weatherTextArea.setText("Error fetching weather data: " + ex.getMessage());
                    }
                } else {
                    weatherTextArea.setText("Please enter a location.");
                }
            }
        });

        frame.setVisible(true);
    });
}

    /**
     * Retrieves weather data for a specified location.
     *
     * @param locName the name of the location for which weather data is requested.
     * @return a JSONObject containing the weather data for the specified location, or null if data retrieval fails.
     * @throws IOException    if an I/O exception occurs while fetching data from the API.
     * @throws ParseException if an error occurs while parsing the JSON response.
     */
    public static JSONObject getWData(String locName) throws IOException, ParseException {
        JSONArray locData = getLocData(locName);
        if (locData == null || locData.isEmpty()) {
            return null;
        }

        JSONObject location = (JSONObject) locData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlStr = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,weather_code,sunrise,sunset" +
                "&timezone=Europe%2FBerlin&forecast_days=7";

        HttpURLConnection connection = fetchApiResponse(urlStr);
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }

        Scanner scanner = new Scanner(new InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();
        while (scanner.hasNext()) {
            result.append(scanner.nextLine());
        }
        scanner.close();

        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(result.toString());
    }

    /**
     * Retrieves location data based on the provided location name.
     *
     * @param locName the name of the location for which data is requested.
     * @return a JSONArray containing location data from the geocoding API response, or null if data retrieval fails.
     * @throws IOException    if an I/O exception occurs while fetching data from the API.
     * @throws ParseException if an error occurs while parsing the JSON response.
     */

    public static JSONArray getLocData(String locName) throws IOException, ParseException {
        locName = locName.replaceAll(" ", "+");
        String urlS = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locName + "&count=10&language=en&format=json";

        HttpURLConnection connection = fetchApiResponse(urlS);
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }

        Scanner scanner = new Scanner(new InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();
        while (scanner.hasNext()) {
            result.append(scanner.nextLine());
        }
        scanner.close();

        JSONParser parser = new JSONParser();
        JSONObject resultJOBJ = (JSONObject) parser.parse(result.toString());
        return (JSONArray) resultJOBJ.get("results");
    }

    /**
     * Establishes a connection to the specified URL and fetches the API response.
     *
     * @param urlS the URL to which the API request is made.
     * @return a HttpURLConnection object representing the connection to the API.
     * @throws IOException if an I/O exception occurs while connecting to the URL or fetching the response.
     */

    private static HttpURLConnection fetchApiResponse(String urlS) throws IOException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }


    /**
     * Formats the weather data into a human-readable string.
     *
     * @param jsonData the JSONObject containing the weather data.
     * @return a formatted string representing the weather forecast for the next seven days.
     */
    public static String formatWeatherData(JSONObject jsonData) {
        StringBuilder formattedData = new StringBuilder();

        JSONObject daily = (JSONObject) jsonData.get("daily");
        JSONArray weatherCodes = (JSONArray) daily.get("weather_code");
        JSONArray tempMax = (JSONArray) daily.get("temperature_2m_max");
        JSONArray tempMin = (JSONArray) daily.get("temperature_2m_min");
        JSONArray precipitation = (JSONArray) daily.get("precipitation_sum");
        JSONArray sunrise = (JSONArray) daily.get("sunrise");
        JSONArray sunset = (JSONArray) daily.get("sunset");

        String[] dayLabels = {"Today", "Tomorrow", "In two days", "In three days", "In four days", "In five days", "In six days"};

        for (int i = 0; i < DAYS_TO_DISPLAY; i++) {
            String sunriseTime = ((String) sunrise.get(i)).substring(11);
            String sunsetTime = ((String) sunset.get(i)).substring(11);
            long weatherCode = (long) weatherCodes.get(i);

            formattedData.append(dayLabels[i]).append(":\n");
            formattedData.append("Weather: ").append(convertWCode(weatherCode)).append("\n");
            formattedData.append("Max Temp: ").append(tempMax.get(i)).append("°C\n");
            formattedData.append("Min Temp: ").append(tempMin.get(i)).append("°C\n");
            formattedData.append("Precipitation: ").append(precipitation.get(i)).append(" mm\n");
            formattedData.append("Sunrise: ").append(sunriseTime).append("\n");
            formattedData.append("Sunset: ").append(sunsetTime).append("\n");
            formattedData.append("\n");
        }

        return formattedData.toString();
    }

    /**
     * Converts a weather code into a human-readable weather condition.
     *
     * @param weatherCode the code representing the weather condition.
     * @return a string describing the weather condition corresponding to the given code.
     */
    private static String convertWCode(long weatherCode) {
        String weatherCond = "";
        if (weatherCode == 0L) {
            weatherCond = "Clear";
        } else if (weatherCode <= 3L && weatherCode > 0L) {
            weatherCond = "Cloudy";
        } else if ((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
            weatherCond = "Rain";
        } else if (weatherCode >= 71L && weatherCode <= 77L) {
            weatherCond = "Snow";
        }
        return weatherCond;
    }
}
