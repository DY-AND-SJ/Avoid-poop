import java.awt.*;

public class Collision
{
    public static boolean isPointInCircle(double px, double py, double circleX, double circleY, double R)
    {
        double dist = Math.sqrt((px - circleX) * (px - circleX) + (py - circleY) * (py - circleY));
        if (dist <= R) return true;
        return false;
    }
    public static boolean CircleIntersection(Circle c1, Circle c2)
    {
        if (c1.mid.distance(c2.mid) < c1.r+c2.r) return true;
        return false;
    }
    public static boolean circleRectangleIntersection(double x, double y, double range, Rectangle rec)
    {
        if (rec.x <= x && x <= rec.x + rec.width || rec.y <= y && y <= rec.y + rec.height)
        {
            Rectangle ex = new Rectangle(rec.x, rec.y, rec.width, rec.height);
            ex.grow((int)range, (int) range);
            //ex.x -= range;
            //ex.y -= range;
            if (ex.contains(x, y)) return true;
        } else
        {
            int dx[] = {0, rec.width, 0, rec.width};
            int dy[] = {rec.height, 0, 0, rec.height};
            for (int i = 0; i < 4; ++i)
                if (isPointInCircle(rec.x + dx[i], rec.y + dy[i], x, y, range)) return true;
        }
        return false;
    }


    public boolean intersects(Collision c)
    {
        if (this instanceof Circle)
        {
            Circle my = (Circle)this;
            if (c instanceof Circle)
            {
                return CircleIntersection(((Circle) this), (Circle)c);
            }
            else if (c instanceof Rect)
            {
                return circleRectangleIntersection(my.mid.x, my.mid.y, my.r, ((Rect) c).rect);
            }
        }
        else if (this instanceof Rect)
        {
            Rect my = (Rect) this;
            if (c instanceof Circle)
            {
                return circleRectangleIntersection(((Circle)c).mid.x, ((Circle)c).mid.y, ((Circle)c).r, my.rect);
            }
            else if (c instanceof Rect)
            {
                return my.rect.intersects(((Rect) c).rect);
            }
        }
        return false;
    }

}
