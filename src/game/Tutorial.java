package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import engine.PathedTextures;
import engine.SyncTrack;

public class Tutorial
{
	private SoundBuffer buff = new SoundBuffer();
	private Sound sound = new Sound();
	private SyncTrack soundfx;
	
	RectangleShape 
		m_ship, 
		m_sky;
	
	private final float 
			TEXTURE_HEIGHT_MULTIPLIER = 30.f,
			SKY_SCROLL_SPEED_PIX_PER_FRAME = -50.f;
	private float TEXTURE_WIDTH;
	private float color_clamp = 0.f;
	private static final float COLOR_SPECTRUM_DELTA = 0.0001f;
	private Color clear_color = new Color(0, 0, 0);
	
	Tutorial ( ) throws IOException
	{
		buff.loadFromFile(Paths.get("sfx/menu_loop.ogg"));
		sound.setBuffer(buff);
		soundfx = new SyncTrack(sound);
		
		// We need a sky background, ship in the middle.
		// Sky must be moving.
		
		m_ship = new RectangleShape ( );
		m_ship.setSize(new Vector2f(200, 200));
		m_ship.setPosition(Main.wnd.getSize().x / 2 - m_ship.getSize().x / 2, Main.wnd.getSize().y / 2 - m_ship.getSize().y / 2);
		
		m_sky = new RectangleShape ( );
		try 
		{
			Texture tex = PathedTextures.getTexture(Paths.get("res/sky.tga"));
			tex.setRepeated ( true );
			TEXTURE_WIDTH = tex.getSize().x;
			m_sky.setSize ( new Vector2f ( TEXTURE_WIDTH, Main.wnd.getSize().y * TEXTURE_HEIGHT_MULTIPLIER ) );
			m_sky.setTexture ( tex );
			m_sky.setPosition(new Vector2f(0.f, Main.wnd.getSize().y));
		} 
		catch ( IOException exc_obj ) 
		{
			exc_obj.printStackTrace();
		}
		
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
							soundfx.play();
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
		// Change the sky's color to be more earthly in this frame.
		if (color_clamp < .75f)
			color_clamp += COLOR_SPECTRUM_DELTA;
		
		// Set the new color:
		clear_color = new Color((int) (255 * color_clamp * color_clamp), (int) (255 * color_clamp * color_clamp), (int) (255 * color_clamp));
		
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
						, Main.wnd.getSize().y
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
		Main.wnd.clear ( clear_color );
		Main.wnd.draw ( m_sky );
		Main.wnd.draw ( m_ship );
		Main.wnd.display ( );
	}
}
