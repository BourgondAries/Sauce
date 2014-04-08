package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsfml.audio.Sound;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import engine.PathedFonts;
import engine.PathedSounds;
import engine.PathedTextures;

public class SpeechBox implements Drawable {
	private String current_text;
	private int current_text_index;
	private List<String> total_text;
	
	private float percent_open;
	
	private Vector2f size = new Vector2f(600,200);
	private Vector2f position = new Vector2f(0,0);
	private Vector2f content_text_position = new Vector2f(0,0);
	
	private boolean locked = false;
	
	private int tick_counter = 0;
	
	// Textbox
	private Sprite corner_top_left;
	private Sprite corner_top_right;
	private Sprite corner_bottom_left;
	private Sprite corner_bottom_right;
	private Sprite corner_left;
	private Sprite corner_right;
	private Sprite corner_top;
	private Sprite corner_bottom;
	private Sprite corner_middle;
	
	// Divider
	private Sprite divider_bottom_left;
	private Sprite divider_bottom_middle;
	private Sprite divider_bottom_right;
	private Sprite divider_top_left;
	private Sprite divider_top_middle;
	private Sprite divider_top_right;
	
	// Header
	private Sprite header_left;
	private Sprite header_right;
	private Sprite header_middle;
	
	// Footer
	private Sprite footer_left;
	private Sprite footer_right;
	private Sprite footer_middle;
	
	// Font
	private Font font;
	private Text content_text;
	private Text footer_text;
	private Text header_text;
	
	// Music
	private Sound keypress;
	
	private StatusBox box_state;
	private StatusWriting writing_state;
	
	private List<Sprite> elements = new ArrayList<>();
	
	private static final float OPEN_CLOSE_SPEED = 0.05f;
	private static final float TICKS_BETWEEN_NEW_LETTER = 1;
	private static final int CONTENT_TEXT_SIZE = 10;
	private static final int HEADER_TEXT_SIZE = 15;
	private static final int FOOTER_TEXT_SIZE = 10;
	private static final float DIVIDER_SPACING = -2;
	private static final float CONTENT_PADDING = 10;
	private static final float HEADER_SPACING_TOP = 7;
	private static final float FOOTER_SPACING_TOP = 10;
	private static final float KEYPRESS_VOLUME = 40;
	
	public static enum StatusBox {
		open,
		is_closing,
		is_opening,
		closed
	}
	
	public static enum StatusWriting {
		is_writing,
		ready,
		interrupted
	}
	
	public SpeechBox() throws IOException {
		// Setup variables
		current_text = "";
		
		current_text_index = 0;
		total_text = new ArrayList<>();
		
		percent_open = 0;
		
		// Setup statuses
		box_state = StatusBox.closed;
		writing_state = StatusWriting.interrupted;
		
		// Load textures
		loadTextures();
		
		// Setup text
		updateContentText();
		header_text = new Text("",font,HEADER_TEXT_SIZE);
		footer_text = new Text("",font,HEADER_TEXT_SIZE);
		
		// Update position
		updatePosition();
		
		// Update elements
		elements.addAll(Arrays.asList(
				divider_top_left,
				divider_top_middle,
				divider_top_right,
				divider_bottom_left,
				divider_bottom_middle,
				divider_bottom_right,
				
				corner_bottom,
				corner_top,
				corner_left,
				corner_right,
				corner_middle,
				corner_bottom_right,
				corner_bottom_left,
				corner_top_right,
				corner_top_left,
				
				header_left,
				header_middle,
				header_right,
				
				footer_left,
				footer_middle,
				footer_right
		));
	}
	
