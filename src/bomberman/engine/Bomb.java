package bomberman.engine;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Class responsible for creating and handling all the bomb elements and events.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class Bomb {

	//Position of the bomb
	private float x;
	private float y;
	
	//Bomb Animation
	private Animation animation;
	private SpriteSheet bombSheet;
	private Animation bombAnimation;
	
	//Explosion Sprites
	private SpriteSheet centerExplosionSheet,midExplosionSheet,endExplosionSheet;
	private Animation centerExplosionAnimation,midExplosionAnimation,endExplosionAnimation;
	private int range;
	
	//Flag to see if the bomb was put
	private int bombFlag = 0;
	//Flag to see if the bomb is explosing
	private int explodeBombFlag = 0;
	//Flag to see if the bom is available
	private int bombAvailableFlag = 0;

	/**
	 * Constructor of this class.Function responsible for loading the animations of the bomb and initializing its range.
	 * @param range Range of the bomb
	 * @throws SlickException
	 */
	public Bomb(int range) throws SlickException{
		this.loadBombAnimations();
		this.animation = bombAnimation;
		this.range = range;
	}

	/**
	 * Function responsible for exploding a bomb.
	 * @param level Level where the bombs is to explode
	 */
	public void explode(Level level) {
		
		//draws the center of the explosion
		getCenterExplosionAnimation().draw(this.x-8,this.y-8);
		level.setExplosionsLocation((int) ((this.x-8)/32),(int) ((this.y-8)/32), true);
		level.setCollisions((int) (this.x/32),(int) (this.y/32), false);
		//counter to be used for checking the range of the explosion
		int i=1;

		//draws the explosion to the right
		do{
			if((!level.getDestructibles((int) ((this.getX()-8)+i*32)/32,(int) (this.getY()-8)/32))){
				if(!level.getCollisions((int) ((this.getX()-8)+i*32)/32,(int) (this.getY()-8)/32)){
					if(i == this.getRange()-1){
						this.getEndExplosionAnimation().getCurrentFrame().setRotation(0);
						this.getEndExplosionAnimation().draw((this.getX()-8)+i*32,(int) (this.getY()-8));
					}else{
						this.getMidExplosionAnimation().getCurrentFrame().setRotation(0);
						this.getMidExplosionAnimation().draw((this.getX()-8)+i*32,(int) (this.getY()-8));
					}
					level.setExplosionsLocation((int) (((this.getX()-8)+i*32)/32),(int) ((this.getY()-8)/32),true);
				}else{
					i = this.getRange()-1;
				}
			}else{
				level.getWallDestructionAnimation().draw((this.getX()-8)+i*32,(int) (this.getY()-8));
				level.setScore(level.getScore()+0.05);
				//updates the collision and destructibles matrix
				level.setCollisions((int) ((this.getX()-8)+i*32)/32,(int) (this.getY()-8)/32,false);
				level.setExplosionsLocation((int) (((this.getX()-8)+i*32)/32),(int) ((this.getY()-8)/32),true);
				//condition to end the cycle
				i = this.getRange()-1;
			}
			i++;
		}while(i != this.getRange());

		i=1;
		//draws the explosion up
		do{
			if(!level.getDestructibles((int) (this.getX()-8)/32,(int) ((this.getY()-8)-i*32)/32)){
				if(!level.getCollisions((int) (this.getX()-8)/32,(int) ((this.getY()-8)-i*32)/32)){
					if(i == this.getRange()-1){
						this.getEndExplosionAnimation().getCurrentFrame().setRotation(-90);
						this.getEndExplosionAnimation().draw((this.getX()-8),(int) (this.getY()-8)-i*32);
					}else{
						this.getMidExplosionAnimation().getCurrentFrame().setRotation(-90);
						this.getMidExplosionAnimation().draw((this.getX()-8),(int) (this.getY()-8)-i*32);
					}
					level.setExplosionsLocation((int) ((this.getX()-8)/32),(int) (((this.getY()-8)-i*32)/32), true);
				}else{
					i = this.getRange()-1;
				}
			}else{
				level.getWallDestructionAnimation().draw((this.getX()-8),(int) (this.getY()-8)-i*32);
				level.setScore(level.getScore()+0.05);
				//updates the collision matrix
				level.setCollisions((int) (this.getX()-8)/32,(int) ((this.getY()-8)-i*32)/32,false);
				level.setExplosionsLocation((int) ((this.getX()-8)/32),(int) (((this.getY()-8)-i*32)/32),true);
				//condition to end the cycle
				i = this.getRange()-1;
			}
			i++;
		}while(i != this.getRange());

		i=1;
		//draws the explosion to the left
		do{
			if(!level.getDestructibles((int) ((this.getX()-8)-i*32)/32,(int) (this.getY()-8)/32)){
				if(!level.getCollisions((int) ((this.getX()-8)-i*32)/32,(int) (this.getY()-8)/32)){
					if(i == this.getRange()-1){
						this.getEndExplosionAnimation().getCurrentFrame().setRotation(-180);
						this.getEndExplosionAnimation().draw((this.getX()-8-i*32),(int) (this.getY()-8));
					}else{
						this.getMidExplosionAnimation().getCurrentFrame().setRotation(-180);
						this.getMidExplosionAnimation().draw((this.getX()-8-i*32),(int) (this.getY()-8));
					}
					level.setExplosionsLocation((int) (((this.getX()-8)-i*32)/32),(int) ((this.getY()-8)/32),true);
				}else{
					i = this.getRange()-1;
				}
			}else{
				level.getWallDestructionAnimation().draw((this.getX()-8-i*32),(int) (this.getY()-8));
				level.setScore(level.getScore()+0.05);
				//updates the collision matrix
				level.setCollisions((int) ((this.getX()-8)-i*32)/32,(int) (this.getY()-8)/32,false);
				level.setExplosionsLocation((int) (((this.getX()-8)-i*32)/32),(int) ((this.getY()-8)/32),true);
				//condition to end the cycle
				i = this.getRange()-1;
			}
			i++;
		}while(i != this.getRange());

		i=1;
		//draws the explosion down
		do{
			if(!level.getDestructibles((int) (this.getX()-8)/32,(int) ((this.getY()-8)+i*32)/32)){
				if(!level.getCollisions((int) (this.getX()-8)/32,(int) ((this.getY()-8)+i*32)/32)){
					if(i == this.getRange()-1){
						this.getEndExplosionAnimation().getCurrentFrame().setRotation(-270);
						this.getEndExplosionAnimation().draw((this.getX()-8),(int) (this.getY()-8+i*32));
					}else{
						this.getMidExplosionAnimation().getCurrentFrame().setRotation(-270);
						this.getMidExplosionAnimation().draw((this.getX()-8),(int) (this.getY()-8+i*32));
					}
					level.setExplosionsLocation((int) (this.getX()-8)/32,(int) ((this.getY()-8)+i*32)/32,true);
				}else{
					i = this.getRange()-1;
				}
			}else{
				level.getWallDestructionAnimation().draw((this.getX()-8),(int) (this.getY()-8+i*32));
				level.setScore(level.getScore()+0.05);
				//updates the collision matrix
				level.setCollisions((int) (this.getX()-8)/32,(int) ((this.getY()-8)+i*32)/32,false);
				level.setExplosionsLocation((int) (this.getX()-8)/32,(int) ((this.getY()-8)+i*32)/32,true);
				//condition to end the cycle
				i = this.getRange()-1;
			}
			i++;
		}while(i != this.getRange());
	}
	
	/**
	 * Function responsible for loading the bomb animations.
	 * @throws SlickException
	 */
	public void loadBombAnimations() throws SlickException{
		//Bomb Animation
		this.bombSheet = new SpriteSheet("gameResources/Sprites/bombs2.png",16,16);
		this.bombAnimation = new Animation(bombSheet,188);
		this.centerExplosionSheet = new SpriteSheet("gameResources/Sprites/CenterExplosion.png",32,32);
		this.centerExplosionAnimation = new Animation(centerExplosionSheet,125);
		this.midExplosionSheet = new SpriteSheet("gameResources/Sprites/MidExplosion.png",32,32);
		this.midExplosionAnimation = new Animation(midExplosionSheet,125);
		this.endExplosionSheet = new SpriteSheet("gameResources/Sprites/EndExplosion.png",32,32);
		this.endExplosionAnimation = new Animation(endExplosionSheet,125);
		this.centerExplosionAnimation.stopAt(6);
		this.bombAnimation.stopAt(13);
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

	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}

	public Animation getBombAnimation() {
		return bombAnimation;
	}

	public void setBombAnimation(Animation bombAnimation) {
		this.bombAnimation = bombAnimation;
	}

	public Animation getCenterExplosionAnimation() {
		return centerExplosionAnimation;
	}

	public void setCenterExplosionAnimation(Animation centerExplosionAnimation) {
		this.centerExplosionAnimation = centerExplosionAnimation;
	}

	public Animation getMidExplosionAnimation() {
		return midExplosionAnimation;
	}

	public void setMidExplosionAnimation(Animation midExplosionAnimation) {
		this.midExplosionAnimation = midExplosionAnimation;
	}

	public Animation getEndExplosionAnimation() {
		return endExplosionAnimation;
	}

	public void setEndExplosionAnimation(Animation endExplosionAnimation) {
		this.endExplosionAnimation = endExplosionAnimation;
	}

	public int getBombFlag() {
		return bombFlag;
	}

	public void setBombFlag(int bombFlag) {
		this.bombFlag = bombFlag;
	}

	public int getExplodeBombFlag() {
		return explodeBombFlag;
	}

	public void setExplodeBombFlag(int explodeBombFlag) {
		this.explodeBombFlag = explodeBombFlag;
	}

	public int getBombAvailableFlag() {
		return bombAvailableFlag;
	}

	public void setBombAvailableFlag(int bombAvailableFlag) {
		this.bombAvailableFlag = bombAvailableFlag;
	}
	
	
}
