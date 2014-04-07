package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import engine.*;


/**
 * 
 * @author Kevin R. Stravers
 * This class represents a so-called "Heads-Up-Display" (HUD).
 * The HUD is used to show data to the player; data that is not actually used inside the 
 * game itself (nothing collides with it, etc.). Currently, this implementation
 * simply draws a single string from the origin of the view (top left).
 * 
 * USE \n FOR NEW LINES!
 *
 */
public class HUD extends Absolute implements Drawable
{
	private RectangleShape[]
		m_health = new RectangleShape[Player.getMaxHealth()];
	
	private Font m_drawing_font;
	private Text m_display_text;
	private boolean m_active = true;
	
	private Layer
		m_layer = new Layer();
	
	private Texture
		m_good_health,
		m_bad_health;
	
	private int 
		healthy = m_health.length;
	
	public HUD (RenderWindow x)
	{
		try
		{
			m_good_health = PathedTextures.getTexture(Paths.get("res/hud/hud_gear_whole.tga"));
			m_bad_health = PathedTextures.getTexture(Paths.get("res/hud/hud_gear_broken.tga"));
		}
		catch (IOException exc_obj)
		{
			exc_obj.printStackTrace();
		}
			
		for (int i = 0; i < m_health.length; ++i)
		{
			m_health[i] = new RectangleShape();
			m_health[i].setSize(new Vector2f(m_good_health.getSize()));
			m_health[i].setTexture(m_good_health);
			m_health[i].setPosition(Main.wnd.getSize().x / 1.3f - (m_health.length  - i) * m_good_health.getSize().x, Main.wnd.getSize().y / 25);
			
			m_layer.add(m_health[i]);
		}
		
		super.setAbsoluteView(x.getDefaultView());
		try 
		{
			m_drawing_font = PathedFonts.getFont(Paths.get("res/pixelmix.ttf"));
			m_display_text = new Text("DERP", m_drawing_font, 12);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
//		m_layer.add(m_display_text);
	}
	
	public void updateHealth(int health)
	{
		if (healthy > health)
		{
			m_health[healthy - 1].setTexture(m_bad_health);
			healthy = health;
		}
	}
	
	public void setActive(boolean state)
	{
		m_active = state;
	}
	
	public boolean getActive()
	{
		return m_active;
	}
	
	public void setText ( String argument )
	{
		m_display_text.setString(argument);
	}

	public void draw(RenderTarget target, RenderStates states) 
	{
		if (m_active)
		{
			super.draw(target, m_layer);
		}
	}
}
