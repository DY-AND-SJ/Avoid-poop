

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
public class Handler
{
    private TreeSet<GameObject> objects = new TreeSet<>();
    private Queue<GameObject> addList = new LinkedList<>();
    private Queue<GameObject> removeList = new LinkedList<>();
    private Game game;
    public Game getGame()
    {
        return game;
    }
    public Handler(Game game)
    {
        this.game =game;
        int w = Game.WIDTH/10;
        int h = Game.HEIGHT;
        for (int i=0;i<10;++i)
            addObject(new Line(w*i, 0, w, h, ID.Line, this, 50));
        BufferedImage playerImage = GameManager.loadImage("Player.png");
        Player p = new Player(Game.WIDTH/2 - playerImage.getWidth()/2,h - playerImage.getHeight(),playerImage,ID.Player,this);
        addObject(p);
        addObject(new Spownser(-100, -100, 0, 0, ID.Spowner, this, p));
        addObject(new GE(this, p));
    }
    public void tick(double frameTime)
    {
        objects.forEach(i->i.tick(frameTime));
        while (!addList.isEmpty()) objects.add(addList.poll());
        while (!removeList.isEmpty())
        {
            removeList.peek().destructor();
            objects.remove(removeList.poll());
        }
    }

    public void render(Graphics2D g2d)
    {
        objects.forEach(i -> i.render(g2d));
    }

    public void addObject(GameObject object)
    {
        this.addList.add(object);
    }

    public void removeObject(GameObject object)
    {
        this.removeList.add(object);
    }
    public GameObject findObjectById(ID id)
    {
        for (GameObject tempObject : objects)
        {
            if (tempObject.getId() == id)
            {
                return tempObject;
            }
        }
        return null;
    }
    public TreeSet<GameObject> findObjectsById(ID id)
    {
        TreeSet<GameObject> res = new TreeSet<>();
        for (GameObject tempObject : objects)
            if (tempObject.getId().equals(id)) res.add(tempObject);
        return res;
    }

    public void removeObjectsById(ID id)
    {
        objects.forEach(i ->
        {
            if (i.getId() == id) removeObject(i);
        });
    }

    public void clear()
    {
        removeList.addAll(objects);
        removeList.addAll(addList);
    }

    public int size()
    {
        return objects.size();
    }

    public HashSet<GameObject> Collide(GameObject object)
    {
        HashSet<GameObject> result = new HashSet<>();
        for (GameObject tempObject : objects)
            if (tempObject.getBounds().intersects(object.getBounds()) && tempObject != object)
            {
                result.add(tempObject);
            }
        return result;
    }
    public HashSet<GameObject> Collide(Collision bound)
    {
        HashSet<GameObject> result = new HashSet<>();
        for (GameObject tempObject : objects)
        {
            if (tempObject.getBounds().intersects(bound)) result.add(tempObject);
        }
        return result;
    }
    public TreeSet<GameObject> getObjects()
    {
        return objects;
    }
}
