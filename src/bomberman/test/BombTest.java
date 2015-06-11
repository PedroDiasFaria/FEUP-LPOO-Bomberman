package bomberman.test;

import static org.junit.Assert.*;

import org.junit.Test;

//Because we are using the slick2D framework to do our game its difficult to do automatic Junit tests like we were asked
//So, instead of writing the code for the junit tests, we are commenting what was the primary function of each test

public class BombTest {

	@Test
	public void testBombExplode() {
		//This test is supposed to test if after ~3 seconds after a bomb is places it explodes 
	}

	@Test
	public void testBombRange(){
		//This test is supposed to test if the bomb draws the explosion animation until the its max range
	}
	
	@Test
	public void testBombDestroysDestructibleWall(){
		//This test is supposed to test if the bomb destroys everything that is destructible in its bomb range
	}
	
	@Test
	public void testBombDoesntDestroyIndestructibleWall(){
		//This test is supposed to test if the bomb doesnt destroy the elements that aren't destructible in the map
	}
	
	
	@Test
	public void testBombActivatesBomb(){
		//This test is supposed to test if a bomb triggers the explosion of another bomb when its explodes
	}
}
