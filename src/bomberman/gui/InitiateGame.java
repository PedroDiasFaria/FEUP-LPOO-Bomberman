package bomberman.gui;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Class responsible for the initializing the game.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class InitiateGame extends StateBasedGame{

	public InitiateGame() {
		super("Bomberman");
	}
	
	/**
	 * Initiates the game
	 * @param args
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException
	{
		AppGameContainer app = new AppGameContainer(new InitiateGame());
		app.setIcon("gameResources/sprites/Icon/Bomberman.png");
		app.setDisplayMode(800, 640, false);
		app.start();
	}

	@Override
	/**
	 * Initiates the state list of the game
	 */
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new InitialMenu());
		addState(new PlayMenu());
		addState(new SoundMenu());
		addState(new Campaign());
		addState(new TwoPlayersMode());
	}

}
