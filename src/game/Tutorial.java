package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import engine.Formulas;
import engine.PathedSounds;
import engine.PathedTextures;
import game.SpeechBox.StatusBox;

public class Tutorial
{	
	RectangleShape 
		m_ship, 
		m_sky;
	
	private HUD hud = new HUD(Main.wnd);
	
	private final float 
			TEXTURE_HEIGHT_MULTIPLIER = 30.f,
			SKY_SCROLL_SPEED_PIX_PER_FRAME = -70.f,
			STAR_SCROLL_SPEED_PIX_PER_FRAME = -5,
			TIME_TO_OPEN_BOX = 5000;
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
	private float textbox_spacing_bottom = 30;
	private float number_of_stars = 50;
	private List<Sprite> stars = new ArrayList<>();
	private Music aud_wind = new Music();
	private boolean exit_initiated = false;
	private boolean exit_started = false;
	private boolean box_not_opened = true;
	private float time_to_fade_in_music = 3000;
	private float displacement_crash_textures = 50;
	private long start_time;
	private long start_end_time;
	private boolean end_precache_done = false;
	private SpeechBox box;
	private Music background_music = new Music();
	private boolean fase_1 = true;
	private boolean fase_2 = false;
	private boolean fase_3 = false;
	private float fase_2_scale = 0.2f;
	private Sprite img_mountains;
	private Sprite img_grass;
	private List<Sprite> speedness = new ArrayList<>();
	private boolean init_after_crach = true;
	private float duration_of_shake = 1000;
	private float duration_of_wait = 2000;
	private float when_to_play_sound = 2500;
	private boolean sound_is_played = false;
	private Sound aud_before_expl;
	private Sound aud_expl;
	private Sound aud_impact;
	private Sound aud_digging;
	private float fase_2_wind_pitch = 3;
	private RectangleShape flash = new RectangleShape();
	
	private Texture to_core_rand_1;
	private Texture to_core_rand_2;
	private Texture to_core_rand_3;
	private Texture to_core_rand_4;
	private Texture to_core_rand_5;
	
	private float fase_2_ground_height = 200;
	private Vector2f background_position;
	private float fase_2_ship_speed = 20f;
	private float time_to_dig = 1000;

	private int ammount_of_stuff = 50;
	
