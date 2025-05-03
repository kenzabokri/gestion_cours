package entity;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Oeuvre {
    private int id;
    private String nomOeuvre;
    private String description;
    private LocalDate dateDeCreation;
    private String fichierMultimedia;
    private boolean signature;
    private int likes = 0;
    private CategorieOeuvre categorie;
    private List<CommentaireOeuvre> commentaires = new ArrayList<>();
    // Transient field for JavaFX (not stored in DB)
    private transient Image image;

    // Constructeurs



    public Oeuvre(String nomOeuvre, String description, String fichierMultimedia, int likes, CategorieOeuvre categorie ) {
        this.nomOeuvre = nomOeuvre;
        this.description = description;
        this.fichierMultimedia = fichierMultimedia;
        this.likes = likes;
        this.categorie = categorie;
    }

    public Oeuvre(int id , String nomOeuvre, String description, LocalDate dateDeCreation,
                  String fichierMultimedia, boolean signature, int likes, CategorieOeuvre categorie ) {
        this.id = id;
        this.nomOeuvre = nomOeuvre;
        this.description = description;
        this.dateDeCreation = dateDeCreation;
        this.fichierMultimedia = fichierMultimedia;
        this.signature = signature;
        this.likes = likes;

        this.categorie = categorie;    }

    public Oeuvre() {

    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomOeuvre() {
        return nomOeuvre;
    }

    public void setNomOeuvre(String nomOeuvre) {
        this.nomOeuvre = nomOeuvre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDeCreation() {
        return dateDeCreation;
    }

    public void setDateDeCreation(LocalDate dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
    }

    public String getFichierMultimedia() {
        return fichierMultimedia;
    }

    // Ajouter une validation dans le setter
    public void setFichierMultimedia(String fichierMultimedia) {
        if (fichierMultimedia != null) {
            String extension = fichierMultimedia.substring(fichierMultimedia.lastIndexOf(".") + 1);
            if (!extension.matches("(?i)png|jpe?g|webp|GIF")) {
                throw new IllegalArgumentException("Format de fichier non supporté");
            }
        }
        this.fichierMultimedia = fichierMultimedia;
    }
    public boolean isSignature() {
        return signature;
    }

    public void setSignature(boolean signature) {
        this.signature = signature;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

    public CategorieOeuvre getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieOeuvre categorie) {
        this.categorie = categorie;
    }

    public List<CommentaireOeuvre> getCommentaires() {
        return commentaires;
    }

    public void addCommentaire(CommentaireOeuvre commentaire) {
        if (!commentaires.contains(commentaire)) {
            commentaires.add(commentaire);
            commentaire.setOeuvre(this);
        }
    }

    public void removeCommentaire(CommentaireOeuvre commentaire) {
        if (commentaires.remove(commentaire)) {
            if (commentaire.getOeuvre() == this) {
                commentaire.setOeuvre(null);
            }
        }
    }


    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Oeuvre{" +
                "id=" + id +
                ", nomOeuvre='" + nomOeuvre + '\'' +
                ", description='" + description + '\'' +
                ", dateDeCreation=" + dateDeCreation +
                ", fichierMultimedia='" + fichierMultimedia + '\'' +
                ", signature=" + signature +
                ", likes=" + likes +
                '}';
    }




}