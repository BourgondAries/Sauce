package game;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.audio.*;
import org.jsfml.window.event.*;

import engine.Player;

import java.util.ArrayList;
import java.util.Random;


public class Core
{
	
	public Core()
	{
		System.out.println("CORE\n");
		m_rs.setFillColor(new Color(200, 200, 200));
		m_rs.setSize(new Vector2f(10, 10));
		
		m_items.add(m_rs);
		m_items.add(m_player);
		m_items.add(m_bedrock);
		m_items.add(m_magma);
		
		run();
	}
	
	public void run()
	{
		while (Main.game_state == Main.states.core)
		{
			handleEvents();
			runGameLogic();
			render();
		}
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
						case UP:
							m_player.jump();
							break;
						case ESCAPE:
							Main.game_state = Main.states.menu;
							return;
						default:
							break;
					
					}
					System.out.println(keyev.key);
					m_rs.move(new Vector2f(0.f, -1.f));
				} break;
				case CLOSED:
				{
//					Main.game_state = Main.states.menu;
					Main.dispose();
					return;
				}
				default:
					break;
			}
		}
		
		
		if (Keyboard.isKeyPressed(Keyboard.Key.LEFT))
		{
			m_player.move(-7.f, 0.f);
		}
		else if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT))
		{
			m_player.move(7.f, 0.f);
		}
	}
	
	private void runGameLogic()
	{
		m_rs.move(new Vector2f(0.f, 1.f));
		m_player.logic();
		
		View v = Main.view;
		v = new View(m_player.getPosition(), Main.wnd.getDefaultView().getSize());
		v.move(m_rng.nextInt() % 300 - 150, m_rng.nextInt() % 300 - 150);
		Main.wnd.setView(v);
		
	}
	
	private void render()
	{
		Main.wnd.clear(new Color(m_rng.nextInt() % 255, m_rng.nextInt() % 255, m_rng.nextInt() % 255));
		
		for (Drawable x : m_items)
		{
			Main.wnd.draw(x);
		}
		
		Main.wnd.display();
	}
	
	private java.util.ArrayList<Drawable> m_items = new java.util.ArrayList<>();
	private final float m_gravity = -9.81f;
	
	private RectangleShape m_rs = new RectangleShape();
	private Player m_player = new Player();
	private Random m_rng = new Random(System.currentTimeMillis());
	
	private BottomOfTheWorld m_bedrock = new BottomOfTheWorld();
	private MagmaOfTheWorld m_magma = new MagmaOfTheWorld();

}
