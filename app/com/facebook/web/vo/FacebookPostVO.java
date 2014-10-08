/**
 * 
 */
package com.facebook.web.vo;

/**
 * @author vinod
 *
 */
public class FacebookPostVO {
	
	private String from; //name of the merchant
	private String message;
	private String picture;
	private String link;
	private String type;
	private String icon;
	private String created_Date;
	private String created_time;
	private String updated_Date;
	private String updated_time;
	
	public FacebookPostVO() {
		
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCreated_Date() {
		return created_Date;
	}

	public void setCreated_Date(String created_Date) {
		this.created_Date = created_Date;
	}

	public String getUpdated_Date() {
		return updated_Date;
	}

	public void setUpdated_Date(String updated_Date) {
		this.updated_Date = updated_Date;
	}

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getCreated_time() {
		return created_time;
	}
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}
	public String getUpdated_time() {
		return updated_time;
	}
	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}
	@Override
	public String toString() {
		return "FacebookPostVO [from=" + from + ", message=" + message
				+ ", picture=" + picture + ", link=" + link + ", type=" + type
				+ ", created_Date=" + created_Date + ", created_time=" + created_time + ", updated_Date="+ updated_Date + ",updated_time="
				+ updated_time + "]";
	}
	
}
