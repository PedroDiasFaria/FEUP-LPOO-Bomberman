package bomberman.engine;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


/**
 * Class responsible for creating and handling all the pink bomberman elements and events.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class PinkBomberman extends Bomberman{

	public PinkBomberman(float initialX, float initialY, int lives,int bombRange, float speed, Level level) throws SlickException {
		super(initialX, initialY, lives, bombRange, speed, level);
	}
	
	
	/**
	 * Function responsible for the movement of the pink bomberman.
	 * @param gc GameContainer where the game is being displayed
	 * @param delta Number of milliseconds between frames
	 * @param level Level where the bomberman is to be moved
	 * @throws SlickException
	 */
	public void move(GameContainer gc,int delta,Level level) throws SlickException{

		Input input = gc.getInput();

		if(this.isAlive()){
			if(input.isKeyDown(Input.KEY_L)){
				this.setAnimation(bombermanRightAnimation);
				if(!checkCollisionsSide((this.getX() + delta* this.getSpeed())+bombermanSpriteWidth,this.getY(),level)){
					this.getAnimation().update(delta);
					this.setX(this.getX() + delta * this.getSpeed());
				}
			}else
				if(input.isKeyDown(Input.KEY_J)){
					this.setAnimation(bombermanLeftAnimation);
					if(!checkCollisionsSide(this.getX() - delta*this.getSpeed(),this.getY(),level)){
						this.getAnimation().update(delta);
						this.setX(this.getX() - delta *this.getSpeed());
					}
				}else
					if(input.isKeyDown(Input.KEY_K)){
						this.setAnimation(bombermanFrontAnimation);
						if(!checkCollisionsUpDown(this.getX(),(this.getY() + delta*this.getSpeed())+bombermanSpriteHeight,level)){
							if(this.getAnimation() == null)
								System.out.println("NULO");
							this.getAnimation().update(delta);
							this.setY(this.getY() + delta * this.getSpeed());
						}
					}else
						if(input.isKeyDown(Input.KEY_I)){
							this.setAnimation(bombermanBackAnimation);
							if(!checkCollisionsUpDown(this.getX(),this.getY() - delta*this.getSpeed()+10,level)){
								this.getAnimation().update(delta);
								this.setY(this.getY() - delta * this.getSpeed());
							}
						}else{
							bombermanFrontAnimation.setCurrentFrame(0);
							this.setAnimation(bombermanFrontAnimation);
						}

			if(input.isKeyDown(Input.KEY_O)){
				for(int b=0;b<bombs.size();b++){
					if(bombs.get(b).getBombFlag() == 1){
						if(bombs.get(b).getBombAvailableFlag() == 0){
							bombs.get(b).setBombFlag(0);
						}
					}
					else{
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
			checksForPowerUp(level);
		}
	}
	
	/**
	 * Function responsible for loading the pink bomberman animations
	 * @throws SlickException
	 */
	public void loadBombermanAnimations() throws SlickException{
		//Bomberman Movement Animations
		bombermanFrontSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanFront2.png",28,36);
		bombermanFrontAnimation = new Animation(bombermanFrontSheet,300);	
		bombermanBackSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanBack2.png",28,36);
		bombermanBackAnimation = new Animation(bombermanBackSheet,300);
		bombermanRightSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanRight2.png",28,36);
		bombermanRightAnimation = new Animation(bombermanRightSheet,300);
		bombermanLeftSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanLeft2.png",28,36);
		bombermanLeftAnimation = new Animation(bombermanLeftSheet,300);
		bombermanFrontAnimation.setAutoUpdate(false);
		bombermanBackAnimation.setAutoUpdate(false);
		bombermanRightAnimation.setAutoUpdate(false);
		bombermanLeftAnimation.setAutoUpdate(false);

		//Bomberman Deads Animations
		bombermanDiesToBombSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanExplode2.png",33,33);
		bombermanDiesToMonsterSheet = new SpriteSheet("gameResources/Sprites/Bomberman/BombermanDies2.png",36,37);
		bombermanDiesToBombAnimation = new Animation(bombermanDiesToBombSheet,280);
		bombermanDiesToMonsterAnimation = new Animation(bombermanDiesToMonsterSheet,180);
		bombermanDiesToBombAnimation.stopAt(7);
		bombermanDiesToMonsterAnimation.stopAt(10);
	}

}
