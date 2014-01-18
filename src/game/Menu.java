package game;

//Standard import
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.*;
//import org.lwjgl.*; isn't used yet in this class, but might be in the future?

//Class-specific import
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

public class Menu {
	// Constructor
	
	public void init() {
	}
	
	public void run() {
		
		// Initiate the loop
		while (!Display.isCloseRequested() && Main.game_state == Main.states.menu) {
			
			// Clear the window
			glClear(GL_COLOR_BUFFER_BIT);
			
			// Render image
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Main.game_state = Main.states.tutorial;
			}
			
			int mouse_y = Main.screen_height - Mouse.getY() - 1;
			int mouse_x = Mouse.getX();
			int mouse_dy = -Mouse.getDY();
			int mouse_dx = Mouse.getDX();
			
			glBegin(GL_LINES);
				glVertex2i(mouse_x,mouse_y);
				glVertex2i(mouse_x+mouse_dx,mouse_y+mouse_dy);
			glEnd();
			
			glBegin(GL_QUADS);
				glVertex2i(400, 400); // Upper-left
				glVertex2i(450, 400); // Upper-right
				glVertex2i(450, 450); // Bottom-right
				glVertex2i(400, 450); // Bottom-left
			glEnd();
			
			// Show the rendering
			Display.update();
			Display.sync(60);
		}
	}
}
