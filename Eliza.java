
//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           Eliza BP2 
// Files:           
// Course:          CS200 , First Term, and 2018
//
// Author:          Amber Zedeck
// Email:           zedeck@wisc.edu
// Lecturer's Name: Renault
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates 
// strangers, etc do.  If you received no outside help from either type of 
// source, then please explicitly indicate NONE.
//
// Persons:         Drop in Tutoring 1289
// Online Sources:  (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * The Eliza class holds the user input and response formation for a system that
 * collects user input and responds appropriately. Eliza is based off of a
 * computer program written at MIT in the 1960's by Joseph Weizenbaum. Eliza
 * uses keyword matching to respond to users in a way that displays interest in
 * the users and continues the conversation until instructed otherwise.
 */
public class Eliza {

	/*
	 * This method does input and output with the user. It calls supporting methods
	 * to read and write files and process each user input.
	 * 
	 * @param args (unused)
	 */
	public static void main(String[] args) {
		// Milestone 2
		Scanner userInput = new Scanner(System.in);
		Random random = new Random(Config.SEED);

//		
		// create a scanner for reading user input and a random number
		// generator with Config.SEED as the seed

		// Milestone 3
		// How the program starts depends on the command-line arguments.
		// Command-line arguments can be names of therapists for example:
		// Eliza Joe Laura
		// If no command-line arguments then the therapists name is Eliza
		// and this reads a file with that name and the Config.RESPONSE_FILE_EXTENSION.
		// Example filename: Eliza.rsp
		// If only one command-line argument, then read the responses from
		// the corresponding file with Config.RESPONSE_FILE_EXTENSION.
		// If there is more than one command-line argument then offer them
		// as a list of people to talk with. For the three therapists above the prompt
		// is
		// "Would you like to speak with Eliza, Joe, or Laura?"
		// When a user types a name then read the responses from the file which
		// is the selected name and Config.RESPONSE_FILE_EXTENSION extension.
		// Whatever name the user types has the extension appended and
		// is read using loadResponseTable. If loadResponseTable can't load
		// the file then it will report an error.

		// Milestone 2
		// name prompt
		File file;
		String therapistName = "Eliza"; // If there is no command-line arguments, Eliza is the default


		ArrayList<String> dialog = new ArrayList<String>(); //ArrayList with prompts, userInputs, and responses
		if (args.length == 1) { //If command line has one argument
			therapistName = args[0];

		}

		if (args.length > 1) { // if the command line has more than one argument
			System.out.print("Would you like to speak with ");
			for (int i = 0; i < args.length-1 ; i++) {
				System.out.print(args[i] + ", ");

			}
			System.out.print("or " + args[args.length - 1] + "?");
			therapistName = userInput.nextLine();
		}
		file = new File(therapistName + Config.RESPONSE_FILE_EXTENSION); //Creating new file extension specific to the name of therapist
		String intro = "Hi I'm " + therapistName + ", what is your name?";
		dialog.add(intro); //Adds string to dialog arrayList
		System.out.println(intro);
		String name = userInput.nextLine();
		dialog.add(name); //Adds string to dialog arrayList
		String introAfterName = "Nice to meet you " + name + ". What is on your mind?";
		dialog.add(introAfterName);
		System.out.println(introAfterName);

		while (userInput.hasNextLine()) {

			String onMind = userInput.nextLine();
			dialog.add(onMind); //Adds string to dialog arrayList

			ArrayList<String> separateInput = new ArrayList<String>();
			separateInput = separatePhrases(onMind);

			if (foundQuitWord(separateInput)) {
				break;
			} else {
				String[] input = prepareInput(onMind);
				String preparedResponse = prepareResponse(input, random, loadResponseTable("Eliza.rsp"));
				System.out.println(preparedResponse);
				dialog.add(preparedResponse);//Adds string to dialog arrayList
				
			}

		}
		String preferredFile = ""; 
		System.out.println("Goodbye " + name);
		boolean endMessage = true; // For correct print statement and break of loop
		do {
			System.out.print("Would you like to have a record of our conversation (y/n): ");
			String saveResponse = userInput.nextLine();
			if (saveResponse.equals("y")) {
				System.out.print("Enter filename:");
				preferredFile = userInput.nextLine();
				try {
					saveDialog(dialog, preferredFile);
					break;
				} catch (IOException e) {					
					
					System.out.println("Unable to save conversation to: " + preferredFile);
				}
			
			}else {
				endMessage = false; // If "n"  break out of loop
				break;
			}
			
		}while(true);
		if(endMessage) { // if true, print
			System.out.println("Thanks again for talking! Our conversation is saved in: " + preferredFile);
		}
	
	

	// Milestone 3
	// Save all conversation (user and system responses) starting
	// with this program saying "Hi I'm..." and concludes with
	// "Goodbye <name>.".
	// Always prompt the user to see if they would like to save a
	// record of the conversation. If the user enters a y or Y as the
	// first non-whitespace character then prompt for filename and save,
	// otherwise don't save dialog. After successfully saving a dialog
	// print the message "Thanks again for talking! Our conversation is saved in:
	// <filename>".
	// If saveDialog throws an IOException then catch it, print out the error:
	// "Unable to save conversation to: " <name of file>
	// Repeat the code prompting the user if they want to save the dialog.

	}

