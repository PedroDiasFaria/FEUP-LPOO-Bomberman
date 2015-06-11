package bomberman.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import bomberman.engine.Level;
import bomberman.engine.PinkBomberman;

/**
 * Class responsible for the display and update of the two players menu of the game.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class TwoPlayersMode extends BasicGameState{
	
	//ID of this state
	public static final int ID = 4;

	//Images
	private Image player1VictoryImage,player2VictoryImage;
	
	//Level used to play 
	private Level level;
	
	//Pink bomberman for the second player
	PinkBomberman player2 ;
	
	//Variable to check who was the player who won
	private int winnerPlayer = 0;
	

	@Override
	/**
	 * Function responsible for initializing the images and variables used in the two players mode
	 */
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		level = new Level("gameResources/level7/level7.tmx",7,0);
		player2 = new PinkBomberman(640,352,1,1,0.15f, level);
		player1VictoryImage = new Image("gameResources/Sprites/Menus/P1Win.png");
		player2VictoryImage = new Image("gameResources/Sprites/Menus/P2Win.png");
	}

	@Override
	/**
	 * Function responsible for updating the game logic of this game mode based on the input of the players
	 */
	public void update(GameContainer gc, StateBasedGame sbg,int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		//game logic(AI,user input)
		level.getBomberman().move(gc,delta,level);
		player2.move(gc, delta, level);
		
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
		
		for(int b=0;b<player2.getBombs().size();b++){
			if(player2.getBombs().get(b).getBombFlag() == 1){
				if(player2.getBombs().get(b).getY()+24 < player2.getY() || player2.getBombs().get(b).getY()-24 > player2.getY() || player2.getBombs().get(b).getX()+24 < player2.getX() || player2.getBombs().get(b).getX()-24 > player2.getX()){
					level.setCollisions((int) (player2.getBombs().get(b).getX()/32),(int) (player2.getBombs().get(b).getY()/32),true);
				}

				if(player2.getBombs().get(b).getAnimation().isStopped()){
					player2.getBombs().get(b).setExplodeBombFlag(1);
					//level.detonateOtherBombs(level, b);
					player2.setBombsActivated((((int)((player2.getBombs().get(b).getX())/32) * 32)+8)/32 , (((int)((player2.getBombs().get(b).getY())/32) * 32)+8)/32 , false);
				}
			}else
				level.setCollisions((int) (player2.getBombs().get(b).getX()/32),(int) (player2.getBombs().get(b).getY()/32),false);
		}

		for(int i=0;i<level.getMonsters().size();i++)
			level.getMonsters().get(i).getAnimation().update(delta);
		level.checkIfEndOfLevel();
		
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			sbg.enterState(0);
		}
	}

	@Override
	/**
	 * Function responsible for rendering all the graphical elements of this game mode
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		//draws all graphics
		level.getMap().render(0,0);
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
		
		for(int b=0;b<player2.getBombs().size();b++){
			if(player2.getBombs().get(b).getBombFlag() == 1){
				player2.getBombs().get(b).getBombAnimation().draw(player2.getBombs().get(b).getX(), player2.getBombs().get(b).getY());

				if(player2.getBombs().get(b).getExplodeBombFlag() == 1){
					if(level.getWallDestructionAnimation().isStopped() || player2.getBombs().get(b).getCenterExplosionAnimation().isStopped()){
						player2.getBombs().get(b).setBombFlag(0);
						level.eraseExplosions(b);
						player2.getBombs().get(b).setExplodeBombFlag(0);
						player2.getBombs().get(b).setBombAvailableFlag(0);
					}
					else{
						for(int i=0;i<level.getMonsters().size();i++)
							level.getMonsters().get(i).checkIfIsAlive(level);

						player2.getBombs().get(b).explode(level);
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
					
					}
				}
			}
		
		level.getBomberman().checkIfIsAlive(level);
		player2.checkIfIsAlive(level);
		
		if(level.getBomberman().getAnimation().isStopped() && winnerPlayer!=2){
			if(!gc.getInput().isKeyPressed(Input.KEY_ENTER)){
				winnerPlayer = 1;
				player2VictoryImage.draw(150,120);
			}else{
				winnerPlayer = 0;
				this.init(gc, sbg);
				sbg.enterState(0);
			}
		}else
			if(player2.getAnimation().isStopped() && winnerPlayer!=1){
				if(!gc.getInput().isKeyPressed(Input.KEY_ENTER)){
					winnerPlayer = 2;
					player1VictoryImage.draw(150,120);
				}else{
					winnerPlayer = 0;
					this.init(gc, sbg);
					sbg.enterState(0);
				}
		}

		if(level.getBomberman().isAlive() || !level.getBomberman().getAnimation().isStopped())
			level.getBomberman().getAnimation().draw((int)level.getBomberman().getX(), (int)level.getBomberman().getY());
		
		if(player2.isAlive() || !player2.getAnimation().isStopped())
			player2.getAnimation().draw((int)player2.getX(), (int)player2.getY());

		for(int i=0;i<level.getMonsters().size();i++){
			if(level.getMonsters().get(i).isAlive())
				level.getMonsters().get(i).getAnimation().draw((int)level.getMonsters().get(i).getX(),(int)level.getMonsters().get(i).getY());
		}

	}

	@Override
	public int getID() {
		return this.ID;
	}

}
