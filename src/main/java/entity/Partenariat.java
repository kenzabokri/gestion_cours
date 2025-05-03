package entity;

import java.time.LocalDate;


public class Partenariat {
    private int id;
    private String nom_p;
    private String type_p;
    private String desc_p;
    private String adresse_p;
    private String logo_p;
    private int phone_p;
    private LocalDate duree_deb_p;
    private LocalDate duree_fin_p;

    public Partenariat(int id, String nom_p, String type_p, String desc_p, String adresse_p,String logo_p, int phone_p, LocalDate duree_deb_p, LocalDate duree_fin_p) {
        this.id = id;
        this.nom_p = nom_p;
        this.type_p = type_p;
        this.desc_p = desc_p;
        this.adresse_p = adresse_p;
        this.logo_p = logo_p;
        this.phone_p = phone_p;
        this.duree_deb_p = duree_deb_p;
        this.duree_fin_p = duree_fin_p;
    }


    public Partenariat(String nom_p, String type_p, String desc_p, String adresse_p,String logo_p, int phone_p, LocalDate duree_deb_p, LocalDate duree_fin_p) {
        this.nom_p = nom_p;
        this.type_p = type_p;
        this.desc_p = desc_p;
        this.adresse_p = adresse_p;
        this.logo_p = logo_p;
        this.phone_p = phone_p;
        this.duree_deb_p = duree_deb_p;
        this.duree_fin_p = duree_fin_p;

    }

    public Partenariat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_p() {
        return nom_p;
    }

    public String getLogo_p() {
        return logo_p;
    }

    public void setLogo_p(String logo_p) {
        this.logo_p = logo_p;
    }

    public void setNom_p(String nom_p) {
        this.nom_p = nom_p;
    }

    public void setType_p(String type_p) {
        this.type_p = type_p;
    }

    public void setDesc_p(String desc_p) {
        this.desc_p = desc_p;
    }

    public void setAdresse_p(String adresse_p) {
        this.adresse_p = adresse_p;
    }

    public void setPhone_p(int phone_p) {
        this.phone_p = phone_p;
    }

    public void setDuree_deb_p(LocalDate duree_deb_p) {
        this.duree_deb_p = duree_deb_p;
    }


    public void setDuree_fin_p(LocalDate duree_fin_p) {
        this.duree_fin_p = duree_fin_p;
    }



    public String getType_p() {
        return type_p;
    }

    public String getDesc_p() {
        return desc_p;
    }

    public String getAdresse_p() {
        return adresse_p;
    }

    public int getPhone_p() {
        return phone_p;
    }

    public LocalDate getDuree_deb_p() {
        return duree_deb_p;
    }

    public LocalDate getDuree_fin_p() {
        return duree_fin_p;
    }

    @Override
    public String toString() {
        return nom_p;
    }


}