package net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

import model.Answer;
import model.Player;

/**
 * Klasa klienta - łączy z serwerem oraz oczekuje na informacje od niego.
 * Kolejno je przekazuje do kontrolera.
 *
 * @author Piotr Dymala / p.dymala@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class Client extends Observable {

	protected boolean isStopped = false;
	private int serverPort;
	private String ip;
	private OutputStream output;
	private InputStream input;
	private DataOutputStream out;
	private DataInputStream in;
	private Player player;
	private Socket serverSocket;
	Thread t2 = null;
	/**
	 * Wysyła informacje do serwera. Może być to informacja int lub String.
	 * Informacje do serwera wysylamy i dostajemy w dw�ch czesciach. W pierwszej
	 * typu int/byte, okreslany jest typ wiadomosci np, 0 - hello msg 10 -
	 * wiadomosc/odpowiedz na pytanie 99 - koniec gry
	 * 
	 * W drugiej czesci jest wartosc tej informacji, np. tresc wiadomosci. Czyli
	 * tekst czatu wysylamy: sendMsg(0); sendMsg("czesc!")
	 * 
	 * @param msg          Informacja do wyslania do serwera (int lub String)
	 * @param serverSocket Socket do którego jesteśmy połączeni z serwerem.
	 *                     Najprosciej Client.getServerSocket
	 * 
	 * 
	 */
	public void sendMsg(Object msg, Socket serverSocket) {

		// checking if socket exist
		if (serverSocket != null && serverSocket.isClosed() == false) {
			try {
				output = serverSocket.getOutputStream();
				out = new DataOutputStream(output);

				// choosing type of msg and sending it
				if (msg instanceof Integer) {
					out.writeInt((int) msg);

				}

				else if (msg instanceof String) {
					out.writeUTF((String) msg);
					
				}

				else {
					new IOException("wrong msg type");
				}

				Thread.sleep(50);
			} catch (SocketException e) {
				
				String[] temp88 = { Integer.toString(88) };
				setChanged();
				notifyObservers(temp88);
				e.printStackTrace();
			} catch (IOException | InterruptedException e) {
			
				e.printStackTrace();
			}
		} else {
			
		}

	}

	/**
	 * Konstruktor który łączy z serwerem. Nasłuchuje informacje od niego i
	 * przekazuje je do kontrolera
	 * 
	 * @param ip         IP serwera
	 * @param serverPort PORT serwera
	 * @param clientName imie klienta który się łączy
	 */
	@SuppressWarnings("deprecation")
	public Client(String ip, int serverPort, String clientName) {
		this.serverPort = serverPort;
		this.ip = ip;

		try {
			serverSocket = new Socket(ip, serverPort);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (t2 != null) {t2.stop();}
			// listening for msgs from client
			Runnable serverInput = () -> {

				sendMsg((int) 1, serverSocket);
				sendMsg(clientName, serverSocket);

				while (serverSocket != null && serverSocket.isClosed() == false
						&& !Thread.currentThread().isInterrupted()) {
					try {
						input = serverSocket.getInputStream();
						in = new DataInputStream(input);

						int messageType = in.readInt();
						switch (messageType) {

						case 0:

							break;

						case 1:
							player = new Player(in.readUTF());
							setChanged();
							notifyObservers(player);

							break;

						case 9:
							String[] temp9 = { Integer.toString(9), in.readUTF() };
							setChanged();
							notifyObservers(temp9);

							break;
						case 11:
							String[] temp11 = { Integer.toString(11), in.readUTF() };
							setChanged();
							notifyObservers(temp11);

							break;

						case 12:
							String[] temp12 = { Integer.toString(12), in.readUTF() };
							setChanged();
							notifyObservers(temp12);

							break;

						case 13:
							String[] temp13 = { Integer.toString(13), in.readUTF() };
							setChanged();
							notifyObservers(temp13);

							break;

						case 14:
							String[] temp14 = { Integer.toString(14) };
							setChanged();
							notifyObservers(temp14);

							break;

						case 20:
							String[] temp20 = { Integer.toString(20) };
							setChanged();
							notifyObservers(temp20);

							break;

						case 10: // Player had sent an answer (or a msg in general)

							Answer answer = new Answer(player, in.readUTF());
							setChanged();
							notifyObservers(answer);
							break;

						case 80: // end game

							String[] temp80 = { Integer.toString(80), Integer.toString(in.readInt()),
									Integer.toString(in.readInt()) };
							setChanged();
							notifyObservers(temp80);

							break;

						}

					} catch (SocketException e) {
						
						String[] temp88 = { Integer.toString(88) };
						setChanged();
						notifyObservers(temp88);

						Thread.currentThread().interrupt();

						

						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
			
			
				 t2 = new Thread(serverInput);
					t2.start();
				
			

		} catch (IOException e) {
			if (isStopped()) {
			
				return;
			}
			throw new RuntimeException("Error accepting client connection", e);
			
			// o tu!
		}

	}

	public synchronized boolean isStopped() {
		return this.isStopped;
	}

	/**
	 * Zatrzymuje serwer
	 * 
	 */
	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Socket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public int getServerPort() {
		return serverPort;
	}

}
