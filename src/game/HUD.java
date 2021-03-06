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
	private boolean m_active = false;
	
	private Layer
		m_layer = new Layer();
	
	
	private Texture
		m_good_health,
		m_bad_health,
		m_hud_under,
		m_hud_over;
	
	private int 
		healthy = m_health.length;
	
	public HUD (RenderWindow x)
	{
		try
		{
			m_good_health = PathedTextures.getTexture(Paths.get("res/hud/hud_gear_whole.tga"));
			m_bad_health = PathedTextures.getTexture(Paths.get("res/hud/hud_gear_broken.tga"));
			m_hud_under = PathedTextures.getTexture(Paths.get("res/hud/hud_bottom.tga"));
			m_hud_over = PathedTextures.getTexture(Paths.get("res/hud/hud_top.tga"));
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
		
		{
			Sprite hud_under = new Sprite(m_hud_under);
			hud_under.setPosition(0, Main.wnd.getSize().y);
			m_layer.add(hud_under);
		}
		
		{
			Sprite hud_over = new Sprite(m_hud_over);
			hud_over.setPosition(0, Main.wnd.getSize().y);
			m_layer.add(hud_over);
		}
		
		super.setAbsoluteView(x.getDefaultView());
		try 
		{
			m_drawing_font = PathedFonts.getFont(Paths.get("res/pixelmix.ttf"));
			m_display_text = new Text("DERP", m_drawing_font, 10);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//m_layer.add(m_display_text);
	}
	
	public void updateHealth(int health)
	{
		if (healthy > health)
		{
			m_health[healthy - 1].setTexture(m_bad_health);
			--healthy;
		}
		else if (healthy < health)
		{
			m_health[healthy].setTexture(m_good_health);
			++healthy;
		}
	}
	
	public void setDebugState(boolean state)
	{
		if (m_active == false && state)
		{
			m_layer.add(m_display_text);
		}
		else if (m_active == true && state == false)
		{
			m_layer.remove(m_display_text);
		}
		m_active = state;
	}
	
	public boolean getDebugState()
	{
		return m_active;
	}
	
	public void setText ( String argument )
	{
		m_display_text.setString(argument);
	}

	public void draw(RenderTarget target, RenderStates states) 
	{
		super.draw(target, m_layer);
	}
}
