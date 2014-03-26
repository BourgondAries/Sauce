package engine;

import org.jsfml.audio.Sound;

public class SyncTrack {
	private Sound track;
	
	public SyncTrack(Sound track) {
		this.track = track;
	}
	
	public Sound fetchTrack() {
		return track;
	}
	
	public void setTrack(Sound track) {
		this.track = track;
	}
	
	public Sound getTrack() {
		return new Sound(track);
	}
	
	public void play() {
		Sound new_sound = new Sound(track);
		
		new_sound.play();
	}
}
