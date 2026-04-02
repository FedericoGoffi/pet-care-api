package com.saltoagro.pet_care_api.service;

import static org.mockito.Mockito.*;
import org.springframework.security.access.AccessDeniedException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saltoagro.pet_care_api.repository.*;
import com.saltoagro.pet_care_api.dto.AplicarVacunaRequest;
import com.saltoagro.pet_care_api.dto.MascotaRequest;
import com.saltoagro.pet_care_api.dto.MascotaResponse;
import com.saltoagro.pet_care_api.exception.VacunaVigenteException;
import com.saltoagro.pet_care_api.model.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

        @Mock
        private MascotaRepository mascotaRepository;

        @Mock
        private UsuarioRepository usuarioRepository;

        @Mock
        private VacunaService vacunaService;

        @Mock
        private VacunacionRepository vacunacionRepository;

        @InjectMocks
        private MascotaService mascotaService;

        @Test
        void deberiaCrearMascota() {
                String username = "Federico";

                Usuario usuario = new Usuario();
                usuario.setUsername(username);

                MascotaRequest request = new MascotaRequest(
                                "Pepito",
                                Especie.PERRO,
                                null,
                                null,
                                null,
                                false);

                Mascota mascotaGuardada = new Mascota();
                mascotaGuardada.setNombre("Pepito");
                mascotaGuardada.setUsuario(usuario);

                when(usuarioRepository.findByUsername(username))
                                .thenReturn(Optional.of(usuario));

                when(mascotaRepository.save(any(Mascota.class)))
                                .thenReturn(mascotaGuardada);

                MascotaResponse resultado = mascotaService.crear(request, username);

                assertEquals("Pepito", resultado.getNombre());
                verify(mascotaRepository).save(any(Mascota.class));
        }

        @Test
        void deberiaLanzarExcepcionSiUsuarioNoExiste() {
                String username = "Federico";

                MascotaRequest request = new MascotaRequest(
                                "Pepito",
                                Especie.PERRO,
                                null,
                                null,
                                null,
                                false);

                when(usuarioRepository.findByUsername(username))
                                .thenReturn(Optional.empty());

                assertThrows(RuntimeException.class, () -> {
                        mascotaService.crear(request, username);
                });
        }

        @Test
        void deberiaLanzarAccessDeniedSiMascotaNoEsDelUsuario() {

                Long mascotaId = 1L;

                Usuario usuario = new Usuario();
                usuario.setUsername("Carlos");

                Mascota mascota = new Mascota();
                mascota.setId(mascotaId);
                mascota.setNombre("Benjamin");
                mascota.setUsuario(usuario);

                when(mascotaRepository.findById(mascotaId))
                                .thenReturn(Optional.of(mascota));

                assertThrows(AccessDeniedException.class, () -> {
                        mascotaService.obtenerPorId(mascotaId, "Federico");
                });

        }

        @Test
        void deberiaObtenerMascotaSiPerteneceAlUsuario() {

                Long mascotaId = 1L;
                String username = "Federico";

                Usuario usuario = new Usuario();
                usuario.setUsername(username);

                Mascota mascota = new Mascota();
                mascota.setId(mascotaId);
                mascota.setNombre("Benjamin");
                mascota.setUsuario(usuario);

                when(mascotaRepository.findById(mascotaId))
                                .thenReturn(Optional.of(mascota));

                MascotaResponse resultado = mascotaService.obtenerPorId(mascotaId, username);

                assertEquals("Benjamin", resultado.getNombre());
        }

        @Test
        void deberiaLanzarVacunaVigenteException() {

                Long mascotaId = 1L;
                Long vacunaId = 10L;
                String username = "Federico";

                Usuario usuario = new Usuario();
                usuario.setUsername(username);

                Mascota mascota = new Mascota();
                mascota.setId(mascotaId);
                mascota.setUsuario(usuario);

                Vacuna vacuna = new Vacuna();
                vacuna.setId(vacunaId);
                vacuna.setNombre("Rabia");

                AplicarVacunaRequest request = new AplicarVacunaRequest();
                request.setVacunaId(vacunaId);

                when(mascotaRepository.findById(mascotaId))
                                .thenReturn(Optional.of(mascota));

                when(vacunaService.obtenerPorId(vacunaId))
                                .thenReturn(vacuna);

                when(vacunacionRepository
                                .existsByMascotaIdAndVacunaIdAndFechaVencimientoAfter(
                                                eq(mascotaId),
                                                eq(vacunaId),
                                                any()))
                                .thenReturn(true);

                assertThrows(VacunaVigenteException.class, () -> {
                        mascotaService.aplicarVacuna(mascotaId, request, username, false);
                });

        }

}