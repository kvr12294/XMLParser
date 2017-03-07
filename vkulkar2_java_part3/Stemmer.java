import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Varun Kulkarni
 *	Stemmer class calls fileprocessor which gives a line read from
 *  input file and then applies various stemming rules to get the final 
 *  terms to add in  a dictionary.
 */
public class Stemmer {
	public static String[] applystemming(String entered_query){
		String data = entered_query.replaceAll("\\<.*?>", "").toLowerCase();
		data = data.replaceAll("\\-", " ").replaceAll("\\_", "");
		data = data.replaceAll("[^\\w\\s]", "");
		data = checkstopwords(data);
		data = apply_stemming(data);
		String[] processed_query = finalwords(data);
		return processed_query;
	}
	
	public static List<String> applystemming(File inputfileName) {
		List<String> completeTokens = new ArrayList<String>();
		FileProcessor fp = new FileProcessor(inputfileName);
		String line = "";
		try {
			while ((line = fp.readLineFromFile()) != null) {
				String data = line.replaceAll("\\<.*?>", "").toLowerCase();
				data = data.replaceAll("\\-", " ").replaceAll("\\_", "");
				data = data.replaceAll("[^\\w\\s]", "");
				data = checkstopwords(data);
				data = apply_stemming(data);
				String[] tokens = finalwords(data);
				if (tokens.length != 0) {
					for (String term : tokens) {
						completeTokens.add(term);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return completeTokens;
	}
/**
 * 
 * @param data
 * @return String array with all final words
 */
	private static String[] finalwords(String data) {
		String[] finalwords = data.split(" ");
		List<String> temp = new ArrayList<String>();		
		for (String word : finalwords) {
			if (word.trim().length() > 1) {
				temp.add(word);
			}
		}
		String[] dictionaryWords = new String[temp.size()];
		for(int i = 0; i < temp.size(); i++) {
			dictionaryWords[i] = temp.get(i);
		}
		return dictionaryWords;
	}
/**
 * 
 * @param s_word
 * @return String which is word after applying all stemming rules
 */
	private static String apply_stemming(String s_word) {
		String[] words = s_word.split(" ");
		String new_data = "";

		for (String word : words) {
			if (word.endsWith(".") || word.endsWith(":") || word.endsWith(";") || word.endsWith("?")
					|| word.endsWith("!") || word.endsWith(",")) {
				word = word.substring(0, word.length() - 1);
			}
			if (word.startsWith("(") || word.startsWith("[") || word.startsWith("\"") || word.startsWith("'")
					|| word.startsWith("\"") || word.startsWith("\'")) {
				word = word.substring(1, word.length());
			}

			if (word.endsWith(")") || word.endsWith("]") || word.endsWith("\"") || word.endsWith("'")
					|| word.endsWith("\"") || word.endsWith("\'")) {
				word = word.substring(0, word.length() - 1);
			}
			if (word.matches(".*.'s$|.*.s'$")) {
				if (word.length() > 2)
					word = word.substring(0, word.length() - 2);
			}

			if (word.matches(".*.ies$")
					&& (word.charAt(word.length() - 4) != 'a' && word.charAt(word.length() - 4) != 'e')) {
				if (word.length() > 4) {
					word = word.substring(0, word.length() - 3);
					word += 'y';
				}
			} else if (word.matches(".*.es$") && (word.charAt(word.length() - 3) != 'a'
					&& word.charAt(word.length() - 3) != 'e' && word.charAt(word.length() - 3) != 'o')) {
				if (word.length() > 3) {
					word = word.substring(0, word.length() - 2);
					word += 'e';
				}
			} else if (word.matches(".*.s$")
					&& (word.charAt(word.length() - 2) != 'u' && word.charAt(word.length() - 2) != 's')) {
				if (word.length() > 2) {
					word = word.substring(0, word.length() - 1);
				}
			} else {

			}
			new_data += word + " ";
		}
		return new_data;
	}

	private static String checkstopwords(String data) {
		String[] words = data.split(" ");
		String new_data = "";
		for (String word : words) {
			if (word.length() > 1 && !isStopWord(word)) {
				new_data += word + " ";
			}
		}
		return new_data;
	}
	/**
	 * 
	 * @param word
	 * @return yes or no
	 * just checks for a stop word
	 */

	private static boolean isStopWord(String word) {
		return (word.equalsIgnoreCase("and") || word.equalsIgnoreCase("an") || word.equalsIgnoreCase("by")
				|| word.equalsIgnoreCase("from") || word.equalsIgnoreCase("of") || word.equalsIgnoreCase("the")
				|| word.equalsIgnoreCase("in") || word.equalsIgnoreCase("with") || word.equalsIgnoreCase("a")
				|| word.equalsIgnoreCase("for") || word.equalsIgnoreCase("hence") || word.equalsIgnoreCase("within")
				|| word.equalsIgnoreCase("who") || word.equalsIgnoreCase("when") || word.equalsIgnoreCase("where")
				|| word.equalsIgnoreCase("why") || word.equalsIgnoreCase("how") || word.equalsIgnoreCase("whom")
				|| word.equalsIgnoreCase("have") || word.equalsIgnoreCase("had") || word.equalsIgnoreCase("has")
				|| word.equalsIgnoreCase("not") || word.equalsIgnoreCase("but") || word.equalsIgnoreCase("do")
				|| word.equalsIgnoreCase("does") || word.equalsIgnoreCase("done"));
	}
}
