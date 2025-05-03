package service;

import entity.PublicationForum;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicationForumService implements IService<PublicationForum> {

    private Connection conn;

    public PublicationForumService() {
        conn = DataSource.getInstance().getConnection();
    }

    private boolean rubriqueExists(long rubriqueId) {
        String sql = "SELECT COUNT(*) FROM rubrique WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, rubriqueId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la vérification de la rubrique : " + e.getMessage());
        }
        return false;
    }

    @Override
    public void create(PublicationForum pub) {
        if (!rubriqueExists(pub.getRubriqueId())) {
            System.err.println("❌ La rubrique avec l'ID " + pub.getRubriqueId() + " n'existe pas.");
            return;
        }

        String sql = "INSERT INTO publication_forum (nom_publication_forum, contenu_publication_forum, image_publication_forum, date_creation_pub_forum, rubrique_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pub.getNomPublicationForum());
            stmt.setString(2, pub.getContenuPublicationForum());
            stmt.setString(3, pub.getImagePublicationForum());
            stmt.setDate(4, java.sql.Date.valueOf(pub.getDateCreationPubForum()));
            stmt.setLong(5, pub.getRubriqueId());

            stmt.executeUpdate();
            System.out.println("✅ Publication ajoutée : " + pub);
        } catch (SQLException e) {
            System.err.println("❌ Erreur create : " + e.getMessage());
        }
    }



    @Override
    public void update(PublicationForum pub) {
        String sql = "UPDATE publication_forum SET nom_publication_forum = ?, contenu_publication_forum = ?, image_publication_forum = ?, date_creation_pub_forum = ?, rubrique_id = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pub.getNomPublicationForum());
            stmt.setString(2, pub.getContenuPublicationForum());
            stmt.setString(3, pub.getImagePublicationForum());
            stmt.setDate(4, java.sql.Date.valueOf(pub.getDateCreationPubForum()));
            stmt.setLong(5, pub.getRubriqueId());
            stmt.setLong(6, pub.getId());
            stmt.executeUpdate();
            System.out.println("🛠️ Publication mise à jour : " + pub);
        } catch (SQLException e) {
            System.err.println("❌ Erreur update : " + e.getMessage());
        }
    }

    @Override
    public void delete(PublicationForum pub) {
        String sql = "DELETE FROM publication_forum WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, pub.getId());
            stmt.executeUpdate();
            System.out.println("🗑️ Publication supprimée : " + pub);
        } catch (SQLException e) {
            System.err.println("❌ Erreur delete : " + e.getMessage());
        }
    }

    @Override
    public List<PublicationForum> getAll() {
        List<PublicationForum> pubs = new ArrayList<>();
        String sql = "SELECT * FROM publication_forum";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PublicationForum pub = new PublicationForum(
                        rs.getLong("id"),
                        rs.getString("nom_publication_forum"),
                        rs.getString("contenu_publication_forum"),
                        rs.getString("image_publication_forum"),
                        rs.getDate("date_creation_pub_forum").toLocalDate(),
                        rs.getLong("rubrique_id")
                );
                pubs.add(pub);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getAll : " + e.getMessage());
        }

        return pubs;
    }

    @Override
    public PublicationForum getById(int id) {
        String sql = "SELECT * FROM publication_forum WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new PublicationForum(
                        rs.getLong("id"),
                        rs.getString("nom_publication_forum"),
                        rs.getString("contenu_publication_forum"),
                        rs.getString("image_publication_forum"),
                        rs.getDate("date_creation_pub_forum").toLocalDate(),
                        rs.getLong("rubrique_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getById : " + e.getMessage());
        }

        return null;
    }
    public List<PublicationForum> getAllByRubriqueId(long rubriqueId) {
        List<PublicationForum> pubs = new ArrayList<>();
        String sql = "SELECT * FROM publication_forum WHERE rubrique_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, rubriqueId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PublicationForum pub = new PublicationForum(
                        rs.getLong("id"),
                        rs.getString("nom_publication_forum"),
                        rs.getString("contenu_publication_forum"),
                        rs.getString("image_publication_forum"),
                        rs.getDate("date_creation_pub_forum").toLocalDate(),
                        rs.getLong("rubrique_id")
                );
                pubs.add(pub);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getAllByRubriqueId : " + e.getMessage());
        }

        return pubs;
    }

    public void save(PublicationForum publication) {
    }
}