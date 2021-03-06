package engine;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundSource;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

public class Button implements Drawable {
	private Vector2f position;
	private Vector2f size;
	private boolean mouse_was_down_over = false;
	private boolean loaded = true;
	private boolean first_time = true;
	private static boolean exit;
	private static float distance_remaining;
	private boolean activated = false;
	
	// Button
	private Sprite button_left;
	private Sprite button_middle;
	private Sprite button_right;
	
	// Borders
	private Sprite button_border_left;
	private Sprite button_border_right;
	
	// Text
	private Text text;
	
	// Sounds
	private Sound button_press;
	private Sound button_hover;
	
	// Textures (must be initialized from another class)
	public static Sprite img_button_left;
	public static Sprite img_button_middle;
	public static Sprite img_button_right;
	public static Sprite img_button_left_white;
	public static Sprite img_button_middle_white;
	public static Sprite img_button_right_white;
	public static Sprite img_button_border_left;
	public static Sprite img_button_border_right;
	public static SyncTrack aud_button_press;
	public static SyncTrack aud_button_hover;
	public static Font fon_button;
	
	private static final int TEXT_SIZE = 30; // 40
	private static final float BORDER_START_DISTANCE = 20; // 40
	private static final float BORDER_END_DISTANCE = 30; // 50
	private static final float START_OPACITY = 0.7f;
	private static final float BORDER_SPEED = 0.1f;
	private static final float TEXT_Y_COMPENSATION = 0.1f;
	private static final float START_OFFSET_MULTIPLIER = -0.1f;
	
	public Button(Vector2f position, float width, String text) {
		
		float length_of_smallest_button =
				img_button_left.getGlobalBounds().width + 
				img_button_right.getGlobalBounds().width + 
				img_button_middle.getGlobalBounds().width;
		
		width = width<length_of_smallest_button ? length_of_smallest_button : width;
		
		// Initialize textures
		button_left = new Sprite(img_button_left.getTexture());
		button_middle = new Sprite(img_button_middle.getTexture());
		button_right = new Sprite(img_button_right.getTexture());
		button_border_left = new Sprite(img_button_border_left.getTexture());
		button_border_right = new Sprite(img_button_border_right.getTexture());
		
		// Setup sounds
		button_press = aud_button_press.getTrack();
		button_hover = aud_button_hover.getTrack();
		button_hover.setLoop(true);
		
		// Setup text
		this.text = new Text(text,fon_button,TEXT_SIZE);
		
		// Setup start alpha
		Color standar_color = new Color(255,255,255,(int) (START_OPACITY*255));
		button_left.setColor(standar_color);
		button_middle.setColor(standar_color);
		button_right.setColor(standar_color);
		
		// Setup positions
		this.position = position;
		this.size = new Vector2f(width,img_button_left.getGlobalBounds().height);
		
		this.text.setPosition(
				position.x + size.x/2 - this.text.getGlobalBounds().width/2,
				position.y + size.y/2 - this.text.getGlobalBounds().height/2 - TEXT_SIZE*TEXT_Y_COMPENSATION);
		
		button_left.setPosition(
				position.x,
				position.y);
		
		button_right.setPosition(
				position.x + size.x - button_right.getGlobalBounds().width,
				position.y);
		
		button_middle.setPosition(
				position.x + button_left.getGlobalBounds().width,
				position.y);
		
		button_middle.setTextureRect(new IntRect(
				0,
				0,
				(int) (size.x - button_right.getGlobalBounds().width - button_left.getGlobalBounds().width),
				(int) (button_middle.getGlobalBounds().height)));
		
		button_border_left.setPosition(
				position.x - BORDER_START_DISTANCE,
				position.y - button_border_left.getGlobalBounds().height/2 + size.y/2);
		
		button_border_right.setPosition(
				position.x + size.x + BORDER_START_DISTANCE - button_border_right.getGlobalBounds().width,
				position.y - button_border_right.getGlobalBounds().height/2 + size.y/2);
	}
	
	public void doPlayStart() {
		loaded = false;
		
		button_border_left.setPosition(
				position.x + size.x*START_OFFSET_MULTIPLIER/2 - button_border_left.getGlobalBounds().width/2,
				position.y - button_border_left.getGlobalBounds().height/2 + size.y/2);
		
		button_border_right.setPosition(
				position.x + size.x - size.x*START_OFFSET_MULTIPLIER/2 - button_border_right.getGlobalBounds().width/2,
				position.y - button_border_right.getGlobalBounds().height/2 + size.y/2);
		
		Color standar_color = new Color(255,255,255,0);
		button_left.setColor(standar_color);
		button_middle.setColor(standar_color);
		button_right.setColor(standar_color);
		button_border_left.setColor(standar_color);
		button_border_right.setColor(standar_color);
		text.setColor(standar_color);
	}
	
	private void exitAnimation() {
		Color standar_color = new Color(255,255,255,255);
		button_left.setColor(standar_color);
		button_middle.setColor(standar_color);
		button_right.setColor(standar_color);
		button_left.setColor(standar_color);
		button_middle.setColor(standar_color);
		button_right.setTexture(img_button_right_white.getTexture());
		button_middle.setTexture(img_button_middle_white.getTexture());
		button_left.setTexture(img_button_left_white.getTexture());
		text.setColor(standar_color);
		exit = true;
	}
	
