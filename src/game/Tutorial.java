package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import engine.Formulas;
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
			SKY_SCROLL_SPEED_PIX_PER_FRAME = -70.f,
			STAR_SCROLL_SPEED_PIX_PER_FRAME = -5;
	private float TEXTURE_WIDTH;
	private float color_clamp = 0.f;
	private Vector2f ship_move_to;
	private Vector2f ship_move_from;
	private Vector2f ship_move_completeness = new Vector2f(0,0);
	private float ship_speed = 2;
	private float ship_position_distance_from_center =
			Main.wnd.getSize().x>=Main.wnd.getSize().y ? Main.wnd.getSize().y/4 : Main.wnd.getSize().x/4;
	private static final float COLOR_SPECTRUM_DELTA = 0.0001f;
	private Color clear_color = new Color(0, 0, 0);
	private Texture star;
	private float star_max_size = 1;
	private float star_min_size = 0.2f;
	private float number_of_stars = 50;
	private List<Sprite> stars = new ArrayList<>();
	
	Tutorial ( ) throws IOException
	{
		buff.loadFromFile(Paths.get("sfx/menu_loop.ogg"));
		sound.setBuffer(buff);
		soundfx = new SyncTrack(sound);
		
		// We need a sky background, ship in the middle.
		// Sky must be moving.
		
		m_ship = new RectangleShape ( );
		m_ship.setSize(new Vector2f(200, 200));
		//m_ship.setPosition(Main.wnd.getSize().x / 2 - m_ship.getSize().x / 2, Main.wnd.getSize().y / 2 - m_ship.getSize().y / 2);
		//m_ship.setPosition(Main.wnd.getSize().x / 2 - m_ship.getSize().x / 2, - m_ship.getSize().y);
		ship_move_to = new Vector2f(Main.wnd.getSize().x / 2 - m_ship.getSize().x / 2,0);
		ship_move_from = new Vector2f(Main.wnd.getSize().x / 2 - m_ship.getSize().x / 2, - m_ship.getSize().y);
		
		m_sky = new RectangleShape ( );
		try 
		{
			star = PathedTextures.getTexture(Paths.get("res/menu/star.tga"));
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
		
		// Make stars
		for (int i = 0; i < number_of_stars; i++) {
			stars.add(makeStar());
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
		if (color_clamp < 1.f)
			color_clamp += COLOR_SPECTRUM_DELTA;
		
		// Set the new color:
		clear_color = new Color((int) (189 * color_clamp * color_clamp), (int) (230 * color_clamp * color_clamp), (int) (255 * color_clamp));
		m_sky.setFillColor(new Color(255,255,255,(int) (255*color_clamp)));
		
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
	
	private Sprite makeStar() {
		Sprite new_sprite = new Sprite(star);
		new_sprite.setOrigin(new_sprite.getLocalBounds().width/2, new_sprite.getLocalBounds().height/2);
		new_sprite.setPosition((float) Math.random()*Main.wnd.getSize().x, (float) Math.random()*Main.wnd.getSize().y);
		float scale = (float) (Math.random()*(star_max_size-star_min_size)+star_min_size);
		new_sprite.setScale(scale, scale);
		new_sprite.setColor(new Color(255,255,255,0));
		return new_sprite;
	}
	
	private void updateObjects()
	{
		// Update move to position
		if (ship_move_completeness.x>=1) {
			ship_move_completeness = new Vector2f(0,ship_move_completeness.y);
			ship_move_from = new Vector2f(ship_move_to.x,ship_move_from.y);
			
			float rand_x = (float) ((Main.wnd.getSize().x-m_ship.getSize().x)/2+
					Math.random()*ship_position_distance_from_center*2-ship_position_distance_from_center);
			ship_move_to = new Vector2f(rand_x,ship_move_to.y);
			System.out.println("x: "+ship_move_to.x);
		}
		if (ship_move_completeness.y>=1) {
			ship_move_completeness = new Vector2f(ship_move_completeness.x,0);
			ship_move_from = new Vector2f(ship_move_from.x,ship_move_to.y);
			
			float rand_y = (float) ((Main.wnd.getSize().y-m_ship.getSize().y)/2+
					Math.random()*ship_position_distance_from_center*2-ship_position_distance_from_center);
			ship_move_to = new Vector2f(ship_move_to.x,rand_y);
			System.out.println("y: "+ship_move_to.y);
		}
		
		// Move ship to position
		ship_move_completeness = Vector2f.add(ship_move_completeness,
				new Vector2f(ship_speed/Math.abs(ship_move_to.x-ship_move_from.x),ship_speed/Math.abs(ship_move_to.y-ship_move_from.y)));
		m_ship.setPosition(
				ship_move_from.x-Formulas.sinus_in(ship_move_completeness.x)*(ship_move_from.x-ship_move_to.x),
				ship_move_from.y-Formulas.sinus_in(ship_move_completeness.y)*(ship_move_from.y-ship_move_to.y));
		
		// Update stars
		for (Sprite star : stars) {
			if (star.getPosition().y<-star.getGlobalBounds().height/2) {
				star.setPosition((float) Math.random()*Main.wnd.getSize().x,
						Main.wnd.getSize().y + star.getGlobalBounds().height/2);
			}
			star.move(0, STAR_SCROLL_SPEED_PIX_PER_FRAME);
			star.setColor(new Color(255,255,255,(int) (255*Formulas.slow_top_early_then_return(color_clamp))));
		}
	}
	
	private void drawFrame()
	{
		Main.wnd.clear ( clear_color );
		if (color_clamp<1f)	for (Sprite star : stars) {
			Main.wnd.draw ( star );
		}
		Main.wnd.draw ( m_sky );
		Main.wnd.draw ( m_ship );
		Main.wnd.display ( );
	}
}
