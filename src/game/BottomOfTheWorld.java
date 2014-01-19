package game;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

/*
 * This represents the bottom of the world,
 * it will be a chunk that is re-generated if the coordinates are accessed.
 * We can implement this using tiles at a random position, connected,
 * below a certain x-line.
 */

public class BottomOfTheWorld implements Drawable
{
	private RectangleShape m_bottom = null;
	
	public BottomOfTheWorld()
	{
		m_bottom = new RectangleShape();
		m_bottom.setSize(new Vector2f(3000, 300));
		m_bottom.setPosition(new Vector2f(-200, Main.BOTTOM_OF_THE_WORLD));
		m_bottom.setFillColor(new Color(90, 90, 90));
	}
	
	public void draw(RenderTarget t, RenderStates s)
	{
		t.draw(m_bottom);
	}
}
