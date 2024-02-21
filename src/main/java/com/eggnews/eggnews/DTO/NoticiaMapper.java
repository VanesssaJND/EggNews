package com.eggnews.eggnews.DTO;

import com.eggnews.eggnews.entidades.Noticia;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoticiaMapper {

    NoticiaMapper INSTANCE = Mappers.getMapper(NoticiaMapper.class);
    NoticiaDTO NoticiaANoticiaDTO(Noticia noticia);
    Noticia NoticiaDTOaNoticia(NoticiaDTO noticiaDTO);
}
