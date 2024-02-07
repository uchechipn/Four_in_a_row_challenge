package com.bptn.course.week2.challenge;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import com.bptn.course.week2.challenge.Exceptions.ColumnFullException;
import com.bptn.course.week2.challenge.Exceptions.InvalidMoveException;

public class Game {
	private Player[] players;
	private Board board;
	private static Scanner scanner = new Scanner(System.in);

	class GameTimer {
		private Timer timer;
		private int timeLimitInSecs;
		private boolean timeUp;

		public GameTimer(int timeLimitInSecs) {
			this.timer = new Timer(true);
			this.timeLimitInSecs = timeLimitInSecs;
			this.timeUp = false;
		}

		public void start() {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					timeUp = true;
					timer.cancel(); // Stop the timer when time is up
					System.out.println("Time is up!");
					System.exit(0);
				}
			}, timeLimitInSecs * 1000); // Convert seconds to milliseconds
		}

		public boolean isTimeUp() {
			return timeUp;
		}
	}

	public Game() {
		// Let's default it two players for now. Later, you can improve upon this to
		// allow the game creator to choose how many players are involved.
		this.players = new Player[2];// complete line.
		this.board = new Board();// complete line
	}

	public void setUpGame() {

		System.out.println("Enter player 1's name: ");
		players[0] = new Player(scanner.nextLine(), "1");
		System.out.println("Enter player 1's color: ");
		players[0].setColor(scanner.nextLine());

		// Ensure player 2's name is unique
		String playerTwoName;
		do {
			System.out.println("Enter player 2's name: ");
			playerTwoName = scanner.nextLine();
			

			if (playerTwoName.equals(players[0].getName())) {
				System.out.println("Error! Both players cannot have the same name.");
			}
		} while (playerTwoName.equals(players[0].getName()));

		// * wrap the code in here with a conditional block that enables the check
		// described above.

		players[1] = new Player(playerTwoName, "2");
		System.out.println("Enter player 2's color: ");
		players[1].setColor(scanner.nextLine());

		// set up the board using the appropriate method
		board.boardSetUp();
		// print the board the using appropriate method
		board.printBoard();
	}

	public void printWinner(Player player) {
		System.out.println(player.getName() + " is the winner");
	}

	public void playerTurn(Player currentPlayer) {
		int col;
		boolean validMove = false;

		do {
			try {
				col = currentPlayer.makeMove();
				validMove = board.addToken(col, currentPlayer.getPlayerNumber());
			} catch (InvalidMoveException e) {
				System.out.println("Invalid column. Please choose a valid column.");
			} catch (ColumnFullException e) {
				System.out.println("Column is full. Choose another column.");
			}
		} while (!validMove);

		// Print the board after successful token placement
		board.printBoard();
	}

	public void play() {
		boolean noWinner = true;
		this.setUpGame();
		int currentPlayerIndex = 0;

		int timeForGame = 15; // 1min
		GameTimer gameTimer = new GameTimer(timeForGame);
		gameTimer.start();

		while (gameTimer.isTimeUp() == false) {

			while (noWinner) {
				if (board.boardFull()) {// provide condition) {
					System.out.println("Board is now full. Game Ends.");
					return;
				}

				Player currentPlayer = players[currentPlayerIndex];
				// Override default to string for Player class
				System.out.println("It is player " + currentPlayer.getPlayerNumber() + "'s turn. " + currentPlayer);
				playerTurn(currentPlayer);
				if (board.checkIfPlayerIsTheWinner(currentPlayer.getPlayerNumber())) {
					printWinner(currentPlayer);
					noWinner = false;
				} else {
					currentPlayerIndex = (currentPlayerIndex + 1) % players.length;// reassign the variable to allow the
																					// game to continue. Note the index
																					// would wrap back to the first
																					// player if we are at the end.
																					// Think of using modulus (%).
				}
			}
		}

	}
}
