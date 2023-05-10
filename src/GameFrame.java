import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
public class GameFrame extends JFrame{

    GamePanel panel;
    GameFrame(){
        panel = new GamePanel();
        this.add(panel);
        this.setTitle("Hockey Game");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();                            //game panel ko size anusar game frame adjust hunxa afai
        this.setVisible(true);
        this.setLocationRelativeTo(null);       //puts frame in the center of the screen
    }

}
