package bomberman.test;

import static org.junit.Assert.*;

import org.junit.Test;

//Because we are using the slick2D framework to do our game its difficult to do automatic Junit tests like we were asked
//So, instead of writing the code for the junit tests, we are commenting what was the primary function of each test

public class MonsterTest {

	@Test
	public void testFreeMonsterMovement() {
		//This test is supposed to test the movement of a monster to a free space on the map
		//If the monster finds a free space he can move to that free space
	}
	
	@Test
	public void testMovementAgainstWall(){
		//This test is supposed to test the movement of the monster against a wall
		//If the monster encounters a wall, it isnt supposed to move
	}
	
	@Test
	public void testMonsterKillsBomberman(){
		//This test is supposed to test if the monster kills the bomberman when they are in the same house of the map
	}
	
	@Test
	public void testMonsterDies(){
		//This test is supposed to test if the monster dies to a bomb when its catched in its range
	}
	
	@Test
	public void testMultipleMonsters(){
		//This test is supposed to test the existence of multiple monsters on the map
	}

}
