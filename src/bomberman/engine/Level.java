package bomberman.engine;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Class responsible for creating and handling the level elements.It is responsible for loading the information from the tmx files
 * to logic structures and for the animations of the level like the game panel on the top of the game window or the walls and the floor.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */
public class Level {

	//Map of the level
	private TiledMap map;

	//ID of the level
	private int ID;

	//Wall Destruction Animation
	private SpriteSheet wallDestructionSheet;
	private Animation wallDestructionAnimation;
	
	//Win and defeat images
	private Image victoryImage,defeatImage;
	private SpriteSheet victorySpritesheet;
	private Animation victoryAnimation;

	//Floor Sprite of the level
	private Image floor;

	//Exit Sprite of the level
	private Image exit;

	//PowerUp Images
	private Image firePowerUp,bombPowerUp,speedPowerUp,skullPowerUp,lifePowerUp;
	
	//Level Panel Image
	private Image levelPanel;
	
	//Number sprites for the level Panel
	private Image n0,n1,n2,n3,n4,n5,n6,n7,n8,n9;

	//Coordinates of the exit
	private int exitX;
	private int exitY;
	//Flag to see where the exit is drawn after a explosion
	private int exitFlag = 0;
	//Flag to see if the bomberman has reached the exit
	private boolean endOfLevel = false;

	//collision matrix
	private boolean[][] collisions;
	//Destructible objects matrix
	private boolean[][] destructibles;
	//Matrix to know where bomb explosions ocurred
	private boolean[][] explosionsLocation;
	//Matrix to know where to re-draw the floor
	private boolean[][] floorLocations;
	//Matrix to know where the power-up are
	private PowerUp[][] powerUps;

	//Bomberman
	private Bomberman bomberman;

	//Array of monsters of this level
	List<Monster> monsters = new ArrayList<Monster>();
	//Number of monsters on the level
	private int nMonsters = 0;

	//Initial coordinates of the bomberman
	private float bombermanInitialX = 32f;
	private float bombermanInitialY = 152f;
	//Initial coordinates of the enemy Monster
	private float enemyX = 0;//256f;
	private float enemyY = 0;//224f;
	
	//Score of the level
	private double score;

	/**
	 * Constructor of this class.Function responsible for the initialization of the primary level values.It loads the tmx file of the current level,
	 * the score of the game and the animations that are going to be used on the level.It creates the bomberman and loads the 
	 * parameters of the tmx files to logic structures.
	 * 
	 * @param string Name of the map (tmx file)
	 * @param ID	Number that identifies this level
	 * @param score Number that represents the actual score at the begining of the level
	 * @throws SlickException
	 */
	public Level(String string,int ID,double score) throws SlickException{
		this.map = new TiledMap(string);
		this.ID = ID;
		this.score = score;
		this.loadAnimations();
		bomberman = new Bomberman(bombermanInitialX,bombermanInitialY,1,1,0.15f,this);
		this.loadCollisionsNDestructiblesNPowerUps();
	}

