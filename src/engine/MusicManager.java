package engine;


import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.audio.*;


public class MusicManager implements Runnable
{
	// Constructor
	public MusicManager()
	{
		which_is_playing = false;
		runs = true;
		vol = 100.f;
		fadespeed = 20;
		
		m_ms[0] = new Music();
		m_ms[1] = new Music();
		
		thr.start();
	}
	
	protected void finalize()
	{
		runs = false;
		try {
			thr.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fade(String file)
	{
		synchronized(istr)
		{
			istr = file;
		}
		synchronized(signal)
		{
			signal.notify();
		}
	}
	
	public void setVolume(float volume)
	{
		vol = volume;
		m_ms[which_is_playing ? 1 : 0].setVolume(vol);
	}
	
	public void play()
	{
		m_ms[(which_is_playing ? 1 : 0)].play();
	}
	
	public void pause()
	{
		m_ms[(which_is_playing ? 1 : 0)].pause();
	}
	
	public void stop()
	{
		m_ms[(which_is_playing ? 1 : 0)].stop();
	}
	
	public void setFadeSpeed(int fadespeed_ms)
	{
		synchronized(fadespeed)
		{
			fadespeed = fadespeed_ms;
		}
	}
	
	Music[] m_ms = new Music[2];

	private boolean 
		which_is_playing,
		runs;
	
	public void run()
	{
		synchronized(signal)
		{
			try {
				signal.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (runs)
		{
			synchronized(m_ms[(which_is_playing ? 1 : 0)])
			{
				try {
					m_ms[(which_is_playing ? 1 : 0)].openFromFile(Paths.get(istr));
				} catch (IOException e) {
					System.out.println("File could not be found");
				};
			}
			
			m_ms[(which_is_playing ? 1 : 0)].play();
			
			for (float i = 0.f; i <= vol; ++i)
			{
				m_ms[(which_is_playing ? 1 : 0)].setVolume(i);
				m_ms[(which_is_playing ? 0 : 1)].setVolume(100.f - i);
				try {
					Thread.sleep(fadespeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			m_ms[(which_is_playing ? 0 : 1)].stop();
			
			which_is_playing = !which_is_playing;
			
			synchronized(signal)
			{
				try {
					signal.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	float vol;
	Integer fadespeed;
	String istr = new String();
	
	Thread thr = new Thread(this);
	Boolean signal = new Boolean(true);
	
}