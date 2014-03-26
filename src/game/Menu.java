package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import engine.PathedSounds;
import engine.PathedTextures;
import engine.SyncTrack;

public class Menu {
	
	// Audio
	private Music aud_intro;
	private Music aud_menu_loop;
	private SyncTrack aud_menu_switch;
	private SyncTrack aud_teleport_far;
	private SyncTrack aud_teleport_close;
	
	// Graphic
	private Sprite img_planet;
	private Sprite img_planet_dark;
	private Sprite img_planet_atmosphere;
	private Sprite img_sun;
	private Sprite img_close_ship;
	private Sprite img_close_ship_red;
	private Sprite img_close_ship_cyan;
	private Sprite img_ship_flames_forward;
	private Sprite img_ship_flames_backward;
	private Sprite img_anim_smoke;
	private Sprite img_anim_ship;
	
	// Tilable graphic
	private final static int SPACE_EXTRA_SIZE = 100;
	private Sprite img_space;
	private Sprite img_space_blur;
	
	// Render Queue
	ArrayList<Drawable> render_queue = new ArrayList<>();
	ArrayList<MenuShip> menu_ships = new ArrayList<>();
	
	public Menu() throws IOException {
		spashScreen();
		loadResources();
		long time_used = playIntro();
		runMenu(time_used);
		playOutro();
		Main.game_state = Main.states.tutorial;
	}

	private void spashScreen() {
		Main.wnd.clear(new Color(0,0,0));
		Main.wnd.display();
	}
	
	private void loadResources() throws IOException {
		
		// Music (streamed form file)
		aud_intro = loadMusic("sfx/intro.ogg");
		aud_menu_loop = loadMusic("sfx/menu_loop.ogg");
		
		// Sounds (loaded into memory)
		aud_menu_switch = loadSound("sfx/menu_switch.ogg");
		aud_teleport_far = loadSound("sfx/teleport_far.ogg");
		aud_teleport_close = loadSound("sfx/teleport_close.ogg");
		
		// Textures
		img_planet = loadImage("res/menu/planet.tga");
		img_planet_atmosphere = loadImage("res/menu/planet_atmosphere.tga");
		img_planet_dark = loadImage("res/menu/planet_dark.tga");
		img_sun = loadImage("res/menu/sun.tga");
		img_close_ship = loadImage("res/menu/close_ship.tga");
		img_close_ship_red = loadImage("res/menu/close_ship_red.tga");
		img_close_ship_cyan = loadImage("res/menu/close_ship_cyan.tga");
		img_ship_flames_forward = loadImage("res/menu/ship_flames_forward.tga");
		img_ship_flames_backward = loadImage("res/menu/ship_flames_backward.tga");
		
		img_anim_smoke = loadAnimatedImage("res/menu/smoke.tga",320,320,4);
		img_anim_ship = loadAnimatedImage("res/menu/ship.tga",300,300,9);
		
		img_space = loadTilableImage("res/menu/space.tga", Main.wnd.getSize().x + SPACE_EXTRA_SIZE, Main.wnd.getSize().y + SPACE_EXTRA_SIZE);
		img_space_blur = loadTilableImage("res/menu/space_blur.tga", Main.wnd.getSize().x + SPACE_EXTRA_SIZE, Main.wnd.getSize().y + SPACE_EXTRA_SIZE);
	}
	
	private Music loadMusic(String path) throws IOException {
		Music new_sound = new Music();
		new_sound.openFromFile(Paths.get(path));
		return new_sound;
	}
	
	private SyncTrack loadSound(String path) throws IOException {
		Sound new_sound = new Sound();
		new_sound.setBuffer(PathedSounds.getSound(Paths.get(path)));
		return new SyncTrack(new_sound);
	}
	
	private Sprite loadImage(String path) throws IOException {
		Sprite new_sprite = new Sprite();
		new_sprite.setTexture(PathedTextures.getTexture(Paths.get(path)));
		return new_sprite;
	}
	
	private Sprite loadTilableImage(String path, int width, int height) throws IOException {
		Sprite new_sprite = new Sprite();
		Texture new_texture = PathedTextures.getTexture(Paths.get(path));
		new_texture.setRepeated(true);
		new_sprite.setTexture(new_texture);
		new_sprite.setTextureRect(new IntRect(0,0,width,height));
		return new_sprite;
	}
	
	private Sprite loadAnimatedImage(String path, int width, int height, int frames) throws IOException {
		//TODO: make animated sprites and find a way to load them
		Sprite new_sprite = loadImage(path);
		new_sprite.setTextureRect(new IntRect(0, 0, width, height));
		return new_sprite;
	}
	
