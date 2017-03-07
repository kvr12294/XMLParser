import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Wordcount {
	private int count;
	private String word;
	private long collectionFrequencyOfTerm;
	private Set<Integer> docIdsOfTerm = new HashSet<Integer>();
	private int offset;
	private long df; 
	private List<PostingsList> postings = new ArrayList<PostingsList>();

	public Wordcount(String term, int docId) {
		this.word = term;
		this.collectionFrequencyOfTerm++;
		this.docIdsOfTerm.add(docId);
		this.offset = count++;
		this.df++;
	}
	public Wordcount(String[] words) {
		this.word = words[0];
		this.collectionFrequencyOfTerm = Long.parseLong(words[1]);
		this.df = Long.parseLong(words[2]);
		this.offset = Integer.parseInt(words[3]);
	}

	public void incrementCollectionFrequency() {
		this.collectionFrequencyOfTerm++;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String term) {
		this.word = term;
	}

	public long getCollectionFrequencyOfTerm() {
		return collectionFrequencyOfTerm;
	}

	public void setCollectionFrequencyOfTerm(long collectionFrequencyOfTerm) {
		this.collectionFrequencyOfTerm = collectionFrequencyOfTerm;
	}

	public long getDf() {
		//System.out.println(docIdsOfTerm.size() == this.documentFrequencyOfTerm);
		//return this.documentFrequencyOfTerm;
		if(!docIdsOfTerm.isEmpty()) {
			return docIdsOfTerm.size();
		}
		return df;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List<PostingsList> getPostings() {
		return postings;
	}

	public void setPostings(List<PostingsList> postings) {
		this.postings = postings;
	}

	public void addDocIdOfTerm(int documentNumber) {
		this.docIdsOfTerm.add(documentNumber);
		this.offset++;
	}
}