/**
 * 
 * This class represents a single Card. The card is a traditional playing card -
 * A, 2, 3, ... K with suits clubs, diamonds, spades, or hearts. Jokers are not
 * represented.
 * 
 * Face values are represented by the numbers 2 through 14 where 11 is a jack,
 * 12 is a queen, 13 is a king, and 14 is an ace. Suits are represented by 1 for
 * spades, 2 for hearts, 3 for clubs, and 4 for diamonds. Ordinal value is such
 * that number cards are worth their face value, picture cards are worth 10 and
 * Ace is worth 11.
 */

public class Card implements CardInterface, Comparable {

	// instance variables
	private int suit;
	private int rank;

	/**
	 * Default constructor. Initializes card to a random card in the deck.
	 */
	public Card() {
		rank = (int) (Math.random() * 13) + 2;
		suit = (int) (Math.random() * 4) + 1;
	}

	/**
	 * Copy constructor - needed for Hand class
	 * 
	 * @param other
	 */

	public Card(Object other) {
		if (other != null && other instanceof Card) {
			this.setRank((((Card) other).getRank()));
			this.setSuit(((Card) other).getSuit());
		}

	}

	/**
	 * Initializes to a specific card. Card is initialized to ace of spades if
	 * the parameters are illegal.
	 * 
	 * @param rank
	 *            face value
	 * @param suit
	 *            suit value
	 */
	public Card(int suit, int rank) {
		this.setSuit(suit);
		this.setRank(rank);

	}

	/**
	 * Returns the face value of the card, where the cards' values are specified
	 * in the set of constants defined above.
	 * 
	 * @return integer value of the rank - 2, 3, ... 14.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Returns the suit value of the card, where the suits' values are specified
	 * in the set of constants defined above.
	 * 
	 * @return integer value of the suit - 1, 2, 3, 4.
	 */
	public int getSuit() {
		return suit;
	}

	/**
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(int rank) {
		if (rank >= TWO && rank <= ACE)
			this.rank = rank;
		else
			throw new InvalidCardException("Attempt to set a card to an invalid rank");
	}

	/**
	 * @param suit
	 *            the suit to set
	 */
	public void setSuit(int suit) {
		if (suit >= SPADES && suit <= DIAMONDS)
			this.suit = suit;
		else
			throw new InvalidCardException("Attempt to set card to an invalid suit");
	}

	/**
	 * Compares suit of two cards
	 * 
	 * @param other
	 *            - Card to compare to
	 * @return true if other.suit == this.suit
	 */
	public boolean sameSuit(Card other) {
		if (other != null) {
			if (other.getSuit() == this.getSuit())
				return true;
			else
				return false;
		} else
			return false;
	}

	/**
	 * Compares the rank of 2 cards;
	 * 
	 * @param other
	 *            - other Card
	 * @return difference in rank as this.rank-other.rank; returns
	 *         Integer.MAX_VALUE if other is null.
	 */
	public int compareTo(Object other) {
		if (other == null || !(other instanceof Card)) {
			return Integer.MAX_VALUE;
		} else {
			return this.rank - ((Card) other).rank;
		}
	}

	/**
	 * Returns a String representation of the card's face value. "Two", "Three",
	 * ... "Ten", "Jack", "Queen", "King", "Ace".
	 * 
	 * @return String representation of face value.
	 */

	public String getRankAsString() {
		switch (rank) {
		case TWO:
			return "Two";
		case THREE:
			return "Three";
		case FOUR:
			return "Four";
		case FIVE:
			return "Five";
		case SIX:
			return "Six";
		case SEVEN:
			return "Seven";
		case EIGHT:
			return "Eight";
		case NINE:
			return "Nine";
		case TEN:
			return "Ten";
		case ACE:
			return "Ace";
		case KING:
			return "King";
		case QUEEN:
			return "Queen";
		case JACK:
			return "Jack";
		default:
			return Integer.toString(rank);
		}
	}

	/**
	 * Returns a String representation of the card's suit. "Clubs", "Hearts",
	 * "Diamonds", "Spades".
	 * 
	 * @return String representation of suit.
	 */
	public String getSuitAsString() {
		switch (suit) {
		case CLUBS:
			return "Clubs";
		case DIAMONDS:
			return "Diamonds";
		case HEARTS:
			return "Hearts";
		default:
			return "Spades";

		}
	}

	/**
	 * String contains the instance data rank and suit in the form rank of suit
	 * 
	 * @returns a String representation of Card
	 * 
	 */
	public String toString() {
		String retString = "rank: " + this.getRank() + " suit: " + this.getSuit();
		return retString;
	}

	@Override
	public int compareTo(Card other) {
		// TODO Auto-generated method stub
		return 0;
	}
}
