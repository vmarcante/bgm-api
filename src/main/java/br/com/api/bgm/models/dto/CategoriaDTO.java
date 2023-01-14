package br.com.api.bgm.models.dto;

import java.util.List;

import br.com.api.bgm.models.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoriaDTO extends BaseDTO {
    String descricao;
    String comentario;
    List<TransacaoDTO> transacoes;
}
