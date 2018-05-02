package may;

/**
 * An interface for a playing card
 * 
 */
public interface CardInterface {
	// Define the suit
	public static final int SPADES = 1;
	public static final int HEARTS = 2;
	public static final int CLUBS = 3;
	public static final int DIAMONDS = 4;
	// define the card rank
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	public static final int SIX = 6;
	public static final int SEVEN = 7;
	public static final int EIGHT = 8;
	public static final int NINE = 9;
	public static final int TEN = 10;
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;
	public static final int ACE = 14;

	/**
	 * Returns the face value of the card, where the cards' values are specified
	 * in a set of constants.
	 * 
	 * @return integer value of the rank.
	 */
	public int getRank();

	//
	/**
	 * Returns the suit value of the card, where the suits' values are specified
	 * in a set of constants.
	 * 
	 * @return integer value of the suit - 1, 2, 3, 4.
	 */
	public int getSuit();

	//
	/**
	 * Sets the rank of the card, a number from 2 to 14, representing the face
	 * value of a numeric card, or 11 for Jack, 12 for Queen, 13 for King and 14
	 * for Ace
	 * 
	 * @param rank
	 *            the rank to set if rank is not valid, rank is set to 2.
	 */
	public void setRank(int rank);

	//
	/**
	 * sets the suit of the card, a number from 1 to 4, representing spades,
	 * hearts, clubs, diamonds respectively.
	 * 
	 * @param suit
	 *            the suit to set if suit is not valid, suit is set to spades
	 *            (1)
	 */
	public void setSuit(int suit);

	//
	/**
	 * Compares suit of two cards
	 * 
	 * @param other
	 *            - Card to compare to
	 * @return true if other.suit == this.suit
	 */
	public boolean sameSuit(Card other);

	//
	/**
	 * Compares the rank of 2 cards;
	 * 
	 * @param other
	 *            - other Card
	 * @return difference in rank as this.rank-other.rank; returns
	 *         Integer.MAX_VALUE if other is null.
	 */
	public int compareTo(Card other);

	/**
	 * Compares the rank of 2 cards;
	 * 
	 * @param other
	 *            - other Card
	 * @return difference in rank as this.rank-other.rank; returns
	 *         Integer.MAX_VALUE if other is null.
	 */
	public int compareTo(Object other);

	//
	/**
	 * Returns a String representation of the card's face value. "Two", "Three",
	 * ... "Ten", "Jack", "Queen", "King", "Ace".
	 * 
	 * @return String representation of face value.
	 */
	public String getRankAsString();

	//
	/**
	 * Returns a String representation of the card's suit. "Hearts" or "Spades"
	 * or "Diamonds" or "Clubs"
	 * 
	 * @return String representation of suit.
	 */
	public String getSuitAsString();

	//
	@Override
	/**
	 * Returns a string indicating suit and rank as raw integers
	 * 
	 * @return string representation of numeric suit and rank
	 */
	public String toString();

}
