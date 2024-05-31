import org.json.simple.JSONObject;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Launcher {
    public static void main(String[] args) {

        {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Weekly Weather Forecast");
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
                                JSONObject weatherData = Weekly.getWData(location);
                                if (weatherData != null) {
                                    String formattedWeather = Weekly.formatWeatherData(weatherData);
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
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new PocasiAplikace().setVisible(true);

                }
            });
        }
    }
}