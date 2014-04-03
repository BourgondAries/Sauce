package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;

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
	private Font m_drawing_font;
	private Text m_display_text;
	private boolean m_active = true;
	
	public HUD (RenderWindow x)
	{
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
			super.draw(target, m_display_text);
		}
	}
}
