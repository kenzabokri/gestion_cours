package service;

import entity.Event;
import util.DataSource;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IService<Event> {

    private final Connection cnx;

    public EventService() {
        cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void create(Event event) {
        String sql = "INSERT INTO event (nom_event, date_event, localisation_event, description_event, statu_event, image_event, qr_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, event.getNomEvent());
            pst.setDate(2, Date.valueOf(event.getDateEvent()));
            pst.setString(3, event.getLocalisationEvent());
            pst.setString(4, event.getDescriptionEvent());
            pst.setString(5, event.getStatuEvent());
            pst.setString(6, event.getImageEvent());
            pst.setString(7, event.getQrCode());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Event event) {
        String sql = "UPDATE event SET nom_event=?, date_event=?, localisation_event=?, description_event=?, statu_event=?, image_event=?, qr_code=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, event.getNomEvent());
            pst.setDate(2, Date.valueOf(event.getDateEvent()));
            pst.setString(3, event.getLocalisationEvent());
            pst.setString(4, event.getDescriptionEvent());
            pst.setString(5, event.getStatuEvent());
            pst.setString(6, event.getImageEvent());
            pst.setString(7, event.getQrCode());
            pst.setInt(8, event.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Event event) {
        String sql = "DELETE FROM event WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, event.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Event> getAll() {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM event";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Event e = new Event();
                e.setId(rs.getInt("id"));
                e.setNomEvent(rs.getString("nom_event"));
                e.setDateEvent(rs.getDate("date_event").toLocalDate());
                e.setLocalisationEvent(rs.getString("localisation_event"));
                e.setDescriptionEvent(rs.getString("description_event"));
                e.setStatuEvent(rs.getString("statu_event"));
                e.setImageEvent(rs.getString("image_event"));
                e.setQrCode(rs.getString("qr_code"));
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Event getById(int id) {
        String sql = "SELECT * FROM event WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Event e = new Event();
                    e.setId(rs.getInt("id"));
                    e.setNomEvent(rs.getString("nom_event"));
                    e.setDateEvent(rs.getDate("date_event").toLocalDate());
                    e.setLocalisationEvent(rs.getString("localisation_event"));
                    e.setDescriptionEvent(rs.getString("description_event"));
                    e.setStatuEvent(rs.getString("statu_event"));
                    e.setImageEvent(rs.getString("image_event"));
                    e.setQrCode(rs.getString("qr_code"));
                    return e;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
