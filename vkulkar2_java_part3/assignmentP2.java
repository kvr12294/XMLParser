import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 
 * @author Varun Kulkarni
 *	Main class which reads all the files from a given directory path
 *  and creates required output files. 
 */
public class assignmentP2 {
	private static List<Document> documents = new ArrayList<Document>();
	private static Map<String,Wordcount> dm = new HashMap<String, Wordcount>();
	private static long count;
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.out.println("Too few arguments");
			System.exit(0);
		}
	
		File file = new File(args[0]);
		List<File> file_Paths = new ArrayList<File>();
		
		Set<String> file_names = new HashSet<String>();
		getallFiles(file, file_names, file_Paths);
		
		for(File f : file_Paths) {
			Document doc = new Document(f);
			List<String> tokens = Stemmer.applystemming(f);
			doc.setWordCount(tokens.size());
			documents.add(doc);
			count += tokens.size();
			addTokens(tokens, doc.getDocnumber());
		}
		createDocument();
		createDictionary();
		givetotalcount();
	}
	private static void createDictionary() throws IOException {
		BufferedWriter bw2 = null;
		BufferedWriter bw3 = null;		
		try {
			bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("dictionary.csv")));
			bw3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("postings.csv")));
			List<String> termList = new ArrayList<String>();
			termList.addAll(dm.keySet());
			Collections.sort(termList);

			long offSet = 0;
			for(int i = 0; i < termList.size(); i++) {				
				Wordcount wc = dm.get(termList.get(i));
				bw2.write(wc.getWord() + "," + wc.getCollectionFrequencyOfTerm() + "," + wc.getDf() + "," + offSet + "\n");
				offSet += wc.getDf();
				List<PostingsList> postings = wc.getPostings();
				Collections.sort(postings);
				for(PostingsList p : postings) {
					bw3.write(p.getDocumentId() + "," + p.getTermFrequency() + "\n");
				}
			}
		} finally {
			bw2.flush();bw2.close();bw3.flush();bw3.close();
		}
	}
	/**
	 * 
	 * 	 * @throws IOException
	 * 	This function gives total number of words present in the directory by
	 *  reading all the files of the directory. 
	 */
	private static void givetotalcount() throws IOException {
		BufferedWriter bw = null;
		try{
			long sum = 0;
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("total.txt")));
			List<String> termList = new ArrayList<String>();
			termList.addAll(dm.keySet());
			Collections.sort(termList);
			
			for(int i = 0; i < termList.size(); i++) {				
				Wordcount wc = dm.get(termList.get(i));
				sum += wc.getCollectionFrequencyOfTerm();
			}
			bw.write("Total Number of words: " + sum);
			//bw.write("\nTotal Number of unique words: " + dm.size());
		} finally {
			bw.flush();	bw.close();
		}	
	}
	private static void createDocument() throws IOException {
		BufferedWriter bw1 = null;
		try {
			bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("docsTable.csv")));
			Collections.sort(documents);
			for(Document doc : documents) {
				bw1.write(doc.getDocnumber() + "," + doc.getHeadLine() + "," + doc.getWordCount() + "," + doc.getSnippet() + "," + doc.getFilepath() + "\n");
			}
		} finally {
			bw1.flush();bw1.close();
		}		
	}
	private static void addTokens(List<String> tokens, int documentNumber) {
		for(String term : tokens) {
			Wordcount termDictionary = dm.get(term);
			if(termDictionary == null) {
				termDictionary = new Wordcount(term, documentNumber);

				PostingsList posting = new PostingsList(documentNumber, 1);
				termDictionary.getPostings().add(posting);

				Set<Integer> docIds = new HashSet<Integer>();
				docIds.add(documentNumber);

				dm.put(term, termDictionary);
			} else {
				termDictionary.incrementCollectionFrequency();
				termDictionary.addDocIdOfTerm(documentNumber);

				boolean docPresent = false;
				for(PostingsList p : termDictionary.getPostings()) {
					if(p.getDocumentId() == documentNumber) {
						p.incrementTermFrequency();
						docPresent = true;
						break;
					}
				}

				if(!docPresent) {
					PostingsList newPosting = new PostingsList(documentNumber, 1);
					termDictionary.getPostings().add(newPosting);
				}
			}
		}		
	}
	
	private static void getallFiles(File file_path, Set<String> file_names, List<File> set_files_Paths) {
		if(file_path.isDirectory()) {
			File[] subFiles = file_path.listFiles();
			for (File file : subFiles)
				getallFiles(file, file_names, set_files_Paths);
		} else {
			if(!"".equalsIgnoreCase(file_path.getName()) && !file_names.contains(file_path.getName()))
				set_files_Paths.add(file_path);
		}
		
	}
}