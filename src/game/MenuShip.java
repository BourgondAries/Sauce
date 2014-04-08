package game;

import org.jsfml.audio.Sound;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import engine.AnimatedSprite;
import engine.Formulas;
import engine.SyncTrack;

public class MenuShip implements Drawable {
	private Vector2f position;
	private AnimatedSprite ship;
	private Sprite pod;
	private Sprite flame_forward;
	private Sprite flame_backward;
	private long spawned;
	private float relative_distance;
	private float depth;
	private float flight_time;
	private float wait_time;
	private float distance_relation;
	private boolean have_played_incomming_sound;
	private boolean turn_ship;
	private boolean turn_time_taken = false;
	private boolean has_changed_frame_to_stationary = false;
	private long turn_start;
	private float rotation_angle;
	private boolean pod_fired = false;
	private boolean has_breaked = false;
	private boolean turning_ship = false;
	private Vector2f pod_relative_distance;
	private Vector2f pod_to;
	private Vector2f pod_speed;

	// These have to be set from another class before spawning any ship!
	public static AnimatedSprite body;
	public static Sprite img_flame_forward;
	public static Sprite img_flame_backward;
	public static SyncTrack teleport_far;
	public static SyncTrack teleport_close;
	public static SyncTrack flames_far;
	public static SyncTrack flames_close;
	public static SyncTrack pod_fire_far;
	public static SyncTrack pod_fire_close;
	public static float start_position;
	
	public static final float SHIP_TIME_BEFORE_STOP_TURNING_PLAY_FLAMES = 100;
	public static final float SHIP_HOVER_FREQUENCY = 2; // Waves per second
	public static final float SHIP_HOVER_AMPLITUDE = 1; // Relative distance to cover in a wave
	public static final float SHIP_FLIGHT_SPEED = 20; // Speed in relative pixels per milliseconds
	public static final float SHIP_RELATIVE_DISTANCE = 10; // How much longer the ship farthest back must travel
	public static final float SHIP_MAX_SIZE_MULTIPLIER = 0.5f; // Maximum scale of the sprite
	public static final float SHIP_MIN_SIZE_MULTIPLIER = 0.05f; // Minimum scale of the sprite
	public static final float SHIP_TIME_BEFORE_STOP_TO_PLAY_SOUND = 500; // Milliseconds before sound
	public static final float SHIP_TURN_DURATION = 1500; // Time ship uses to turn
	public static final float SHIP_WAIT_AFTER_TURN = 3000; // Time the ship should wait after turning
	public static final float SHIP_POD_SPEED = 2; // Speed in relative pixels per milliseconds
	
	public MenuShip(float to_x, float to_y, long diff_time, float depth) {
		
		// Setup body
		ship = new AnimatedSprite(body);
		
		// Setup pod
		pod = new Sprite(ship.fetchSprite().getTexture(),ship.fetchFrames().get(ship.fetchFrames().size()-1));
		
		// Setup flames
		flame_backward = new Sprite(img_flame_backward.getTexture());
		flame_forward = new Sprite(img_flame_forward.getTexture());
		
		// Set correct origin
		ship.fetchSprite().setOrigin(
				ship.fetchSprite().getLocalBounds().width/2,
				ship.fetchSprite().getLocalBounds().height/2);
		pod.setOrigin(pod.getLocalBounds().width/2, pod.getLocalBounds().height/2);
		flame_backward.setOrigin(flame_backward.getLocalBounds().width/2, flame_backward.getLocalBounds().height/2);
		flame_forward.setOrigin(flame_forward.getLocalBounds().width/2, flame_forward.getLocalBounds().height/2);
		
		// Setup scale
		float size_factor = depth*(SHIP_MAX_SIZE_MULTIPLIER-SHIP_MIN_SIZE_MULTIPLIER)+SHIP_MIN_SIZE_MULTIPLIER;
		ship.fetchSprite().setScale(size_factor, size_factor);
		
		// Setup position
		position = new Vector2f(to_x,to_y);
		ship.fetchSprite().setPosition(start_position+ship.fetchSprite().getGlobalBounds().width/2, to_y);
		this.depth = depth;
		
		// Setup angle ship will rotate to
		rotation_angle = (float) (90-Math.toDegrees(Math.atan((Main.wnd.getSize().y-to_y)/to_x)));
		
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
		wait_time = wait_time<0 ? 0 : wait_time;
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
				Sound local_aud_teleport_far = teleport_far.getTrack();
				Sound local_aud_teleport_close = teleport_close.getTrack();
				local_aud_teleport_far.setVolume((1-depth)*100);
				local_aud_teleport_close.setVolume(depth*100);
				SyncTrack.play(local_aud_teleport_far);
				SyncTrack.play(local_aud_teleport_close);
			}
			
			// Check if the ship has to wait before flying for the sound to play correctly
			if (wait_time > time_since_spawn) return;
			
