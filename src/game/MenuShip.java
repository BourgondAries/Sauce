package game;

import org.jsfml.audio.Sound;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import engine.SyncTrack;

public class MenuShip implements Drawable {
	private Vector2f position;
	private Sprite ship;
	private long spawned;
	private float relative_distance;
	private float depth;
	private float flight_time;
	private float wait_time;
	private float distance_relation;
	private boolean have_played_incomming_sound;
	private boolean turn_ship;
	private Sound local_aud_teleport_far;
	private Sound local_aud_teleport_close;

	// These have to be set from another class before spawning any ship!
	public static Sprite body;
	public static Sprite smoke;
	public static SyncTrack teleport_far;
	public static SyncTrack teleport_close;
	public static float start_position;
	
	public static final float SHIP_HOVER_FREQUENCY = 2; // Waves per second
	public static final float SHIP_HOVER_AMPLITUDE = 1; // Relative distance to cover in a wave
	public static final float SHIP_FLIGHT_SPEED = 7; // Speed in relative pixels per millisecond
	public static final float SHIP_RELATIVE_DISTANCE = 10; // How much longer the ship farthest back must travel
	public static final float SHIP_MAX_SIZE_MULTIPLIER = 0.5f; // Maximum scale of the sprite
	public static final float SHIP_MIN_SIZE_MULTIPLIER = 0.05f; // Minimum scale of the sprite
	public static final float SHIP_TIME_BEFORE_STOP_TO_PLAY_SOUND = 200; // Milliseconds before sound
	
	public MenuShip(float to_x, float to_y, long diff_time, float depth) {
		
		// Setup body
		ship = new Sprite(body.getTexture(),body.getTextureRect());
		
		// Setup position
		position = new Vector2f(to_x,to_y);
		ship.setPosition(start_position, to_y);
		this.depth = depth;
		
		// Setup scale
		float size_factor = depth*(SHIP_MAX_SIZE_MULTIPLIER-SHIP_MIN_SIZE_MULTIPLIER)+SHIP_MIN_SIZE_MULTIPLIER;
		ship.setScale(size_factor, size_factor);
		
		// Setup speed
		spawned = diff_time;
		distance_relation = (1-depth)*SHIP_RELATIVE_DISTANCE+1;
		relative_distance = (start_position - to_x)*distance_relation;
		flight_time = relative_distance/SHIP_FLIGHT_SPEED;
		
		// Setup sounds
		have_played_incomming_sound = false;
		
		// Setup turning
		turn_ship = false;
		
		// Setup waiting at start
		wait_time = SHIP_TIME_BEFORE_STOP_TO_PLAY_SOUND - flight_time;
	}
	
	public void update(long diff_time) {
		
		// Calculate time since start
		long time_since_spawn = diff_time - spawned;
		
		// Calculate time since flight
		long time_since_flight = (long) (diff_time - wait_time - spawned);
		
		if (time_since_flight < flight_time) {
			
			// Find time until stop
			float time_remaining = flight_time + wait_time - time_since_spawn;
			
			if (!have_played_incomming_sound && time_remaining < SHIP_TIME_BEFORE_STOP_TO_PLAY_SOUND) {
				
				// Tell script that sound has been played
				have_played_incomming_sound = true;
				
				// Setup sounds
				local_aud_teleport_far = teleport_far.getTrack();
				local_aud_teleport_close = teleport_close.getTrack();
				local_aud_teleport_far.setVolume((1-depth)*100);
				local_aud_teleport_close.setVolume(depth*100);
				local_aud_teleport_far.play();
				local_aud_teleport_close.play();
			}
			
			// Check if the ship has to wait before flying for the sound to play correctly
			if (wait_time > time_since_spawn) return;
			
			// Update ship position
			float new_distance = relative_distance - SHIP_FLIGHT_SPEED*time_since_flight;
			ship.setPosition(position.x + new_distance, position.y);
			
		} else if (!turn_ship) {
			
			// Make ship hover
			float sine_x = (time_since_flight-flight_time)*SHIP_HOVER_FREQUENCY/1000;
			float amplitude = SHIP_HOVER_AMPLITUDE/distance_relation;
			
			ship.setPosition(
					ship.getPosition().x,
					(float) (ship.getPosition().y + Math.sin(sine_x)*amplitude));
		}
	}
	
	public void turnShip(long diff_time) {
		turn_ship = true;
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		ship.draw(arg0, arg1);
	}
}
