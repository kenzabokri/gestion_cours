package service;

import entity.Materiel;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielService implements IService<Materiel> {
    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    private ResultSet rs;

    public MaterielService(){
        cnx= DataSource.getInstance().getConnection();
    }
    @Override
    public void create(Materiel materiel) {
        String requete="insert into materiel (nom_materiel,description_materiel,prix_materiel,lieu_achat_materiel,image_materiel) values (?,?,?,?,?)";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setString(1,materiel.getNom_materiel());
            pst.setString(2,materiel.getDescription_materiel());
            pst.setFloat(3,materiel.getPrix_materiel());
            pst.setString(4,materiel.getLieu_achat_materiel());
            pst.setString(5,materiel.getImage_materiel());

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Materiel materiel) {
        String requete = "UPDATE materiel SET nom_materiel = ?, description_materiel = ?, prix_materiel = ?, lieu_achat_materiel = ?,image_materiel = ?WHERE id = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, materiel.getNom_materiel());
            pst.setString(2, materiel.getDescription_materiel());
            pst.setFloat(3,materiel.getPrix_materiel());
            pst.setString(4,materiel.getLieu_achat_materiel());
            pst.setString(5, materiel.getImage_materiel());
            pst.setInt(6,materiel.getId());

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
    public void delete(Materiel materiel) {

    }

    @Override
    public List<Materiel> getAll() {
        return List.of();
    }

    @Override
    public Materiel getById(int id) {
        return null;
    }

    public void delete(int id) {
        String requete="delete from materiel where id = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ materiel supprimé avec ID : " + id);
            } else {
                System.out.println("⚠️ Aucun materiel trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Materiel> readAll() {
        String requete="select * from materiel";
        List<Materiel> list=new ArrayList<>();
        try {
            ste=cnx.createStatement();
            rs=ste.executeQuery(requete);
            while(rs.next()){
                list.add(new Materiel(
                        rs.getInt("id"),
                        rs.getString("nom_materiel"),
                        rs.getString("description_materiel"),
                        rs.getFloat("prix_materiel"),
                        rs.getString("lieu_achat_materiel"),
                        rs.getString("image_materiel")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    public Materiel readById(int id) {
        return null;
    }
    public Materiel findById(int id) {
        // à adapter selon ta logique DAO
        return readAll().stream().filter(m -> m.getId() == id).findFirst().orElse(null);
    }

}
