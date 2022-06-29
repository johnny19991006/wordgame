import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

public class BGM{


    public BGM(){

            try {

                AudioInputStream ais3 = AudioSystem.getAudioInputStream(new File("sound/music.wav"));
                Clip clip3 = AudioSystem.getClip();
                clip3.open(ais3);
                //소리설정
                FloatControl gainControl3 = (FloatControl) clip3.getControl(FloatControl.Type.MASTER_GAIN);
                //볼륨설정
                gainControl3.setValue(-30.0f);
                clip3.start();


            } catch (Exception e) {
                e.printStackTrace();//예외처리
            }


    }
}