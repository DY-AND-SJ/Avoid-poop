import java.awt.*;

public class Circle extends Collision
{
    public Point mid;
    public double r;
    public Circle(int x, int y, int r)
    {
        mid = new Point(x, y);
        this.r = r;
    }
}