package cn.com.cimgroup.bean;

import java.io.Serializable;


public class ImageInfo implements Serializable{
	private String imageUrl;
	private String imageId;
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getImageId() {
		return imageId;
	}
	
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	
}
