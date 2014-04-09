package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

import org.jsfml.audio.Music;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import engine.Layer;
import engine.PathedTextures;


public class PostShaftCinematic 
{
	private TransmittableData m_data;
	private RectangleShape 
		m_left_side = new RectangleShape(),
		m_right_side = new RectangleShape(),
		m_background = new RectangleShape();
	private long 
		m_vibrate = 3000,
		m_start_time = 0;
	
	private boolean
		m_explode = false;
	
	private Layer 
		m_earth = new Layer();
	
	private Music
		m_explosion = new Music();
	
	private Random m_rng = new Random();
	
	public PostShaftCinematic(TransmittableData data) throws IOException
	{
		
		Texture tex = PathedTextures.getTexture(Paths.get("res/end/end_1.tga"));
		m_right_side.setSize(new Vector2f(tex.getSize()));
		m_right_side.setTexture(tex);
		m_right_side.setPosition(Main.wnd.getSize().x / 2, 0);
		
		tex = PathedTextures.getTexture(Paths.get("res/end/end_2.tga"));
		m_left_side.setSize(new Vector2f(tex.getSize()));
		m_left_side.setTexture(tex);
		m_left_side.setPosition(-Main.wnd.getSize().x / 2, 0);
		
		tex = PathedTextures.getTexture(Paths.get("res/end/end_3.tga"));
		m_background.setSize(new Vector2f(tex.getSize()));
		m_background.setTexture(tex);
		m_background.setPosition(0, 0);
		
		m_earth.add(m_background);
		m_earth.add(m_left_side);
		m_earth.add(m_right_side);
		
		m_data = data;
		m_data.ship.setPosition(new Vector2f(Main.wnd.getSize().x / 2, Main.wnd.getSize().y * 2));
		initShip();
		run();
	}
	
	private void initShip()
	{
		m_data.ship.setSize(new Vector2f(30, 30));
		try 
		{
			m_data.ship.setTexture(PathedTextures.getTexture(Paths.get("res/drop2.png")));
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
		m_data.ship.setOrigin(m_data.ship.getSize().x / 2, m_data.ship.getSize().y / 2);
//		m_data.ship.rotate(180.f);
		m_data.ship.setMass(10.f);
		
	}
	
	public void run() throws IOException
	{
		do
		{
			handleEvents();
			runLogic();
			update();
			drawFrame();
		}
		while (Main.game_state == Main.states.postshaft);
		Main.wnd.setView(Main.wnd.getDefaultView());
		m_explosion.stop();
	}
	
	private void handleEvents()
	{
		for (Event event : Main.wnd.pollEvents())
		{
			switch (event.type)
			{
				case KEY_PRESSED:
				{
					KeyEvent keyev = event.asKeyEvent();
					switch (keyev.key)
					{
						case ESCAPE:
							Main.game_state = Main.states.enterscore;
							return;
						default:
							break;
					}
				} break;
				default:
					break;
			}
		}
	}
	
	private void runLogic()
	{
		m_data.ship.fetchSpeed().y = -15.f;
	}
	
	private void update() throws IOException
	{
		m_data.ship.update();
		
		if (m_explode == false && m_data.ship.getPosition().y < -m_data.ship.getSize().y)
		{
			m_start_time = System.currentTimeMillis();
			
			m_explosion.openFromFile(Paths.get("sfx/teleport_close.ogg"));
			m_explosion.play();
			
			m_explode = true;
		}
		if (m_explode)
		{
			updateView();
			if (isDone())
				Main.game_state = Main.states.enterscore;
	
		}
	}
	
	private boolean isDone()
	{
		return System.currentTimeMillis() - m_start_time >= m_vibrate;
	}
	
	private void updateView()
	{
		View v = Main.view;
		v = new View();
		
		v.setCenter( Main.wnd.getDefaultView().getCenter());
		v.setSize( Main.wnd.getDefaultView().getSize());
		v.setViewport( Main.wnd.getDefaultView().getViewport());
		
		// Scramble the view (Give the vibrating illusion), also topkek, magic numbers
		long proximity = -(m_start_time - System.currentTimeMillis());
		long maxtime = m_vibrate;
		long inverse = maxtime - proximity;
		float divergence = 10.f * ((float) inverse) / ((float) maxtime);
		v.move(m_rng.nextInt() % divergence - divergence / 2.f, m_rng.nextInt() % divergence - (proximity / 20.f) * divergence / 2.f);
		Main.wnd.setView(v);
	}
	
	private void fade(long start_time2, float time_to_dig2)
	{
		RectangleShape rs = new RectangleShape();
		rs.setSize(new Vector2f(Main.wnd.getSize().x, Main.wnd.getSize().y));
		rs.setFillColor
		(
			new Color
			(
				255,
				255, 
				255, 
				(int) (((float) 255.f * (System.currentTimeMillis()-start_time2)) / ((float)time_to_dig2))
			)
		);
//		rs.setPosition(new Vector2f(-Main.wnd.getSize().x, -Main.wnd.getSize().y));
		ConstView view = Main.wnd.getView();
		Main.wnd.setView(Main.wnd.getDefaultView());
		Main.wnd.draw(rs);
		Main.wnd.setView(view);
	}
	
	private void drawFrame()
	{
		Main.wnd.clear(new Color(189,230,255));
		
		Main.wnd.draw(m_earth);
		Main.wnd.draw(m_data.ship);
		
		if (m_explode)
		{
			fade(m_start_time, m_vibrate);
		}
		
		Main.wnd.display();
	}
	
	
}
