import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class PocasiAplikace extends JFrame {

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
        searchTextField.setFont(new Font("Dialog",Font.ROMAN_BASELINE,27));
        add(searchTextField);

        JLabel temperatureTxt = new JLabel("10 C");
        temperatureTxt.setBounds(13,350,550,60);
        temperatureTxt.setFont(new Font("Dialog",Font.ROMAN_BASELINE,52));
        temperatureTxt.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureTxt);

        JLabel whatWeather = new JLabel("cloudy ");
        whatWeather.setBounds(13,400,550,45);
        whatWeather.setFont(new Font("Dialog",Font.ROMAN_BASELINE,42));
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
        uvIndexTxt.setBounds(60,500,150,75);
        uvIndexTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(uvIndexTxt);

        JLabel pressureTxt = new JLabel("2500ph");
        pressureTxt.setBounds(250,500,150,75);
        pressureTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(pressureTxt);

        JLabel precipitationTxt = new JLabel("10mm");
        precipitationTxt.setBounds(435,500,150,75);
        precipitationTxt.setFont(new Font("dialog", Font.BOLD,20));
        add(precipitationTxt);

        JLabel sunriseTxt = new JLabel("06:99");
        sunriseTxt.setBounds(60,600,150,75);
        sunriseTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(sunriseTxt);

        JLabel sunsetTxt = new JLabel("21:99");
        sunsetTxt.setBounds(250,600,150,75);
        sunsetTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(sunsetTxt);

        JLabel moonStateTxt = new JLabel("new moon");
        moonStateTxt.setBounds(435,600,150,75);
        moonStateTxt.setFont(new Font("dialog",Font.BOLD,20));
        add(moonStateTxt);


        JButton searchB = new JButton(loadIM("src/pocasiFoto/search.png"));
        searchB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchB.setBounds(455   ,25,75,75);
        add(searchB);

        JLabel weatherIM = new JLabel(loadIM("src/pocasiFoto/cloudy.png"));
        weatherIM.setBounds(13,125,550,250);
        add(weatherIM);

        JLabel humidityIM = new JLabel(loadIM("src/pocasiFoto/humidity.png"));
        humidityIM.setBounds(50,350,75,75);
        add(humidityIM);

        JLabel windForceIM = new JLabel(loadIM("src/pocasiFoto/windspeed.png"));
        windForceIM.setBounds(435,350,75,75);
        add(windForceIM);

        JLabel uvIndexIM = new JLabel(loadIM("src/pocasiFoto/search.png"));
        uvIndexIM.setBounds(60,450,75,75);
        add(uvIndexIM);

        JLabel pressureIM = new JLabel(loadIM("src/pocasiFoto/search.png"));
        pressureIM.setBounds(250,450,75,75);
        add(pressureIM);

        JLabel precipitationIM = new JLabel("src/pocasiFoto/search.png");
        precipitationIM.setBounds(435,450,75,75);
        add(precipitationIM);

        JLabel sunriseIM = new JLabel("src/pocasiFoto/search.png");
        sunriseIM.setBounds(60,550,75,75);
        add(sunriseIM);

        JLabel sunsetIM = new JLabel("src/pocasiFoto/search.png");
        sunsetIM.setBounds(250,550,75,75);
        add(sunsetIM);

        JLabel moonStateIM = new JLabel("src/pocasiFoto/search.png");
        moonStateIM.setBounds(435,550,75,75);
        add(moonStateIM);

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
