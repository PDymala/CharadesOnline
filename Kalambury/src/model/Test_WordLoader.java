package model;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;

/**
 * Klasa testowa metod� JUnit4 klasy WordLoader
 * 
 * @author Katarzyna Samkowska / Piotr Dyma�a
 * @version 1.0
 * @since 2019-01-20
 */

public class Test_WordLoader {
	String realFile = "Slowa.tsv";
	String fakeFile = "idontexist.tsv";
	String emptyFile = "EmptySlowa.tsv";
	String addAndDelTestFile = "SlowaAddTest.tsv";

	
	
	
	/**
	 * Test gdy podano nieistniej�cy plik ze s�owami do gry
	 */

	@Test(expected = SecurityException.class)
	public void noSuchTextFileExist() {
		WordLoader wordLoader = new WordLoader(fakeFile);
		// assertTrue(wordLoader.getWordsForGame(1) == null);
	}

	/**
	 * Test gdy podano istniej�cy plik ze s�owami do gry lecz pusty
	 */
	@Test(expected = NumberFormatException.class)
	public void emptyTextFileButExist() {
		WordLoader wordLoader = new WordLoader(emptyFile);
		
	}

	/**
	 * Test gdy potrzeba do gry wi�cej s��w ni� istnieje w bazie
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void moreWordsNeededThanExist() {
		WordLoader wordLoader = new WordLoader(realFile);
		wordLoader.getRandomWordsForGame(40);

	}

	/**
	 * Test gdy potrzeba do gry okre�lon� ilo�� s��w i sprawdza si� czy klasa j�
	 * zwraca
	 */
	@Test
	public void exactWordsExistInFile() {
		WordLoader wordLoader = new WordLoader(realFile);
		wordLoader.getRandomWordsForGame(3);

		assertTrue(wordLoader.getRandomWordsForGame(3).size() == 3);
	}

	

	/**
	 * Test usuni�cia s�owa z bazy
	 */
	@Test
	public void delWordFromMemory() {

		WordLoader wordLoader = new WordLoader(addAndDelTestFile);
		int beforeDel = wordLoader.getWordDataBase().size();

		wordLoader.deleteWord(wordLoader.getWordDataBase().get(2));
		int afterDel = wordLoader.getWordDataBase().size();

		assertTrue(beforeDel > afterDel);

	}

	/**
	 * Test dodania slowa do bazy
	 */
	@Test
	public void addWordAndCheckSize() {

		WordLoader wordLoader = new WordLoader(addAndDelTestFile);
		int beforeAdd = wordLoader.getWordDataBase().size();


		wordLoader.addWordToDataBase(new Word(wordLoader.getWordDataBase().size()+1, "a", "b", "c", "d"), addAndDelTestFile);

	
		int afterAdd = wordLoader.getWordDataBase().size();

		assertTrue(beforeAdd < afterAdd);

	}

}
