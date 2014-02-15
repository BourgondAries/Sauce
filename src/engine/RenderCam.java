package engine;

import game.Main;
import java.util.ArrayList;

public class RenderCam {
	
	// Setup the player's camera
	private XYZRAxes cam_position;
	private float cam_fov;
	private float cam_clip;
	
	// Setup variables connected to following a target
	private float cam_transitionspeed;
	private float cam_dist_from_trg;
	private boolean cam_follow_trg_rotation;
	private StaticObject cam_target;
	
	// Setup list of drawables
	private ArrayList<StaticObject> drawables = new ArrayList<StaticObject>();
	
	// Constructors
	public RenderCam(StaticObject target, float distance_from_target, float transitionspeed) {
		cam_position = new XYZRAxes(
				target.getX(),
				target.getY(),
				target.getZ() + distance_from_target,
				target.getRotation()
		);
		
		cam_fov = 90;
		cam_clip = 1;
		cam_dist_from_trg = distance_from_target;
		cam_transitionspeed = transitionspeed;
		cam_follow_trg_rotation = false;
		cam_target = target;
	}
	
	public RenderCam(XYZRAxes position) {
		cam_position = new XYZRAxes(
				position.getX(),
				position.getY(),
				position.getZ(),
				position.getRotation()
		);
		
		cam_fov = 90;
		cam_clip = 1;
		cam_follow_trg_rotation = false;
	}
	
	// Change targets
	public void setTarget(XYZRAxes position) {
		cam_target = null;
		cam_position.setX(position.getX());
		cam_position.setY(position.getY());
		cam_position.setZ(position.getZ());
		cam_position.setRotation(position.getRotation());
	}

	public void setTarget(StaticObject target, float distance_from_target, float transitionspeed) {
		cam_target = target;
		cam_dist_from_trg = distance_from_target;
		cam_transitionspeed = transitionspeed;
	}
	
	// Set camera settings
	public float getFov() {
		return cam_fov;
	}
	
	public void setFov(float fov) {
		cam_fov = fov;
	}
	
	public float getDistance() {
		return cam_dist_from_trg;
	}
	
	public void setDistance(float distance_from_target) {
		cam_dist_from_trg = distance_from_target;
	}
	
	public void addDistance(float distance_from_target) {
		cam_dist_from_trg += distance_from_target;
	}
	
	public float getTransitionspeed() {
		return cam_transitionspeed;
	}
	
	public void setTransitionspeed(float cam_transitionspeed) {
		this.cam_transitionspeed = cam_transitionspeed;
	}
	
	public void enableFollowTargetRotation() {
		cam_follow_trg_rotation = true;
	}
	
	public void disableFollowTargetRotation(float degree) {
		cam_follow_trg_rotation = false;
		cam_position.setRotation(degree);
	}
	
	public void disableFollowTargetRotation() {
		cam_follow_trg_rotation = false;
	}
	
	// Position
	public float getCamX() {
		return cam_position.getX();
	}
	
	public void setCamX(float x) {
		if (cam_target == null) {
			cam_position.setX(x);
		}
	}
	
	public void addCamX(float x) {
		if (cam_target == null) {
			cam_position.addX(x);
		}
	}
	
	public float getCamY() {
		return cam_position.getY();
	}
	
	public void setCamY(float y) {
		if (cam_target == null) {
			cam_position.setY(y);
		}
	}
	
	public void addCamY(float y) {
		if (cam_target == null) {
			cam_position.addY(y);
		}
	}
	
	public float getCamZ() {
		return cam_position.getZ();
	}
	
	public void setCamZ(float z) {
		if (cam_target == null) {
			cam_position.setZ(z);
		}
	}
	
	public void addCamZ(float z) {
		if (cam_target == null) {
			cam_position.addZ(z);
		}
	}
	
	public float getCamRotation() {
		return cam_position.getRotation();
	}
	
	public void setCamRotation(float rotation_cw) {
		if (!cam_follow_trg_rotation || cam_target == null) {
			cam_position.setRotation(rotation_cw);
		}
	}
	
	public void addCamRotation(float rotation_cw) {
		if (!cam_follow_trg_rotation || cam_target == null) {
			cam_position.addRotation(rotation_cw);
		}
	}
	
	public float[] getRenderView(float z) {
		float relation = calculateRelation(z);
		return new float[]{cam_position.getX()-Main.wnd.getSize().x/(2*relation), cam_position.getY()-Main.wnd.getSize().y/(2*relation), Main.wnd.getSize().x/relation, Main.wnd.getSize().y/relation};
	}
	
	private float calculateRelation(float z) {
		return (float) (Main.wnd.getSize().x / (2 * (cam_position.getZ() - z) * Math.tan(Math.toRadians(cam_fov) / 2)));

	}
	
	// Update camera position relative to target's position.
	private void updateCam() {
		if (cam_target != null) {
			
			cam_position.setX(getCamX() - (getCamX() - (cam_target.getX())) * cam_transitionspeed);
			cam_position.setY(getCamY() - (getCamY() - (cam_target.getY())) * cam_transitionspeed);
			cam_position.setZ(getCamZ() - (getCamZ() - (cam_target.getZ() + cam_dist_from_trg)) * cam_transitionspeed);
			
			if (!cam_follow_trg_rotation) return;
			
			if ((getCamRotation() - cam_target.getRotation() + 360) % 360 > 180) {
				cam_position.setRotation(getCamRotation() + (cam_target.getRotation() - getCamRotation()) * cam_transitionspeed);
			} else {
				cam_position.setRotation(getCamRotation() - (getCamRotation() - cam_target.getRotation()) * cam_transitionspeed);	
			}
		}
	}
	
	// Make a object visible to the camera, ordered by z-coordinate
	public void makeDrawable(StaticObject object) {
		// wtf, this will only permit 1 object to exist in drawables...
		if (drawables.size()==0) {
			drawables.add(object);
			return;
		}
		
		for (int i = 0; i < drawables.size(); i++) {
			if (object.getZ() <= drawables.get(i).getZ()) {
				drawables.add(i, object);
				return;
			}
		}
		
		drawables.add(object);
	}
	
	// Make a object invisible to the camera
	public void removeDrawable(StaticObject object) {
		drawables.remove(object);
	}
	
	// Render next frame
	public void renderFrame() {
		
		// Clear window
		Main.wnd.clear();
		
		// Update camera
		updateCam();
		
		// Draw all visible objects
		for (StaticObject object : drawables) {
			
			// Skip object if it's out of bounds
			if (object.getZ() >= cam_position.getZ()-cam_clip) continue;
			
			// Calculate the relation between the object's layer and the screen
			float relation = calculateRelation(object.getZ());
			
			// Update the position of the sprite relative to camera
			object.setRenderPosition(
				(object.getX()-cam_position.getX())*relation+Main.wnd.getSize().x/2,
				(object.getY()-cam_position.getY())*relation+Main.wnd.getSize().y/2
			);
			
			// Update the scale of the sprite relative to camera
			object.setRenderScale(
				object.getScaleWidth()*relation,
				object.getScaleHeight()*relation
			);
			
			// Update camera rotation
			Main.view.setRotation(cam_position.getRotation());
			
			// Set view
			Main.wnd.setView(Main.view);
			
			// Draw object to buffer
			Main.wnd.draw(object);
		}
		
		// Flip front/back buffer
		Main.wnd.display();
	}
}
