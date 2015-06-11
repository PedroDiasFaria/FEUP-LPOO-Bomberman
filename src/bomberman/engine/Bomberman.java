package bomberman.engine;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Class responsible for creating and handling all the bomberman elements and events.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class Bomberman {

	//Logic
	//Coordinates
	private float x;
	private float y;
	//Number of lifes
	private int lives;
	//Alive flag
	private boolean alive = true;
	//Number of bombs he can put
	private int nBombs;
	//Range of his bombs
	private int bombRange;
	//Speed movement
	private float speed;

	//Sprite
	//Size of the sprite
	protected int bombermanSpriteWidth = 27;
	protected int bombermanSpriteHeight = 35;
	//Gap for the extra movement between sprites
	private int bombermanGap = bombermanSpriteHeight/2;
	//Bombs of the bomberman
	protected List<Bomb> bombs = new ArrayList<Bomb>();

	protected boolean[][] bombsActivated;;

	private Animation currentAnimation;

	//Bomberman Animation
	protected SpriteSheet bombermanFrontSheet,bombermanBackSheet,bombermanRightSheet,bombermanLeftSheet,bombermanDiesToBombSheet,bombermanDiesToMonsterSheet;
	protected Animation bombermanFrontAnimation,bombermanBackAnimation,bombermanRightAnimation,bombermanLeftAnimation,bombermanDiesToBombAnimation,bombermanDiesToMonsterAnimation;

	/**
	 * Constructor of this class.Function responsible for creating the bomberman.It initializes the bomberman animations,is current position
	 * his number of lives,number of bombs and speed.
	 * @param initialX X of the initial position of the bomberman
	 * @param initialY Y of the initial position of the bomberman
	 * @param lives Number of initial lives of the bomberman
	 * @param bombRange Range of the bombs of the bomberman
	 * @param speed Speed of the bomberman movement
	 * @param level Level where the bomberman is to be created
	 * @throws SlickException
	 */
	public Bomberman(float initialX,float initialY,int lives,int bombRange,float speed,Level level) throws SlickException{
		this.loadBombermanAnimations();
		this.currentAnimation = bombermanFrontAnimation;
		this.x = initialX;
		this.y = initialY;
		this.lives = lives;
		this.nBombs = 2;
		this.bombRange = bombRange;
		this.speed = speed;
		bombsActivated = new boolean[level.getMap().getWidth()][level.getMap().getHeight()];

		for(int i=0;i<10;i++)
			for(int j=0;j<10;j++)
				bombsActivated[i][j] = false;

		for(int i=0;i<nBombs;i++)
			bombs.add(new Bomb(bombRange+1));
		
	}

	/**
	 * Function responsible for the movement of the bomberman.
	 * @param gc GameContainer where the game is being displayed
	 * @param delta Number of milliseconds between frames
	 * @param level Level where the bomberman is to be moved
	 * @throws SlickException
	 */
	public void move(GameContainer gc,int delta,Level level) throws SlickException{

		//Variable to receive the input of the keyboard
		Input input = gc.getInput();

		//Checks if the bomberman is alive before moving
		if(this.isAlive()){
			if(input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)){
				//sets the current animation
				this.setAnimation(bombermanRightAnimation);
				//verifies the collisions
				if(!checkCollisionsSide((this.getX() + delta* this.getSpeed())+bombermanSpriteWidth,this.getY(),level)){
					//updates the current animation
					this.getAnimation().update(delta);
					//updates the position of the bomberman
					this.setX(this.getX() + delta * this.getSpeed());
				}
			}else
				if(input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)){
					//sets the current animation
					this.setAnimation(bombermanLeftAnimation);
					//verifies the collisions
					if(!checkCollisionsSide(this.getX() - delta*this.getSpeed(),this.getY(),level)){
						//updates the current animation
						this.getAnimation().update(delta);
						//updates the position of the bomberman
						this.setX(this.getX() - delta *this.getSpeed());
					}
				}else
					if(input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)){
						//sets the current animation
						this.setAnimation(bombermanFrontAnimation);
						//verifies the collisions
						if(!checkCollisionsUpDown(this.getX(),(this.getY() + delta*this.getSpeed())+bombermanSpriteHeight,level)){
							//updates the current animation
							this.getAnimation().update(delta);
							//updates the position of the bomberman
							this.setY(this.getY() + delta * this.getSpeed());
						}
					}else
						if(input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)){
							//sets the current animation
							this.setAnimation(bombermanBackAnimation);
							//verifies the collisions
							if(!checkCollisionsUpDown(this.getX(),this.getY() - delta*this.getSpeed()+10,level)){
								//updates the current animation
								this.getAnimation().update(delta);
								//updates the position of the bomberman
								this.setY(this.getY() - delta * this.getSpeed());
							}
						}else{
							//if the bomberman is not moving put resets the animation to the first animation
							bombermanFrontAnimation.setCurrentFrame(0);
							this.setAnimation(bombermanFrontAnimation);
						}
			
			//Checks if the player putted a bomb
			if(input.isKeyDown(Input.KEY_SPACE)){
				for(int b=0;b<bombs.size();b++){
					if(bombs.get(b).getBombFlag() == 1){
						if(bombs.get(b).getBombAvailableFlag() == 0){
							bombs.get(b).setBombFlag(0);
						}
					}
					else{//checks if the bomberman hasn´t already put a bomb in that spot
						if((bombsActivated[(int)this.getX()/32][((int)this.getY()+32)/32] == false) && bombs.get(b).getBombAvailableFlag() == 0){
							bombsActivated[(int)this.getX()/32][((int)this.getY()+32)/32] = true;
							bombs.get(b).setBombFlag(1);
							bombs.get(b).setBombAvailableFlag(1);
							level.getWallDestructionAnimation().restart();
							putBomb(b);
						}
					}
				}
			}
			//checks for powerUps 
			checksForPowerUp(level);
		}
	}

	/**
	 * Checks for collisions left and right.It checks for half of the bomberman sprite height if that sprite intersects a blocked element of the level.
	 * @param x X of the position of the bomberman
	 * @param y Y of the position of the bomberman
	 * @param level Level where the bomberman is
	 * @return true if it is possible to move or false if it isn't
	 */
	public boolean checkCollisionsSide(float x,float y,Level level){
		for(int i=0;i<bombermanSpriteHeight/2;i++){
			if(isBlocked(x,y+i+bombermanGap,level))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks for collisions up and down.It checks for the bomberman sprite width if any pixel intersects a blocked element of the level.
	 * @param x X of the position of the bomberman
	 * @param y Y of the position of the bomberman
	 * @param level Level where the bomberman is
	 * @return true if it is possible to move or false if it isn't
	 */
	public boolean checkCollisionsUpDown(float x,float y,Level level){
		for(int i=0;i<bombermanSpriteWidth;i++){
			if(isBlocked(x+i,y,level))
				return true;
		}
		return false;
	}
	
	/**
	 * Function responsible for checking the value of the pixel of the level on the collisions matrix.
	 * @param x X of the pixel to be checked
	 * @param y Y of the pixel to be checked
	 * @param level Level where the pixel is
	 * @return The value of the collision matrix.True if it is to be collided or false otherwise
	 */
	public boolean isBlocked(float x,float y,Level level){
		int xBlock = (int)(x / 32);
		int yBlock = (int)(y / 32);
		return level.getCollisions(xBlock, yBlock);
	}

	/**
	 * Function responsible for putting a bomb on the level in the actual position of the bomberman
	 * @param b Number of the bomb to be put
	 */
	public void putBomb(int b){
		//restarts the animations of this bomb
		bombs.get(b).getAnimation().restart();
		bombs.get(b).getCenterExplosionAnimation().restart();
		bombs.get(b).getMidExplosionAnimation().restart();
		bombs.get(b).getEndExplosionAnimation().restart();
		//restarts the flag of the explosion
		bombs.get(b).setExplodeBombFlag(0);
		//sets the new position of the bomb
		bombs.get(b).setX(((int)((this.getX())/32) * 32)+8);
		bombs.get(b).setY(((int)((this.getY()+32)/32) * 32)+8);
	}

	/**
	 * Function responsible for checking if there is powerUps in the actual position of the bomberman.It there is its checks for
	 * its type.
	 * @param level Level where the bomberman is
	 * @throws SlickException
	 */
	public void checksForPowerUp(Level level) throws SlickException{
		if(level.getPowerUps()[(int)(this.getX()/32)][((int)(this.getY()+32)/32)].getExists() == true){

			switch(level.getPowerUps()[(int)(this.getX()/32)][(int)((this.getY()+32)/32)].getType()){

			case 1: firePowerUp();
					level.setScore(level.getScore()+100);
			break;
			case 2: bombPowerUp();
					level.setScore(level.getScore()+100);	
			break;
			case 3:	speedPowerUp();
					level.setScore(level.getScore()+100);
			break;
			case 4: skullPowerUp();
			break;
			case 5: lifePowerUp();
					level.setScore(level.getScore()+100);
					break;
			}
			level.getPowerUps()[(int)this.getX()/32][(int)(this.getY()+32)/32].setExists(false);
			level.getPowerUps()[(int)this.getX()/32][(int)(this.getY()+32)/32].setActivated(false);
		}
	}

	/**
	 * Function responsible for getting the firePowerUp
	 */
	public void firePowerUp(){
		for(int b=0;b<this.getBombs().size();b++)
			this.getBombs().get(b).setRange(this.getBombs().get(b).getRange()+1);
	}

	/**
	 * Function responsible for getting the bombPowerUp
	 */
	public void bombPowerUp() throws SlickException{
		this.getBombs().add(new Bomb(this.getBombs().get(0).getRange()));
	}

	/**
	 * Function responsible for getting the speedPowerUp
	 */
	public void speedPowerUp(){
		this.setSpeed(this.getSpeed()+0.05f);
	}
	
	/**
	 * Function responsible for getting the lifePowerUp
	 */
	public void lifePowerUp(){
		this.lives++;
	}

	/**
	 * Function responsible for getting the skullPowerUp
	 */
	public void skullPowerUp() throws SlickException{
		Random randomGenerator = new Random();
		int skullEffect = randomGenerator.nextInt(3);

		switch(skullEffect){

		//decreases the explosion range by 1
		case 0: for(int b=0;b<this.getBombs().size();b++)
			if(this.getBombs().get(b).getRange()>2)
				this.getBombs().get(b).setRange(this.getBombs().get(b).getRange()-1);
		break;
		//decreases the number of bombs by 1
		case 1: this.getBombs().remove(this.getBombs().size()-1);
		break;
		//decreases the number of lifes by 1
		case 2:	this.setLives(this.getLives()-1);
			break;
		}
	}

	/**
	 * Function responsible for checking the alive status of the bomberman.It checks if he has died to a bomb or to a monster.
	 * @param level Level where the bomberman is
	 */
	public void checkIfIsAlive(Level level){
		for(int i=0;i<level.getMap().getWidth();i++){
			for(int j=0;j<level.getMap().getHeight();j++){

				//checks if a bomb kills bomberman
				if(level.getExplosionsLocation(i,j)){
					if((((int)(this.getX()))/32 == i) && (((int)this.getY())/32 == (j-1))){
						this.currentAnimation = bombermanDiesToBombAnimation; 
						this.setAlive(false);
					}
				}else{
					//checks if there if a monster kills bomberman
					for(int m=0;m<level.getMonsters().size();m++){
						if(((int)(level.getMonsters().get(m).getX())/32) == ((int)(this.getX())/32) && ((int)(level.getMonsters().get(m).getY())/32) == ((int)(this.getY())/32)+1){
							this.currentAnimation = bombermanDiesToMonsterAnimation;
							this.setAlive(false);
						}
					}
				}
			}
		}

		if(this.currentAnimation.isStopped()){
			if(this.getLives() == 0){
				this.setAlive(false);
				//System.exit(0);	
			}else{
				this.setLives(this.getLives()-1);
				bombermanDiesToBombAnimation.restart();
				bombermanDiesToMonsterAnimation.restart();
				this.setAlive(true);
				this.currentAnimation = bombermanFrontAnimation;
			}
		}
	}
	
	/**
	 * Function responsible for loading the bomberman animations
	 * @throws SlickException
	 */
	public void loadBombermanAnimations() throws SlickException{
		//Bomberman Movement Animations
		bombermanFrontSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanFront.png",27,35);
		bombermanFrontAnimation = new Animation(bombermanFrontSheet,300);	
		bombermanBackSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanBack.png",27,35);
		bombermanBackAnimation = new Animation(bombermanBackSheet,300);
		bombermanRightSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanRight.png",27,35);
		bombermanRightAnimation = new Animation(bombermanRightSheet,300);
		bombermanLeftSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanLeft.png",26,35);
		bombermanLeftAnimation = new Animation(bombermanLeftSheet,300);
		bombermanFrontAnimation.setAutoUpdate(false);
		bombermanBackAnimation.setAutoUpdate(false);
		bombermanRightAnimation.setAutoUpdate(false);
		bombermanLeftAnimation.setAutoUpdate(false);

		//Bomberman Deads Animations
		bombermanDiesToBombSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanExplode.png",33,33);
		bombermanDiesToMonsterSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanDies.png",36,37);
		bombermanDiesToBombAnimation = new Animation(bombermanDiesToBombSheet,280);
		bombermanDiesToMonsterAnimation = new Animation(bombermanDiesToMonsterSheet,180);
		bombermanDiesToBombAnimation.stopAt(7);
		bombermanDiesToMonsterAnimation.stopAt(10);
	}

	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public int getLives() {
		return lives;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public int getnBombs() {
		return nBombs;
	}
	public void setnBombs(int nBombs) {
		this.nBombs = nBombs;
	}
	public int getBombRange() {
		return bombRange;
	}
	public void setBombRange(int bombRange) {
		this.bombRange = bombRange;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public Animation getAnimation() {
		return currentAnimation;
	}
	public void setAnimation(Animation animation) {
		this.currentAnimation = animation;
	}

	public List<Bomb> getBombs() {
		return bombs;
	}

	public void setBombs(List<Bomb> bombs) {
		this.bombs = bombs;
	}

	public boolean[][] getBombsActivated() {
		return bombsActivated;
	}

	public void setBombsActivated(int i,int j,boolean value) {
		this.bombsActivated[i][j] = value;
	}



}
