package bomberman.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


import bomberman.engine.MusicEngine;

/**
 * Class responsible for the display and update of the play menu of the game.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class PlayMenu extends BasicGameState {
	
	//ID of this state
	public static final int ID = 1;
	
	//Images
	private Image menuBackground,playMenu,menuCursor;
	
	//Variable for checking the menu choice
	private int menuChoice = 1;
	
	//Music
	MusicEngine music = MusicEngine.getInstance();
	
	//Cursor Position
	private int cursorX = 540;
	private int cursorY = 232;

	@Override
	/**
	 * Function responsible for initializing the images and variables used in the play menu 
	 */
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		menuBackground = new Image("gameResources/sprites/Menus/MenuBackground.png");
		menuCursor = new Image("gameResources/sprites/Menus/Cursor.png");
		playMenu = new Image("gameResources/sprites/Menus/Menu2.png");
	}

	@Override
	/**
	 * Function responsible for rendering all the graphical elements of this menu
	 */
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		menuBackground.draw(0,0);
		menuCursor.draw(cursorX, cursorY);
		playMenu.draw(600,240);
		
	}

	@Override
	/**
	 * Function responsible for updating the logic of this menu based on the input of the player
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
		
		Input input = gc.getInput();
		
		if(input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP)){
			cursorY-= 170;
			menuChoice-=1;
		}else
			if(input.isKeyPressed(Input.KEY_S) || input.isKeyPressed(Input.KEY_DOWN)){
				cursorY+=170;
				menuChoice+=1;
			}

		if(menuChoice == 3 ){
			menuChoice = 1;
			cursorY = 232;
		}else
			if(menuChoice == 0){
				menuChoice = 2;
				cursorY = 232+170;
			}
		
		if(input.isKeyPressed(Input.KEY_ENTER)){

			switch(menuChoice){
			case(1) : if(music.isMusicOn()){
						music.getBackgroundMusic().playAsMusic(1.0f, 1.0f, true);
					}
					 sbg.enterState(3);
					 break;
			case(2) : sbg.enterState(4);
					 break;
			}
		}
		
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			sbg.enterState(0);
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}
