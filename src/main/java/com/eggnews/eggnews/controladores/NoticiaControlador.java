package com.eggnews.eggnews.controladores;

import com.eggnews.eggnews.DTO.NoticiaDTO;
import com.eggnews.eggnews.DTO.NoticiaMapper;
import com.eggnews.eggnews.entidades.Noticia;
import com.eggnews.eggnews.excepciones.MiException;
import com.eggnews.eggnews.excepciones.ResourceNotFoundException;
import com.eggnews.eggnews.servicios.NoticiaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class NoticiaControlador {

    private final NoticiaServicio noticiaServicio;

    public NoticiaControlador(NoticiaServicio noticiaServicio){
        this.noticiaServicio = noticiaServicio;
    }

    @GetMapping("/mostrartodas")
    public ResponseEntity<List<NoticiaDTO>> mostrarTodasLasNoticias() {
        try {
            List<NoticiaDTO> noticias = noticiaServicio.listarNoticias();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(noticias);
        } catch (Exception e) {
            System.err.println("Error al obtener todas las noticias: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(path = "/crearnoticia",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoticiaDTO> crearNoticia(@RequestBody NoticiaDTO noticiaDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(noticiaServicio.crearNoticia(noticiaDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //Sobre Optional.ofNullable:
    // Este método está diseñado para convertir un valor potencialmente nulo en un Optional.
    // Si el valor proporcionado es null, fNullable crea un Optional vacío (Optional.empty());
    // de lo contrario, crea un Optional que contiene el valor proporcionado.

    @GetMapping("noticias/{titulo}")
    public ResponseEntity<NoticiaDTO> verNoticiaPorNombre(@PathVariable String titulo)  {
        Optional<Noticia>optionalNoticia = Optional.ofNullable(noticiaServicio.buscarNoticiaPorTitulo(titulo));
        Noticia noticia = optionalNoticia.orElseThrow(() -> new ResourceNotFoundException("Noticia con el titulo " + titulo + " no encontrada"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(NoticiaMapper.INSTANCE.NoticiaANoticiaDTO(noticia));
    }

    @GetMapping("/noticias/{id}")
    public ResponseEntity<NoticiaDTO> verNoticiaPorId(@PathVariable Long id) {
        //noticiaServicio.buscarNoticiaPorId(id) puede potencialmente arrojar nulo
        Optional<Noticia> optionalNoticia = Optional.ofNullable(noticiaServicio.buscarNoticiaPorId(id));
        Noticia noticia = optionalNoticia.orElseThrow(() -> new ResourceNotFoundException("Noticia con ID " + id + " no encontrada"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(NoticiaMapper.INSTANCE.NoticiaANoticiaDTO(noticia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticiaDTO> actualizarNoticia(@PathVariable long id, @RequestBody NoticiaDTO detallesNoticiaDTO) {
        try {
            NoticiaDTO noticiaActualizadaDTO = NoticiaMapper.INSTANCE.NoticiaANoticiaDTO(noticiaServicio.actualizarNoticia(id, detallesNoticiaDTO.getTitulo(), detallesNoticiaDTO.getCuerpo()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(noticiaActualizadaDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MiException e) {
            throw new RuntimeException(e);
        }
    }
}


