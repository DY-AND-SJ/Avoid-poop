import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GameManager
{
    public static void playSound(String file, boolean Loop)
    {
        try
        {
            AudioInputStream au= AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(au);
            clip.start();
            if (Loop) clip.loop(-1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private static HashMap<String, BufferedImage> imageHashMap = new HashMap<>();

    public static BufferedImage loadImage(String path)
    {
        if (imageHashMap.containsKey(path))
            return imageHashMap.get(path);
        BufferedImage image = null;
        try
        {
            if (!new File(path).exists()) System.out.println("Adsf");
            image = ImageIO.read(new File(path));

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        imageHashMap.put(path, image);
        return image;
    }
}
