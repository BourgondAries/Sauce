package game;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

public class MagmaOfTheWorld implements Drawable
{
	private RectangleShape m_rs = new RectangleShape();
	
	public MagmaOfTheWorld()
	{
		m_rs.setSize(new Vector2f(10000, 1000));
		m_rs.setFillColor(new Color(255, 140, 0, 127));
		m_rs.setPosition(-200, Main.START_OF_MAGMA);
	}
	
	public void draw(RenderTarget t, RenderStates s)
	{
		t.draw(m_rs);
	}
}
