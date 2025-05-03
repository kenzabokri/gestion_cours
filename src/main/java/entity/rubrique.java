package entity;

import java.time.LocalDate;

public class rubrique {
    private int id;
    private String nomRubrique;
    private String descriptionRubrique;
    private LocalDate dateCreationRubrique;
    private int forumId;

    public rubrique(int id, String nomRubrique, String descriptionRubrique, LocalDate dateCreationRubrique, int forumId) {
        this.id = id;
        this.nomRubrique = nomRubrique;
        this.descriptionRubrique = descriptionRubrique;
        this.dateCreationRubrique = dateCreationRubrique;
        this.forumId = forumId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomRubrique() {
        return nomRubrique;
    }

    public void setNomRubrique(String nomRubrique) {
        this.nomRubrique = nomRubrique;
    }

    public String getDescriptionRubrique() {
        return descriptionRubrique;
    }

    public void setDescriptionRubrique(String descriptionRubrique) {
        this.descriptionRubrique = descriptionRubrique;
    }

    public LocalDate getDateCreationRubrique() {
        return dateCreationRubrique;
    }

    public void setDateCreationRubrique(LocalDate dateCreationRubrique) {
        this.dateCreationRubrique = dateCreationRubrique;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    @Override
    public String toString() {
        return "Rubrique{" +
                "id=" + id +
                ", nomRubrique='" + nomRubrique + '\'' +
                ", descriptionRubrique='" + descriptionRubrique + '\'' +
                ", dateCreationRubrique=" + dateCreationRubrique +
                ", forumId=" + forumId +
                '}';
    }
}