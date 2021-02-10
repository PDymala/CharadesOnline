package net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Klasa pokazująca IP komputera
 *
 * @author Piotr Dymala / p.dymala@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class ServerIP {

	/**
	 * KOnstruktor bezparametrowy
	 * 
	 */
	public ServerIP() {

	}

	private InetAddress IPadress;

	/**
	 * Zwraca IP w formie String danego komputera
	 * 
	 * @return IP komputera
	 * 
	 */
	public String getIpAdress() {

		try {
			IPadress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("Error while reading IP adress");
			e.printStackTrace();
		}

		return IPadress.getHostAddress();
	}
}
