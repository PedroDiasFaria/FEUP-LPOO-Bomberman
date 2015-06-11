package bomberman.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import bomberman.engine.Level;
import bomberman.engine.MusicEngine;

/**
 * Class responsible for the display and update of the 1player campaign mode of the game.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class Campaign extends BasicGameState
{
	//Id of this state
	public static final int ID = 3;

	//Level of the game
	private Level level;

	//Counter of the number of the level
	private int levelNumber = 1;
	
	//Music
	MusicEngine music = MusicEngine.getInstance();

	public Campaign()
	{
		super();
	}

	
	@Override
	/**
	 * Function responsible for initializing the images and variables used in the campaign mode of the game 
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		//load all fonts,graphics,sounds,etc..
		String levelName = getLevelName(levelNumber);
		level = new Level(levelName,levelNumber,0);
		gc.setShowFPS(false);
	}

	@Override
	/**
	 * Function responsible for updating the logic of the campaign menu based on the input of the player
	 */
	public void update(GameContainer gc, StateBasedGame sbg,int delta) throws SlickException
	{
		Input input = gc.getInput();

		//game logic(AI,user input)
		level.getBomberman().move(gc,delta,level);
		for(int i=0;i<level.getMonsters().size();i++)
			level.getMonsters().get(i).move(delta,level);

		for(int b=0;b<level.getBomberman().getBombs().size();b++){
			if(level.getBomberman().getBombs().get(b).getBombFlag() == 1){
				if(level.getBomberman().getBombs().get(b).getY()+24 < level.getBomberman().getY() || level.getBomberman().getBombs().get(b).getY()-24 > level.getBomberman().getY() || level.getBomberman().getBombs().get(b).getX()+24 < level.getBomberman().getX() || level.getBomberman().getBombs().get(b).getX()-24 > level.getBomberman().getX()){
					level.setCollisions((int) (level.getBomberman().getBombs().get(b).getX()/32),(int) (level.getBomberman().getBombs().get(b).getY()/32),true);
				}

				if(level.getBomberman().getBombs().get(b).getAnimation().isStopped()){
					level.getBomberman().getBombs().get(b).setExplodeBombFlag(1);
					//level.detonateOtherBombs(level, b);
					level.getBomberman().setBombsActivated((((int)((level.getBomberman().getBombs().get(b).getX())/32) * 32)+8)/32 , (((int)((level.getBomberman().getBombs().get(b).getY())/32) * 32)+8)/32 , false);
				}
			}else
				level.setCollisions((int) (level.getBomberman().getBombs().get(b).getX()/32),(int) (level.getBomberman().getBombs().get(b).getY()/32),false);
		}

		for(int i=0;i<level.getMonsters().size();i++)
			level.getMonsters().get(i).getAnimation().update(delta);
		level.checkIfEndOfLevel();

		if(level.getEndOfLevel()){
			if(levelNumber != 6){
				level = new Level(getLevelName(levelNumber+1),levelNumber+1,level.getScore());
				levelNumber+=1;
			}
		}

		if(music.isMusicOn()){
			SoundStore.get().poll(0);
		}
		
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			sbg.enterState(0);
		}
		
	}

	@Override
	/**
	 * Function responsible for rendering all the graphical elements of the campaign mode of the game
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		//draws all graphics
		level.getMap().render(0,0);
		level.getLevelBar().draw(0,0);
		drawGamePanelValues();
		level.drawFloor();

		for(int b=0;b<level.getBomberman().getBombs().size();b++){
			if(level.getBomberman().getBombs().get(b).getBombFlag() == 1){
				level.getBomberman().getBombs().get(b).getBombAnimation().draw(level.getBomberman().getBombs().get(b).getX(), level.getBomberman().getBombs().get(b).getY());

				if(level.getBomberman().getBombs().get(b).getExplodeBombFlag() == 1){
					if(level.getWallDestructionAnimation().isStopped() || level.getBomberman().getBombs().get(b).getCenterExplosionAnimation().isStopped()){
						level.getBomberman().getBombs().get(b).setBombFlag(0);
						level.eraseExplosions(b);
					}
					else{
						for(int i=0;i<level.getMonsters().size();i++)
							level.getMonsters().get(i).checkIfIsAlive(level);

						level.getBomberman().getBombs().get(b).explode(level);
					}
				}
			}
		}
		if(level.getExitFlag() == 1){
			level.getExit().draw(level.getExitX()*32,level.getExitY()*32);
		}

		for(int i=0;i<level.getMap().getWidth();i++)
			for(int j=0;j<level.getMap().getHeight();j++)
			{
				if(level.getPowerUps()[i][j].isActivated() == true)
				{
					switch(level.getPowerUps()[i][j].getType()){

					case 1: level.getFirePowerUp().draw(i*32,j*32);
					break;
					case 2: level.getBombPowerUp().draw(i*32,j*32);
					break;
					case 3:	level.getSpeedPowerUp().draw(i*32,j*32);
					break;
					case 4: level.getSkullPowerUp().draw(i*32, j*32);
					break;
					case 5: level.getLifePowerUp().draw(i*32, j*32);
					break;

					}
				}
			}

		level.getBomberman().checkIfIsAlive(level);

		if(level.getBomberman().getAnimation().isStopped()){
			if(!gc.getInput().isKeyPressed(Input.KEY_ENTER)){
				level.getDefeatImage().draw(200,180);
			}else{
				this.init(gc, sbg);
				sbg.enterState(0);
			}
		}

		if(level.getEndOfLevel()){
			if(levelNumber == 6){
				if(!gc.getInput().isKeyPressed(Input.KEY_ENTER)){
					level.getVictoryImage().draw(200,180);
					level.getVictoryAnimation().draw(150,360);
				}else{
					this.init(gc, sbg);
					sbg.enterState(0);
				}
			}
		}

		if(level.getBomberman().isAlive() || !level.getBomberman().getAnimation().isStopped())
			level.getBomberman().getAnimation().draw((int)level.getBomberman().getX(), (int)level.getBomberman().getY());

		for(int i=0;i<level.getMonsters().size();i++){
			if(level.getMonsters().get(i).isAlive())
				level.getMonsters().get(i).getAnimation().draw((int)level.getMonsters().get(i).getX(),(int)level.getMonsters().get(i).getY());
		}

	}

	/**
	 * Function responsible for creating the string of location of the map to be used based on the actual levelnumber.
	 * @param level Level being used
	 * @return String with the location of the map of the current level
	 */
	public String getLevelName(int level){
		String levelNumber = "level".concat(Integer.toString(level));
		String levelName = "gameResources/".concat(levelNumber).concat("/").concat(levelNumber).concat(".tmx");
		return levelName;

	}

	/**
	 * Function responsible for drawing a number on the (x,y) position of the screen.
	 * @param x X position where the value is going to be drawn
	 * @param y Y position where the value is going to be drawn
	 * @param value Value of the number to be drawn
	 */
	public void drawNumber(int x,int y,int value){
		switch(value){
		case 0:level.getN0().draw(x, y);
		break;
		case 1:level.getN1().draw(x, y);
		break;
		case 2:level.getN2().draw(x, y);
		break;
		case 3:level.getN3().draw(x, y);
		break;
		case 4:level.getN4().draw(x, y);
		break;
		case 5:level.getN5().draw(x, y);
		break;
		case 6:level.getN6().draw(x, y);
		break;
		case 7:level.getN7().draw(x, y);
		break;
		case 8:level.getN8().draw(x, y);
		break;
		case 9:level.getN9().draw(x, y);
		break;
		}
	}

	/**
	 * Function responsible for drawing the number on the game panel on top of the level
	 */
	public void drawGamePanelValues(){
		//bomb powerup
		drawNumber(35,60,level.getBomberman().getBombs().size());
		//fire powerup
		drawNumber(135,60,level.getBomberman().getBombs().get(0).getRange());
		//life powerup
		drawNumber(235,60 ,level.getBomberman().getLives());
		drawScore();
	}

	/**
	 * Function responsible for drawing the score of the game on the game panel on top of the level
	 */
	public void drawScore(){
		String score = Integer.toString((int) level.getScore());
		int j=1;
		for(int i=score.length()-1;i>=0;i--){
			drawNumber(800-35*j,60,Character.getNumericValue(score.charAt(i)));
			j++;
		}
	}

	@Override
	public int getID() {
		return ID;
	}
}