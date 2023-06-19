package com.veiculosmg.service.implementacaoService;

import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.ElementoEmUmRelacionamentoException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.model.repository.CarroRepository;
import com.veiculosmg.service.interfaceService.CarroService;
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
    public Carro salvaNovaEntidade(Carro carro) {
        log.info("Criação de novo carro iniciada.");
        try {
            log.info("Formatação nome dos atributos iniciada.");
            Carro.formataAtributos(carro);

            carroRepository.save(carro);
            log.info("Criação de novo carro concluída.");
            return carro;
        } catch (DataIntegrityViolationException ex) {
            log.error("Erro. Placa: {}", carro.getPlaca() + " já está cadastrada.", ex);
            throw new AtributoDuplicadoException("Placa: " + carro.getPlaca() + " já está cadastrada!");
        }
    }

    @Override
    public List<Carro> listaEntidades() {
        log.info("Busca lista de carros iniciada.");
        return carroRepository.findAll();
    }

    @Override
    public Optional<Carro> entidadePorId(Long id) {
        return Optional.of(verificaSeCarroExiste(id));
    }

    @Override
    public List<Carro> disponivel() {
        log.info("Busca de carros disponíveis iniciada.");
        return carroRepository.findAll()
                .stream()
                .filter(Carro::isDisponivel)
                .toList();
    }

    @Override
    public List<Carro> listCategoria(String categoria) {
        log.info("Busca de carros {}", categoria.toUpperCase() + " Iniciada.");
        log.info("Busca de carros pela categoria concluída.");
        return filtroCarrosPorCategoria.apply(categoria);
    }

    @Override
    public void updateEntidade(Carro carroAtualizado, Long id) {
        log.info("Atualização do carro Id:{}", id + " iniciada.");
        log.info("Body da requisição Carro:{}", carroAtualizado);
        try {
            Carro existeCarroComId = verificaSeCarroExiste(id);

            log.info("Formatando nome dos atributos do carro atualizado.");
            Carro.formataAtributos(carroAtualizado);

            verificarPlacasDiferentes(carroAtualizado, existeCarroComId);
            carroAtualizado.setId(existeCarroComId.getId());

            log.info("Atualização do carro concluída.");
            carroRepository.save(carroAtualizado);
        } catch (DataIntegrityViolationException ex) {
            log.error("Erro ao atualizar o carro.", ex);
            throw new AtributoDuplicadoException("Placa: " + carroAtualizado.getPlaca() + " já está cadastrada!");
        }
    }

    @Override
    public void deletaEntidade(Long id) {
        log.info("Delete do carro Id:{}", id + " iniciada.");
        Carro carro = verificaSeCarroExiste(id);

        if (!carro.isDisponivel()) throw new ElementoEmUmRelacionamentoException("Carro está relacionado a um Aluguel.");

        log.info("Delete do carro concluído");
        carroRepository.deleteById(id);
    }


    /* Privado */

    private void verificarPlacasDiferentes(Carro carroAtualizado, Carro carroJaCadastrado) {
        log.info("Verificando se placa: {}", carroAtualizado.getPlaca() + " já está cadastrada.");
        String placaDoCarroJaCadastrado = carroJaCadastrado.getPlaca();
        String placaDoNovoCarro = carroAtualizado.getPlaca();

        if (vericaSeExistePlacaCadastrada(carroAtualizado) && !placaDoCarroJaCadastrado.equalsIgnoreCase(placaDoNovoCarro)) {
            log.warn("Placa: {}", carroAtualizado.getPlaca() + " está cadastrada em outro veículo!");
            throw new AtributoDuplicadoException("Placa informada já está salva em outro veículo!");
        }
    }

    private Carro verificaSeCarroExiste(Long id) {
        log.info("Verificando se o carro existe.");
        Optional<Carro> existeCarroComOIdInformado = carroRepository.findById(id);

        if (existeCarroComOIdInformado.isEmpty()) {
            log.info("Carro com Id: {}", id + " não encontrado!");
            throw new RecursoNaoEncontradoException("Carro com Id: " + id + " não Encontrado!");
        }

        log.info("Verificação se o carro existe concluída.");
        return existeCarroComOIdInformado.get();
    }

    private boolean vericaSeExistePlacaCadastrada(Carro carro) {
        List<String> placas = carroRepository.findAll()
                .stream()
                .map(Carro::getPlaca)
                .toList();

        return placas.contains(carro.getPlaca());
    }

    private final Function<String, List<Carro>> filtroCarrosPorCategoria = nomeCategoria ->
            listaEntidades()
                    .stream()
                    .filter(carro -> carro.getCategoria().equalsIgnoreCase(nomeCategoria))
                    .toList();
}


