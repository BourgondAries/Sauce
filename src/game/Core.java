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
	private final static int CM_REMOVAL_DISTANCE = 0;
	private final static int CM_ACCELERATION_DISTANCE = Main.wnd.getSize().x / 10;
	private final static float CM_ACCELERATION_SPEED = Main.wnd.getSize().x / 10000.f;
	private final static float CM_FRICTIONAL_FORCE_DECAY = 1.02f;
	private final static float CM_GEYSER_INVERSE_JUMPFORCE = 3.f;
	private final static float CM_ROTATIONAL_MULTIPLIER = 0.2f;
	
	private LayerCollection m_layers = new LayerCollection();
	private Player 			m_player;
	private int 			m_damage_frames = CM_RED_FLASH_FRAME_COUNT;
	private int	 			m_gameover_time = Main.framerate * 5;
	private Text			m_gameover_text = new Text();
	
	private InfiniteBox 		m_lava;
	private BottomOfTheWorld 	m_bedrock;
	private HUD 				m_heads_up_display;
	private Bool 				m_collision_with_bedrock = new Bool(false);
	private Timer 				m_timer = new Timer(Main.wnd);
	private Bool				m_jumped = new Bool(true);
	private ScoreCounter		m_score_counter = new ScoreCounter();
	private RecentScore			m_recent_score = new RecentScore();
	
	private Random m_rng = new Random();
	
	private ArrayList<Malm> m_malm_objects;
	
	
	public Core() throws IOException
	{
		m_player = new Player();
		m_player.setSize(new Vector2f(30, 30));
		m_player.setTexture(PathedTextures.getTexture(Paths.get("res/drop2.png")));
		m_player.setOrigin(5, 5);
		m_player.setPosition(Main.wnd.getSize().x / 2, Main.wnd.getSize().y);
		m_player.setMass(10.f);
		
		m_bedrock = new BottomOfTheWorld ( );
		m_malm_objects = new ArrayList<>();
		
		m_bedrock.generateTiles();
		
		m_lava = new InfiniteBox();
		m_lava.setSize( new Vector2f(Main.wnd.getSize().x * 2, Main.wnd.getSize().y + BottomOfTheWorld.getTileCountY()*BottomOfTheWorld.getTileHeight()) );
		m_lava.setPosition(-Main.wnd.getSize().x / 2, -Main.wnd.getSize().y + BottomOfTheWorld.getTileCountY()*BottomOfTheWorld.getTileHeight());
		m_lava.setFillColor(new Color(255, 165, 0, 127));
		
		m_player.setPosition(new Vector2f(0.f, -100.f));
		m_player.setOrigin(m_player.getSize().x / 2, m_player.getSize().y / 2);
		
		m_heads_up_display = new HUD(Main.wnd);
		
		Layer player_layer = new Layer();
		player_layer.add(m_player);
		player_layer.add(m_lava);
		player_layer.add(m_bedrock);
		
		
		Layer information_layer = new Layer();
		information_layer.add(m_heads_up_display);
		information_layer.add(m_timer);
		information_layer.add(m_score_counter);
		information_layer.add(m_recent_score);
		
		m_layers.add(player_layer, 0);
		m_layers.add(information_layer, 1);
		
		m_score_counter.setAbsoluteView(Main.wnd.getView());
		
		m_gameover_text.setFont(PathedFonts.getFont(Paths.get("res/pixelmix.ttf")));
		m_gameover_text.setString("GAME OVER");
		m_gameover_text.setPosition(Main.wnd.getSize().x / 2 - m_gameover_text.getGlobalBounds().width / 2, Main.wnd.getSize().y / 2);
		
//		run();
	}
	
	public double run()
	{
		while (Main.game_state == Main.states.core)
		{
			handleEvents();
			runGameLogic();
			updateObjects();
			drawFrame();
			
			if ( m_timer.hasEnded() )
			{
				Main.wnd.setView(Main.wnd.getDefaultView());
				Main.game_state = Main.states.shaft;
				return 0.;
			}
		}
		
		// Calculate difficulty based on time left.
		{
			Main.wnd.setView(Main.wnd.getDefaultView());
			return ((double)m_timer.getTimeLeft()) / ((double)m_timer.getMaxDuration());
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
							if (m_jumped.fetchAndDisable())
								m_player.jump();
							break;
						case ESCAPE:
							Main.game_state = Main.states.shaft;
							return;
						case E:
							m_player.fetchImpulse().z += 1.f;
							break;
						case Q:
							m_player.fetchImpulse().z -= 1.f;
							break;
//						case H:
//							m_heads_up_display.setActive(!m_heads_up_display.getActive());
						default:
							break;
					
					}
				} break;
				case KEY_RELEASED:
				{
					KeyEvent keyev = event.asKeyEvent();
					switch (keyev.key)
					{
						case UP:
							m_jumped.set(true);
							break;
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
		if (isGameOver() == false)
		{
			// Effect of gravity on the player
			{
				m_player.fetchImpulse().y += 1.f;
			}
		}
		// Re-position part of BottomOfTheWorld so that we have fake continuity
		{ 
			float xpos = m_player.getPosition().x;
			if ( xpos - Main.wnd.getSize().x < m_bedrock.getXBounds().first )
				m_bedrock.generateLeft();
			else if (xpos + Main.wnd.getSize().x > m_bedrock.getXBounds().second )
				m_bedrock.generateRight();
		}
		
		if (isGameOver() == false)
		{
			// This block removes a random tile at the top (per frame, with a chance of 1% per frame)
			{
				if (Math.random() > .25f) // random returns a float within [0, 1]
				{
					Vector2f position = m_bedrock.eraseRandomTileAtTheTop();
					if ( position != null )
					{
						m_malm_objects.add
						(
							new Malm
							(
								new Vector2f
								(
									(float) (position.x + BottomOfTheWorld.getTileWidth() / 2.f)
									, (float) (position.y + BottomOfTheWorld.getTileHeight() / 2.f)
								)	
							)
						);
						m_malm_objects.get(m_malm_objects.size() - 1).setFillColor(new Color(127, 127, 127));
					}
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
					(
						!m_bedrock.doesATileExistHere(new Vector2f(m_player.getPosition().x - m_player.getSize().x, m_player.getPosition().y + m_player.getSize().y)) 
						|| !m_bedrock.doesATileExistHere(new Vector2f(m_player.getPosition().x + m_player.getSize().x, m_player.getPosition().y + m_player.getSize().y))
					)
	//				||
	//				( // Check for collision with a geyser!
	//					m_bedrock.thereIsACollisionWithGeyser ( m_player.getPosition().x )
	//					|| m_bedrock.thereIsACollisionWithGeyser ( m_player.getPosition().x + m_player.getSize().x ) 
	//				)
				)
				{
					m_player.fetchImpulse().y += Player.CM_JUMPFORCE / CM_GEYSER_INVERSE_JUMPFORCE;
					m_collision_with_bedrock.set(true);
					if ( m_damage_frames == CM_RED_FLASH_FRAME_COUNT)
						m_player.takeDamage();
					m_player.fetchImpulse().z += m_player.fetchSpeed().x * CM_ROTATIONAL_MULTIPLIER;
				}
				
				else
				{
					if 
					( 
						m_player.getPosition().y > m_lava.getPosition().y
						&& 
						(
							m_bedrock.thereIsACollisionWithGeyser ( m_player.getPosition().x )
							|| m_bedrock.thereIsACollisionWithGeyser ( m_player.getPosition().x + m_player.getSize().x ) 
						)
					)
					{
						m_player.fetchImpulse().y += Player.CM_JUMPFORCE / CM_GEYSER_INVERSE_JUMPFORCE;
						m_collision_with_bedrock.set(true);
						if ( m_damage_frames == CM_RED_FLASH_FRAME_COUNT)
							m_player.takeDamage();
						m_player.fetchImpulse().z += m_player.fetchSpeed().x * CM_ROTATIONAL_MULTIPLIER;
					}
				}
			}
			
			
			// For each malm object, slow it down and perform updates
			{
				for ( DynamicObject x : m_malm_objects )
				{
					// 1. Perform all relocations
					x.update();
					
					// 2. Perform friction
					x.fetchSpeed().x /= CM_FRICTIONAL_FORCE_DECAY;
					x.fetchSpeed().y /= CM_FRICTIONAL_FORCE_DECAY;
				}
			}
			
			// Each malm object, if within a certain box, accelerates towards the player
			{
				ArrayList<DynamicObject> to_remove = new ArrayList<>();
				for ( Malm x : m_malm_objects )
				{
					if (x.isBoxNear(m_player, CM_REMOVAL_DISTANCE))
					{
						to_remove.add(x);
						m_score_counter.addScore(x.getScore());
						m_recent_score.pushScore(x.getScore(), m_player.getPosition(), new Color( (int) ( 255.f * ((float) x.getScore()) / Malm.getMaxScore()), 0, 0) );
					}
					else if (m_player.isBoxNear(x, CM_ACCELERATION_DISTANCE))
					{
						x.accelerateTowards(m_player, CM_ACCELERATION_SPEED);
					}
				}
				for ( DynamicObject x : to_remove )
				{
					m_malm_objects.remove(x);
				}
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
		
		m_score_counter.update();
		m_recent_score.update();
		
		m_heads_up_display.updateHealth(m_player.getHealth());
		
		// Update the position of the crimson ( The magma under bedrock itself )
		{
			m_bedrock.updateCrimsonRelativeTo(m_player.getPosition().x);
		}
		
		// Set lava position to be correct:
		{
			m_lava.updateViaX(m_player.getPosition().x);
		}
		
		if (isGameOver())
			--m_gameover_time;
		
		if (!(m_gameover_time > 0))
			Main.game_state = Main.states.menu;
	}
	
	private void setViewToPlayer()
	{
		View v = Main.view;
		v = new View(m_player.getPosition(), Main.wnd.getDefaultView().getSize());
		
		// Scramble the view (Give the vibrating illusion), also topkek, magic numbers
		long proximity = m_timer.getTimeLeft();
		long maxtime = m_timer.getMaxDuration();
		long inverse = maxtime - proximity;
		float divergence = 10.f * ((float) inverse) / ((float) maxtime);
		v.move(m_rng.nextInt() % divergence - divergence / 2.f, m_rng.nextInt() % divergence - divergence / 2.f);
		Main.wnd.setView(v);
	}
	
	public boolean isGameOver()
	{
		return m_player.getHealth() == 0;
	}
	
	private void drawFrame ( )
	{
		Main.wnd.clear(new Color(30, 30, 30));
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
		
		if (isGameOver())
		{
			ConstView oldview = Main.wnd.getView();
			Main.wnd.setView(Main.wnd.getDefaultView());
			Main.wnd.draw(m_gameover_text);
			Main.wnd.setView(oldview);
		}
		
		Main.wnd.display();
	}
}
