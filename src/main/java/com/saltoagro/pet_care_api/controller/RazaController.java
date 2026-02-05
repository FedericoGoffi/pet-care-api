package com.saltoagro.pet_care_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.saltoagro.pet_care_api.model.Raza;
import com.saltoagro.pet_care_api.service.RazaService;

@RestController
@RequestMapping("/razas")
public class RazaController {

    private final RazaService razaService;

    public RazaController(RazaService razaService) {
        this.razaService = razaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Raza> listar() {
        return razaService.listarTodas();
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Raza buscar(@RequestParam String nombre) {
        return razaService.buscarPorNombre(nombre);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Raza crear(@RequestBody Raza raza) {
        return razaService.guardar(raza);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void borrar(@PathVariable Long id) {
        razaService.eliminar(id);
    }
}