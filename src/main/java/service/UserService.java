package service;

import entity.User;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    private ResultSet rs;

    public UserService() {
        cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void create(User user) {
        String requete = "INSERT INTO user (nom, prenom, email, password, roles, is_admin, is_member, is_artist, image) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            pst = cnx.prepareStatement(requete); //plus rapide et plus securisé
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPassword());
            pst.setString(5, user.isAdmin() ? "[\"ROLE_ADMIN\"]" :
                    user.isArtist() ? "[\"ROLE_ARTIST\"]" : "[\"ROLE_MEMBER\"]");
            pst.setBoolean(6, user.isAdmin());
            pst.setBoolean(7, user.isMember());
            pst.setBoolean(8, user.isArtist());
            pst.setString(9, user.getImage());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        String requete = "UPDATE user SET nom=?, prenom=?, email=?, roles=?, " +
                "is_admin=?, is_member=?, is_artist=?, image=?, password=? WHERE id=?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getRoles());
            pst.setBoolean(5, user.isAdmin());
            pst.setBoolean(6, user.isMember());
            pst.setBoolean(7, user.isArtist());
            pst.setString(8, user.getImage());
            pst.setString(9, user.getPassword());
            pst.setInt(10, user.getId()); // Make sure this is set

            System.out.println("Updating user with ID: " + user.getId()); // Debug line

            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage()); // Debug line
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User user) {
        String requete = "DELETE FROM user WHERE id=?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, user.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAll() {
        String requete = "SELECT * FROM user";
        List<User> list = new ArrayList<>();
        try {
            ste = cnx.createStatement();
            rs = ste.executeQuery(requete);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRoles(rs.getString("roles"));
                user.setAdmin(rs.getBoolean("is_admin"));
                user.setMember(rs.getBoolean("is_member"));
                user.setArtist(rs.getBoolean("is_artist"));
                user.setImage(rs.getString("image"));
                user.setSnapphoto(rs.getString("snapphoto"));
                user.setGoogleId(rs.getString("google_id"));
                user.setGoogleAccessToken(rs.getString("google_access_token"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public User getById(int id) {
        String requete = "SELECT * FROM user WHERE id=?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRoles(rs.getString("roles"));
                user.setAdmin(rs.getBoolean("is_admin"));
                user.setMember(rs.getBoolean("is_member"));
                user.setArtist(rs.getBoolean("is_artist"));
                user.setImage(rs.getString("image"));
                user.setSnapphoto(rs.getString("snapphoto"));
                user.setGoogleId(rs.getString("google_id"));
                user.setGoogleAccessToken(rs.getString("google_access_token"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public User findByEmail(String email) {
        String requete = "SELECT * FROM user WHERE email = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setAdmin(rs.getBoolean("is_admin"));
                user.setMember(rs.getBoolean("is_member"));
                user.setArtist(rs.getBoolean("is_artist"));
                user.setImage(rs.getString("image"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}