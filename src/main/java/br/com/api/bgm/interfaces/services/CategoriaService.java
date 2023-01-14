package br.com.api.bgm.interfaces.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.bgm.interfaces.repositories.CategoriaRepository;
import br.com.api.bgm.interfaces.repositories.TransacaoRepository;
import br.com.api.bgm.interfaces.repositories.custom.TransacaoRepositoryCustom;
import br.com.api.bgm.models.dto.CategoriaDTO;
import br.com.api.bgm.models.entity.Categoria;
import br.com.api.bgm.models.entity.Transacao;
import jakarta.transaction.Transactional;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    TransacaoRepositoryCustom transacaoRepositoryCustom;

    @Autowired
    TransacaoRepository transacaoRepository;

    @Autowired
    TransacaoService transacaoService;

    public CategoriaDTO mapEntityToDTO(Categoria entity) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setComentario(entity.getComentario());
        dto.setDataCriacao(entity.getDataCriacao());
        dto.setTransacoes(entity.getTransacoes().stream().map(x -> transacaoService.mapEntityToDTO(x))
                .collect(Collectors.toList()));
        dto.setId(entity.getId());
        dto.setDescricao(entity.getDescricao());

        return dto;
    }

    public Categoria mapDTOtoEntity(CategoriaDTO dto, boolean alterID) {
        Categoria entity = new Categoria();

        if (alterID) {
            entity.setId(dto.getId() != null ? dto.getId() : UUID.randomUUID());
        }
        entity.setDataCriacao(new Date());
        entity.setComentario(dto.getComentario());
        entity.setDescricao(dto.getDescricao());

        return entity;
    }

    @Transactional
    public CategoriaDTO salvarCategoria(CategoriaDTO categoriaDTO) {
        Categoria entity = this.mapDTOtoEntity(categoriaDTO, true);
        categoriaDTO.setId(entity.getId());
        this.categoriaRepository.save(entity);
        return categoriaDTO;
    }

    @Transactional
    public void deleteById(UUID id) {
        List<Transacao> transacoesDB = this.transacaoRepositoryCustom.listTransacoesByCategoria(id);
        for (Transacao transacao : transacoesDB) {
            transacao.setIdCategoria(null);
            this.transacaoRepository.save(transacao);
        }

        this.categoriaRepository.deleteById(id);
    }

    public List<CategoriaDTO> listAll () {
        return this.categoriaRepository.findAll().stream().map(this::mapEntityToDTO).collect(Collectors.toList());
    }
}
