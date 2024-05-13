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
        searchTextField.setBounds(25,25,400,75);
        searchTextField.setFont(new Font("Dialog",Font.ROMAN_BASELINE,27));
        add(searchTextField);
        JButton searchB = new JButton(loadImage("pocasiFoto/search.png"));
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
