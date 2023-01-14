package br.com.api.bgm.models.dto;

import java.util.UUID;

import br.com.api.bgm.models.dto.base.BaseDTO;
import br.com.api.bgm.models.enums.TransacaoType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransacaoDTO extends BaseDTO {
    private String dataTransacao;
    private String descricao;
    private String comentario;
    private String valor;
    private TransacaoType tipoTransacao;
    private Integer parcelas;
    private Integer parcelaAtual;
    private Boolean recorrente;
    private String nomeCategoria;
    private UUID idCategoria;
    private UUID idTransacaoOriginal;
}