	Tutorial ( ) throws IOException
	{	
		// Load windsound
		aud_wind.openFromFile(Paths.get("sfx/wind.ogg"));
		aud_wind.setVolume(0);
		aud_wind.setLoop(true);
		aud_wind.play();
		
		// Load music
		background_music.openFromFile(Paths.get("sfx/tutorial_music.ogg"));
		background_music.setLoop(true);
		background_music.setVolume(0);
		background_music.play();
		
		// We need a sky background, ship in the middle.
		// Sky must be moving.
		
		m_ship = new RectangleShape ( );
		{
			Texture tex = PathedTextures.getTexture(Paths.get("res/drop2.png"));
			m_ship.setSize(new Vector2f(tex.getSize()));
			m_ship.setTexture(tex);
		}
		//m_ship.setPosition(Main.wnd.getSize().x / 2 - m_ship.getSize().x / 2, Main.wnd.getSize().y / 2 - m_ship.getSize().y / 2);
		//m_ship.setPosition(Main.wnd.getSize().x / 2 - m_ship.getSize().x / 2, - m_ship.getSize().y);
		ship_move_to = new Vector2f(Main.wnd.getSize().x / 2 - m_ship.getSize().x / 2,0);
		ship_move_from = new Vector2f(Main.wnd.getSize().x / 2 - m_ship.getSize().x / 2, - m_ship.getSize().y);
		
		flash.setSize(new Vector2f(Main.wnd.getSize()));
		flash.setPosition(0,0);
		flash.setFillColor(new Color(255,255,255,0));
		
		m_sky = new RectangleShape ( );
		try 
		{
			// Load sounds
			aud_before_expl = new Sound(PathedSounds.getSound(Paths.get("sfx/crash_right_before_launch.ogg")));
			aud_expl = new Sound(PathedSounds.getSound(Paths.get("sfx/crash_launch.ogg")));
			aud_impact = new Sound(PathedSounds.getSound(Paths.get("sfx/crash.ogg")));
			aud_digging = new Sound(PathedSounds.getSound(Paths.get("sfx/crash_through_earth.ogg")));
			aud_digging.setLoop(true);
			
			// Load stuff
			to_core_rand_1 = PathedTextures.getTexture(Paths.get("res/tutorial/to_core_rand_1.tga"));
			to_core_rand_2 = PathedTextures.getTexture(Paths.get("res/tutorial/to_core_rand_2.tga"));
			to_core_rand_3 = PathedTextures.getTexture(Paths.get("res/tutorial/to_core_rand_3.tga"));
			to_core_rand_4 = PathedTextures.getTexture(Paths.get("res/tutorial/to_core_rand_4.tga"));
			to_core_rand_5 = PathedTextures.getTexture(Paths.get("res/tutorial/to_core_rand_5.tga"));
			
			box = new SpeechBox();
			star = PathedTextures.getTexture(Paths.get("res/tutorial/star.tga"));
			img_mountains = new Sprite(PathedTextures.getTexture(Paths.get("res/tutorial/crash_back.tga")));
			img_grass = new Sprite(PathedTextures.getTexture(Paths.get("res/tutorial/crash_front.tga")));
			Texture tex = PathedTextures.getTexture(Paths.get("res/tutorial/sky.tga"));
			tex.setRepeated ( true );
			TEXTURE_WIDTH = tex.getSize().x;
			m_sky.setSize ( new Vector2f ( TEXTURE_WIDTH, Main.wnd.getSize().y * TEXTURE_HEIGHT_MULTIPLIER ) );
			m_sky.setTexture ( tex );
			m_sky.setPosition(new Vector2f(0.f, Main.wnd.getSize().y));
		}
		catch ( IOException exc_obj ) 
		{
			exc_obj.printStackTrace();
			Main.dispose();
		}
		
		// Set position for background
		background_position = new Vector2f((Main.wnd.getSize().x-img_mountains.getGlobalBounds().width)/2,
				Main.wnd.getSize().y+displacement_crash_textures-img_mountains.getGlobalBounds().height);
		img_mountains.setPosition(background_position);
		img_grass.setPosition(img_mountains.getPosition());
		
		// Write tutorial text
		box.changePosition(new Vector2f(
				(Main.wnd.getSize().x-box.getSize().x)/2,
				Main.wnd.getSize().y - box.getSize().y - textbox_spacing_bottom));
		
		box.lockBox();
		
		box.queueText(
				  "Welcome to another mission, solider!\n\n"
				+ "You are currently breaching through the atmosphere of a planet\n"
				+ "which is said to contain no life. However, our scanners show a large\n"
				+ "consentration of valuables deep inside the core.\n\n"
				+ "We need you to go there, release a chemical compound to compact the material\n"
				+ "and make it gatherable before gathering it and getting it out.");
		
		box.queueText(
				  "In order to do such, you will need to learn the basics of the process.\n"
				+ "You use the WASD keys to move your ship around.\n"
				+ "Try to gather as much valuables as possible while in the core.\n"
				+ "When the core becomes unstable, the shockwave will launch you\n"
				+ "towards the surface.\n"
				+ "Here you must dodge debris and get powerups to get through the shaft alive.");
		
		box.queueText(
				  "The heads up display will tell you all you need to know about your ships status.\n"
				+ "The gears shows how much more damage your ship can sustain.\n"
				+ "The horisontal bar shows how much of your boost that is remaining.\n"
				+ "The vertical bar shows the distance between you and the surface.\n"
				+ "Lastly, the numbers representate your payment at the end of the mission.\n"
				+ "If your ships gets high damage, you use long time and pick up few\n"
				+ "valuables, the payment will be bad. Remember that!");
		
		box.queueText(
				  "The only thing left to say now is good luck!\n\nCaptain out!");
		
		box.updateHeader("Tutorial");
		box.updateFooter("Press (LEFT) to advance, (RIGHT) to view previous and (ESCAPE) to skip");
		
		// Make stars
		for (int i = 0; i < number_of_stars; i++) {
			stars.add(makeStar());
		}
		
		start_time = System.currentTimeMillis();
		run();
	}
	
	
	
