package br.com.api.bgm.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import br.com.api.bgm.models.enums.TransacaoType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "TB_TRANSACAO")
public class Transacao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private Date dataCriacao;

    @Column(name= "DATA_TRANSACAO", nullable = false)
    private String dataTransacao;

    @Enumerated
    @Column(name = "TIPO_TRANSACAO", nullable = false)
    private TransacaoType tipoTransacao;

    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    @Column(name = "COMENTARIO")
    private String comentario;

    @Column(name= "VALOR", nullable = false)
    private String valor;

    @Column(name = "PARCELAS")
    private Integer parcelas;

    @Column(name = "PARCELA_ATUAL")
    private Integer parcelaAtual;

    @Column(name = "RECORRENTE")
    private Boolean recorrente;

    @Column(name = "TRANSACAO_ORIGINAL", nullable = true)
    private UUID idTransacaoOriginal;

    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA", insertable = false, updatable = false)
    private Categoria categoria;

    @Column(name="ID_CATEGORIA")
    private UUID idCategoria;

}
