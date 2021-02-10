package model;

/**
 * Klasa słowo. Posiada wszystkie jego parametry, tj kategorie i podpowiedzi
 *
* @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class Word {
	private int id;
	private String word;
	private String category;
	private String hint1;
	private String hint2;

	/**
	 * Konstruktor słowa
	 *
	 * @param id       id słowa
	 * @param word     słowo do odgadnięcia
	 * @param category kategoria słowa
	 * @param hint1    podpowiedz pierwsza
	 * @param hint2    podpowiedz druga
	 * 
	 */
	public Word(int id, String word, String category, String hint1, String hint2) {
		this.id = id;
		this.word = word;
		this.category = category;
		this.hint1 = hint1;
		this.hint2 = hint2;

	}

	public int getId() {
		return id;
	}

	public String getWord() {
		return word;
	}

	public String getCategory() {
		return category;
	}

	public String getHint1() {
		return hint1;
	}

	public String getHint2() {
		return hint2;
	}
}