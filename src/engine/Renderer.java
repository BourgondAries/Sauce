package engine;

import game.Main;

import java.util.ArrayList;

import org.jsfml.graphics.*;

public class Renderer {
	// Setup the player's camera
	private float cam_x;
	private float cam_y;
	private float cam_z;
	private float cam_fov;
	
	// Setup variables connected to following a target
	private float cam_transitionspeed;
	private float distance_from_target;
	private Player target_player;
	private Object target_object;
	
	public Renderer(Player target, float fov, float distance_from_target, float transitionspeed) {
		cam_x = target.getPosition().x;
		cam_y = target.getPosition().y;
		cam_z = distance_from_target;
		cam_fov = fov;
		cam_transitionspeed = transitionspeed;
		target_player = target;
	}
	
	public Renderer(Object target, float fov, float distance_from_target, float transitionspeed) {
		cam_x = target.getX();
		cam_y = target.getY();
		cam_z = target.getZ() + distance_from_target;
		cam_fov = fov;
		cam_transitionspeed = transitionspeed;
		target_object = target;
	}
	
	public Renderer(float x, float y, float z, float fov) {
		cam_x = x;
		cam_y = y;
		cam_z = z;
		cam_fov = fov;
	}
	
	public void setTarget(float x, float y, float z) {
		target_player = null;
		target_object = null;
		cam_x = x;
		cam_y = y;
		cam_z = z;
	}
	
	public void setTarget(Player target) {
		target_player = target;
		target_object = null;
	}

	public void setTarget(Object target) {
		target_player = null;
		target_object = target;
	}
	
	public void setFov(float fov) {
		cam_fov = fov;
	}
	
	public void setDistance(float distance_from_target) {
		this.distance_from_target = distance_from_target;
	}
	
	public void setTransitionspeed(float cam_transitionspeed) {
		this.cam_transitionspeed = cam_transitionspeed;
	}
	
	private void updateCam() {
		if (target_player != null) {
			cam_x -= (cam_x - (target_player.getPosition().x + target_player.getScale().x / 2)) * cam_transitionspeed;
			cam_y -= (cam_y - (target_player.getPosition().y + target_player.getScale().y / 2)) * cam_transitionspeed;
			cam_z -= (cam_z - distance_from_target) * cam_transitionspeed;
		} else if (target_object != null) {
			cam_x -= (cam_x - (target_object.getX() + target_object.getWidth() / 2)) * cam_transitionspeed;
			cam_y -= (cam_y - (target_object.getY() + target_object.getHeight() / 2)) * cam_transitionspeed;
			cam_z -= (cam_z - (target_object.getZ() + distance_from_target)) * cam_transitionspeed;
		}
	}
	
	//ArrayList<Drawable> sorted_scene, Player player, RenderWindow window som argumenter? eller ingen argumenter?
	public void renderFrame(ArrayList<Drawable> sorted_scene, Player player) {
		// Clear window
		Main.wnd.clear();
		
		// Update camera
		updateCam();
		View view = new View(Main.wnd.getDefaultView().getCenter(), Main.wnd.getDefaultView().getSize());
		view.move(m_rng.nextInt() % 30 - 15, m_rng.nextInt() % 30 - 15);
		Main.wnd.setView(view);
		
		// Draw
		for (Drawable item : sorted_scene) {
			Main.wnd.draw(item);
		}
		
		// Refresh
		Main.wnd.display();
	}
}
