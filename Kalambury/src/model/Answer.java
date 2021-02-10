package model;

/**
* Klasa odpowiedzi udzielanej przez użytkowników
*
* @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
* @version 1.0
* @since   2019-01-20
*/
public class Answer {

	Player player;
	String answer;

	/**
	 * Konstruktor - odpowiedz udzielona przez użytkownika
	 *
	 * @param player Gracz
	 *
	 * @param answer Odpowiedz

	 */
	
	public Answer(Player player, String answer) {
		this.player = player;
		this.answer = answer;
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	
}
