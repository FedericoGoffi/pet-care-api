package com.saltoagro.pet_care_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.saltoagro.pet_care_api.exception.VacunaNoEncontradaException;
import com.saltoagro.pet_care_api.model.Vacuna;
import com.saltoagro.pet_care_api.repository.VacunaRepository;

@Service
public class VacunaService {

    private final VacunaRepository vacunaRepository;

    public VacunaService(VacunaRepository vacunaRepository) {
        this.vacunaRepository = vacunaRepository;
    }

    public List<Vacuna> obtenerTodas() {
        return vacunaRepository.findAll();
    }

    public Vacuna obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la vacuna no puede ser null");
        }

        return vacunaRepository.findById(id)
                .orElseThrow(() -> new VacunaNoEncontradaException(id));
    }

    public Vacuna guardar(Vacuna vacuna) {
        if (vacuna == null) {
            throw new IllegalArgumentException("La vacuna no puede ser null");
        }
        return vacunaRepository.save(vacuna);
    }
}
