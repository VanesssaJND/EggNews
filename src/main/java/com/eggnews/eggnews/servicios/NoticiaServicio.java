package com.eggnews.eggnews.servicios;

import com.eggnews.eggnews.DTO.NoticiaDTO;
import com.eggnews.eggnews.DTO.NoticiaMapper;
import com.eggnews.eggnews.entidades.Noticia;
import com.eggnews.eggnews.excepciones.MiException;
import com.eggnews.eggnews.excepciones.ResourceNotFoundException;
import com.eggnews.eggnews.repositorios.NoticiaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticiaServicio {

    private final NoticiaRepositorio noticiaRepositorio;
    @Autowired
    public NoticiaServicio(NoticiaRepositorio noticiaRepositorio) {
        this.noticiaRepositorio = noticiaRepositorio;
    }


    @Transactional
    public NoticiaDTO crearNoticia(NoticiaDTO noticiaDTO) throws MiException {
        validarInformacion(noticiaDTO.getTitulo(), noticiaDTO.getCuerpo());

        Noticia noticia = new Noticia();
        noticia.setTitulo(noticiaDTO.getTitulo());
        noticia.setCuerpo(noticiaDTO.getCuerpo());

        // Guardar la noticia en el repositorio
        Noticia nuevaNoticia = noticiaRepositorio.save(noticia);

        // Mapear la nueva noticia a un DTO y devolverlo en la respuesta
        return NoticiaMapper.INSTANCE.NoticiaANoticiaDTO(nuevaNoticia);
    }





    @Transactional
    public Noticia actualizarNoticia(Long id, String titulo, String cuerpo) throws ResourceNotFoundException, MiException {
        validarInformacion(titulo, cuerpo);

        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            return noticiaRepositorio.save(noticia);
        } else {
            throw new ResourceNotFoundException("Noticia con ID " + id + " no encontrada");
        }
    }

    public Noticia buscarNoticiaPorId(Long id){
        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        return respuesta.orElse(null);

    }

    public Noticia buscarNoticiaPorTitulo(String titulo){
        return noticiaRepositorio.buscarPorTitulo(titulo);


    }

    public List<NoticiaDTO> listarNoticias(){
        List<Noticia> noticias = noticiaRepositorio.findAll();

        return noticias.stream().map(
                NoticiaMapper.INSTANCE::NoticiaANoticiaDTO
        ).collect(Collectors.toList());
    }

    private void validarInformacion(String titulo, String cuerpo) throws MiException {
        if(titulo.isEmpty()){
            throw new MiException("El titulo no puede ser nulo");
        }
        if(cuerpo.isEmpty()){
            throw new MiException("El cuerpo de la noticia no puede estar vacio");
        }
    }


}
