package service;

import entity.Partenariat;

import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartenariatService implements IService<Partenariat>{

    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    private ResultSet rs;

    public PartenariatService(){
        cnx= DataSource.getInstance().getConnection();
    }
    @Override
    public void create(Partenariat p) {
        String requete="insert into Partenariat (nom_partenariat, type_partenariat, description_partenariat, adresse_partenariat,logo_partenariat, phone_partenariat, duree_debut_partenariat, duree_fin_partenariat) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setString(1, p.getNom_p());
            pst.setString(2, p.getType_p());
            pst.setString(3, p.getDesc_p());
            pst.setString(4, p.getAdresse_p());
            pst.setString(5, p.getLogo_p());
            pst.setInt(6, p.getPhone_p());
            pst.setDate(7, java.sql.Date.valueOf(p.getDuree_deb_p()));
            pst.setDate(8, java.sql.Date.valueOf(p.getDuree_fin_p()));
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Partenariat p) {
        String requete = "UPDATE Partenariat SET nom_partenariat = ?, type_partenariat = ?, description_partenariat = ?, adresse_partenariat = ?,logo_partenariat = ?, phone_partenariat = ?, duree_debut_partenariat = ?, duree_fin_partenariat = ? WHERE id = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, p.getNom_p());
            pst.setString(2, p.getType_p());
            pst.setString(3, p.getDesc_p());
            pst.setString(4, p.getAdresse_p());
            pst.setString(5, p.getLogo_p());
            pst.setInt(6, p.getPhone_p());
            pst.setDate(7, java.sql.Date.valueOf(p.getDuree_deb_p()));
            pst.setDate(8, java.sql.Date.valueOf(p.getDuree_fin_p()));
            pst.setInt(9, p.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Partenariat mis à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucun partenariat trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);        }

    }

    @Override
    public void delete(Partenariat partenariat) {

    }

    @Override
    public List<Partenariat> getAll() {
        return List.of();
    }

    @Override
    public Partenariat getById(int id) {
        return null;
    }


    public void delete(int id) {
        String requete="delete from Partenariat where id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Partenariat supprimé avec ID : " + id);
            } else {
                System.out.println("⚠️ Aucun partenariat trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<Partenariat> readAll() {

        String requete="select * from Partenariat";
        List<Partenariat> list=new ArrayList<>();
        try {
            ste=cnx.createStatement();
            rs=ste.executeQuery(requete);
            while(rs.next()){
                list.add(new Partenariat(
                                rs.getInt("id"),
                                rs.getString("nom_partenariat"),
                                rs.getString("type_partenariat"),
                                rs.getString("description_partenariat"),
                                rs.getString("adresse_partenariat"),
                                rs.getString("logo_partenariat"),
                                rs.getInt("phone_partenariat"),
                                rs.getDate("duree_debut_partenariat").toLocalDate(),
                                rs.getDate("duree_fin_partenariat").toLocalDate()
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    public Partenariat readById(int id) {
        return null;
    }
}