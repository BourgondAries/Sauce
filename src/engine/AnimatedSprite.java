package engine;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2i;

public class AnimatedSprite implements Drawable {
	private Sprite sprite;
	private List<IntRect> frames = new ArrayList<>();
	private int current_frame;
	
	public AnimatedSprite(Sprite sprite, int width, int height) {
		Vector2i tex_size = sprite.getTexture().getSize();
		
		for (int y = 0; y*height < tex_size.y; y++) {
			for (int x = 0; x*width < tex_size.x; x++) {
				frames.add(new IntRect(x*width,y*height,width,height));
			}
		}
		
		this.sprite = new Sprite(sprite.getTexture());
		frame(0);
	}
	
	public AnimatedSprite(Sprite sprite, int number_of_frames) {
		Vector2i tex_size = sprite.getTexture().getSize();
		int frame_width = tex_size.x/number_of_frames;
		
		for (int i = 0; i < number_of_frames; i++) {
			frames.add(new IntRect(i*frame_width,0,frame_width,tex_size.y));
		}
		
		this.sprite = new Sprite(sprite.getTexture());
		frame(0);
	}
	
	public AnimatedSprite(Sprite sprite, List<IntRect> frames) {
		this.frames.addAll(frames);
		this.sprite = new Sprite(sprite.getTexture());
		frame(0);
	}
	
	public AnimatedSprite(AnimatedSprite animated_sprite) {
		frames.addAll(animated_sprite.fetchFrames());
		sprite = new Sprite(animated_sprite.fetchSprite().getTexture());
		sprite.setPosition(animated_sprite.fetchSprite().getPosition());
		sprite.setScale(animated_sprite.fetchSprite().getScale());
		sprite.setRotation(animated_sprite.fetchSprite().getRotation());
		sprite.setOrigin(animated_sprite.fetchSprite().getOrigin());
		frame(animated_sprite.getCurrentFrame());
	}
	
	public void next() {
		if (current_frame + 1 < frames.size()) {
			current_frame++;
			sprite.setTextureRect(frames.get(current_frame));
		}
	}
	
	public void previous() {
		if (current_frame > 0) {
			current_frame--;
			sprite.setTextureRect(frames.get(current_frame));
		}
	}
	
	public void frame(int index) {
		if (index > -1 && index < frames.size()) {
			current_frame = index;
			sprite.setTextureRect(frames.get(current_frame));
		}
	}
	
	public int getCurrentFrame() {
		return current_frame;
	}
	
	public Sprite fetchSprite() {
		return sprite;
	}

	public List<IntRect> fetchFrames() {
		return frames;
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		sprite.draw(arg0, arg1);
	}
}
