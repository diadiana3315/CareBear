package ro.gnd.solutions.carebear.models;

public class Announcement {
    private String title;
    private String description;
    private String date;

    public Announcement() {
        // Default constructor required for Firebase
    }

    public Announcement(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
