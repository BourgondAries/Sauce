package game;

import java.io.IOException;
import java.nio.file.*;

import engine.*;
import ttl.*;

import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

public class Shaft
{
	private Layer 
		m_walls = new Layer(),
		m_game_elements = new Layer();
	private LayerCollection m_layers = new LayerCollection();
	private DynamicObject 
		m_wall_left = new DynamicObject(),
		m_wall_right = new DynamicObject();
	private Player
		m_ship = new Player();
	private java.util.ArrayList<DynamicObject>
		m_wall_objects = new java.util.ArrayList<>();
		
	private Bool 
		m_collision = new Bool(false);
	
	private final int 
		CM_BORDER_SIZE;
		
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
		
		m_game_elements.add(m_ship);
		
		m_layers.add(m_walls, 1);
		m_layers.add(m_game_elements, 0);
		
		CM_BORDER_SIZE = (int) m_wall_left.getSize().x;
		
		initShip();
		
		run();
	}
	
	private void initShip()
	{
		m_ship.setSize(new Vector2f(30, 30));
		try 
		{
			m_ship.setTexture(PathedTextures.getTexture(Paths.get("res/drop2.png")));
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
		m_ship.setOrigin(m_ship.getSize().x / 2, m_ship.getSize().y / 2);
		m_ship.setPosition(Main.wnd.getSize().x / 2.1f, Main.wnd.getSize().y / 1.5f);
		m_ship.rotate(180.f);
		m_ship.setMass(10.f);
	}
	
	private void run()
	{
		do
		{
			handleEvents();
			runGameLogic();
			updateObjects();
			drawFrame();
		}
		while ( Main.game_state == Main.states.shaft );
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
							Main.game_state = Main.states.menu;
							return;
						case E:
							m_ship.fetchImpulse().z += 1.f;
							break;
						case Q:
							m_ship.fetchImpulse().z -= 1.f;
						default:
							break;
					
					}
				} break;
				case KEY_RELEASED:
				{
					KeyEvent keyev = event.asKeyEvent();
					switch (keyev.key)
					{
						default:
							break;
					}
				} break;
			default:
				break;
			}
		}
		
		if (Keyboard.isKeyPressed(Keyboard.Key.UP)) 
		{
			m_ship.move(new Vector2f(0.f, -10.f));
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.DOWN)) 
		{
			m_ship.move(new Vector2f(0.f, 10.f));
		}
		if (Keyboard.isKeyPressed(Keyboard.Key.LEFT)) 
		{
			m_ship.move(new Vector2f(-10.f, 0.f));
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT)) 
		{
			m_ship.move(new Vector2f(10.f, 0.f));
		}
		else if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)) 
		{
			m_ship.jump();
		}
		else if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) 
		{
			Main.game_state = Main.states.menu;
		}
	}
	
	private void runGameLogic()
	{
		for (DynamicObject x : m_wall_objects)
		{
			x.fetchSpeed().y = 100.3f;
			if (x.getPosition().y - x.getOrigin().y > 0)
			{
				x.setPosition(x.getPosition().x, 0);
			}
		}
		
		// Ship logic:
		/*
		 * If the ship is atop of the screen, it is prohibited from moving any further.
		 * The same goes for the bottom. There is a slight padding so that the pixels do not collide!
		 * 
		 * If we go into the sides, we activate the ridging system and take damage.
		 */
		{
			if (m_ship.getPosition().y - m_ship.getSize().y < 0)
			{
				m_ship.setPosition(new Vector2f(m_ship.getPosition().x, m_ship.getSize().y));
			}
			else if (m_ship.getPosition().y + m_ship.getSize().y > Main.wnd.getSize().y)
			{
				m_ship.setPosition(new Vector2f(m_ship.getPosition().x, Main.wnd.getSize().y - m_ship.getSize().y));
			}
			
			// Second part: collision with the walls (causes damage)
			if (m_ship.getPosition().x < CM_BORDER_SIZE)
			{
				m_collision.set(true);
				m_ship.setPosition(m_ship.getPosition().x + m_ship.getSize().x, m_ship.getPosition().y);
			}
			else if (m_ship.getPosition().x > Main.wnd.getSize().x - CM_BORDER_SIZE)
			{
				m_collision.set(true);
				m_ship.setPosition(m_ship.getPosition().x - m_ship.getSize().x, m_ship.getPosition().y);
			}
		}
	}
	
	private void updateObjects()
	{
		for (DynamicObject x : m_wall_objects)
		{
			x.update();
		}
		
		m_ship.update();
		
	}
	
	private void drawFrame()
	{
		if (m_collision.fetchAndDisable())
			Main.wnd.clear(new Color(255, 0, 0, 127));
		else
			Main.wnd.clear();
		Main.wnd.draw(m_layers);
		Main.wnd.display();
	}
	
}
