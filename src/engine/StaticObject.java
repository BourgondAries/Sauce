package engine;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

public class StaticObject implements Drawable
{
	
	// Setup data
	protected Sprite sprite;
	
	// Setup position, size and orientation
	protected Vector3f position; //Har laget klasser for å erstatte vektorer om det blir problemer med dem, hvis det funker med vektorer, sletter vi klassene
	protected Vector2f size;
	protected float rotation_cv;
	
	//Information for the draw method
	protected Transform transformation;
	
	public StaticObject(String image_path, Vector3f position, Vector2f size, float rotation_cv) throws IOException {
		this.sprite = PathedTextures.addImage(Paths.get(image_path));
		this.position = position;
		this.size = size;
		this.rotation_cv = rotation_cv;
	}
	
	public void draw(RenderTarget target, RenderStates states) {
		//Kan du hjelpe meg med denne? finner ingen dokumentasjon om den...
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector2f getSize() {
		return size;
	}
	
	public void setSize(Vector2f size) {
		this.size = size;
	}

	public float getRotation() {
		return rotation_cv;
	}
}
