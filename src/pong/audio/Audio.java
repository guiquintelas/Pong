package pong.audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Audio {
	public String nome;
	protected Clip clip;

	public Audio(String fileName) {		
		URL url = getClass().getResource(fileName);
		
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audio);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void play() {
		clip.stop();
		clip.setFramePosition(0);
		clip.start();							
	}
	
	public void stop() {
		clip.stop();
		clip.setFramePosition(0);
	}
	

}
