package engine;

import java.util.ArrayList;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundSource;

/**
 * Makes you able to play a unlimited amount of sounds from a single sound.
 * You can change the sound by using fetchSound and setSound.
 * If you use getSound, you can change volume and other things before playing.
 * Use cleanList to remove old sounds that are done playing.
 * 
 * @BUG: Pushing too many tracks disables the entire sound engine.
 */
public class SyncTrack {
	private Sound track;
	private static ArrayList<Sound> sounds_playing = new ArrayList<>();
	
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
	
	public static void cleanList() {
		for (int i = 0; i<sounds_playing.size(); i++) {
			Sound sound = sounds_playing.get(i);
			if (sound.getStatus() != SoundSource.Status.PLAYING) 
			{
				System.out.println("Removed sound");
				sounds_playing.remove(i);
				i--;
			}
		}
	}
	
	public static void forceClean()
	{
		for(Sound x : sounds_playing)
			x.stop();
		sounds_playing.clear();
	}
	
	public void play() {
		if (sounds_playing.size() > 30)
			forceClean();
		else
			cleanList();
		Sound new_sound = new Sound(track);
		sounds_playing.add(new_sound);
		new_sound.play();
	}
	
	public static void play(Sound sound) {
		if (sounds_playing.size() > 30)
			forceClean();
		else
			cleanList();
		Sound new_sound = new Sound(sound);
		sounds_playing.add(new_sound);
		new_sound.play();
	}
}