	/**
	 * This method processes the user input, returning an ArrayList containing
	 * Strings, where each String is a phrase from the user's input. This is done by
	 * removing leading and trailing whitespace, making the user's input all lower
	 * case, then going through each character of the user's input. When going
	 * through each character this keeps all digits, alphabetic characters and '
	 * (single quote). The characters ? ! , . signal the end of a phrase, and
	 * possibly the beginning of the next phrase, but are not included in the
	 * result. All other characters such as ( ) - " ] etc. should be replaced with a
	 * space. This method makes sure that every phrase has some visible characters
	 * but no leading or trailing whitespace and only a single space between words
	 * of a phrase. If userInput is null then return null, if no characters then
	 * return a 0 length list, otherwise return a list of phrases. Empty phrases and
	 * phrases with just invalid/whitespace characters should NOT be added to the
	 * list.
	 * 
	 * Example userInput: "Hi, I am! a big-fun robot!!!" Example returned: "hi", "i
	 * am", "a big fun robot"
	 * 
	 * @param userInput text the user typed
	 * @return the phrases from the user's input
	 */
	public static ArrayList<String> separatePhrases(String userInput) {
		ArrayList<String> phrases = new ArrayList<String>();

		if (userInput == null) {
			return null;
		}

		if (userInput == "") {
			return phrases;
		}

		String[] userPhrase = userInput.split("[?!,.]"); //Creates Array by splitting phrase at indicated symbols

		for (int i = 0; i < userPhrase.length; i++) { //Iterates through userPhrase
			userPhrase[i] = userPhrase[i].toLowerCase();
			userPhrase[i] = userPhrase[i].replaceAll(" +", " ");
			userPhrase[i] = userPhrase[i].trim();
			for (int j = 0; j < userPhrase[i].length(); j++) { //Iterates through the each word in the Phrase
				if (!Character.isLetter(userPhrase[i].charAt(j)) && !Character.isDigit(userPhrase[i].charAt(j)) // If the Character is something other than a letter, a digit, or one of the indicated symbols
						&& (userPhrase[i].charAt(j) != '?') && (userPhrase[i].charAt(j) != '!')						// replace it with a space
						&& (userPhrase[i].charAt(j) != ',') && (userPhrase[i].charAt(j) != '.')
						&& (userPhrase[i].charAt(j) != '\'')) {
					userPhrase[i] = userPhrase[i].replace(userPhrase[i].charAt(j), ' ');
					
				}
			}
			if (!userPhrase[i].equals(" ") && !userPhrase[i].equals("")) { 
				userPhrase[i] = userPhrase[i].trim(); 
				phrases.add(userPhrase[i]);// Add userPhrase to Phrase Array

			}

		}

		return phrases;
	}

	/**
	 * Checks whether any of the phrases in the parameter match a quit word from
	 * Config.QUIT_WORDS. Note: complete phrases are matched, not individual words
	 * within a phrase.
	 * 
	 * @param phrases List of user phrases
	 * @return true if any phrase matches a quit word, otherwise false
	 */
	public static boolean foundQuitWord(ArrayList<String> phrases) {
		int i;
		boolean isQuitWord = false; 

		for (i = 0; i < Config.QUIT_WORDS.length; ++i) { 
			for (int j = 0; j < phrases.size(); j++)//Iterates through ArrayList 
				if (phrases.get(j).equals(Config.QUIT_WORDS[i])) { //If a quit word is found, change value of boolean to True
					isQuitWord = true;

				}
		}
  
		return isQuitWord;
	}

