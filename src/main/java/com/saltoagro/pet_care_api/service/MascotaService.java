package com.saltoagro.pet_care_api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
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

        public Page<MascotaResponse> listarTodas(int pagina, int tamanio) {
                Pageable pageable = PageRequest.of(pagina, tamanio, Sort.by("nombre").ascending());
                return mascotaRepository.findAll(pageable)
                                .map(this::toResponse);
        }

        public MascotaResponse obtenerPorId(Long id, String username) {
                return toResponse(obtenerEntidad(id, username));
        }

        private Mascota obtenerEntidad(Long id, String username) {
                Mascota mascota = mascotaRepository.findById(id)
                                .orElseThrow(() -> new MascotaNoEncontradaException(id));

                if (!mascota.getUsuario().getUsername().equals(username)) {
                        throw new AccessDeniedException("No es tu mascota");
                }

                return mascota;
        }

        private Mascota obtenerEntidadSinRestriccion(Long id) {
                return mascotaRepository.findById(id)
                                .orElseThrow(() -> new MascotaNoEncontradaException(id));
        }

        private Mascota resolverMascota(Long id, String username, boolean esPrivilegiado) {
                return esPrivilegiado
                                ? obtenerEntidadSinRestriccion(id)
                                : obtenerEntidad(id, username);
        }

        public void aplicarVacuna(Long mascotaId, AplicarVacunaRequest request,
                        String username, boolean esPrivilegiado) {

                Mascota mascota = resolverMascota(mascotaId, username, esPrivilegiado);
                Vacuna vacuna = vacunaService.obtenerPorId(request.getVacunaId());

                boolean existeVigente = vacunacionRepository.existsByMascotaIdAndVacunaIdAndFechaVencimientoAfter(
                                mascotaId, vacuna.getId(), LocalDate.now());

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

        public List<VacunacionResponse> vacunasVencidas(Long mascotaId,
                        String username, boolean esPrivilegiado) {

                resolverMascota(mascotaId, username, esPrivilegiado);

                return vacunacionRepository
                                .findByMascotaIdAndFechaVencimientoBefore(mascotaId, LocalDate.now())
                                .stream()
                                .map(this::toVacunacionResponse)
                                .toList();
        }

        public List<VacunacionResponse> vacunasPorVencer(Long mascotaId, int dias,
                        String username, boolean esPrivilegiado) {

                resolverMascota(mascotaId, username, esPrivilegiado);

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