package bomberman.engine;

/**
 * Class responsible for the powerUp structure used in the logic of the game level.
 * 
 * @author Rui Figueira - ei11021
 * @author Pedro Faria - ei11167
 */

public class PowerUp {

	private int type;
	private boolean exists = false;
	private boolean activated = false;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	public boolean getExists() {
		return exists;
	}
	public void setExists(boolean exists) {
		this.exists = exists;
	}
	
	/**
	 * Constructor of this class.Creates a powerUp object with 2 fields to be used in the logic matrix of powerups.
	 */
	public PowerUp(){
		this.exists = false;
		this.activated = false;
	}
}