	private void loadTextures() throws IOException {
		
		// Music
		keypress = new Sound(PathedSounds.getSound(Paths.get("sfx/keypress.ogg")));
		keypress.setVolume(KEYPRESS_VOLUME);
		
		// Textbox
		corner_bottom_left = new Sprite(PathedTextures.getTexture(Paths.get(
						"res/textbox/textbox_content_bottom_left.tga")));
		corner_bottom_right = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_content_bottom_right.tga")));
		corner_top_left = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_content_top_left.tga")));
		corner_top_right = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_content_top_right.tga")));
		
		corner_top = new Sprite(loadTileable("res/textbox/textbox_content_middle_top.tga"));
		corner_bottom = new Sprite(loadTileable("res/textbox/textbox_content_middle_bottom.tga"));
		corner_left = new Sprite(loadTileable("res/textbox/textbox_content_middle_left.tga"));
		corner_right = new Sprite(loadTileable("res/textbox/textbox_content_middle_right.tga"));
		corner_middle = new Sprite(loadTileable("res/textbox/textbox_content_middle.tga"));
		
		// Header
		header_left = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_header_left.tga")));
		header_middle = new Sprite(loadTileable("res/textbox/textbox_header_middle.tga"));
		header_right = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_header_right.tga")));
		
		// Footer
		footer_left = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_footer_left.tga")));
		footer_middle = new Sprite(loadTileable("res/textbox/textbox_footer_middle.tga"));
		footer_right = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_footer_right.tga")));
		
		// Divider
		divider_top_left = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_divider_left.tga")));
		divider_bottom_left = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_divider_left.tga")));
		divider_top_middle = new Sprite(loadTileable("res/textbox/textbox_divider_middle.tga"));
		divider_bottom_middle = new Sprite(loadTileable("res/textbox/textbox_divider_middle.tga"));
		divider_top_right = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_divider_right.tga")));
		divider_bottom_right = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_divider_right.tga")));
		
		// Font
		font = PathedFonts.getFont(Paths.get("res/pixelmix.ttf"));
	}
	
	private Texture loadTileable(String path) throws IOException {
		Texture new_tex = PathedTextures.getTexture(Paths.get(path));
		new_tex.setRepeated(true);
		return new_tex;
	}
	
