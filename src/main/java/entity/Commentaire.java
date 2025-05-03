package entity;

import java.time.LocalDate;

public class Commentaire {
    private long id;
    private String contenuCommentaire;
    private LocalDate dateCreationCommentaire;
    private int likes;
    private int dislikes;
    private long publicationForumId;

    // Constructor
    public Commentaire(long id, String contenuCommentaire, LocalDate dateCreationCommentaire,
                       int likes, int dislikes, long publicationForumId) {
        this.id = id;
        this.contenuCommentaire = contenuCommentaire;
        this.dateCreationCommentaire = dateCreationCommentaire;
        this.likes = likes;
        this.dislikes = dislikes;
        this.publicationForumId = publicationForumId;
    }

    // Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContenuCommentaire() {
        return contenuCommentaire;
    }

    public void setContenuCommentaire(String contenuCommentaire) {
        this.contenuCommentaire = contenuCommentaire;
    }

    public LocalDate getDateCreationCommentaire() {
        return dateCreationCommentaire;
    }

    public void setDateCreationCommentaire(LocalDate dateCreationCommentaire) {
        this.dateCreationCommentaire = dateCreationCommentaire;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public long getPublicationForumId() {
        return publicationForumId;
    }

    public void setPublicationForumId(long publicationForumId) {
        this.publicationForumId = publicationForumId;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", contenuCommentaire='" + contenuCommentaire + '\'' +
                ", dateCreationCommentaire=" + dateCreationCommentaire +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", publicationForumId=" + publicationForumId +
                '}';
    }
}