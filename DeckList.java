package may;

import java.util.LinkedList;
import java.util.Random;

public class DeckList extends Card {
	public static final int DECKSIZE = 52;
	private LinkedList<Card> deck;
	private int cardsDealt = 0;

	/**
	 * Constructor
	 */
	public DeckList() {
		deck = new LinkedList<Card>();
		this.initializeDeck();
		this.cardsDealt = 0;
	}

	private void initializeDeck() {
		for (int suit = Card.SPADES; suit <= Card.DIAMONDS; suit++) {
			for (int rank = Card.TWO; rank <= Card.ACE; rank++) {
				Card e = new Card(suit, rank);
				deck.addLast(e);

			}
		}
	}

	/**
	 * resets the deck to 0 cards dealt and all 52 cards in the deck
	 */
	@SuppressWarnings("unused")
	public void reset() {
		cardsDealt = 0;
		this.initializeDeck();
	}

	/**
	 * @return number of cards left in the deck
	 */
	public int getCardsLeft() {
		return this.DECKSIZE - cardsDealt;
	}

	/**
	 * @return number of cards already dealt
	 */
	public int getCardsDealt() {
		return cardsDealt;
	}

	/**
	 * deals a card from the deck returns top card and removes it from the deck
	 * 
	 * @return card at the top of the deck
	 */
	public Card getCard() {
		if (this.getCardsLeft() <= 0 || this.deck.isEmpty()) {
			return null;
		}
		this.cardsDealt++;
		Card deal = (Card) deck.getFirst();
		deck.removeFirst();
		return deal;
	}

	/**
	 * This methods peeks at the top card but does not remove it from the deck
	 * 
	 * @return copy of the top card in the deck
	 */
	public Card peek() {
		if (this.deck.isEmpty())
			return null;// no card to see
		else {
			return new Card(deck.getFirst());// use copy constructor
		}
	}

	/**
	 * Shuffles the deck in random order
	 */

	public void shuffle() {
		Random random = new Random(System.nanoTime());
		for (int i = 0; i < deck.size(); i++) {
			int pos1 = random.nextInt(deck.size());

			// swap two Cards in deck
			// get and remove a random card
			Card temp1 = (Card) this.deck.get(pos1);
			this.deck.remove(pos1);
			// get and remove another random card
			int pos2 = random.nextInt(deck.size());
			// make sure you are not swapping the same two
			while (pos2 == pos1)
				pos2 = random.nextInt(deck.size());
			Card temp2 = (Card) this.deck.get(pos2);
			this.deck.remove(pos2);
			// replace pos2 card with pos1 card
			deck.add(pos2, temp1);
			// replace pos1 card with pos2 card
			deck.add(pos1, temp2);
		}
	}

	public void showDeck() {
		for (int i = 0; i < deck.size(); i++) {
			System.out.print(this.deck.get(i) + ", ");
			if ((i + 1) % 5 == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	public void showDeck(int numPerLine) {
		for (int i = 0; i < deck.size(); i++) {
			System.out.print(this.deck.get(i) + ", ");
			if ((i + 1) % numPerLine == 0 && i != 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeckList [deck=" + deck + ", cardsDealt=" + cardsDealt + "]";
	}
	
	public static void main(String[] args) {
		System.out.println("WOW");
		DeckList one = new DeckList();
		one.showDeck();
	}

}
