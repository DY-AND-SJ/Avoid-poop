import java.awt.*;
import java.awt.image.BufferedImage;

public class Spownser extends GameObject
{
    private Player player;

    public Spownser(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, Player player)
    {
        super(x, y, WIDTH, HEIGHT, id, handler);
        this.player = player;
        this.color = Color.black;
    }

    private BufferedImage obstacleImage = GameManager.loadImage("obstacle.png");
    private int level = 1;
    private long lastSpownTime = System.currentTimeMillis();
    private long lastLevelUpTime = System.currentTimeMillis();
    private final long startTime = System.currentTimeMillis();
    private long lastFakeSpawnTime = System.currentTimeMillis();
    private long score;
    public long getScore()
    {
        return score;
    }
    int cnt = 0;
    @Override
    public void tick(double frameTime)
    {
        if (player.isDie()) return;
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastLevelUpTime >= 5000)
        {
            ++level;
            player.level = level;
            lastLevelUpTime = nowTime;
        }
        if (Math.sqrt(level)*2* (nowTime - lastSpownTime) > 1000)
        {

            int line = (int) (Math.random() * 10);
            spown(line);
            lastSpownTime = nowTime;
        }
        score = (nowTime - startTime) / 1000;
        if (handler.dna.virtual_spawn_count * (nowTime - lastFakeSpawnTime) > 1000)
        {
            lastFakeSpawnTime = nowTime;
            Obstacle ob =null;
            if (cnt %2 == 0)
                ob = new Obstacle(-30 - obstacleImage.getWidth(), -200, obstacleImage, ID.Obstacle, handler, handler.dna.virtual_spawn_speed, player, -1);
            else
                ob = new Obstacle(Game.WIDTH + 30, -200, obstacleImage, ID.Obstacle, handler, handler.dna.virtual_spawn_speed, player, -1);
            ++cnt;
            handler.addObject(ob);
        }
    }

    private void spown(int line)
    {
        int lineWidth = Game.WIDTH / 10;
        int x = line * lineWidth + lineWidth / 2 - obstacleImage.getWidth() / 2;
        Obstacle ob = new Obstacle(x, -200, obstacleImage, ID.Obstacle, handler, level < 4 ? level * 2 : (int) (Math.random() * 3 * Math.sqrt(level)) + 5, player, line);
        handler.addObject(ob);
    }

    private Font font = new Font("serif", Font.BOLD, 20);

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.drawString(score + "", 10, 20);
    }
}
