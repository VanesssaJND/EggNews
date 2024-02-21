package com.eggnews.eggnews;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.eggnews.eggnews.DTO.NoticiaDTO;
import com.eggnews.eggnews.DTO.NoticiaMapper;
import com.eggnews.eggnews.entidades.Noticia;
import org.junit.jupiter.api.Test;

class MapStrutTest {

    @Test
    public void testMapeo() {


        Noticia noticia= new Noticia(65L,"agua","no hay agua");

        NoticiaDTO noticiaDto= NoticiaMapper.INSTANCE.NoticiaANoticiaDTO(noticia);

        assertEquals("agua",noticiaDto.getTitulo());
        assertEquals("no hay agua",noticiaDto.getCuerpo());

    }
}