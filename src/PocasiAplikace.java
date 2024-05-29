import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class PocasiAplikace extends JFrame {

    private JSONObject wData;

    public PocasiAplikace(){
        super("Počasí");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,900);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        addComponents();
    }

    private void addComponents(){
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(50,25,400,75);
        searchTextField.setFont(new Font("Dialog",Font.BOLD,27));
        add(searchTextField);

        JLabel temperatureTxt = new JLabel("10 C");
        temperatureTxt.setBounds(13,350,550,60);
        temperatureTxt.setFont(new Font("Dialog",Font.BOLD,52));
        temperatureTxt.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureTxt);

        JLabel whatWeather = new JLabel("cloudy ");
        whatWeather.setBounds(13,410,553,48);
        whatWeather.setFont(new Font("Dialog",Font.BOLD,42));
        whatWeather.setHorizontalAlignment(SwingConstants.CENTER);
        add(whatWeather);

        JLabel humidityTxt = new JLabel("10%");
        humidityTxt.setBounds(60,400,150,75);
        humidityTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(humidityTxt);

        JLabel windForceTxt = new JLabel("10 km/h");
        windForceTxt.setBounds(435,400,150,75);
        windForceTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(windForceTxt);

        JLabel uvIndexTxt = new JLabel("1000");
        uvIndexTxt.setBounds(60,550,150,75);
        uvIndexTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(uvIndexTxt);

        JLabel pressureTxt = new JLabel("2500ph");
        pressureTxt.setBounds(250,550,150,75);
        pressureTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(pressureTxt);

        JLabel precipitationTxt = new JLabel("10mm");
        precipitationTxt.setBounds(435,550,150,75);
        precipitationTxt.setFont(new Font("dialog", Font.BOLD,20));
        add(precipitationTxt);

        JLabel sunriseTxt = new JLabel("06:99");
        sunriseTxt.setBounds(60,675,150,75);
        sunriseTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(sunriseTxt);

        JLabel sunsetTxt = new JLabel("21:99");
        sunsetTxt.setBounds(435,675,150,75);
        sunsetTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(sunsetTxt);






        JLabel weatherIM = new JLabel(loadIM("src/pocasiFoto/cloudy.png"));
        weatherIM.setBounds(140,125,256,256);
        add(weatherIM);

        JLabel humidityIM = new JLabel(loadIM("src/pocasiFoto/humidity.png"));
        humidityIM.setBounds(50,350,64,64);
        add(humidityIM);

        JLabel windForceIM = new JLabel(loadIM("src/pocasiFoto/windforce.png"));
        windForceIM.setBounds(435,350,64,64);
        add(windForceIM);

        JLabel uvIndexIM = new JLabel(loadIM("src/pocasiFoto/uvindex.png"));
        uvIndexIM.setBounds(60,500,64,64);
        add(uvIndexIM);

        JLabel pressureIM = new JLabel(loadIM("src/pocasiFoto/pressure.png"));
        pressureIM.setBounds(250,500,64,64);
        add(pressureIM);

        JLabel precipitationIM = new JLabel(loadIM("src/pocasiFoto/precipitations.png"));
        precipitationIM.setBounds(435,500,64,64);
        add(precipitationIM);

        JLabel sunriseIM = new JLabel(loadIM("src/pocasiFoto/sunrise.png"));
        sunriseIM.setBounds(60,625,64, 64);
        add(sunriseIM);

        JLabel sunsetIM = new JLabel(loadIM("src/pocasiFoto/sunset.png"));
        sunsetIM.setBounds(435,625,64,64);
        add(sunsetIM);


        JButton searchB = new JButton(loadIM("src/pocasiFoto/search.png"));
        searchB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchB.setBounds(455   ,25,64,64);
        searchB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText();

                if (userInput.replaceAll("//s","").length() <= 0){
                    return;
                }
                wData = Geolocation.getWData(userInput);


                String wCond = (String) wData.get("weather_condition");
                switch (wCond){
                    case"Clear":
                        weatherIM.setIcon(loadIM("src/pocasiFoto/clear.png"));
                        break;
                    case"Cloudy":
                        weatherIM.setIcon(loadIM("src/pocasiFoto/cloudy.png"));
                        break;
                    case"Rain":
                        weatherIM.setIcon(loadIM("src/pocasiFoto/rain.png"));
                        break;
                    case"Snow":
                        weatherIM.setIcon(loadIM("src/pocasiFoto/snow.png"));
                        break;

                }
                double temperature = (double) wData.get("temperature");
                temperatureTxt.setText(temperature + " °C");

                whatWeather.setText(wCond);

                long humidity = (long) wData.get("humidity");
                humidityTxt.setText("<html><b>Humidity:</b> " + humidity + "%</html>");

                double pressure = (double) wData.get("pressure");
                pressureTxt.setText("<html><b>Pressure:</b> " + pressure + " hPa</html>");

                double windspeed = (double) wData.get("windspeed");
                windForceTxt.setText("<html><b>Windspeed:</b> " + windspeed + " km/h</html>");

                double uvIndex = (double) wData.get("uvIndex");
                uvIndexTxt.setText("<html><b>Uv Index:</b> " + uvIndex + " </html>");

                double precipitations = (double) wData.get("precipitation");
                precipitationTxt.setText("<html><b>Precipitations:</b> " + precipitations + " mm</html>");

                String sunrise  = (String) wData.get("sunrise");
                sunriseTxt.setText("<html><b>Sunrise:</b> " + sunrise + "</html>");

                String sunset = (String) wData.get("sunset");
                sunsetTxt.setText("<html><b>Sunset:</b> " + sunset + " </html>");

            }

        });
        add(searchB);

    }
    private ImageIcon loadIM(String resourcePath){
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("resource not found!");
        return null;
    }
}
