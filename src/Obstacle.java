import java.awt.image.BufferedImage;

public class Obstacle extends GameObject
{
    private Player player;
    public int line;
    public Obstacle(double x, double y, BufferedImage image, ID id, Handler handler, double velY, Player player, int line)
    {
        super(x, y, image, id, handler);
        this.velY = velY;
        this.player = player;
        this.line = line;
    }
    @Override
    public Collision getBounds()
    {
        return new Circle((int)this.x + this.WIDTH/2, (int)this.y + this.HEIGHT/2, this.WIDTH/2);
    }
    private boolean imKiller = false;
    @Override
    public void tick(double frameTime)
    {
        if (imKiller) return;
        this.y += velY * frameTime / 10000000;
        if (this.y + 2*velY>= Game.HEIGHT) handler.removeObject(this);
        if (getBounds().intersects(player.getBounds()))
        {
            handler.removeObject(player);
            imKiller = true;
            this.image = GameManager.loadImage("obstacle3.png");
        }
    }
}
