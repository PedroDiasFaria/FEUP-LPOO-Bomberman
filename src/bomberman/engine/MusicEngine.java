package bomberman.engine;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Class responsible for the music in the game and respective control of the sound.
 * Implements the Singleton Design Pattern.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class MusicEngine {
	
		private Audio backgroundMusic;
		private boolean musicOn;
 
        /**
         * Default Constructor of this class.This function initiates the sound script with the initial values.
         */
        private MusicEngine (){
               musicOn = true;
               try {
       			this.setBackgroundMusic(AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("gameResources/Music/A_Bit_of_Daft_Punk.ogg")));
       		} catch (IOException e) {
       			e.printStackTrace();
       		}
        }
       
        /**
         * Function that creates a new instance of this class if none was created before.
         *
         */
        private static class MusicEngineHolder{
                public static final MusicEngine  INSTANCE = new MusicEngine ();
        }
       
        /**
         * @return the instance of the class currently created, or a new one if none was created so far.
         */
        public static MusicEngine getInstance() {
                return MusicEngineHolder.INSTANCE;
        }

		public Audio getBackgroundMusic() {
			return backgroundMusic;
		}

		public void setBackgroundMusic(Audio backgroundMusic) {
			this.backgroundMusic = backgroundMusic;
		}

		public boolean isMusicOn() {
			return musicOn;
		}

		public void setMusicOn(boolean musicOn) {
			this.musicOn = musicOn;
		}
        
        
}