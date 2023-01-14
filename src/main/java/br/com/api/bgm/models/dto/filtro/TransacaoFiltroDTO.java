package br.com.api.bgm.models.dto.filtro;

import java.util.UUID;

import br.com.api.bgm.models.dto.base.BaseFiltroDTO;
import br.com.api.bgm.models.enums.TransacaoType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransacaoFiltroDTO extends BaseFiltroDTO {
    private String ano;
    private String mes;
    private String descricao;
    private String idCategoria;
    private TransacaoType tipoTransacao;
}
