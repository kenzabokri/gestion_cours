package service;

import entity.CommentaireOeuvre;
import entity.Oeuvre;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireOeuvreService implements IService<CommentaireOeuvre> {

    private Connection cnx;
    private PreparedStatement pst;
    private ResultSet rs;

    public CommentaireOeuvreService() {
        cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void create(CommentaireOeuvre commentaire) {
        String requete = "INSERT INTO commentaire_oeuvre (contenu, date_creation, id_oeuvre) VALUES (?, ?, ?)";
        try {
            pst = cnx.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, commentaire.getContenu());
            pst.setTimestamp(2, Timestamp.valueOf(commentaire.getDateCreation()));
            pst.setInt(3, commentaire.getOeuvre().getId());

            pst.executeUpdate();

            // Récupérer l'ID généré
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                commentaire.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du commentaire", e);
        }
    }

    @Override
    public void update(CommentaireOeuvre commentaire) {
        String requete = "UPDATE commentaire_oeuvre SET contenu = ?, date_creation = ? WHERE id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setString(1, commentaire.getContenu());
            pst.setTimestamp(2, Timestamp.valueOf(commentaire.getDateCreation()));
            pst.setInt(3, commentaire.getOeuvre().getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du commentaire", e);
        }
    }

    @Override
    public void delete(CommentaireOeuvre commentaire) {
        String requete = "DELETE FROM commentaire_oeuvre WHERE id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, commentaire.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du commentaire", e);
        }
    }

    @Override
    public List<CommentaireOeuvre> getAll() {
        return List.of();
    }

    @Override
    public CommentaireOeuvre getById(int id) {
        return null;
    }


    public List<CommentaireOeuvre> readAll() {
        String requete = "SELECT c.*, o.nom_oeuvre FROM commentaire_oeuvre c JOIN oeuvre o ON c.id_oeuvre = o.id";
        List<CommentaireOeuvre> list = new ArrayList<>();

        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(requete)) {

            while (rs.next()) {
                Oeuvre oeuvre = new Oeuvre();
                oeuvre.setId(rs.getInt("id_oeuvre"));
                oeuvre.setNomOeuvre(rs.getString("nom_oeuvre"));

                CommentaireOeuvre commentaire = new CommentaireOeuvre(
                        rs.getInt("id"),
                        rs.getString("contenu"),
                        oeuvre,
                        rs.getTimestamp("date_creation").toLocalDateTime()
                );

                list.add(commentaire);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture des commentaires", e);
        }
        return list;
    }


    public CommentaireOeuvre readById(int id) {
        String requete = "SELECT c.*, o.nom_oeuvre FROM commentaire_oeuvre c JOIN oeuvre o ON c.id_oeuvre = o.id WHERE c.id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                Oeuvre oeuvre = new Oeuvre();
                oeuvre.setId(rs.getInt("id_oeuvre"));
                oeuvre.setNomOeuvre(rs.getString("nom_oeuvre"));

                return new CommentaireOeuvre(
                        rs.getInt("id"),
                        rs.getString("contenu"),
                        oeuvre,
                        rs.getTimestamp("date_creation").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du commentaire par ID", e);
        }
        return null;
    }

    // Méthode spécifique pour récupérer les commentaires d'une œuvre
    public List<CommentaireOeuvre> findByOeuvre(Oeuvre oeuvre) {
        String requete = "SELECT * FROM commentaire_oeuvre WHERE id_oeuvre = ? ORDER BY date_creation DESC";
        List<CommentaireOeuvre> list = new ArrayList<>();

        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, oeuvre.getId());
            rs = pst.executeQuery();

            while (rs.next()) {
                CommentaireOeuvre commentaire = new CommentaireOeuvre(
                        rs.getInt("id"),
                        rs.getString("contenu"),
                        oeuvre,
                        rs.getTimestamp("date_creation").toLocalDateTime()
                );

                list.add(commentaire);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des commentaires par œuvre", e);
        }
        return list;
    }

    // Méthode pour compter les commentaires d'une œuvre
    public int countByOeuvre(Oeuvre oeuvre) {
        String requete = "SELECT COUNT(*) FROM commentaire_oeuvre WHERE id_oeuvre = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, oeuvre.getId());
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des commentaires", e);
        }
        return 0;
    }
}