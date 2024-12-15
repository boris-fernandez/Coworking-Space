package controller;

import model.Sala;
import persistence.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Salas {

    public void agregarSala(Sala sala) {
        String sql = "INSERT INTO salas (nombre, capacidad) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, sala.getNombre());
            statement.setInt(2, sala.getCapacidad());
            statement.executeUpdate();

            System.out.println("Sala añadida exitosamente: " + sala);

        } catch (SQLException e) {
            System.err.println("Error al agregar la sala: " + e.getMessage());
        }
    }

    public List<Sala> listarSalas() {
        String sql = "SELECT * FROM salas";
        List<Sala> salas = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Sala sala = new Sala(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("capacidad")
                );
                salas.add(sala);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar las salas: " + e.getMessage());
        }

        return salas;
    }

    public boolean salaTieneReservas(int salaId) {
        String sql = "SELECT * FROM reservas WHERE sala_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, salaId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.err.println("Error al verificar reservas de la sala: " + e.getMessage());
            return false;
        }
    }

    public void eliminarSala(int id) {
        if (salaTieneReservas(id)) {
            System.out.println("Error: No se puede eliminar la sala porque tiene reservas activas.");
            return;
        }

        String sql = "DELETE FROM salas WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sala eliminada exitosamente.");
            } else {
                System.out.println("No se encontró la sala con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar la sala: " + e.getMessage());
        }
    }
}
