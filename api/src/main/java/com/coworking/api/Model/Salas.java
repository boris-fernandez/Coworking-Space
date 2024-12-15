package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sala {
    private int id;
    private String nombre;
    private int capacidad;
