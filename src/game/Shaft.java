package game;

import java.io.IOException;
import java.nio.file.*;

import engine.*;
import org.jsfml.graphics.*;
import org.jsfml.system.*;

public class Shaft
{
	private Layer m_walls = new Layer();
	private DynamicObject 
		m_wall_left = new DynamicObject(),
		m_wall_right = new DynamicObject();
	private java.util.ArrayList<DynamicObject>
		m_wall_objects = new java.util.ArrayList<>();
	
	public Shaft()
	{
		try {
			Texture tex = PathedTextures.getTexture(Paths.get("res/shaft/wall.tga"));
			m_wall_left.setSize(new Vector2f(tex.getSize()));
			m_wall_left.setTexture(tex);
			m_wall_right.setSize(m_wall_left.getSize());
			m_wall_right.setTexture(tex);
		} catch (IOException exc_obj) {
			exc_obj.printStackTrace();
		}
		m_wall_left.setOriginToMiddle();
		m_wall_right.setOriginToMiddle();
		m_wall_right.setRotation(180.f);
		m_wall_right.setPosition(Main.wnd.getSize().x - m_wall_right.getSize().x / 2, 0);
		m_wall_objects.add(m_wall_left);
		m_wall_objects.add(m_wall_right);
		m_wall_left.setPosition(new Vector2f(m_wall_left.getSize().x / 2, 0));
		
		m_walls.add(m_wall_left);
		m_walls.add(m_wall_right);
		run();
	}
	
	public void run()
	{
		do
		{
			handleEvents();
			runGameLogic();
			updateObjects();
			drawFrame();
			
//			Main.wnd.close();
//			Main.game_state = Main.states.exit;
		}
		while ( Main.game_state == Main.states.shaft );
	}
	
	private void handleEvents()
	{
		
	}
	
	private void runGameLogic()
	{
		for (DynamicObject x : m_wall_objects)
		{
			x.fetchSpeed().y = -103.3f;
			if (x.getPosition().y + x.getSize().y < Main.wnd.getSize().y)
			{
				x.setPosition(x.getPosition().x, x.getSize().y * 2);
			}
		}
	}
	
	private void updateObjects()
	{
		for (DynamicObject x : m_wall_objects)
		{
			x.update();
		}
		
	}
	
	private void drawFrame()
	{
		Main.wnd.clear();
		Main.wnd.draw(m_walls);
		Main.wnd.display();
	}
	
}
