package service;

import entity.sponsoring;
import util.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SponsoringService implements IService<sponsoring>{

    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    private ResultSet rs;

    public SponsoringService(){
        cnx= DataSource.getInstance().getConnection();
    }

    @Override
    public void create(sponsoring sponsoring) {
        String req = "INSERT INTO Sponsoring (type_sponsoring, date_debut_sponsoring, date_fin_sponsoring, partenariat_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, sponsoring.getType_s());
            pst.setDate(2, java.sql.Date.valueOf(sponsoring.getDate_debut_s()));
            pst.setDate(3, java.sql.Date.valueOf(sponsoring.getDate_fin_s()));
            pst.setInt(4, sponsoring.getId_p());
            pst.executeUpdate();
            System.out.println("Sponsoring ajouté !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }

    }

    @Override
    public void update(sponsoring sponsoring) {
        String req = "UPDATE Sponsoring SET type_sponsoring=?, date_debut_sponsoring=?, date_fin_sponsoring=? WHERE id=?";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, sponsoring.getType_s());
            pst.setDate(2, java.sql.Date.valueOf(sponsoring.getDate_debut_s()));
            pst.setDate(3, java.sql.Date.valueOf(sponsoring.getDate_fin_s()));
            pst.setInt(4, sponsoring.getId());
            pst.executeUpdate();
            System.out.println("Sponsoring modifié !");
        } catch (SQLException e) {
            System.out.println("Erreur modification : " + e.getMessage());
        }

    }

    @Override
    public void delete(sponsoring sponsoring) {

    }

    @Override
    public List<sponsoring> getAll() {
        return List.of();
    }

    @Override
    public sponsoring getById(int id) {
        return null;
    }


    public void delete(int id) {
        String requete="delete from Sponsoring where id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Sponsoring supprimé avec ID : " + id);
            } else {
                System.out.println("⚠️ Aucun Sponsoring trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<sponsoring> readAll() {
        List<sponsoring> list = new ArrayList<>();
        String req = "SELECT * FROM Sponsoring";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                sponsoring s = new sponsoring(
                        rs.getInt("id"),
                        rs.getString("type_sponsoring"),
                        rs.getDate("date_debut_sponsoring").toLocalDate(),
                        rs.getDate("date_fin_sponsoring").toLocalDate(),
                        rs.getInt("partenariat_id")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lecture : " + e.getMessage());
        }
        return list;
    }


    public sponsoring readById(int id) {
        return null;
    }

    public List<sponsoring> getSponsoringsByPartenariat(int partenariatId) {
        List<sponsoring> sponsorings = new ArrayList<>();
        String requete = "SELECT * FROM Sponsoring WHERE partenariat_id = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, partenariatId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                sponsoring s = new sponsoring(
                        rs.getInt("id"),
                        rs.getString("type_sponsoring"),
                        rs.getDate("date_debut_sponsoring").toLocalDate(),
                        rs.getDate("date_fin_sponsoring").toLocalDate(),
                        rs.getInt("partenariat_id")
                );
                sponsorings.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des sponsorings : " + e.getMessage());
        }

        return sponsorings;
    }

    // SponsoringService.java
    public List<sponsoring> getByPartenariatId(int partenariatId) {
        List<sponsoring> all = readAll(); // ou une requête directe en BDD
        return all.stream()
                .filter(s -> s.getId_p() == partenariatId)
                .collect(Collectors.toList());
    }




}