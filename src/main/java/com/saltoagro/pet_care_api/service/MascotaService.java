package com.saltoagro.pet_care_api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.saltoagro.pet_care_api.dto.AplicarVacunaRequest;
import com.saltoagro.pet_care_api.dto.MascotaRequest;
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
        private final UsuarioRepository usuarioRepository;
        private final VacunaService vacunaService;
        private final VacunacionRepository vacunacionRepository;

        public MascotaService(
                        MascotaRepository mascotaRepository,
                        UsuarioRepository usuarioRepository,
                        VacunaService vacunaService,
                        VacunacionRepository vacunacionRepository) {

                this.mascotaRepository = mascotaRepository;
                this.usuarioRepository = usuarioRepository;
                this.vacunaService = vacunaService;
                this.vacunacionRepository = vacunacionRepository;
        }

        // Ahora recibe MascotaRequest (DTO) en lugar de la entidad Mascota.
        // El mapeo a entidad se hace acá, en el service, donde controlamos
        // exactamente qué campos se asignan. El usuario se asigna desde
        // el token JWT, nunca desde el body del request.
        public MascotaResponse crear(MascotaRequest request, String username) {

                Usuario usuario = usuarioRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Mascota mascota = new Mascota();
                mascota.setNombre(request.nombre());
                mascota.setEspecie(request.especie());
                mascota.setFechaNacimiento(request.fechaNacimiento());
                mascota.setSexo(request.sexo());
                mascota.setPesoKg(request.pesoKg());
                mascota.setEsterilizado(request.esterilizado());
                mascota.setUsuario(usuario);

                return toResponse(mascotaRepository.save(mascota));
        }

        public List<MascotaResponse> listarDelUsuario(String username) {
                return mascotaRepository.findByUsuarioUsername(username)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        public MascotaResponse obtenerPorId(Long id, String username) {
                Mascota mascota = obtenerEntidad(id, username);
                return toResponse(mascota);
        }

        private Mascota obtenerEntidad(Long id, String username) {

                Mascota mascota = mascotaRepository.findById(id)
                                .orElseThrow(() -> new MascotaNoEncontradaException(id));

                if (!mascota.getUsuario().getUsername().equals(username)) {
                        throw new AccessDeniedException("No es tu mascota");
                }

                return mascota;
        }

        public void aplicarVacuna(Long mascotaId, AplicarVacunaRequest request, String username) {

                Mascota mascota = obtenerEntidad(mascotaId, username);
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

        public List<VacunacionResponse> vacunasVencidas(Long mascotaId, String username) {
                obtenerEntidad(mascotaId, username);

                return vacunacionRepository
                                .findByMascotaIdAndFechaVencimientoBefore(mascotaId, LocalDate.now())
                                .stream()
                                .map(this::toVacunacionResponse)
                                .toList();
        }

        public List<VacunacionResponse> vacunasPorVencer(Long mascotaId, int dias, String username) {
                obtenerEntidad(mascotaId, username);

                LocalDate hoy = LocalDate.now();
                LocalDate limite = hoy.plusDays(dias);

                return vacunacionRepository
                                .findByMascotaIdAndFechaVencimientoBetween(mascotaId, hoy, limite)
                                .stream()
                                .map(this::toVacunacionResponse)
                                .toList();
        }

        private MascotaResponse toResponse(Mascota mascota) {
                return new MascotaResponse(
                                mascota.getId(),
                                mascota.getNombre(),
                                mascota.getEspecie(),
                                mascota.getFechaNacimiento(),
                                mascota.getSexo(),
                                mascota.getPesoKg(),
                                mascota.isEsterilizado(),
                                mascota.getVacunaciones()
                                                .stream()
                                                .map(this::toVacunacionResponse)
                                                .toList());
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
}