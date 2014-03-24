package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import engine.PathedTextures;

public class Tutorial
{
	Tutorial ( )
	{
		// We need a sky background, ship in the middle.
		// Sky must be moving.
		
		m_ship = new RectangleShape ( );
		m_ship.setSize(new Vector2f(200, 200));
		m_ship.setPosition(400 - 100, 300 - 100);
		
		m_sky = new RectangleShape ( );
		try 
		{
			Texture tex = PathedTextures.getTexture(Paths.get("res/sky2.png"));
			tex.setRepeated ( true );
			TEXTURE_WIDTH = tex.getSize().x;
			m_sky.setSize ( new Vector2f ( TEXTURE_WIDTH, Main.wnd.getSize().y * TEXTURE_HEIGHT_MULTIPLIER ) );
			m_sky.setTexture ( tex );
		} 
		catch ( IOException exc_obj ) 
		{
			exc_obj.printStackTrace();
		}
		
		run();
	}
	
	RectangleShape 
		m_ship, 
		m_sky;
	
	private final float 
			TEXTURE_HEIGHT_MULTIPLIER = 30.f,
			SKY_SCROLL_SPEED_PIX_PER_FRAME = -50.f;
	private float TEXTURE_WIDTH;
	
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
		while ( Main.game_state == Main.states.tutorial );
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
							break;
						default:
							break;
					
					}
				} break;
			default:
				break;
			}
		}
//		
//		if (Keyboard.isKeyPressed(Keyboard.Key.LEFT)) 
//		{
//			m_player.fetchSpeed().x -= 1.f;
//		} 
//		else if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT)) 
//		{
//			m_player.fetchSpeed().x += 1.f;
//		}
//		else if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)) 
//		{
//			m_player.jump();
//		} 
//		else if (Keyboard.isKeyPressed(Keyboard.Key.RETURN)) 
//		{
//			m_bedrock.eraseRandomTileAtTheTop();
//		} 
//		else if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) 
//		{
//			Main.game_state = Main.states.menu;
//		}
	}
	
	private void runGameLogic()
	{
		// Move the sky texture upwards.
		// If the end has been reached, we return the position to (0, 0).
		// We also randomize the x-axis so that each iteration looks semi-random.
		{
			m_sky.move(new Vector2f(0.f, SKY_SCROLL_SPEED_PIX_PER_FRAME));
			if ( m_sky.getPosition().y < -m_sky.getSize().y + Main.wnd.getSize().y )
			{
				m_sky.setPosition 
				( 
					new Vector2f 
					( 
						(float) (-Math.random()) * m_sky.getSize().x / 2.f
						, 0.f 
					) 
				);
			}
		}
	}	
	
	private void updateObjects()
	{
		
	}
	
	private void drawFrame()
	{
		Main.wnd.clear ( );
		Main.wnd.draw ( m_sky );
		Main.wnd.draw ( m_ship );
		Main.wnd.display ( );
	}
}
