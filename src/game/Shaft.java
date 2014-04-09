package game;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

import engine.*;
import ttl.*;

import org.jsfml.audio.*;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

public class Shaft
{
	private Layer 
		m_background_layer = new Layer(),
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
	private ArrayList<FallingRock>
		m_falling_rocks = new ArrayList<>();
	private FallingHealth 
		m_falling_health = new FallingHealth(),
		m_falling_booster = new FallingHealth();
	private int 
		m_framal_godmode = 0,
		m_framal_booster = -1,
		m_progress = 0,
		m_progress_speed = 20;
	private RectangleShape 
		m_background = new RectangleShape(),
		m_background_hue = new RectangleShape();
	private ProgressBar
		m_progress_bar = new ProgressBar();
	private HUD
		m_hud;
	private Music
		m_background_music;
	private final int 
		CM_MAX_PROGRESS = 90000;//90000;
	private Bool 
		m_collision = new Bool(false);
	private final int 
		CM_BORDER_SIZE;
	private double // Clamped [0, 1], 0 = hardest, 1= doesn't spawn FallingRocks
		m_difficulty = 0.1,
		m_rock_start_speed = 20., // The rock spawn with a certain speed
		m_rock_speed_aberrant = 20., // A rock that is especially fast!
		m_rock_speed_uncertainty = 1.,
		m_rock_size = 10.,
		m_booster_increase = 30.,
		m_booster_decrease = -20.;
	private int	 			
		m_gameover_time = Main.framerate * 5;
	private Text			
		m_gameover_text = new Text();
		
