package entity;

public class GuestEvent {
    private int id;
    private String nomGuest;
    private String prenomGuest;
    private String emailGuest;
    private int phoneGuest;
    private String imageGuest;
    private int eventId;

    // Constructeurs
    public GuestEvent() {}

    public GuestEvent(int id, String nomGuest, String prenomGuest, String emailGuest,
                      int phoneGuest, String imageGuest, int eventId) {
        this.id = id;
        this.nomGuest = nomGuest;
        this.prenomGuest = prenomGuest;
        this.emailGuest = emailGuest;
        this.phoneGuest = phoneGuest;
        this.imageGuest = imageGuest;
        this.eventId = eventId;
    }

    public GuestEvent(String nom, String prenom, String email, String phone, String imagePath, int eventId) {
        this.nomGuest = nom;
        this.prenomGuest = prenom;
        this.emailGuest = email;
        this.phoneGuest = Integer.parseInt(phone);
        this.imageGuest = imagePath;
        this.eventId = eventId;
    }


    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomGuest() { return nomGuest; }
    public void setNomGuest(String nomGuest) { this.nomGuest = nomGuest; }

    public String getPrenomGuest() { return prenomGuest; }
    public void setPrenomGuest(String prenomGuest) { this.prenomGuest = prenomGuest; }

    public String getEmailGuest() { return emailGuest; }
    public void setEmailGuest(String emailGuest) { this.emailGuest = emailGuest; }

    public int getPhoneGuest() { return phoneGuest; }
    public void setPhoneGuest(int phoneGuest) { this.phoneGuest = phoneGuest; }

    public String getImageGuest() { return imageGuest; }
    public void setImageGuest(String imageGuest) { this.imageGuest = imageGuest; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
}

