package com.veiculosmg.service.implementacaoService;

import com.veiculosmg.dto.AluguelDTO;
import com.veiculosmg.dto.AluguelResponseDTO;
import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Aluguel;
import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.model.entity.Cliente;
import com.veiculosmg.model.enums.StatusAluguel;
import com.veiculosmg.model.repository.AluguelRepository;
import com.veiculosmg.model.repository.CarroRepository;
import com.veiculosmg.model.repository.ClienteRepository;
import com.veiculosmg.service.interfaceService.AluguelService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class ImplAluguelService implements AluguelService {

    private final AluguelRepository aluguelRepository;
    private final ClienteRepository clienteRepository;
    private final CarroRepository carroRepository;

    @Override
    public Aluguel criarAluguel(AluguelDTO aluguelDTO) {
        verificaSeClienteExiste(aluguelDTO.getNomeCliente(), aluguelDTO.getCpfCliente());
        verificaSeCarroExiste(aluguelDTO.getPlacaCarro());

        Aluguel novoAluguel = new Aluguel();
        Cliente cliente = clienteRepository.findByCpf(aluguelDTO.getCpfCliente());
        Carro carro = carroRepository.findByPlaca(aluguelDTO.getPlacaCarro());

        List<String> listaAux = verificacaoSeClienteECarroEstaoDisponiveis(cliente.getCpf(), carro.getPlaca());

        try {
            cliente.setDisponivel(false);
            carro.setDisponivel(false);

            LocalDate dataFimAux = LocalDate.now().plusDays(aluguelDTO.getDiasAluguel());
            BigDecimal valorTotal = calculaValorTotalAluguel(carro.getValorDiaria(), aluguelDTO.getDiasAluguel());

            novoAluguel.setCarro(carro);
            novoAluguel.setCliente(cliente);
            novoAluguel.setDataInicio(LocalDate.now());
            novoAluguel.setDataFim(dataFimAux);
            novoAluguel.setValorTotal(valorTotal);
            novoAluguel.setStatusAluguel(StatusAluguel.INICIADO);

            aluguelRepository.save(novoAluguel);

            return novoAluguel;
        } catch (DataIntegrityViolationException e) {
            throw new AtributoDuplicadoException("Já está vinculado a outro Aluguel: " + listaAux);
        }
    }

    @Override
    public List<AluguelResponseDTO> listarAlugueis() {
        return aluguelRepository.findAll().stream()
                .map(this::converteAluguelParaReponseCustomizada)
                .toList();
    }

    @Override
    public AluguelResponseDTO buscarAluguelPorId(Long id) {
        Aluguel aluguel = verificaSeAluguelComIdInformadoExiste(id);
        return converteAluguelParaReponseCustomizada(aluguel);
    }

    @Override
    public void atualizaAluguel(Long id) {
        Aluguel aluguel = verificaSeAluguelComIdInformadoExiste(id);
        Cliente cliente = aluguel.getCliente();
        Carro carro = aluguel.getCarro();

        cliente.setDisponivel(true);
        carro.setDisponivel(true);

        clienteRepository.save(cliente);
        carroRepository.save(carro);

        calculaMulta(aluguel);

        aluguel.setStatusAluguel(StatusAluguel.FINALIZADO);

        aluguelRepository.save(aluguel);
    }

    @Override
    public void excluirAluguel(Long id) {
        verificaSeAluguelComIdInformadoExiste(id);

        Cliente cliente = verificaSeAluguelComIdInformadoExiste(id).getCliente();
        Carro carro = verificaSeAluguelComIdInformadoExiste(id).getCarro();

        cliente.setDisponivel(true);
        carro.setDisponivel(true);

        clienteRepository.save(cliente);
        carroRepository.save(carro);

        aluguelRepository.deleteById(id);
    }

    private BigDecimal calculaMulta(Aluguel aluguel) {
        LocalDate dataAtual = LocalDate.now();
        long diasDeAtraso = DAYS.between(aluguel.getDataFim(), dataAtual);

        if (diasDeAtraso == 0) { return BigDecimal.ZERO; }

        BigDecimal valorDiarioDaMulta = aluguel.getValorTotal().divide(BigDecimal.valueOf(31), 2, RoundingMode.HALF_UP);
        return valorDiarioDaMulta.multiply(BigDecimal.valueOf(diasDeAtraso));
    }

    private List<String> verificacaoSeClienteECarroEstaoDisponiveis(String cpf, String placa) throws DataIntegrityViolationException {
        List<String> listaAux = new ArrayList<>();
        Cliente cliente = clienteRepository.findByCpf(cpf);
        Carro carro = carroRepository.findByPlaca(placa);

        if (!cliente.isDisponivel()) listaAux.add("Cliente");
        if (!carro.isDisponivel()) listaAux.add("Carro");

        return listaAux;
    }

    private void verificaSeClienteExiste(String nome, String cpf) {
        List<String> camposNaoEncontrados = new ArrayList<>();
        boolean nomeCliente = clienteRepository.existsByNome(nome);
        boolean cpfCliente = clienteRepository.existsByCpf(cpf);

        if (!nomeCliente) {
            camposNaoEncontrados.add("Nome");
        }
        if (!cpfCliente) {
            camposNaoEncontrados.add("CPF");
        }

        if (!camposNaoEncontrados.isEmpty()) {
            throw new RecursoNaoEncontradoException("Dados informados não encontrados: " + camposNaoEncontrados);
        }
    }

    private void verificaSeCarroExiste(String placa) {
        if (!carroRepository.existsByPlaca(placa)) {
            throw new RecursoNaoEncontradoException("Carro com placa: " + placa + " não encontrado");
        }
    }

    private BigDecimal calculaValorTotalAluguel(BigDecimal valorDiaria, int diasAluguel) {
        return valorDiaria.multiply(BigDecimal.valueOf(diasAluguel));
    }

    private Aluguel verificaSeAluguelComIdInformadoExiste(Long id) {
        //log.info("Verificando se o carro existe.");
        Optional<Aluguel> aluguelComIdInformado = aluguelRepository.findById(id);

        if (aluguelComIdInformado.isEmpty()) {
            //log.info("Carro com Id: {}", id + " não encontrado!");
            throw new RecursoNaoEncontradoException("Aluguel com Id: " + id + " não Encontrado!");
        }

        //log.info("Verificação se o carro existe concluída.");
        return aluguelComIdInformado.get();
    }

    private AluguelResponseDTO converteAluguelParaReponseCustomizada(Aluguel aluguel) {

        return AluguelResponseDTO.builder()
                .id(aluguel.getId())
                .nome(aluguel.getCliente().getNome())
                .cpf(aluguel.getCliente().getCpf())
                .numeroTelefone(aluguel.getCliente().getNumeroTelefone())
                .logradouro(aluguel.getCliente().getEndereco().getLogradouro())
                .bairro(aluguel.getCliente().getEndereco().getBairro())
                .localidade(aluguel.getCliente().getEndereco().getLocalidade())
                .uf(aluguel.getCliente().getEndereco().getUf())
                .marca(aluguel.getCarro().getMarca())
                .modelo(aluguel.getCarro().getModelo())
                .placa(aluguel.getCarro().getPlaca())
                .dataInicio(aluguel.getDataInicio())
                .dataFim(aluguel.getDataFim())
                .valorTotal(aluguel.getValorTotal())
                .statusAluguel(aluguel.getStatusAluguel())
                .build();
    }

}
