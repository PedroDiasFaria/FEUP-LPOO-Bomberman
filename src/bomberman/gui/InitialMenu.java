package bomberman.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Class responsible for the display and update of the initial menu of the game.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class InitialMenu extends BasicGameState{

	//ID of this state
	public static final int ID = 0;

	//Images
	private Image menuBackground,menuText,menuCursor,credits,options;
	//Flag to see if the credits are being shown or not
	private boolean creditsFlag = false;

	//Position of the cursor
	private int cursorX = 540;
	private int cursorY = 232;
	//Integer to know the menu choice
	private int menuChoice = 1;

	@Override
	/**
	 * Function responsible for initializing the images and variables used in the initial menu 
	 */
	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
		menuBackground = new Image("gameResources/sprites/Menus/MenuBackground.png");
		menuText = new Image("gameResources/sprites/Menus/Menu1.png");
		menuCursor = new Image("gameResources/sprites/Menus/Cursor.png");
		credits = new Image("gameResources/sprites/Menus/Credits.png");
	}

	@Override
	/**
	 * Function responsible for rendering all the graphical elements of this menu
	 */
	public void render(GameContainer gc, StateBasedGame arg1, Graphics arg2) throws SlickException {
		if(creditsFlag){
				credits.draw(0, 0);
			}else{
				menuBackground.draw(0,0);
				menuText.draw(600, 240);
				menuCursor.draw(cursorX,cursorY);
			}
	}

	@Override
	/**
	 * Function responsible for updating the logic of this menu based on the input of the player
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

		Input input = gc.getInput();

		if(creditsFlag == false){
			if(input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP)){
				cursorY-= 85;
				menuChoice-=1;
			}else
				if(input.isKeyPressed(Input.KEY_S) || input.isKeyPressed(Input.KEY_DOWN)){
					cursorY+=85;
					menuChoice+=1;
				}

			if(menuChoice == 5 ){
				menuChoice = 1;
				cursorY = 232;
			}else
				if(menuChoice == 0){
					menuChoice = 4;
					cursorY = 232+3*85;
				}

			if(input.isKeyPressed(Input.KEY_ENTER)){

				switch(menuChoice){
				case(1)	: sbg.enterState(1);
						break;
				case(2) : sbg.enterState(2);
						break;
				case(3) :creditsFlag = true;
						break;
				case(4) : System.exit(0);
				break;
				}
			}
		}

		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			creditsFlag = false;
			menuChoice = 4;
			cursorY = 232+3*85;
		}

	}

	@Override
	public int getID() {
		return ID;
	}

}
