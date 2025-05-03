package entity;

import java.util.ArrayList;
import java.util.List;

public class CategorieOeuvre {
    private int id;
    private String nomCategorie;
    private String styleCategorie;
    private String techniqueCategorie;

    private List<Oeuvre> oeuvres = new ArrayList<>();



    public CategorieOeuvre() {
    }

    public CategorieOeuvre(String nomCategorie) {
         this.nomCategorie = nomCategorie;
    }

    // Constructeurs

    public CategorieOeuvre(int id , String nomCategorie, String techniqueCategorie) {
        this.id = id;
        this.nomCategorie = nomCategorie;
        this.techniqueCategorie = techniqueCategorie;
    }

    public CategorieOeuvre(int id , String nomCategorie, String styleCategorie, String techniqueCategorie) {
        this.id = id;
        this.nomCategorie = nomCategorie;
        this.styleCategorie = styleCategorie;
        this.techniqueCategorie = techniqueCategorie;
    }

    public CategorieOeuvre(String nomCategorie, String styleCategorie, String techniqueCategorie) {
        this.nomCategorie = nomCategorie;
        this.styleCategorie = styleCategorie;
        this.techniqueCategorie = techniqueCategorie;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getStyleCategorie() {
        return styleCategorie;
    }

    public void setStyleCategorie(String styleCategorie) {
        this.styleCategorie = styleCategorie;
    }

    public String getTechniqueCategorie() {
        return techniqueCategorie;
    }

    public void setTechniqueCategorie(String techniqueCategorie) {
        this.techniqueCategorie = techniqueCategorie;
    }

    // Gestion des œuvres
    public List<Oeuvre> getOeuvres() {
        return oeuvres;
    }

    public void addOeuvre(Oeuvre oeuvre) {
        if (!oeuvres.contains(oeuvre)) {
            oeuvres.add(oeuvre);
            oeuvre.setCategorie(this);
        }
    }

    public void removeOeuvre(Oeuvre oeuvre) {
        if (oeuvres.remove(oeuvre)) {
            if (oeuvre.getCategorie() == this) {
                oeuvre.setCategorie(null);
            }
        }
    }

    @Override
    public String toString() {
        return
                 nomCategorie;
    }
}
