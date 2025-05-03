package service;

import entity.CategorieOeuvre;
import entity.Oeuvre;
import util.DataSource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OeuvreService implements IService<Oeuvre> {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "OeuvreImages" + File.separator;
    private Connection cnx;
    private PreparedStatement pst;
    private ResultSet rs;

    public OeuvreService() {
        cnx = DataSource.getInstance().getConnection();
    }

    // Méthode pour sauvegarder une image
    private String saveImage(InputStream imageStream, String fileName) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        System.out.println("Tentative d'upload dans: " + uploadDir.getAbsolutePath());

        if (!uploadDir.exists()) {
            boolean dirsCreated = uploadDir.mkdirs();
            System.out.println("Répertoires créés: " + dirsCreated);
            if (!dirsCreated) {
                throw new IOException("Impossible de créer le répertoire: " + uploadDir.getAbsolutePath());
            }

        }

        // Nettoyage du nom de fichier
        String safeFileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        String uniqueFileName = System.currentTimeMillis() + "_" + safeFileName;

        File destFile = new File(uploadDir, uniqueFileName);
        System.out.println("Destination finale: " + destFile.getAbsolutePath());

        try (OutputStream outStream = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = imageStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }

        System.out.println("Fichier sauvegardé: " + destFile.getAbsolutePath());
        return uniqueFileName;
    }


    // Méthode pour charger une image
    public Image loadImage(String imageName) throws IOException {
        if (imageName == null || imageName.isEmpty()) return null;

        File imageFile = new File(UPLOAD_DIR + imageName);
        if (!imageFile.exists()) return null;

        return ImageIO.read(imageFile);
    }

    // Méthode pour supprimer une image
    private boolean deleteImage(String imageName) {
        if (imageName == null || imageName.isEmpty()) return false;

        File imageFile = new File(UPLOAD_DIR + imageName);
        return imageFile.delete();
    }

    @Override
    public void create(Oeuvre oeuvre) {
        String requete = "INSERT INTO oeuvre (nom_oeuvre, description, date_de_creation, "
                + "fichier_multimedia, signature, likes, categorie_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = cnx.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, oeuvre.getNomOeuvre());
            pst.setString(2, oeuvre.getDescription());
            pst.setDate(3, Date.valueOf(oeuvre.getDateDeCreation()));
            pst.setString(4, oeuvre.getFichierMultimedia());
            pst.setBoolean(5, oeuvre.isSignature());
            pst.setInt(6, oeuvre.getLikes());
            pst.setInt(7, oeuvre.getCategorie().getId());

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    oeuvre.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating oeuvre", e);
        }
    }

    public void createWithImage(Oeuvre oeuvre, InputStream imageStream, String fileName) throws IOException {
        String imageName = saveImage(imageStream, fileName);
        oeuvre.setFichierMultimedia(imageName);
        create(oeuvre);
    }

    @Override
    public void update(Oeuvre oeuvre) {
        String requete = "UPDATE oeuvre SET nom_oeuvre=?, description=?, date_de_creation=?, "
                + "fichier_multimedia=?, signature=?, likes=?, categorie_id=? WHERE id=?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setString(1, oeuvre.getNomOeuvre());
            pst.setString(2, oeuvre.getDescription());
            pst.setDate(3, Date.valueOf(oeuvre.getDateDeCreation()));
            pst.setString(4, oeuvre.getFichierMultimedia());
            pst.setBoolean(5, oeuvre.isSignature());
            pst.setInt(6, oeuvre.getLikes());
            pst.setInt(7, oeuvre.getCategorie().getId());
            pst.setInt(8, oeuvre.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating oeuvre", e);
        }
    }

    public void updateWithImage(Oeuvre oeuvre, InputStream imageStream, String fileName) throws IOException {
        // Supprimer l'ancienne image si elle existe
        if (oeuvre.getFichierMultimedia() != null) {
            deleteImage(oeuvre.getFichierMultimedia());
        }

        String imageName = saveImage(imageStream, fileName);
        oeuvre.setFichierMultimedia(imageName);
        update(oeuvre);
    }

    @Override
    public void delete(Oeuvre oeuvre) {
        // Supprimer l'image associée
        if (oeuvre.getFichierMultimedia() != null) {
            deleteImage(oeuvre.getFichierMultimedia());
        }

        String requete = "DELETE FROM oeuvre WHERE id=?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, oeuvre.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting oeuvre", e);
        }
    }

    @Override
    public List<Oeuvre> getAll() {
        return List.of();
    }

    @Override
    public Oeuvre getById(int id) {
        return null;
    }


    public List<Oeuvre> readAll() {
        String requete = "SELECT o.*, c.nom_categorie, c.style_categorie, c.technique_categorie "
                + "FROM oeuvre o JOIN categorie_oeuvre c ON o.categorie_id = c.id";
        List<Oeuvre> list = new ArrayList<>();

        try {
            pst = cnx.prepareStatement(requete);
            rs = pst.executeQuery();

            while (rs.next()) {
                CategorieOeuvre categorie = new CategorieOeuvre(
                        rs.getInt("categorie_id"),
                        rs.getString("nom_categorie"),
                        rs.getString("style_categorie"),
                        rs.getString("technique_categorie")
                );

                Oeuvre oeuvre = new Oeuvre(
                        rs.getInt("id"),
                        rs.getString("nom_oeuvre"),
                        rs.getString("description"),
                        rs.getDate("date_de_creation").toLocalDate(),
                        rs.getString("fichier_multimedia"),
                        rs.getBoolean("signature"),
                        rs.getInt("likes"),
                        categorie
                );
                list.add(oeuvre);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error reading oeuvres", e);
        }
        return list;
    }


    public Oeuvre readById(int id) {
        String requete = "SELECT o.*, c.nom_categorie, c.style_categorie, c.technique_categorie "
                + "FROM oeuvre o JOIN categorie_oeuvre c ON o.categorie_id = c.id "
                + "WHERE o.id=?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                CategorieOeuvre categorie = new CategorieOeuvre(
                        rs.getInt("categorie_id"),
                        rs.getString("nom_categorie"),
                        rs.getString("style_categorie"),
                        rs.getString("technique_categorie")
                );

                return new Oeuvre(
                        rs.getInt("id"),
                        rs.getString("nom_oeuvre"),
                        rs.getString("description"),
                        rs.getDate("date_de_creation").toLocalDate(),
                        rs.getString("fichier_multimedia"),
                        rs.getBoolean("signature"),
                        rs.getInt("likes"),
                        categorie
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading oeuvre by id", e);
        }
        return null;
    }

    public Oeuvre readByIdWithImage(int id) throws IOException {
        Oeuvre oeuvre = readById(id);
        if (oeuvre != null && oeuvre.getFichierMultimedia() != null) {
            oeuvre.setImage(loadImage(oeuvre.getFichierMultimedia()));
        }
        return oeuvre;
    }

    public void incrementLikes(Oeuvre oeuvre) {
        String requete = "UPDATE oeuvre SET likes = likes + 1 WHERE id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, oeuvre.getId());
            pst.executeUpdate();
            oeuvre.incrementLikes();
        } catch (SQLException e) {
            throw new RuntimeException("Error incrementing likes", e);
        }
    }


    public void decrementLikes(Oeuvre oeuvre) {
        String requete = "UPDATE oeuvre SET likes = GREATEST(likes - 1, 0) WHERE id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, oeuvre.getId());
            pst.executeUpdate();
            oeuvre.decrementLikes();
        } catch (SQLException e) {
            throw new RuntimeException("Error decrementing likes", e);
        }
    }


    public List<Oeuvre> findByCategorie(CategorieOeuvre categorie) {
        String requete = "SELECT * FROM oeuvre WHERE categorie_id = ?";
        List<Oeuvre> list = new ArrayList<>();
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, categorie.getId());
            rs = pst.executeQuery();

            while (rs.next()) {
                Oeuvre oeuvre = new Oeuvre(
                        rs.getInt("id"),
                        rs.getString("nom_oeuvre"),
                        rs.getString("description"),
                        rs.getDate("date_de_creation").toLocalDate(),
                        rs.getString("fichier_multimedia"),
                        rs.getBoolean("signature"),
                        rs.getInt("likes"),
                        categorie
                );
                list.add(oeuvre);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding by category", e);
        }
        return list;
    }


    public int countByCategorie(CategorieOeuvre categorie) {
        String query = "SELECT COUNT(*) FROM oeuvre WHERE categorie_id = ?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, categorie.getId());
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting artworks by category: " + e.getMessage());
        }
        return 0;
    }

    public Connection getConnection() {
        return cnx;
    }
    public String getUploadDir() {
        return UPLOAD_DIR;
    }


}