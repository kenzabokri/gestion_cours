package service;

import entity.CoursMateriel;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursMaterielService {
    private Connection cnx;

    public CoursMaterielService() {
        cnx = DataSource.getInstance().getConnection();
    }

    public void create(CoursMateriel cm) {
        String requete = "INSERT INTO cours_materiel (cours_id, materiel_id) VALUES (?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, cm.getCoursId());
            pst.setInt(2, cm.getMaterielId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CoursMateriel> readAll() {
        String requete = "SELECT cm.cours_id, cm.materiel_id, c.type_atelier, m.nom_materiel " +
                "FROM cours_materiel cm " +
                "JOIN cours c ON cm.cours_id = c.id " +
                "JOIN materiel m ON cm.materiel_id = m.id";
        List<CoursMateriel> list = new ArrayList<>();
        try (Statement ste = cnx.createStatement(); ResultSet rs = ste.executeQuery(requete)) {
            while (rs.next()) {
                list.add(new CoursMateriel(
                        rs.getInt("cours_id"),
                        rs.getInt("materiel_id"),
                        rs.getString("type_atelier"),
                        rs.getString("nom_materiel")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void delete(int coursId, int materielId) {
        String requete = "DELETE FROM cours_materiel WHERE cours_id = ? AND materiel_id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, coursId);
            pst.setInt(2, materielId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
