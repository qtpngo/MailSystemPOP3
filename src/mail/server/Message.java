package mail.server;

public class Message {
	int messageId;
	String messageName;
	String messageHeader;
	String messageContent;
	boolean isDeleted = false;
	
	

	public Message(int messageId, String messageName, String messageHeader, String messageContent) {
		super();
		this.messageId = messageId;
		this.messageName = messageName;
		this.messageHeader = messageHeader;
		this.messageContent = messageContent;
	}
	
	public String getMessageHeader() {
		return messageHeader;
	}



	public void setMessageHeader(String messageHeader) {
		this.messageHeader = messageHeader;
	}



	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public long getSize() {
		return messageContent.length();
	}

	public void setDeleted(boolean b) {
		this.isDeleted = b;		
	}

	public int getMessageId() {
		return messageId;
	}

}
