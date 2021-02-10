package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Klasa gry. Posiada wszystkie jej parametry
 *
* @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */

public class Game extends Observable {

	private boolean isOn = false;
	private int numberOfRounds;
	private List<Word> listWordsForGame = new ArrayList<Word>();

	private WordLoader wordsForGame;
	private int hintTime;// ms
	private int gameTime;
	private int currentRound = 0;

	/**
	 * Konstruktor - gra. Ustawia parametry gry oraz pobiera losowe słowa do niej.
	 *
	 * @param hintTime       Czas podpowiedzi
	 *
	 * @param numberOfRounds Liczba rund
	 * 
	 * @param gameTime       Czas gry
	 */
	public Game(int hintTime, int numberOfRounds, int gameTime) {
		this.hintTime = hintTime;
		this.numberOfRounds = numberOfRounds;
		this.gameTime = gameTime;
		
		//ClassLoader classLoader = getClass().getClassLoader();
		//File file = new File(classLoader.getResource("Slowa.txt").getFile());
		
		
		//wordsForGame = new WordLoader(new File("src/model/Slowa.txt"));
		wordsForGame = new WordLoader("Slowa.tsv");
		//wordsForGame = new WordLoader(file);
		
		
		listWordsForGame.addAll(wordsForGame.getRandomWordsForGame(numberOfRounds));
	}

	/**
	 * Zwraca słowo i jej podpowiedzi dla danej rundy
	 * 
	 * @param round numer rundy
	 * @return słowo (klasa Word) dla danej rundy
	 */

	public Word getWord(int round) {
		return listWordsForGame.get(round);
	}

	/**
	 * Sprawdza poprawność podanej odpowiedzi
	 * 
	 * @param answer odpowiedz (String)
	 * @param round  numer rundy
	 * @return ok/nie ok
	 */
	public boolean checkAnswer(String answer, int round) {
		boolean isOk = false;

		if (answer.equalsIgnoreCase(listWordsForGame.get(round).getWord())) {
			isOk = true;

		}
		return isOk;
	}

	/**
	 * Ustawia następną rundę i przekazuję te informację do obserwatorów
	 */
	public void setNextRound() {
		currentRound = getCurrentRound() + 1;
		setChanged();
		notifyObservers(currentRound);
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

	public int getGameTime() {
		return gameTime;
	}

	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public void setStart() {

		setChanged();
		notifyObservers(0);
	}

	public WordLoader getWordsForGame() {
		return wordsForGame;
	}

	public void setWordsForGame(WordLoader wordsForGame) {
		this.wordsForGame = wordsForGame;
	}

	public int getHintTime() {
		return hintTime;
	}

	public void setHintTime(int hintTime) {
		this.hintTime = hintTime;
	}

	public int getNumberOfRounds() {
		return numberOfRounds;
	}

	public void setNumberOfRounds(int numberOfRounds) {
		this.numberOfRounds = numberOfRounds;
	}

}
