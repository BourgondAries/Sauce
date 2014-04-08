package game;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import engine.*;

public class ProgressBar extends Absolute implements Drawable
{
	private RectangleShape 
		m_border, 
		m_fill;
	
	private Layer
		m_layer = new Layer();
	
	public ProgressBar ()
	{
		super.setAbsoluteView(Main.wnd.getDefaultView());
		
		m_border = new RectangleShape();
		m_fill = new RectangleShape();
		
		m_border.setOutlineColor(Color.BLUE);
		m_border.setOutlineThickness(3);
		m_border.setFillColor(Color.TRANSPARENT); // top kek, transparent "color"
		m_border.setSize(new Vector2f(Main.wnd.getSize().x, 20));
		m_border.setPosition(new Vector2f(0, Main.wnd.getSize().y - m_border.getGlobalBounds().height));
		
		m_fill.setFillColor(new Color(218, 165, 32));
		m_fill.setPosition(m_border.getPosition());
		m_fill.setSize(new Vector2f(0, m_border.getSize().y));
		
		m_layer.add(m_border);
		m_layer.add(m_fill);
	}
	
	public void update(int progress, int maxprogress)
	{
		m_fill.setSize(new Vector2f(((float) progress)/((float)maxprogress) * m_border.getSize().x, m_fill.getSize().y));
	}
	
	public void draw ( RenderTarget target, RenderStates states )
	{
		super.draw(target, m_layer);
	}
	
	
}
