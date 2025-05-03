package service;

import util.DataSource;
import entity.forum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class forumservice implements IService<forum> {

    private final Connection connection;

    public forumservice() {
        this.connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void create(forum f) {
        String sql = "INSERT INTO forum (nom_forum, theme_forum, description_forum, date_creation_forum, image_forum) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) { // plus rapide et plus securisée
            ps.setString(1, f.getNomForum());
            ps.setString(2, f.getThemeForum());
            ps.setString(3, f.getDescriptionForum());
            ps.setDate(4, Date.valueOf(f.getDateCreationForum()));
            ps.setString(5, f.getImageForum());
            ps.executeUpdate();
            System.out.println("Forum created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(forum f) {
        String sql = "UPDATE forum SET nom_forum=?, theme_forum=?, description_forum=?, date_creation_forum=?, image_forum=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, f.getNomForum());
            ps.setString(2, f.getThemeForum());
            ps.setString(3, f.getDescriptionForum());
            ps.setDate(4, Date.valueOf(f.getDateCreationForum()));
            ps.setString(5, f.getImageForum());
            ps.setLong(6, f.getId());
            ps.executeUpdate();
            System.out.println("Forum updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(forum f) {
        String sql = "DELETE FROM forum WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, f.getId());
            ps.executeUpdate();
            System.out.println("Forum deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<forum> getAll() {
        List<forum> forums = new ArrayList<>();
        String sql = "SELECT * FROM forum";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                forum f = new forum(
                        rs.getInt("id"),
                        rs.getString("nom_forum"),
                        rs.getString("theme_forum"),
                        rs.getString("description_forum"),
                        rs.getDate("date_creation_forum").toLocalDate(),
                        rs.getString("image_forum")
                );
                forums.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return forums;
    }

    @Override
    public forum getById(int id) {
        String sql = "SELECT * FROM forum WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new forum(
                            rs.getInt("id"),
                            rs.getString("nom_forum"),
                            rs.getString("theme_forum"),
                            rs.getString("description_forum"),
                            rs.getDate("date_creation_forum").toLocalDate(),
                            rs.getString("image_forum")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}