			// Update ship position
			float new_distance = relative_distance - SHIP_FLIGHT_SPEED*time_since_flight;
			ship.fetchSprite().setPosition(position.x + new_distance/distance_relation, position.y);
			
		} else if (!turn_ship) {
			
			// Change frame if not already
			if (!has_changed_frame_to_stationary) {
				ship.fetchSprite().setPosition(position);
				ship.next();
				has_changed_frame_to_stationary = true;
			}
			
			// Make ship hover
			float sine_x = (time_since_flight-flight_time)*SHIP_HOVER_FREQUENCY/1000;
			float amplitude = SHIP_HOVER_AMPLITUDE/distance_relation;
			
			ship.fetchSprite().setPosition(
					ship.fetchSprite().getPosition().x,
					(float) (ship.fetchSprite().getPosition().y + Math.sin(sine_x)*amplitude));
		} else {
			
			// Turn ship
			if (!turn_time_taken) {
				
				// Setup sounds
				Sound local_aud_flames_close = flames_close.getTrack();
				Sound local_aud_flames_far = flames_far.getTrack();
				local_aud_flames_far.setVolume((1-depth)*100);
				local_aud_flames_close.setVolume(depth*100);
				SyncTrack.play(local_aud_flames_close);
				SyncTrack.play(local_aud_flames_far);
				
				turn_time_taken = true;
				turn_start = diff_time;
			}
			
			// Time since started turning
			float uptime = diff_time-turn_start;
			
			if (uptime<SHIP_TURN_DURATION) {
				
				// Turn completeness
				float x = (uptime)/SHIP_TURN_DURATION;
				
				// Play brake-sound
				if (!has_breaked && uptime>SHIP_TURN_DURATION-SHIP_TIME_BEFORE_STOP_TURNING_PLAY_FLAMES) {
					has_breaked = true;
					Sound local_aud_flames_close = flames_close.getTrack();
					Sound local_aud_flames_far = flames_far.getTrack();
					local_aud_flames_far.setVolume((1-depth)*100);
					local_aud_flames_close.setVolume(depth*100);
					SyncTrack.play(local_aud_flames_close);
					SyncTrack.play(local_aud_flames_far);
				}
				
				// Set color
				flame_forward.setColor(new Color(255,255,255,(int) (255*Formulas.slow_top_late_then_return(x))));
				flame_backward.setColor(new Color(255,255,255,(int) (255*Formulas.slow_top_early_then_return(x))));
				
				// Set rotation
				ship.fetchSprite().setRotation(Formulas.slow_stop(x)*rotation_angle);
				flame_forward.setRotation(ship.fetchSprite().getRotation());
				flame_backward.setRotation(ship.fetchSprite().getRotation());
			} else if (uptime<SHIP_TURN_DURATION+SHIP_WAIT_AFTER_TURN) {
				
				// Ship no longer turning
				turning_ship = false;
				
				// Play animation
				if (
						uptime >
						SHIP_TURN_DURATION +
						ship.getCurrentFrame() *
						(SHIP_WAIT_AFTER_TURN/(ship.fetchFrames().size()-3)))
					ship.next();
			} else {
				if (!pod_fired) firePod();
				updatePod(uptime-SHIP_TURN_DURATION-SHIP_WAIT_AFTER_TURN);
			}
		}
	}
	
	public float getDepth() {
		return depth;
	}
	
	public void turnShip() {
		
		// Setup flames
		Color invisible = new Color(255,255,255,0);
		flame_forward.setColor(invisible);
		flame_backward.setColor(invisible);
		flame_forward.setScale(ship.fetchSprite().getScale());
		flame_backward.setScale(ship.fetchSprite().getScale());
		flame_forward.setPosition(ship.fetchSprite().getPosition());
		flame_backward.setPosition(ship.fetchSprite().getPosition());
		
		turn_ship = true;
		turning_ship = true;
	}
	
	private void firePod() {
		
		// Play sound
		Sound local_aud_pod_fire_close = pod_fire_close.getTrack();
		Sound local_aud_pod_fire_far = pod_fire_far.getTrack();
		local_aud_pod_fire_far.setVolume((1-depth)*100);
		local_aud_pod_fire_close.setVolume(depth*100);
		SyncTrack.play(local_aud_pod_fire_close);
		SyncTrack.play(local_aud_pod_fire_far);
		
		pod_fired = true;
		pod.setPosition(ship.fetchSprite().getPosition());
		pod.setRotation(rotation_angle);
		pod.setScale(ship.fetchSprite().getScale());
		ship.next();
		pod_to = new Vector2f(0,Main.wnd.getSize().y);
		pod_relative_distance = Vector2f.mul(new Vector2f(
				Math.abs(pod_to.x - position.x),
				Math.abs(pod_to.y - position.y)),
				distance_relation);
		float total_distance = (float) (Math.sqrt(Math.pow(pod_relative_distance.x,2)+Math.pow(pod_relative_distance.y,2)));
		pod_speed = new Vector2f(
				SHIP_POD_SPEED*pod_relative_distance.x/total_distance,
				SHIP_POD_SPEED*pod_relative_distance.y/total_distance);
	}
	
	private void updatePod(float time_since_fire) {
		float x_relation = (pod_speed.x*time_since_fire)/pod_relative_distance.x;
		float y_relation = (pod_speed.y*time_since_fire)/pod_relative_distance.y;
		x_relation = x_relation>1 ? 1 : x_relation;
		y_relation = y_relation>1 ? 1 : y_relation;
		Vector2f new_pod_distance = new Vector2f(
				pod_relative_distance.x*(1-Formulas.slow_stop(x_relation)),
				pod_relative_distance.y*(1-Formulas.slow_stop(y_relation)));
		pod.setPosition(
				pod_to.x + new_pod_distance.x/distance_relation,
				pod_to.y - new_pod_distance.y/distance_relation);
		pod.setScale(new Vector2f(
				ship.fetchSprite().getScale().x*new_pod_distance.x/pod_relative_distance.x,
				ship.fetchSprite().getScale().y*new_pod_distance.y/pod_relative_distance.y));
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		ship.draw(arg0, arg1);
		if (pod_fired) pod.draw(arg0, arg1);
		if (turning_ship) {
			flame_backward.draw(arg0, arg1);
			flame_forward.draw(arg0, arg1);
		}
	}
}
