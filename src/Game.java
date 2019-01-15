import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class Game extends Canvas implements Runnable {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;
    public long lastScore;
    private Thread thread;
    private boolean running = false;
    private Handler handler;
    private Window window;
    private static final double mutationRate = 0.01;


    private Game(DNA dna) {
        //this.addKeyListener(new KeyInput());

        //this.window = new Window(WIDTH, HEIGHT, this);

        handler = new Handler(this, dna);
        this.start();
    }

    public static void testGame(DNA[] dna) {
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<Long>[] futures = new Future[dna.length];
        //Thread[] threads = new Thread[dna.length];
        for (int i = 0; i < dna.length; ++i) {
            DNA testDNA = dna[i];
            futures[i] = ex.submit(() -> new Game(testDNA).lastScore);
        }
        for (int i = 0; i < dna.length; ++i) {
            try {
                dna[i].fitness = futures[i].get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        ex.shutdown();
    }

    public static void main(String[] args) {
        final int totalDNA = 100;
        DNA[] d = new DNA[totalDNA];
        for (int Game = 0; Game < 10; ++Game) {
            for (int i = 0; i < totalDNA; ++i) d[i] = new DNA();
            testGame(d);
            for (int i = 0; i < totalDNA; ++i) System.out.println(d[i].fitness);

            ArrayList<DNA> matingPool = new ArrayList<>();

            for (int i = 0; i < d.length; ++i) {
                int n = (int) d[i].fitness * 100;

                for(int j = 0; j < n; ++j) {
                    matingPool.add(d[i]);
                }
            }

            Random random = new Random();
            for(int i = 0; i < d.length; ++i) {
                int a = (int) random.nextDouble() * matingPool.size();
                int b = (int) random.nextDouble() * matingPool.size();

                while (a == b) {
                    a = (int) random.nextDouble() * matingPool.size();
                    b = (int) random.nextDouble() * matingPool.size();
                }

                DNA parentA = matingPool.get(a);
                DNA parentB = matingPool.get(b);

                DNA child = parentA.crossover(parentB);

                child.mutate(mutationRate);

                d[i] = child;
            }
        }
    }

    public void start() {
        thread = new Thread(this);

        running = true;
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (GameObject obj : handler.getObjects())
            if (obj instanceof Spownser) {
                //System.out.println(((Spownser) obj).getScore());
                lastScore = ((Spownser) obj).getScore();
                break;
            }
    }

    public void stop() {
        running = false;
        //window.frame.dispose();
    }

    @Override
    public void run() {
        double currentTime = System.nanoTime();

        while (running) {
            double newTime = System.nanoTime();
            double frameTime = newTime - currentTime;
            currentTime = newTime;
            tick(frameTime);
            //render();
        }
    }

    private void tick(double frameTime) {
        handler.tick(frameTime);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
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
}

class DNA {
    double diagonal_dist;
    double velocity;
    double y_coord;
    double resemblance;
    double node_erase;
    double decrease_rate;
    double virtual_spawn_count;
    double virtual_spawn_speed;
    double player_speed;//1~30
    double fitness;

    DNA() {
        Random random = new Random();
        diagonal_dist = random.nextDouble() * (5 - 0.2) + 0.2;
        velocity = random.nextDouble() * (5 - 0.2) + 0.2;
        y_coord = random.nextDouble() * (5 - 0.2) + 0.2;
        resemblance = random.nextDouble() * (7000 - 10) + 10;
        node_erase = random.nextDouble() * 30000;
        decrease_rate = random.nextDouble();
        virtual_spawn_count = random.nextDouble() * 20;
        virtual_spawn_speed = random.nextDouble() * (30 - 1) + 1;
        player_speed = random.nextDouble() * 29 + 1;
    }

    void fitness() {
        //fitness = Game.testGame(new double[]{diagonal_dist, velocity, y_coord, resemblance, node_erase, decrease_rate, virtual_spawn_count, virtual_spawn_speed});
    }

    DNA crossover(DNA partner) {
        DNA child = new DNA();

        child.diagonal_dist = (this.diagonal_dist + partner.diagonal_dist) / 2;
        child.velocity = (this.velocity + partner.velocity) / 2;
        child.y_coord = (this.y_coord + partner.y_coord) / 2;
        child.resemblance = (this.resemblance + partner.resemblance) / 2;
        child.node_erase = (this.node_erase + partner.node_erase) / 2;
        child.decrease_rate = (this.decrease_rate + partner.decrease_rate) / 2;
        child.virtual_spawn_count = (this.virtual_spawn_count + partner.virtual_spawn_count) / 2;
        child.virtual_spawn_speed = (this.virtual_spawn_speed + partner.virtual_spawn_speed) / 2;
        child.player_speed = (this.player_speed + partner.player_speed) / 2;

        return child;
    }

    void mutate(double mutationRate) {
        diagonal_dist = mutate(mutationRate, diagonal_dist, 0.2, 5);
        velocity = mutate(mutationRate, velocity, 0.2, 5);
        y_coord = mutate(mutationRate, y_coord, 0.2, 5);
        resemblance = mutate(mutationRate, resemblance, 10, 7000);
        node_erase = mutate(mutationRate, node_erase, 0, 30000);
        decrease_rate = mutate(mutationRate, decrease_rate, 0, 1);
        virtual_spawn_count = mutate(mutationRate, virtual_spawn_count, 0, 20);
        virtual_spawn_speed = mutate(mutationRate, virtual_spawn_speed, 1, 30);
        player_speed =  mutate(mutationRate, player_speed, 1, 30);
    }

    double mutate(double mutationRate, double target, double b1, double b2) {
        Random r = new Random();
        if (r.nextDouble() < mutationRate) {
            return r.nextDouble() * (b2 - b1) + b1;
        }
        return target;
    }
}