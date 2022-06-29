import java.awt.*;
import javax.swing.*;

public class WordGame extends JFrame {
    public static final int WIDTH = 710;
    public WordGame() {
        initUI();
        initSound();
    }

    private void initUI() {
        setSize(WIDTH, 920);
        setResizable(false);
        setTitle("불멸의 이순신 WodGame_ver");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add( new Board() );

        /*Music introMusic=new Music("introMusic",true);
        introMusic.start();*/

    }
    private void initSound(){

        new BGM();
    }

    public static void main(String[] args) {

        WordGame game = new WordGame();
        game.setVisible(true);
    }
}