package game;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.audio.*;
import org.jsfml.window.event.*;

import engine.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Core {
	private Player m_player;
	static RenderCam render_cam;
	
	private BottomOfTheWorld m_bedrock;
	private InfiniteBox m_magma;
	
	static ArrayList<DynamicObject> physical_objects;
	//private RockCeiling m_rockceiling = new RockCeiling();
	
	public Core() throws IOException {
		m_player = new Player("res/drop2.png", new XYZRAxes(0,0,0,0), 10f);
		render_cam = new RenderCam(m_player, 1000, 0.01f);
		
		m_bedrock = new BottomOfTheWorld(render_cam);
		m_magma = new InfiniteBox(Main.START_OF_MAGMA,0,false,true,render_cam);
		m_magma.setColor(new Color(200,150,0));
		physical_objects = new ArrayList<DynamicObject>();
		
		physical_objects.add(m_player);
		render_cam.makeDrawable(m_player);
		//render_cam.makeDrawable(m_magma);
		render_cam.makeDrawable(m_bedrock);
		
		System.out.println("CORE\n");
		
		m_bedrock.generateTiles();
		
		run();
	}
	
	public void run() {
		while (Main.game_state == Main.states.core) {
			handleEvents();
			runGameLogic();
			addImpulses();
			render_cam.renderFrame();
		}
	}
	
	private void handleEvents() {
		for (Event event : Main.wnd.pollEvents()) {
			switch (event.type) {
				case CLOSED:
					Main.dispose();
					return;
				default:
					break;
			}
		}
		
		if (Keyboard.isKeyPressed(Keyboard.Key.LEFT)) {
			m_player.addImpulseX(-7.f);
		} else if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT)) {
			m_player.addImpulseX(7.f);
		} else if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
			m_player.jump();
		} else if (Keyboard.isKeyPressed(Keyboard.Key.RETURN)) {
			m_bedrock.eraseRandomTileAtTheTop();
		} else if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
			Main.game_state = Main.states.menu;
		}
	}
	
	private void runGameLogic() {

		// Add impulses applying all dynamics, add update dynamic objects, make an array for all dynamics, make static add automatically to cam-queue, make cam have speed instead

		if (Math.random() > 0.95) m_bedrock.eraseRandomTileAtTheTop();

		m_bedrock.getCollisionTilePosition(m_player.getX(),m_player.getY(),m_player.getZ());
		if (m_bedrock.doesATileExistHere(m_player.getX(),m_player.getY(),m_player.getZ()) && m_bedrock.doesATileExistHere(m_player.getX()+33,m_player.getY(),m_player.getZ()))
			m_player.setY(m_player.getY()+m_player.getSpeedY() + 0.5f);
		else
			m_player.setY(0);
		
		// If the new position is a collision, we must find the maximum allowed dX and dY
		if (m_player.getY() + m_player.getBoundBottom() > m_bedrock.getCollisionTilePosition(m_player.getX(),m_player.getY(),m_player.getZ()).getY())
		{
			m_player.setY(m_bedrock.getCollisionTilePosition(m_player.getX(),m_player.getY() + m_player.getBoundBottom(),m_player.getZ()).getY());
		}
		if (m_player.getY() + m_player.getBoundBottom() > m_bedrock.getCollisionTilePosition(m_player.getX() + m_player.getBoundRight(),m_player.getY(),m_player.getZ()).getY())
			m_player.setY(m_bedrock.getCollisionTilePosition(m_player.getX() + m_player.getBoundRight(),m_player.getY(),m_player.getZ()).getY() - m_player.getBoundBottom());
		
		// Reset movement on the x-axis, because that mostly comes from user input
		m_player.setSpeedX(0.f);
		
		//View v = Main.view;
		//v = new View(m_player, Main.wnd.getDefaultView().getSize());
		//v.move(m_rng.nextInt() % 2 - 1, m_rng.nextInt() % 2 - 1);
		//Main.wnd.setView(v);
	}
	
	private void addImpulses() {
		for (int i = 0; i < physical_objects.size(); i++) {
			physical_objects.get(i).addImpulseY(2*physical_objects.get(i).getMass());
			physical_objects.get(i).update();
		}
	}
	
	private void runCollisionTestsOnPlayer()
	{
		
	}
}