	/**
	 * Loads the animations that are going to be used on the level
	 * @throws SlickException
	 */
	public void loadAnimations() throws SlickException{

		//Wall Destruction Animation
		wallDestructionSheet = new SpriteSheet("gameResources/Sprites/wallDestruction.png",32,32);
		wallDestructionAnimation = new Animation(wallDestructionSheet,167);
		wallDestructionAnimation.stopAt(5);

		//Floor image
		String floorSprite = "gameResources/Level".concat(Integer.toString(ID));
		floorSprite = floorSprite.concat("/Floor.png");
		floor = new Image(floorSprite);

		//Exit image
		exit = new Image("gameResources/Sprites/Exit.png");

		//PowerUp images
		firePowerUp = new Image("gameResources/Sprites/PowerUps/Fire.png");
		bombPowerUp = new Image("gameResources/Sprites/PowerUps/bomb.png");
		skullPowerUp = new Image("gameResources/Sprites/PowerUps/Skull.png");
		lifePowerUp = new Image("gameResources/Sprites/PowerUps/Heart.png");
		speedPowerUp = new Image("gameResources/Sprites/PowerUps/Speed.png");
		
		//LevelPanel image
		levelPanel = new Image("gameResources/Sprites/Menus/gamepanel.png");
		
		//Number images
		n0 = new Image("gameResources/Sprites/Numbers/0.png");
		n1 = new Image("gameResources/Sprites/Numbers/1.png");
		n2 = new Image("gameResources/Sprites/Numbers/2.png");
		n3 = new Image("gameResources/Sprites/Numbers/3.png");
		n4 = new Image("gameResources/Sprites/Numbers/4.png");
		n5 = new Image("gameResources/Sprites/Numbers/5.png");
		n6 = new Image("gameResources/Sprites/Numbers/6.png");
		n7 = new Image("gameResources/Sprites/Numbers/7.png");
		n8 = new Image("gameResources/Sprites/Numbers/8.png");
		n9 = new Image("gameResources/Sprites/Numbers/9.png");
		
		//Win and defeat screens and Animation
		victoryImage = new Image("gameResources/Sprites/Menus/Victory.png");
		defeatImage = new Image("gameResources/Sprites/Menus/Defeat.png");
		victorySpritesheet = new SpriteSheet("gameResources/Sprites/Menus/Victory2.png",496,117);
		victoryAnimation = new Animation(victorySpritesheet,300);
	}

	/**
	 * Loads the tmx properties to logic structures
	 * @throws SlickException
	 */
	public void loadCollisionsNDestructiblesNPowerUps() throws SlickException{
		
		//Variable used to read the monster type of the tiled
		int monsterType = 0;
		
		//initiates the matrixes 
		collisions = new boolean[map.getWidth()][map.getHeight()];
		destructibles = new boolean[map.getWidth()][map.getHeight()];
		explosionsLocation = new boolean[map.getWidth()][map.getHeight()];
		floorLocations = new boolean[map.getWidth()][map.getHeight()];
		powerUps = new PowerUp[map.getWidth()][map.getHeight()];
		
		for(int i=0;i < map.getWidth();i++){
			for(int j=0;j < map.getHeight();j++){
				powerUps[i][j] = new PowerUp();
			}
		}

		//loop to read the tilemap
		for(int i=0;i < map.getWidth();i++){
			for(int j=0;j < map.getHeight();j++){
				int tileID = map.getTileId(i, j, 2);
				//reads the collision property of the walls tiles to the collision matrix
				String value = map.getTileProperty(tileID, "collision", "false");
				if("true".equals(value)){
					collisions[i][j] = true;
				}
				//reads the destructible property of the walls tile to the destructibles matrix
				value = map.getTileProperty(tileID, "destructible", "false");
				if("true".equals(value)){
					destructibles[i][j] = true;
				}
				//reads the exit property of the powerUps tile to the exit variables
				tileID = map.getTileId(i, j, 1);
				value = map.getTileProperty(tileID, "exit", "false");
				if("true".equals(value)){
					exitX = i;
					exitY = j;
				}
				//reads the powerUp property of the powerUps tile to the powerUps matrix
				value = map.getTileProperty(tileID, "powerUp", "false");
				if("true".equals(value)){
					powerUps[i][j].setExists(true);
				}
				//reads the powerUpType property of the powerUps tile to the powerUps matrix
				value = map.getTileProperty(tileID, "powerUpType", "0");
				powerUps[i][j].setType(Integer.parseInt(value));				

				tileID = map.getTileId(i,j,3);
				
				//reads the enemieType property the enemies tile to the powerUps matrix
				value = map.getTileProperty(tileID, "enemieType", "0");
				if(Integer.parseInt(value) != 0){
					monsterType = Integer.parseInt(value);
				}
				//reads the enemie property of the enemies tile to the powerUps matrix
				value = map.getTileProperty(tileID, "enemie", "false");
				if("true".equals(value)){
					monsters.add(new Monster(enemyX,enemyY,0.05f,monsterType));
					monsters.get(nMonsters).setX(i*32);
					monsters.get(nMonsters).setY(j*32);
					monsters.get(nMonsters).setOldMonsterX(i*32);
					monsters.get(nMonsters).setOldMonsterY(j*32);	
					nMonsters++;
				}

				//initiates the explosionsLocation matrix 
				explosionsLocation[i][j] = false;
				//initiates the floorLocations matrix 
				floorLocations[i][j] = false;
			}
		}
	}

