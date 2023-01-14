package br.com.api.bgm.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.bgm.controllers.base.BaseController;
import br.com.api.bgm.interfaces.repositories.CategoriaRepository;
import br.com.api.bgm.interfaces.services.CategoriaService;
import br.com.api.bgm.models.dto.CategoriaDTO;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/categoria")
public class CategoriaController extends BaseController {

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    CategoriaRepository categoriaRepository;


    @PostMapping
    public ResponseEntity<Object> save(@RequestBody CategoriaDTO categoriaDTO) throws Exception {
        try{
            return response(categoriaService.salvarCategoria(categoriaDTO));
        } catch (Exception e) {
            throw new Exception("Erro ao salvar a Transação.");
        }
    }

    // @PostMapping("/filter")
    // public ResponseEntity<Object> listByFilter(@RequestBody CategoriaFiltroDTO filtro) throws Exception {
    //     try{
    //         return response(categoriaService.filterTransacoes(filtro));
    //     } catch (Exception e) {
    //         throw new Exception("Erro ao listar a(s) transacão(ões).");
    //     }
    // }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable("id") UUID id) throws Exception {
        try{
            categoriaService.deleteById(id);
            return response(HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao deletar a transacão.");
        }
    }

    // @PutMapping("/update")
    // public ResponseEntity<Object> updateCategoria(@RequestBody CategoriaDTO dto) throws Exception {
    //     try{
    //         return response(categoriaService.alterarCategoria(dto));
    //     } catch (Exception e) {
    //         throw new Exception("Erro ao atualizar a transacão.");
    //     }
    // }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAll() throws Exception {
        try{
            // CategoriaFiltroDTO filtro = new CategoriaFiltroDTO();
            return response(categoriaService.listAll());
        } catch (Exception e) {
            throw new Exception("Erro ao listar a(s) transação(ões).");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id) throws Exception {
        try{
            return response(categoriaRepository.findById(id));
        } catch (Exception e) {
            throw new Exception("Erro ao listar a transação.");
        }
    }

    

    
    
}
