package engine;

import org.jsfml.system.Vector2f;

public class Button {
	private Vector2f position;
	private Vector2f size;
	
	public Button(Vector2f position, Vector2f size) {
		this.position = position;
		this.size = size;
	}
	
	public boolean over(Vector2f coordinate) {
		boolean within_x = position.x >= coordinate.x && coordinate.x <= position.x + size.x;
		boolean within_y = position.y >= coordinate.y && coordinate.y <= position.y + size.y;
		
		if (within_x && within_y) return true;
		else return false;
	}
}
