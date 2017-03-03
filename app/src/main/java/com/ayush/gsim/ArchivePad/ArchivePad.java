package com.ayush.gsim.ArchivePad;

/**
 * @author greg
 * @since 6/21/13
 */
public class ArchivePad {

    private String name;
    private String link;
    private String session;
    private String semester;
    private String imageUrl;
    private  String authorUrl;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private ArchivePad() {
    }

    ArchivePad(String name, String link, String session, String semester) {
        this.name = name;
        this.link = link;
        this.session = session;
        this.semester =semester;

    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
    public String getSession() {
        return session;
    }



    public String getSemester() {
        return semester;
    }
}