	private long playIntro() {
		
		// Timings in milliseconds
		float time_to_stop_move_planets = 16000;
		float time_to_start_spawn_ships = 4000;
		float time_to_stop_spawn_ships = 14000;
		float time_to_teleport_closest_ship = 14700;
		float duration_of_teleport_effect_on_closest_ship = 1000;
		float time_to_spawn_menu = 15500;
		float duration_of_menu_flickering = 500;
		float where_to_start_menu_loop = 29000;
		
		// Constants
		float teleport_effect_on_closest_ship_amplitude = 100;
		
		// Probability
		float likeliness_of_spawning_ship = 0.02f;
		
		// Setup fases
		boolean move_planets = true;
		boolean spawn_ships = false;
		boolean played_menu_loop = false;
		boolean inside_ship = false;
		boolean showed_menu = false;
		boolean showed_closest_ship = false;
		
		// Setup visibility
		img_space.setColor(new Color(255,255,255,0));
		img_space_blur.setColor(new Color(255,255,255,0));
		img_planet.setColor(new Color(255,255,255,0));
		img_planet_atmosphere.setColor(new Color(255,255,255,0));
		img_close_ship.setColor(new Color(255,255,255,0));
		img_close_ship_cyan.setColor(new Color(255,255,255,0));
		img_close_ship_red.setColor(new Color(255,255,255,0));
		
		// Setup render queue
		render_queue.addAll(Arrays.asList(
				img_space,
				img_space_blur,
				img_sun,
				img_planet_dark,
				img_planet,
				img_planet_atmosphere
		));
		
		// Setup update queue
		MenuShip.body = img_anim_ship;
		MenuShip.smoke = img_anim_smoke;
		MenuShip.teleport_far = aud_teleport_far;
		MenuShip.teleport_close = aud_teleport_close;
		MenuShip.start_position = Main.wnd.getSize().x;
		
		// Setup positions in pixels
		Vector2f space_move = new Vector2f(SPACE_EXTRA_SIZE,-SPACE_EXTRA_SIZE);
		Vector2f planet_move = new Vector2f(-50,50);
		Vector2f sun_move = new Vector2f(80,-80);
		
		Vector2f coordinates_left_bottom = new Vector2f(0, Main.wnd.getSize().y);
		Vector2f coordinates_top_right = new Vector2f(Main.wnd.getSize().x, 0);
		
		Vector2f space_start = Vector2f.add(
				coordinates_top_right,
				new Vector2f(-img_space.getGlobalBounds().width,0));
		
		Vector2f planet_start = Vector2f.add(
				coordinates_left_bottom,
				new Vector2f(0,-img_planet.getGlobalBounds().height));
		
		Vector2f closest_ship_start = Vector2f.add(new Vector2f(
				coordinates_top_right.x/2, coordinates_left_bottom.y/2),
				new Vector2f(-img_close_ship.getGlobalBounds().width/2,-img_close_ship.getGlobalBounds().height/2));
		
		// Setup music
		aud_menu_loop.setLoop(true);
		aud_intro.play();
		
		// Time of start
		long start_time = System.currentTimeMillis();
		
		while(true) {
			
			// Calculate time since start in milliseconds
			long diff_time = System.currentTimeMillis() - start_time;
			
			// Handle fases
			if (diff_time<time_to_stop_move_planets) move_planets = true;
			else move_planets = false;
			if (diff_time>time_to_start_spawn_ships && diff_time<time_to_stop_spawn_ships) spawn_ships = true;
			else spawn_ships = false;
			if (diff_time>time_to_teleport_closest_ship) inside_ship = true;
			else inside_ship = false;
			
			// Move planets
			if (move_planets) {
				
				// Calculate formula for change
				float x = diff_time/time_to_stop_move_planets;
				
				float slow_stop = x*(2-x);
				float very_slow_stop = (float) (1-Math.pow(1-x,4));
				float slow_top_then_return = x*(1-x);
				
				//slow_stop = slow_stop>1 ? 1 : slow_stop<0 ? 0 : slow_stop;
				//slow_top_then_return = slow_top_then_return>1 ? 1 : slow_top_then_return<0 ? 0 : slow_top_then_return;
				
				// Update transparency
				Color color = new Color(255,255,255,(int) (slow_stop*255));
				img_planet.setColor(color);
				img_space.setColor(color);
				img_planet_atmosphere.setColor(new Color(255,255,255,(int) (very_slow_stop*255)));
				img_space_blur.setColor(new Color(255,255,255,(int) (slow_top_then_return*255)));
				
				// Update position
				img_planet.setPosition(Vector2f.add(
								planet_start,
								Vector2f.mul(planet_move, slow_stop)));
				img_planet_atmosphere.setPosition(img_planet.getPosition());
				img_planet_dark.setPosition(img_planet.getPosition());
				
				img_sun.setPosition(Vector2f.add(
						planet_start,
						Vector2f.mul(sun_move, slow_stop)));
				
				img_space.setPosition(Vector2f.add(
						space_start,
						Vector2f.mul(space_move, slow_stop)));
				img_space_blur.setPosition(img_space.getPosition());
			}
			
			// Ship spawning
			if (spawn_ships) {
				
				// Spawn new ship
				if (Math.random()<likeliness_of_spawning_ship) {
				
					// Sequence depth
					float depth =
							(diff_time - time_to_start_spawn_ships) /
							(time_to_stop_spawn_ships - time_to_start_spawn_ships);
					
					// Calculate scale the ship will get
					float ship_scale = (depth*(MenuShip.SHIP_MAX_SIZE_MULTIPLIER-1)+1)*MenuShip.SHIP_MIN_SIZE_MULTIPLIER;
					
					// Random position
					float x_domain = coordinates_top_right.x-img_anim_ship.getGlobalBounds().width*ship_scale;
					float x = (float) Math.random()*x_domain;
				
					float y_domain = (coordinates_left_bottom.y-img_anim_ship.getGlobalBounds().height*ship_scale); //*x/x_domain;
					float y = (float) Math.random()*y_domain;
					
					// Spawn ship
					MenuShip new_ship = new MenuShip(x, y, diff_time, depth);
					
					// Make new ship renderable and updateable
					menu_ships.add(new_ship);
					render_queue.add(new_ship);
				}
			}
			
			// Update existing ships
			for (MenuShip ship : menu_ships) {
				ship.update(diff_time);
			}
			
			// Closest ship
			if (inside_ship) {
				
				// Play menu song if not already
				if (!played_menu_loop) {
					aud_menu_loop.setPlayingOffset(Time.getMilliseconds((long) where_to_start_menu_loop));
					played_menu_loop = true;
				}
				
				// Make closest ship renderable if not already
				if (!showed_closest_ship) {
					showed_closest_ship = true;
					render_queue.add(img_close_ship);
					render_queue.add(img_close_ship_cyan);
					render_queue.add(img_close_ship_red);
				}
				
				// Apply effect on closest ship
				if (diff_time<duration_of_teleport_effect_on_closest_ship+time_to_teleport_closest_ship) {
					
					// Calculate percent complete
					float x = (diff_time - time_to_teleport_closest_ship) /
							duration_of_teleport_effect_on_closest_ship;
					
					float slow_stop = x*(2-x);
					float slow_top_then_return = x*(1-x);
					
					// Update transparency
					img_close_ship.setColor(new Color(255,255,255,(int) (x*255)));
					img_close_ship_cyan.setColor(new Color(255,255,255,(int) (slow_top_then_return*255)));
					img_close_ship_red.setColor(new Color(255,255,255,(int) (slow_top_then_return*255)));
					
					// Update movement
					img_close_ship_cyan.setPosition(Vector2f.add(
							closest_ship_start,
							new Vector2f(
									(float) (Math.random()*teleport_effect_on_closest_ship_amplitude*(1-x)),
									0)));
					
					img_close_ship_red.setPosition(Vector2f.add(
							closest_ship_start,
							new Vector2f(
									(float) (Math.random()*teleport_effect_on_closest_ship_amplitude*(1-x)),
									0)));
					
					img_close_ship.setPosition(Vector2f.add(
							closest_ship_start,
							new Vector2f(
									(float) (Math.random()*teleport_effect_on_closest_ship_amplitude*(1-x)),
									0)));
				}
				
				// Display menu
				if (diff_time>time_to_spawn_menu) {
					
					// Play sound and make menu renderable if not already done
					if (!showed_menu) {
						showed_menu = true;
						aud_menu_switch.play();
					}
					
					if (diff_time>duration_of_menu_flickering+time_to_spawn_menu) {
						
						// Make sure everything is opaque
						img_close_ship.setColor(new Color(255,255,255,255));
						img_planet_atmosphere.setColor(new Color(255,255,255,255));
						img_space.setColor(new Color(255,255,255,255));
						img_planet.setColor(new Color(255,255,255,255));
						
						// Make sure everything is in the right place
						img_close_ship.setPosition(closest_ship_start);
						img_planet.setPosition(Vector2f.add(planet_start,planet_move));
						img_planet_atmosphere.setPosition(Vector2f.add(planet_start,planet_move));
						img_sun.setPosition(Vector2f.add(planet_start,sun_move));
						img_space.setPosition(Vector2f.add(space_start,space_move));
						
						// Remove invisible stuff
						render_queue.remove(img_close_ship_cyan);
						render_queue.remove(img_close_ship_red);
						render_queue.remove(img_planet_dark);
						render_queue.remove(img_space_blur);
						
						// Head over to the interactive menu
						return diff_time;
					}
				}
			}
			
			drawFrame();
		}
	}
	
	private void runMenu(long time_so_far) {
		
		// Start time
		long start_time = System.currentTimeMillis();
		
		while (true) {
			
			// Menu uptime
			long uptime = System.currentTimeMillis() - start_time;
			
			// Update existing ships
			for (MenuShip ship : menu_ships) {
				ship.update(time_so_far+uptime);
			}
			
			// Draw the frame
			drawFrame();
		}
	}
	
	private void playOutro() {
		// TODO Auto-generated method stub	
	}
	
	private void drawFrame()
	{
		Main.wnd.clear ();
		
		for (Drawable drawable : render_queue) {
			Main.wnd.draw(drawable);
		}
		
		Main.wnd.display ();
	}
}
