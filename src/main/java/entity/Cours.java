package entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.Date;

public class Cours {
    private int id;
    private String type_atelier;
    private String description_cours;
    private String duree_cours;
    private Date date_cours;
    private String image_cours;
    private String lien_video_cours;
    public Cours() {}
    public Cours(int id, String type_atelier, String description_cours, String duree_cours, Date date_cours, String image_cours, String lien_video_cours) {
        this.id = id;
        this.type_atelier = type_atelier;
        this.description_cours = description_cours;
        this.duree_cours = duree_cours;
        this.date_cours = date_cours;
        this.image_cours = image_cours;
        this.lien_video_cours = lien_video_cours;
    }

    public Cours(String type_atelier, String description_cours, String duree_cours, Date date_cours, String image_cours, String lien_video_cours) {
        this.type_atelier = type_atelier;
        this.description_cours = description_cours;
        this.duree_cours = duree_cours;
        this.date_cours = date_cours;
        this.image_cours = image_cours;
        this.lien_video_cours = lien_video_cours;
    }
    public ImageView getImageView() {
        if (image_cours == null || image_cours.isEmpty()) return null;

        File file = new File(image_cours);
        if (!file.exists()) return null;

        ImageView imageView = new ImageView(new Image(file.toURI().toString()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_atelier() {
        return type_atelier;
    }

    public void setType_atelier(String type_atelier) {
        this.type_atelier = type_atelier;
    }

    public String getDescription_cours() {
        return description_cours;
    }

    public void setDescription_cours(String description_cours) {
        this.description_cours = description_cours;
    }

    public String getDuree_cours() {
        return duree_cours;
    }

    public void setDuree_cours(String duree_cours) {
        this.duree_cours = duree_cours;
    }

    public Date getDate_cours() {
        return date_cours;
    }

    public void setDate_cours(Date date_cours) {
        this.date_cours = date_cours;
    }

    public String getImage_cours() {

        return image_cours;
    }

    public void setImage_cours(String image_cours) {
        this.image_cours = image_cours;
    }

    public String getLien_video_cours() {
        return lien_video_cours;
    }

    public void setLien_video_cours(String lien_video_cours) {
        this.lien_video_cours = lien_video_cours;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", type_atelier='" + type_atelier + '\'' +
                ", description_cours='" + description_cours + '\'' +
                ", duree_cours='" + duree_cours + '\'' +
                ", date_cours=" + date_cours +
                ", image_cours='" + image_cours + '\'' +
                ", lien_video_cours='" + lien_video_cours + '\'' +
                '}';
    }

}
