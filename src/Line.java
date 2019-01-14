import java.awt.*;

public class Line extends GameObject
{


    public Line(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.color = Color.red;
    }

    @Override
    public void tick(double frameTime)
    {
    }


}
