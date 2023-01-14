package br.com.api.bgm.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.bgm.controllers.base.BaseController;
import br.com.api.bgm.interfaces.repositories.TransacaoRepository;
import br.com.api.bgm.interfaces.repositories.custom.TransacaoRepositoryCustom;
import br.com.api.bgm.interfaces.services.TransacaoService;
import br.com.api.bgm.models.dto.TransacaoDTO;
import br.com.api.bgm.models.dto.filtro.TransacaoFiltroDTO;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/transacao")
public class TransacaoController extends BaseController {

    @Autowired
    TransacaoService transacaoService;

    @Autowired
    TransacaoRepository transacaoRepository;

    @Autowired
    TransacaoRepositoryCustom transacaoRepositoryCustom;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody TransacaoDTO transacaoDTO) throws Exception {
        try{
            return response(transacaoService.salvarTransacao(transacaoDTO));
        } catch (Exception e) {
            throw new Exception("Erro ao salvar a Transação.");
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<Object> listByFilter(@RequestBody TransacaoFiltroDTO filtro) throws Exception {
        try{
            return response(transacaoService.filterTransacoes(filtro));
        } catch (Exception e) {
            throw new Exception("Erro ao listar a(s) transacão(ões).");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable("id") UUID id) throws Exception {
        try{
            transacaoService.deleteById(id);
            return response(HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao deletar a transacão.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateTransacao(@RequestBody TransacaoDTO dto) throws Exception {
        try{
            return response(transacaoService.alterarTransacao(dto));
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar a transacão.");
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAll() throws Exception {
        try{
            TransacaoFiltroDTO filtro = new TransacaoFiltroDTO();
            return response(transacaoService.filterTransacoes(filtro));
        } catch (Exception e) {
            throw new Exception("Erro ao listar a(s) transação(ões).");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id) throws Exception {
        try{
            return response(transacaoRepository.findById(id));
        } catch (Exception e) {
            throw new Exception("Erro ao listar a transação.");
        }
    }

    

    
    
}