	/**
	 * Iterates through the phrases of the user's input, finding the longest phrase
	 * to which to respond. If two phrases are the same length, returns whichever
	 * has the lower index in the list. If phrases parameter is null or size 0 then
	 * return null.
	 * 
	 * @param phrases List of user phrases
	 * @return the selected phrase
	 */
	public static String selectPhrase(ArrayList<String> phrases) {
		String longestPhrase = "";
		
		if (phrases.size() == 0 || phrases == null) {
			return "";
		}
		for (int i = 0; i < phrases.size(); i++) { //Iterates through the Phrases arrayList
			if (phrases.get(i).length() > longestPhrase.length()) { //Compares lengths
				longestPhrase = phrases.get(i); // Assigns longest Phrase 

			} else if (phrases.get(i).length() == longestPhrase.length()) { //If they are equals
				if (i < phrases.indexOf(longestPhrase)) { //Return phrase of lower index
					longestPhrase = phrases.get(i);
				}
			}

		}

		return longestPhrase;
	}

	/**
	 * Looks for a replacement word for the word parameter and if found, returns the
	 * replacement word. Otherwise if the word parameter is not found then the word
	 * parameter itself is returned. The wordMap parameter contains rows of match
	 * and replacement strings. On a row, the element at the 0 index is the word to
	 * match and if it matches return the string at index 1 in the same row. Some
	 * example word maps that will be passed in are Config.INPUT_WORD_MAP and
	 * Config.PRONOUN_MAP.
	 * 
	 * If word is null return null. If wordMap is null or wordMap length is 0 simply
	 * return word parameter. For this implementation it is reasonable to assume
	 * that if wordMap length is >= 1 then the number of elements in each row is at
	 * least 2.
	 * 
	 * @param word    The word to look for in the map
	 * @param wordMap The map of words to look in
	 * @return the replacement string if the word parameter is found in the wordMap
	 *         otherwise the word parameter itself.
	 */
	public static String replaceWord(String word, String[][] wordMap) {
		String replacementWord = word;

		if (word == null) {
			return null;
		}

		if (wordMap == null) {
			return word;
		}
		if (wordMap.length == 0) {
			return word;
		}

		for (int i = 0; i < wordMap.length; i++) { //Iterates through wordsMap
			if (wordMap[i][0].equals(word)) { //If a word in wordMap is equal to word
				replacementWord = wordMap[i][1]; //replace word
				return replacementWord;
			}
		}

		return replacementWord;

	}

	/**
	 * Concatenates the elements in words parameter into a string with a single
	 * space between each array element. Does not change any of the strings in the
	 * words array. There are no leading or trailing spaces in the returned string.
	 * 
	 * @param words a list of words
	 * @return a string containing all the words with a space between each.
	 */
	public static String assemblePhrase(String[] words) {

		String stringWords = "";

		if (words == null || words.length == 0) {
			return stringWords;
		}
		for (int i = 0; i < words.length - 1; i++) { //Iterates through words Array
			words[i] = words[i].trim();
			stringWords = stringWords.concat(words[i]); 
			stringWords = stringWords + " ";
		}
		stringWords = stringWords.concat(words[words.length - 1]); 
		words[words.length - 1] = words[words.length - 1].trim();
		
		return stringWords;
	}

	/**
	 * Replaces words in phrase parameter if matching words are found in the mapWord
	 * parameter. A word at a time from phrase parameter is looked for in wordMap
	 * which may result in more than one word. For example: i'm => i am Uses the
	 * replaceWord and assemblePhrase methods. Example wordMaps are
	 * Config.PRONOUN_MAP and Config.INPUT_WORD_MAP. If wordMap is null then phrase
	 * parameter is returned. Note: there will Not be a case where a mapping will
	 * itself be a key to another entry. In other words, only one pass through
	 * swapWords will ever be necessary.
	 * 
	 * @param phrase  The given phrase which contains words to swap
	 * @param wordMap Pairs of corresponding match & replacement words
	 * @return The reassembled phrase
	 */
	public static String swapWords(String phrase, String[][] wordMap) {
		String replacementPhrase = "";
		
		if (wordMap == null) {
			return phrase;
		}

		String separatePhrase[] = phrase.split(" "); //Creates String Array by splitting phrase at spaces
		for (int i = 0; i < separatePhrase.length; i++) { //Iterates through separate Phrases
			separatePhrase[i] = replaceWord(separatePhrase[i], wordMap);
		}

		replacementPhrase = assemblePhrase(separatePhrase); //Calls assemblePhrases 
		return replacementPhrase;
	}

