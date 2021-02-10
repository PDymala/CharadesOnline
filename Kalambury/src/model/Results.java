package model;

/**
 * Klasa wynik. Posiada wszystkie jego parametry
 *
* @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class Results {

	private int id;
	private String nick1;
	private int result1;
	private String nick2;
	private int result2;
	private String date;

	/**
	 * Konstruktor
	 *
	 * @param id      id gracza
	 * @param nick1   Imie gracza 1
	 * @param result1 Wynik gracza 1
	 * @param nick2   Imie gracza 2
	 * @param result2 Wynik gracza 2
	 * @param date    Data gry
	 * 
	 */

	public Results(int id, String nick1, int result1, String nick2, int result2, String date) {
		this.id = id;
		this.nick1 = nick1;
		this.result1 = result1;
		this.nick2 = nick2;
		this.result2 = result2;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNick1() {
		return nick1;
	}

	public void setNick1(String nick1) {
		this.nick1 = nick1;
	}

	public int getResult1() {
		return result1;
	}

	public void setResult1(int result1) {
		this.result1 = result1;
	}

	public String getNick2() {
		return nick2;
	}

	public void setNick2(String nick2) {
		this.nick2 = nick2;
	}

	public int getResult2() {
		return result2;
	}

	public void setResult2(int result2) {
		this.result2 = result2;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}