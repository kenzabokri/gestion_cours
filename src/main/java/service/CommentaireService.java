package service;

import util.DataSource;
import entity.Commentaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements IService<Commentaire> {

    private Connection conn;

    public CommentaireService() {
        conn = DataSource.getInstance().getConnection();
    }

    @Override
    public void create(Commentaire commentaire) {
        String sql = "INSERT INTO commentaire_publication_forum (id, contenu_commentaire, date_creation_commentaire, likes, dislikes, publication_forum_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, commentaire.getId());
            stmt.setString(2, commentaire.getContenuCommentaire());
            stmt.setDate(3, Date.valueOf(commentaire.getDateCreationCommentaire()));
            stmt.setInt(4, commentaire.getLikes());
            stmt.setInt(5, commentaire.getDislikes());
            stmt.setLong(6, commentaire.getPublicationForumId());
            stmt.executeUpdate();
            System.out.println("✅ Commentaire ajouté à la base : " + commentaire);
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du commentaire : " + e.getMessage());
        }
    }

    @Override
    public void update(Commentaire commentaire) {
        String sql = "UPDATE commentaire_publication_forum SET contenu_commentaire = ?, date_creation_commentaire = ?, likes = ?, dislikes = ?, publication_forum_id = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commentaire.getContenuCommentaire());
            stmt.setDate(2, Date.valueOf(commentaire.getDateCreationCommentaire()));
            stmt.setInt(3, commentaire.getLikes());
            stmt.setInt(4, commentaire.getDislikes());
            stmt.setLong(5, commentaire.getPublicationForumId());
            stmt.setLong(6, commentaire.getId());
            stmt.executeUpdate();
            System.out.println("🛠️ Commentaire mis à jour : " + commentaire);
        } catch (SQLException e) {
            System.err.println("❌ Erreur update commentaire : " + e.getMessage());
        }
    }

    @Override
    public void delete(Commentaire commentaire) {
        String sql = "DELETE FROM commentaire_publication_forum WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, commentaire.getId());
            stmt.executeUpdate();
            System.out.println("🗑️ Commentaire supprimé : " + commentaire);
        } catch (SQLException e) {
            System.err.println("❌ Erreur delete commentaire : " + e.getMessage());
        }
    }

    @Override
    public List<Commentaire> getAll() {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT * FROM commentaire_publication_forum";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Commentaire c = new Commentaire(
                        rs.getLong("id"),
                        rs.getString("contenu_commentaire"),
                        rs.getDate("date_creation_commentaire").toLocalDate(),
                        rs.getInt("likes"),
                        rs.getInt("dislikes"),
                        rs.getLong("publication_forum_id")
                );
                commentaires.add(c);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getAll commentaires : " + e.getMessage());
        }

        return commentaires;
    }

    @Override
    public Commentaire getById(int id) {
        String sql = "SELECT * FROM commentaire_publication_forum WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Commentaire(
                        rs.getLong("id"),
                        rs.getString("contenu_commentaire"),
                        rs.getDate("date_creation_commentaire").toLocalDate(),
                        rs.getInt("likes"),
                        rs.getInt("dislikes"),
                        rs.getLong("publication_forum_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getById commentaire : " + e.getMessage());
        }

        return null;
    }
}