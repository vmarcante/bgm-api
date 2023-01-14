package br.com.api.bgm.models.dto;

import java.util.List;

import lombok.Data;

@Data
public class MesDTO {
    private List<TransacaoDTO> transacoes;
    private Double montanteMes;
    private String numeroMes;
    private String nomeMes;
    private String ano;
    
}
