package com.saltoagro.pet_care_api.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.saltoagro.pet_care_api.model.Raza;
import com.saltoagro.pet_care_api.repository.RazaRepository;

@Service
public class RazaService {

    private final RazaRepository razaRepository;

    public RazaService(RazaRepository razaRepository) {
        this.razaRepository = razaRepository;
    }

    public List<Raza> listarTodas() {
        return razaRepository.findAll();
    }

    public Raza obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la raza no puede ser null");
        }

        return razaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));
    }

    public Raza guardar(Raza raza) {
        if (raza == null) {
            throw new IllegalArgumentException("La raza no puede ser null");
        }
        return razaRepository.save(raza);
    }

    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la raza no puede ser null");
        }
        razaRepository.deleteById(id);
    }

    public Raza buscarPorNombre(String nombre) {
        Raza raza = razaRepository.findByNombreIgnoreCase(nombre);
        if (raza == null)
            throw new RuntimeException("Raza no encontrada");
        return raza;
    }
}
