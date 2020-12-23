import java.awt.Graphics;

public interface Nav {

    public void move();

    public void restrict(Direction d);

    public void draw(Graphics g);

}
