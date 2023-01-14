package br.com.api.bgm.models.dto.filtro;

import br.com.api.bgm.models.dto.base.BaseFiltroDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoriaFiltroDTO extends BaseFiltroDTO {
    String descricao;
}
