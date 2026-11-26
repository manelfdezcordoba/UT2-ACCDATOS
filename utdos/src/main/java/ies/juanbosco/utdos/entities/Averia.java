package ies.juanbosco.utdos.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name= "averias")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor


public class Averia {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;

    @Enumerated(EnumType.STRING) // Para el enum, que sea solo algun STRING de las tres posibilidades que tenemos
    private EstadoReparacion estado;

    @Column(updatable = false) // Impide la modificacion a futuro
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaUltimaActualizacion;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)  //No puede haber una averia , que no pertenezca a ningun vehiculo
    private Vehiculo vehiculo;


    //Cuando creamos una averia el estado aparecera como pendiente
    @PrePersist
    public void alCrear() {
        if (estado == null) {
            estado = EstadoReparacion.PENDIENTE;
        }
        //La fecha de creacion de la averia sera el momento en el que se cree la averia
        fechaCreacion = LocalDateTime.now();
        fechaUltimaActualizacion = fechaCreacion;
    }


    //Modificamos la fecha cuando actualizamos
    @PreUpdate
    public void alModificar() {
        fechaUltimaActualizacion = LocalDateTime.now();
    }
}
