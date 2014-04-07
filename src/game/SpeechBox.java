package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import engine.PathedFonts;
import engine.PathedTextures;

public class SpeechBox implements Drawable {
	private String current_text;
	private int current_text_index;
	private List<String> total_text;
	
	private float percent_open;
	
	private Vector2f size = new Vector2f(600,200);
	private Vector2f position = new Vector2f(0,0);
	private float padding_content = 10;
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
	
	private StatusBox box_state;
	private StatusWriting writing_state;
	
	private static final float OPEN_CLOSE_SPEED = 0.01f;
	private static final float TICKS_BETWEEN_NEW_LETTER = 5;
	private static final int CONTENT_TEXT_SIZE = 10;
	private static final int HEADER_TEXT_SIZE = 15;
	private static final int FOOTER_TEXT_SIZE = 15;
	private static final float DIVIDER_SPACING = 0;
	
	private static enum StatusBox {
		open,
		is_closing,
		is_opening,
		closed
	}
	
	private static enum StatusWriting {
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
		content_text = new Text("",font,CONTENT_TEXT_SIZE);
		header_text = new Text("",font,HEADER_TEXT_SIZE);
		footer_text = new Text("",font,FOOTER_TEXT_SIZE);
		
		// Update position
		updatePosition();
	}
	
	private void loadTextures() throws IOException {
		
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
				"res/textbox/textbox_footer_left.tga")));
		divider_bottom_left = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_footer_left.tga")));
		divider_top_middle = new Sprite(loadTileable("res/textbox/textbox_footer_middle.tga"));
		divider_bottom_middle = new Sprite(loadTileable("res/textbox/textbox_footer_middle.tga"));
		divider_top_right = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_footer_right.tga")));
		divider_bottom_right = new Sprite(PathedTextures.getTexture(Paths.get(
				"res/textbox/textbox_footer_right.tga")));
		
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
		
		// Header
		header_left.setPosition(position);
		header_right.setPosition(position.x + size.x - header_right.getGlobalBounds().width, position.y);
		header_middle.setPosition(position.x + header_left.getGlobalBounds().width, position.y);
		
		// Footer
		footer_left.setPosition(position.x, footer_y);
		footer_middle.setPosition(position.x + footer_left.getGlobalBounds().width, footer_y);
		footer_right.setPosition(position.x + size.x - footer_right.getGlobalBounds().width, footer_y);
		
		// Dividers
		divider_top_left.setPosition(position.x,divider_top_y);
		divider_top_middle.setPosition(position.x + divider_top_left.getGlobalBounds().width, divider_top_y);
		divider_top_right.setPosition(position.x + size.x - divider_top_right.getGlobalBounds().width, divider_top_y);
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
	
	public void openBox() {
		box_state = StatusBox.is_opening;
		percent_open = 0;
	}
	
	public void closeBox() {
		box_state = StatusBox.is_closing;
		percent_open = 1;
	}
	
	public void nextText() {
		gotoText(current_text_index + 1);
	}
	
	public void previousText() {
		gotoText(current_text_index - 1);
	}
	
	public void gotoText(int number) {
		if (number<getNumberOfTexts() && number > 0) {
			resetText();
			current_text_index = number;
		}
	}
	
	public void skipWrite() {
		if (writing_state == StatusWriting.is_writing) {
			current_text = total_text.get(current_text_index);
		}
	}
	
	private void resetText() {
		current_text = "";
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
	
	public void update() {
		
		handleStatuses();
		
		switch (box_state) {
			case is_opening:
				percent_open += OPEN_CLOSE_SPEED;
				percent_open = percent_open<0 ? 0 : percent_open>1 ? 1 : percent_open;
				break;
			case is_closing:
				percent_open -= OPEN_CLOSE_SPEED;
				percent_open = percent_open<0 ? 0 : percent_open>1 ? 1 : percent_open;
				break;
			case open:
				break;
			case closed:
				break;
		}
		
		switch (writing_state) {
			case is_writing:
				if (current_text.length()<=total_text.get(current_text_index).length())
				current_text = total_text.get(current_text_index).substring(0,current_text.length());
				text.setString(current_text);
				break;
			case ready:
				break;
			case interrupted:
				break;
		}
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		if (writing_state != StatusWriting.interrupted) text.draw(arg0, arg1);
		corner_bottom.draw(arg0, arg1);
		corner_top.draw(arg0, arg1);
		corner_left.draw(arg0, arg1);
		corner_right.draw(arg0, arg1);
		corner_middle.draw(arg0, arg1);
		corner_bottom_right.draw(arg0, arg1);
		corner_bottom_left.draw(arg0, arg1);
		corner_top_right.draw(arg0, arg1);
		corner_top_left.draw(arg0, arg1);
		
		header_left.draw(arg0, arg1);
		header_middle.draw(arg0, arg1);
		header_right.draw(arg0, arg1);
		
		footer_left.draw(arg0, arg1);
		footer_middle.draw(arg0, arg1);
		footer_right.draw(arg0, arg1);
		
		divider_top_left
	}
}