	/**
	 * Function responsible for erasing the explosions on the level after a explosion occored
	 * @param b Number of the bomb which explosions are going to be erased
	 */
	public void eraseExplosions(int b){
		for(int i=0;i<this.getMap().getWidth();i++){
			for(int j=0;j<this.getMap().getHeight();j++){
				if(this.getExplosionsLocation(i,j) == true){
					this.setExplosionsLocation(i,j,false);
					//sets that position on the floor matrix
					this.setFloorLocations(i,j,true);
					//updates the destructibles matrix
					this.setDestructibles(i,j,false);
					if((i == exitX) && (j == exitY)){
						exitFlag = 1;
					}		
					if((powerUps[i][j].getExists()) == true){
						powerUps[i][j].setActivated(true);
					}
				}
			}
		}
		this.getBomberman().getBombs().get(b).setExplodeBombFlag(0);
		this.getBomberman().getBombs().get(b).setBombAvailableFlag(0);
	}

	/**
	 * Function responsible for re-drawing the floor after a explosion occurred
	 */
	public void drawFloor(){
		for(int i=0;i<this.getMap().getWidth();i++){
			for(int j=0;j<this.getMap().getHeight();j++){
				if(this.getFloorLocations(i,j) == true){
					this.getFloor().draw(i*32,j*32);
				}
			}
		}
	}
	/*
	public void detonateOtherBombs(Level level,int b){
		for(int i=0;i<level.getBomberman().getBombs().get(b).getRange()-2;i++){
			for(int j=0;j<level.getBomberman().getBombs().size();j++){
				if(j!=b){
					if((int)(level.getBomberman().getBombs().get(b).getX()+i*32)/32 == (int)(level.getBomberman().getBombs().get(j).getX())/32 || (int)(level.getBomberman().getBombs().get(b).getX()-i*32)/32 == (int)(level.getBomberman().getBombs().get(j).getX())/32 || (int)(level.getBomberman().getBombs().get(b).getY()-i*32)/32 == (int)(level.getBomberman().getBombs().get(j).getY())/32 || (int)(level.getBomberman().getBombs().get(b).getY()+i*32)/32 == (int)(level.getBomberman().getBombs().get(j).getY())/32){
						level.getBomberman().getBombs().get(j).setExplodeBombFlag(1);
					}
				}
			}
		}
	}*/

	/**
	 * Function responsible for checking if the bomberman is on top of the exit and has killed all monsters on the level
	 */
	public void checkIfEndOfLevel(){
		if(((((int)(bomberman.getX()))/32 == exitX) && (((int)(bomberman.getY()))/32 == exitY-1)) && (checkIfAllMonstersDead())){
			endOfLevel = true;
		}
	}
	
	/**
	 * Function responsible for checking if all the monsters are dead
	 * @return true if all monsters are dead or false otherwise
	 */
	public boolean checkIfAllMonstersDead(){
		for(int i=0;i<this.getMonsters().size();i++){
			if(this.getMonsters().get(i).isAlive() == true)
				return false;
		}
		
		return true;
	}

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public Animation getWallDestructionAnimation() {
		return wallDestructionAnimation;
	}

	public void setWallDestructionAnimation(Animation wallDestructionAnimation) {
		this.wallDestructionAnimation = wallDestructionAnimation;
	}

	public Image getFloor() {
		return floor;
	}

	public void setFloor(Image floor) {
		this.floor = floor;
	}

	public boolean getCollisions(int i,int j) {
		return collisions[i][j];
	}

	public void setCollisions(int i,int j,boolean value) {
		this.collisions[i][j] = value;
	}

	public boolean getDestructibles(int i,int j) {
		return destructibles[i][j];
	}

	public void setDestructibles(int i,int j,boolean value) {
		this.destructibles[i][j] = value;
	}

	public boolean getExplosionsLocation(int i,int j) {
		return explosionsLocation[i][j];
	}

	public void setExplosionsLocation(int i,int j,boolean value) {
		this.explosionsLocation[i][j] = value;
	}

	public boolean getFloorLocations(int i,int j) {
		return floorLocations[i][j];
	}

