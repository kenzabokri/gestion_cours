package service;

import entity.Note;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class NoteService implements IService<Note> {
    private Connection cnx;
    private PreparedStatement pst;
    private ResultSet rs;

    public NoteService() {
        cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void create(Note note) {
        if (!existeNote(note.getUtilisateurId(), note.getCoursId())) {
            String requete = "INSERT INTO note (valeur, utilisateurId, coursId) VALUES (?, ?, ?)";
            try {
                pst = cnx.prepareStatement(requete);
                pst.setInt(1, note.getValeur());
                pst.setInt(2, note.getUtilisateurId());
                pst.setInt(3, note.getCoursId());
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            update(note);
        }
    }

    @Override
    public void update(Note note) {
        String requete = "UPDATE note SET valeur = ? WHERE utilisateurId = ? AND coursId = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, note.getValeur());
            pst.setInt(2, note.getUtilisateurId());
            pst.setInt(3, note.getCoursId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Note note) {
        String requete = "DELETE FROM note WHERE id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, note.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Note> getAll() {
        List<Note> notes = new ArrayList<>();
        String requete = "SELECT * FROM note";
        try (Statement stmt = cnx.createStatement()) {
            rs = stmt.executeQuery(requete);
            while (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setValeur(rs.getInt("valeur"));
                note.setUtilisateurId(rs.getInt("utilisateurId"));
                note.setCoursId(rs.getInt("coursId"));
                notes.add(note);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notes;
    }

    @Override
    public Note getById(int id) {
        String requete = "SELECT * FROM note WHERE id = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setValeur(rs.getInt("valeur"));
                note.setUtilisateurId(rs.getInt("utilisateurId"));
                note.setCoursId(rs.getInt("coursId"));
                return note;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public boolean existeNote(int utilisateurId, int coursId) {
        String requete = "SELECT id FROM note WHERE utilisateurId = ? AND coursId = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, utilisateurId);
            pst.setInt(2, coursId);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNoteParCoursUtilisateur(int utilisateurId, int coursId) {
        String requete = "SELECT valeur FROM note WHERE utilisateurId = ? AND coursId = ?";
        try {
            pst = cnx.prepareStatement(requete);
            pst.setInt(1, utilisateurId);
            pst.setInt(2, coursId);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("valeur");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
