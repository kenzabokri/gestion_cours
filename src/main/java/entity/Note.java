package entity;

public class Note {
    private int id;
    private int valeur; // 1 à 5
    private int utilisateurId; // l'id de l'utilisateur
    private int coursId; // l'id du cours

    public Note() {}

    public Note(int valeur, int utilisateurId, int coursId) {
        this.valeur = valeur;
        this.utilisateurId = utilisateurId;
        this.coursId = coursId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getValeur() { return valeur; }
    public void setValeur(int valeur) { this.valeur = valeur; }

    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }

    public int getCoursId() { return coursId; }
    public void setCoursId(int coursId) { this.coursId = coursId; }
}

