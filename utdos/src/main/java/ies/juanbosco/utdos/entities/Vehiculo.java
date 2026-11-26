package ies.juanbosco.utdos.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

@Table(name= "vehiculos")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matricula;
    private String modelo;
    private LocalDate fechaMatriculacion;


    @JsonIgnore //Para evitar entrar en un bucle infinito de Vehiculo-Averia-Vehiculo....

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Averia> averias = new ArrayList<>();



}
