package net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

import model.Answer;
import model.Player;

/**
 * Klasa serwera - łączy z klientem oraz oczekuje na informacje od niego.
 * Kolejno je przekazuje do kontrolera.
 *
 * @author Piotr Dymala / p.dymala@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class Server extends Observable {

	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	private int serverPort = 8080;
	private OutputStream output;
	private InputStream input;
	private DataOutputStream out;
	private DataInputStream in;
	private Socket clientSocket;
	private Player player;

	/**
	 * Konstruktor serwera. Oczekuje na połączenie od klienta, oczekuje na
	 * informacje od niego i przekazuje je do kontrolera
	 * 
	 * @param port     PORT nasluchiwania serwera
	 * @param hostName imie Host'a
	 */
	public Server(int port, String hostName) {
		serverPort = port;
		openServerSocket();

		try {
			clientSocket = this.serverSocket.accept();

			Runnable serverInput = () -> {

				while (clientSocket != null && clientSocket.isClosed() == false
						&& !Thread.currentThread().isInterrupted()) {

					try {
						input = clientSocket.getInputStream();
						in = new DataInputStream(input);

						int messageType = 0;
						if (in.available() > 0 && ((messageType = in.readInt()) == 0))
							break;

						switch (messageType) {

						case 1: // Hello! player has connected.

							setChanged();
							notifyObservers(in.readUTF());

							sendMsg((int) 1);
							sendMsg(hostName);

							break;

						case 10: // Player had sent an answer (or a msg in general)

							Answer answer = new Answer(player, in.readUTF());
							setChanged();
							notifyObservers(answer);

						case 99: // Player had sent an answer (or a msg in general)

							setChanged();
							notifyObservers(99);

							break;

						case 80: // end game

							break;
						}

					} catch (SocketException e) {

						setChanged();
						notifyObservers(88);
						// cos aby pokazalo ze sie skonczylo
						Thread.currentThread().interrupt();
						return;

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
			Thread t2 = new Thread(serverInput);
			t2.start();

		} catch (IOException e) {
			if (isStopped()) {

				return;
			}
			throw new RuntimeException("Error accepting client connection", e);
		}

	}

	public int getServerPort() {
		return serverPort;
	}

	/**
	 * Wysyła informacje do klienta Informacje wysylamy i dostajemy w dw�ch
	 * czesciach. W pierwszej typu int/byte, okreslany jest typ wiadomosci np, 0 -
	 * hello msg 10 - wiadomosc/odpowiedz na pytanie 99 - koniec gry
	 * 
	 * W drugiej czesci jest wartosc tej informacji, np. tresc wiadomosci. Czyli
	 * tekst czatu wysylamy: sendMsg(0); sendMsg("czesc!")
	 * 
	 * @param msg Informacja do wysłania. Int albo String
	 * 
	 * 
	 * 
	 */
	public void sendMsg(Object msg) {

		// checking if socket exist
		if (clientSocket != null && clientSocket.isClosed() == false) {
			try {
				output = clientSocket.getOutputStream();
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

				// out.flush();

				// Thread.sleep(50);
			} catch (SocketException e) {

				setChanged();
				notifyObservers(88);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

		}

	}

	public synchronized boolean isStopped() {
		return this.isStopped;
	}

	/**
	 * Zatrzymuje serwer
	 * 
	 * 
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

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port", e);
		}
	}
}
