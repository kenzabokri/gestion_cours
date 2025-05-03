package entity;


import java.time.LocalDate;

public class sponsoring {
    private int id;
    private String type_s;
    private LocalDate date_debut_s;
    private LocalDate date_fin_s;
    private int id_p;
    private Partenariat partenariat;

    public sponsoring(int id, String type_s, LocalDate date_debut_s, LocalDate date_fin_s, int id_p) {
        this.id = id;
        this.type_s = type_s;
        this.date_debut_s = date_debut_s;
        this.date_fin_s = date_fin_s;
        this.id_p = id_p;
    }

    public sponsoring(String type_s, LocalDate date_debut_s, LocalDate date_fin_s, int id_p) {
        this.type_s = type_s;
        this.date_debut_s = date_debut_s;
        this.date_fin_s = date_fin_s;
        this.id_p = id_p;
    }

    public sponsoring() {

    }

    @Override
    public String toString() {
        return "sponsoring{" +
                "id=" + id +
                ", type_s='" + type_s + '\'' +
                ", date_debut_s=" + date_debut_s +
                ", date_fin_s=" + date_fin_s +
                ", id_p='" + id_p + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate_debut_s() {
        return date_debut_s;
    }

    public void setDate_debut_s(LocalDate date_debut_s) {
        this.date_debut_s = date_debut_s;
    }

    public int getId_p() {
        return id_p;
    }

    public void setId_p(int id_p) {
        this.id_p = id_p;
    }

    public LocalDate getDate_fin_s() {
        return date_fin_s;
    }

    public void setDate_fin_s(LocalDate date_fin_s) {
        this.date_fin_s = date_fin_s;
    }

    public String getType_s() {
        return type_s;
    }

    public void setType_s(String type_s) {
        this.type_s = type_s;
    }

    public Partenariat getPartenariat() {
        return partenariat;
    }

    public void setPartenariat(Partenariat partenariat) {
        this.partenariat = partenariat;
    }
}