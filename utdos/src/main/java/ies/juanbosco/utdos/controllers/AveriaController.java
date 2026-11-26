package ies.juanbosco.utdos.controllers;

import ies.juanbosco.utdos.entities.Averia;
import ies.juanbosco.utdos.entities.Vehiculo;
import ies.juanbosco.utdos.repositories.AveriaRepositorio;
import ies.juanbosco.utdos.repositories.VehiculoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AveriaController {

    @Autowired
    private AveriaRepositorio avRep;

    @Autowired
    private VehiculoRepositorio vehRep;


    //Mostramos todas las averias
    @GetMapping("/averias")
    public List<Averia> listar() {
        return avRep.findAll();
    }

    //Mostramos una averia con un id especifico
    @GetMapping("/averias/{id}")
    public ResponseEntity<Averia> obtener(@PathVariable Long id) {
        return avRep.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Creamos una averia
    @PostMapping("/averias")
    public ResponseEntity<Averia> crear(@RequestBody Averia averia) {
        if (averia.getVehiculo() == null || averia.getVehiculo().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Vehiculo> opt = vehRep.findById(averia.getVehiculo().getId());
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vehiculo vehiculo = opt.get();
        averia.setId(null);
        averia.setVehiculo(vehiculo);
        vehiculo.getAverias().add(averia);


        Averia guardada = avRep.save(averia);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    //Actualizamos una averia
    @PutMapping("/averias/{id}")
    public ResponseEntity<Averia> modificar(@PathVariable Long id, @RequestBody Averia datos) {
        Optional<Averia> opt = avRep.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Averia averia = opt.get();
        averia.setDescripcion(datos.getDescripcion());
        if (datos.getEstado() != null) {
            averia.setEstado(datos.getEstado());
        }

        return ResponseEntity.ok(avRep.save(averia));
    }

//Borramos una averia 
    @DeleteMapping("/averias/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        Optional<Averia> opt = avRep.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Averia averia = opt.get();
        if (averia.getVehiculo() != null) {
            averia.getVehiculo().getAverias().remove(averia);
        }
        avRep.delete(averia);
        return ResponseEntity.noContent().build();
    }
}