	public void run()
	{
		do 
		{
			handleEvents();
			runGameLogic();
			if (fase_1) faseOne();
			else if (fase_2) faseTwo();
			else faseThree();
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
						case RIGHT:
							box.nextText();
							break;
						case LEFT:
							box.previousText();
							break;
						case ESCAPE:
							box.closeBox();
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
		if (fase_2) {
			clear_color = new Color(189,230,255);
		} else if (fase_3) {
			clear_color = new Color(109,92,85);
		} else {
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
	
	private Sprite makeSpeedThing() {
		double likeliness = Math.random();
		Texture random_tex = 
			likeliness>0.8?to_core_rand_1:
			likeliness>0.6?to_core_rand_2:
			likeliness>0.4?to_core_rand_3:
			likeliness>0.2?to_core_rand_4:
			to_core_rand_5;
		Sprite new_sprite = new Sprite(random_tex);
		new_sprite.setOrigin(new_sprite.getLocalBounds().width/2, new_sprite.getLocalBounds().height/2);
		new_sprite.setPosition((float) (Math.random()*Main.wnd.getSize().x),
				(float) (Math.random()*Main.wnd.getSize().y));
		return new_sprite;
	}
	
	private void faseTwo() {
		if (Main.wnd.getSize().y - m_ship.getPosition().y>fase_2_ground_height) {
			m_ship.move(0, fase_2_ship_speed);
		} else if (init_after_crach) {
			aud_wind.stop();
			init_after_crach = false;
			start_time = System.currentTimeMillis();
			aud_impact.play();
		} else if (System.currentTimeMillis()-start_time<duration_of_shake) {
			float x = 1-(System.currentTimeMillis()-start_time)/duration_of_shake;
			img_mountains.setPosition( (float) (background_position.x+(Math.random()*2-1)*displacement_crash_textures*x),
					(float) (background_position.y+(Math.random()*2-1)*displacement_crash_textures*x));
			img_grass.setPosition((float) (background_position.x+(Math.random()*2-1)*displacement_crash_textures*x),
					(float) (background_position.y+(Math.random()*2-1)*displacement_crash_textures*x));
			flash.setFillColor(new Color(255,255,255,(int) (255*x)));
		} else if (System.currentTimeMillis()-start_time<duration_of_shake+duration_of_wait) {
			if (System.currentTimeMillis()-start_time<when_to_play_sound && !sound_is_played) {
				aud_before_expl.play();
				sound_is_played = true;
			}
		} else {
			aud_before_expl.stop();
			aud_expl.play();
			aud_digging.play();
			fase_2 = false;
			fase_3 = true;
			start_time = System.currentTimeMillis();
		}
	}
	
	private void faseThree() {
		speedness.clear();
		for (int i = 0; i < ammount_of_stuff ; i++) {
			speedness.add(makeSpeedThing());
		}
		if (System.currentTimeMillis()-start_time>=time_to_dig) {
			aud_digging.stop();
			Main.game_state = Main.states.core;
		}
	}
	
	private void faseOne()
	{
		// Handle textbox
		if (box_not_opened && System.currentTimeMillis() - start_time >= TIME_TO_OPEN_BOX) {
			box.unlockBox();
			box.openBox();
			box_not_opened = false;
		} else if (!box_not_opened && box.getBoxState() == StatusBox.closed) {
			exit();
		}
		
		// Update textbox
		box.update();
		
		// Update music
		aud_wind.setVolume(100*Formulas.very_slow_stop(color_clamp));
		float volume;
		if (!exit_initiated) {
			volume = (System.currentTimeMillis() - start_time)/time_to_fade_in_music;
		} else {
			if (!end_precache_done) {
				start_end_time = System.currentTimeMillis();
				end_precache_done = true;
			}
			volume = 1-(System.currentTimeMillis() - start_end_time)/time_to_fade_in_music;
		}
		volume = volume > 1 ? 1 : volume < 0 ? 0 : volume;
		background_music.setVolume(100*volume);
		
		// Update move to position
		if (ship_move_completeness.x>=1) {
			ship_move_completeness = new Vector2f(0,ship_move_completeness.y);
			ship_move_from = new Vector2f(ship_move_to.x,ship_move_from.y);
			
			float rand_x = (float) ((Main.wnd.getSize().x-m_ship.getSize().x)/2+
					(Math.random()*2-1)*ship_position_distance_from_center*Formulas.sinus_in(color_clamp));
			ship_move_to = new Vector2f(rand_x,ship_move_to.y);
//			System.out.println("x: "+ship_move_to.x);
		}
		if (ship_move_completeness.y>=1) {
			if (exit_started) {
				aud_wind.setPitch(fase_2_wind_pitch);
				aud_wind.setVolume(100);
				background_music.stop();
				fase_2 = true;
				fase_1 = false;
				m_ship.setScale(fase_2_scale, fase_2_scale);
				m_ship.setPosition(Main.wnd.getSize().x/2, -m_ship.getSize().y);
				return;
			}
			ship_move_completeness = new Vector2f(ship_move_completeness.x,0);
			ship_move_from = new Vector2f(ship_move_from.x,ship_move_to.y);
			
			float rand_y;
			if (exit_initiated) {
				rand_y = Main.wnd.getSize().y+m_ship.getSize().y/2;
				exit_started = true;
			} else {
				rand_y = (float) ((Main.wnd.getSize().y-m_ship.getSize().y)/2+
						(Math.random()*2-1)*ship_position_distance_from_center*Formulas.sinus_in(color_clamp));
			}
			ship_move_to = new Vector2f(ship_move_to.x,rand_y);
//			System.out.println("y: "+ship_move_to.y);
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
	
	private void exit() {
		exit_initiated = true;
	}
	
	private void drawFrame()
	{
		Main.wnd.clear ( clear_color );
		if (color_clamp<1f && fase_1)	for (Sprite star : stars) {
			Main.wnd.draw ( star );
		}
		if (fase_3) for (Sprite thing : speedness) {
			Main.wnd.draw ( thing );
		}
		if (fase_1) Main.wnd.draw ( m_sky );
		if (fase_2) Main.wnd.draw ( img_mountains );
		Main.wnd.draw ( m_ship );
		if (fase_2) Main.wnd.draw ( img_grass );
		if (fase_1) Main.wnd.draw ( box );
		Main.wnd.draw(hud);
		if (fase_2) Main.wnd.draw( flash );
		Main.wnd.display ( );
	}
}
