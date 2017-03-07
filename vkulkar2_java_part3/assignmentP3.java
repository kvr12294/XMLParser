import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author Varun Kulkarni
 *	This is basically a class that is processing a query one by one
 *  as entered. It applies stemming on queries as well and then given the details 
 *  about the queries.
 */
public class assignmentP3 {
	private static final float w = 0.1f;
	private static int collectionCountofword;
	private static List<Document> docList = new ArrayList<Document>();
	private static Map<String, Wordcount> dictionaryMap = new HashMap<String, Wordcount>();

	public static void main(String[] args) {
		BufferedReader br = null;
		BufferedReader br1 = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader("docsTable.csv"));
			String line = "";
			Document new_document = null;
			while ((line = br.readLine()) != null) {
				String[] temp = line.split(",");
				new_document = new Document(temp);
				docList.add(new_document);
			}
			br = new BufferedReader(new FileReader("total.txt"));
			line = br.readLine();
			String[] totalcount = line.split(" ");
			collectionCountofword = Integer.parseInt(totalcount[4]);
			br = new BufferedReader(new FileReader("dictionary.csv"));
			br1 = new BufferedReader(new FileReader("postings.csv"));
			long numdocs;
			String[] temp1, temp2;
			int documentId, term_frequency;
			while ((line = br.readLine()) != null) {
				if ("".equalsIgnoreCase(line.trim())) {
					continue;
				}
				temp1 = line.split(",");
				Wordcount wc = new Wordcount(temp1);
				numdocs = wc.getDf();
				PostingsList p = null;
				List<PostingsList> postingList = new ArrayList<PostingsList>();
				for (int i = 0; i < numdocs; i++) {
					line = br1.readLine();
					if (line != null && !"".equalsIgnoreCase(line.trim())) {
						temp2 = line.split(",");
						documentId = Integer.parseInt(temp2[0]);
						term_frequency = Integer.parseInt(temp2[1]);
						p = new PostingsList(documentId, term_frequency);
						postingList.add(p);
						wc.addDocIdOfTerm(documentId);
					}
				}
				wc.setPostings(postingList);
				dictionaryMap.put(wc.getWord(), wc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		Scanner s1 = null;
		try {
			s1 = new Scanner(System.in);
			System.out.println("Start entering queries:-\n");
			bw = new BufferedWriter(new FileWriter("result.txt"));
			while (true) {
				String entered_query = s1.nextLine();
				if ("EXIT".equals(entered_query)) {
					break;
				} else {
					//apply stemming on query
					String[] processed_query = Stemmer.applystemming(entered_query);
					List<RankDocuments> docsRankList = doc_rank_for_entered_query(processed_query);
					final_results(bw, entered_query, docsRankList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.flush();	bw.close();	s1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
/**
 * 
 * @param bw
 * @param entered_query
 * @param rankList
 * @throws IOException
 * This function writes the final result to a file names result.txt
 * if there are no results then it writes no relevant documents found
 */
	private static void final_results(BufferedWriter bw, String entered_query, List<RankDocuments> rankList)
			throws IOException {
		String final_output = "\nEntered Query is:\n" + entered_query;
		if (rankList.isEmpty()) {
			final_output += "\nRelevant Documents not found:\n";
		}
		else{
			Collections.sort(rankList);
			//Top 5 documents
			List<RankDocuments> final_list = new ArrayList<RankDocuments>();
			if(rankList.size() > 5)
					final_list = rankList.subList(0, 5);
			else 
				final_list = rankList;
			
			for (RankDocuments rankdocuments : final_list) {
				final_output += "\nDocument:\n" + rankdocuments.getFilePath() + "\n";
				final_output += "Headline:\n" + rankdocuments.getHeadLine() + "\n";
				final_output += "Computed Probability : " + rankdocuments.getRankOf_documents() + "\n";
				final_output += "Snippet:\n" + rankdocuments.getSnippet() + "\n";
			}
		}
		bw.write(final_output);
		bw.flush();
	}
/**
 * 
 * @param bw
 * @param processed_query
 * @return
 * @throws IOException
 * This function calculates the required information related to documents and term such as 
 * term frequency, collection frequency from map 
 */
	private static List<RankDocuments> doc_rank_for_entered_query(String[] processed_query) throws IOException {
		List<RankDocuments> list_docs = new ArrayList<RankDocuments>();
		Set<Integer> documents = new TreeSet<Integer>();
		for (String word : processed_query) {
			Wordcount wc = dictionaryMap.get(word);
			if (wc == null) {
				continue;
			}
			List<PostingsList> pos = wc.getPostings();
			for (PostingsList posting : pos) {
				documents.add(posting.getDocumentId());
			}
		}
		for (Integer docId : documents) {
			double rank = 0;
			Document doc = retrievedocs(docId);

			for (String term : processed_query) {
				Wordcount count = dictionaryMap.get(term);
				if (count != null) {
					long collectionFreq = count.getCollectionFrequencyOfTerm();
					int tf = tffromPostingsList(docId, count.getPostings());

					long docWordCount = doc.getWordCount();
					rank += rankofDoc(tf, docWordCount, collectionFreq, collectionCountofword);
				}
			}
			RankDocuments rd = new RankDocuments();
			rd.setDocumentId(docId);
			rd.setHeadLine(doc.getHeadLine());
			rd.setRankOf_documents(rank);
			rd.setSnippet(doc.getSnippet());
			rd.setFilePath(doc.getFilepath());
			list_docs.add(rd);
		}
		return list_docs;
	}

	private static int tffromPostingsList(Integer docId, List<PostingsList> postings) {
		for (PostingsList p : postings) {
			if (docId == p.getDocumentId()) {
				return p.getTermFrequency();}
		}return 0;
	}

	private static Document retrievedocs(Integer docId) {
		for (Document doc : docList) {
			if (doc.getDocnumber() == docId) {
				return doc;}
		}return null;
	}
/**
 * 
 * @param w
 * @param tf
 * @param docWordCount
 * @param collectionFreq
 * @param collectionCountofword
 * @return
 * This function calculates rank by the given formula 
 */
	private static double rankofDoc(double tf, double docWordCount, double collectionFreq,
			double collectionCountofword) {
		double rank = (((1 - w) * (tf / docWordCount)) + w * (collectionFreq / collectionCountofword));
		double finalrank = Math.log(rank) / Math.log(2);
		return finalrank;
	}
}