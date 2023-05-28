package com.veiculosmg.service.implementacao;

import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.model.repository.CarroRepository;
import com.veiculosmg.service.CarroService;
import com.veiculosmg.utilitarios.FormataNome;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ImplCarroService implements CarroService {

    private final CarroRepository carroRepository;

    public ImplCarroService(CarroRepository carroRepository) {
        this.carroRepository = carroRepository;
    }

    @Override
    public List<Carro> listaEntidades() {
        return carroRepository.findAll();
    }

    @Override
    public Optional<Carro> entidadePorId(Long id) {
        Optional<Carro> carro = carroRepository.findById(id);

        if (carro.isEmpty()) {
            throw new RecursoNaoEncontradoException("Carro com ID: " + id + " Não Encontrado!");
        }

        return carro;
    }

    @Override
    public List<Carro> disponivel() {
        return carroRepository.findAll()
                .stream()
                .filter(Carro::isDisponivel)
                .toList();
    }

    @Override
    public List<Carro> listCategoria(String categoria) {
        return listaCategoria.apply(categoria);
    }

    @Override
    public Carro salvaNovaEntidade(Carro carro) {
        try {
            Carro.formataAtributos(carro);
            return carroRepository.save(carro);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
            throw new AtributoDuplicadoException("Placa informada já está cadastrada!");
        }
    }

    @Override
    public void deletaEntidade(Long id) {
        Optional<Carro> existeCarroId = carroRepository.findById(id);

        if (existeCarroId.isEmpty()) {
            throw new RecursoNaoEncontradoException("Carro com ID: " + id + " Não Encontrado!");
        }

        carroRepository.deleteById(id);
    }

    @Override
    public void updateEntidade(Carro carroAtualizado, Long id) {
        Carro existeCarro = verificaCarroExistente(id);

        verificarPlacasDiferentes(carroAtualizado, existeCarro);

        carroAtualizado.setId(existeCarro.getId());
        Carro.formataAtributos(carroAtualizado);
        carroRepository.save(carroAtualizado);
    }


    /* Privado */

    private void verificarPlacasDiferentes(Carro carroAtualizado, Carro carroJaCadastrado) {
        String placaDoCarro = carroJaCadastrado.getPlaca();
        String placaDoNovoCarro = carroAtualizado.getPlaca();

        if (!placaDoCarro.equalsIgnoreCase(placaDoNovoCarro) && vericaSeExistePlacaCadastrada(carroAtualizado)) {
            throw new AtributoDuplicadoException("Placa informada já está salva em outro veículo!");
        }
    }

    private Carro verificaCarroExistente(Long id) {
        Optional<Carro> existeCarro = carroRepository.findById(id);

        return existeCarro.orElseThrow(() ->
                new RecursoNaoEncontradoException("Carro com ID: " + id + " Não Encontrado!"));
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


