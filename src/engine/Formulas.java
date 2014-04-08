package engine;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;

public class Formulas {
	private Formulas() {}
	
	public static float slow_top_then_return(float x) {
		return x*(1-x);
	}
	
	public static float slow_top_early_then_return(float x) {
		return (float) (3125*Math.pow(1-x,4)*x/256);
	}
	
	public static float slow_top_late_then_return(float x) {
		return (float) (3125*Math.pow(x,4)*(1-x)/256);
	}
	
	public static float slow_stop(float x) {
		return x*(2-x);
	}
	
	public static float very_slow_stop(float x) {
		return (float) (1-Math.pow(1-x,4));
	}
	
	public static float sinus_out(float x) {
		return (float) (Math.pow(Math.cos(x*0.5*Math.PI),2));
	}
	
	public static float sinus_in(float x) {
		return 1-sinus_out(x);
	}
	
	public static Music loadMusic(String path) throws IOException {
		Music new_sound = new Music();
		new_sound.openFromFile(Paths.get(path));
		return new_sound;
	}
	
	public static SyncTrack loadSound(String path) throws IOException {
		Sound new_sound = new Sound();
		new_sound.setBuffer(PathedSounds.getSound(Paths.get(path)));
		return new SyncTrack(new_sound);
	}
}
