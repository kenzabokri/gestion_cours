package entity;

import java.time.LocalDateTime;

public class CommentaireOeuvre {
    private int id;
    private String contenu;
    private Oeuvre oeuvre;
    private LocalDateTime dateCreation;

    // Constructeurs
    public CommentaireOeuvre() {
        this.dateCreation = LocalDateTime.now();
    }

    public CommentaireOeuvre(String contenu, Oeuvre oeuvre) {
        this.contenu = contenu;
        this.oeuvre = oeuvre;
        this.dateCreation = LocalDateTime.now();
    }

    public CommentaireOeuvre(int id, String contenu, Oeuvre oeuvre, LocalDateTime dateCreation) {
        this.id = id;
        this.contenu = contenu;
        this.oeuvre = oeuvre;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Oeuvre getOeuvre() {
        return oeuvre;
    }

    public void setOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "CommentaireOeuvre{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }
}