	/**
	 * This prepares the user input. First, it separates input into phrases (using
	 * separatePhrases). If a phrase is a quit word (foundQuitWord) then return
	 * null. Otherwise, select a phrase (selectPhrase), swap input words (swapWords
	 * with Config.INPUT_WORD_MAP) and return an array with each word its own
	 * element in the array.
	 * 
	 * @param input The input from the user
	 * @return words from the selected phrase
	 */
	public static String[] prepareInput(String input) {
		ArrayList<String> inputArray = new ArrayList<String>();
		inputArray = separatePhrases(input);
		
		if (inputArray == null) {
			return null;
		}
		String selectedPhrase = "";
		String wordsSwapped = "";

		if (foundQuitWord(inputArray)) {
			return null;
		}

		selectedPhrase = selectPhrase(inputArray); //Calls selectPhrase 

		wordsSwapped = swapWords(selectedPhrase, Config.INPUT_WORD_MAP); //Calls swapWords

		String[] swappedWordsArray = wordsSwapped.split(" "); //Creates String Array by splitting wordSwapped at the spaces
		return swappedWordsArray;

	}

	/**
	 * Reads a file that contains keywords and responses. A line contains either a
	 * list of keywords or response, any blank lines are ignored. All leading and
	 * trailing whitespace on a line is ignored. A keyword line begins with
	 * "keywords" with all the following tokens on the line, the keywords. Each line
	 * that follows a keyword line that is not blank is a possible response for the
	 * keywords. For example (the numbers are for our description purposes here and
	 * are not in the file):
	 * 
	 * 1 keywords computer 2 Do computers worry you? 3 Why do you mention computers?
	 * 4 5 keywords i dreamed 6 Really, <3>? 7 Have you ever fantasized <3> while
	 * you were awake? 8 9 Have you ever dreamed <3> before?
	 *
	 * In line 1 is a single keyword "computer" followed by two possible responses
	 * on lines 2 and 3. Line 4 and 8 are ignored since they are blank (contain only
	 * whitespace). Line 5 begins new keywords that are the words "i" and "dreamed".
	 * This keyword list is followed by three possible responses on lines 6, 7 and
	 * 9.
	 * 
	 * The keywords and associated responses are each stored in their own ArrayList.
	 * The response table is an ArrayList of the keyword and responses lists. For
	 * every keywords list there is an associated response list. They are added in
	 * pairs into the list that is returned. There will always be an even number of
	 * items in the returned list.
	 * 
	 * Note that in the event an IOException occurs when trying to read the file
	 * then an error message "Error reading <fileName>", where <fileName> is the
	 * parameter, is printed and a non-null reference is returned, which may or may
	 * not have any elements in it.
	 * 
	 * @param fileName The name of the file to read
	 * @return The response table
	 */
	public static ArrayList<ArrayList<String>> loadResponseTable(String fileName) { 
		Scanner scnr = null;
		ArrayList<ArrayList<String>> keywordsAndResponses = new ArrayList<ArrayList<String>>();
		
		
		
		try {  //Tries to read from file
			File file = new File(fileName);
			scnr = new Scanner(file); 
			while (scnr.hasNextLine()) {
				String curLine = scnr.nextLine().trim();
				if (curLine.isEmpty()) {
					continue;
				}
				if (curLine.indexOf("keywords") != -1) { 
					ArrayList<String> keys = new ArrayList<String>();
					ArrayList<String> responses = new ArrayList<String>();
					String[] keywords = curLine.split(" "); //Creates ArrayList  by splitting the curLine at the spaces
					for (int i = 1; i < keywords.length; i++) { //Iterates through keyWords
						keys.add(keywords[i]); //Adds keywords to key arrayList
					}
					keywordsAndResponses.add(keys); // adds keys to arrayList of keys and responses
					keywordsAndResponses.add(responses); // adds responses to arrayList of keys and responses
				} else {
					keywordsAndResponses.get(keywordsAndResponses.size() - 1).add(curLine);
				}
			}

		} catch (FileNotFoundException e) { //If the file cannot be found
			System.out.print("Error reading: " + fileName);
		} finally { //Always reached 
			if (scnr != null)
				scnr.close();
		}
		return keywordsAndResponses;

	}

