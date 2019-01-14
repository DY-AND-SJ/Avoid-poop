import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public final class Game extends Canvas implements Runnable
{
    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;
    private Thread thread;
    private boolean running = false;
    private Handler handler;
    public long lastScore;
    private Window window;


    public static double[] genetics;

    public static long testGame(double[] G)
    {
        genetics = G;
        return new Game().lastScore;
    }

    private Game()
    {
        this.addKeyListener(new KeyInput());

        this.window = new Window(WIDTH, HEIGHT, this);

        handler = new Handler(this);
        this.start();

    }

    public void start()
    {
        thread = new Thread(this);

        running = true;
        thread.start();
        try
        {
            thread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        for (GameObject obj : handler.getObjects())
            if (obj instanceof Spownser)
            {
                //System.out.println(((Spownser) obj).getScore());
                lastScore = ((Spownser) obj).getScore();
                break;
            }
    }

    public void stop()
    {
        running = false;
        window.frame.dispose();
    }

    @Override
    public void run()
    {
        double currentTime = System.nanoTime();

        while (running)
        {
            double newTime = System.nanoTime();
            double frameTime = newTime - currentTime;
            currentTime = newTime;
            tick(frameTime);
            render();
        }
    }

    private void tick(double frameTime)
    {
        handler.tick(frameTime);
    }

    private void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();


        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        handler.render(g2d);

        g2d.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {

        for (int i = 0; i < 10; ++i)
            System.out.println(testGame());
    }
}

class DNA
{
    double diagonal_dist;
    double velocity;
    double y_coord;
    double resemblance;
    double node_erase;
    double decrease_rate;
    double virtual_spawn_count;
    double virtual_spawn_speed;

    double fitness;

    DNA()
    {
        Random random = new Random();
        diagonal_dist = random.nextDouble() * (5 - 0.2) + 0.2;
        velocity = random.nextDouble() * (5 - 0.2) + 0.2;
        y_coord = random.nextDouble() * (5 - 0.2) + 0.2;
        resemblance = random.nextDouble() * (7000 - 10) + 10;
        node_erase = random.nextDouble() * 30000;
        decrease_rate = random.nextDouble();
        virtual_spawn_count = random.nextDouble() * 20;
        virtual_spawn_speed = random.nextDouble() * (30 - 1) + 1;
    }

    void fitness()
    {
        fitness = Game.testGame(new double[]{diagonal_dist, velocity, y_coord, resemblance, node_erase, decrease_rate, virtual_spawn_count, virtual_spawn_speed});
    }

    DNA crossover(DNA partner)
    {
        DNA child = new DNA();
        

        return child;
    }

    void mutate(double mutationRate)
    {

    }
}