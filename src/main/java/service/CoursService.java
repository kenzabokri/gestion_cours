package service;

import entity.Cours;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursService implements IService<Cours>{
    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    private ResultSet rs;

    public CoursService(){
        cnx= DataSource.getInstance().getConnection();
    }

    @Override
    public void create(Cours cours) {
        String requete="insert into cours (type_atelier,description_cours,duree_cours,date_cours,image_cours,lien_video_cours) values (?,?,?,?,?,?)";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setString(1,cours.getType_atelier());
            pst.setString(2,cours.getDescription_cours());
            pst.setString(3,cours.getDuree_cours());
            pst.setDate(4,cours.getDate_cours());
            pst.setString(5,cours.getImage_cours());
            pst.setString(6,cours.getLien_video_cours());
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Cours cours) {
        String requete = "UPDATE cours SET type_atelier = ?, description_cours = ?, duree_cours = ?, date_cours = ?,image_cours = ?, lien_video_cours = ? WHERE id = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, cours.getType_atelier());
            pst.setString(2, cours.getDescription_cours());
            pst.setString(3,cours.getDuree_cours());
            pst.setDate(4,cours.getDate_cours());
            pst.setString(5, cours.getImage_cours());
            pst.setString(6, cours.getLien_video_cours());
            pst.setInt(7,cours.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ cours mis à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucun cours trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);        }
    }

    @Override
    public void delete(Cours cours) {

    }

    @Override
    public List<Cours> getAll() {
        return List.of();
    }

    @Override
    public Cours getById(int id) {
        return null;
    }


    public void delete(int id) {
        String requete="delete from cours where id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ cours supprimé avec ID : " + id);
            } else {
                System.out.println("⚠️ Aucun cours trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Cours> readAll() {
        String requete="select * from cours";
        List<Cours> list=new ArrayList<>();
        try {
            ste=cnx.createStatement();
            rs=ste.executeQuery(requete);
            while(rs.next()){
                list.add(new Cours(
                        rs.getInt("id"),
                        rs.getString("type_atelier"),
                        rs.getString("description_cours"),
                        rs.getString("duree_cours"),
                        rs.getDate("date_cours"),
                        rs.getString("image_cours"),
                        rs.getString("lien_video_cours")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    public Cours readById(int id) {
        return null;
    }
}