	public void resetButton() {
		first_time = true;
		activated = false;
		button_right.setTexture(img_button_right.getTexture());
		button_middle.setTexture(img_button_middle.getTexture());
		button_left.setTexture(img_button_left.getTexture());
	}
	
	public boolean update(Vector2f mouse_position, boolean mouse_down) {
		if (first_time) { exit = false; first_time = false; }
		
		if (!loaded) {
			
			// Make button visible
			float alpha_remaining = 255*START_OPACITY - button_left.getColor().a;
			Color new_color = new Color(255,255,255,(int) (button_left.getColor().a + alpha_remaining*BORDER_SPEED));
			button_left.setColor(new_color);
			button_middle.setColor(new_color);
			button_right.setColor(new_color);
			button_border_left.setColor(new_color);
			button_border_right.setColor(new_color);
			text.setColor(new_color);
			
			// Move borders towards start-position
			float distance_remaining = (position.x - BORDER_START_DISTANCE) - button_border_left.getPosition().x;
			button_border_left.move(distance_remaining*BORDER_SPEED,0);
			button_border_right.move(-distance_remaining*BORDER_SPEED,0);
			
			if (distance_remaining<1) {
				button_border_left.setColor(new Color(255,255,255,255));
				button_border_right.setColor(new Color(255,255,255,255));
				loaded = true;
			}
			
			return false;
		}
		
		if (exit) {
			
			// If hoversound is playing, stop it
			if (button_hover.getStatus()==SoundSource.Status.PLAYING) button_hover.stop();
			
			// Make button visible
			float alpha_remaining = button_left.getColor().a;
			Color new_color = new Color(255,255,255,(int) (button_left.getColor().a - alpha_remaining*BORDER_SPEED));
			button_left.setColor(new_color);
			button_middle.setColor(new_color);
			button_right.setColor(new_color);
			button_border_left.setColor(new_color);
			button_border_right.setColor(new_color);
			text.setColor(new_color);
			
			// Move borders towards start-position
			if (activated) {
				distance_remaining = (position.x + size.x*START_OFFSET_MULTIPLIER/2 - button_border_right.getGlobalBounds().width/2) - button_border_left.getPosition().x;
				button_border_left.move(distance_remaining*BORDER_SPEED,0);
				button_border_right.move(-distance_remaining*BORDER_SPEED,0);
			}
			
			if (Math.abs(distance_remaining)<1) {
				button_border_left.setColor(new Color(255,255,255,0));
				button_border_right.setColor(new Color(255,255,255,0));
				if (activated) return true;
				return false;
			}
			
			return false;
		}
		
		if (over(mouse_position)) {
			
			// Move borders away
			float distance_remaining = button_border_left.getPosition().x - (position.x - BORDER_END_DISTANCE);
			button_border_left.move(-distance_remaining*BORDER_SPEED,0);
			button_border_right.move(distance_remaining*BORDER_SPEED,0);
			
			// Play hoversound
			if (button_hover.getStatus()!=SoundSource.Status.PLAYING) button_hover.play();
			
			// Make button flicker
			Color flicker_color = new Color(255,255,255,(int) (255*(Math.random()*(1-START_OPACITY)+START_OPACITY)));
			button_left.setColor(flicker_color);
			button_middle.setColor(flicker_color);
			button_right.setColor(flicker_color);
			text.setColor(flicker_color);
			
			// Return true if the mouse just clicked
			if (!mouse_down && mouse_was_down_over) {
				mouse_was_down_over = false;
				activated = true;
				button_press.play();
				exitAnimation();
			} else if (mouse_down) {
				mouse_was_down_over = true;
			}
		} else {
			
			// Reset color
			Color standar_color = new Color(255,255,255,(int) (255*START_OPACITY));
			button_left.setColor(standar_color);
			button_middle.setColor(standar_color);
			button_right.setColor(standar_color);
			text.setColor(standar_color);
			
			// If hoversound is playing, stop it
			if (button_hover.getStatus()==SoundSource.Status.PLAYING) button_hover.stop();
			
			// Move borders back into place
			float distance_remaining = (position.x - BORDER_START_DISTANCE) - button_border_left.getPosition().x;
			button_border_left.move(distance_remaining*BORDER_SPEED,0);
			button_border_right.move(-distance_remaining*BORDER_SPEED,0);
			
			// If the mouse is released outside, it wasn't down over button last
			if (!mouse_down) {
				mouse_was_down_over = false;
			}
		}
		return false;
	}
	
	private boolean over(Vector2f coordinate) {
		boolean within_x = coordinate.x >= position.x && coordinate.x <= position.x + size.x;
		boolean within_y = coordinate.y >= position.y && coordinate.y <= position.y + size.y;
		
		if (within_x && within_y) return true;
		else return false;
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		button_left.draw(arg0, arg1);
		button_middle.draw(arg0, arg1);
		button_right.draw(arg0, arg1);
		text.draw(arg0, arg1);
		button_border_left.draw(arg0, arg1);
		button_border_right.draw(arg0, arg1);
	}
}
