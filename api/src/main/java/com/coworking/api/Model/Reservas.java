package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Reserva {
    private int id;
    private String cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int salaId;
