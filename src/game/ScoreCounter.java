package game;


import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;

import engine.Absolute;
import engine.PathedFonts;


/**
 * This class is a primitive version of the score counter.
 * @author Kevin
 * 
 * SCHEDULED FOR DELETION - Thormod will probably make a better UI (HUD)
 *
 */
public class ScoreCounter extends Absolute implements Drawable
{
	
	private Font m_font;
	private Text m_text = new Text();
	private long m_score = 0;
	
	
	public ScoreCounter()
	{
		try {
			m_font = PathedFonts.getFont(Paths.get("res/pixelmix.ttf"));
		} catch (IOException exc_obj) {
			exc_obj.printStackTrace();
		}
		m_text.setFont(m_font);
		m_text.setString("Derp");
	}
	
	public void addScore(long n)
	{
		m_score += n;
	}
	
	public void update()
	{
		m_text.setString ( String.valueOf(m_score) );
	}
	
	public void draw(RenderTarget target, RenderStates states)
	{
		draw(target, m_text);
	}
}
