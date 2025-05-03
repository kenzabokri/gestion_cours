package entity;

public class CoursMateriel {
    private int coursId;
    private int materielId;

    private String nomCours;
    private String nomMateriel;

    public CoursMateriel(int coursId, int materielId, String nomCours, String nomMateriel) {
        this.coursId = coursId;
        this.materielId = materielId;
        this.nomCours = nomCours;
        this.nomMateriel = nomMateriel;
    }

    public int getCoursId() { return coursId; }
    public int getMaterielId() { return materielId; }

    public String getNomCours() { return nomCours; }
    public String getNomMateriel() { return nomMateriel; }
}

