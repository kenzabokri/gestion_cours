package entity;
import java.time.LocalDate;

public class forum {
    private int id;
    private String nomForum;
    private String themeForum;
    private String descriptionForum;
    private LocalDate dateCreationForum;
    private String imageForum;

    // Constructor
    public forum(int id, String nomForum, String themeForum, String descriptionForum, LocalDate dateCreationForum, String imageForum) {
        this.id = id;
        this.nomForum = nomForum;
        this.themeForum = themeForum;
        this.descriptionForum = descriptionForum;
        this.dateCreationForum = dateCreationForum;
        this.imageForum = imageForum;
    }

    public forum() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomForum() {
        return nomForum;
    }

    public void setNomForum(String nomForum) {
        this.nomForum = nomForum;
    }

    public String getThemeForum() {
        return themeForum;
    }

    public void setThemeForum(String themeForum) {
        this.themeForum = themeForum;
    }

    public String getDescriptionForum() {
        return descriptionForum;
    }

    public void setDescriptionForum(String descriptionForum) {
        this.descriptionForum = descriptionForum;
    }

    public LocalDate getDateCreationForum() {
        return dateCreationForum;
    }

    public void setDateCreationForum(LocalDate dateCreationForum) {
        this.dateCreationForum = dateCreationForum;
    }

    public String getImageForum() {
        return imageForum;
    }

    public void setImageForum(String imageForum) {
        this.imageForum = imageForum;
    }

    // toString method (optional)
    @Override
    public String toString() {
        return "Forum{" +
                "id=" + id +
                ", nomForum='" + nomForum + '\'' +
                ", themeForum='" + themeForum + '\'' +
                ", descriptionForum='" + descriptionForum + '\'' +
                ", dateCreationForum=" + dateCreationForum +
                ", imageForum='" + imageForum + '\'' +
                '}';
    }
}