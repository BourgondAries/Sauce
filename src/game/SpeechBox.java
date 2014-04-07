package game;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;

public class SpeechBox implements Drawable {
	private String current_text;
	private int current_text_index;
	private List<String> total_text;
	
	private float percent_open;
	
	private float width = 600;
	private float height = 200;
	private float padding_content = 10;
	private int ticks_to_wait_between_leatters = 5;
	
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
	private Text text;
	
	private StatusBox box_state;
	private StatusWriting writing_state;
	
	private static final float OPEN_CLOSE_SPEED = 0.01f;
	
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
	
	public SpeechBox() {
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
	}
	
	private void loadTextures() {
		
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
				if (current_text.length()>=total_text.get(current_text_index).length())
				current_text = total_text.get(current_text_index).substring(0,current_text.length());
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
	}
}
