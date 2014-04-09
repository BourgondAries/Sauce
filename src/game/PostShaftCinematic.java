package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import engine.Layer;
import engine.PathedTextures;


public class PostShaftCinematic 
{
	private TransmittableData m_data;
	private RectangleShape 
		m_left_side = new RectangleShape(),
		m_right_side = new RectangleShape(),
		m_background = new RectangleShape();
	
	private Layer 
		m_earth = new Layer();
	
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
		run();
	}
	
	public void run()
	{
		do
		{
			handleEvents();
			runLogic();
			update();
			drawFrame();
		}
		while (Main.game_state == Main.states.surface);
	}
	
	private void handleEvents()
	{
		// No need
	}
	
	private void runLogic()
	{
		m_data.ship.fetchSpeed().y = -100.f;
	}
	
	private void update()
	{
		m_data.ship.update();
	}
	
	private void drawFrame()
	{
		Main.wnd.clear(new Color(189,230,255));
		
		Main.wnd.draw(m_earth);
		Main.wnd.draw(m_data.ship);
		
		Main.wnd.display();
	}
	
	
}