	/**
	 * Checks to see if the keywords match the sentence. In other words, checks to
	 * see that all the words in the keyword list are in the sentence and in the
	 * same order. If all the keywords match then this method returns an array with
	 * the unmatched words before, between and after the keywords. If the keywords
	 * do not match then null is returned.
	 * 
	 * When the phrase contains elements before, between, and after the keywords,
	 * each set of the three is returned in its own element String[] keywords =
	 * {"i", "dreamed"}; String[] phrase = {"do", "you", "know", that", "i", "have",
	 * "dreamed", "of", "being", "an", "astronaut"};
	 * 
	 * toReturn[0] = "do you know that" toReturn[1] = "have" toReturn[2] = "of being
	 * an astronaut"
	 * 
	 * In an example where there is a single keyword, the resulting List's first
	 * element will be the the pre-sequence element and the second element will be
	 * everything after the keyword, in the phrase String[] keywords = {"always"};
	 * String[] phrase = {"I", "always", "knew"};
	 * 
	 * toReturn[0] = "I" toReturn[1] = "knew"
	 * 
	 * In an example where a keyword is not in the phrase in the correct order, null
	 * is returned. String[] keywords = {"computer"}; String[] phrase = {"My","dog",
	 * "is", "lost"};
	 * 
	 * return null
	 * 
	 * @param keywords The words to match, in order, in the sentence.
	 * @param phrase   Each word in the sentence.
	 * @return The unmatched words before, between and after the keywords or null if
	 *         the keywords are not all matched in order in the phrase.
	 */
	public static String[] findKeyWordsInPhrase(ArrayList<String> keywords, String[] phrase) {
		String[] unmatchedArray = new String[keywords.size() + 1]; //Array size of keywords-1 according to Algorithm
		ArrayList<String> listKeywords = new ArrayList<String>();
		int count = 0; //Count of keywords in phrase
		
		for (int j = 0; j < unmatchedArray.length; j++) { //Iterates through unmatchedArray and creates "" in each element
			unmatchedArray[j] = "";
		}
		if (keywords.size() == 0) { 
			unmatchedArray[0] = assemblePhrase(phrase); //Calls assemble phrases on unmatchedArray [0] if there is no keywords
			return unmatchedArray;
		}
		String[] keywordsArray = keywords.toArray(new String[keywords.size()]); //Converts keywords to an Array
		for (int k = 0; k < phrase.length; k++) { //Iterates through phrase
			if (phrase[k].equals("")) { //if the element is equal to empty string, increment index
				continue;
			}
			for (int l = 0; l < keywordsArray.length; l++) { //iterates through keywordsArray
				if (keywordsArray[l].equals(phrase[k])) { // if there is a keyword in phrase
					count++;// increment count
					listKeywords.add(keywordsArray[l]); //add that word to arrayList
				}
			}
		}
		if (count != keywords.size()) { //Ensuring the number of keywordsinPhrase is equal to the amount of keywords in keyWords array
			return null;
		}
		for (int i = 0; i < keywordsArray.length; i++) { //Iterates through keywordsArray
			if (!keywordsArray[i].equals(listKeywords.get(i))) { //Make sure all words are in the correct order
				return null;
			}
		}

		int currentIndex = 0; //
		for (int i = 0; i < phrase.length; i++) { //Iterates through phrase 
			if (currentIndex < keywordsArray.length && phrase[i].equals(keywordsArray[currentIndex])) {  //If phrase at index is equal to keywords at Index 
				currentIndex++; //increase index
			} else { //If the words are not equal
				if (unmatchedArray[currentIndex].equals("")) {  
					unmatchedArray[currentIndex] = phrase[i].trim(); 
 				} else {
					unmatchedArray[currentIndex] = unmatchedArray[currentIndex] + " " + phrase[i].trim(); //Puts word into same array element as the previous word if it ws not empty
				}
			}
		}

		return unmatchedArray;
	}

	/**
	 * Selects a randomly generated response within the list of possible responses
	 * using the provided random number generator where the number generated
	 * corresponds to the index of the selected response. Use Random nextInt(
	 * responseList.size()) to generate the random number. If responseList is null
	 * or 0 length then return null.
	 * 
	 * @param rand         A random number generator.
	 * @param responseList A list of responses to choose from.
	 * @return A randomly selected response
	 */
	public static String selectResponse(Random rand, ArrayList<String> responseList) {
		if (responseList == null || responseList.size() == 0) {
			return null;
		}
		
		int index = rand.nextInt(responseList.size());
		String randomResponse = responseList.get(index);

		return randomResponse;
	}

