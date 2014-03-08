package game;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Vector3f;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.audio.*;
import org.jsfml.window.event.*;

import ttl.Bool;
import engine.*;


public class Core 
{
	private final static int CM_RED_FLASH_FRAME_COUNT = 30;
	private final static int CM_RED_FLASH_RESET_NUMBER = 0;
	private final static int CM_INTEGER_COLOR_MAX = 255;
	
	private Layer m_layer;
	private Player m_player;
	private int m_damage_frames = 0;
	
	private BottomOfTheWorld m_bedrock;
	private InfiniteBox m_magma;
	private HUD m_heads_up_display;
	private Bool m_collision_with_bedrock = new Bool(false);
	
	private Random m_rng = new Random();
	
	static ArrayList<DynamicObject> physical_objects;
	
	public Core() throws IOException
	{
		m_player = new Player();
		m_player.setSize(new Vector2f(30, 30));
		m_player.setTexture(PathedTextures.getTexture(Paths.get("res/drop2.png")));
		m_player.setOrigin(5, 5);
		m_player.setPosition(Main.wnd.getSize().x / 2, Main.wnd.getSize().y);
		m_player.setMass(10.f);
		
		m_bedrock = new BottomOfTheWorld ( );
		physical_objects = new ArrayList<DynamicObject>();
		
		System.out.println("CORE\n");
		
		m_bedrock.generateTiles();
		
		m_layer = new Layer();
		m_layer.add(m_player);
		m_layer.add(m_bedrock);
		
		m_player.setPosition(new Vector2f(0.f, -100.f));
		
		m_heads_up_display = new HUD(Main.wnd);
		
		run();
	}
	
	public void run()
	{
		while (Main.game_state == Main.states.core)
		{
			handleEvents();
			runGameLogic();
			updateObjects();
			drawFrame();
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
				} break;
			default:
				break;
			}
		}
		
		if (Keyboard.isKeyPressed(Keyboard.Key.LEFT)) 
		{
			m_player.fetchSpeed().x -= 7.f;
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT)) 
		{
			m_player.fetchSpeed().x += 7.f;
		}
		else if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)) 
		{
			m_player.jump();
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.RETURN)) 
		{
			m_bedrock.eraseRandomTileAtTheTop();
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) 
		{
			Main.game_state = Main.states.menu;
		}
//		System.out.println(m_player);
//		System.out.println(m_bedrock);
	}
	
	private void runGameLogic()
	{
		// Effect of gravity on the player
		{
			m_player.fetchImpulse().y += 1.f;
		}
		
		// Re-position part of BottomOfTheWorld so that we have fake continuity
		{ 
			float xpos = m_player.getPosition().x;
			if ( xpos - Main.wnd.getSize().x < m_bedrock.getXBounds().first )
				m_bedrock.generateLeft();
			else if (xpos + Main.wnd.getSize().x > m_bedrock.getXBounds().second )
				m_bedrock.generateRight();
		}
	
		// This block removes a random tile at the top (per frame, with a chance of 1% per frame)
		{
			if (Math.random() > 0.99f) // random returns a float within [0, 1]
//				m_bedrock.eraseRandomTileAtTheTop();
				;
		}
		
		// This block sets the text for the HUD, currently quite inefficient
		{
			m_heads_up_display.setText
			(
				"Above bedrock column: " + m_bedrock.getTheTopOfTheTileLine((int) m_player.getPosition().x)
				+ "\nPlayer d^2y: " + m_player.getImpulse().y
				+ "\nPlayer dy: " + m_player.getSpeed().y
				+ "\nPlayer y: " + m_player.getPosition().y
				+ "\nCurrent bedrock column height: " + m_bedrock.getCollisionTilePosition((int) m_player.getPosition().x)
				+ "\nSecond bedrock column height: " + m_bedrock.getCollisionTilePosition((int) (m_player.getPosition().x + m_player.getSize().x))
				+ (!m_bedrock.doesATileExistHere(m_player.getPosition()) ? "\ncollision" : "\n")
				+ "Player health: " + m_player.getHealth()
			);
		}
		
		// This block checks for collision against bedrock, if it's true, the player is "bounced" upwards!
		// It also flashes the screen red 
		{
			// Check if player's bottom is below the top-most tile
			if 
			( 
				!m_bedrock.doesATileExistHere(new Vector2f(m_player.getPosition().x, m_player.getPosition().y + m_player.getSize().y)) 
				|| !m_bedrock.doesATileExistHere(new Vector2f(m_player.getPosition().x + m_player.getSize().x, m_player.getPosition().y + m_player.getSize().y)) 
			)
			{
				m_player.fetchImpulse().y += Player.CM_JUMPFORCE / 3.f;
				m_collision_with_bedrock.set(true);
				if ( m_damage_frames == CM_RED_FLASH_FRAME_COUNT)
					m_player.takeDamage();
			}
		}
	}

	private void updateObjects() 
	{
		m_player.update();
		m_player.fetchSpeed().x = 0.f;
		
		setViewToPlayer();
	}
	
	private void setViewToPlayer()
	{
		View v = Main.view;
		v = new View(m_player.getPosition(), Main.wnd.getDefaultView().getSize());
		
		v.move(m_rng.nextInt() % 2 - 1, m_rng.nextInt() % 2 - 1);
		Main.wnd.setView(v);
	}
	
	
	
	private void drawFrame ( )
	{
		Main.wnd.clear();
		Main.wnd.draw(m_layer);
		Main.wnd.draw(m_bedrock);
		Main.wnd.draw(m_heads_up_display);
		
		// Red flashing when we have had collision with bedrock!
		if ( m_collision_with_bedrock.fetchAndDisable() )
			m_damage_frames = CM_RED_FLASH_RESET_NUMBER;
		if ( m_damage_frames < CM_RED_FLASH_FRAME_COUNT )
		{
			RectangleShape rs = new RectangleShape();
			rs.setSize(new Vector2f(Main.wnd.getSize()));
			// A little dark magic:
			rs.setFillColor
			(
				new Color
				(
					CM_INTEGER_COLOR_MAX,
					0, 
					0, 
					CM_INTEGER_COLOR_MAX - ( (int) ((((float) m_damage_frames) / ((float) CM_RED_FLASH_FRAME_COUNT)) * ((float) CM_INTEGER_COLOR_MAX ))) )
			);
			ConstView view = Main.wnd.getView();
			Main.wnd.setView(Main.wnd.getDefaultView());
			Main.wnd.draw(rs);
			Main.wnd.setView(view);
			++m_damage_frames;
		}
		Main.wnd.display();
	}
}
