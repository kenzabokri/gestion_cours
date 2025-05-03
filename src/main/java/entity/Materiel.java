package entity;

public class Materiel {
    private int id;
    private String nom_materiel;
    private String description_materiel;
    private float prix_materiel;
    private String lieu_achat_materiel;
    private String image_materiel;
    public Materiel() {}

    public Materiel(int id, String nom_materiel, String description_materiel, float prix_materiel, String lieu_achat_materiel, String image_materiel) {
        this.id = id;
        this.nom_materiel = nom_materiel;
        this.description_materiel = description_materiel;
        this.prix_materiel = prix_materiel;
        this.lieu_achat_materiel = lieu_achat_materiel;
        this.image_materiel = image_materiel;
    }

    public Materiel(String nom_materiel, String description_materiel, float prix_materiel, String lieu_achat_materiel, String image_materiel) {
        this.nom_materiel = nom_materiel;
        this.description_materiel = description_materiel;
        this.prix_materiel = prix_materiel;
        this.lieu_achat_materiel = lieu_achat_materiel;
        this.image_materiel = image_materiel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_materiel() {
        return nom_materiel;
    }

    public void setNom_materiel(String nom_materiel) {
        this.nom_materiel = nom_materiel;
    }

    public String getDescription_materiel() {
        return description_materiel;
    }

    public void setDescription_materiel(String description_materiel) {
        this.description_materiel = description_materiel;
    }

    public float getPrix_materiel() {
        return prix_materiel;
    }

    public void setPrix_materiel(float prix_materiel) {
        this.prix_materiel = prix_materiel;
    }

    public String getLieu_achat_materiel() {
        return lieu_achat_materiel;
    }

    public void setLieu_achat_materiel(String lieu_achat_materiel) {
        this.lieu_achat_materiel = lieu_achat_materiel;
    }

    public String getImage_materiel() {
        return image_materiel;
    }

    public void setImage_materiel(String image_materiel) {
        this.image_materiel = image_materiel;
    }

    @Override
    public String toString() {
        return "Materiel{" +
                "id=" + id +
                ", nom_materiel='" + nom_materiel + '\'' +
                ", description_materiel='" + description_materiel + '\'' +
                ", prix_materiel=" + prix_materiel +
                ", lieu_achat_materiel='" + lieu_achat_materiel + '\'' +
                ", image_materiel='" + image_materiel + '\'' +
                '}';
    }
}
