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
        setSize(650,900);
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