	private void updatePosition() {
		// Set positions
		float footer_y = position.y + size.y - footer_left.getGlobalBounds().height;
		float content_y = position.y + header_left.getGlobalBounds().height +
				2*DIVIDER_SPACING + divider_top_left.getGlobalBounds().height;
		float divider_top_y = position.y + header_left.getGlobalBounds().height + DIVIDER_SPACING;
		float divider_bottom_y = position.y + size.y - footer_left.getGlobalBounds().height -
				DIVIDER_SPACING - divider_bottom_left.getGlobalBounds().height;
		
		// Set sizes
		float size_content_y = size.y - 4*DIVIDER_SPACING - header_left.getGlobalBounds().height -
				footer_left.getGlobalBounds().height - 2*divider_top_left.getGlobalBounds().height;
		
		// Text
		content_text.setPosition(position.x, content_y);
		content_text_position = new Vector2f(position.x+CONTENT_PADDING,content_y+CONTENT_PADDING);
		header_text.setPosition(position.x + (size.x - header_text.getGlobalBounds().width)/2,
				position.y + HEADER_SPACING_TOP);
		footer_text.setPosition(position.x + (size.x - footer_text.getGlobalBounds().width)/2,
				footer_y + FOOTER_SPACING_TOP);
		
		// Header
		header_left.setPosition(position);
		header_right.setPosition(position.x + size.x - header_right.getGlobalBounds().width, position.y);
		header_middle.setPosition(position.x + header_left.getGlobalBounds().width, position.y);
		header_middle.setTextureRect(new IntRect(0,0,
				(int) (size.x - header_right.getGlobalBounds().width - header_left.getGlobalBounds().width),
				(int) header_middle.getLocalBounds().height));
		
		// Footer
		footer_left.setPosition(position.x, footer_y);
		footer_right.setPosition(position.x + size.x - footer_right.getGlobalBounds().width, footer_y);
		footer_middle.setPosition(position.x + footer_left.getGlobalBounds().width, footer_y);
		footer_middle.setTextureRect(new IntRect(0,0,
				(int) (size.x - footer_right.getGlobalBounds().width - footer_left.getGlobalBounds().width),
				(int) footer_middle.getLocalBounds().height));
		
		// Dividers
		divider_top_left.setPosition(position.x,divider_top_y);
		divider_top_right.setPosition(position.x + size.x - divider_top_right.getGlobalBounds().width, divider_top_y);
		divider_top_middle.setPosition(position.x + divider_top_left.getGlobalBounds().width, divider_top_y);
		divider_top_middle.setTextureRect(new IntRect(0,0,
				(int) (size.x - divider_top_right.getGlobalBounds().width - divider_top_left.getGlobalBounds().width),
				(int) divider_top_middle.getLocalBounds().height));
		
		divider_bottom_left.setPosition(position.x, divider_bottom_y);
		divider_bottom_right.setPosition(position.x + size.x - divider_bottom_right.getGlobalBounds().width, divider_bottom_y);
		divider_bottom_middle.setPosition(position.x + divider_bottom_left.getGlobalBounds().width, divider_bottom_y);
		divider_bottom_middle.setTextureRect(new IntRect(0,0,
				(int) (size.x - divider_bottom_right.getGlobalBounds().width - divider_bottom_left.getGlobalBounds().width),
				(int) divider_bottom_middle.getLocalBounds().height));
		
		// Contentbox
		corner_top_left.setPosition(position.x,content_y);
		corner_top_right.setPosition(position.x + size.x - corner_top_right.getGlobalBounds().width,content_y);
		corner_top.setPosition(position.x + corner_top_left.getGlobalBounds().width,content_y);
		corner_top.setTextureRect(new IntRect(0,0,
				(int) (size.x - corner_top_left.getGlobalBounds().width - corner_top_right.getGlobalBounds().width),
				(int) corner_top.getLocalBounds().height));
		
		corner_bottom_left.setPosition(position.x,
				content_y + size_content_y - corner_bottom_left.getGlobalBounds().height);
		corner_bottom_right.setPosition(position.x + size.x - corner_bottom_right.getGlobalBounds().width,
				content_y + size_content_y - corner_bottom_left.getGlobalBounds().height);
		corner_bottom.setPosition(position.x + corner_bottom_left.getGlobalBounds().width,
				content_y + size_content_y - corner_bottom_left.getGlobalBounds().height);
		corner_bottom.setTextureRect(new IntRect(0,0,
				(int) (size.x - corner_top_left.getGlobalBounds().width - corner_top_right.getGlobalBounds().width),
				(int) corner_top.getLocalBounds().height));
		
		corner_left.setPosition(position.x,content_y + corner_top_left.getGlobalBounds().height);
		corner_right.setPosition(position.x + size.x - corner_right.getGlobalBounds().width,
				content_y + corner_top_left.getGlobalBounds().height);
		corner_middle.setPosition(position.x + corner_left.getGlobalBounds().width,
				content_y + corner_top_left.getGlobalBounds().height);
		corner_left.setTextureRect(new IntRect(0,0,(int) corner_left.getLocalBounds().width,
				(int) (size_content_y - corner_top_left.getGlobalBounds().height -
						corner_bottom_left.getGlobalBounds().height)));
		corner_right.setTextureRect(new IntRect(0,0,(int) corner_right.getLocalBounds().width,
				(int) (size_content_y - corner_top_right.getGlobalBounds().height -
						corner_bottom_right.getGlobalBounds().height)));
		corner_middle.setTextureRect(new IntRect(0,0,
				(int) (size.x - corner_top_left.getGlobalBounds().width - corner_top_right.getGlobalBounds().width),
				(int) (size_content_y - corner_top_left.getGlobalBounds().height -
						corner_bottom_left.getGlobalBounds().height)));
	}
	
	public void queueText(String text) {
		total_text.add(text);
	}
	
	public String getText(int number) {
		return total_text.get(number);
	}
	
	public void removeText(int number) {
		total_text.remove(number);
	}
	
	public int getNumberOfTexts() {
		return total_text.size();
	}
	
	public StatusBox getBoxState() {
		return box_state;
	}
	
	public StatusWriting getWritingState() {
		return writing_state;
	}
	
	public void openBox() {
		if (box_state == StatusBox.closed) {
			box_state = StatusBox.is_opening;
			percent_open = 0;
			resetText();
			resetIndex();
			updateContentText();
		}
	}
	
	public void closeBox() {
		if (box_state == StatusBox.open) {
			box_state = StatusBox.is_closing;
			percent_open = 1;
			lockBox();
		}
	}
	
	public void changeSize(Vector2f size) {
		this.size = size;
		updatePosition();
	}
	
	public void changePosition(Vector2f position) {
		this.position = position;
		updatePosition();
	}
	
	public Vector2f getPosition() {
		return new Vector2f(position.x,position.y);
	}
	
