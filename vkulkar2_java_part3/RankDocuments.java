
public class RankDocuments implements Comparable<RankDocuments>{
	int documentId;
	String filePath, headLine, snippet;
	String similar;
	double rankOf_documents;
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getHeadLine() {
		return headLine;
	}
	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	public String getSimilar() {
		return similar;
	}
	public void setSimilar(String similar) {
		this.similar = similar;
	}
	public double getRankOf_documents() {
		return rankOf_documents;
	}
	public void setRankOf_documents(double rankOf_documents) {
		this.rankOf_documents = rankOf_documents;
	}	
	@Override
	public int compareTo(RankDocuments rank) {
		if(this.rankOf_documents < rank.rankOf_documents)
			return 1;
		if(this.rankOf_documents > rank.rankOf_documents)
			return -1;
		return 0;
	}

}
