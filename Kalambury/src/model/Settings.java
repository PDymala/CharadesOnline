package model;

/**
 * Klasa Settings. Ustawienia gry, serwera i klienta
 *
* @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class Settings {
	private int port;
	private int rounds;
	private String ip;
	private Type type;
	private String resultListUrl = "Wyniki.tsv";



	/**
	 * Konstruktor ustawień
	 *
	 * @param port   PORT do założenia serwera lub do połączenia się z nim
	 * @param rounds Liczba rund w zakładanej grze
	 * @param type   Typ ustawień (HOST, CLIENT) enum
	 * 
	 */
	public Settings(int port, int rounds, Type type) {
		this.port = port;
		this.rounds = rounds;
	}

	/**
	 * Konstruktor ustawień
	 *
	 * @param port PORT do założenia serwera lub do połączenia się z nim
	 * @param ip   IP do połączenia
	 * @param type Typ ustawień (HOST, CLIENT) enum
	 * 
	 */
	public Settings(int port, String ip, Type type) {
		this.port = port;
		this.ip = ip;
	}

	public enum Type {
		HOST, CLIENT
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getResultListUrl() {
		return resultListUrl;
	}

	public void setResultListUrl(String resultListUrl) {
		this.resultListUrl = resultListUrl;
	}
}
