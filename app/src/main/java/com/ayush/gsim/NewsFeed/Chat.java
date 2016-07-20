package com.ayush.gsim.NewsFeed;

/**
 * @author greg
 * @since 6/21/13
 */
public class Chat {

    private String message;
    private String author;
    private long time;
    private String url;
    private String phone;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    Chat(String message, String author, long time, String url, String phone) {
        this.message = message;
        this.author = author;
        this.time = time;
        this.url = url;
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public long getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public String getPhone() {
        return phone;
    }

}
