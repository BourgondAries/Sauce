package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import engine.Button;
import engine.Formulas;
import engine.PathedFonts;
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
	private SyncTrack aud_button_press;
	private SyncTrack aud_button_hover;
	
	// Graphic
	private Sprite img_planet;
	private Sprite img_planet_dark;
	private Sprite img_planet_atmosphere;
	private Sprite img_sun;
	private Sprite img_close_ship;
	private Sprite img_close_ship_red;
	private Sprite img_close_ship_cyan;
	private Sprite img_anim_smoke;
	private Sprite img_anim_ship;
	private Sprite img_button_left;
	private Sprite img_button_right;
	private Sprite img_button_left_white;
	private Sprite img_button_right_white;
	private Sprite img_button_border_left;
	private Sprite img_button_border_right;
	private Sprite img_ship_flames_forward;
	private Sprite img_ship_flames_backward;
	
	// Tilable graphic
	private final static int SPACE_EXTRA_SIZE = 100;
	private Sprite img_space;
	private Sprite img_space_blur;
	private Sprite img_button_middle;
	private Sprite img_button_middle_white;
	
	private final static int MENU_UNDERLINE_SPACING_FIRE = -10; // 0
	private final static int MENU_UNDERLINE_SPACING_WINDOW_X = 65; // 100
	private ImageScrambler scr_menu_underline_middle_left;
	private ImageScrambler scr_menu_underline_middle_right;
	
	// Fonts
	private Font fon_button;
	
	// Scramblable graphic
	private ImageScrambler scr_menu_border_left_down;
	private ImageScrambler scr_menu_border_right_down;
	private ImageScrambler scr_menu_border_left_up;
	private ImageScrambler scr_menu_border_right_up;
	private ImageScrambler scr_menu_s;
	private ImageScrambler scr_menu_h;
	private ImageScrambler scr_menu_c;
	private ImageScrambler scr_menu_t;
	private ImageScrambler scr_menu_fire;
	private ImageScrambler scr_menu_underline_left_left;
	private ImageScrambler scr_menu_underline_right_left;
	private ImageScrambler scr_menu_underline_left_right;
	private ImageScrambler scr_menu_underline_right_right;
	
	// Render Queue
	ArrayList<Drawable> render_queue = new ArrayList<>();
	ArrayList<MenuShip> menu_ships = new ArrayList<>();
	
	// Skip menu
	private boolean skip_intro = false;
	
	public Menu() throws IOException, TextureCreationException {
		Main.view.setCenter(Vector2f.div(new Vector2f(Main.wnd.getSize()),2));
		Main.wnd.setView(Main.view);
		spashScreen();
		loadResources();
		long time_used = playIntro();
		time_used = runMenu(time_used);
		playOutro(time_used);
		SyncTrack.cleanList();
	}

	private void spashScreen() {
		Main.wnd.clear(new Color(0,0,0));
		Main.wnd.display();
	}
	
	private void loadResources() throws IOException, TextureCreationException {
		
		// Music (streamed from file)
		aud_intro = loadMusic("sfx/intro_short.ogg");
		aud_menu_loop = loadMusic("sfx/menu_loop_rock.ogg");
		
		// Sounds (loaded into memory)
		aud_menu_switch = loadSound("sfx/menu_switch.ogg");
		aud_teleport_far = loadSound("sfx/teleport_far.ogg");
		aud_teleport_close = loadSound("sfx/teleport_close.ogg");
		aud_button_press = loadSound("sfx/button_press.ogg");
		aud_button_hover = loadSound("sfx/button_hover.ogg");
		
		// Fonts
		fon_button = loadFont("res/pixelmix.ttf");
		
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
		img_button_left = loadImage("res/menu/holo/small/button_left.tga");
		img_button_right = loadImage("res/menu/holo/small/button_right.tga");
		img_button_left_white = loadImage("res/menu/holo/small/button_left_white.tga");
		img_button_right_white = loadImage("res/menu/holo/small/button_right_white.tga");
		img_button_border_left = loadImage("res/menu/holo/small/button_border_left.tga");
		img_button_border_right = loadImage("res/menu/holo/small/button_border_right.tga");
		
		img_anim_smoke = loadAnimatedImage("res/menu/smoke.tga",320,320,4);
		img_anim_ship = loadAnimatedImage("res/menu/ship.tga",300,300,9);
		
		img_space = loadTilableImage("res/menu/space.tga", Main.wnd.getSize().x + SPACE_EXTRA_SIZE, Main.wnd.getSize().y + SPACE_EXTRA_SIZE);
		img_space_blur = loadTilableImage("res/menu/space_blur.tga", Main.wnd.getSize().x + SPACE_EXTRA_SIZE, Main.wnd.getSize().y + SPACE_EXTRA_SIZE);
		img_button_middle = loadTilableImage("res/menu/holo/small/button_middle.tga", 1, 124);
		img_button_middle_white = loadTilableImage("res/menu/holo/small/button_middle_white.tga", 1, 124);
		
		scr_menu_underline_left_left = new ImageScrambler(loadImage("res/menu/holo/small/menu_underline_left.tga"));
		scr_menu_underline_right_left = new ImageScrambler(loadImage("res/menu/holo/small/menu_underline_right.tga"));
		scr_menu_underline_left_right = new ImageScrambler(loadImage("res/menu/holo/small/menu_underline_left.tga"));
		scr_menu_underline_right_right = new ImageScrambler(loadImage("res/menu/holo/small/menu_underline_right.tga"));
		scr_menu_border_left_down = new ImageScrambler(loadImage("res/menu/holo/small/menu_border_left_down.tga"));
		scr_menu_border_right_down = new ImageScrambler(loadImage("res/menu/holo/small/menu_border_right_down.tga"));
		scr_menu_border_left_up = new ImageScrambler(loadImage("res/menu/holo/small/menu_border_left_up.tga"));
		scr_menu_border_right_up = new ImageScrambler(loadImage("res/menu/holo/small/menu_border_right_up.tga"));
		scr_menu_s = new ImageScrambler(loadImage("res/menu/holo/small/menu_s.tga"));
		scr_menu_h = new ImageScrambler(loadImage("res/menu/holo/small/menu_h.tga"));
		scr_menu_fire = new ImageScrambler(loadImage("res/menu/holo/small/menu_fire.tga"));
		scr_menu_c = new ImageScrambler(loadImage("res/menu/holo/small/menu_c.tga"));
		scr_menu_t = new ImageScrambler(loadImage("res/menu/holo/small/menu_t.tga"));
		
		scr_menu_underline_middle_left = new ImageScrambler(loadTilableImage(
						"res/menu/holo/small/menu_underline_middle.tga",
						(int) (Main.wnd.getSize().x/2 -
						30 - //60
						MENU_UNDERLINE_SPACING_FIRE -
						MENU_UNDERLINE_SPACING_WINDOW_X -
						scr_menu_fire.fetchSprite().getLocalBounds().width/2),
						25)); //50
		
		scr_menu_underline_middle_right = new ImageScrambler(loadTilableImage(
						"res/menu/holo/small/menu_underline_middle.tga",
						(int) (Main.wnd.getSize().x/2 -
						30 - //60
						MENU_UNDERLINE_SPACING_FIRE -
						MENU_UNDERLINE_SPACING_WINDOW_X -
						scr_menu_fire.fetchSprite().getLocalBounds().width/2),
						25)); //50
	}
	
	private Music loadMusic(String path) throws IOException {
		Music new_sound = new Music();
		new_sound.openFromFile(Paths.get(path));
		return new_sound;
	}
	
	private Font loadFont(String path) throws IOException {
		return PathedFonts.getFont(Paths.get(path));
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
		float time_to_stop_move_planets = 9900; // 16000
		float time_to_start_spawn_ships = 1000; // 4000
		float time_to_stop_spawn_ships = 8500; // 14000
		float time_to_teleport_closest_ship = 9400; // 14700
		float duration_of_teleport_effect_on_closest_ship = 1000; // 1000
		float time_to_spawn_menu = 9900; // 15500
		float duration_of_menu_flickering = 500; // 500
		float where_to_start_menu_loop = 29000; // 29000
		
		// Constants
		float teleport_effect_on_closest_ship_amplitude = 100;
		float menu_effect_amplitude = 20;
		float menu_border_spacing = 10; // 0
		float menu_underline_spacing_y = 180; // 294
		float menu_letters_spacing_underline = 10; // 0
		float menu_fire_spacing_top = 60; // 50
		float menu_letters_spacing_multiplier = 0.8f;
		float menu_loop_volume = 40;
		float menu_button_volume = 10;
		float menu_switch_volume = 50;
		
		// Probability
		float likeliness_of_spawning_ship = 0.03f; // 0.02f
		
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
		
		// Setup origins
		img_close_ship.setOrigin(
				img_close_ship.getLocalBounds().width/2,
				img_close_ship.getLocalBounds().height/2);
		img_close_ship_red.setOrigin(
				img_close_ship_red.getLocalBounds().width/2,
				img_close_ship_red.getLocalBounds().height/2);
		img_close_ship_cyan.setOrigin(
				img_close_ship_cyan.getLocalBounds().width/2,
				img_close_ship_cyan.getLocalBounds().height/2);
		
		// Setup render queue
		render_queue.addAll(Arrays.asList(
				img_space,
				img_space_blur,
				img_sun,
				img_planet_dark,
				img_planet,
				img_planet_atmosphere
		));
		
		// Setup ships
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
		
		Vector2f closest_ship_start = new Vector2f(
				coordinates_top_right.x/2, coordinates_left_bottom.y/2);
		
		scr_menu_border_left_down.updatePosition(new Vector2f(
				coordinates_left_bottom.x + menu_border_spacing,
				coordinates_left_bottom.y - menu_border_spacing - scr_menu_border_left_down.fetchSprite().getLocalBounds().height));
		
		scr_menu_border_left_up.updatePosition(new Vector2f(
				coordinates_left_bottom.x + menu_border_spacing,
				coordinates_top_right.y + menu_border_spacing));
		
		scr_menu_border_right_down.updatePosition(new Vector2f(
				coordinates_top_right.x - menu_border_spacing - scr_menu_border_right_down.fetchSprite().getLocalBounds().width,
				coordinates_left_bottom.y - menu_border_spacing - scr_menu_border_right_down.fetchSprite().getLocalBounds().height));
		
		scr_menu_border_right_up.updatePosition(new Vector2f(
				coordinates_top_right.x - menu_border_spacing - scr_menu_border_right_up.fetchSprite().getLocalBounds().width,
				coordinates_top_right.y + menu_border_spacing));
		
		scr_menu_fire.updatePosition(new Vector2f(
				coordinates_top_right.x/2 - scr_menu_fire.fetchSprite().getLocalBounds().width/2,
				menu_fire_spacing_top));
		
		scr_menu_underline_left_left.updatePosition(new Vector2f(
				MENU_UNDERLINE_SPACING_WINDOW_X,
				menu_underline_spacing_y));
		
		scr_menu_underline_right_left.updatePosition(new Vector2f(
				coordinates_top_right.x/2 -
				scr_menu_fire.fetchSprite().getLocalBounds().width/2 -
				scr_menu_underline_right_left.fetchSprite().getLocalBounds().width -
				MENU_UNDERLINE_SPACING_FIRE,
				menu_underline_spacing_y));
		
		scr_menu_underline_middle_left.updatePosition(new Vector2f(
				MENU_UNDERLINE_SPACING_WINDOW_X + scr_menu_underline_left_left.fetchSprite().getLocalBounds().width,
				menu_underline_spacing_y));
		
		scr_menu_underline_right_right.updatePosition(new Vector2f(
				coordinates_top_right.x -
				MENU_UNDERLINE_SPACING_WINDOW_X -
				scr_menu_underline_right_right.fetchSprite().getLocalBounds().width,
				menu_underline_spacing_y));
		
		scr_menu_underline_left_right.updatePosition(new Vector2f(
				coordinates_top_right.x/2 +
				scr_menu_fire.fetchSprite().getLocalBounds().width/2 +
				MENU_UNDERLINE_SPACING_FIRE,
				menu_underline_spacing_y));
		
		scr_menu_underline_middle_right.updatePosition(new Vector2f(
				coordinates_top_right.x/2 +
				scr_menu_fire.fetchSprite().getLocalBounds().width/2 +
				MENU_UNDERLINE_SPACING_FIRE +
				scr_menu_underline_left_right.fetchSprite().getLocalBounds().width,
				menu_underline_spacing_y));
		
		scr_menu_s.updatePosition(new Vector2f(
				scr_menu_underline_middle_left.fetchSprite().getPosition().x +
				scr_menu_underline_middle_left.fetchSprite().getLocalBounds().width *
				menu_letters_spacing_multiplier/3 -
				scr_menu_s.fetchSprite().getLocalBounds().width/2,
				menu_underline_spacing_y -
				menu_letters_spacing_underline -
				scr_menu_s.fetchSprite().getLocalBounds().height));
		
		scr_menu_h.updatePosition(new Vector2f(
				scr_menu_underline_middle_left.fetchSprite().getPosition().x +
				scr_menu_underline_middle_left.fetchSprite().getLocalBounds().width -
				scr_menu_underline_middle_left.fetchSprite().getLocalBounds().width *
				menu_letters_spacing_multiplier/3 -
				scr_menu_h.fetchSprite().getLocalBounds().width/2,
				menu_underline_spacing_y -
				menu_letters_spacing_underline -
				scr_menu_h.fetchSprite().getLocalBounds().height));
		
		scr_menu_c.updatePosition(new Vector2f(
				scr_menu_underline_middle_right.fetchSprite().getPosition().x +
				scr_menu_underline_middle_right.fetchSprite().getLocalBounds().width *
				menu_letters_spacing_multiplier/3 -
				scr_menu_c.fetchSprite().getLocalBounds().width/2,
				menu_underline_spacing_y -
				menu_letters_spacing_underline -
				scr_menu_c.fetchSprite().getLocalBounds().height));
		
		scr_menu_t.updatePosition(new Vector2f(
				scr_menu_underline_middle_right.fetchSprite().getPosition().x +
				scr_menu_underline_middle_right.fetchSprite().getLocalBounds().width -
				scr_menu_underline_middle_right.fetchSprite().getLocalBounds().width *
				menu_letters_spacing_multiplier/3 -
				scr_menu_t.fetchSprite().getLocalBounds().width/2,
				menu_underline_spacing_y -
				menu_letters_spacing_underline -
				scr_menu_t.fetchSprite().getLocalBounds().height));
		
		// Setup music
		aud_menu_loop.setLoop(true);
		aud_menu_loop.setVolume(menu_loop_volume);
		aud_button_hover.fetchTrack().setVolume(menu_button_volume);
		aud_menu_switch.fetchTrack().setVolume(menu_switch_volume);
		aud_intro.play();
		
		// Setup skipping
		long skip_ahead = 0;
		
		// Setup start time
		long start_time = System.currentTimeMillis();
		
		while(true) {
			
			// Calculate time since start in milliseconds
			long diff_time = System.currentTimeMillis() - start_time + skip_ahead;
			
			// Skip
			if (skip_intro && diff_time < time_to_teleport_closest_ship) {
				skip_ahead = (long) (time_to_teleport_closest_ship) - diff_time;
				skip_intro = false;
				continue;
			}
			
			// Handle phases
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
				
				//slow_stop = slow_stop>1 ? 1 : slow_stop<0 ? 0 : slow_stop;
				//slow_top_then_return = slow_top_then_return>1 ? 1 : slow_top_then_return<0 ? 0 : slow_top_then_return;
				
				// Update transparency
				Color color = new Color(255,255,255,(int) (Formulas.slow_stop(x)*255));
				img_planet.setColor(color);
				img_space.setColor(color);
				img_planet_atmosphere.setColor(new Color(255,255,255,(int) (Formulas.very_slow_stop(x)*255)));
				img_space_blur.setColor(new Color(255,255,255,(int) (Formulas.slow_top_then_return(x)*255)));
				
				// Update position
				img_planet.setPosition(Vector2f.add(
								planet_start,
								Vector2f.mul(planet_move, Formulas.slow_stop(x))));
				img_planet_atmosphere.setPosition(img_planet.getPosition());
				img_planet_dark.setPosition(img_planet.getPosition());
				
				img_sun.setPosition(Vector2f.add(
						planet_start,
						Vector2f.mul(sun_move, Formulas.slow_stop(x))));
				
				img_space.setPosition(Vector2f.add(
						space_start,
						Vector2f.mul(space_move, Formulas.slow_stop(x))));
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
					
					// Update transparency
					img_close_ship.setColor(new Color(255,255,255,(int) (x*255)));
					img_close_ship_cyan.setColor(new Color(255,255,255,(int) (Formulas.slow_top_then_return(x)*255)));
					img_close_ship_red.setColor(new Color(255,255,255,(int) (Formulas.slow_top_then_return(x)*255)));
					
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
					
					// Calculate percent complete
					float x = (diff_time - time_to_spawn_menu) /
							duration_of_menu_flickering;
					
					// Play sound and make menu renderable if not already done
					if (!showed_menu) {
						render_queue.add(scr_menu_s);
						render_queue.add(scr_menu_h);
						render_queue.add(scr_menu_c);
						render_queue.add(scr_menu_t);
						render_queue.add(scr_menu_fire);
						render_queue.add(scr_menu_underline_left_left);
						render_queue.add(scr_menu_underline_middle_left);
						render_queue.add(scr_menu_underline_right_left);
						render_queue.add(scr_menu_underline_left_right);
						render_queue.add(scr_menu_underline_middle_right);
						render_queue.add(scr_menu_underline_right_right);
						render_queue.add(scr_menu_border_left_down);
						render_queue.add(scr_menu_border_right_down);
						render_queue.add(scr_menu_border_left_up);
						render_queue.add(scr_menu_border_right_up);
						
						showed_menu = true;
						aud_menu_switch.play();
					}
					
					// Update flicker-values
					float max_distort = (1-x)*menu_effect_amplitude;
					scr_menu_s.setMaxDistort(max_distort);
					scr_menu_h.setMaxDistort(max_distort);
					scr_menu_c.setMaxDistort(max_distort);
					scr_menu_t.setMaxDistort(max_distort);
					scr_menu_fire.setMaxDistort(max_distort);
					scr_menu_underline_left_left.setMaxDistort(max_distort);
					scr_menu_underline_middle_left.setMaxDistort(max_distort);
					scr_menu_underline_right_left.setMaxDistort(max_distort);
					scr_menu_underline_left_right.setMaxDistort(max_distort);
					scr_menu_underline_middle_right.setMaxDistort(max_distort);
					scr_menu_underline_right_right.setMaxDistort(max_distort);
					scr_menu_border_left_down.setMaxDistort(max_distort);
					scr_menu_border_right_down.setMaxDistort(max_distort);
					scr_menu_border_left_up.setMaxDistort(max_distort);
					scr_menu_border_right_up.setMaxDistort(max_distort);
					
					float visibility = x;
					scr_menu_s.setVisibility(visibility);
					scr_menu_h.setVisibility(visibility);
					scr_menu_c.setVisibility(visibility);
					scr_menu_t.setVisibility(visibility);
					scr_menu_fire.setVisibility(visibility);
					scr_menu_underline_left_left.setVisibility(visibility);
					scr_menu_underline_middle_left.setVisibility(visibility);
					scr_menu_underline_right_left.setVisibility(visibility);
					scr_menu_underline_left_right.setVisibility(visibility);
					scr_menu_underline_middle_right.setVisibility(visibility);
					scr_menu_underline_right_right.setVisibility(visibility);
					scr_menu_border_left_down.setVisibility(visibility);
					scr_menu_border_right_down.setVisibility(visibility);
					scr_menu_border_left_up.setVisibility(visibility);
					scr_menu_border_right_up.setVisibility(visibility);
					
					// Make everything flicker
					scr_menu_s.scramble();
					scr_menu_h.scramble();
					scr_menu_c.scramble();
					scr_menu_t.scramble();
					scr_menu_fire.scramble();
					scr_menu_underline_left_left.scramble();
					scr_menu_underline_middle_left.scramble();
					scr_menu_underline_right_left.scramble();
					scr_menu_underline_left_right.scramble();
					scr_menu_underline_middle_right.scramble();
					scr_menu_underline_right_right.scramble();
					scr_menu_border_left_down.scramble();
					scr_menu_border_right_down.scramble();
					scr_menu_border_left_up.scramble();
					scr_menu_border_right_up.scramble();
					
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
			handleEvents();
			drawFrame();
		}
	}
	
	private long runMenu(long time_so_far) {
		
		// Update flicker-values
		float max_distort = 2;
		scr_menu_s.setMaxDistort(max_distort);
		scr_menu_h.setMaxDistort(max_distort);
		scr_menu_c.setMaxDistort(max_distort);
		scr_menu_t.setMaxDistort(max_distort);
		
		float visibility = 1;
		scr_menu_s.setVisibility(visibility);
		scr_menu_h.setVisibility(visibility);
		scr_menu_c.setVisibility(visibility);
		scr_menu_t.setVisibility(visibility);
		
		// Ease the load on the GPU
		scr_menu_fire.simpleRender(true);
		scr_menu_border_left_down.simpleRender(true);
		scr_menu_border_right_down.simpleRender(true);
		scr_menu_border_left_up.simpleRender(true);
		scr_menu_border_right_up.simpleRender(true);
		scr_menu_underline_left_left.simpleRender(true);
		scr_menu_underline_middle_left.simpleRender(true);
		scr_menu_underline_right_left.simpleRender(true);
		scr_menu_underline_left_right.simpleRender(true);
		scr_menu_underline_middle_right.simpleRender(true);
		scr_menu_underline_right_right.simpleRender(true);
		
		// Setup buttons
		Button.img_button_border_left = img_button_border_left;
		Button.img_button_border_right = img_button_border_right;
		Button.img_button_left = img_button_left;
		Button.img_button_middle = img_button_middle;
		Button.img_button_right = img_button_right;
		Button.img_button_left_white = img_button_left_white;
		Button.img_button_middle_white = img_button_middle_white;
		Button.img_button_right_white = img_button_right_white;
		Button.aud_button_hover = aud_button_hover;
		Button.aud_button_press = aud_button_press;
		Button.fon_button = fon_button;
		
		// Button settings
		float button_distance_from_sides = 80; // 150
		float button_distance_from_bottom = 0; // 150
		float button_distance_from_top = 0; // 100
		float number_of_button_places = 3;
		
		float button_placement_space_y =
				Main.wnd.getSize().y -
				scr_menu_underline_left_left.fetchSprite().getPosition().y -
				scr_menu_underline_left_left.fetchSprite().getLocalBounds().height -
				button_distance_from_bottom -
				button_distance_from_top;
		float button_placement_start_y =
				scr_menu_underline_left_left.fetchSprite().getPosition().y +
				scr_menu_underline_left_left.fetchSprite().getLocalBounds().height +
				button_distance_from_top;
		float space_between_buttons = button_placement_space_y/(number_of_button_places+1);
		float button_width =
				Main.wnd.getSize().x -
				2*button_distance_from_sides;
		
		// Make buttons
		Button btn_play = new Button(new Vector2f(
				button_distance_from_sides,
				button_placement_start_y +
				space_between_buttons -
				Button.img_button_middle.getLocalBounds().height/2),
				button_width,
				"PLAY");
		btn_play.doPlayStart();
		render_queue.add(btn_play);
		
		Button btn_score = new Button(new Vector2f(
				button_distance_from_sides,
				button_placement_start_y +
				space_between_buttons*2 -
				Button.img_button_middle.getLocalBounds().height/2),
				button_width,
				"CORE");
		btn_score.doPlayStart();
		render_queue.add(btn_score);
		
		Button btn_exit = new Button(new Vector2f(
				button_distance_from_sides,
				button_placement_start_y +
				space_between_buttons*3 -
				Button.img_button_middle.getLocalBounds().height/2),
				button_width,
				"EXIT");
		btn_exit.doPlayStart();
		render_queue.add(btn_exit);
		
		// Start time
		long start_time = System.currentTimeMillis();
		
		while (true) {
			
			// Menu uptime
			long uptime = System.currentTimeMillis() - start_time;
			
			// Update buttons
			boolean play_state = btn_play.update(
					new Vector2f(Mouse.getPosition(Main.wnd)),
					Mouse.isButtonPressed(Mouse.Button.LEFT));
			boolean score_state = btn_score.update(
					new Vector2f(Mouse.getPosition(Main.wnd)),
					Mouse.isButtonPressed(Mouse.Button.LEFT));
			boolean exit_state = btn_exit.update(
					new Vector2f(Mouse.getPosition(Main.wnd)),
					Mouse.isButtonPressed(Mouse.Button.LEFT));
			
			if (play_state) {
				System.out.println("play");
				Main.game_state = Main.states.tutorial;
				return time_so_far+uptime;
			}
			
			if (exit_state) {
				System.out.println("exit");
				Main.dispose();
			}
			
			if (score_state) {
				System.out.println("core");
				Main.game_state = Main.states.core;
				return time_so_far+uptime;
			}
			
			// Flicker effect on menu
			scr_menu_s.scramble();
			scr_menu_h.scramble();
			scr_menu_c.scramble();
			scr_menu_t.scramble();
			
			// Update existing ships
			for (MenuShip ship : menu_ships) {
				ship.update(time_so_far+uptime);
			}
			
			// Draw the frame
			handleEvents();
			drawFrame();
		}
	}
	
	private void playOutro(long time_so_far) {
		
		// Timings
		float time_to_activate_all_ships = 2000;
		float time_to_turn_closest_ship = 2500;
		float rotation_time = 1000;
		float rotation_angle = 65;
		float wait_before_fire = 1000;
		float duration_of_menu_fade = 500;
		float menu_effect_amplitude = 20;
		
		// Poor GPU...
		scr_menu_fire.simpleRender(false);
		scr_menu_border_left_down.simpleRender(false);
		scr_menu_border_right_down.simpleRender(false);
		scr_menu_border_left_up.simpleRender(false);
		scr_menu_border_right_up.simpleRender(false);
		scr_menu_underline_left_left.simpleRender(false);
		scr_menu_underline_middle_left.simpleRender(false);
		scr_menu_underline_right_left.simpleRender(false);
		scr_menu_underline_left_right.simpleRender(false);
		scr_menu_underline_middle_right.simpleRender(false);
		scr_menu_underline_right_right.simpleRender(false);
		
		// Start menu-sound
		aud_menu_switch.play();
		
		// Check if menu-stuff is already erased
		boolean menu_gone = false;
		
		// Fetch the menu_loop volume so the fade is smooth
		float menu_loop_volume = aud_menu_loop.getVolume();
		
		// Start time
		long start_time = System.currentTimeMillis();
		
		while (true) {
			
			// Outro uptime
			long uptime = System.currentTimeMillis() - start_time;
			
			// Fade menu out
			if (uptime<duration_of_menu_fade) {
				
				// Calculate percent complete
				float x = uptime/duration_of_menu_fade;
				
				// Lower volume
				aud_menu_loop.setVolume(menu_loop_volume*(1-x));
				
				// Update flicker-values
				float max_distort = x*menu_effect_amplitude;
				scr_menu_s.setMaxDistort(max_distort);
				scr_menu_h.setMaxDistort(max_distort);
				scr_menu_c.setMaxDistort(max_distort);
				scr_menu_t.setMaxDistort(max_distort);
				scr_menu_fire.setMaxDistort(max_distort);
				scr_menu_underline_left_left.setMaxDistort(max_distort);
				scr_menu_underline_middle_left.setMaxDistort(max_distort);
				scr_menu_underline_right_left.setMaxDistort(max_distort);
				scr_menu_underline_left_right.setMaxDistort(max_distort);
				scr_menu_underline_middle_right.setMaxDistort(max_distort);
				scr_menu_underline_right_right.setMaxDistort(max_distort);
				scr_menu_border_left_down.setMaxDistort(max_distort);
				scr_menu_border_right_down.setMaxDistort(max_distort);
				scr_menu_border_left_up.setMaxDistort(max_distort);
				scr_menu_border_right_up.setMaxDistort(max_distort);
				
				float visibility = 1-x;
				scr_menu_s.setVisibility(visibility);
				scr_menu_h.setVisibility(visibility);
				scr_menu_c.setVisibility(visibility);
				scr_menu_t.setVisibility(visibility);
				scr_menu_fire.setVisibility(visibility);
				scr_menu_underline_left_left.setVisibility(visibility);
				scr_menu_underline_middle_left.setVisibility(visibility);
				scr_menu_underline_right_left.setVisibility(visibility);
				scr_menu_underline_left_right.setVisibility(visibility);
				scr_menu_underline_middle_right.setVisibility(visibility);
				scr_menu_underline_right_right.setVisibility(visibility);
				scr_menu_border_left_down.setVisibility(visibility);
				scr_menu_border_right_down.setVisibility(visibility);
				scr_menu_border_left_up.setVisibility(visibility);
				scr_menu_border_right_up.setVisibility(visibility);
				
				// Make everything flicker
				scr_menu_s.scramble();
				scr_menu_h.scramble();
				scr_menu_c.scramble();
				scr_menu_t.scramble();
				scr_menu_fire.scramble();
				scr_menu_underline_left_left.scramble();
				scr_menu_underline_middle_left.scramble();
				scr_menu_underline_right_left.scramble();
				scr_menu_underline_left_right.scramble();
				scr_menu_underline_middle_right.scramble();
				scr_menu_underline_right_right.scramble();
				scr_menu_border_left_down.scramble();
				scr_menu_border_right_down.scramble();
				scr_menu_border_left_up.scramble();
				scr_menu_border_right_up.scramble();
			} else if (!menu_gone) {
				menu_gone = true;
				aud_menu_loop.stop();
				render_queue.remove(scr_menu_border_left_down);
				render_queue.remove(scr_menu_border_left_up);
				render_queue.remove(scr_menu_border_right_down);
				render_queue.remove(scr_menu_border_right_up);
				render_queue.remove(scr_menu_s);
				render_queue.remove(scr_menu_h);
				render_queue.remove(scr_menu_fire);
				render_queue.remove(scr_menu_c);
				render_queue.remove(scr_menu_t);
				render_queue.remove(scr_menu_underline_left_left);
				render_queue.remove(scr_menu_underline_left_right);
				render_queue.remove(scr_menu_underline_middle_left);
				render_queue.remove(scr_menu_underline_middle_right);
				render_queue.remove(scr_menu_underline_right_left);
				render_queue.remove(scr_menu_underline_right_right);
			}
			
			// Activate ships
			if (uptime<=time_to_activate_all_ships) {
				float completeness_of_activation = uptime/time_to_activate_all_ships;
				for (MenuShip ship : menu_ships) {
					if (completeness_of_activation>=ship.getDepth()) ship.turnShip();
				}
			}
			
			// Update ships
			for (MenuShip ship : menu_ships) {
				ship.update(uptime+time_so_far);
			}
			
			if (uptime-time_to_turn_closest_ship<rotation_time && uptime>time_to_turn_closest_ship) {
				
				// Calculate completeness
				float x = (uptime-time_to_turn_closest_ship)/rotation_time;
				
				// Rotate view
				Main.view.setRotation(Formulas.slow_stop(x)*rotation_angle);
				Main.wnd.setView(Main.view);
				
				// Rotate closest ship
				img_close_ship.setRotation(Formulas.slow_stop(x)*rotation_angle);
			}
			
			if (uptime-time_to_turn_closest_ship-rotation_time>=wait_before_fire) {
				Main.wnd.clear(new Color(0,0,0));
				Main.wnd.display();
				Main.view.setRotation(0);
				Main.wnd.setView(Main.view);
				return;
			}
			
			// Draw frame
			handleEvents();
			drawFrame();
		}
		
		//aud_menu_loop.stop();
	}
	
	private void drawFrame()
	{
		Main.wnd.clear();
		
		for (Drawable drawable : render_queue) {
			Main.wnd.draw(drawable);
		}
		
		Main.wnd.display();
	}
	
	private void skipIntro()
	{
		aud_intro.stop();
		skip_intro = true;
	}
	
	private void handleEvents() {
		for (Event event : Main.wnd.pollEvents()) {
			switch (event.type)
			{
				case KEY_PRESSED:
				{
					KeyEvent keyev = event.asKeyEvent();
					switch (keyev.key)
					{
						case ESCAPE:
							skipIntro();
							return;
						default:
							break;
					}
				} break;
			default:
				break;
			}
		}
	}
}
