package game;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;


public class Player implements Drawable
{
	public Player()
	{
		m_rs.setSize(new Vector2f(30, 50));
		m_rs.setFillColor(new Color(230, 200, 150));
		m_rs.setPosition(Main.wnd.getSize().x / 2.f, Main.wnd.getSize().y / 2.f);
	}
	
	public void logic()
	{
		
	}
	
	public void draw()
	
	private RectangleShape m_rs = new RectangleShape();
}
