import javax.swing.*;
import java.awt.*;

public class Window
{
    public JFrame frame;
    public Window(int width, int height, Game game)
    {
        frame = new JFrame("new Running Deluxe addition");

        game.setPreferredSize(new Dimension(width, height));
        game.setMaximumSize(new Dimension(width, height));
        game.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.setFocusable(true);
        game.requestFocus();

    }
}
