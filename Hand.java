package may;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Hand extends Card{
	private int maxCardNum;
	private LinkedList<Card> hand;

	/**
	 * constructor
	 * @param maxNum indicates maximum numbers of hand
	 */
	public Hand(int maxNum) {
		maxCardNum = maxNum;
		hand = new LinkedList<Card>();
	}
	

	/**
	 * return false if cards in hand have reached maximum
	 * @param newcard
	 * @return
	 */
	public boolean add(Card newcard) {
		if(hand.size() >= maxCardNum) {
			return false;
		}
		else 
			hand.add(newcard);
		return true;
	}

	/**
	 * 
	 * @return LinkedList<Card> handler
	 */
	public LinkedList<Card> getHand(){
		return hand;
	}
	
	/**
	 * check whether hand is full
	 * @return
	 */
	public boolean isFull() {
		return hand.size() == this.maxCardNum;
	}
	
	/**
	 * check whether hand is empty
	 * @return
	 */
	public boolean isEmpty() {
		return hand.size() == 0;
	}
	
	/**
	 * display all cards in hand
	 */
	public void showHand() {
		for(Card i:hand) {
			System.out.println(i.toString());
		}
	}
	
	/**
	 * convert card info into a string together with maximum card numbers
	 */
	@Override
	public String toString() {
		String ans = "";
		for(Card i:hand){
			ans = ans + i.toString() + " ; ";
		}
		ans = ans + "MaxSizeOfHand: " + this.maxCardNum;
		return ans;
	}
	
	/**
	 * Empty the hand
	 */
	public void resetHand() {
		hand.clear();
	}
	
	/**
	 * remove a random card
	 * @return true if success, false if fail
	 */
	public boolean removeCard() {
		if(hand.size() == 0)
			return false;
		int i = (int) (Math.random() * hand.size());
		hand.remove(i);
		return true;
	}
	
	/**
	 * find and delete the first card a, if not found, raise an exception
	 * @param a A card instance
	 * @return successful or not
	 * @throws InvalidCardReference
	 */
	public boolean removeCard(Card a) throws InvalidCardReference {
		for(int i = 0; i<hand.size(); i++) {
			if(hand.get(i).compareTo(a) == 0 && hand.get(i).sameSuit(a)) {
				hand.remove(i);
				return true;
			}
		}
		throw new InvalidCardReference();
	}
	

	/**
	 * sort hand by suit
	 */
	public void sortBySuit() {
		Collections.sort(hand, new Comparator<Card>() {

			@Override
			public int compare(Card arg0, Card arg1) {
				if(arg0.getSuit() == arg1.getSuit())
					return arg0.getRank() - arg1.getRank();
				return arg0.getSuit() - arg1.getSuit();
			}
			
		});
		
	}
	
	/**
	 * sort hand by rank
	 */
	public void sortByRank() {
		Collections.sort(hand, new Comparator<Card>() {

			@Override
			public int compare(Card o1, Card o2) {
				if(o1.getRank() == o2.getRank())
					return o1.getSuit() - o2.getSuit();
				return o1.getRank() - o2.getRank();
			}
			
		});
	}
	
	/**
	 * 
	 * @return how may cards are there in hand
	 */
	public int getCount() {
		return hand.size();
	}
	
	
	public static void main(String[] args) {
		System.out.println("hello");
		Hand one = new Hand(20);
		for(int i = 0; i<10; i++)
			one.add(new Card());
		one.showHand();
	}
}
