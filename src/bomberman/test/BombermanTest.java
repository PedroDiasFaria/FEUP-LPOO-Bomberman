package bomberman.test;
import bomberman.gui.*;

import static org.junit.Assert.*;

import org.junit.Test;
import org.newdawn.slick.SlickException;

//Because we are using the slick2D framework to do our game its difficult to do automatic Junit tests like we were asked
//So, instead of writing the code for the junit tests, we are commenting what was the primary function of each test

public class BombermanTest{

	@Test
	public void testFreeMovement(){
		//This test is supposed to test the movement of the bomberman to a free space on the map
		//If the bomberman finds a free space he can move to that free space
	}
	
	@Test
	public void testMovementAgainstWall(){
		//This test is supposed to test the movement of the bomberman against a wall
		//If the bomberman encounters a wall, it isnt supposed to move
	}

	@Test 
	public void testPuttingBomb(){
		//This test is supposed to test if the bomberman puts a bomb when the space bar is pressed
	}
	
	@Test
	public void testPuttingMultipleBombs(){
		//This test is supposed to test if the bomberman puts multiple bombs when the space bar is pressed
		//He can only puts as much bombs as its max bomb variable
	}
	
	@Test
	public void testBombermanLosesALifeToAMonster(){
		//This test is supposed to test if the bomberman loses a life when he and a monster are in the same house of the map
	}
	
	@Test
	public void testBombermanLosesALifeToABomb(){
		//This test is supposed to test if the bomberman loses a life to a bomb explosion
	}
	
	@Test
	public void testBombermanDies(){
		//This test is supposed to test if the bomberman dies after losing all its lives
	}
	
	@Test
	public void testBombermanExitsWithMonstersAlive(){
		//This test is supposed to test if the bomberman can exit the level with at least a monster alive
		//He can only exit a level when all the monsters are killed
	}
	
	@Test
	public void testBombermanExitsWithMonstersDead(){
		//This test is supposed to test if the bomberman can exit the level when all monsters are dead
	}
	
	@Test
	public void testBombermanCatchesSkatePowerUp(){
		//This test is supposed to test if after catching the skate powerUp the bomberman increases its speed by 0.05
	}
	
	@Test
	public void testBombermanCatchesFirePowerUp(){
		//This test is supposed to test if after catching the fire powerUp the bomberman increases its bomb range by 1
	}
	
	@Test
	public void testBombermanCatchesBombPowerUp(){
		//This test is supposed to test if catching the bomb powerUp the bomberman increases its bomb number by 1
	}
	
	@Test 
	public void testBombermanCatchesSkullPowerUp(){
		//This test is supposed to test if after catching the skull powerUp the bomberman loses a life/decreases the bomb range by 1/decreases the bomb number by 1//decreases the speed by 0.05
	}
	
	
}
