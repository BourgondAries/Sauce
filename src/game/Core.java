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
		m_items.add(m_rockceiling);
		
		m_bedrock.generateTiles();
		
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
						case RETURN:
							m_bedrock.eraseRandomTileAtTheTop();
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
			m_player.getQueryMovement().setX(-7.f);
		}
		else if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT))
		{
			m_player.getQueryMovement().setX(7.f);
		}
	}
	
	private void runGameLogic()
	{
		if (Math.random() > 0.95)
			m_bedrock.eraseRandomTileAtTheTop();
		
		m_rs.move(new Vector2f(0.f, 1.f));
		System.out.println(m_player);
		Vector2f tmp = new Vector2f(m_player.getPosition().x + 33, m_player.getPosition().y);
		m_bedrock.getCollisionTilePosition(m_player.getPosition());
		if (m_bedrock.doesATileExistHere(m_player.getPosition()) && m_bedrock.doesATileExistHere(tmp))
			m_player.getQueryMovement().setY(m_player.getQueryMovement().getY() + 0.5f);
		else
			m_player.getQueryMovement().setY(0);
		
		// Perform movement additions. (Gravity changes the actual position
		m_player.logic();
		
		// If the new position is a collision, we must find the maximum allowed dX and dY
		if (m_player.getPosition().y + Player.CM_HEIGHT > m_bedrock.getCollisionTilePosition(m_player.getPosition()).y)
		{
			m_player.setPosition(new Vector2f(m_player.getPosition().x, m_bedrock.getCollisionTilePosition(m_player.getPosition()).y - Player.CM_HEIGHT));
		}
		Vector2f right_point_of_player = new Vector2f(m_player.getPosition().x + Player.CM_WIDTH, m_player.getPosition().y);
		if (right_point_of_player.y + Player.CM_HEIGHT > m_bedrock.getCollisionTilePosition(right_point_of_player).y)
			m_player.setPosition(new Vector2f(m_player.getPosition().x, m_bedrock.getCollisionTilePosition(right_point_of_player).y - Player.CM_HEIGHT));
		
		// Reset movement on the x-axis, because that mostly comes from user input
		m_player.getQueryMovement().setX(0.f);
		
		View v = Main.view;
		v = new View(m_player.getPosition(), Main.wnd.getDefaultView().getSize());
		v.move(m_rng.nextInt() % 2 - 1, m_rng.nextInt() % 2 - 1);
		Main.wnd.setView(v);
		
	}
	
	private void runCollisionTestsOnPlayer()
	{
		
	}
	
	private void render()
	{
//		Main.wnd.clear(new Color(m_rng.nextInt() % 255, m_rng.nextInt() % 255, m_rng.nextInt() % 255));
		Main.wnd.clear();
		
		for (Drawable x : m_items)
		{
			Main.wnd.draw(x);
		}
		
		Main.wnd.display();
	}
	
	private java.util.ArrayList<Drawable> m_items = new java.util.ArrayList<>();
	
	private RectangleShape m_rs = new RectangleShape();
	private Player m_player = new Player();
	private Random m_rng = new Random(System.currentTimeMillis());
	
	private BottomOfTheWorld m_bedrock = new BottomOfTheWorld();
	private MagmaOfTheWorld m_magma = new MagmaOfTheWorld();
	private RockCeiling m_rockceiling = new RockCeiling();

}
