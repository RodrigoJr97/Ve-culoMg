package com.veiculosmg.service.implementacao;

import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Endereco;
import com.veiculosmg.model.repository.EnderecoRepository;
import com.veiculosmg.service.EnderecoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ImplEnderecoService implements EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public ImplEnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }


    @Override
    public Endereco salvaNovaEntidade(Endereco endereco) {
        log.info("Criação de novo endereco iniciada.");
        return enderecoRepository.save(endereco);
    }

    @Override
    public List<Endereco> listaEntidades() {
        log.info("Busca lista de endereços iniciada.");
        return enderecoRepository.findAll();
    }

    @Override
    public Optional<Endereco> entidadePorId(Long id) {
        return Optional.of(verificaSeEnderecoExiste(id));
    }

    @Override
    public void updateEntidade(Endereco enderecoAtualizado, Long id) {
        log.info("Atualização do endereço Id:{}", id + " iniciada.");
        Endereco endereco = verificaSeEnderecoExiste(id);

        enderecoAtualizado.setId(endereco.getId());

        log.info("Atualização do endereço concluída.");
        enderecoRepository.save(enderecoAtualizado);
    }

    @Override
    public void deletaEntidade(Long id) {
        log.info("Delete do endereço Id:{}", id + " iniciada.");
        verificaSeEnderecoExiste(id);

        log.info("Delete do endereço concluído.");
        enderecoRepository.deleteById(id);
    }

    /* Privado */

    private Endereco verificaSeEnderecoExiste(Long id) {
        log.info("Verificando se endereço existe.");
        Optional<Endereco> existeEnderecoComIdInformado = enderecoRepository.findById(id);

        if (existeEnderecoComIdInformado.isEmpty()) {
            log.info("Endereço com Id: {}", id + " não encontrado!");
            throw new RecursoNaoEncontradoException("Endereço com Id: " + id + " Não Encontrado!");
        }

        log.info("Verificação se o endereço existe concluída.");
        return existeEnderecoComIdInformado.get();
    }

}
