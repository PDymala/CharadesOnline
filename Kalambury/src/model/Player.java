package model;

/**
 * Klasa gracz. Posiada wszystkie jego parametry
 *
* @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class Player {

	private int id = 0;
	private String nick;
	private int points = 0;
	
	/**
	 * Konstruktor 2 parametrowy- gracz
	 *
	 * @param id id gracza
	 *
	 * @param nick Imie gracza

	 */
	public Player(int id, String nick) {
		this.id = id;
		this.nick = nick;
	}
	
	/**
	 * Konstruktor 1 parametrowy - gracz
	 *
	 *
	 * @param nick Imie gracza
	 */

	public Player(String nick) {
		this.nick = nick;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}


}
