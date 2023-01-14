package br.com.api.bgm.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "TB_CATEGORIA")
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "ID", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private Date dataCriacao;

    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    @Column(name = "COMENTARIO")
    private String comentario;

    @JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categoria", targetEntity = Transacao.class)
    private List<Transacao> transacoes;

}
