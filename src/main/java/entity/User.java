package entity;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.*;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean isMember;
    private boolean isArtist;
    private String roles; // Will store as JSON array string ["ROLE_ADMIN"]
    private String image;
    private String snapphoto;
    private String googleId;
    private String googleAccessToken;



    public User() {
        this.roles = "[\"ROLE_MEMBER\"]";
        this.isMember = true;  // Default value
    }

    // Constructor with fields
    public User(String nom, String prenom, String email, String password) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        updateRoles();
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
        updateRoles();
    }

    public boolean isArtist() {
        return isArtist;
    }

    public void setArtist(boolean artist) {
        isArtist = artist;
        updateRoles();
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSnapphoto() {
        return snapphoto;
    }

    public void setSnapphoto(String snapphoto) {
        this.snapphoto = snapphoto;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    private void updateRoles() {
        List<String> rolesList = new ArrayList<>();
        if (isAdmin) {
            rolesList.add("ROLE_ADMIN");
        }
        if (isMember) {
            rolesList.add("ROLE_MEMBER");
        }
        if (isArtist) {
            rolesList.add("ROLE_ARTIST");
        }
        // Convert to JSON array format
        this.roles = "[\"" + String.join("\",\"", rolesList) + "\"]";
        if (rolesList.isEmpty()) {
            this.roles = "[\"ROLE_USER\"]"; // Default role if no other roles are set
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", isAdmin=" + isAdmin +
                ", isMember=" + isMember +
                ", isArtist=" + isArtist +
                '}';
    }
}