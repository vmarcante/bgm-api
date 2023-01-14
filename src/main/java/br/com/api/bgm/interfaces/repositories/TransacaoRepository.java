package br.com.api.bgm.interfaces.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.bgm.models.entity.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
    
}
