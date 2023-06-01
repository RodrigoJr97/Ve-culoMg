package com.veiculosmg.service.implementacao;

import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.model.repository.CarroRepository;
import com.veiculosmg.service.CarroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
        log.info("Busca lista de carros Iniciada.");

        if (carroRepository.findAll().isEmpty()) {
            log.info("Lista de Carros vazia.");
            throw new RecursoNaoEncontradoException("Nenhum Carro Cadastrado!");
        }

        log.info("Busca lista de carros Concluída.");
        return carroRepository.findAll();
    }

    @Override
    public Optional<Carro> entidadePorId(Long id) {
        return Optional.ofNullable(verificaSeCarroExiste(id));
    }

    @Override
    public List<Carro> disponivel() {
        log.info("Busca de carros disponíveis Iniciada.");
        return carroRepository.findAll()
                .stream()
                .filter(Carro::isDisponivel)
                .toList();
    }

    @Override
    public List<Carro> listCategoria(String categoria) {
        log.info("Busca de carros {}", categoria.toUpperCase() + " Iniciada.");
        return listaCategoria.apply(categoria);
    }

    @Override
    public Carro salvaNovaEntidade(Carro carro) {
        log.info("Criação de novo carro Iniciada.");
        try {
            log.info("Formatação nome dos atributos Iniciada.");
            Carro.formataAtributos(carro);

            carroRepository.save(carro);
            log.info("Criação de novo Carro concluída.");
            return carro;
        } catch (DataIntegrityViolationException ex) {
            log.error("Erro. Placa: {}", carro.getPlaca() + " já está cadastrada.", ex);
            throw new AtributoDuplicadoException("Placa: " + carro.getPlaca() + " já está cadastrada!");
        }

    }

    @Override
    public void deletaEntidade(Long id) {
        log.info("Delete de carro Iniciado.");
        Optional<Carro> existeCarroComId = carroRepository.findById(id);

        if (existeCarroComId.isEmpty()) {
            log.warn("Carro com ID: {} ", id + " não encontrado!");
            throw new RecursoNaoEncontradoException("Carro com Id: " + id + " Não Encontrado!");
        }

        log.info("Delete do Carro com Id: {} ", id + " concluída!");
        carroRepository.deleteById(id);
    }

    @Override
    public void updateEntidade(Carro carroAtualizado, Long id) {
        log.info("Atualização do Carro Id:{}", id + " Iniciada.");
        try {
            Carro existeCarroComId = verificaSeCarroExiste(id);

            log.info("Formatando nome dos atributos do Carro atualizado.");
            Carro.formataAtributos(carroAtualizado);

            verificarPlacasDiferentes(carroAtualizado, existeCarroComId);
            carroAtualizado.setId(existeCarroComId.getId());

            log.info("Atualização do carro concluída.");
            carroRepository.save(carroAtualizado);
        } catch (DataIntegrityViolationException ex) {
            log.error("Erro ao atualizar o Carro.", ex);
            throw new AtributoDuplicadoException("Placa informada já está cadastrada em outro veículo!");
        }
    }


    /* Privado */

    private void verificarPlacasDiferentes(Carro carroAtualizado, Carro carroJaCadastrado) {
        log.info("Verificando se Placa: {}", carroAtualizado.getPlaca() + " já está cadastrada.");
        String placaDoCarro = carroJaCadastrado.getPlaca();
        String placaDoNovoCarro = carroAtualizado.getPlaca();

        if (vericaSeExistePlacaCadastrada(carroAtualizado) && !placaDoCarro.equalsIgnoreCase(placaDoNovoCarro)) {
            log.warn("Placa: {}", carroAtualizado.getPlaca() + " está cadastrada em outro veículo!");
            throw new AtributoDuplicadoException("Placa informada já está salva em outro veículo!");
        }
    }

    private Carro verificaSeCarroExiste(Long id) {
        log.info("Verificando se o carro existe.");
        Optional<Carro> existeCarroComId = carroRepository.findById(id);

        if (existeCarroComId.isEmpty()) {
            log.info("Carro com Id: {}", id + " não encontrado!");
            throw new RecursoNaoEncontradoException("Carro com Id: " + id + " Não Encontrado!");
        }

        return existeCarroComId.get();
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


