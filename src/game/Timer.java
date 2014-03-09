package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import engine.PathedFonts;


/**
 * 
 * The Timer is a part of the CORE gameplay; it's a rather large, maplestory
 * -party-quest like timer (google it: MapleStory Ludibrium PQ, *nostalgia*).
 * 
 * The timer is in min:sec, and shows the time until the core becomes so unstable
 * that it blows up! We will implement a "resurface" button around 1 minute
 * before the core blows up.
 * 
 * We can give the player 5 minutes of playing time inside core, dodging dangerous
 * particles, gathering "malm", dogding geysers,...
 * 
 * The last minute will be devoted to choosing to stay just 1 sec long or to
 * GET THE HELL OUT OF HERE :D
 * 
 * @author Kevin Robert Stravers
 *
 */
public class Timer implements Drawable
{
	private RectangleShape		m_box;
	private Text 				m_text;
	private final long 			m_posix_time_at_start;
	private static final long 	CM_TIME_DURATION_IN_MS = 1000 * 60;
	private ConstView 			m_view;
	
	public Timer ( RenderWindow window )
	{
		m_view = window.getDefaultView();
		try {
			m_text = new Text("XXAXA", PathedFonts.getFont(Paths.get("res/pixelmix.ttf")), 30);
		} catch (IOException e) {
			e.printStackTrace();
		}
		m_box = new RectangleShape();
		m_box.setSize(new Vector2f(200, 100));
		m_box.setFillColor(new Color(100, 0, 0, 127));
		m_box.setOutlineThickness(2);
		m_box.setOutlineColor(new Color(250, 250, 50));
		m_box.setPosition(Main.wnd.getSize().x / 2 - m_box.getSize().x / 2, 10.f);
		m_text.setPosition(Main.wnd.getSize().x / 2 - m_box.getSize().x / 2 + 40, 40.f);
		m_posix_time_at_start = System.currentTimeMillis();
		update();
	}
	
	// Update the timer object
	public void update()
	{
		long timeleft = m_posix_time_at_start - System.currentTimeMillis() +  CM_TIME_DURATION_IN_MS;
		m_text.setString(String.valueOf(timeleft));
	}
	
	public void draw ( RenderTarget target, RenderStates states )
	{
		ConstView oldview = target.getView();
		target.setView(m_view);
		target.draw(m_box);
		target.draw(m_text);
		target.setView(oldview);
	}
	
}
