// Hvis du får errorer, så må du legge inn pathene til lwjgl manuelt
// bare klikk på properties på prosjektet, java build path, og så add external libary
// marker så de tre filene i lib/jars og OK, deretter åpne pilen vedsidena lwjgl.jar og native libary location
// så edit og velg mappa lib/natives-win

// Dette er ikke final, kun en test så du kan se hvordan lwjgl settes opp i forhold til den andre metoden
// Selv er jeg usikker på hvem som er best, du får bedømme, det andre setupet er i sauce, selv fikk jeg det ikke til
// å virke, er det noen bibloteker jeg ikke har?
// Om noe skrewer seg opp, legger jeg dette oppsettet i en annen branch så det er bare å hoppe over til den forrige :)

// Jeg har fulgt denne tutorialen, virket ganske grei: http://www.youtube.com/watch?v=0v56I5UWrYY

package game;

// Standard import
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.*;
import org.lwjgl.*;

public class Main {
	
	// Constructor
	public static int screen_width;
	public static int screen_height;
	public static String game_title;
	public static states game_state;
	
	public enum states {
		menu, tutorial, surface, core, game, scoreboard;
	}
	
	private void init() {
		
		// Setup variables
		screen_width = 640;
		screen_height = 480;
		game_title = "Sauce";
		game_state = states.menu;
		
		// Setup screen
		try {
			Display.setDisplayMode(new DisplayMode(screen_width, screen_height));
			Display.setTitle(game_title);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		// Setup openGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	
	private void run() {
		
		// Run program until close
		while (!Display.isCloseRequested()) {
			
			// Initialize and run a state in the game
			switch (game_state) {
				case menu:
					Menu menu = new Menu();
					menu.init();
					menu.run();
				case tutorial:
				case surface:
				case core:
				case game:
				case scoreboard:
				default:
					return;
			}
		}
	}
	
	private void dispose() {
		// Remove the window
		Display.destroy();
		
		// Exit the system
		System.exit(0);
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.init();
		main.run();
		main.dispose();
	}
}
