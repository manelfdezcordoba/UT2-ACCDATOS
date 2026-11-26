package ies.juanbosco.utdos.controllers;

import ies.juanbosco.utdos.entities.Averia;
import ies.juanbosco.utdos.entities.EstadoReparacion;
import ies.juanbosco.utdos.entities.Vehiculo;
import ies.juanbosco.utdos.repositories.VehiculoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class VehiculoController {

    @Autowired
    private VehiculoRepositorio vehRep;

    //Mostramos todos los vehiculos
    @GetMapping("/vehiculos")
    public List<Vehiculo> listar() {
        return vehRep.findAll();
    }

    //Buscamos un vehiculo por su id
    @GetMapping("/vehiculos/{id}")
    public ResponseEntity<Vehiculo> obtener(@PathVariable Long id) {
        return vehRep.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Agregamos un vehiculo
    @PostMapping("/vehiculos")
    public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo vehiculo) {
        vehiculo.setId(null);
        Vehiculo guardado = vehRep.save(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    //Modificamos un vehiculo
    @PutMapping("/vehiculos/{id}")
    public ResponseEntity<Vehiculo> modificar(@PathVariable Long id, @RequestBody Vehiculo datos) {
        Optional<Vehiculo> opt = vehRep.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Vehiculo vehiculo = opt.get();
        vehiculo.setMatricula(datos.getMatricula());
        vehiculo.setModelo(datos.getModelo());
        vehiculo.setFechaMatriculacion(datos.getFechaMatriculacion());
        return ResponseEntity.ok(vehRep.save(vehiculo));
    }
//Borramos un vehiculo, en caso de que tenga averias sin TERMINAR nos dara error 
    @DeleteMapping("/vehiculos/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        Optional<Vehiculo> opt = vehRep.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Vehiculo vehiculo = opt.get();

        boolean hayAveriasSinTerminar = vehiculo.getAverias().stream()
                .anyMatch(av -> av.getEstado() != EstadoReparacion.TERMINADO);
        if (hayAveriasSinTerminar) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        vehRep.delete(vehiculo);
        return ResponseEntity.noContent().build();
    }
//Buscamos un vehiculo con un id determinado y nos aparecera su averia
    @GetMapping("/vehiculos/{id}/averias")
    public ResponseEntity<List<Averia>> averiasDeVehiculo(@PathVariable Long id) {
        return vehRep.findById(id)
                .map(v -> ResponseEntity.ok(v.getAverias()))
                .orElse(ResponseEntity.notFound().build());
    }
}
