package entity;

import javafx.beans.value.ObservableValue;

import java.time.LocalDate;

public class PublicationForum {
    private long id;
    private String nomPublicationForum;
    private String contenuPublicationForum;
    private String imagePublicationForum;
    private LocalDate dateCreationPubForum;
    private long rubriqueId;

    public PublicationForum(long id, String nomPublicationForum, String contenuPublicationForum,
                            String imagePublicationForum, LocalDate dateCreationPubForum, long rubriqueId) {
        this.id = id;
        this.nomPublicationForum = nomPublicationForum;
        this.contenuPublicationForum = contenuPublicationForum;
        this.imagePublicationForum = imagePublicationForum;
        this.dateCreationPubForum = dateCreationPubForum;
        this.rubriqueId = rubriqueId;
    }

    // Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomPublicationForum() {
        return nomPublicationForum;
    }

    public void setNomPublicationForum(String nomPublicationForum) {
        this.nomPublicationForum = nomPublicationForum;
    }

    public String getContenuPublicationForum() {
        return contenuPublicationForum;
    }

    public void setContenuPublicationForum(String contenuPublicationForum) {
        this.contenuPublicationForum = contenuPublicationForum;
    }

    public String getImagePublicationForum() {
        return imagePublicationForum;
    }

    public void setImagePublicationForum(String imagePublicationForum) {
        this.imagePublicationForum = imagePublicationForum;
    }

    public LocalDate getDateCreationPubForum() {
        return dateCreationPubForum;
    }

    public void setDateCreationPubForum(LocalDate dateCreationPubForum) {
        this.dateCreationPubForum = dateCreationPubForum;
    }

    public long getRubriqueId() {
        return rubriqueId;
    }

    public void setRubriqueId(long rubriqueId) {
        this.rubriqueId = rubriqueId;
    }

    @Override
    public String toString() {
        return "PublicationForum{" +
                "id=" + id +
                ", nomPublicationForum='" + nomPublicationForum + '\'' +
                ", contenuPublicationForum='" + contenuPublicationForum + '\'' +
                ", imagePublicationForum='" + imagePublicationForum + '\'' +
                ", dateCreationPubForum=" + dateCreationPubForum +
                ", rubriqueId=" + rubriqueId +
                '}';
    }


}