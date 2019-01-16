import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.*;

public class GE extends GameObject
{
    private Player player;

    public GE(Handler handler, Player player)
    {
        super(-100, -100, 0, 0, ID.GE, handler, -1000);
        this.player = player;
    }

    private double getDangerVal(Point2D.Double player, Obstacle obstacle)
    {
        return -Math.pow(player.distance(obstacle.getMidPoint()), handler.dna.diagonal_dist) + Math.pow(obstacle.velY, handler.dna.velocity) + Math.pow(Math.max(obstacle.y, 1), handler.dna.y_coord);
    }

    class Data
    {
        double d;
        Obstacle obstacle;

        Data(double d, Obstacle obstacle)
        {
            this.d = d;
            this.obstacle = obstacle;
        }
    }

    @Override
    public void tick(double frameTime)
    {
        KeyInput.ReleaseKey(KeyEvent.VK_RIGHT);
        KeyInput.ReleaseKey(KeyEvent.VK_LEFT);
        TreeSet<GameObject> set = handler.findObjectsById(ID.Obstacle);
        if (set.size() == 0) return;


        Point2D.Double playerMid = player.getMidPoint();


        ArrayList<Data> dangers = new ArrayList<>();

        for (GameObject obj : set)
        {
            Obstacle candi = (Obstacle) obj;
            /////////////////////////
            candi.image = GameManager.loadImage("obstacle.png");
            double danger = getDangerVal(playerMid, candi);
            dangers.add(new Data(danger, candi));
        }
        Collections.sort(dangers, (i, j) -> Double.compare(i.d, j.d));
        double leftVal = 0, rightVal = 0;
        double w = 1;
///////////////////////////////////////
        ArrayList<Integer>[] M = new ArrayList[dangers.size() + 1];
        for (int i = 0; i < M.length; ++i) M[i] = new ArrayList<Integer>();

        for (int i = 0; i < dangers.size(); ++i)
        {
            Data di = dangers.get(i);
            if (di.obstacle.x < 0) continue;
            if (di.obstacle.x + di.obstacle.WIDTH > Game.WIDTH) continue;
            if (di.obstacle.line == -1) continue;
            if (di.obstacle.y < 300) continue;
            for (int j = i + 1; j < dangers.size(); ++j)
            {
                Data dj = dangers.get(j);
                if (dj.obstacle.x < 0) continue;
                if (dj.obstacle.x + dj.obstacle.WIDTH > Game.WIDTH) continue;
                if (dj.obstacle.line == -1) continue;
                if (di.obstacle.getMidPoint().distance(dj.obstacle.getMidPoint()) < 90)
                {
                    M[dangers.size()].add(i);
                    M[dangers.size()].add(j);
                    M[i].add(j);
                    M[j].add(i);
                }
            }
        }
        Set<Integer> visit = new HashSet<>();
        Set<Data> del = new HashSet<>();
        for (int start : M[M.length - 1])
        {
            if (visit.contains(start)) continue;
            visit.add(start);
            Queue<Integer> Q = new LinkedList<>();
            Q.add(start);
            int cand = start;
            ArrayList<Data> allinGroup = new ArrayList<>();
            boolean moreImportant = false;
            while (!Q.isEmpty())
            {
                int now = Q.poll();
                allinGroup.add(dangers.get(now));
                if (dangers.get(now).obstacle.line == 0 || dangers.get(now).obstacle.line == 9)
                {
                    cand = now;
                    moreImportant = true;
                }
                /*if (Math.abs(dangers.get(now).obstacle.x - Game.WIDTH / 2) < Math.abs(dangers.get(cand).obstacle.x - Game.WIDTH / 2))
                    cand = now;*/
                /*else if (dangers.get(cand).obstacle.line == 0 || dangers.get(cand).obstacle.line == 9)
                    cand = now;*/
                //System.out.println(dangers.get(cand).d + "   " + dangers.get(now).d);

                if (dangers.get(cand).d < dangers.get(now).d && dangers.get(cand).obstacle.line != 0 && dangers.get(cand).obstacle.line != 9)
                {
                    //System.out.println(Math.abs(dangers.get(cand).d - dangers.get(now).d));
                    if (Math.abs(dangers.get(cand).d - dangers.get(now).d) < handler.dna.resemblance)
                    {
                        moreImportant = true;
                        if (Math.abs(dangers.get(now).obstacle.getMidPoint().x - Game.WIDTH / 2) < Math.abs(dangers.get(cand).obstacle.getMidPoint().x - Game.WIDTH / 2))
                        {
                            cand = now;
                        }
                        // break;
                    } else cand = now;
                }
                for (int nxt : M[now])
                {
                    if (visit.contains(nxt)) continue;
                    visit.add(nxt);
                    Q.add(nxt);
                }
            }/*
            System.out.print(dangers.get(M[cand].get(0)).d + "  ");*/

            /*if (moreImportant)
                dangers.get(M[cand].get(0)).d += 7000/Math.sqrt(Math.abs(dangers.get(M[cand].get(0)).d));
                //dangers.get(M[cand].get(0)).d += Math.pow(Math.abs(dangers.get(cand).d), 0.73);
            else
                dangers.get(M[cand].get(0)).d += 5000/Math.sqrt(Math.abs(dangers.get(M[cand].get(0)).d));

            System.out.println(dangers.get(M[cand].get(0)).d);
            del.add(dangers.get(cand));*/
            Data mostDanger = dangers.get(cand);
            double adding = 0;
            for (Data data : allinGroup)
            {
                if (mostDanger == data)
                {
                    continue;
                }
                mostDanger.d += handler.dna.node_erase / Math.sqrt(Math.abs(data.d));
                del.add(data);
            }
        }
        for (Data d : del)
        {
            d.obstacle.image = GameManager.loadImage("obstacle2.png");
            dangers.remove(d);
        }
////////////////////////////////////

        Collections.sort(dangers, (i, j) -> -Double.compare(i.d, j.d));
        //System.out.println("Max : " + dangers.get(0).d);
        for (Data data : dangers)
        {

            if (!(data.obstacle.x > player.x + player.WIDTH || player.x > data.obstacle.x + data.obstacle.WIDTH))
            {
                if (playerMid.x <= 30) rightVal += Math.abs(data.d * w);
                else if (playerMid.x > Game.WIDTH - 30) leftVal += Math.abs(data.d * w);
                else if (playerMid.x < data.obstacle.getMidPoint().x) leftVal += Math.abs(data.d * w);
                else rightVal += Math.abs(data.d * w);
            } else
            {
                if (playerMid.x < data.obstacle.getMidPoint().x) leftVal += Math.abs(data.d * w);
                else rightVal += Math.abs(data.d * w);
            }
            w = w * handler.dna.decrease_rate;
        }

        if (leftVal > rightVal)
        {
            KeyInput.PressKey(KeyEvent.VK_LEFT);
        } else if (rightVal > leftVal)
        {
            KeyInput.PressKey(KeyEvent.VK_RIGHT);
        }
    }

    void moveToward(double nowX, double targetX)
    {
        if (nowX < targetX) KeyInput.PressKey(KeyEvent.VK_RIGHT);
        else KeyInput.PressKey(KeyEvent.VK_LEFT);
    }
}
