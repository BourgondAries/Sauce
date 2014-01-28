package engine;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;

public class StaticObject implements Drawable {
	
	// Setup data
	protected Sprite sprite;
	
	// Setup position, size and orientation
	protected XYZRAxes position;
	protected XYAxes scale;
	
	// Setup relative coordinates for corners from origin
	protected XYAxes top_left;
	protected XYAxes bottom_right;
	protected XYAxes top_right;
	protected XYAxes bottom_left;
	
	public StaticObject(String image_path, XYZRAxes position) throws IOException {
		this.sprite = PathedTextures.addImage(Paths.get(image_path));
		
		this.position = new XYZRAxes(
			position.getX(),
			position.getY(),
			position.getZ(),
			position.getRotation()
		);
		
		scale = new XYAxes(1,1);
		
		top_left = new XYAxes(0,0);
		top_right = new XYAxes(0,0);
		bottom_left = new XYAxes(0,0);
		bottom_right = new XYAxes(0,0);
		updateCornerCoordinates();
	}
	
	// Render
	public void draw(RenderTarget target, RenderStates states) {
		target.draw(sprite);
	}
	
	public void setRenderPosition(float x, float y) {
		sprite.setPosition(x, y);
	}
	
	public void setRenderScale(float x, float y) {
		sprite.setScale(x, y);
	}
	
	// Origin
	public void setOriginCenter() {
		this.sprite.setOrigin(getOrginalWidth()/2,getOrginalHeight()/2);
		updateCornerCoordinates();
	}
	
	public void setOrigin(float x, float y) {
		this.sprite.setOrigin(x,y);
		updateCornerCoordinates();
	}
	
	public float getOriginX() {
		return sprite.getOrigin().x;
	}
	
	public float getOriginY() {
		return sprite.getOrigin().y;
	}
	
	// Position
	public void setPosition(XYZRAxes position) {
		this.position.setX(position.getX());
		this.position.setY(position.getY());
		this.position.setZ(position.getZ());
		this.position.setRotation(position.getRotation());
	}
	
	public float getX() {
		return position.getX();
	}
	
	public void setX(float x) {
		position.setX(x);
	}
	
	public void addX(float x) {
		position.addX(x);
	}
	
	public float getY() {
		return position.getY();
	}
	
	public void setY(float y) {
		position.setY(y);
	}
	
	public void addY(float y) {
		position.addY(y);
	}
	
	public float getZ() {
		return position.getZ();
	}
	
	public void setZ(float z) {
		position.setZ(z);
	}
	
	public void addZ(float z) {
		position.addZ(z);
	}
	
	// Rotation
	public float getRotation() {
		return position.getRotation();
	}
	
	public void setRotation(float rotation_cw) {
		position.setRotation(rotation_cw);
		sprite.setRotation(rotation_cw);
		updateCornerCoordinates();
	}
	
	public void addRotation(float rotation_cw) {
		position.addRotation(rotation_cw);
		sprite.setRotation(sprite.getRotation() + rotation_cw);
		updateCornerCoordinates();
	}
	
	// Scale
	public float getHeight() {
		return sprite.getLocalBounds().height*scale.getY();
	}
	
	public float getWidth() {
		return sprite.getLocalBounds().width*scale.getX();
	}
	
	public void setHeight(float height) {
		scale.setY(height/sprite.getLocalBounds().height);
		updateCornerCoordinates();
	}
	
	public void setWidth(float width) {
		scale.setX(width/sprite.getLocalBounds().width);
		updateCornerCoordinates();
	}
	
	public float getOrginalHeight() {
		return sprite.getLocalBounds().height;
	}
	
	public float getOrginalWidth() {
		return sprite.getLocalBounds().width;
	}
	
	public float getScaleHeight() {
		return scale.getY();
	}
	
	public float getScaleWidth() {
		return scale.getX();
	}
	
	public void setScaleHeight(float height) {
		scale.setY(height);
		updateCornerCoordinates();
	}
	
	public void setScaleWidth(float width) {
		scale.setX(width);
		updateCornerCoordinates();
	}
	
	// Bounding-box
	private void updateCornerCoordinates() {
		if (getOriginX()==0 && getOriginY()==0) {
			top_left.setX(getOriginX());
			top_left.setY(getOriginY());
		} else {
			float top_left_x = getOriginX()*getScaleWidth();
			float top_left_y = getOriginY()*getScaleHeight();
			double top_left_angle = Math.toRadians(270-Math.toDegrees(Math.atan(top_left_x/top_left_y))+getRotation());
			double top_left_radius = Math.sqrt(Math.pow(top_left_x, 2)+Math.pow(top_left_y, 2));
			
			top_left.setX( (float) (top_left_radius*Math.cos(top_left_angle)) );
			top_left.setY( (float) (top_left_radius*Math.sin(top_left_angle)) );
		}
		
		double angle = Math.toRadians(getRotation());
		double sin_angle = Math.sin(angle);
		double cos_angle = Math.cos(angle);
		
		top_right.setX( (float) (top_left.getX()+getWidth()*cos_angle) );
		top_right.setY( (float) (top_left.getY()+getWidth()*sin_angle) );
		bottom_right.setX( (float) (top_right.getX()-getHeight()*sin_angle) );
		bottom_right.setY( (float) (top_right.getY()+getHeight()*cos_angle) );
		bottom_left.setX( (float) (bottom_right.getX()-getWidth()*cos_angle) );
		bottom_left.setY( (float) (bottom_right.getY()-getWidth()*sin_angle) );
	}
	
	public float getBoundTop() {
		if (getRotation()<=90) {
			return top_left.getY();
		} else if (getRotation()<=180) {
			return bottom_left.getY();
		} else if (getRotation()<=270) {
			return bottom_right.getY();
		} else {
			return top_right.getY();
		}
	}
	
	public float getBoundBottom() {
		if (getRotation()<=90) {
			return bottom_right.getY();
		} else if (getRotation()<=180) {
			return top_right.getY();
		} else if (getRotation()<=270) {
			return top_left.getY();
		} else {
			return bottom_left.getY();
		}
	}
	
	public float getBoundLeft() {
		if (getRotation()<=90) {
			return bottom_left.getX();
		} else if (getRotation()<=180) {
			return bottom_right.getX();
		} else if (getRotation()<=270) {
			return top_right.getX();
		} else {
			return top_left.getX();
		}
	}
	
	public float getBoundRight() {
		if (getRotation()<=90) {
			return top_right.getX();
		} else if (getRotation()<=180) {
			return top_left.getX();
		} else if (getRotation()<=270) {
			return bottom_left.getX();
		} else {
			return bottom_right.getX();
		}
	}
}
