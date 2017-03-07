import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Document implements Comparable<Document> {
	private static int noOf_documents = 0;
	private int docnumber;
	private String docName;
	private String headLine;
	private long wordCount;
	private String snippet;
	private String filepath;

	public Document(String[] words){
		this.docnumber = Integer.parseInt(words[0]);
		this.headLine = words[1];
		this.wordCount = Long.parseLong(words[2]);
		this.snippet = words[3];
		this.filepath = words[4];
	}
	
	public Document(File doc) throws IOException {
		this.docnumber = ++noOf_documents;
		this.docName = doc.getName();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(doc)));
		String line;
		this.filepath = doc.getAbsolutePath();
		while ((line = reader.readLine()) != null) {
			if (line.equalsIgnoreCase("<headline>")) {
				this.headLine = reader.readLine().replaceAll(",", "").trim();
			}
			if (line.equalsIgnoreCase("<text>")) {
					this.snippet = reader.readLine().replaceAll("\\<.*.>","").replaceAll(",", "").trim();
					while(this.snippet != null && this.snippet.split(" ").length < 40) {
						this.snippet += " " + reader.readLine().replaceAll("\\<.*?>", "").replaceAll(",", "").trim();
					}
			}
			if (this.headLine != null && this.snippet != null) {
				break;
			}
		}
		reader.close();
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public int compareTo(Document doc) {
		if (this.docnumber > 1)
			return 1;
		if (this.docnumber < 1)
			return -1;
		return 0;
	}

	public String getDocName() {
		return docName;
	}

	public int getDocnumber() {
		return this.docnumber;
	}

	public String getHeadLine() {
		return headLine;
	}

	public void setHeadLine(String headline) {
		this.headLine = headline;
	}

	public long getWordCount() {
		return wordCount;
	}

	public void setWordCount(long wordCount) {
		this.wordCount = wordCount;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
}
