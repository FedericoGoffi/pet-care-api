package com.saltoagro.pet_care_api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.saltoagro.pet_care_api.dto.AplicarVacunaRequest;
import com.saltoagro.pet_care_api.dto.MascotaResponse;
import com.saltoagro.pet_care_api.dto.VacunacionResponse;
import com.saltoagro.pet_care_api.exception.MascotaNoEncontradaException;
import com.saltoagro.pet_care_api.exception.VacunaVigenteException;
import com.saltoagro.pet_care_api.model.Mascota;
import com.saltoagro.pet_care_api.model.Usuario;
import com.saltoagro.pet_care_api.model.Vacuna;
import com.saltoagro.pet_care_api.model.Vacunacion;
import com.saltoagro.pet_care_api.repository.MascotaRepository;
import com.saltoagro.pet_care_api.repository.UsuarioRepository;
import com.saltoagro.pet_care_api.repository.VacunacionRepository;

@Service
public class MascotaService {

        private final MascotaRepository mascotaRepository;
        private final VacunaService vacunaService;
        private final VacunacionRepository vacunacionRepository;
        private final UsuarioRepository usuarioRepository;

        public MascotaService(
                        MascotaRepository mascotaRepository,
                        VacunaService vacunaService,
                        VacunacionRepository vacunacionRepository,
                        UsuarioRepository usuarioRepository) {

                this.mascotaRepository = mascotaRepository;
                this.vacunaService = vacunaService;
                this.vacunacionRepository = vacunacionRepository;
                this.usuarioRepository = usuarioRepository;
        }

        private Mascota obtenerEntidadPorId(Long id) {
                return mascotaRepository.findById(id)
                                .orElseThrow(() -> new MascotaNoEncontradaException(id));
        }

        public Mascota obtenerEntidadPorId(Long id, String username) {

                Mascota mascota = obtenerEntidadPorId(id);

                if (!mascota.getUsuario().getUsername().equals(username)) {
                        throw new AccessDeniedException("No es tu mascota");
                }

                return mascota;
        }

        public Mascota guardar(Mascota mascota, String username) {

                if (mascota == null) {
                        throw new IllegalArgumentException("La mascota no puede ser null");
                }

                Usuario usuario = usuarioRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                mascota.setUsuario(usuario);

                return mascotaRepository.save(mascota);
        }

        public List<MascotaResponse> obtenerMisMascotas(String username) {
                return mascotaRepository.findByUsuarioUsername(username)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        public MascotaResponse obtenerPorId(Long id, String username) {
                return toResponse(obtenerEntidadPorId(id, username));
        }

        public void aplicarVacuna(
                        Long mascotaId,
                        AplicarVacunaRequest request,
                        String username) {

                Mascota mascota = obtenerEntidadPorId(mascotaId, username);
                Vacuna vacuna = vacunaService.obtenerPorId(request.getVacunaId());

                boolean existeVigente = vacunacionRepository.existsByMascotaIdAndVacunaIdAndFechaVencimientoAfter(
                                mascotaId,
                                vacuna.getId(),
                                LocalDate.now());

                if (existeVigente) {
                        throw new VacunaVigenteException(vacuna.getNombre());
                }

                Vacunacion vacunacion = new Vacunacion(
                                mascota,
                                vacuna,
                                request.getFechaAplicacion(),
                                request.getVeterinario(),
                                request.getObservaciones());

                vacunacionRepository.save(vacunacion);
        }

        public List<VacunacionResponse> obtenerVacunasVencidas(
                        Long mascotaId,
                        String username) {

                obtenerEntidadPorId(mascotaId, username);

                return vacunacionRepository
                                .findByMascotaIdAndFechaVencimientoBefore(
                                                mascotaId, LocalDate.now())
                                .stream()
                                .map(this::toVacunacionResponse)
                                .toList();
        }

        public List<VacunacionResponse> obtenerVacunasPorVencer(
                        Long mascotaId,
                        int dias,
                        String username) {

                obtenerEntidadPorId(mascotaId, username);

                LocalDate hoy = LocalDate.now();
                LocalDate limite = hoy.plusDays(dias);

                return vacunacionRepository
                                .findByMascotaIdAndFechaVencimientoBetween(
                                                mascotaId, hoy, limite)
                                .stream()
                                .map(this::toVacunacionResponse)
                                .toList();
        }

        private VacunacionResponse toVacunacionResponse(Vacunacion v) {
                return new VacunacionResponse(
                                v.getId(),
                                v.getVacuna().getNombre(),
                                v.getFechaAplicacion(),
                                v.getFechaVencimiento(),
                                v.getVeterinario(),
                                v.getObservaciones());
        }

        private MascotaResponse toResponse(Mascota mascota) {

                List<VacunacionResponse> vacunaciones = mascota.getVacunaciones()
                                .stream()
                                .map(this::toVacunacionResponse)
                                .toList();

                return new MascotaResponse(
                                mascota.getId(),
                                mascota.getNombre(),
                                mascota.getEspecie(),
                                mascota.getFechaNacimiento(),
                                mascota.getSexo(),
                                mascota.getPesoKg(),
                                mascota.isEsterilizado(),
                                vacunaciones);
        }
}
