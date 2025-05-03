package service;

import entity.GuestEvent;
import util.DataSource;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestEventService implements IService<GuestEvent> {

    private final Connection cnx;

    public GuestEventService() {
        cnx = DataSource.getInstance().getConnection();}

    @Override
    public void create(GuestEvent guest) {
        String sql = "INSERT INTO guest_event (nom_guest, prenom_guest, email_guest, phone_guest, image_guest, event_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, guest.getNomGuest());
            pst.setString(2, guest.getPrenomGuest());
            pst.setString(3, guest.getEmailGuest());
            pst.setInt(4, guest.getPhoneGuest());
            pst.setString(5, guest.getImageGuest());
            pst.setInt(6, guest.getEventId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GuestEvent guest) {
        String sql = "UPDATE guest_event SET nom_guest=?, prenom_guest=?, email_guest=?, phone_guest=?, image_guest=?, event_id=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, guest.getNomGuest());
            pst.setString(2, guest.getPrenomGuest());
            pst.setString(3, guest.getEmailGuest());
            pst.setInt(4, guest.getPhoneGuest());
            pst.setString(5, guest.getImageGuest());
            pst.setInt(6, guest.getEventId());
            pst.setInt(7, guest.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(GuestEvent guest) {
        String sql = "DELETE FROM guest_event WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, guest.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<GuestEvent> getAll() {
        List<GuestEvent> list = new ArrayList<>();
        String sql = "SELECT * FROM guest_event";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                GuestEvent g = new GuestEvent();
                g.setId(rs.getInt("id"));
                g.setNomGuest(rs.getString("nom_guest"));
                g.setPrenomGuest(rs.getString("prenom_guest"));
                g.setEmailGuest(rs.getString("email_guest"));
                g.setPhoneGuest(rs.getInt("phone_guest"));
                g.setImageGuest(rs.getString("image_guest"));
                g.setEventId(rs.getInt("event_id"));
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public GuestEvent getById(int id) {
        String sql = "SELECT * FROM guest_event WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    GuestEvent g = new GuestEvent();
                    g.setId(rs.getInt("id"));
                    g.setNomGuest(rs.getString("nom_guest"));
                    g.setPrenomGuest(rs.getString("prenom_guest"));
                    g.setEmailGuest(rs.getString("email_guest"));
                    g.setPhoneGuest(rs.getInt("phone_guest"));
                    g.setImageGuest(rs.getString("image_guest"));
                    g.setEventId(rs.getInt("event_id"));
                    return g;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
