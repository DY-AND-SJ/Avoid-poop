import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends GameObject
{
    private boolean die = false;

    public boolean isDie()
    {
        return die;
    }

    public Player(double x, double y, BufferedImage image, ID id, Handler handler)
    {
        super(x, y, image, id, handler);
    }

    public int level = 1;

    @Override
    public void tick(double frameTime)
    {
        if (KeyInput.isKeyPressed(KeyEvent.VK_LEFT)) this.x -= Math.min(level * 200, 30) * frameTime / 10000000;
        if (KeyInput.isKeyPressed(KeyEvent.VK_RIGHT)) this.x += Math.min(level * 200, 30)* frameTime / 10000000;
        if (this.x < 0) this.x = 0;
        if (this.x + this.WIDTH > Game.WIDTH) this.x = Game.WIDTH - this.WIDTH;
    }

    @Override
    public void destructor()
    {
        die = true;
        handler.getGame().stop();
    }
}
