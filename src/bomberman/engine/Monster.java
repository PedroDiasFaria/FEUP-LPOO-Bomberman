package bomberman.engine;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Class responsible for creating and handling all the monsters elements and events.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class Monster {

	//Position of the monster
	private float x;
	private float y;
	
	//Speed of the monster
	private float speed;
	
	//Values used to calculate the enemy movement
	private char direction = 'S';
	private float enemyMovementQuant = 0;
	private float oldMonsterX;
	private float oldMonsterY;
	
	//Size of the monster sprite
	private int enemySpriteWidth = 	32;
	private int enemySpriteHeight = 32;
	
	//Flag to see if is alive
	private boolean alive = true;
	
	//Type of the monster
	private int type;

	//Enemy Animation
	private SpriteSheet enemyFrontSheet,enemyBackSheet,enemyRightSheet,enemyLeftSheet;
	private Animation animation,enemyFrontAnimation,enemyBackAnimation,enemyRightAnimation,enemyLeftAnimation;

	/**
	 * Constructor of this class.Function responsible for creating the monster.It initializes the monster animations,is current position
	 * ,type and speed.
	 * @param initialX X of the initial position
	 * @param initialY Y of the initial position
	 * @param speed Initial speed of the monster
	 * @param monsterType Monster type
	 * @throws SlickException
	 */
	public Monster(float initialX,float initialY,float speed,int monsterType) throws SlickException{
		this.type = monsterType;
		this.x = initialX;
		this.y = initialY;
		this.oldMonsterX = initialX;
		this.oldMonsterY = initialY;
		this.speed = speed;
		this.loadMonsterAnimations();
		this.animation = enemyFrontAnimation;
	}

	/**
	 * Function responsible for the movement of the monster
	 * @param delta Number of milliseconds between frames
	 * @param level Level where the monster is to be moved
	 */
	public void move(int delta,Level level){
		//System.out.println("X: " + (int)this.x/32 + " || Y: " + (int)this.y/32);
		if(this.isAlive()){
			if((enemyMovementQuant >= 32)){

				switch(this.getDirection()){
				case 'S':
					this.setY(oldMonsterY +32);
					oldMonsterY = this.getY();
					break;
				case 'N':
					this.setY(oldMonsterY -32);
					oldMonsterY = this.getY();
					break;
				case 'E':
					this.setX(oldMonsterX +32);
					oldMonsterX = this.getX();
					break;
				case 'W':
					this.setX(oldMonsterX -32);
					oldMonsterX = this.getX();
					break;
				}
				moveMonster(delta,level);
				enemyMovementQuant = 0;
			}

			enemyMovementQuant +=delta*this.getSpeed();

			if(this.getDirection() == 'S'){
				//if(!checkCollisionsUpDown(this.getX(),this.getY()+delta*this.getSpeed()+enemySpriteHeight)){ //front
				this.setAnimation(enemyFrontAnimation);
				this.setY(this.getY() + delta*this.getSpeed());
			}else
				//if(!checkCollisionsUpDown(this.getX(),this.getY()-delta*this.getSpeed()+10)){ //down
				if(this.getDirection() == 'N'){
					this.setAnimation(enemyBackAnimation);
					this.setY(this.getY() - delta *this.getSpeed());
				}else
					//if(!checkCollisionsSide(this.getX()+delta*this.getSpeed()+enemySpriteWidth,this.getY())){ //right
					if(this.getDirection() == 'E'){
						this.setAnimation(enemyRightAnimation);
						this.setX(this.getX() + delta*this.getSpeed());
					}else
						//if(!checkCollisionsSide(this.getX()-delta*this.getSpeed(),this.getY())){ //left
						if(this.getDirection() == 'W'){
							this.setAnimation(enemyLeftAnimation);
							this.setX(this.getX() - delta *this.getSpeed());
						}
		}
	}

	/**
	 * Function responsible for calculating the random monster movement.
	 * @param delta Number of milliseconds between frames
	 * @param level Level where the monster is to be moved
	 */
	public void moveMonster(int delta,Level level){

		int index = 0;
		List<Character> dirArray = new ArrayList<Character>();

		switch(this.getDirection()){
		case 'S':

			if(!checkMonsterCollisionsSide(this.getX()-delta*this.getSpeed(),this.getY(),level)){ //left
				dirArray.add('W');
				index ++;
			}

			if(!checkMonsterCollisionsSide(this.getX()+delta*this.getSpeed()+enemySpriteWidth,this.getY(),level)){ //right
				dirArray.add('E');
				index ++;
			}
			if(!checkMonsterCollisionsUpDown(this.getX(),this.getY()+delta*this.getSpeed()+enemySpriteHeight,level)){ //front
				dirArray.add('S');
				index ++;
			}

			if(index == 0){
				this.setDirection('N');
				//put movement north
			}
			else{
				Random randomGenerator = new Random();
				int randomIndex = randomGenerator.nextInt(index);
				this.setDirection(dirArray.get(randomIndex));
			}	


			break;

		case 'N':

			//check colision with left
			if(!checkMonsterCollisionsSide(this.getX()-delta*this.getSpeed(),this.getY(),level)){ //left
				dirArray.add('W');
				index ++;
			}
			if(!checkMonsterCollisionsSide(this.getX()+delta*this.getSpeed()+enemySpriteWidth,this.getY(),level)){ //right
				dirArray.add('E');
				index ++;
			}

			if(!checkMonsterCollisionsUpDown(this.getX(),this.getY()-delta*this.getSpeed(),level)){ //down
				dirArray.add('N');
				index ++;
			}

			if(index == 0){
				this.setDirection('S');
				//put movement south
			}
			else{
				Random randomGenerator = new Random();
				int randomIndex = randomGenerator.nextInt(index);
				this.setDirection(dirArray.get(randomIndex));
			}	

			break;

		case 'W':

			//check colision with left
			if(!checkMonsterCollisionsSide(this.getX()-delta*this.getSpeed(),this.getY(),level)){ //left
				dirArray.add('W');
				index ++;
			}

			if(!checkMonsterCollisionsUpDown(this.getX(),this.getY()+delta*this.getSpeed()+enemySpriteHeight,level)){ //front
				dirArray.add('S');
				index ++;
			}

			if(!checkMonsterCollisionsUpDown(this.getX(),this.getY()-delta*this.getSpeed(),level)){ //down
				dirArray.add('N');
				index ++;
			}

			if(index == 0){
				this.setDirection('E');
				//put movement right
			}
			else{
				Random randomGenerator = new Random();
				int randomIndex = randomGenerator.nextInt(index);
				this.setDirection(dirArray.get(randomIndex));
			}	


			break;

		case 'E':

			if(!checkMonsterCollisionsSide(this.getX()+delta*this.getSpeed()+enemySpriteWidth,this.getY(),level)){ //right
				dirArray.add('E');
				index ++;
			}

			if(!checkMonsterCollisionsUpDown(this.getX(),this.getY()+delta*this.getSpeed()+enemySpriteHeight,level)){ //front
				dirArray.add('S');
				index ++;
			}

			if(!checkMonsterCollisionsUpDown(this.getX(),this.getY()-delta*this.getSpeed(),level)){ //down
				dirArray.add('N');
				index ++;
			}

			if(index == 0){
				this.setDirection('W');
				//put movement left
			}
			else{
				Random randomGenerator = new Random();
				int randomIndex = randomGenerator.nextInt(index);
				this.setDirection(dirArray.get(randomIndex));
			}	

			break;

		}
	}
	/**
	 * Function responsible for checking the value of the pixel of the level on the collisions matrix.
	 * @param x X of the pixel to be checked
	 * @param y Y of the pixel to be checked
	 * @param level Level where the pixel is
	 * @return The value of the collision matrix.True if it is to be collided or false otherwise
	 */
	public boolean isBlocked(float x,float y,Level level){
		int xBlock = (int)x / 32;
		int yBlock = (int)y / 32;
		return level.getCollisions(xBlock, yBlock);
	}

	/**
	 * Checks for collisions left and right.It checks for the monster sprite height if that sprite intersects a blocked element of the level.
	 * @param x X of the position of the monster
	 * @param y Y of the position of the monster
	 * @param level Level where the monster is
	 * @return true if it is possible to move or false if it isn't
	 */
	public boolean checkMonsterCollisionsSide(float x,float y,Level level){
		for(int i=0;i<enemySpriteHeight;i++){
			if(isBlocked(x,y+i,level))
				return true;
		}
		return false;
	}

	/**
	 * Checks for collisions up and down.It checks for the monster sprite width if any pixel intersects a blocked element of the level.
	 * @param x X of the position of the monster
	 * @param y Y of the position of the monster
	 * @param level Level where the monster is
	 * @return true if it is possible to move or false if it isn't
	 */
	public boolean checkMonsterCollisionsUpDown(float x,float y,Level level){
		for(int i=0;i<enemySpriteWidth;i++){
			if(isBlocked(x+i,y,level))
				return true;
		}
		return false;
	}

	/**
	 * Function responsible for checking the alive status of the monster.It checks if he has died to a bomb.
	 * @param level Level where the monster is
	 */
	public void checkIfIsAlive(Level level){
		for(int i=0;i<level.getMap().getWidth();i++){
			for(int j=0;j<level.getMap().getHeight();j++){
				if(level.getExplosionsLocation(i,j)){
					//System.out.println("i: " + i + "|| j: " + j);
					if((((int)(this.getX()))/32 == i) && (((int)this.getY())/32 == j)){
						level.setScore(level.getScore()+400);
						this.setAlive(false);
						this.setX(0);
						this.setY(0);
					}
				}
			}
		}
	}

	/**
	 * Function responsible for loading tha animations of the monster.It loads the animations depending on the monsterType.
	 * @throws SlickException
	 */
	public void loadMonsterAnimations() throws SlickException{
		//Enemy Animation

		switch(this.type){
		case 1:	enemyFrontSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyFront1.png",32,35);
				enemyFrontAnimation = new Animation(enemyFrontSheet,300);
				enemyRightSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyRight1.png",32,35);
				enemyRightAnimation = new Animation(enemyRightSheet,300);
				enemyBackSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyBack1.png",32,35);
				enemyBackAnimation = new Animation(enemyBackSheet,300);
				enemyLeftSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyLeft1.png",32,35);
				enemyLeftAnimation = new Animation(enemyLeftSheet,300);
				break;

		case 2: enemyFrontSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyFront2.png",32,32);
		 		enemyFrontAnimation = new Animation(enemyFrontSheet,300);
		 		enemyRightSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyRight2.png",32,32);
		 		enemyRightAnimation = new Animation(enemyRightSheet,300);
		 		enemyBackSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyBack2.png",32,32);
		 		enemyBackAnimation = new Animation(enemyBackSheet,300);
		 		enemyLeftSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyLeft2.png",32,32);
		 		enemyLeftAnimation = new Animation(enemyLeftSheet,300);
		 		enemyFrontAnimation.setPingPong(true);
		 		enemyBackAnimation.setPingPong(true);
		 		enemyRightAnimation.setPingPong(true);
		 		enemyLeftAnimation.setPingPong(true);
		 		break;
		 		
		case 3: enemyFrontSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyFront3.png",32,32);
				enemyFrontAnimation = new Animation(enemyFrontSheet,300);
				enemyRightSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyRight3.png",32,32);
				enemyRightAnimation = new Animation(enemyRightSheet,300);
				enemyBackSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyBack3.png",32,32);
				enemyBackAnimation = new Animation(enemyBackSheet,300);
				enemyLeftSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyLeft3.png",32,32);
				enemyLeftAnimation = new Animation(enemyLeftSheet,300);
				this.speed = 0.05f;
				break;
				
		case 4: enemyFrontSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyFront4.png",32,32);
				enemyFrontAnimation = new Animation(enemyFrontSheet,300);
				enemyRightSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyRight4.png",32,32);
				enemyRightAnimation = new Animation(enemyRightSheet,300);
				enemyBackSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyBack4.png",32,32);
				enemyBackAnimation = new Animation(enemyBackSheet,300);
				enemyLeftSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyLeft4.png",32,32);
				enemyLeftAnimation = new Animation(enemyLeftSheet,300);
				break;
				
		case 5: enemyFrontSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyFront5.png",32,40);
				enemyFrontAnimation = new Animation(enemyFrontSheet,300);
				enemyRightSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyRight5.png",32,40);
				enemyRightAnimation = new Animation(enemyRightSheet,300);
				enemyBackSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyBack5.png",32,40);
				enemyBackAnimation = new Animation(enemyBackSheet,300);
				enemyLeftSheet = new SpriteSheet("gameResources/Sprites/Enemies/EnemyLeft5.png",32,40);
				enemyLeftAnimation = new Animation(enemyLeftSheet,300);
				break;
				
		}

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
	public Animation getAnimation() {
		return animation;
	}
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public char getDirection() {
		return direction;
	}
	public void setDirection(char direction) {
		this.direction = direction;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public float getOldMonsterX() {
		return oldMonsterX;
	}

	public void setOldMonsterX(float oldMonsterX) {
		this.oldMonsterX = oldMonsterX;
	}

	public float getOldMonsterY() {
		return oldMonsterY;
	}

	public void setOldMonsterY(float oldMonsterY) {
		this.oldMonsterY = oldMonsterY;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}



}

