package br.com.api.bgm.models.dto.base;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class BaseDTO {
    private UUID id;
    private Date dataCriacao; 
}