	public Shaft(TransmittableData data)
	{
		m_walls.add(m_progress_bar);
		try 
		{
			m_background_music = Formulas.loadMusic("sfx/flight.ogg"); //use exciting_tune
		}
		catch (IOException exc_obj)
		{
			exc_obj.printStackTrace();
		}
		m_background_music.setLoop(true);
		m_background_music.play();
		
		m_ship.setImpactSound("sfx/vehicle_crash.ogg");
		m_difficulty = data.difficulty;
		
		m_rock_start_speed = (1.1 - m_difficulty) * m_rock_start_speed;
		m_rock_speed_aberrant = (1.1 - m_difficulty) * m_rock_speed_aberrant;
		m_booster_increase = (1.1 - m_difficulty) * m_booster_increase;
		m_booster_decrease = (1.1 - m_difficulty) * m_booster_decrease;
		
		try 
		{
			Texture tex = PathedTextures.getTexture(Paths.get("res/shaft/wall_back.tga"));
			m_background.setSize(new Vector2f(tex.getSize()));
			m_background.setTexture(tex);
			
			tex = PathedTextures.getTexture(Paths.get("res/shaft/wall_left.tga"));
			m_wall_left.setSize(new Vector2f(tex.getSize()));
			m_wall_left.setTexture(tex);
			
			tex = PathedTextures.getTexture(Paths.get("res/shaft/wall_right.tga"));
			m_wall_right.setSize(new Vector2f(tex.getSize()));
			m_wall_right.setTexture(tex);
		} catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
		m_wall_right.setPosition(Main.wnd.getSize().x - m_wall_right.getSize().x, -m_wall_right.getSize().y + Main.wnd.getSize().y);
		m_wall_objects.add(m_wall_left);
		m_wall_objects.add(m_wall_right);
		m_wall_left.setPosition(new Vector2f(0, -m_wall_left.getSize().y + Main.wnd.getSize().y));
		
		m_background_layer.add(m_background);
		m_background_layer.add(m_background_hue);
		m_background_hue.setSize(m_background.getSize());
		m_background_hue.setFillColor(new Color(0, 0, 0, 255));
		
		m_walls.add(m_wall_left);
		m_walls.add(m_wall_right);
		
		m_game_elements.add(m_ship);
		m_game_elements.add(m_falling_health);
		m_falling_health.setPosition(new Vector2f(0, Main.wnd.getSize().y));
		try 
		{
			m_falling_booster.setTexture(PathedTextures.getTexture(Paths.get("res/shaft/boost.tga")));
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
		m_game_elements.add(m_falling_booster);
		
		
		m_layers.add(m_walls, 1);
		m_layers.add(m_game_elements, 0);
		
		m_hud = data.hud;
		m_walls.add(m_hud);
		
		CM_BORDER_SIZE = (int) m_wall_left.getSize().x;
		
		try 
		{
			m_gameover_text.setFont(PathedFonts.getFont(Paths.get("res/pixelmix.ttf")));
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
		m_gameover_text.setString("GAME OVER");
		m_gameover_text.setPosition(Main.wnd.getSize().x / 2 - m_gameover_text.getGlobalBounds().width / 2, Main.wnd.getSize().y / 2);
		
		initShip(data);
		
		run();
	}
	
	private boolean isGameOver()
	{
		return m_ship.getHealth() == 0;
	}
	
	private void initShip(TransmittableData data)
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
		
		m_ship.setHealth(data.ship.getHealth());
	}
	
	private void run()
	{
		do
		{
			if (isGameOver() == false)
			{
				handleEvents();
				runGameLogic();
			}
			updateObjects();
			if (Main.game_state != Main.states.shaft)
			{
				m_background_music.stop();
				return;
			}
			drawFrame();
		}
		while ( Main.game_state == Main.states.shaft );
		m_background_music.stop();
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
						case E:
							m_ship.fetchImpulse().z += 1.f;
							break;
						case Q:
							m_ship.fetchImpulse().z -= 1.f;
							break;
						case D:
							m_hud.setDebugState(!m_hud.getDebugState());
							break;
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
	}
	
	private void runGameLogic()
	{
		for (DynamicObject x : m_wall_objects)
		{
			x.fetchSpeed().y = 100.3f; // Rather redundant... Let's just hope the JVM optimizes this away.
			if (x.getPosition().y > -x.fetchSpeed().y)
			{
				x.setPosition(x.getPosition().x, -x.getSize().y + Main.wnd.getSize().y);
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
		
		/*
		 * Spawn falling rocks that need to be dodged!
		 * The rocks ACCELERATE! OH MY GOOOOOOOOOOOOOOOOOOOOODDDDDDDDDDDDDDDDDDDDDD
		 */
		{
			if (Math.random() > m_difficulty)
			{ 
				FallingRock falling_rock = new FallingRock();
				falling_rock.setPosition(new Vector2f((float) (Math.random() * ( Main.wnd.getSize().x - m_wall_left.getSize().x * 2) + m_wall_left.getSize().x), (float) -m_rock_size));
				falling_rock.setSize(new Vector2f((float)m_rock_size, (float) m_rock_size));
				falling_rock.setFillColor(new Color(255, 255, 255));
				falling_rock.setSpeed
				(
					new Vector3f
					(
						0.f
						, (float) ((Math.random() > m_difficulty ? m_rock_speed_aberrant : m_rock_start_speed) + (Math.random() - 0.5) * m_rock_speed_uncertainty)
						, 0.f
					)
				);
				m_falling_rocks.add(falling_rock);
			}
		}
		
		/*
		 * For all falling rocks, increase their speed by a gravitational constant.
		 */
		{
			for (FallingRock ref : m_falling_rocks)
			{
				ref.accelerateTowards(m_ship, 0.01f);
				ref.fetchImpulse().y += 0.03f;
			}
		}
		
		/*
		 * If a falling rock has position below the screen, delete it.
		 */
		{
			ArrayList<FallingRock> to_remove = new ArrayList<>();
			for (FallingRock ref : m_falling_rocks)
			{
				if (ref.getPosition().y > Main.wnd.getSize().y)
				{
					to_remove.add(ref);
				}
			}
			for (FallingRock rm : to_remove)
			{
				m_falling_rocks.remove(rm);
			}
		}
		
		/*
		 * If a falling rock has collided with the ship, cause a loss of life
		 */
		if (m_framal_godmode == 0)
		{
			for (FallingRock ref : m_falling_rocks)
			{
				if (ref.isBoxNear(m_ship, 0))
				{
					m_collision.set(true);
					m_ship.takeDamage(true);
					m_hud.updateHealth(m_ship.getHealth());
					if (isGameOver())
						try 
						{
							Texture tex = PathedTextures.getTexture(Paths.get("res/shaft/explosion.tga"));
							m_ship.setSize(new Vector2f(tex.getSize()));
							m_ship.setTexture(tex);
						} 
						catch (IOException exc_obj) 
						{
							exc_obj.printStackTrace();
						}
					m_framal_godmode += Main.framerate * 2;
					break;
				}
			}
		}
		
		
		// If the health is at a coord lower than the screen, try to put it atop again based on difficulty
		{
			if (m_falling_health.getPosition().y > Main.wnd.getSize().y)
			{
				if (Math.random() < m_difficulty)
				{
					m_falling_health.setPosition(new Vector2f((float) (Math.random() * ( Main.wnd.getSize().x - m_wall_left.getSize().x * 2) + m_wall_left.getSize().x), (float) -m_falling_health.getSize().y));
				}
			}
		}
		
		// Check if there is collision with the health box!
		{
			if (m_falling_health.isBoxNear(m_ship, 0))
			{
				m_framal_godmode = -Main.framerate * 2;
				m_ship.repairDamage();
				m_falling_health.setPosition(new Vector2f((float) (Math.random() * ( Main.wnd.getSize().x - m_wall_left.getSize().x * 2) + m_wall_left.getSize().x), (float) -Main.wnd.getSize().y));
			}
		}
		
		
		// If the booster is at a coord lower than the screen, try to put it atop again based on difficulty
		{
			if (m_falling_booster.getPosition().y > Main.wnd.getSize().y)
			{
				if (Math.random() < m_difficulty)
				{
					m_falling_booster.setPosition(new Vector2f((float) (Math.random() * ( Main.wnd.getSize().x - m_wall_left.getSize().x * 2) + m_wall_left.getSize().x), (float) -Main.wnd.getSize().y));
				}
			}
		}
		
		// Check if there is collision with the booster box!
		{
			if (m_falling_booster.isBoxNear(m_ship, 0))
			{
				m_framal_godmode = Main.framerate * 5;
				m_framal_booster = m_framal_godmode;
				
				// Set the speeds to be higher
				for (FallingRock x : m_falling_rocks)
				{
					x.setSpeed(new Vector3f(x.getSpeed().x, (float) (x.getSpeed().y + m_booster_increase), x.getSpeed().z));
				}
				m_rock_start_speed += m_booster_increase;
				m_progress_speed += m_booster_increase;
				
				m_falling_booster.setPosition(new Vector2f((float) (Math.random() * Main.wnd.getSize().x), (float) -Main.wnd.getSize().y));
			}
		}
		
		// Decrement framal godmode by 1 IF it is not 0
		{
			if (m_framal_godmode > 0)
				--m_framal_godmode;
			else if (m_framal_godmode < 0)
				++m_framal_godmode;
			if (m_framal_booster > 0)
				--m_framal_booster;
			else if (m_framal_booster == 0)
			{
				for (FallingRock x : m_falling_rocks)
				{
					x.setSpeed(new Vector3f(x.getSpeed().x, (float) (x.getSpeed().y + m_booster_decrease), x.getSpeed().z));
					x.setOutlineColor(new Color(255, 127, 127));
				}
				m_rock_start_speed += m_booster_decrease;
				m_progress_speed += m_booster_decrease;
				m_framal_booster = -1;
			}
		}
		
		
		// This block sets the text for the HUD, currently quite inefficient
		{
			m_hud.setText
			(
				"Player d^2y: " + m_ship.getImpulse().y
				+ "\nPlayer dy: " + m_ship.getSpeed().y
				+ "\nPlayer y: " + m_ship.getPosition().y
				+ "\nPlayer health: " + m_ship.getHealth()
				+ "\nPlayer Speed: " + m_ship.getSpeed()
				+ "\nRock count: " + m_falling_rocks.size()
				+ "\nHealth position: " + m_falling_health.getPosition()
				+ "\nBooster position: " + m_falling_booster.getPosition()
			);
		}
	
		
	}
	
	private void updateObjects()
	{
		if (isGameOver() == false)
		{
			// Update both walls
			for (DynamicObject x : m_wall_objects)
			{
				x.update();
			}
			
			// Update the ship...
			m_ship.update();
			
			// Update the falling rocks position
			{
				for (FallingRock ref : m_falling_rocks)
				{
					ref.update();
				}
			}
			
			// Update the health box, and the booster
			{
				m_falling_health.update();
				m_falling_booster.update();
			}
			
			// Check the progress
			{
				m_progress += m_progress_speed;
				if (m_progress > CM_MAX_PROGRESS)
				{
					Main.game_state = Main.states.enterscore;
				}
			}
			
			// Set the appropriate color for the background hue.
			{
				if (m_framal_booster == -1)
				{
					if (m_framal_godmode > 0)
						m_background_hue.setFillColor(new Color(m_framal_godmode + 30, 30, 30, 127));
					else if (m_framal_godmode < 0)
						m_background_hue.setFillColor(new Color(30, 30 - m_framal_godmode, 30, 127));
				}
				else
					m_background_hue.setFillColor(new Color(30, 30 + (m_framal_booster > 30 ? 30 : m_framal_booster), 30, 127));
			}
			
			// Update the HUD's health display
			{
				m_hud.updateHealth(m_ship.getHealth());
			}
			
			// Update the progress bar
			{
				m_progress_bar.update(m_progress, CM_MAX_PROGRESS);
			}
		}
		// Gameover stuff
		if (isGameOver())
			--m_gameover_time;
		
		if (!(m_gameover_time > 0))
			Main.game_state = Main.states.menu;
	}
	
	private void drawFrame()
	{
		Main.wnd.clear();
		Main.wnd.draw(m_background_layer);
		for (FallingRock ref : m_falling_rocks)
		{
			Main.wnd.draw(ref);
		}
		Main.wnd.draw(m_layers);
		
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
