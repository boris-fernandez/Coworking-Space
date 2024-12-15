package controller;

import model.Reserva;
import persistence.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservas {

    public void agregarReserva(Reserva reserva) {
        if (!validarDisponibilidad(reserva.getFechaInicio(), reserva.getFechaFin(), reserva.getSalaId())) {
            System.out.println("Error: La sala no está disponible en las fechas seleccionadas.");
            return;
        }

        String sql = "INSERT INTO reservas (cliente, fecha_inicio, fecha_fin, sala_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, reserva.getCliente());
            statement.setDate(2, Date.valueOf(reserva.getFechaInicio()));
            statement.setDate(3, Date.valueOf(reserva.getFechaFin()));
            statement.setInt(4, reserva.getSalaId());
            statement.executeUpdate();

            System.out.println("Reserva añadida exitosamente: " + reserva);

        } catch (SQLException e) {
            System.err.println("Error al agregar la reserva: " + e.getMessage());
        }
    }

    public boolean validarDisponibilidad(LocalDate fechaInicio, LocalDate fechaFin, int salaId) {
        String sql = "SELECT * FROM reservas WHERE sala_id = ? AND (fecha_inicio <= ? AND fecha_fin >= ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, salaId);
            statement.setDate(2, Date.valueOf(fechaFin));
            statement.setDate(3, Date.valueOf(fechaInicio));

            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next();

        } catch (SQLException e) {
            System.err.println("Error al validar disponibilidad: " + e.getMessage());
            return false;
        }
    }

    public List<Reserva> listarReservas() {
        String sql = "SELECT * FROM reservas";
        List<Reserva> reservas = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Reserva reserva = new Reserva(
                        resultSet.getInt("id"),
                        resultSet.getString("cliente"),
                        resultSet.getDate("fecha_inicio").toLocalDate(),
                        resultSet.getDate("fecha_fin").toLocalDate(),
                        resultSet.getInt("sala_id")
                );
                reservas.add(reserva);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar las reservas: " + e.getMessage());
        }

        return reservas;
    }
}
