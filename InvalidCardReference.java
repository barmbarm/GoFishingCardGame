package may;

public class InvalidCardReference extends Exception{

	/**
	 * 
	 */
	public InvalidCardReference() {
		// TODO Auto-generated constructor stub
		super("No such card in the hand");
	}
	
	 public InvalidCardReference(String msg)
	 {
	     super(msg);
	 }

	}

