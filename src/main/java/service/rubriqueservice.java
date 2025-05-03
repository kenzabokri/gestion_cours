package service;

import util.DataSource;
import entity.rubrique;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class rubriqueservice implements IService<rubrique> {

    private Connection connection;

    public rubriqueservice() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void create(rubrique r) {
        String sql = "INSERT INTO rubrique (nom_rubrique, description_rubrique, date_creation_rubrique, forum_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, r.getNomRubrique());
            ps.setString(2, r.getDescriptionRubrique());
            ps.setDate(3, Date.valueOf(r.getDateCreationRubrique()));
            ps.setInt(4, r.getForumId());
            ps.executeUpdate();
            System.out.println("Rubrique created: " + r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(rubrique r) {
        String sql = "UPDATE rubrique SET nom_rubrique=?, description_rubrique=?, date_creation_rubrique=?, forum_id=? WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, r.getNomRubrique());
            ps.setString(2, r.getDescriptionRubrique());
            ps.setDate(3, Date.valueOf(r.getDateCreationRubrique()));
            ps.setInt(4, r.getForumId());
            ps.setInt(5, r.getId());
            ps.executeUpdate();
            System.out.println("Rubrique updated: " + r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(rubrique r) {
        String sql = "DELETE FROM rubrique WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, r.getId());
            ps.executeUpdate();
            System.out.println("Rubrique deleted: " + r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<rubrique> getAll() {
        return List.of();
    }


    public List<rubrique> getAllByForumId(int forumId) {
        List<rubrique> list = new ArrayList<>();
        String sql = "SELECT * FROM rubrique WHERE forum_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, forumId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rubrique r = new rubrique(
                        rs.getInt("id"),
                        rs.getString("nom_rubrique"),
                        rs.getString("description_rubrique"),
                        rs.getDate("date_creation_rubrique").toLocalDate(),
                        rs.getInt("forum_id")
                );
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public rubrique getById(int id) {
        String sql = "SELECT * FROM rubrique WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new rubrique(
                        rs.getInt("id"),
                        rs.getString("nom_rubrique"),
                        rs.getString("description_rubrique"),
                        rs.getDate("date_creation_rubrique").toLocalDate(),
                        rs.getInt("forum_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}