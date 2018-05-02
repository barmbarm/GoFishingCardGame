package may;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class PlayGame extends Card{
	
	private static final int MAXHAND = 40;
	private LinkedList<Hand> playerHands;
	private DeckList funDeck;
	private int numPlayers;
	private int[] playerScores;
	
	private ArrayList<Set<Integer>> restPlayers;
	
	private JFrame MessageWin;
	private JTextArea Msg = new JTextArea(25,30);

	/**
	 * Initialize a deck, n playerHands, playerScores, n restPlayers.
	 * @param numPlayers, indicates n players
	 * @param handSize, indicate initialize hand numbers
	 * @throws Exception, throws exception when invalid arguments
	 */
	public PlayGame(int numPlayers, int handSize) throws Exception {
		if(numPlayers > 4 || numPlayers <2)
			throw new Exception("Invalid Player Numbers");
		if(handSize > 13 || handSize <0)
			throw new Exception("Invalid Hand Size");
		funDeck = new DeckList();
		funDeck.shuffle();
		this.numPlayers = numPlayers;
		playerScores = new int[this.numPlayers];
		this.playerHands = new LinkedList<Hand> ();
		dealHands(handSize);
		initPlayers();
		initWindow();
	}
	
	private void initWindow() {
		MessageWin = new JFrame();
		JPanel contentPane = new JPanel(new BorderLayout(5,15));
		contentPane.setBorder(new EmptyBorder(10,10,10,10));
		Msg.setLineWrap(true);
		Msg.setWrapStyleWord(true);
		JScrollPane jsp = new JScrollPane(
				Msg,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(jsp, BorderLayout.PAGE_START);
		
		MessageWin.add(contentPane);
		MessageWin.setTitle("Game Details");
		MessageWin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				MessageWin.dispose();
			}
		});
		JPanel btnConstrain = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		btnConstrain.add(btnOk);
		contentPane.add(btnConstrain, BorderLayout.PAGE_END);
		MessageWin.pack();
	}
	
	/**
	 * Give p cards to n playerHands.
	 * @param handSize, indicates p cards for each player
	 */
	private void dealHands(int handSize) {
		for(int i=0; i<this.numPlayers; i++) {
			Hand newHand = new Hand(MAXHAND);
			for(int j=0; j<handSize; j++){
				newHand.add(funDeck.getCard());
			}
			playerHands.add(newHand);
		}
	}
	
	/**
	 * Record n restPlayers(a HashSet) for each player.
	 */
	private void initPlayers() {
		restPlayers = new ArrayList<Set<Integer>>();
		for(int i = 0; i<numPlayers; i++) {
			Set<Integer> a = new HashSet<Integer>();
			for(int j = 0; j<numPlayers; j++) {
				if(j == i)
					continue;
				a.add(j);
			}
			restPlayers.add(a);
		}
	}
	
	/**
	 * Judge if playerNum's hand has a book
	 * @param playerNum, nth player
	 * @return beginning of the book if it exists, -1 if not
	 */
	private int hasFourOfaKind(int playerNum) {
		playerHands.get(playerNum).sortByRank();
		LinkedList<Card> a = playerHands.get(playerNum).getHand();
		for(int i=0; i<a.size()-3; i++) {
			int rank = a.get(i).getRank();
			for(int j=1; j<4; j++) {
				if(rank == a.get(i+j).getRank()) {
					if(j == 3) {
						displayInfo(playerNum + " has a book of " + a.get(i).getRank() + " @ " + i + " total " + a.size());
						return i;
					}
					continue;
				}
				else
					break;
			}
		}
		return -1;
	}
	
	/**
	 * Delete book of 4 cards at pos, if nth player runs out of cards
	 * 1. draw at most five cards from deck when deck available
	 * 2. quit game and update the status of restPlayers
	 * @param playerNum, nth player
	 * @param pos, beginning of book
	 */
	private void DeleteAndSupply(int playerNum, int pos){
		for(int i=0; i<4; i++) {
			//System.out.println(playerHands.get(playerNum).getHand().size());
			playerHands.get(playerNum).getHand().remove(pos);
		}
		if(playerHands.get(playerNum).getCount() == 0) {
			displayInfo(playerNum + " is running out of Cards.");
			if(funDeck.getCardsLeft() == 0) {
				displayInfo("There is no cards in deck! " + playerNum + " quit");
				for(int i = 0; i<numPlayers; i++) {
					if(i == playerNum)
						continue;
					restPlayers.get(i).remove(playerNum);
				}
			}else {
				displayInfo(playerNum + " is drawing card from deck.");
				for(int i=0; i<5 && funDeck.getCardsLeft()>0; i++)
					playerHands.get(playerNum).add(funDeck.getCard());
			}
		}
	}
	
	/**
	 * Judge ith player has card of specific rank.
	 * @param playerNum, ith player
	 * @param wantedRank, wanted rank
	 * @return card position j if it exists, -1 if not
	 */
	private int hasWantedCard(int playerNum, int wantedRank) {
		LinkedList<Card> a = playerHands.get(playerNum).getHand();
		for(int i=0; i<a.size(); i++) {
			if(a.get(i).getRank() == wantedRank)
				return i;
		}
		return -1;
	}
	
	/**
	 * Update score of ith player
	 * @param playerNum, ith player
	 */
	private void updateScore(int playerNum) {
		this.playerScores[playerNum]++;
	}
	
	/**
	 * To see if winner exist:
	 * 1. no cards available in deck
	 * 2. other players' hand must be empty except me
	 * 3. winner has highest score
	 * @param playerNum, ith player who got the last book
	 * @return -1 when winner not exists, player number when winner exists
	 */
	private int determineWinner(int playerNum) {
		if(this.funDeck.getCardsLeft() != 0)
			return -1;
		//Except myself, other people don't have any cards
		for(int i=0; i<numPlayers; i++) {
			if(i == playerNum)
				continue;
			if(playerHands.get(i).getCount() != 0)
				return -1;
		}
		
		int maxScore = playerScores[0];
		int winner = 0;
		for(int i=1; i<this.numPlayers; i++) {
			if(playerScores[i] > maxScore) {
				winner = i;
				maxScore = playerScores[i];
			}
		}
		if(maxScore == 0)
			return -1;
		return winner;
	}
	
	
	
	// Play GoFishing!
	/**
	 * First player ask for a random card from a random player,
	 * 1.he doesn't have it
	 *   first player go fish, and draw a card from deck
	 *   1.1. available, check hasFourOfaKind, delete and supply, update score and pass to next player
	 *   1.2. unavailable, pass to next player
	 * 2.he has it
	 *   first player got it, and check hasFourOfaKind
	 *   2.1. if has, delete and supply, update score, check if winner exists
	 *        2.1.1. if yes, game ends
	 *        2.2.2. if not, see if first player has got more than one card. if yes, he continues, else pass to next player
	 *   2.2. if not, first player continues.
	 */
	public void playNow() {
		
		int curPlayer = 0;
		
		boolean test  = true;
		while(test) {
			
			//curPlayer ask random person
			int ask_ith_person = new Random().nextInt(restPlayers.get(curPlayer).size());
			int ask_whom = 0;
			int index = 0;
			for(int item:restPlayers.get(curPlayer)) {
				if(index == ask_ith_person) {
					ask_whom = item;
					break;
				}
				index++;
			}
			
			//curPlayer ask random card
			int ask_ith_card = new Random().nextInt(playerHands.get(curPlayer).getHand().size());
			int ask_rank = playerHands.get(curPlayer).getHand().get(ask_ith_card).getRank();
			
			//playerHands.get(curPlayer).showHand();
			displayInfo(curPlayer + " ask " + ask_whom + " for " + ask_rank);
			
			int ith_card = hasWantedCard(ask_whom, ask_rank);
			if(ith_card < 0) {
				displayInfo(ask_whom + " says GoFish");
				if(funDeck.getCardsLeft() > 0) {
					Card deal = funDeck.getCard();
					displayInfo(curPlayer + " draw card " + deal.toString());
					playerHands.get(curPlayer).add(deal);
					int has_book = hasFourOfaKind(curPlayer);
					if(has_book != -1) {
						DeleteAndSupply(curPlayer, has_book);
						updateScore(curPlayer);
					}					
				}
				curPlayer = (curPlayer + 1) % this.numPlayers;
				while(playerHands.get(curPlayer).getCount() == 0)
					curPlayer = (curPlayer + 1) % this.numPlayers;
				//continue;

			}else {
				Card wanted_one = playerHands.get(ask_whom).getHand().remove(ith_card);
				displayInfo(curPlayer + " get Card " + wanted_one.toString() + " from " + ask_whom);
				playerHands.get(curPlayer).add(wanted_one);
				
				int has_book = hasFourOfaKind(curPlayer); 
				if(has_book != -1) {
					DeleteAndSupply(curPlayer, has_book);
					updateScore(curPlayer);
					int ans = determineWinner(curPlayer);
					if(ans != -1) {
						displayInfo("Final winner is: " + ans);
						return;
					}
					if(playerHands.get(curPlayer).getCount() == 0) {
						while(playerHands.get(curPlayer).getCount() == 0)
							curPlayer = (curPlayer + 1) % this.numPlayers;
					}
				}
				
				//continue;
			}
			
		}
		
	}
	
	
	// Display Info
	/**
	 * Display information on console
	 * @param info
	 */
	public void displayInfo(String info) {
		System.out.println(info);
		Msg.append(info + "\n");
	}
	
	
	// Below are access methods
	/** 
	 * @return how many players are there in game
	 */
	public int getNumPlayers() {
		return this.numPlayers;
	}
	
	/**
	 * 
	 * @param playerNum, ith player
	 * @return hand of specific player
	 */
	public Hand getHand(int playerNum) {
		return playerHands.get(playerNum);
	}
	
	/**
	 * Show ith player's hand
	 * @param playerNum, ith player
	 */
	public void showHand(int playerNum) {
		Hand a = playerHands.get(playerNum);
		a.showHand();
	}
	
	

	/**
	 * Test if everything goes right.
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println((int) (3 * Math.random()));
		try {
			PlayGame one = new PlayGame(4, 5);
			one.MessageWin.setVisible(true);
			one.playNow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


/*
0 ask 3 for 4
3 says GoFish
0 draw card rank: 6 suit: 3
1 ask 2 for 9
2 says GoFish
1 draw card rank: 13 suit: 1
2 ask 0 for 5
0 says GoFish
2 draw card rank: 10 suit: 1
3 ask 1 for 8
1 says GoFish
3 draw card rank: 6 suit: 1
0 ask 2 for 8
2 says GoFish
0 draw card rank: 8 suit: 1
1 ask 3 for 9
1 get Card rank: 9 suit: 2 from 3
1 ask 2 for 13
2 says GoFish
1 draw card rank: 14 suit: 2
2 ask 0 for 10
0 says GoFish
2 draw card rank: 12 suit: 4
3 ask 0 for 3
0 says GoFish
3 draw card rank: 14 suit: 3
0 ask 2 for 8
2 says GoFish
0 draw card rank: 14 suit: 1
1 ask 0 for 9
0 says GoFish
1 draw card rank: 12 suit: 1
2 ask 3 for 6
2 get Card rank: 6 suit: 1 from 3
2 ask 0 for 6
2 get Card rank: 6 suit: 3 from 0
2 ask 0 for 11
0 says GoFish
2 draw card rank: 4 suit: 1
3 ask 0 for 10
0 says GoFish
3 draw card rank: 5 suit: 3
0 ask 2 for 7
2 says GoFish
0 draw card rank: 13 suit: 4
1 ask 0 for 7
1 get Card rank: 7 suit: 2 from 0
1 ask 2 for 12
1 get Card rank: 12 suit: 4 from 2
1 ask 2 for 14
2 says GoFish
1 draw card rank: 6 suit: 4
2 ask 1 for 11
1 says GoFish
2 draw card rank: 3 suit: 1
3 ask 0 for 5
0 says GoFish
3 draw card rank: 11 suit: 1
0 ask 1 for 13
0 get Card rank: 13 suit: 1 from 1
0 ask 2 for 8
2 says GoFish
0 draw card rank: 12 suit: 3
1 ask 2 for 2
2 says GoFish
1 draw card rank: 3 suit: 3
2 ask 1 for 3
2 get Card rank: 3 suit: 3 from 1
2 ask 3 for 5
2 get Card rank: 5 suit: 3 from 3
2 ask 0 for 5
0 says GoFish
2 draw card rank: 2 suit: 3
3 ask 0 for 11
0 says GoFish
3 draw card rank: 2 suit: 4
0 ask 2 for 8
2 says GoFish
0 draw card rank: 10 suit: 2
1 ask 3 for 7
3 says GoFish
1 draw card rank: 11 suit: 4
2 ask 0 for 3
0 says GoFish
2 draw card rank: 5 suit: 4
3 ask 0 for 14
3 get Card rank: 14 suit: 1 from 0
3 ask 2 for 2
3 get Card rank: 2 suit: 3 from 2
3 ask 0 for 8
3 get Card rank: 8 suit: 1 from 0
3 ask 1 for 2
3 get Card rank: 2 suit: 1 from 1
3 ask 1 for 11
3 get Card rank: 11 suit: 4 from 1
3 ask 2 for 10
3 get Card rank: 10 suit: 1 from 2
3 ask 0 for 14
0 says GoFish
3 draw card rank: 7 suit: 4
0 ask 1 for 13
1 says GoFish
0 draw card rank: 4 suit: 3
1 ask 3 for 6
3 says GoFish
1 draw card rank: 2 suit: 2
2 ask 1 for 5
1 says GoFish
2 draw card rank: 9 suit: 4
3 ask 1 for 7
3 get Card rank: 7 suit: 1 from 1
3 ask 1 for 11
1 says GoFish
3 draw card rank: 9 suit: 3
0 ask 1 for 13
1 says GoFish
0 draw card rank: 12 suit: 2
1 ask 0 for 14
0 says GoFish
1 draw card rank: 4 suit: 4
2 ask 1 for 3
1 says GoFish
2 draw card rank: 13 suit: 2
3 ask 2 for 10
2 says GoFish
3 draw card rank: 5 suit: 1
0 ask 2 for 13
0 get Card rank: 13 suit: 2 from 2
0 has a book of 13 @ 7 total 11
0 ask 1 for 8
1 says GoFish
1 ask 2 for 12
2 says GoFish
2 ask 1 for 3
1 says GoFish
3 ask 0 for 5
0 says GoFish
0 ask 3 for 4
3 says GoFish
1 ask 3 for 9
1 get Card rank: 9 suit: 3 from 3
1 ask 3 for 7
1 get Card rank: 7 suit: 1 from 3
1 ask 2 for 12
2 says GoFish
2 ask 1 for 4
2 get Card rank: 4 suit: 4 from 1
2 ask 3 for 3
2 get Card rank: 3 suit: 2 from 3
2 has a book of 3 @ 0 total 15
2 ask 0 for 6
0 says GoFish
3 ask 1 for 10
1 says GoFish
0 ask 3 for 12
3 says GoFish
1 ask 3 for 9
3 says GoFish
2 ask 3 for 11
2 get Card rank: 11 suit: 1 from 3
2 ask 1 for 11
1 says GoFish
3 ask 1 for 5
1 says GoFish
0 ask 3 for 4
3 says GoFish
1 ask 2 for 7
2 says GoFish
2 ask 1 for 5
1 says GoFish
3 ask 0 for 10
3 get Card rank: 10 suit: 2 from 0
3 has a book of 10 @ 7 total 14
3 ask 0 for 2
0 says GoFish
0 ask 2 for 4
0 get Card rank: 4 suit: 1 from 2
0 ask 1 for 4
1 says GoFish
1 ask 2 for 14
2 says GoFish
2 ask 1 for 11
1 says GoFish
3 ask 2 for 8
2 says GoFish
0 ask 2 for 8
2 says GoFish
1 ask 3 for 6
3 says GoFish
2 ask 0 for 9
0 says GoFish
3 ask 1 for 8
1 says GoFish
0 ask 1 for 4
1 says GoFish
1 ask 3 for 12
3 says GoFish
2 ask 0 for 4
2 get Card rank: 4 suit: 1 from 0
2 ask 3 for 9
3 says GoFish
3 ask 0 for 5
0 says GoFish
0 ask 3 for 4
3 says GoFish
1 ask 0 for 2
0 says GoFish
2 ask 0 for 9
0 says GoFish
3 ask 2 for 2
2 says GoFish
0 ask 1 for 8
1 says GoFish
1 ask 2 for 14
2 says GoFish
2 ask 3 for 4
3 says GoFish
3 ask 1 for 7
3 get Card rank: 7 suit: 1 from 1
3 ask 2 for 8
2 says GoFish
0 ask 3 for 4
3 says GoFish
1 ask 0 for 7
0 says GoFish
2 ask 1 for 5
1 says GoFish
3 ask 1 for 2
3 get Card rank: 2 suit: 2 from 1
3 has a book of 2 @ 0 total 12
3 ask 1 for 5
1 says GoFish
0 ask 1 for 12
0 get Card rank: 12 suit: 1 from 1
0 ask 3 for 8
0 get Card rank: 8 suit: 1 from 3
0 ask 1 for 4
1 says GoFish
1 ask 3 for 9
3 says GoFish
2 ask 1 for 11
1 says GoFish
3 ask 2 for 14
2 says GoFish
0 ask 2 for 4
0 get Card rank: 4 suit: 1 from 2
0 ask 3 for 8
0 get Card rank: 8 suit: 3 from 3
0 has a book of 8 @ 3 total 10
0 ask 2 for 12
2 says GoFish
1 ask 0 for 9
0 says GoFish
2 ask 1 for 11
1 says GoFish
3 ask 2 for 14
2 says GoFish
0 ask 1 for 4
1 says GoFish
1 ask 2 for 6
1 get Card rank: 6 suit: 1 from 2
1 ask 0 for 14
0 says GoFish
2 ask 0 for 5
0 says GoFish
3 ask 0 for 7
0 says GoFish
0 ask 3 for 4
3 says GoFish
1 ask 2 for 9
1 get Card rank: 9 suit: 4 from 2
1 has a book of 9 @ 4 total 11
1 ask 3 for 12
3 says GoFish
2 ask 3 for 11
2 get Card rank: 11 suit: 4 from 3
2 has a book of 11 @ 6 total 10
2 ask 1 for 5
1 says GoFish
3 ask 1 for 7
3 get Card rank: 7 suit: 2 from 1
3 ask 1 for 7
3 get Card rank: 7 suit: 3 from 1
3 has a book of 7 @ 1 total 7
3 ask 0 for 5
0 says GoFish
0 ask 3 for 12
3 says GoFish
1 ask 2 for 14
2 says GoFish
2 ask 0 for 6
0 says GoFish
3 ask 0 for 14
0 says GoFish
0 ask 1 for 4
1 says GoFish
1 ask 0 for 14
0 says GoFish
2 ask 3 for 5
2 get Card rank: 5 suit: 1 from 3
2 has a book of 5 @ 1 total 7
2 ask 1 for 6
2 get Card rank: 6 suit: 1 from 1
2 ask 0 for 6
0 says GoFish
3 ask 2 for 14
2 says GoFish
0 ask 1 for 4
1 says GoFish
1 ask 0 for 14
0 says GoFish
2 ask 3 for 4
3 says GoFish
3 ask 2 for 14
2 says GoFish
0 ask 2 for 4
0 get Card rank: 4 suit: 4 from 2
0 has a book of 4 @ 0 total 7
0 ask 2 for 12
2 says GoFish
1 ask 0 for 6
0 says GoFish
2 ask 0 for 6
0 says GoFish
3 ask 0 for 14
0 says GoFish
0 ask 2 for 12
2 says GoFish
1 ask 3 for 14
1 get Card rank: 14 suit: 1 from 3
1 ask 3 for 14
1 get Card rank: 14 suit: 3 from 3
1 has a book of 14 @ 2 total 6
1 ask 2 for 12
2 says GoFish
2 ask 1 for 6
2 get Card rank: 6 suit: 4 from 1
2 has a book of 6 @ 0 total 4
2 is running out of Cards.
There is no cards in deck! 2 quit
0 ask 3 for 12
3 says GoFish
1 ask 0 for 12
1 get Card rank: 12 suit: 1 from 0
1 ask 3 for 12
3 says GoFish
0 ask 1 for 12
0 get Card rank: 12 suit: 1 from 1
0 ask 3 for 12
3 says GoFish
1 ask 3 for 12
3 says GoFish
0 ask 1 for 12
0 get Card rank: 12 suit: 4 from 1
0 has a book of 12 @ 0 total 4
0 is running out of Cards.
There is no cards in deck! 0 quit
Final winner is: 0
*/