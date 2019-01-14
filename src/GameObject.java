
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class GameObject implements Comparable
{

    public Handler handler;
    protected double x;
    protected double y;
    protected ID id;
    protected double velX;
    protected double velY;
    protected int renderOrder;
    protected BufferedImage image = null;
    protected Color color = Color.red;
    public int getWIDTH()
    {
        return WIDTH;
    }

    public int getHEIGHT()
    {
        return HEIGHT;
    }

    protected final int WIDTH;
    protected final int HEIGHT;

    public GameObject(double x, double y, BufferedImage image, ID id, Handler handler)
    {
        this(x, y, image.getWidth(), image.getHeight(), id, handler);
        this.image = image;
    }
    public GameObject(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(x, y, WIDTH, HEIGHT, id, handler, 50);
    }

    public GameObject(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        this.x = x;
        this.y = y;
        this.id = id;
        this.handler = handler;
        this.renderOrder = renderOrder;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    public abstract void tick(double frameTime);

    public void render(Graphics2D g2d)
    {
        g2d.setColor(color);
        Collision coll = getBounds();
        if (coll instanceof Rect)
            g2d.draw(((Rect) coll).rect);
        else if (coll instanceof Circle)
        {
            Circle cir = (Circle) coll;
            g2d.drawOval((int) (cir.mid.x - cir.r), (int) (cir.mid.y - cir.r), (int) cir.r * 2, (int) cir.r * 2);
        }
        if (this.image != null)
            g2d.drawImage(this.image, (int)x, (int)y, null);
    }

    public Collision getBounds()
    {
        return new Rect(new Rectangle((int) x, (int) y, WIDTH, HEIGHT));
    }
    public Point2D.Double getMidPoint()
    {
        return new Point2D.Double(x + WIDTH/2, y + HEIGHT/2);
    }
    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setId(ID id)
    {
        this.id = id;
    }

    public ID getId()
    {
        return id;
    }

    public void setVelX(double velX)
    {
        this.velX = velX;
    }

    public void setVelY(double velY)
    {
        this.velY = velY;
    }

    public double getVelX()
    {
        return velX;
    }

    public double getVelY()
    {
        return velY;
    }

    public int getRenderOrder()
    {
        return renderOrder;
    }

    public void setRenderOrder(int renderOrder)
    {
        this.renderOrder = renderOrder;
    }

    @Override
    public int compareTo(Object o)
    {
        GameObject tempObject = (GameObject) o;
        if (this.renderOrder == tempObject.renderOrder) return this.hashCode() - tempObject.hashCode();
        return this.renderOrder - tempObject.renderOrder;
    }
    public void destructor()
    {

    }
}
