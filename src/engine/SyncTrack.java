package engine;

import org.jsfml.audio.Sound;

	/**
	 * Makes you able to play a unlimited amount of sounds from a single sound.
	 * You can change the sound by using fetchSound and setSound.
	 * If you use getSound, you can change volume and other things before playing.
	 */
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