	/**
	 * This method takes processed user input and forms a response. This looks
	 * through the response table in order checking to see if each keyword pattern
	 * matches the userWords. The first matching keyword pattern found determines
	 * the list of responses to choose from. A keyword pattern matches the
	 * userWords, if all the keywords are found, in order, but not necessarily
	 * contiguous. This keyword matching is done by findKeyWordsInPhrase method. See
	 * the findKeyWordsInPhrase algorithm in the Eliza.pdf.
	 * 
	 * If no keyword pattern matches then Config.NO_MATCH_RESPONSE is returned.
	 * Otherwise one of possible responses for the matched keywords is selected with
	 * selectResponse method. The response selected is checked for the replacement
	 * symbol <n> where n is 1 to the length of unmatchedWords array returned by
	 * findKeyWordsInPhrase. For each replacement symbol the corresponding unmatched
	 * words element (index 0 for <1>, 1 for <2> etc.) has its pronouns swapped with
	 * swapWords using Config.PRONOUN_MAP and then replaces the replacement symbol
	 * in the response.
	 * 
	 * @param userWords     using input after preparing.
	 * @param rand          A random number generator.
	 * @param responseTable A table containing a list of keywords and response
	 *                      pairs.
	 * @return The generated response
	 */
	public static String prepareResponse(String[] userWords, Random rand, ArrayList<ArrayList<String>> responseTable) {
		String finalResponse = "";
		String[] match = null; //Match keywords in Phrase
		ArrayList<String> responseList = new ArrayList<String>();
		
		for (int i = 0; i < responseTable.size(); i += 2) { //Iterates through responseTable
			match = findKeyWordsInPhrase(responseTable.get(i), userWords); //
			responseList = responseTable.get(i + 1);
			if (match != null) {
				break;
			}
		}
		if (match == null) {
			return Config.NO_MATCH_RESPONSE;
		} else {
			finalResponse = selectResponse(rand, responseList); //If there was a matching keyword, select a random response

			for (int j = 0; j < match.length; j++) { //Iterates through match
				String number = "<" + (j + 1) + ">"; 
				if (finalResponse.contains(number)) { //
					String phraseWithPronouns = swapWords(match[j], Config.PRONOUN_MAP); //Calls swap words
					finalResponse = finalResponse.replace(number, phraseWithPronouns);  // Creates final response with all swapped words

				}
			}
		}

		// Iterate through the response table.
		// The response table has paired rows. The first row is a list of key
		// words, the next a list of corresponding responses. The 3rd row another
		// list of keywords and 4th row the corresponding responses.

		// checks to see if the current keywords match the user's words
		// using findKeyWordsInPhrase.

		// if no keyword pattern was matched, return Config.NO_MATCH_RESPONSE
		// else, select a response using the appropriate list of responses for the
		// keywords

		// Look for <1>, <2> etc in the chosen response. The number starts with 1 and
		// there won't be more than the number of elements in unmatchedWords returned by
		// findKeyWordsInPhrase. Note the number of elements in unmatchedWords will be
		// 1 more than the number of keywords.
		// Foreach <n> found, find the corresponding unmatchedWords phrase (n-1) and
		// swap
		// its pronoun words (swapWords using Config.PRONOUN_MAP). Then use the
		// result to replace the <n> in the chosen response.

		// in the selected echo, swap pronouns

		// inserts the new phrase with pronouns swapped, into the response
		return finalResponse;

	}

	/**
	 * Creates a file with the given name, and fills that file line-by-line with the
	 * tracked conversation. Every line ends with a newline. Throws an IOException
	 * if a writing error occurs.
	 * 
	 * @param dialog   the complete conversation
	 * @param fileName The file in which to write the conversation
	 * @throws IOException
	 */
	public static void saveDialog(ArrayList<String> dialog, String fileName) throws IOException {
		File file = new File(fileName); //Creates new File
		PrintWriter writer = new PrintWriter(file); //Creates PrintWriter with file 
		for (String elizaDialog : dialog) { //Iterates through dialog and adds each line to file
			writer.println(elizaDialog); //Prints each line
		}
		writer.close(); //closes printWriter
	}

}