package service;

import entity.CategorieOeuvre;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieOeuvreService implements IService<CategorieOeuvre> {

    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    private ResultSet rs;

    public CategorieOeuvreService() {
        cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void create(CategorieOeuvre categorie) {
        String requete = "INSERT INTO categorie_oeuvre (nom_categorie, style_categorie, technique_categorie) " +
                "VALUES ('" + categorie.getNomCategorie() + "', '" +
                categorie.getStyleCategorie() + "', '" +
                categorie.getTechniqueCategorie() + "')";
        try {
            ste = cnx.createStatement();
            ste.executeUpdate(requete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Version avec PreparedStatement
    public void createPst(CategorieOeuvre categorie) {
        String requete = "INSERT INTO categorie_oeuvre (nom_categorie, style_categorie, technique_categorie) " +
                "VALUES (?, ?, ?)";
        try {
            pst = cnx.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS); // Add this to get generated ID
            pst.setString(1, categorie.getNomCategorie());
            pst.setString(2, categorie.getStyleCategorie());
            pst.setString(3, categorie.getTechniqueCategorie());
            pst.executeUpdate();

            // Retrieve the generated ID and set it to the categorie object
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                categorie.setId(rs.getInt(1)); // Set the generated ID
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(CategorieOeuvre categorie) {
        String requete = "UPDATE categorie_oeuvre SET nom_categorie = ?, style_categorie = ?, " +
                "technique_categorie = ? WHERE id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setString(1, categorie.getNomCategorie());
            pst.setString(2, categorie.getStyleCategorie());
            pst.setString(3, categorie.getTechniqueCategorie());
            pst.setInt(4, categorie.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(CategorieOeuvre categorie) {
        String requete = "DELETE FROM categorie_oeuvre WHERE id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, categorie.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CategorieOeuvre> getAll() {
        return List.of();
    }

    @Override
    public CategorieOeuvre getById(int id) {
        return null;
    }


    public List<CategorieOeuvre> readAll() {
        String requete = "SELECT * FROM categorie_oeuvre";
        List<CategorieOeuvre> list = new ArrayList<>();
        try {
            ste = cnx.createStatement();
            rs = ste.executeQuery(requete);
            while (rs.next()) {
                list.add(new CategorieOeuvre(
                        rs.getInt("id"),
                        rs.getString("nom_categorie"),
                        rs.getString("style_categorie"),
                        rs.getString("technique_categorie")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    public CategorieOeuvre readById(int id) {
        String requete = "SELECT * FROM categorie_oeuvre WHERE id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new CategorieOeuvre(
                        rs.getInt("id"),
                        rs.getString("nom_categorie"),
                        rs.getString("style_categorie"),
                        rs.getString("technique_categorie")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Méthode supplémentaire pour trouver par nom
    public CategorieOeuvre findByNom(String nom_categorie) {
        String requete = "SELECT * FROM categorie_oeuvre WHERE nom_categorie = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setString(1, nom_categorie);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new CategorieOeuvre(
                        rs.getInt("id"),
                        rs.getString("nom_categorie"),
                        rs.getString("style_categorie"),
                        rs.getString("technique_categorie")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public CategorieOeuvre findById(int id) throws SQLException {
        String query = "SELECT * FROM categorie_oeuvre WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new CategorieOeuvre(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("techniqueCategorie")
                );
            }
            return null;
        }
    }

}