	public Vector2f getSize() {
		return new Vector2f(size.x,size.y);
	}
	
	public void nextText() {
		if (locked) return;
		if (writing_state == StatusWriting.is_writing) {
			skipWrite();
			return;
		}
		
		if (current_text_index + 1 == getNumberOfTexts()) {
			closeBox();
			return;
		}
		
		if (box_state == StatusBox.closed) {
			openBox();
			return;
		}
		
		gotoText(current_text_index + 1);
	}
	
	public void previousText() {
		if (locked) return;
		skipWrite();
		gotoText(current_text_index - 1);
	}
	
	public void gotoText(int number) {
		if (locked) return;
		if (number<getNumberOfTexts() && number >= 0) {
			resetText();
			current_text_index = number;
		}
	}
	
	public void skipWrite() {
		if (writing_state == StatusWriting.is_writing) {
			String text = total_text.get(current_text_index);
			current_text = text.substring(0, text.length()-1);
		}
	}
	
	public void lockBox() {
		locked = true;
	}
	
	public void unlockBox() {
		locked = false;
	}
	
	private void resetText() {
		current_text = "";
	}
	
	public void updateHeader(String title) {
		header_text = new Text(title,font,HEADER_TEXT_SIZE);
		updatePosition();
	}
	
	public void updateFooter(String footer) {
		footer_text = new Text(footer,font,FOOTER_TEXT_SIZE);
		updatePosition();
	}
	
	private void resetIndex() {
		current_text_index = 0;
	}
	
	private void handleStatuses() {
		
		// Handle box state
		if (percent_open == 1 && box_state == StatusBox.is_opening) {
			box_state = StatusBox.open;
		} else if (percent_open == 0 && box_state == StatusBox.is_closing) {
			box_state = StatusBox.closed;
		}
		
		// Handle writing state
		if (box_state == StatusBox.open) {
			if (getNumberOfTexts() == 0 || current_text == total_text.get(current_text_index)) {
				writing_state = StatusWriting.ready;
			} else {
				writing_state = StatusWriting.is_writing;
			}
		} else {
			writing_state = StatusWriting.interrupted;
		}
	}
	
	private void updateContentText() {
		content_text = new Text(current_text,font,CONTENT_TEXT_SIZE);
		content_text.setPosition(content_text_position);
	}
	
	public void update() {
		
		handleStatuses();
		
		switch (box_state) {
			case is_opening:
				percent_open += OPEN_CLOSE_SPEED;
				percent_open = percent_open<0 ? 0 : percent_open>1 ? 1 : percent_open;
				
				Color opaque_color = new Color(255,255,255,(int) (percent_open*255));
				
				for (Sprite sprite : elements) {
					sprite.setColor(opaque_color);
				}
				
				content_text.setColor(opaque_color);
				header_text.setColor(opaque_color);
				footer_text.setColor(opaque_color);
				break;
			case is_closing:
				percent_open -= OPEN_CLOSE_SPEED;
				percent_open = percent_open<0 ? 0 : percent_open>1 ? 1 : percent_open;
				
				Color opaque_color_2 = new Color(255,255,255,(int) (percent_open*255));
				
				for (Sprite sprite : elements) {
					sprite.setColor(opaque_color_2);
				}
				
				content_text.setColor(opaque_color_2);
				header_text.setColor(opaque_color_2);
				footer_text.setColor(opaque_color_2);
				break;
			case open:
				break;
			case closed:
				break;
		}
		
		switch (writing_state) {
			case is_writing:
				if (current_text.length()<=total_text.get(current_text_index).length()) {
					if (tick_counter<TICKS_BETWEEN_NEW_LETTER) {
						tick_counter++;
					} else {
						tick_counter = 0;
						current_text = total_text.get(current_text_index).substring(0,current_text.length()+1);
						updateContentText();
						keypress.play();
					}
				}
				break;
			case ready:
				break;
			case interrupted:
				break;
		}
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		if (box_state == StatusBox.closed) return;
		
		for (Drawable drawable : elements) {
			drawable.draw(arg0, arg1);
		}
		
		content_text.draw(arg0, arg1);
		header_text.draw(arg0, arg1);
		footer_text.draw(arg0, arg1);
	}
}
