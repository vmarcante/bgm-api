package br.com.api.bgm.interfaces.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.bgm.interfaces.repositories.TransacaoRepository;
import br.com.api.bgm.interfaces.repositories.custom.TransacaoRepositoryCustom;
import br.com.api.bgm.models.dto.MesDTO;
import br.com.api.bgm.models.dto.TransacaoDTO;
import br.com.api.bgm.models.dto.filtro.TransacaoFiltroDTO;
import br.com.api.bgm.models.entity.Transacao;
import br.com.api.bgm.models.enums.TransacaoType;
import jakarta.transaction.Transactional;

@Service
public class TransacaoService {

    @Autowired
    TransacaoRepository transacaoRepository;

    @Autowired
    TransacaoRepositoryCustom transacaoRepositoryCustom;

    private String[] nomeMeses = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto",
            "Setembro", "Outubro", "Novembro", "Dezembro" };

    public TransacaoDTO mapEntityToDTO(Transacao entity) {
        TransacaoDTO dto = new TransacaoDTO();
        dto.setId(entity.getId());
        dto.setDataTransacao(entity.getDataTransacao());
        dto.setDataCriacao(entity.getDataCriacao());
        dto.setParcelas(entity.getParcelas());
        dto.setDescricao(entity.getDescricao());
        dto.setRecorrente(entity.getRecorrente());
        dto.setValor(entity.getValor());
        dto.setTipoTransacao(entity.getTipoTransacao());
        dto.setComentario(entity.getComentario());
        dto.setParcelaAtual(entity.getParcelaAtual());
        dto.setIdTransacaoOriginal(entity.getIdTransacaoOriginal());
        dto.setNomeCategoria(entity.getCategoria() != null ? entity.getCategoria().getDescricao() : null);
        dto.setIdCategoria(entity.getCategoria() != null ? entity.getCategoria().getId() : null);
        return dto;
    }

    public Transacao mapDTOtoEntity(TransacaoDTO dto, boolean alterID) {
        Transacao entity = new Transacao();
        LocalDateTime dataAtual = LocalDateTime.now();

        if (alterID) {
            entity.setId(dto.getId() != null ? dto.getId() : UUID.randomUUID());
        }
        entity.setDataCriacao(new Date());
        entity.setParcelas(dto.getParcelas());
        entity.setValor(dto.getValor());
        entity.setTipoTransacao(dto.getTipoTransacao());
        entity.setRecorrente(dto.getRecorrente());
        entity.setComentario(dto.getComentario());
        entity.setParcelaAtual(dto.getParcelaAtual());
        entity.setDataTransacao(
                dto.getDataTransacao() != null && !dto.getDataTransacao().equals("") ? dto.getDataTransacao()
                        : dataAtual.getDayOfMonth() + "/" + dataAtual.getMonthValue() + "/" + dataAtual.getYear());
        entity.setDescricao(dto.getDescricao());
        entity.setIdTransacaoOriginal(dto.getIdTransacaoOriginal());
        entity.setIdCategoria(dto.getIdCategoria());

        return entity;
    }

    @Transactional
    public TransacaoDTO salvarTransacao(TransacaoDTO transacaoDTO) {
        if (transacaoDTO.getParcelas() != null && transacaoDTO.getParcelas() > 1) {
            Integer numeroParcelas = transacaoDTO.getParcelas();
            Double valorOriginal = Double.valueOf(transacaoDTO.getValor());
            String valorParcela = String.valueOf(valorOriginal / numeroParcelas);

            transacaoDTO.setValor(valorParcela);
            for (int index = 1; index <= numeroParcelas; index++) {
                transacaoDTO.setParcelaAtual(index);
                Transacao transacaoParcial = this.mapDTOtoEntity(transacaoDTO, true);
                transacaoRepository.save(transacaoParcial);

                if (index == numeroParcelas) {  break;  }
                if (index == 1) { transacaoDTO.setIdTransacaoOriginal(transacaoParcial.getId()); }

                transacaoDTO.setId(null);

                String[] dataParcela = transacaoDTO.getDataTransacao().split("\\/");
                Integer mesParcela = Integer.valueOf(dataParcela[1]);
                Integer anoParcela = Integer.valueOf(dataParcela[2]);

                if (mesParcela != 12) {
                    mesParcela++;
                } else {
                    mesParcela = 1;
                    anoParcela++;
                }

                String novaDataParcela = dataParcela[0] + (mesParcela < 10 ? "/0" : "/") + String.valueOf(mesParcela) + "/" + String.valueOf(anoParcela);
                transacaoDTO.setDataTransacao(novaDataParcela);
            }
        } else {
            Transacao transacao = this.mapDTOtoEntity(transacaoDTO, true);
            transacaoRepository.save(transacao);
        }

        return transacaoDTO;
    }

    public Boolean alterarTransacao(TransacaoDTO transacaoDTO) throws Exception {
        List<Transacao> transacaoDB = this.transacaoRepositoryCustom.selectAllParcelasTransacao(transacaoDTO.getId());
        if (transacaoDB == null || (transacaoDB != null && transacaoDB.isEmpty())) {
            throw new Exception("Erro ao atualizar transação, ID não encontrado");
        }

        if (transacaoDB.get(0).getParcelas() > 1L) {
            deletarTransacoesRelacionadas(transacaoDTO.getId());
        }

        salvarTransacao(transacaoDTO);

        return true;
    }

    private MesDTO criarMes(Integer mes, List<TransacaoDTO> transacoes) {
        MesDTO mesDTO = new MesDTO();
        Double montante = Double.valueOf(0);
        String ano = transacoes.get(0).getDataTransacao().split("\\/")[2];

        for (TransacaoDTO transacao : transacoes) {
            if (transacao.getTipoTransacao().equals(TransacaoType.despesa)) {
            montante -= Double.valueOf(transacao.getValor());

            } else {
            montante += Double.valueOf(transacao.getValor());

            }
        }

        mesDTO.setNomeMes(nomeMeses[mes - 1]);
        mesDTO.setNumeroMes(mes < 10 ? "0" + mes : mes.toString());
        mesDTO.setMontanteMes(montante);
        mesDTO.setTransacoes(transacoes);
        mesDTO.setAno(ano);

        return mesDTO;
    }

    public Object filterTransacoes(TransacaoFiltroDTO filtroDTO) {
        List<MesDTO> mesesDTO = new ArrayList<MesDTO>();
        List<Integer> mesesDetectados = new ArrayList<Integer>();

        if (filtroDTO.getAno() == null || (filtroDTO.getAno() != null && filtroDTO.getAno().equals(""))) {
            filtroDTO.setAno(String.valueOf(LocalDateTime.now().getYear()));
        }

        List<TransacaoDTO> transacaoDTOList = transacaoRepositoryCustom.filter(filtroDTO).stream()
                .map(x -> this.mapEntityToDTO(x)).collect(Collectors.toList());

        if (transacaoDTOList != null && !transacaoDTOList.isEmpty()) {
            boolean contemRecorrente = !transacaoDTOList.stream()
                    .noneMatch(x -> x.getRecorrente().equals(Boolean.TRUE));

            for (TransacaoDTO transacaoDTO : transacaoDTOList) {
                Integer mesTransacao = Integer.valueOf(transacaoDTO.getDataTransacao().split("\\/")[1]);

                if (contemRecorrente && transacaoDTO.getRecorrente().equals(Boolean.TRUE)) {
                    for (int i = mesTransacao; i <= 12; i++) {
                        if (!mesesDetectados.contains(i)) {
                            mesesDetectados.add(i);
                        }
                    }
                    break;
                }

                if (!mesesDetectados.contains(mesTransacao)) {
                    mesesDetectados.add(mesTransacao);
                }
            }

            for (Integer mesDetectado : mesesDetectados) {
                List<TransacaoDTO> transacoesMes = new ArrayList<TransacaoDTO>();

                for (TransacaoDTO transacao : transacaoDTOList) {
                    Integer mesTransacao = Integer.valueOf(transacao.getDataTransacao().split("\\/")[1]);
                    if ((mesTransacao == mesDetectado) || (transacao.getRecorrente() != null
                            && transacao.getRecorrente().equals(Boolean.TRUE) && mesDetectado >= mesTransacao)) {
                        transacoesMes.add(transacao);
                    }
                }

                mesesDTO.add(this.criarMes(mesDetectado, transacoesMes));
            }
        }

        return mesesDTO;

    }

    @Transactional
    public void deleteById(UUID id) throws Exception {
        Optional<Transacao> transacaoDB = this.transacaoRepository.findById(id);
        if (transacaoDB == null || (transacaoDB != null && !transacaoDB.isPresent())) {
            throw new Exception("Erro ao deletar transação, ID não encontrado");
        }

        Transacao transacao = transacaoDB.get();
        if (transacao.getParcelas() != null && transacao.getParcelas() > 1) {
            this.deletarTransacoesRelacionadas(
                    transacao.getIdTransacaoOriginal() != null ? transacao.getIdTransacaoOriginal() : id);
        } else {
            this.transacaoRepository.deleteById(id);
        }
    }

    private void deletarTransacoesRelacionadas(UUID id) {
        List<Transacao> transacoes = this.transacaoRepositoryCustom.selectAllParcelasTransacao(id);
        for (Transacao parcela : transacoes) {
            this.transacaoRepository.delete(parcela);
        }
    }
}
