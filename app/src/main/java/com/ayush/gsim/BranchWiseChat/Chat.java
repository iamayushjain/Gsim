package com.ayush.gsim.BranchWiseChat;

/**
 * @author greg
 * @since 6/21/13
 */
public class Chat {

    private String message;
    private String id;
    private String author;
    private long time;


    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    Chat(String message, String id,String author, long time) {
        this.message = message;
        this.id=id;
        this.author = author;
        this.time = time;

//        this.url = url;
//        this.phone = phone;
//        this.imageUrl = imageUrl;
//        this.authorUrl = authorUrl;
    }

    public String getMessage() {
        return message;
    }
    public String getId() {
        return id;
    }
    public String getAuthor() {
        return author;
    }

    public long getTime() {
        return time;
    }

//    public String getUrl() {
//        return url;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }

}
