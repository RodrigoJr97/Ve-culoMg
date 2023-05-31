package com.veiculosmg.service.implementacao;

import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.model.repository.CarroRepository;
import com.veiculosmg.service.CarroService;
import com.veiculosmg.utilitarios.FormataNome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
public class ImplCarroService implements CarroService {

    private final CarroRepository carroRepository;

    public ImplCarroService(CarroRepository carroRepository) {
        this.carroRepository = carroRepository;
    }

    @Override
    public List<Carro> listaEntidades() {
        log.info("Iniciando busca da lista de carros.");

        if (carroRepository.findAll().isEmpty()) {
            log.info("Lista de Carros vazia.");
            throw new RecursoNaoEncontradoException("Nenhum Carro Cadastrado!");
        }

        log.info("Busca de lista de carros concluída.");
        return carroRepository.findAll();
    }

    @Override
    public Optional<Carro> entidadePorId(Long id) {
        return Optional.ofNullable(verificaCarroExistente(id));
    }

    @Override
    public List<Carro> disponivel() {
        log.info("Iniciando busca carros disponíveis.");
        return carroRepository.findAll()
                .stream()
                .filter(Carro::isDisponivel)
                .toList();
    }

    @Override
    public List<Carro> listCategoria(String categoria) {
        log.info("Iniciando busca carros {}.", categoria.toUpperCase());
        return listaCategoria.apply(categoria);
    }

    @Override
    public Carro salvaNovaEntidade(Carro carro) {
        log.info("Iniciando a criação de um novo Carro.");
        try {
            log.info("Iniciando formatação do nome dos atributos.");
            Carro.formataAtributos(carro);

            carroRepository.save(carro);
            log.info("Criação de novo Carro concluído.");
            return carro;
        } catch (DataIntegrityViolationException ex) {
            log.error("Erro. Placa: {}", carro.getPlaca() + " já está cadastrada.", ex);
            throw new AtributoDuplicadoException("Placa: " + carro.getPlaca() + " já está cadastrada!");
        }

    }

    @Override
    public void deletaEntidade(Long id) {
        log.info("Iniciando o delete de um Carro.");
        Optional<Carro> existeCarroId = carroRepository.findById(id);

        if (existeCarroId.isEmpty()) {
            log.warn("Carro com ID:{} ", id + " não encontrado!");
            throw new RecursoNaoEncontradoException("Carro com ID: " + id + " Não Encontrado!");
        }

        log.info("Delete do Carro com ID:{} ", id + " concluída!");
        carroRepository.deleteById(id);
    }

    @Override
    public void updateEntidade(Carro carroAtualizado, Long id) {
        log.info("Iniciando atualização do Carro ID: {}", id);
        try {
            Carro existeCarro = verificaCarroExistente(id);

            log.info("Formatando nome dos atributos do Carro atualizado.");
            Carro.formataAtributos(carroAtualizado);

            verificarPlacasDiferentes(carroAtualizado, existeCarro);
            carroAtualizado.setId(existeCarro.getId());

            log.info("Atualização do carro concluída.");
            carroRepository.save(carroAtualizado);
        } catch (DataIntegrityViolationException ex) {
            log.error("Erro ao atualizar o Carro.", ex);
            throw new AtributoDuplicadoException("Placa informada já está cadastrada em outro veículo!");
        }
    }


    /* Privado */

    private void verificarPlacasDiferentes(Carro carroAtualizado, Carro carroJaCadastrado) {
        log.info("Iniciando a verificação se Placa: {}", carroAtualizado.getPlaca() + " já está cadastrada.");
        String placaDoCarro = carroJaCadastrado.getPlaca();
        String placaDoNovoCarro = carroAtualizado.getPlaca();

        if (vericaSeExistePlacaCadastrada(carroAtualizado) && !placaDoCarro.equalsIgnoreCase(placaDoNovoCarro)) {
            log.warn("Placa: {}", carroAtualizado.getPlaca() + " está cadastrada em outro veículo!");
            throw new AtributoDuplicadoException("Placa informada já está salva em outro veículo!");
        }
    }

    private Carro verificaCarroExistente(Long id) {
        log.info("Verificando se o carro existe.");
        Optional<Carro> existeCarro = carroRepository.findById(id);

        if (existeCarro.isEmpty()) {
            log.info("Carro com ID:{}", id + " não encontrado!");
            throw new RecursoNaoEncontradoException("Carro com ID: " + id + " Não Encontrado!");
        }

        return existeCarro.get();
    }

    private boolean vericaSeExistePlacaCadastrada(Carro carro) {
        List<String> placas = carroRepository.findAll()
                .stream()
                .map(Carro::getPlaca)
                .toList();

        return placas.contains(carro.getPlaca());
    }

    private final Function<String, List<Carro>> listaCategoria = nomeCategoria ->
            listaEntidades()
                    .stream()
                    .filter(carro -> carro.getCategoria().equalsIgnoreCase(nomeCategoria))
                    .toList();
}


