package br.com.api.bgm.interfaces.repositories.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.api.bgm.models.dto.filtro.TransacaoFiltroDTO;
import br.com.api.bgm.models.entity.Transacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class TransacaoRepositoryCustom {

    @PersistenceContext
    protected EntityManager entityManager;

    public List<Transacao> selectAllParcelasTransacao(UUID id) {
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append(" SELECT transacao ");
        sqlQuery.append(" FROM Transacao transacao ");
        sqlQuery.append(" WHERE ");
        sqlQuery.append(" transacao.id = '" + id + "'");
        sqlQuery.append(" OR transacao.idTransacaoOriginal = '" + id + "'");

        return entityManager.createQuery(sqlQuery.toString()).getResultList();
    }

    public List<Transacao> filter(TransacaoFiltroDTO filtro) {
        List<Transacao> transacoes = new ArrayList<Transacao>();

        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append(" SELECT transacao ");
        sqlQuery.append(" FROM Transacao transacao ");
        sqlQuery.append(" WHERE 1=1 ");

        if (filtro.getId() != null) {
            sqlQuery.append(" AND transacao.id = " + filtro.getId() + " ");
        }

        if (filtro.getTipoTransacao() != null) {
            sqlQuery.append(" AND transacao.tipoTransacao LIKE '" + filtro.getTipoTransacao() + "' ");
        }

        if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
            sqlQuery.append(" AND transacao.descricao LIKE '%" + filtro.getDescricao() + "%' ");
        }

        if (filtro.getMes() != null && !filtro.getMes().equals("")) {
            if (filtro.getAno() != null && !filtro.getAno().equals("")) {
                sqlQuery.append(
                        " AND transacao.dataTransacao LIKE %/" + filtro.getMes() + "/" + filtro.getAno() + "' ");
            } else {
                sqlQuery.append(" AND transacao.dataTransacao LIKE '%/" + filtro.getMes() + "/%' ");
            }
        }

        if (filtro.getAno() != null && !filtro.getAno().equals("")
                && (filtro.getMes() == null || (filtro.getMes() != null && !filtro.getMes().equals("")))) {
            sqlQuery.append(" AND transacao.dataTransacao LIKE '%/" + filtro.getAno() + "' ");
        }

        if (filtro.getIdCategoria() != null && !filtro.getIdCategoria().equals("")) {
            sqlQuery.append(" AND transacao.idCategoria LIKE '" + filtro.getIdCategoria() + "' ");
        }

        sqlQuery.append(" ORDER BY transacao.dataTransacao ASC ");

        Query query = entityManager.createQuery(sqlQuery.toString());

        transacoes = query.getResultList();

        return transacoes;
    }

    public List<Transacao> listTransacoesByCategoria(UUID idCategoria) {
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append(" SELECT transacao ");
        sqlQuery.append(" FROM Transacao transacao ");
        sqlQuery.append(" WHERE transacao.idCategoria = '" + idCategoria + "'");
        
        return entityManager.createQuery(sqlQuery.toString()).getResultList();
    }
}
