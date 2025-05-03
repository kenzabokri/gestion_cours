package entity;

import java.time.LocalDate;

public class Event {
    private int id;
    private String nomEvent;
    private LocalDate dateEvent;
    private String localisationEvent;
    private String descriptionEvent;
    private String statuEvent;
    private String imageEvent;
    private String qrCode;

    // Constructeurs
    public Event() {}

    public Event(int id, String nomEvent, LocalDate dateEvent, String localisationEvent,
                 String descriptionEvent, String statuEvent, String imageEvent, String qrCode) {
        this.id = id;
        this.nomEvent = nomEvent;
        this.dateEvent = dateEvent;
        this.localisationEvent = localisationEvent;
        this.descriptionEvent = descriptionEvent;
        this.statuEvent = statuEvent;
        this.imageEvent = imageEvent;
        this.qrCode = qrCode;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomEvent() { return nomEvent; }
    public void setNomEvent(String nomEvent) { this.nomEvent = nomEvent; }

    public LocalDate getDateEvent() { return dateEvent; }
    public void setDateEvent(LocalDate dateEvent) { this.dateEvent = dateEvent; }

    public String getLocalisationEvent() { return localisationEvent; }
    public void setLocalisationEvent(String localisationEvent) { this.localisationEvent = localisationEvent; }

    public String getDescriptionEvent() { return descriptionEvent; }
    public void setDescriptionEvent(String descriptionEvent) { this.descriptionEvent = descriptionEvent; }

    public String getStatuEvent() { return statuEvent; }
    public void setStatuEvent(String statuEvent) { this.statuEvent = statuEvent; }

    public String getImageEvent() { return imageEvent; }
    public void setImageEvent(String imageEvent) { this.imageEvent = imageEvent; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    @Override
    public String toString() {
        return nomEvent + " (ID: " + id + ")";
    }

}
