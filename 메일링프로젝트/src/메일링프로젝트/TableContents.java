package 메일링프로젝트;

public class TableContents {
	private int no;
	private boolean reading;
	private String folderName;
	private String subject;
	private String sender;
	private String senderURL;
	private String time;
	private boolean attach;
	private String contents;

	public void setContents(String contents){
		this.contents = contents;
	}
	
	public String getContents(){
		return this.contents;
	}
	
	public void setNo(int no) {
		this.no = no;
	}

	public void setReading(boolean reading) {
		this.reading = reading;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSenderURL(String senderURL) {
		this.senderURL = senderURL;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setAttach(boolean attach) {
		this.attach = attach;
	}

	public int getNo() {
		return this.no;
	}

	public boolean getReading() {
		return this.reading;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getSender() {
		return this.sender;
	}

	public String getSenderURL() {
		return this.senderURL;
	}

	public String getTime() {
		return this.time;
	}

	public boolean getAttach() {
		return this.attach;
	}
}