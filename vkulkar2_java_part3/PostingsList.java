
public class PostingsList implements Comparable<PostingsList> {
	int documentId;
	int termFrequency;

	public PostingsList(int docId, int termFreq) {
		this.documentId = docId;
		this.termFrequency = termFreq;
	}

	public int compareTo(PostingsList p1) {
		if (this.documentId < p1.getDocumentId())
			return -1;
		if (this.documentId > p1.getDocumentId())
			return 1;
		return 0;
	}
	
	public void incrementTermFrequency() {
		this.termFrequency++;
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public int getTermFrequency() {
		return termFrequency;
	}

	public void setTermFrequency(int termFrequency) {
		this.termFrequency = termFrequency;
	}

}
