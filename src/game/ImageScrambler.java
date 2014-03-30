package game;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;

public class ImageScrambler implements Drawable {
	private ArrayList<Sprite> rows;
	private Vector2f position;
	private Sprite original_sprite;
	private boolean render_simple = false;
	
	private float max_alpha = 1;
	private float min_alpha = 1;
	private float visibility = 1;
	private float max_distort = 0;
	
	private float max_r = 1;
	private float max_g = 1;
	private float max_b = 1;
	
	private float min_r = 1;
	private float min_g = 1;
	private float min_b = 1;
	
	/**
	 * This class takes in a sprite and departs it into several 1 pixel high rows sprites.
	 * This makes it possible to make the image have a TV-like distortion effect per frame.
	 * You can also use the getScrambledImage to only scramble the texture per pixel and
	 * return it to the original sprite, but this cannot be done every frame due to
	 * massive resource hog.
	 */
	public ImageScrambler(Sprite old_sprite) throws TextureCreationException {
		
		// Backup original texture
		original_sprite = old_sprite;
		
		// Setup position
		position = old_sprite.getPosition();
		
		// Setup arrayList
		rows = new ArrayList<>();
		
		// Make texture editable
		Image old_image = old_sprite.getTexture().copyToImage();
		
		// Make a new sprite for each row and put it into the arrayList
		for (int i = 0; i < old_sprite.getLocalBounds().height; i++) {
			
			// Setup new image
			Image new_image = new Image();
			new_image.create((int) (old_sprite.getLocalBounds().width), 1);
			
			// Enable support for tiled textures
			int reset = 0;
			
			// Load data into image
			for (int j = 0; j < old_sprite.getLocalBounds().width; j++) {
				int old_read_x = j-old_image.getSize().x*reset;
				if (old_read_x>=old_image.getSize().x-1) reset++;
				Color old_color = old_image.getPixel(old_read_x, i);
				new_image.setPixel(j, 0, old_color);
			}
			
			// Make texture of image
			Texture new_texture = new Texture();
			new_texture.loadFromImage(new_image);
			
			// Put new sprite in arrayList
			Sprite new_sprite = new Sprite(new_texture);
			new_sprite.setPosition(new Vector2f(position.x,position.y + i));
			rows.add(new_sprite);
		}
	}
	
	public void scramble() {
		for (Sprite row : rows) {
			
			// Set position
			row.setPosition( (float) (position.x + Math.random()*max_distort*2 - max_distort), row.getPosition().y);
			
			// Choose if the row should be visible
			int row_visible = Math.random()<visibility ? 1 : 0;
			
			// Set color and alpha
			row.setColor(new Color(
					(int) (255*(Math.random()*(max_r-min_r)+min_r)),
					(int) (255*(Math.random()*(max_g-min_g)+min_g)),
					(int) (255*(Math.random()*(max_b-min_b)+min_b)),
					(int) (255*row_visible*(Math.random()*(max_alpha-min_alpha)+min_alpha))));
		}
	}
	
	public void simpleRender(boolean render_simple) {
		this.render_simple = render_simple;
	}
	
	public void setColorR(float max_r, float min_r) {
		max_r = limitValue(max_r);
		min_r = limitValue(min_r);
		
		this.max_r = max_r;
		this.min_r = min_r;
	}
	
	public void setColorG(float max_g, float min_g) {
		max_g = limitValue(max_g);
		min_g = limitValue(min_g);
		
		this.max_g = max_g;
		this.min_g = min_g;
	}
	
	public void setColorB(float max_b, float min_b) {
		max_b = limitValue(max_b);
		min_b = limitValue(min_b);
		
		this.max_b = max_b;
		this.min_b = min_b;
	}
	
	public void setAlpha(float max_alpha, float min_alpha) {
		max_alpha = limitValue(max_alpha);
		min_alpha = limitValue(min_alpha);
		
		this.max_alpha = max_alpha;
		this.min_alpha = min_alpha;
	}
	
	public void setVisibility(float visibility) {
		visibility = limitValue(visibility);
		
		this.visibility = visibility;
	}
	
	public void setMaxDistort(float max_distort) {
		max_distort = max_distort<0 ? 0 : max_distort;
		
		this.max_distort = max_distort;
	}
	
	private float limitValue(float value) {
		return value<0 ? 0 : value>1 ? 1 : value;
	}
	
	public void updatePosition(Vector2f position) {
		for (Sprite row : rows) {
			row.setPosition(
					row.getPosition().x - this.position.x + position.x,
					row.getPosition().y - this.position.y + position.y);
		}
		original_sprite.setPosition(position);
		this.position = position;
	}
	
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		if (render_simple){
			original_sprite.draw(arg0, arg1);
			return;
		}
		
		for (Sprite row : rows) {
			row.draw(arg0, arg1);
		}
	}
	
	public Sprite fetchSprite() {
		return original_sprite;
	}
	
	public static ConstTexture getScrambledTexture(ConstTexture texture, int max_distort, float visibility) {
		
		// Remap visibility
		visibility = visibility<0 ? 0 : visibility>1 ? 1 : visibility;
		
		// Make texture editable
		Image old_image = texture.copyToImage();
		
		// Setup the new image
		int new_x_size = old_image.getSize().x + 2*max_distort;
		Image new_image = new Image();
		new_image.create(new_x_size, old_image.getSize().y);
		
		// Loop through every row
		for (int i = 0; i < old_image.getSize().y; i++) {
			
			// How visible the row should be
			Color invisible_color = new Color(255,255,255,0);
			int opacity = Math.random()<visibility ? 1 : 0;
			
			// How much to distort the row
			int distortion = (int) (Math.random()*max_distort*2);
			
			// Fill in new array
			for (int j = 0; j < new_x_size; j++) {
				
				if (j<distortion)
					
					// Fill leading alpha
					new_image.setPixel(j, i, invisible_color);
				
				else if (j<distortion + old_image.getSize().x) {
					
					// Color to fill image with
					Color opc = old_image.getPixel(j-distortion, i);
					Color pixel_color = new Color(opc.r,opc.g,opc.b,opacity*opc.a);
					
					// Fill image
					new_image.setPixel(j, i, pixel_color);
				
				} else
					
					// Fill trailing alpha
					new_image.setPixel(j, i, invisible_color);
			}
		}
		
		// Return new texture (or the old if it fails)
		try {
			Texture new_texture = new Texture();
			new_texture.loadFromImage(new_image);
			return new_texture;
		} catch (TextureCreationException e) {
			return texture;
		}
	}
}
