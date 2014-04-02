package game;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;


import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.*;
import org.jsfml.window.event.*;

import ttl.Bool;
import engine.*;


public class Core 
{
	// Constants
	private final static int CM_RED_FLASH_FRAME_COUNT = 30;
	private final static int CM_RED_FLASH_RESET_NUMBER = 0;
	private final static int CM_INTEGER_COLOR_MAX = 255;
	
	private LayerCollection m_layers = new LayerCollection();
	private Player 			m_player;
	private int 			m_damage_frames = CM_RED_FLASH_FRAME_COUNT;
	
	private InfiniteBox 		m_lava;
	private BottomOfTheWorld 	m_bedrock;
	private HUD 				m_heads_up_display;
	private Bool 				m_collision_with_bedrock = new Bool(false);
	private Timer 				m_timer = new Timer(Main.wnd);
	
	private Random m_rng = new Random();
	
	private ArrayList<DynamicObject> m_malm_objects;
	
	
	public Core() throws IOException
	{
		m_player = new Player();
		m_player.setSize(new Vector2f(30, 30));
		m_player.setTexture(PathedTextures.getTexture(Paths.get("res/drop2.png")));
		m_player.setOrigin(5, 5);
		m_player.setPosition(Main.wnd.getSize().x / 2, Main.wnd.getSize().y);
		m_player.setMass(10.f);
		
		m_bedrock = new BottomOfTheWorld ( );
		m_malm_objects = new ArrayList<DynamicObject>();
		
		System.out.println("CORE\n");
		
		m_bedrock.generateTiles();
		
		m_lava = new InfiniteBox();
		m_lava.setSize( new Vector2f(Main.wnd.getSize().x * 2, BottomOfTheWorld.getTileCountY()*BottomOfTheWorld.getTileHeight()) );
		m_lava.setPosition(-Main.wnd.getSize().x, -Main.wnd.getSize().y + BottomOfTheWorld.getTileCountY()*BottomOfTheWorld.getTileHeight());
		m_lava.setFillColor(new Color(255, 165, 0, 127));
		
		m_player.setPosition(new Vector2f(0.f, -100.f));
		m_player.setOrigin(m_player.getSize().x / 2, m_player.getSize().y / 2);
		
		m_heads_up_display = new HUD(Main.wnd);
		
		Layer player_layer = new Layer();
		player_layer.add(m_player);
		player_layer.add(m_bedrock);
		player_layer.add(m_lava);
		
		Layer information_layer = new Layer();
		information_layer.add(m_heads_up_display);
		information_layer.add(m_timer);
		
		m_layers.add(player_layer, 0);
		m_layers.add(information_layer, 1);
		
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
			
			if ( m_timer.hasEnded() )
			{
				Main.game_state = Main.states.shaft;
				return;
			}
			System.out.println("CORE");
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
						case E:
							m_player.fetchImpulse().z += 1.f;
							break;
						case Q:
							m_player.fetchImpulse().z -= 1.f;
							break;
						case H:
							m_heads_up_display.setActive(!m_heads_up_display.getActive());
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
			m_player.fetchSpeed().x -= 1.f;
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT)) 
		{
			m_player.fetchSpeed().x += 1.f;
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
			if (Math.random() > .95f) // random returns a float within [0, 1]
			{
				Vector2f position = m_bedrock.eraseRandomTileAtTheTop();
				if ( position != null )
					m_malm_objects.add(new Malm(new Vector2f(position.x + BottomOfTheWorld.getTileWidth() / 2.f, position.y + BottomOfTheWorld.getTileHeight() / 2.f)));
			}
		}
		
		// This block sets the text for the HUD, currently quite inefficient
		{
			m_heads_up_display.setText
			(
				"Player d^2y: " + m_player.getImpulse().y
				+ "\nPlayer dy: " + m_player.getSpeed().y
				+ "\nPlayer y: " + m_player.getPosition().y
				+ "\nCurrent bedrock column height: " + m_bedrock.getCollisionTilePosition((int) m_player.getPosition().x)
				+ "\nSecond bedrock column height: " + m_bedrock.getCollisionTilePosition((int) (m_player.getPosition().x + m_player.getSize().x))
				+ (!m_bedrock.doesATileExistHere(m_player.getPosition()) ? "\ncollision" : "\n")
				+ "Player health: " + m_player.getHealth()
				+ "\nPlayer Speed: " + m_player.getSpeed()
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
				m_player.fetchImpulse().z += m_player.fetchSpeed().x * 0.2f;
			}
			// Check for collision with a geyser!
			else
			{
				if 
				( 
					m_bedrock.thereIsACollisionWithGeyser ( m_player.getPosition().x )
					|| m_bedrock.thereIsACollisionWithGeyser ( m_player.getPosition().x + m_player.getSize().x )
				)
				{
					m_player.fetchImpulse().y += Player.CM_JUMPFORCE / 3.f;
					m_collision_with_bedrock.set(true);
					if ( m_damage_frames == CM_RED_FLASH_FRAME_COUNT)
						m_player.takeDamage();
					m_player.fetchImpulse().z += m_player.fetchSpeed().x * 0.2f;
				}
			}
		}
		
		
		// Update the position of the crimson ( The magma under bedrock itself )
		{
			m_bedrock.updateCrimsonRelativeTo(m_player.getPosition().x);
		}
		
		// Set lava position to be correct:
		{
			m_lava.updateViaX(m_player.getPosition().x);
		}
		
		// For each malm object, slow it down and perform updates
		{
			for ( DynamicObject x : m_malm_objects )
			{
				// 1. Perform all relocations
				x.update();
				
				// 2. Perform friction
				x.fetchSpeed().x /= 1.02f;
				x.fetchSpeed().y /= 1.02f;
			}
		}
		
		// Each malm object, if within a certain box, accelerates towards the player
		{
			ArrayList<DynamicObject> to_remove = new ArrayList<>();
			for ( DynamicObject x : m_malm_objects )
			{
				if (x.isBoxNear(m_player, 0))
				{
					to_remove.add(x);
				}
				else if (m_player.isBoxNear(x, 75))
				{
					x.accelerateTowards(m_player, 0.1f);
				}
			}
			for ( DynamicObject x : to_remove )
			{
				m_malm_objects.remove(x);
			}
		}
	}

	private void updateObjects() 
	{
		m_player.update();
		m_player.fetchSpeed().x /= 1.02f;
		m_player.fetchSpeed().z /= 1.02f;
		
		setViewToPlayer();
		m_timer.update();
	}
	
	private void setViewToPlayer()
	{
		View v = Main.view;
		v = new View(m_player.getPosition(), Main.wnd.getDefaultView().getSize());
		
		// Scramble the view (Give the vibrating illusion), also topkek, magic numbers
		v.move(m_rng.nextInt() % 2 - 1, m_rng.nextInt() % 2 - 1);
		Main.wnd.setView(v);
	}
	
	
	
	private void drawFrame ( )
	{
		Main.wnd.clear();
		Main.wnd.draw(m_layers);
		
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
		
		for ( DynamicObject x : m_malm_objects )
			Main.wnd.draw(x);
		Main.wnd.display();
	}
}
