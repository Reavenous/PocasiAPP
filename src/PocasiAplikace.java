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
        setSize(650,750);
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
        JButton searchB = new JButton(loadImage("src/pocasiFoto/search.png"));
        searchB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchB.setBounds(455   ,25,75,75);
        add(searchB);

        JLabel weatherImage = new JLabel(loadImage("src/pocasiFoto/cloudy.png"));
        weatherImage.setBounds(13,125,550,250);
        add(weatherImage);

        JLabel temperatureTxt = new JLabel("10 C");
        temperatureTxt.setBounds(13,350,550,60);
        temperatureTxt.setFont(new Font("dialog",Font.ROMAN_BASELINE,52));
        temperatureTxt.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureTxt);
    }
    private ImageIcon loadImage(String resourcePath){
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