	public void setFloorLocations(int i,int j,boolean value) {
		this.floorLocations[i][j] = value;
	}

	public Image getExit() {
		return exit;
	}

	public void setExit(Image exit) {
		this.exit = exit;
	}

	public int getExitX() {
		return exitX;
	}

	public void setExitX(int exitX) {
		this.exitX = exitX;
	}

	public int getExitY() {
		return exitY;
	}

	public void setExitY(int exitY) {
		this.exitY = exitY;
	}

	public int getExitFlag() {
		return exitFlag;
	}

	public void setExitFlag(int exitFlag) {
		this.exitFlag = exitFlag;
	}

	public Bomberman getBomberman() {
		return bomberman;
	}

	public void setBomberman(Bomberman bomberman) {
		this.bomberman = bomberman;
	}

	public boolean getEndOfLevel() {
		return endOfLevel;
	}

	public void setEndOfLevel(boolean endOfLevel) {
		this.endOfLevel = endOfLevel;
	}

	public List<Monster> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<Monster> monsters) {
		this.monsters = monsters;
	}

	public PowerUp[][] getPowerUps() {
		return powerUps;
	}

	public void setPowerUps(PowerUp[][] powerUps) {
		this.powerUps = powerUps;
	}

	public Image getFirePowerUp() {
		return firePowerUp;
	}

	public void setFirePowerUp(Image firePowerUp) {
		this.firePowerUp = firePowerUp;
	}

	public Image getBombPowerUp() {
		return bombPowerUp;
	}

	public void setBombPowerUp(Image bombPowerUp) {
		this.bombPowerUp = bombPowerUp;
	}

	public Image getSpeedPowerUp() {
		return speedPowerUp;
	}

	public void setSpeedPowerUp(Image speedPowerUp) {
		this.speedPowerUp = speedPowerUp;
	}

	public Image getLifePowerUp() {
		return lifePowerUp;
	}

	public void setLifePowerUp(Image lifePowerUp) {
		this.lifePowerUp = lifePowerUp;
	}

	public Image getSkullPowerUp() {
		return skullPowerUp;
	}

	public void setSkullPowerUp(Image skullPowerUp) {
		this.skullPowerUp = skullPowerUp;
	}

	public Image getLevelBar() {
		return levelPanel;
	}

	public void setLevelBar(Image levelBar) {
		this.levelPanel = levelBar;
	}

	public Image getN0() {
		return n0;
	}

	public void setN0(Image n0) {
		this.n0 = n0;
	}

	public Image getN1() {
		return n1;
	}

	public void setN1(Image n1) {
		this.n1 = n1;
	}

	public Image getN2() {
		return n2;
	}

	public void setN2(Image n2) {
		this.n2 = n2;
	}

	public Image getN3() {
		return n3;
	}

	public void setN3(Image n3) {
		this.n3 = n3;
	}

	public Image getN4() {
		return n4;
	}

	public void setN4(Image n4) {
		this.n4 = n4;
	}

	public Image getN5() {
		return n5;
	}

	public void setN5(Image n5) {
		this.n5 = n5;
	}

	public Image getN6() {
		return n6;
	}

	public void setN6(Image n6) {
		this.n6 = n6;
	}

	public Image getN7() {
		return n7;
	}

	public void setN7(Image n7) {
		this.n7 = n7;
	}

	public Image getN8() {
		return n8;
	}

	public void setN8(Image n8) {
		this.n8 = n8;
	}

	public Image getN9() {
		return n9;
	}

	public void setN9(Image n9) {
		this.n9 = n9;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double d) {
		this.score = d;
	}

	public Image getVictoryImage() {
		return victoryImage;
	}

	public void setVictoryImage(Image victoryImage) {
		this.victoryImage = victoryImage;
	}

	public Image getDefeatImage() {
		return defeatImage;
	}

	public void setDefeatImage(Image defeatImage) {
		this.defeatImage = defeatImage;
	}

	public Animation getVictoryAnimation() {
		return victoryAnimation;
	}

	public void setVictoryAnimation(Animation victoryAnimation) {
		this.victoryAnimation = victoryAnimation;
	}

	
}
