package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa ładowania słów z bazy (pliku txt) do programu
 *
 * @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class WordLoader {

	private List<Word> WordDataBase = new ArrayList<Word>();

	public List<Word> getWordDataBase() {
		return WordDataBase;
	}

	/**
	 * Konstruktor- ładuję wszystkie słowa z bazy do pamięci
	 * 
	 * @param plik Plik z hasłami i podpowiedziami
	 */

	public WordLoader(String string) {

		try {
			InputStream in = getClass().getResourceAsStream(string);
			//InputStream in = new FileInputStream(string);

			BufferedReader input = new BufferedReader(new InputStreamReader(in));

			String line = "";
			String cvsSplitBy = "\t";

			while ((line = input.readLine()) != null) {
				// adding words into memory
				// id, word, category, hint1, hint2
				Object[] word = line.split(cvsSplitBy);

				WordDataBase.add(new Word(Integer.valueOf((String) word[0]), (String) word[1], (String) word[2],
						(String) word[3], (String) word[4]));
			}

			input.close();

		} catch (NullPointerException e1 ) {
			throw new SecurityException();
		} catch (NumberFormatException e2) {
			throw new NumberFormatException();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void addWordToDataBase(Word word, String string) {
		BufferedWriter bw = null;

		try {

			File file = new File(string);

			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			// WordDataBase.add(new Word(Integer.valueOf((String) word[0]), (String)
			// word[1], (String) word[2],
			// (String) word[3], (String) word[4]));

			WordDataBase.add(word);

			for (int x = 0; x < WordDataBase.size(); x++) {

				bw.write(WordDataBase.get(x).getId() + "\t" + WordDataBase.get(x).getWord() + "\t"
						+ WordDataBase.get(x).getCategory() + "\t" + WordDataBase.get(x).getHint1() + "\t"
						+ WordDataBase.get(x).getHint2()

				);
				bw.newLine();

			}
			/*
			 * bw.write( word.getId() + "\t" + word.getWord() + "\t" + word.getCategory()+
			 * "\t" + word.getHint1() + "\t" + word.getHint2() );
			 */

			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Zwraca listę słów wylsowanych do gry według podanej ilości
	 *
	 * @param numberOfWords liczba słów którą należy wylosować
	 * @return zwraca ArrayList'ę słów wylosowanych do gry
	 */
	public ArrayList<Word> getRandomWordsForGame(int numberOfWords) {

		ArrayList<Word> tempWordList = new ArrayList<Word>();

		for (int y = 0; y < numberOfWords; y++) {

			Word tempWord = WordDataBase.get((int) (Math.random() * WordDataBase.size()));
			WordDataBase.remove(WordDataBase.indexOf(tempWord));
			tempWordList.add(tempWord);

		}

		return tempWordList;

	}

	public void deleteWord(Word word) {
		WordDataBase.remove(WordDataBase.indexOf(word));
	}

}
