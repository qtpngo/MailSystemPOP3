package utilities;

import java.io.Serializable;

public class MessageListAdapter implements Serializable{
	private static final long serialVersionUID = 1L;
	String from, time, subject, content, attachment;

	public MessageListAdapter(String from, String time, String subject, String content, String attachment) {
		super();
		this.from = from;
		this.time = time;
		this.subject = subject;
		this.content = content;
		this.attachment = attachment;
	}

	public MessageListAdapter() {
		super();
	}
	
	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
