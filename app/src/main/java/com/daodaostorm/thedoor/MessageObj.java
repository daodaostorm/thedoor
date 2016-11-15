package com.daodaostorm.thedoor;

/**
 * Created by Administrator on 2016/5/12.
 */
public class MessageObj {
    //private String username;
    //private String count;
	private String id;
    private String title;
    private String content;
    //private int icon;
	private String imageUrl;

    public MessageObj(String id, String title, String content, String imageUrl) {
        //this.username = username;
        //this.count = count;
		this.id = id;
        this.title = title;
        this.content = content;
        //this.icon = icon;
		this.imageUrl = imageUrl;
    }


    public String getContent() {
        return content;
    }

	public String getId() {
        return id;
    }
	
    public String getTitle() {
        return title;
    }


	public String getimageUrl() {
        return imageUrl;
    }

}
